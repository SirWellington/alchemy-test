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

import java.net.URL;
import java.time.Instant;
import java.util.Date;
import java.util.List;
import java.util.concurrent.TimeUnit;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.model.TestClass;
import org.mockito.runners.MockitoJUnitRunner;

import static org.hamcrest.Matchers.empty;
import static org.hamcrest.Matchers.greaterThan;
import static org.hamcrest.Matchers.greaterThanOrEqualTo;
import static org.hamcrest.Matchers.is;
import static org.hamcrest.Matchers.isEmptyOrNullString;
import static org.hamcrest.Matchers.lessThan;
import static org.hamcrest.Matchers.not;
import static org.hamcrest.Matchers.notNullValue;
import static org.hamcrest.Matchers.nullValue;
import static org.junit.Assert.assertThat;
import static tech.sirwellington.alchemy.test.junit.ThrowableAssertion.assertThrows;

/**
 *
 * @author SirWellington
 */
@RunWith(MockitoJUnitRunner.class)
public class TestClassInjectorsTest
{

    @Before
    public void setUp()
    {
    }

    @Test
    public void testPopulateGeneratedFields() throws Exception
    {
        System.out.println("testPopulateGeneratedFields");
        
        TestClass testClass = new TestClass(FakeTestClass.class);
        FakeTestClass instance = new FakeTestClass();
 
        TestClassInjectors.populateGeneratedFields(testClass, instance);
        instance.setUp();
    }

    @Test
    public void testPopulateGeneratedFieldsWithBadEnum() throws Exception
    {
        System.out.println("testPopulateGeneratedFieldsWithBadEnum");
        
        TestClass testClass = new TestClass(BadEnumTest.class);
        BadEnumTest instance = new BadEnumTest();
        
        assertThrows(() ->  TestClassInjectors.populateGeneratedFields(testClass, instance))
            .isInstanceOf(IllegalArgumentException.class);
    }
    
    @Test
    public void testPopulateGeneratedFieldsWithBadList() throws Exception
    {
        System.out.println("testPopulateGeneratedFieldsWithBadList");
        
        TestClass testClass = new TestClass(BadListTest.class);
        BadListTest instance = new BadListTest();
        
        assertThrows(() ->  TestClassInjectors.populateGeneratedFields(testClass, instance))
            .isInstanceOf(IllegalArgumentException.class);
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
        
        private static final long MIN_LONG = -19532L;
        private static final long MAX_LONG = 394813391439L;
        
        private static final double MIN_DOUBLE = -39341.1983123153;
        private static final double MAX_DOUBLE = 29414.5329;
        
        private static final long START_TIME = 20431;
        private static final long END_TIME = START_TIME + 25442545;

        @GenerateString(length = STRING_LENGTH)
        private String string;
        
        @GenerateInteger(value = GenerateInteger.Type.RANGE, min = MIN_INT, max = MAX_INT)
        private int integer;
        
        @GenerateLong(value = GenerateLong.Type.RANGE, min = MIN_LONG, max=MAX_LONG)
        private long lon;
        
        @GenerateDouble(value = GenerateDouble.Type.RANGE, min = MIN_DOUBLE, max = MAX_DOUBLE)
        private double decimal;
        
        @GenerateDate(value = GenerateDate.Type.RANGE, startDate = START_TIME, endDate = END_TIME)
        private Date date;
        
        @GenerateDate(GenerateDate.Type.PAST)
        private Date pastDate;
        
        @GenerateInstant(value = GenerateInstant.Type.RANGE, startTime = START_TIME, endTime = END_TIME)
        private Instant instant;
        
        @GeneratePojo
        private SamplePojo pojo;
        
        @GenerateEnum
        private TimeUnit timeUnit;
        
        @GenerateList(String.class)
        private List<String> names;
        
        @GenerateList(value = SamplePojo.class, size = 5)
        private List<SamplePojo> pojos;
        
        @GenerateURL
        private URL url;

        @Before
        public void setUp()
        {
            assertThat(string, not(isEmptyOrNullString()));
            assertThat(string.length(), is(STRING_LENGTH));
            
            assertThat(integer, greaterThanOrEqualTo(MIN_INT));
            assertThat(integer, lessThan(MAX_INT));
            
            assertThat(lon, greaterThanOrEqualTo(MIN_LONG));
            assertThat(lon, lessThan(MAX_LONG));
            
            assertThat(decimal, greaterThan(MIN_DOUBLE));
            assertThat(decimal, lessThan(MAX_DOUBLE));
            
            assertThat(date, notNullValue());
            assertThat(date.getTime(), greaterThanOrEqualTo(START_TIME));
            assertThat(date.getTime(), lessThan(END_TIME));
            
            assertThat(instant, notNullValue());
            assertThat(instant.toEpochMilli(), greaterThanOrEqualTo(START_TIME));
            assertThat(instant.toEpochMilli(), lessThan(END_TIME));
            
            Date now = new Date();
            assertThat(pastDate.before(now), is(true));
            
            checkPojo(pojo);
            
            assertThat(timeUnit, notNullValue());
            
            assertThat(names, notNullValue());
            assertThat(names, not(empty()));
            
            assertThat(pojos, notNullValue());
            assertThat(pojos, not(empty()));
            assertThat(pojos.size(), is(5));
            
            assertThat(url, notNullValue());
            
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
    
    private static class BadListTest
    {
        @GenerateList(value = String.class, size = -1)
        private List<String> strings;
        
        @Before
        public void setUp()
        {
            assertThat(strings, nullValue());
        }
    }
    
    private static class BadEnumTest
    {
        @GenerateEnum
        private Object object;
        
        @Before
        public void setUp()
        {
            assertThat(object, nullValue());
        }
    }
    
}
