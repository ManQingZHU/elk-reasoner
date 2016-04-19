/**
 * 
 */
package org.semanticweb.elk.reasoner.tracing;

/*
 * #%L
 * ELK Reasoner
 * $Id:$
 * $HeadURL:$
 * %%
 * Copyright (C) 2011 - 2015 Department of Computer Science, University of Oxford
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

import static org.junit.Assume.assumeTrue;

import java.io.IOException;
import java.net.URISyntaxException;
import java.net.URL;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.semanticweb.elk.exceptions.ElkException;
import org.semanticweb.elk.loading.AxiomLoader;
import org.semanticweb.elk.loading.Owl2StreamLoader;
import org.semanticweb.elk.owl.interfaces.ElkClass;
import org.semanticweb.elk.owl.interfaces.ElkObject;
import org.semanticweb.elk.owl.managers.ElkObjectEntityRecyclingFactory;
import org.semanticweb.elk.owl.parsing.Owl2ParseException;
import org.semanticweb.elk.owl.parsing.javacc.Owl2FunctionalStyleParserFactory;
import org.semanticweb.elk.reasoner.Reasoner;
import org.semanticweb.elk.reasoner.TestReasonerUtils;
import org.semanticweb.elk.reasoner.saturation.conclusions.model.ClassConclusion;
import org.semanticweb.elk.reasoner.stages.PostProcessingStageExecutor;
import org.semanticweb.elk.testing.ConfigurationUtils;
import org.semanticweb.elk.testing.ConfigurationUtils.TestManifestCreator;
import org.semanticweb.elk.testing.PolySuite;
import org.semanticweb.elk.testing.PolySuite.Config;
import org.semanticweb.elk.testing.PolySuite.Configuration;
import org.semanticweb.elk.testing.TestInput;
import org.semanticweb.elk.testing.TestManifest;
import org.semanticweb.elk.testing.VoidTestOutput;
import org.semanticweb.elk.testing.io.URLTestIO;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Tests tracing and axiom binding for all atomic subsumption inferences in all
 * our standard test ontologies.
 * 
 * @author Pavel Klinov
 * 
 */
@RunWith(PolySuite.class)
public class TracingTest {

	final static String INPUT_DATA_LOCATION = "classification_test_input";
	protected static final Logger LOGGER_ = LoggerFactory	.getLogger(TracingTest.class);

	protected final TracingTestManifest manifest;

	public TracingTest(	TracingTestManifest testManifest) {
		manifest = testManifest;
	}

	@Before
	public void before() throws IOException, Owl2ParseException {
		assumeTrue(!ignore(manifest.getInput()));
	}

	@SuppressWarnings("static-method")
	protected boolean ignore(@SuppressWarnings("unused") TestInput input) {
		return false;
	}

	@Test
	public void tracingTest() throws Exception {
		AxiomLoader fileLoader = new Owl2StreamLoader(
				new Owl2FunctionalStyleParserFactory(),
				manifest.getInput().getInputStream());
		Reasoner reasoner = TestReasonerUtils.createTestReasoner(fileLoader,
				new PostProcessingStageExecutor());

		try {
			TracingTests tests = getTracingTests(reasoner);

			tests.accept(getTracingTestVisitor(reasoner));
		} finally {
			reasoner.shutdown();
		}
	}	
	
	private TracingTestVisitor getTracingTestVisitor(final Reasoner reasoner) {
		return new TracingTestVisitor() {
			
			private final ElkObject.Factory factory_ = new ElkObjectEntityRecyclingFactory();
			
			@Override
			public void subsumptionTest(ElkClass subsumee, ElkClass subsumer) {
//				ReasonerStateAccessor.cleanClassTraces(reasoner);
				
				try {
					LOGGER_.trace("Tracing test: {} => {}", subsumee, subsumer);
					
					ClassConclusion conclusion = reasoner.getConclusion(factory_.getSubClassOfAxiom(subsumee, subsumer));			
					reasoner.explainConclusion(conclusion);						
					TracingTestUtils.checkTracingCompleteness(conclusion, reasoner);

				} catch (ElkException e) {
					throw new RuntimeException(e);
				}
			}

			@Override
			public void inconsistencyTest() throws Exception {
//				ReasonerStateAccessor.cleanClassTraces(reasoner);
				
				try {
					LOGGER_.trace("Tracing test: inconsistency");
					
					reasoner.explainInconsistency();
						
					TracingTestUtils.checkTracingOfInconsistencyCompleteness(reasoner);
				} catch (ElkException e) {
					throw new RuntimeException(e);
				}
			}
		};
	}

	@SuppressWarnings("static-method")
	protected TracingTests getTracingTests(Reasoner reasoner) throws ElkException {
		return new ComprehensiveSubsumptionTracingTests(reasoner);
	}
	
	@Config
	public static Configuration getConfig() throws URISyntaxException,
			IOException {
		return ConfigurationUtils
				.loadFileBasedTestConfiguration(
						INPUT_DATA_LOCATION,
						TracingTest.class,
						"owl",
						new TestManifestCreator<URLTestIO, VoidTestOutput, VoidTestOutput>() {
							@Override
							public TestManifest<URLTestIO, VoidTestOutput, VoidTestOutput> create(
									URL input, URL output) throws IOException {
								// don't need an expected output for these tests
								return new TracingTestManifest(input);
							}
						});
	}
}