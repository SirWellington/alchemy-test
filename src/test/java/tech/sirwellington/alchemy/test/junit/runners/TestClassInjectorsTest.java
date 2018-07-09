/*
 * Copyright © 2018. Sir Wellington.
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

package tech.sirwellington.alchemy.test.junit.runners;

import java.net.URL;
import java.sql.Timestamp;
import java.util.*;
import java.util.concurrent.TimeUnit;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.model.TestClass;
import org.mockito.junit.MockitoJUnitRunner;
import tech.sirwellington.alchemy.annotations.testing.IntegrationTest;
import tech.sirwellington.alchemy.generator.AlchemyGenerator;
import tech.sirwellington.alchemy.generator.StringGenerators;

import static org.hamcrest.Matchers.*;
import static org.junit.Assert.assertThat;

/**
 * @author SirWellington
 */
@IntegrationTest
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

    @Test(expected = IllegalArgumentException.class)
    public void testPopulateGeneratedFieldsWithBadEnum() throws Exception
    {
        System.out.println("testPopulateGeneratedFieldsWithBadEnum");

        TestClass testClass = new TestClass(BadEnumTest.class);
        BadEnumTest instance = new BadEnumTest();

        TestClassInjectors.populateGeneratedFields(testClass, instance);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testPopulateGeneratedFieldsWithBadList() throws Exception
    {
        System.out.println("testPopulateGeneratedFieldsWithBadList");

        TestClass testClass = new TestClass(BadListTest.class);
        BadListTest instance = new BadListTest();

        TestClassInjectors.populateGeneratedFields(testClass, instance);
    }

    @Test
    public void testInflateString() throws Exception
    {
    }

    public static class FakeTestClass
    {

        private static final int STRING_LENGTH = 346;

        private static final int MIN_INT = 34;
        private static final int MAX_INT = 2021145;

        private static final long MIN_LONG = -19532L;
        private static final long MAX_LONG = 394813391439L;

        private static final float MIN_FLOAT = -29582891.4958f;
        private static final float MAX_FLOAT = 4054582.39295029f;

        private static final double MIN_DOUBLE = -39341.1983123153;
        private static final double MAX_DOUBLE = 29414.5329;

        private static final long START_TIME = 20431;
        private static final long END_TIME = START_TIME + 25442545;

        @GenerateString(length = STRING_LENGTH)
        private String string;

        @GenerateBoolean
        private Boolean bool;

        @GenerateInteger(value = GenerateInteger.Type.RANGE, min = MIN_INT, max = MAX_INT)
        private int integer;

        @GenerateLong(value = GenerateLong.Type.RANGE, min = MIN_LONG, max = MAX_LONG)
        private long lon;

        @GenerateFloat(value = GenerateFloat.Type.RANGE, min = MIN_FLOAT, max = MAX_FLOAT)
        private float floatNumber;

        @GenerateDouble(value = GenerateDouble.Type.RANGE, min = MIN_DOUBLE, max = MAX_DOUBLE)
        private double doubleNumber;

        @GenerateDate(value = GenerateDate.Type.RANGE, startDate = START_TIME, endDate = END_TIME)
        private Date date;

        @GenerateDate(GenerateDate.Type.PAST)
        private Date pastDate;

        @GenerateDate
        private Timestamp timestamp;

        @GenerateDate
        private java.sql.Date sqlDate;

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

        @GenerateCustom(BookGenerator.class)
        private Book book;


        @Before
        public void setUp()
        {
            assertThat(string, not(isEmptyOrNullString()));
            assertThat(string.length(), is(STRING_LENGTH));

            assertThat(bool, notNullValue());

            assertThat(integer, greaterThanOrEqualTo(MIN_INT));
            assertThat(integer, lessThan(MAX_INT));

            assertThat(lon, greaterThanOrEqualTo(MIN_LONG));
            assertThat(lon, lessThan(MAX_LONG));

            assertThat(floatNumber, greaterThan(MIN_FLOAT));
            assertThat(floatNumber, lessThan(MAX_FLOAT));

            assertThat(doubleNumber, greaterThan(MIN_DOUBLE));
            assertThat(doubleNumber, lessThan(MAX_DOUBLE));

            assertThat(date, notNullValue());
            assertThat(date.getTime(), greaterThanOrEqualTo(START_TIME));
            assertThat(date.getTime(), lessThan(END_TIME));

            assertThat(timestamp, notNullValue());
            assertThat(sqlDate, notNullValue());

            Date now = new Date();
            assertThat(pastDate.before(now), is(true));

            assertThat(timeUnit, notNullValue());

            assertThat(names, notNullValue());
            assertThat(names, not(empty()));

            assertThat(pojos, notNullValue());
            assertThat(pojos, not(empty()));
            assertThat(pojos.size(), is(5));

            assertThat(url, notNullValue());

            assertThat(book, notNullValue());
            checkBook();

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

        @Test
        public void checkBook()
        {
            assertThat(book, notNullValue());
            assertThat(book.author, not(isEmptyOrNullString()));
            assertThat(book.title, not(isEmptyOrNullString()));
        }

        private static class SamplePojo
        {
            private String name;
            private Date birthday;
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

    private static class Book
    {
        private String title;
        private String author;

        @Override
        public boolean equals(Object o)
        {
            if (this == o)
            {
                return true;
            }
            if (o == null || getClass() != o.getClass())
            {
                return false;
            }
            Book book = (Book) o;
            return Objects.equals(title, book.title) &&
                    Objects.equals(author, book.author);
        }

        @Override
        public int hashCode()
        {
            return Objects.hash(title, author);
        }
    }

    private static class BookGenerator implements AlchemyGenerator<Book>
    {
        public BookGenerator()
        {
        }

        @Override
        public Book get()
        {
            String title = StringGenerators.alphanumericStrings().get();
            String author = StringGenerators.alphanumericStrings().get();
            Book book = new Book();
            book.title = title;
            book.author = author;

            return book;
        }
    }


}
