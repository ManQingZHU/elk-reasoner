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
package org.semanticweb.elk.owl.implementation;

import org.semanticweb.elk.owl.interfaces.ElkIndividual;
import org.semanticweb.elk.owl.interfaces.ElkNegativeObjectPropertyAssertionAxiom;
import org.semanticweb.elk.owl.interfaces.ElkObjectPropertyExpression;
import org.semanticweb.elk.owl.visitors.ElkAssertionAxiomVisitor;
import org.semanticweb.elk.owl.visitors.ElkAxiomVisitor;
import org.semanticweb.elk.owl.visitors.ElkObjectVisitor;
import org.semanticweb.elk.util.hashing.HashGenerator;

/**
 * ELK implementation of ElkNegativeObjectPropertyAssertion.
 * 
 * @author Markus Kroetzsch
 * 
 */
public class ElkNegativeObjectPropertyAssertionAxiomImpl extends
		ElkObjectPropertyExpressionObject implements
		ElkNegativeObjectPropertyAssertionAxiom {

	protected final ElkIndividual firstIndividual;
	protected final ElkIndividual secondIndividual;

	private static final int constructorHash_ = "ElkNegativeObjectPropertyAssertionAxiom"
			.hashCode();

	/* package-private */ElkNegativeObjectPropertyAssertionAxiomImpl(
			ElkObjectPropertyExpression objectPropertyExpression,
			ElkIndividual firstIndividual, ElkIndividual secondIndividual) {
		super(objectPropertyExpression);
		this.firstIndividual = firstIndividual;
		this.secondIndividual = secondIndividual;
		this.structuralHashCode = HashGenerator.combineListHash(
				constructorHash_,
				objectPropertyExpression.structuralHashCode(),
				firstIndividual.structuralHashCode(),
				secondIndividual.structuralHashCode());
	}

	public ElkIndividual getFirstIndividual() {
		return firstIndividual;
	}

	public ElkIndividual getSecondIndividual() {
		return secondIndividual;
	}

	@Override
	public String toString() {
		StringBuilder result = new StringBuilder(
				"NegativeObjectPropertyAssertion(");
		result.append(objectPropertyExpression.toString());
		result.append(" ");
		result.append(firstIndividual.toString());
		result.append(" ");
		result.append(secondIndividual.toString());
		result.append(")");
		return result.toString();
	}

	public boolean structuralEquals(Object object) {
		if (this == object) {
			return true;
		} else if (object instanceof ElkNegativeObjectPropertyAssertionAxiom) {
			return objectPropertyExpression
					.equals(((ElkNegativeObjectPropertyAssertionAxiom) object)
							.getObjectPropertyExpression())
					&& firstIndividual
							.equals(((ElkNegativeObjectPropertyAssertionAxiom) object)
									.getFirstIndividual())
					&& secondIndividual
							.equals(((ElkNegativeObjectPropertyAssertionAxiom) object)
									.getSecondIndividual());
		} else {
			return false;
		}
	}

	public <O> O accept(ElkAssertionAxiomVisitor<O> visitor) {
		return visitor.visit(this);
	}

	public <O> O accept(ElkAxiomVisitor<O> visitor) {
		return visitor.visit(this);
	}

	public <O> O accept(ElkObjectVisitor<O> visitor) {
		return visitor.visit(this);
	}

}
