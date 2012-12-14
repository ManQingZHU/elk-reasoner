package org.semanticweb.elk.reasoner.saturation.rules;
/*
 * #%L
 * ELK Reasoner
 * $Id:$
 * $HeadURL:$
 * %%
 * Copyright (C) 2011 - 2012 Department of Computer Science, University of Oxford
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

import org.semanticweb.elk.reasoner.indexing.hierarchy.IndexedClass;
import org.semanticweb.elk.reasoner.indexing.hierarchy.IndexedDataHasValue;
import org.semanticweb.elk.reasoner.indexing.hierarchy.IndexedObjectIntersectionOf;
import org.semanticweb.elk.reasoner.indexing.hierarchy.IndexedObjectSomeValuesFrom;
import org.semanticweb.elk.reasoner.saturation.SaturationState.Writer;
import org.semanticweb.elk.reasoner.saturation.context.Context;

/**
 * 
 * @author Pavel Klinov
 *
 * pavel.klinov@uni-ulm.de
 */
public class CombinedDecompositionRuleApplicationVisitor implements
		DecompositionRuleApplicationVisitor {

	final private DecompositionRuleApplicationVisitor first_;
	final private DecompositionRuleApplicationVisitor second_;	
	
	public CombinedDecompositionRuleApplicationVisitor(DecompositionRuleApplicationVisitor first, DecompositionRuleApplicationVisitor second) {
		first_ = first;
		second_ = second;
	}
	
	@Override
	public void visit(IndexedClass ice, Writer writer, Context context) {
		first_.visit(ice, writer, context);
		second_.visit(ice, writer, context);
	}

	@Override
	public void visit(IndexedDataHasValue ice, Writer writer, Context context) {
		first_.visit(ice, writer, context);
		second_.visit(ice, writer, context);
	}

	@Override
	public void visit(IndexedObjectIntersectionOf ice, Writer writer,
			Context context) {
		first_.visit(ice, writer, context);
		second_.visit(ice, writer, context);
	}

	@Override
	public void visit(IndexedObjectSomeValuesFrom ice, Writer writer,
			Context context) {
		first_.visit(ice, writer, context);
		second_.visit(ice, writer, context);
	}

}