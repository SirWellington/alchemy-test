/*
 * Copyright 2015 Sir Wellington.
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

package tech.sirwellington.alchemy.test;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.UUID;
import java.util.function.Supplier;
import org.apache.commons.lang3.RandomStringUtils;
import org.apache.commons.lang3.RandomUtils;
import org.apache.commons.lang3.StringUtils;
import org.hamcrest.Matchers;
import static org.hamcrest.Matchers.greaterThan;
import static org.hamcrest.Matchers.greaterThanOrEqualTo;
import static org.hamcrest.Matchers.is;
import static org.hamcrest.Matchers.lessThan;
import static org.hamcrest.Matchers.lessThanOrEqualTo;
import static org.hamcrest.Matchers.notNullValue;
import org.junit.After;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertThat;
import static org.junit.Assert.assertTrue;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import org.mockito.runners.MockitoJUnitRunner;
import static tech.sirwellington.alchemy.test.DataGenerator.alphabeticString;
import static tech.sirwellington.alchemy.test.DataGenerator.integers;
import static tech.sirwellington.alchemy.test.DataGenerator.oneOf;
import static tech.sirwellington.alchemy.test.DataGenerator.strings;
import static tech.sirwellington.alchemy.test.DataGenerator.uuids;
import static tech.sirwellington.alchemy.test.junit.ThrowableAssertion.assertThrows;

/**
 *
 * @author SirWellington
 */
@RunWith(MockitoJUnitRunner.class)
public class DataGeneratorTest
{

    private int iterations;

    @Before
    public void setUp()
    {
        iterations = RandomUtils.nextInt(500, 5000);
    }

    @After
    public void tearDown()
    {
    }

    @Test
    public void testGetOneValueFrom()
    {
        System.out.println("getOneValueFrom");
        DataGenerator instance = mock(DataGenerator.class);
        Object expected = mock(Object.class);
        when(instance.get()).thenReturn(expected);
        Object result = DataGenerator.oneOf(instance);
        verify(instance).get();
        assertEquals(expected, result);

    }

    @Test
    public void testIntegers()
    {
        System.out.println("testIntegers");

        int lowerBound = 0;
        int upperBound = 49506;
        DataGenerator<Integer> instance = DataGenerator.integers(lowerBound, upperBound);
        for (int i = 0; i < iterations; ++i)
        {
            int value = instance.get();
            assertThat(value, greaterThanOrEqualTo(lowerBound));
            assertThat(value, lessThan(upperBound));
        }
    }

    @Test
    public void testIntegersWithMinAndMax()
    {
        System.out.println("testIntegersWithMinAndMax");

        int lowerBound = Integer.MIN_VALUE;
        int upperBound = Integer.MAX_VALUE;

        DataGenerator<Integer> instance = DataGenerator.integers(lowerBound, upperBound);

        for (int i = 0; i < iterations; ++i)
        {
            int value = instance.get();
            assertThat(value, greaterThanOrEqualTo(lowerBound));
            assertThat(value, lessThan(upperBound));
        }
    }

    @Test
    public void testIntegersWithNegativeRange()
    {
        System.out.println("testIntegersWithNegativeRange");
        int lowerBound = -10;
        int upperBound = 150;
        DataGenerator<Integer> instance = DataGenerator.integers(lowerBound, upperBound);
        for (int i = 0; i < iterations; ++i)
        {
            int value = instance.get();
            assertThat(value, greaterThanOrEqualTo(lowerBound));
            assertThat(value, lessThan(upperBound));
        }

        lowerBound = -4934;
        upperBound = -500;
        instance = DataGenerator.integers(lowerBound, upperBound);
        for (int i = 0; i < iterations; ++i)
        {
            int value = instance.get();
            assertThat(value, greaterThanOrEqualTo(lowerBound));
            assertThat(value, lessThan(upperBound));
        }

        lowerBound = -5000;
        upperBound = -1;
        instance = DataGenerator.integers(lowerBound, upperBound);
        for (int i = 0; i < iterations; ++i)
        {
            int value = instance.get();
            assertThat(value, greaterThanOrEqualTo(lowerBound));
            assertThat(value, lessThan(upperBound));
        }

        lowerBound = Integer.MIN_VALUE;
        upperBound = -1;
        instance = DataGenerator.integers(lowerBound, upperBound);
        for (int i = 0; i < iterations; ++i)
        {
            int value = instance.get();
            assertThat(value, greaterThanOrEqualTo(lowerBound));
            assertThat(value, lessThan(upperBound));
        }
    }

    @Test
    public void testIntegersWithBadBounds()
    {
        System.out.println("testIntegersWithBadBounds");
        assertThrows(() -> DataGenerator.integers(7, 3))
                .isInstanceOf(IllegalArgumentException.class);

        assertThrows(() -> DataGenerator.integers(-10, -100))
                .isInstanceOf(IllegalArgumentException.class);

        assertThrows(() -> DataGenerator.integers(50, -600))
                .isInstanceOf(IllegalArgumentException.class);
    }

    @Test
    public void testLongs()
    {
        System.out.println("testLongs");
        long lowerBound = 10;
        long upperBound = 134_355_532_554_545L;
        DataGenerator<Long> instance = DataGenerator.longs(lowerBound, upperBound);
        for (int i = 0; i < iterations; ++i)
        {
            long value = instance.get();
            assertThat(value, greaterThanOrEqualTo(lowerBound));
            assertThat(value, lessThan(upperBound));
        }
    }

    @Test
    public void testLongsWithNegativeRange()
    {
        System.out.println("testLongsWithNegativeRange");
        long lowerBound = -10;
        long upperBound = 150_435_353_256_241L;
        DataGenerator<Long> instance = DataGenerator.longs(lowerBound, upperBound);
        for (int i = 0; i < iterations; ++i)
        {
            long value = instance.get();
            assertThat(value, greaterThanOrEqualTo(lowerBound));
            assertThat(value, lessThan(upperBound));
        }

        lowerBound = -493_435_754_432_216_763L;
        upperBound = -500_000;
        instance = DataGenerator.longs(lowerBound, upperBound);
        for (int i = 0; i < iterations; ++i)
        {
            long value = instance.get();
            assertThat(value, greaterThanOrEqualTo(lowerBound));
            assertThat(value, lessThan(upperBound));
        }

        lowerBound = Long.MIN_VALUE;
        upperBound = -1L;
        instance = DataGenerator.longs(lowerBound, upperBound);
        for (int i = 0; i < iterations; ++i)
        {
            long value = instance.get();
            assertThat(value, greaterThanOrEqualTo(lowerBound));
            assertThat(value, lessThan(upperBound));
        }
    }

    @Test
    public void testLongsWithBadBounds()
    {
        System.out.println("testLongsWithBadBounds");
        assertThrows(() -> DataGenerator.longs(7_423_352_214L, 3))
                .isInstanceOf(IllegalArgumentException.class);

        assertThrows(() -> DataGenerator.longs(-10L, -100L))
                .isInstanceOf(IllegalArgumentException.class);

        assertThrows(() -> DataGenerator.longs(50L, -600L))
                .isInstanceOf(IllegalArgumentException.class);
    }

    @Test
    public void testLongsWithMinAndMax()
    {
        System.out.println("testLongsWithMinAndMax");

        long lowerBound = Long.MIN_VALUE;
        long upperBound = Long.MAX_VALUE;

        DataGenerator<Long> instance = DataGenerator.longs(lowerBound, upperBound);

        for (int i = 0; i < iterations; ++i)
        {
            long value = instance.get();
            assertThat(value, greaterThanOrEqualTo(lowerBound));
            assertThat(value, lessThan(upperBound));
        }
    }

    @Test
    public void testDoubles()
    {
        System.out.println("testDoubles");
        double lowerBound = 80.0;
        double upperBound = 190.0;
        DataGenerator<Double> instance = DataGenerator.doubles(lowerBound, upperBound);
        for (int i = 0; i < iterations; ++i)
        {
            double value = instance.get();
            assertThat(value, greaterThanOrEqualTo(lowerBound));
            assertThat(value, lessThanOrEqualTo(upperBound));
        }
    }

    @Test
    public void testDoublesWithNegativeRange()
    {
        System.out.println("testDoublesWithNegativeRange");
        double lowerBound = -1343.0;
        double upperBound = 2044532.3;
        DataGenerator<Double> instance = DataGenerator.doubles(lowerBound, upperBound);

        for (int i = 0; i < iterations; ++i)
        {
            double value = instance.get();
            assertThat(value, greaterThanOrEqualTo(lowerBound));
            assertThat(value, lessThanOrEqualTo(upperBound));
        }

        lowerBound = -492425;
        upperBound = -5945;
        instance = DataGenerator.doubles(lowerBound, upperBound);
        for (int i = 0; i < iterations; ++i)
        {
            double value = instance.get();
            assertThat(value, greaterThanOrEqualTo(lowerBound));
            assertThat(value, lessThanOrEqualTo(upperBound));
        }
    }

    @Test
    public void testDoublesWithBadBounds()
    {
        System.out.println("testDoublesWithBadBounds");

        assertThrows(() -> DataGenerator.doubles(50, 35))
                .isInstanceOf(IllegalArgumentException.class);

        assertThrows(() -> DataGenerator.doubles(50, -35))
                .isInstanceOf(IllegalArgumentException.class);

        assertThrows(() -> DataGenerator.doubles(-50, -350))
                .isInstanceOf(IllegalArgumentException.class);
    }

    @Test
    public void testBooleans()
    {
        System.out.println("testBooleans");
        DataGenerator<Boolean> instance = DataGenerator.booleans();
        assertNotNull(instance);
        for (int i = 0; i < iterations; ++i)
        {
            assertNotNull(instance.get());
        }
    }

    @Test
    public void testStrings()
    {
        System.out.println("testStrings");
        int length = 59;
        DataGenerator<String> instance = DataGenerator.strings(length);
        assertNotNull(instance);
        for (int i = 0; i < iterations; ++i)
        {
            String value = instance.get();
            assertTrue(value.length() == length);
        }
    }

    @Test(expected = IllegalArgumentException.class)
    public void testStringsWithBadSize()
    {
        System.out.println("testStringsWithBadSize");
        int length = -59;
        DataGenerator<String> instance = DataGenerator.strings(length);
        assertNotNull(instance);
        instance.get();
    }

    @Test
    public void testHexadecimalString()
    {
        System.out.println("testHexadecimalString");
        int length = 90;
        DataGenerator<String> instance = DataGenerator.hexadecimalString(length);
        for (int i = 0; i < iterations; ++i)
        {
            String value = instance.get();
            assertTrue(value.length() == length);
        }
    }

    @Test(expected = IllegalArgumentException.class)
    public void testHexadecimalStringWithBadSize()
    {
        System.out.println("testHexadecimalStringWithBadSize");
        int length = -90;
        DataGenerator<String> instance = DataGenerator.hexadecimalString(length);
        instance.get();
    }

    @Test
    public void testAlphabeticString_int()
    {
        System.out.println("testAlphabeticString");
        int length = oneOf(integers(40, 100));

        DataGenerator<String> instance = DataGenerator.alphabeticString(length);
        for (int i = 0; i < iterations; ++i)
        {
            String value = instance.get();
            assertTrue(value.length() == length);
        }
    }

    @Test
    public void testAlphabeticString()
    {
        System.out.println("testAlphabeticString");
        DataGenerator<String> instance = alphabeticString();
        for (int i = 0; i < iterations; ++i)
        {
            String value = instance.get();
            assertThat(StringUtils.isEmpty(value), is(false));
        }
    }

    @Test(expected = IllegalArgumentException.class)
    public void testAlphabeticStringWithBadSize()
    {
        System.out.println("testAlphabeticStringWithBadSize");
        int length = 0;
        DataGenerator<String> instance = DataGenerator.alphabeticString(length);
        instance.get();
    }

    @Test
    public void testStringsFromFixedList()
    {
        System.out.println("testStringsFromFixedList");
        List<String> values = new ArrayList<>();
        for (int i = 0; i < iterations; ++i)
        {
            values.add(RandomStringUtils.randomAlphabetic(i + 1));
        }
        DataGenerator<String> instance = DataGenerator.stringsFromFixedList(values);

        for (int i = 0; i < iterations; ++i)
        {
            String value = instance.get();
            assertTrue(values.contains(value));
        }
    }

    @Test
    public void testIntegersFromFixedList()
    {
        System.out.println("testIntegersFromFixedList");
        List<Integer> values = new ArrayList<>();

        for (int i = 0; i < iterations; ++i)
        {
            values.add(RandomUtils.nextInt(4, 35));
        }

        DataGenerator<Integer> instance = DataGenerator.integersFromFixedList(values);

        for (int i = 0; i < iterations; ++i)
        {
            int value = instance.get();
            assertTrue(values.contains(value));
        }
    }

    @Test
    public void testDoublesFromFixedList()
    {
        System.out.println("testDoublesFromFixedList");
        List<Double> values = new ArrayList<>();
        for (int i = 0; i < iterations; ++i)
        {
            values.add(RandomUtils.nextDouble(4, 365));
        }

        DataGenerator<Double> instance = DataGenerator.doublesFromFixedList(values);

        for (int i = 0; i < iterations; ++i)
        {
            double value = instance.get();
            assertTrue(values.contains(value));
        }
    }

    @Test
    public void testPositiveIntegers()
    {
        System.out.println("testPositiveIntegers");
        DataGenerator<Integer> instance = DataGenerator.positiveIntegers();
        assertNotNull(instance);

        for (int i = 0; i < iterations; ++i)
        {
            assertThat(instance.get(), greaterThan(0));
        }
    }

    @Test
    public void testPositiveDoubles()
    {
        System.out.println("testPositiveDoubles");
        DataGenerator<Double> instance = DataGenerator.positiveDoubles();
        assertNotNull(instance);

        for (int i = 0; i < iterations; ++i)
        {
            assertThat(instance.get(), greaterThan(0.0));
        }

    }

    @Test
    public void testSmallPositiveIntegers()
    {
        System.out.println("testSmallPositiveIntegers");

        DataGenerator<Integer> instance = DataGenerator.smallPositiveIntegers();
        assertThat(instance, notNullValue());

        for (int i = 0; i < iterations; ++i)
        {
            int value = instance.get();
            assertThat(value, greaterThan(0));
            assertThat(value, lessThanOrEqualTo(1000));
        }
    }

    @Test
    public void testNegativeIntegers()
    {
        System.out.println("testNegativeIntegers");

        DataGenerator<Integer> instance = DataGenerator.negativeIntegers();
        assertThat(instance, notNullValue());

        for (int i = 0; i < iterations; ++i)
        {
            int value = instance.get();
            assertThat(value, lessThan(0));
        }
    }

    @Test
    public void testPositiveLongs()
    {
        System.out.println("testPositiveLongs");

        DataGenerator<Long> instance = DataGenerator.positiveLongs();
        assertThat(instance, notNullValue());

        for (int i = 0; i < iterations; ++i)
        {
            long value = instance.get();
            assertThat(value, greaterThan(0L));
        }
    }

    @Test
    public void testSmallPositiveLongs()
    {
        System.out.println("testSmallPositiveLongs");

        DataGenerator<Long> instance = DataGenerator.smallPositiveLongs();
        assertThat(instance, notNullValue());

        for (int i = 0; i < iterations; ++i)
        {
            long value = instance.get();
            assertThat(value, greaterThan(0L));
            assertThat(value, lessThanOrEqualTo(10_000L));
        }
    }

    @Test
    public void testSmallPositiveDoubles()
    {
        System.out.println("testSmallPositiveDoubles");

        DataGenerator<Double> instance = DataGenerator.smallPositiveDoubles();
        assertThat(instance, notNullValue());

        for (int i = 0; i < iterations; ++i)
        {
            double value = instance.get();
            assertThat(value, greaterThan(0.0));
            assertThat(value, lessThanOrEqualTo(1000.0));
        }
    }

    @Test
    public void testStringsFromFixedList_List()
    {
        System.out.println("testStringsFromFixedList_List");
        List<String> values = new ArrayList<>();
        String one = strings(10).get();
        String two = strings(10).get();
        String three = strings(10).get();
        values.add(one);
        values.add(two);
        values.add(three);

        DataGenerator<String> instance = DataGenerator.stringsFromFixedList(one, two, three);
        for (int i = 0; i < iterations; ++i)
        {
            assertThat(instance.get(), org.hamcrest.Matchers.isIn(values));
        }
    }

    @Test
    public void testStringsFromFixedList_StringArr()
    {
        System.out.println("testStringsFromFixedList_StringArr");
        DataGenerator<String> alphabetic = alphabeticString(10);
        String[] values = new String[]
        {
            alphabetic.get(), alphabetic.get(), alphabetic.get()
        };
        DataGenerator<String> instance = DataGenerator.stringsFromFixedList(values);
        assertThat(instance, notNullValue());
        for (int i = 0; i < 50; ++i)
        {
            String result = instance.get();
            assertThat(result, Matchers.isIn(values));
        }
    }

    @Test
    public void testListOf_DataGenerator()
    {
        System.out.println("testListOf_DataGenerator");

        Object value = new Object();
        DataGenerator generator = mock(DataGenerator.class);
        when(generator.get()).thenReturn(value);

        List result = DataGenerator.listOf(generator);
        assertThat(result.isEmpty(), is(false));
        result.forEach(i -> assertThat(i, is(value)));
    }

    @Test
    public void testListOf_DataGenerator_int()
    {
        System.out.println("testListOf_DataGenerator_int");

        Object value = new Object();
        int size = 50;
        DataGenerator generator = mock(DataGenerator.class);
        when(generator.get()).thenReturn(value);
        List result = DataGenerator.listOf(generator, 50);
        assertThat(result, notNullValue());
        assertThat(result.size(), is(size));
        result.forEach(i -> assertThat(i, is(value)));
    }

    @Test
    public void testEnumValueOf()
    {
        System.out.println("testEnumValueOf");
        Supplier<Fruit> fruits = DataGenerator.enumValueOf(Fruit.class);
        assertThat(fruits, notNullValue());
        for (int i = 0; i < iterations; ++i)
        {
            Fruit fruit = fruits.get();
            assertThat(fruit, notNullValue());
            assertThat(fruit, Matchers.isA(Fruit.class));
        }
    }

    enum Fruit
    {

        Apple, Orange, Pear
    }

    @Test
    public void testBinaryGenerator()
    {
        System.out.println("binaryGenerator");
        int bytes = integers(50, 5000).get();
        DataGenerator<byte[]> instance = DataGenerator.binaryGenerator(bytes);

        assertNotNull(instance);

        for (int i = 0; i < iterations; ++i)
        {
            byte[] value = instance.get();
            assertThat(value, notNullValue());
            assertThat(value.length, is(bytes));
        }
    }

    @Test
    public void testMapOf()
    {
        System.out.println("mapOf");
        String string = strings(50).get();
        DataGenerator<String> valueGenerator = () -> string;
        int size = integers(5, 100).get();

        Map<String, String> result = DataGenerator.mapOf(uuids, valueGenerator, size);
        assertThat(result, notNullValue());
        assertThat(result.size(), is(size));

        for (Map.Entry<String, String> entry : result.entrySet())
        {
            UUID.fromString(entry.getKey());
            assertThat(entry.getValue(), is(string));
        }

    }

    @Test
    public void testAlphabeticStringWithNoArgs()
    {
        System.out.println("testAlphabeticStringWithNoArgs");

        DataGenerator<String> instance = DataGenerator.alphabeticString();
        assertThat(instance, notNullValue());

        for (int i = 0; i < iterations; ++i)
        {
            String value = instance.get();
            assertThat(value, notNullValue());
            assertThat(value.length(), greaterThanOrEqualTo(5));
            assertThat(value.length(), lessThanOrEqualTo(20));
        }

    }
}
