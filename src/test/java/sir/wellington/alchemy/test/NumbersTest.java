/*
 * Copyright 2015 SirWellington.
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
package sir.wellington.alchemy.test;

import static org.hamcrest.Matchers.is;
import static org.junit.Assert.assertThat;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.runners.MockitoJUnitRunner;
import static sir.wellington.alchemy.test.DataGenerator.integers;
import static sir.wellington.alchemy.test.DataGenerator.longs;
import static sir.wellington.alchemy.test.DataGenerator.oneOf;
import static sir.wellington.alchemy.test.Numbers.safeDecrement;
import static sir.wellington.alchemy.test.Numbers.safeIncrement;

/**
 *
 * @author SirWellington
 */
@RunWith(MockitoJUnitRunner.class)
public class NumbersTest
{

    @Before
    public void setUp()
    {
    }

    @Test
    public void testSafeIncrement_long()
    {
        System.out.println("testSafeIncrement_long");

        long value = oneOf(longs(-10_000L, 10_000L));
        long result = safeIncrement(value);
        assertThat(result, is(value + 1));

        value = Long.MAX_VALUE;
        result = safeIncrement(value);
        assertThat(result, is(value));

        value = Long.MIN_VALUE;
        result = safeIncrement(value);
        assertThat(result, is(value + 1));
    }

    @Test
    public void testSafeDecrement_long()
    {
        System.out.println("testSafeDecrement_long");

        long value = oneOf(longs(-10_000L, 10_000L));
        long result = safeDecrement(value);
        assertThat(result, is(value - 1));

        value = Long.MIN_VALUE;
        result = safeDecrement(value);
        assertThat(result, is(value));

        value = Long.MAX_VALUE;
        result = safeDecrement(value);
        assertThat(result, is(value - 1));
    }

    @Test
    public void testSafeIncrement_int()
    {
        System.out.println("testSafeIncrement_int");

        int value = oneOf(integers(-10_000, 10_000));
        int result = safeIncrement(value);
        assertThat(result, is(value + 1));

        value = Integer.MAX_VALUE;
        result = safeIncrement(value);
        assertThat(result, is(value));

        value = Integer.MIN_VALUE;
        result = safeIncrement(value);
        assertThat(result, is(value + 1));
    }

    @Test
    public void testSafeDecrement_int()
    {
        System.out.println("testSafeDecrement_int");

        int value = oneOf(integers(-10_000, 10_000));
        int result = safeDecrement(value);
        assertThat(result, is(value - 1));

        value = Integer.MIN_VALUE;
        result = safeDecrement(value);
        assertThat(result, is(value));

        value = Integer.MAX_VALUE;
        result = safeDecrement(value);
        assertThat(result, is(value - 1));
    }

}
