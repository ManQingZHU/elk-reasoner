/*
 * #%L
 * elk-reasoner
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
package org.semanticweb.elk.syntax.preprocessing;

import java.util.ArrayList;

import org.semanticweb.elk.owl.interfaces.ElkAsymmetricObjectPropertyAxiom;
import org.semanticweb.elk.owl.interfaces.ElkAxiom;
import org.semanticweb.elk.owl.interfaces.ElkClass;
import org.semanticweb.elk.owl.interfaces.ElkClassAssertionAxiom;
import org.semanticweb.elk.owl.interfaces.ElkClassExpression;
import org.semanticweb.elk.owl.interfaces.ElkDeclarationAxiom;
import org.semanticweb.elk.owl.interfaces.ElkDifferentIndividualsAxiom;
import org.semanticweb.elk.owl.interfaces.ElkDisjointClassesAxiom;
import org.semanticweb.elk.owl.interfaces.ElkDisjointObjectPropertiesAxiom;
import org.semanticweb.elk.owl.interfaces.ElkDisjointUnionAxiom;
import org.semanticweb.elk.owl.interfaces.ElkEntity;
import org.semanticweb.elk.owl.interfaces.ElkEquivalentClassesAxiom;
import org.semanticweb.elk.owl.interfaces.ElkEquivalentObjectPropertiesAxiom;
import org.semanticweb.elk.owl.interfaces.ElkFunctionalObjectPropertyAxiom;
import org.semanticweb.elk.owl.interfaces.ElkInverseFunctionalObjectPropertyAxiom;
import org.semanticweb.elk.owl.interfaces.ElkInverseObjectPropertiesAxiom;
import org.semanticweb.elk.owl.interfaces.ElkIrreflexiveObjectPropertyAxiom;
import org.semanticweb.elk.owl.interfaces.ElkNamedIndividual;
import org.semanticweb.elk.owl.interfaces.ElkNegativeObjectPropertyAssertionAxiom;
import org.semanticweb.elk.owl.interfaces.ElkObjectAllValuesFrom;
import org.semanticweb.elk.owl.interfaces.ElkObjectComplementOf;
import org.semanticweb.elk.owl.interfaces.ElkObjectExactCardinality;
import org.semanticweb.elk.owl.interfaces.ElkObjectFactory;
import org.semanticweb.elk.owl.interfaces.ElkObjectHasSelf;
import org.semanticweb.elk.owl.interfaces.ElkObjectHasValue;
import org.semanticweb.elk.owl.interfaces.ElkObjectIntersectionOf;
import org.semanticweb.elk.owl.interfaces.ElkObjectInverseOf;
import org.semanticweb.elk.owl.interfaces.ElkObjectMaxCardinality;
import org.semanticweb.elk.owl.interfaces.ElkObjectMinCardinality;
import org.semanticweb.elk.owl.interfaces.ElkObjectOneOf;
import org.semanticweb.elk.owl.interfaces.ElkObjectProperty;
import org.semanticweb.elk.owl.interfaces.ElkObjectPropertyAssertionAxiom;
import org.semanticweb.elk.owl.interfaces.ElkObjectPropertyChain;
import org.semanticweb.elk.owl.interfaces.ElkObjectPropertyDomainAxiom;
import org.semanticweb.elk.owl.interfaces.ElkObjectPropertyExpression;
import org.semanticweb.elk.owl.interfaces.ElkObjectPropertyRangeAxiom;
import org.semanticweb.elk.owl.interfaces.ElkObjectSomeValuesFrom;
import org.semanticweb.elk.owl.interfaces.ElkObjectUnionOf;
import org.semanticweb.elk.owl.interfaces.ElkReflexiveObjectPropertyAxiom;
import org.semanticweb.elk.owl.interfaces.ElkSameIndividualAxiom;
import org.semanticweb.elk.owl.interfaces.ElkSubClassOfAxiom;
import org.semanticweb.elk.owl.interfaces.ElkSubObjectPropertyExpression;
import org.semanticweb.elk.owl.interfaces.ElkSubObjectPropertyOfAxiom;
import org.semanticweb.elk.owl.interfaces.ElkSymmetricObjectPropertyAxiom;
import org.semanticweb.elk.owl.interfaces.ElkTransitiveObjectPropertyAxiom;
import org.semanticweb.elk.owl.visitors.ElkAxiomVisitor;
import org.semanticweb.elk.owl.visitors.ElkClassExpressionVisitor;
import org.semanticweb.elk.owl.visitors.ElkEntityVisitor;
import org.semanticweb.elk.owl.visitors.ElkObjectPropertyExpressionVisitor;
import org.semanticweb.elk.owl.visitors.ElkSubObjectPropertyExpressionVisitor;

/**
 * A visitor for creating variants of ElkAxiom, ElkClassExpression and
 * ElkObjectPropertyExpression objects.
 * 
 * @author Markus Kroetzsch
 */
public class RenamingExpressionVisitor implements ElkEntityVisitor<ElkEntity>,
		ElkClassExpressionVisitor<ElkClassExpression>,
		ElkObjectPropertyExpressionVisitor<ElkObjectPropertyExpression>,
		ElkAxiomVisitor<ElkAxiom> {

	class RenamingSubObjectPropertyExpressionVisitor
			implements
			ElkSubObjectPropertyExpressionVisitor<ElkSubObjectPropertyExpression> {

		public ElkSubObjectPropertyExpression visit(
				ElkObjectProperty elkObjectProperty) {
			// TODO Auto-generated method stub
			return null;
		}

		public ElkSubObjectPropertyExpression visit(
				ElkObjectInverseOf elkObjectInverseOf) {
			// TODO Auto-generated method stub
			return null;
		}

		public ElkSubObjectPropertyExpression visit(
				ElkObjectPropertyChain elkObjectPropertyChain) {
			// TODO Auto-generated method stub
			return null;
		}

	}

	protected RenamingSubObjectPropertyExpressionVisitor renamingSubObjectPropertyExpressionVisitor;

	protected String postfix;

	protected final ElkObjectFactory objectFactory;

	public RenamingExpressionVisitor(ElkObjectFactory objectFactory,
			String postfix) {
		this.postfix = postfix;
		this.objectFactory = objectFactory;
	}

	public void setPostfix(String postfix) {
		this.postfix = postfix;
		this.renamingSubObjectPropertyExpressionVisitor = new RenamingSubObjectPropertyExpressionVisitor();
	}

	public ElkEquivalentClassesAxiom visit(
			ElkEquivalentClassesAxiom elkEquivalentClassesAxiom) {
		ArrayList<ElkClassExpression> newClassExpressions = new ArrayList<ElkClassExpression>();
		for (ElkClassExpression classExpression : elkEquivalentClassesAxiom
				.getClassExpressions()) {
			newClassExpressions.add(classExpression.accept(this));
		}
		return objectFactory.getEquivalentClassesAxiom(newClassExpressions);
	}

	public ElkSubClassOfAxiom visit(ElkSubClassOfAxiom elkSubClassOfAxiom) {
		return objectFactory.getSubClassOfAxiom(elkSubClassOfAxiom
				.getSubClassExpression().accept(this), elkSubClassOfAxiom
				.getSuperClassExpression().accept(this));
	}

	public ElkClass visit(ElkClass classExpression) {
		return objectFactory.getClass(classExpression.getIri() + postfix);
	}

	public ElkObjectIntersectionOf visit(ElkObjectIntersectionOf classExpression) {
		ArrayList<ElkClassExpression> newSubClassExpressions = new ArrayList<ElkClassExpression>();
		for (ElkClassExpression subClassExpression : classExpression
				.getClassExpressions()) {
			newSubClassExpressions.add(subClassExpression.accept(this));
		}
		return objectFactory.getObjectIntersectionOf(newSubClassExpressions);
	}

	public ElkObjectSomeValuesFrom visit(ElkObjectSomeValuesFrom classExpression) {
		ElkClassExpression newClass = classExpression.getClassExpression()
				.accept(this);
		ElkObjectPropertyExpression newProperty = classExpression
				.getObjectPropertyExpression().accept(this);
		return objectFactory.getObjectSomeValuesFrom(newProperty, newClass);
	}

	public ElkObjectProperty visit(ElkObjectProperty elkObjectProperty) {
		return objectFactory.getObjectProperty(elkObjectProperty.getIri()
				+ postfix);
	}

	public ElkObjectPropertyExpression visit(
			ElkObjectInverseOf elkObjectInverseOf) {
		// TODO Auto-generated method stub
		return null;
	}

	public ElkAxiom visit(
			ElkFunctionalObjectPropertyAxiom elkFunctionalObjectPropertyAxiom) {
		// TODO Auto-generated method stub
		return null;
	}

	public ElkAxiom visit(
			ElkInverseFunctionalObjectPropertyAxiom elkInverseFunctionalObjectPropertyAxiom) {
		// TODO Auto-generated method stub
		return null;
	}

	public ElkAxiom visit(
			ElkInverseObjectPropertiesAxiom elkInverseObjectPropertiesAxiom) {
		// TODO Auto-generated method stub
		return null;
	}

	public ElkAxiom visit(
			ElkSubObjectPropertyOfAxiom elkSubObjectPropertyOfAxiom) {
		return objectFactory
				.getSubObjectPropertyOfAxiom(
						elkSubObjectPropertyOfAxiom
								.getSubObjectPropertyExpression()
								.accept(this.renamingSubObjectPropertyExpressionVisitor),
						elkSubObjectPropertyOfAxiom
								.getSuperObjectPropertyExpression()
								.accept(this));
	}

	public ElkAxiom visit(
			ElkTransitiveObjectPropertyAxiom elkTransitiveObjectPropertyAxiom) {
		return objectFactory
				.getTransitiveObjectPropertyAxiom(elkTransitiveObjectPropertyAxiom
						.getObjectPropertyExpression().accept(this));
	}

	public ElkDeclarationAxiom visit(ElkDeclarationAxiom elkDeclarationAxiom) {
		return objectFactory.getDeclarationAxiom(elkDeclarationAxiom
				.getEntity().accept(this));
	}

	public ElkClassExpression visit(ElkObjectHasValue elkObjectHasValue) {
		// TODO Auto-generated method stub
		return null;
	}

	public ElkEntity visit(ElkNamedIndividual elkNamedIndividual) {
		// TODO Auto-generated method stub
		return null;
	}

	public ElkClassExpression visit(ElkObjectOneOf elkObjectOneOf) {
		// TODO Auto-generated method stub
		return null;
	}

	public ElkClassExpression visit(ElkObjectHasSelf elkObjectHasSelf) {
		// TODO Auto-generated method stub
		return null;
	}

	public ElkSubObjectPropertyExpression visit(
			ElkObjectPropertyChain elkObjectPropertyChain) {
		// TODO Auto-generated method stub
		return null;
	}

	public ElkAxiom visit(ElkDisjointClassesAxiom elkDisjointClasses) {
		// TODO Auto-generated method stub
		return null;
	}

	public ElkAxiom visit(ElkDisjointUnionAxiom elkDisjointUnionAxiom) {
		// TODO Auto-generated method stub
		return null;
	}

	public ElkAxiom visit(
			ElkEquivalentObjectPropertiesAxiom elkEquivalentObjectProperties) {
		// TODO Auto-generated method stub
		return null;
	}

	public ElkAxiom visit(
			ElkDisjointObjectPropertiesAxiom elkDisjointObjectPropertiesAxiom) {
		// TODO Auto-generated method stub
		return null;
	}

	public ElkAxiom visit(
			ElkReflexiveObjectPropertyAxiom elkReflexiveObjectPropertyAxiom) {
		// TODO Auto-generated method stub
		return null;
	}

	public ElkAxiom visit(
			ElkAsymmetricObjectPropertyAxiom elkAsymmetricObjectPropertyAxiom) {
		// TODO Auto-generated method stub
		return null;
	}

	public ElkAxiom visit(
			ElkIrreflexiveObjectPropertyAxiom elkIrreflexiveObjectPropertyAxiom) {
		// TODO Auto-generated method stub
		return null;
	}

	public ElkAxiom visit(
			ElkObjectPropertyDomainAxiom elkObjectPropertyDomainAxiom) {
		// TODO Auto-generated method stub
		return null;
	}

	public ElkAxiom visit(
			ElkObjectPropertyRangeAxiom elkObjectPropertyRangeAxiom) {
		// TODO Auto-generated method stub
		return null;
	}

	public ElkAxiom visit(
			ElkSymmetricObjectPropertyAxiom elkSymmetricObjectPropertyAxiom) {
		// TODO Auto-generated method stub
		return null;
	}

	public ElkClassExpression visit(
			ElkObjectAllValuesFrom elkObjectAllValuesFrom) {
		// TODO Auto-generated method stub
		return null;
	}

	public ElkClassExpression visit(
			ElkObjectExactCardinality elkObjectExactCardinality) {
		// TODO Auto-generated method stub
		return null;
	}

	public ElkClassExpression visit(
			ElkObjectMaxCardinality elkObjectMaxCardinality) {
		// TODO Auto-generated method stub
		return null;
	}

	public ElkClassExpression visit(
			ElkObjectMinCardinality elkObjectMaxCardinality) {
		// TODO Auto-generated method stub
		return null;
	}

	public ElkClassExpression visit(ElkObjectUnionOf elkObjectUnionOf) {
		// TODO Auto-generated method stub
		return null;
	}

	public ElkAxiom visit(ElkClassAssertionAxiom elkClassAssertionAxiom) {
		// TODO Auto-generated method stub
		return null;
	}

	public ElkAxiom visit(
			ElkDifferentIndividualsAxiom elkDifferentIndividualsAxiom) {
		// TODO Auto-generated method stub
		return null;
	}

	public ElkAxiom visit(
			ElkNegativeObjectPropertyAssertionAxiom elkNegativeObjectPropertyAssertion) {
		// TODO Auto-generated method stub
		return null;
	}

	public ElkAxiom visit(
			ElkObjectPropertyAssertionAxiom elkObjectPropertyAssertionAxiom) {
		// TODO Auto-generated method stub
		return null;
	}

	public ElkAxiom visit(ElkSameIndividualAxiom elkSameIndividualAxiom) {
		// TODO Auto-generated method stub
		return null;
	}

	public ElkClassExpression visit(ElkObjectComplementOf elkObjectComplementOf) {
		// TODO Auto-generated method stub
		return null;
	}

}
