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
package org.semanticweb.elk.syntax.implementation;

import java.util.List;

import org.semanticweb.elk.syntax.ElkAssertionAxiomVisitor;
import org.semanticweb.elk.syntax.ElkAxiomVisitor;
import org.semanticweb.elk.syntax.ElkObjectVisitor;
import org.semanticweb.elk.syntax.interfaces.ElkIndividual;
import org.semanticweb.elk.syntax.interfaces.ElkSameIndividualAxiom;

/**
 * ELK implementation of ElkSameIndividualAxiom.
 * 
 * @author Markus Kroetzsch
 */
public class ElkSameIndividualAxiomImpl extends ElkIndividualListObject
		implements ElkSameIndividualAxiom {

	private static final int constructorHash_ = "ElkSameIndividualAxiom"
			.hashCode();

	/* package-private */ElkSameIndividualAxiomImpl(
			List<? extends ElkIndividual> individuals) {
		super(individuals);
		this.structuralHashCode = ElkObjectImpl.computeCompositeHash(
				constructorHash_, individuals);
	}

	@Override
	public String toString() {
		return buildFssString("SameIndividual");
	}

	public boolean structuralEquals(Object object) {
		if (this == object) {
			return true;
		} else if (object instanceof ElkSameIndividualAxiom) {
			return elkObjects.equals(((ElkSameIndividualAxiom) object)
					.getIndividuals());
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
