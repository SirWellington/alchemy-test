/*
 * Copyright © 2019. Sir Wellington.
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 *
 * You may obtain a copy of the License at
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package tech.sirwellington.alchemy.test.junit.generation;

import org.junit.jupiter.api.*;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.mockito.junit.jupiter.MockitoExtension;
import tech.sirwellington.alchemy.generator.AlchemyGenerator;
import tech.sirwellington.alchemy.test.junit.AlchemyTest;

import java.net.URL;
import java.util.Date;
import java.util.List;
import java.util.concurrent.TimeUnit;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.*;
import static org.mockito.Mockito.when;

/**
 * @author SirWellington
 */
public class AlchemyTestRunnerTest {
    @AlchemyTest
    public static class MockTest {

        @GenerateString
        private String string;

        @GenerateInteger(GenerateInteger.Type.POSITIVE)
        private int integer;

        @GenerateDate(GenerateDate.Type.FUTURE)
        private Date futureDate;

        @GeneratePojo
        private SamplePojo pojo;

        @GenerateEnum
        private TimeUnit timeUnit;

        @GenerateList(Date.class)
        private List<Date> dates;

        @GenerateURL
        private URL url;

        @Mock
        private AlchemyGenerator<?> object;

        @Test
        void testGenenerators() {
            MockitoAnnotations.openMocks(this);
            assertThat(object, notNullValue());
            assertThat(string, not(isEmptyOrNullString()));
            assertThat(integer, greaterThan(0));
            assertThat(futureDate, notNullValue());
            assertThat(futureDate.after(new Date()), is(true));
            assertThat(pojo, notNullValue());
            assertThat(pojo.name, not(isEmptyOrNullString()));
            assertThat(pojo.number, greaterThan(0));
            assertThat(timeUnit, notNullValue());
            assertThat(dates, notNullValue());
            assertThat(dates, not(empty()));
            assertThat(url, notNullValue());

            when(object.get()).thenReturn(null);
            assertThat(object.get(), nullValue());
        }

    }

    @AlchemyTest
    public static class BadTest {

        private static int firstTotalRuns = 0;
        private static int secondTotalRuns = 0;

        @BeforeAll
        public static void whenBegin() {
            firstTotalRuns = 0;
            secondTotalRuns = 0;
        }

        @BeforeEach
        public void setup() {
        }

        @Test
        public void runFirstTest() {
            ++firstTotalRuns;
        }

        @Test
        public void runSecondTest() {
            ++secondTotalRuns;
        }

        @AfterAll
        public static void whenDone() {
            assertThat(firstTotalRuns, is(1));
            assertThat(secondTotalRuns, is(1));
        }
    }

    @AlchemyTest
    @ExtendWith(MockitoExtension.class)
    public static class RegularTest {

        private static int totalRuns = 0;

        @Mock
        private Object object;

        @Test
        public void testRun() {
            assertThat(object, notNullValue());
            ++totalRuns;
        }

        @AfterAll
        public static void whenDone() {
            assertThat(totalRuns, is(1));
        }
    }

    private static class SamplePojo {
        private String name;
        private int number;
    }

}
