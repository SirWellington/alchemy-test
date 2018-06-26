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

import java.lang.annotation.Annotation;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.runners.MockitoJUnitRunner;
import tech.sirwellington.alchemy.generator.AlchemyGenerator;

import static org.hamcrest.Matchers.*;
import static org.junit.Assert.assertThat;
import static tech.sirwellington.alchemy.generator.AlchemyGenerator.Get.one;
import static tech.sirwellington.alchemy.generator.EnumGenerators.enumValueOf;
import static tech.sirwellington.alchemy.generator.NumberGenerators.longs;
import static tech.sirwellington.alchemy.test.junit.runners.GenerateLong.Type.RANGE;

/**
 * @author SirWellington
 */
@RunWith(MockitoJUnitRunner.class)
public class GenerateLongTest
{

    private GenerateLong.Type type;
    private long min;
    private long max;

    private GenerateLongInstance annotation;

    @Before
    public void setUp()
    {
        type = enumValueOf(GenerateLong.Type.class).get();
        min = one(longs(Long.MIN_VALUE, 1000));
        max = one(longs(1000, Long.MAX_VALUE));
        annotation = new GenerateLongInstance(type, min, max);
    }

    @Test(expected = IllegalAccessException.class)
    public void testCannotInstatiate() throws IllegalAccessException, InstantiationException
    {
        System.out.println("testCannotInstantiate");

        GenerateLong.Values.class.newInstance();
    }

    @Test
    public void testValues()
    {
        System.out.println("testValues");

        AlchemyGenerator<Long> result = GenerateLong.Values.createGeneratorFor(annotation);
        assertThat(result, notNullValue());

        Long value = result.get();
        assertThat(value, notNullValue());

        if (type == RANGE)
        {
            assertThat(value, greaterThanOrEqualTo(min));
            assertThat(value, lessThan(max));
        }
        else
        {
            switch (type)
            {
                case POSITIVE:
                    assertThat(value, greaterThan(0L));
                    break;
                case NEGATIVE:
                    assertThat(value, lessThan(0L));
                    break;
            }
        }

    }

    @Test(expected = IllegalArgumentException.class)
    public void testValuesEdgeCases1()
    {
        System.out.println("testValuesEdgeCases");

        GenerateLong.Values.createGeneratorFor(null);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testValuesEdgeCases2()
    {
        System.out.println("testValuesEdgeCases");

        annotation = new GenerateLongInstance(null, min, max);
        GenerateLong.Values.createGeneratorFor(annotation);
    }


    @Test(expected = IllegalArgumentException.class)
    public void testValuesEdgeCases3()
    {
        System.out.println("testValuesEdgeCases");

        long badMin = max;
        long badMax = min;
        type = RANGE;
        annotation = new GenerateLongInstance(type, badMin, badMax);
        GenerateLong.Values.createGeneratorFor(annotation);

    }


    private static class GenerateLongInstance implements GenerateLong
    {

        private final GenerateLong.Type type;
        private final long min;
        private final long max;

        private GenerateLongInstance(GenerateLong.Type type, long min, long max)
        {
            this.type = type;
            this.min = min;
            this.max = max;
        }

        @Override
        public GenerateLong.Type value()
        {
            return type;
        }

        @Override
        public long min()
        {
            return min;
        }

        @Override
        public long max()
        {
            return max;
        }

        @Override
        public Class<? extends Annotation> annotationType()
        {
            return GenerateLong.class;
        }
    }

}
