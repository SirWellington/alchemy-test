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

import java.time.Instant;
import java.util.Date;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.model.TestClass;
import org.mockito.runners.MockitoJUnitRunner;

import static org.hamcrest.Matchers.greaterThan;
import static org.hamcrest.Matchers.greaterThanOrEqualTo;
import static org.hamcrest.Matchers.is;
import static org.hamcrest.Matchers.isEmptyOrNullString;
import static org.hamcrest.Matchers.lessThan;
import static org.hamcrest.Matchers.not;
import static org.hamcrest.Matchers.notNullValue;
import static org.junit.Assert.assertThat;
import static tech.sirwellington.alchemy.test.junit.runners.GenerateInteger.Type.RANGE;

/**
 *
 * @author SirWellington
 */
@RunWith(MockitoJUnitRunner.class)
public class AlchemyGeneratorAnnotationsTest
{

    @Before
    public void setUp()
    {
    }

    @Test
    public void testPopulateGeneratedFields() throws Exception
    {
        TestClass testClass = new TestClass(FakeTestClass.class);
        FakeTestClass instance = new FakeTestClass();
 
        AlchemyGeneratorAnnotations.populateGeneratedFields(testClass, instance);
        instance.setUp();
    }

    @Test
    public void testInflateString() throws Exception
    {
    }

    private static class FakeTestClass
    {

        private static final int STRING_LENGTH = 346;
        
        private static final int MIN_INT = 34;
        private static final int MAX_INT = 2021145;
        
        private static final long START_TIME = 20431;
        private static final long END_TIME = START_TIME + 25442545;

        @GenerateString(length = STRING_LENGTH)
        private String string;
        
        @GenerateInteger(value = RANGE, min = MIN_INT, max = MAX_INT)
        private int integer;
        
        @GenerateDate(value = GenerateDate.Type.RANGE, startDate = START_TIME, endDate = END_TIME)
        private Date date;
        
        @GenerateDate(GenerateDate.Type.PAST)
        private Date pastDate;
        
        @GenerateInstant(value = GenerateInstant.Type.RANGE, startTime = START_TIME, endTime = END_TIME)
        private Instant instant;
        
        @GeneratePojo
        private SamplePojo pojo;
        

        @Before
        public void setUp()
        {
            assertThat(string, not(isEmptyOrNullString()));
            assertThat(string.length(), is(STRING_LENGTH));
            
            assertThat(integer, greaterThanOrEqualTo(MIN_INT));
            assertThat(integer, lessThan(MAX_INT));
            
            assertThat(date, notNullValue());
            assertThat(date.getTime(), greaterThanOrEqualTo(START_TIME));
            assertThat(date.getTime(), lessThan(END_TIME));
            
            assertThat(instant, notNullValue());
            assertThat(instant.toEpochMilli(), greaterThanOrEqualTo(START_TIME));
            assertThat(instant.toEpochMilli(), lessThan(END_TIME));
            
            Date now = new Date();
            assertThat(pastDate.before(now), is(true));
            
            checkPojo(pojo);
            
        }

        private void checkPojo(SamplePojo pojo)
        {
            assertThat(pojo, notNullValue());
            assertThat(pojo.name, not(isEmptyOrNullString()));
            assertThat(pojo.birthday, notNullValue());
            assertThat(pojo.age, greaterThan(0));
            assertThat(pojo.balance, greaterThan(0L));
        }
        
        private static class SamplePojo
        {
            private String name;
            private Instant birthday;
            private int age;
            private long balance;
        }
    }

}
