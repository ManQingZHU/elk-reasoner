package org.semanticweb.elk.matching.conclusions;

import org.semanticweb.elk.matching.root.IndexedContextRootMatch;
import org.semanticweb.elk.owl.interfaces.ElkObjectProperty;
import org.semanticweb.elk.reasoner.saturation.conclusions.model.BackwardLink;

/*
 * #%L
 * ELK Proofs Package
 * $Id:$
 * $HeadURL:$
 * %%
 * Copyright (C) 2011 - 2016 Department of Computer Science, University of Oxford
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

public class BackwardLinkMatch2
		extends AbstractClassConclusionMatch<BackwardLinkMatch1> {

	private final ElkObjectProperty relationMatch_;

	private final IndexedContextRootMatch destinationMatch_;

	BackwardLinkMatch2(BackwardLinkMatch1 parent,
			ElkObjectProperty relationMatch,
			IndexedContextRootMatch destinationMatch) {
		super(parent);
		this.relationMatch_ = relationMatch;
		this.destinationMatch_ = destinationMatch;
	}

	/**
	 * @return a match for {@link BackwardLink#getRelation()}
	 */
	public ElkObjectProperty getRelationMatch() {
		return relationMatch_;
	}

	public IndexedContextRootMatch getDestinationMatch() {
		return destinationMatch_;
	}

	@Override
	public <O> O accept(ClassConclusionMatch.Visitor<O> visitor) {
		return visitor.visit(this);
	}

	/**
	 * A factory for creating instances
	 * 
	 * @author Yevgeny Kazakov
	 *
	 */
	public interface Factory {

		BackwardLinkMatch2 getBackwardLinkMatch2(BackwardLinkMatch1 parent,
				ElkObjectProperty relationMatch,
				IndexedContextRootMatch destinationMatch);

	}

	/**
	 * The visitor pattern for instances
	 * 
	 * @author Yevgeny Kazakov
	 *
	 * @param <O>
	 *            the type of the output
	 */
	interface Visitor<O> {

		O visit(BackwardLinkMatch2 conclusionMatch);

	}

}
