/*
 * #%L
 * ELK Reasoner
 * 
 * $Id$
 * $HeadURL$
 * %%
 * Copyright (C) 2011 Department of Computer Science, University of Oxford
 * %%
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 * 
 *      http://www.apache.org/licenses/LICENSE-2.0
 * 
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 * #L%
 */
package org.semanticweb.elk.alc.indexing.hierarchy;

import org.semanticweb.elk.owl.AbstractElkAxiomVisitor;
import org.semanticweb.elk.owl.implementation.ElkObjectFactoryImpl;
import org.semanticweb.elk.owl.interfaces.ElkAnnotationProperty;
import org.semanticweb.elk.owl.interfaces.ElkAxiom;
import org.semanticweb.elk.owl.interfaces.ElkClass;
import org.semanticweb.elk.owl.interfaces.ElkClassExpression;
import org.semanticweb.elk.owl.interfaces.ElkDataProperty;
import org.semanticweb.elk.owl.interfaces.ElkDatatype;
import org.semanticweb.elk.owl.interfaces.ElkDeclarationAxiom;
import org.semanticweb.elk.owl.interfaces.ElkEquivalentClassesAxiom;
import org.semanticweb.elk.owl.interfaces.ElkNamedIndividual;
import org.semanticweb.elk.owl.interfaces.ElkObjectFactory;
import org.semanticweb.elk.owl.interfaces.ElkObjectProperty;
import org.semanticweb.elk.owl.interfaces.ElkObjectPropertyDomainAxiom;
import org.semanticweb.elk.owl.interfaces.ElkSubClassOfAxiom;
import org.semanticweb.elk.owl.predefined.PredefinedElkClass;
import org.semanticweb.elk.owl.visitors.ElkEntityVisitor;

/**
 * An abstract class for indexing axioms. Its purpose is to reduce many
 * syntactically different forms of OWL axioms to a small number of canonical
 * axiom forms. Concrete instances of this class then only need to implement
 * indexing of the canonical axioms.
 * 
 * @author Frantisek Simancik
 * 
 */
public abstract class AbstractElkAxiomIndexerVisitor extends
		AbstractElkAxiomVisitor<Void> implements ElkAxiomIndexer {

	/**
	 * Object factory that is used internally to replace some syntactic
	 * constructs with other logically equivalent constructs. ElkObjects created
	 * in this class are only used for this purpose (temporarily), hence we can
	 * use any factory implementation here.
	 */
	private final ElkObjectFactory objectFactory = new ElkObjectFactoryImpl();

	@Override
	protected Void defaultLogicalVisit(ElkAxiom axiom) {
		throw new ElkIndexingUnsupportedException(axiom);
	}

	@Override
	public Void visit(ElkObjectPropertyDomainAxiom axiom) {
		indexSubClassOfAxiom(objectFactory.getObjectSomeValuesFrom(
				axiom.getProperty(), PredefinedElkClass.OWL_THING),
				axiom.getDomain());
		return null;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * org.semanticweb.elk.owl.visitors.ElkClassAxiomVisitor#visit(org.semanticweb
	 * .elk.owl.interfaces.ElkEquivalentClassesAxiom)
	 * 
	 * Reduces equivalent classes to subclass axioms.
	 */
	@Override
	public Void visit(ElkEquivalentClassesAxiom axiom) {
		ElkClassExpression first = null;
		for (ElkClassExpression c : axiom.getClassExpressions()) {
			if (first == null)
				first = c;
			else {
				indexSubClassOfAxiom(first, c);
				indexSubClassOfAxiom(c, first);
			}
		}
		return null;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * org.semanticweb.elk.owl.visitors.ElkClassAxiomVisitor#visit(org.semanticweb
	 * .elk.owl.interfaces.ElkSubClassOfAxiom)
	 * 
	 * Subclass axioms are supported directly.
	 */
	@Override
	public Void visit(ElkSubClassOfAxiom axiom) {
		indexSubClassOfAxiom(axiom.getSubClassExpression(),
				axiom.getSuperClassExpression());
		return null;
	}

	/**
	 * Declares the corresponding entity
	 */
	@Override
	public Void visit(ElkDeclarationAxiom axiom) {
		return axiom.getEntity().accept(entityDeclarator);
	}

	/**
	 * Entity visitor for calling the appropriate type of declarations.
	 */
	private final ElkEntityVisitor<Void> entityDeclarator = new ElkEntityVisitor<Void>() {

		@Override
		public Void visit(ElkClass elkClass) {
			indexClassDeclaration(elkClass);
			return null;
		}

		/**
		 * Nothing is done, datatypes are supported only syntactically. Warning
		 * is logged when indexing ElkDataHasValue.
		 */
		@Override
		public Void visit(ElkDatatype elkDatatype) {
			return null;
		}

		@Override
		public Void visit(ElkObjectProperty elkObjectProperty) {
			indexObjectPropertyDeclaration(elkObjectProperty);
			return null;
		}

		/**
		 * Nothing is done, datatypes are supported only syntactically. Warning
		 * is logged when indexing ElkDataHasValue.
		 */
		@Override
		public Void visit(ElkDataProperty elkDataProperty) {
			return null;
		}

		@Override
		public Void visit(ElkNamedIndividual elkNamedIndividual) {
			return null;
		}

		/**
		 * Nothing is done, annotations are ignored during indexing.
		 */
		@Override
		public Void visit(ElkAnnotationProperty elkAnnotationProperty) {
			return null;
		}
	};
}