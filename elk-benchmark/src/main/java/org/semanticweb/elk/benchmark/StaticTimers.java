/*
 * #%L
 * ELK Bencharking Package
 * 
 * $Id$
 * $HeadURL$
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
package org.semanticweb.elk.benchmark;

/**
 * 
 * @author Pavel Klinov
 *
 * pavel.klinov@uni-ulm.de
 *
 */
public class StaticTimers {

	private static final Timers sTimers = new Timers();
	
	public static void start(String name) {
		sTimers.start(name);
	}
	
	public static void stop(String name) {
		sTimers.stop(name);
	}
	
	public static void restart(String name) {
		sTimers.restart(name);
	}
	
	public static void stopAll() {
		sTimers.stopAll();
	}
	
	public static void printAll() {
		sTimers.printAll(System.out);
	}
}