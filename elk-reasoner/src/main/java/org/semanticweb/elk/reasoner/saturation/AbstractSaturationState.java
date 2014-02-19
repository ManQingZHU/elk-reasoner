package org.semanticweb.elk.reasoner.saturation;

/*
 * #%L
 * ELK Reasoner
 * $Id:$
 * $HeadURL:$
 * %%
 * Copyright (C) 2011 - 2014 Department of Computer Science, University of Oxford
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

import java.util.AbstractCollection;
import java.util.Collection;
import java.util.Collections;
import java.util.Iterator;
import java.util.Queue;
import java.util.concurrent.ConcurrentLinkedQueue;
import java.util.concurrent.atomic.AtomicInteger;

import org.semanticweb.elk.reasoner.indexing.OntologyIndex;
import org.semanticweb.elk.reasoner.indexing.hierarchy.IndexedClassExpression;
import org.semanticweb.elk.reasoner.saturation.conclusions.Conclusion;
import org.semanticweb.elk.reasoner.saturation.context.Context;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * A skeleton to simplify implementation of {@link SaturationState}s
 * 
 * @author "Yevgeny Kazakov"
 * 
 */
public abstract class AbstractSaturationState implements SaturationState {

	// logger for events
	private static final Logger LOGGER_ = LoggerFactory
			.getLogger(AbstractSaturationState.class);

	final OntologyIndex ontologyIndex;

	/**
	 * The queue containing all activated contexts (whose todo queue is not
	 * empty). Every activated context occurs exactly once.
	 */
	private final Queue<Context> activeContexts_ = new ConcurrentLinkedQueue<Context>();

	/**
	 * The queue containing all {@link Context}s of this {@link SaturationState}
	 * that are not saturated, i.e., for which {@link Context#isSaturated()}
	 * returns {@code false}. Each non-saturated context occurs exactly once.
	 */
	private final Queue<ExtendedContext> notSaturatedContexts_ = new ConcurrentLinkedQueue<ExtendedContext>();

	private final AtomicInteger notSaturatedContextsCount_ = new AtomicInteger(
			0);

	public AbstractSaturationState(OntologyIndex index) {
		this.ontologyIndex = index;
	}

	@Override
	public OntologyIndex getOntologyIndex() {
		return ontologyIndex;
	}

	@Override
	public Collection<? extends Context> getNotSaturatedContexts() {
		return Collections
				.unmodifiableCollection(new AbstractCollection<ExtendedContext>() {
					@Override
					public Iterator<ExtendedContext> iterator() {
						return notSaturatedContexts_.iterator();
					}

					@Override
					public int size() {
						return notSaturatedContextsCount_.get();
					}
				});
	}

	@Override
	public int getNonSaturatedContextCount() {
		return notSaturatedContextsCount_.get();
	}

	@Override
	public Context setNextContextSaturated() {
		ExtendedContext next = notSaturatedContexts_.poll();
		if (next == null)
			return null;
		// else
		notSaturatedContextsCount_.decrementAndGet();
		next.setSaturated(true);
		LOGGER_.trace("{}: marked as saturated", next);
		return next;
	}

	@Override
	public SaturationStateWriter getContextCreatingWriter(
			ContextCreationListener contextCreationListener,
			ContextModificationListener contextModificationListener) {
		return new ContextCreatingWriter(contextCreationListener,
				contextModificationListener);
	}

	@Override
	public SaturationStateWriter getContextModifyingWriter(
			ContextModificationListener contextModificationListener) {
		return new ContextModifyingWriter(contextModificationListener);
	}

	@Override
	abstract public ExtendedContext getContext(IndexedClassExpression ice);

	abstract void resetContexts();

	/**
	 * Adds the given {@link ExtendedContext} to this {@link SaturationState} if
	 * no {@link ExtendedContext} was assigned for its root
	 * 
	 * @param context
	 *            the {@link ExtendedContext} to be added to this
	 *            {@link SaturationState}
	 * @return the {@link ExtendedContext} in this {@link SaturationState}
	 *         assigned to the same root, or {@link null} if no such
	 *         {@link Context} existed before this method is called
	 * 
	 * @see ExtendedContext#getRoot()
	 */
	abstract ExtendedContext setIfAbsent(ExtendedContext context);

	/**
	 * 
	 * 
	 */
	class ContextModifyingWriter implements SaturationStateWriter {

		private final ContextModificationListener contextModificationListener_;

		private ContextModifyingWriter(
				ContextModificationListener contextSaturationListener) {
			this.contextModificationListener_ = contextSaturationListener;
		}

		@Override
		public Context pollForActiveContext() {
			return activeContexts_.poll();
		}

		void produce(Context context, Conclusion conclusion) {
			LOGGER_.trace("{}: produced conclusion {}", context, conclusion);
			if (context.addToDo(conclusion)) {
				LOGGER_.trace("{}: activated", context);
				// context was activated
				activeContexts_.add(context);
			}
		}

		@Override
		public void produce(IndexedClassExpression root, Conclusion conclusion) {
			// TODO: what if NPE?
			produce(getContext(root), conclusion);
		}

		void markAsNotSaturatedInternal(ExtendedContext context) {
			LOGGER_.trace("{}: marked as non-saturated", context);
			notSaturatedContexts_.add(context);
			notSaturatedContextsCount_.incrementAndGet();
			contextModificationListener_.notifyContextModification(context);
		}

		@Override
		public boolean markAsNotSaturated(IndexedClassExpression root) {
			ExtendedContext context = getContext(root);
			if (context == null)
				return false;
			// else
			if (context.setSaturated(false)) {
				markAsNotSaturatedInternal(context);
				return true;
			}
			// else
			return false;
		}

		@Override
		public void resetContexts() {
			AbstractSaturationState.this.resetContexts();
		}

		@Override
		public SaturationState getSaturationState() {
			return AbstractSaturationState.this;
		}

	}

	/**
	 * @author Pavel Klinov
	 * 
	 *         pavel.klinov@uni-ulm.de
	 * @author "Yevgeny Kazakov"
	 */
	protected class ContextCreatingWriter extends ContextModifyingWriter
			implements SaturationStateWriter {

		private final ContextCreationListener contextCreationListener_;

		protected ContextCreatingWriter(
				ContextCreationListener contextCreationListener,
				ContextModificationListener contextModificationListener) {
			super(contextModificationListener);

			this.contextCreationListener_ = contextCreationListener;
		}

		protected ContextCreatingWriter() {
			super(ContextModificationListener.DUMMY);
			this.contextCreationListener_ = ContextCreationListener.DUMMY;
		}

		@Override
		public void produce(IndexedClassExpression root, Conclusion conclusion) {
			produce(getCreateContext(root), conclusion);
		}

		public ExtendedContext getCreateContext(IndexedClassExpression root) {
			ExtendedContext previous = getContext(root);
			if (previous != null)
				return previous;
			// else try to assign a new context
			ContextImpl newContext = new ContextImpl(root);
			previous = setIfAbsent(newContext);
			if (previous != null)
				// the context is already assigned meanwhile
				return previous;
			// else the context is new and not saturated
			markAsNotSaturatedInternal(newContext);
			contextCreationListener_.notifyContextCreation(newContext);
			LOGGER_.trace("{}: context created", newContext);
			return newContext;
		}

	}

}