/*
 * Copyright 2015 SirWellington Tech.
 *
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
 */
package tech.sirwellington.alchemy.test.junit.runners;

import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;
import org.junit.runner.JUnitCore;
import org.junit.runner.Result;
import org.junit.runner.RunWith;
import org.junit.runners.model.InitializationError;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;
import tech.sirwellington.alchemy.generator.AlchemyGenerator;

import static org.hamcrest.Matchers.is;
import static org.hamcrest.Matchers.notNullValue;
import static org.hamcrest.Matchers.nullValue;
import static org.junit.Assert.assertThat;
import static org.junit.Assert.assertTrue;
import static org.mockito.Mockito.when;

/**
 *
 * @author SirWellington
 */
@RunWith(MockitoJUnitRunner.class)
public class AlchemyTestRunnerTest
{

    private static final int RUNS = 99;

    @Before
    public void setUp() throws InitializationError
    {
    }

    @Test
    public void testRun()
    {
        //Run an inner test
        Result result;

        result = JUnitCore.runClasses(MockTestClass.class);
        assertTrue("Test runs failed: " + result.getFailures(),
                   result.wasSuccessful());

        result = JUnitCore.runClasses(BadTest.class);
        assertTrue("Test runs failed: " + result.getFailures(),
                   result.wasSuccessful());

        result = JUnitCore.runClasses(RegularTest.class);
        assertTrue("Test runs failed: " + result.getFailures(),
                   result.wasSuccessful());

    }

    @RunWith(AlchemyTestRunner.class)
    @Repeat(RUNS)
    public static class MockTestClass
    {

        @Mock
        private AlchemyGenerator<?> object;

        private static int firstTotalRuns = 0;
        private static int secondTotalRuns = 0;
        private static int thirdTotalRuns = 0;

        @BeforeClass
        public static void whenBegin()
        {
            firstTotalRuns = 0;
            secondTotalRuns = 0;
            thirdTotalRuns = 0;
        }

        @Before
        public void setup()
        {
            assertThat(object, notNullValue());

            when(object.get()).thenReturn(null);
            assertThat(object.get(), nullValue());
        }

        @Test
        public void runFirstTest()
        {
            ++firstTotalRuns;
        }

        @Repeat(5)
        @Test
        public void runSecondTest()
        {
            ++secondTotalRuns;
        }

        @DontRepeat
        @Test
        public void runThirdTest()
        {
            ++thirdTotalRuns;
        }

        @AfterClass
        public static void whenDone()
        {
            assertThat(firstTotalRuns, is(RUNS));
            assertThat(secondTotalRuns, is(5));
            assertThat(thirdTotalRuns, is(1));
        }
    }

    @RunWith(AlchemyTestRunner.class)
    @Repeat(-10)
    public static class BadTest
    {

        private static int firstTotalRuns = 0;
        private static int secondTotalRuns = 0;

        @BeforeClass
        public static void whenBegin()
        {
            firstTotalRuns = 0;
            secondTotalRuns = 0;
        }

        @Before
        public void setup()
        {
        }

        @Test
        public void runFirstTest()
        {
            ++firstTotalRuns;
        }

        @Repeat(-5)
        @Test
        public void runSecondTest()
        {
            ++secondTotalRuns;
        }

        @AfterClass
        public static void whenDone()
        {
            assertThat(firstTotalRuns, is(1));
            assertThat(secondTotalRuns, is(1));
        }
    }

    @RunWith(AlchemyTestRunner.class)
    public static class RegularTest
    {

        private static int totalRuns = 0;

        @Test
        public void testRun()
        {
            ++totalRuns;
        }

        @AfterClass
        public static void whenDone()
        {
            assertThat(totalRuns, is(1));
        }
    }

}
