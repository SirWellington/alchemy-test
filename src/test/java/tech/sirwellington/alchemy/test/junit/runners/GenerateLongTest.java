/*
 * Copyright 2015 Aroma Tech.
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

import java.lang.annotation.Annotation;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import tech.sirwellington.alchemy.generator.AlchemyGenerator;

import static org.hamcrest.Matchers.*;
import static org.junit.Assert.*;
import static tech.sirwellington.alchemy.generator.AlchemyGenerator.one;
import static tech.sirwellington.alchemy.generator.EnumGenerators.enumValueOf;
import static tech.sirwellington.alchemy.generator.NumberGenerators.longs;
import static tech.sirwellington.alchemy.test.junit.ThrowableAssertion.assertThrows;
import static tech.sirwellington.alchemy.test.junit.runners.GenerateLong.Type.RANGE;

/**
 *
 * @author SirWellington
 */
@Repeat(10)
@RunWith(AlchemyTestRunner.class)
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

    @Test
    public void testCannotInstatiate()
    {
        System.out.println("testCannotInstatiate");

        assertThrows(() -> GenerateLong.Values.class.newInstance())
            .isInstanceOf(IllegalAccessException.class);
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

    @Test
    public void testValuesEdgeCases()
    {
        System.out.println("testValuesEdgeCases");

        assertThrows(() -> GenerateLong.Values.createGeneratorFor(null))
            .isInstanceOf(IllegalArgumentException.class);

        annotation = new GenerateLongInstance(null, min, max);
        assertThrows(() -> GenerateLong.Values.createGeneratorFor(annotation))
            .isInstanceOf(IllegalArgumentException.class);

        long badMin = max;
        long badMax = min;
        type = RANGE;
        annotation = new GenerateLongInstance(type, badMin, badMax);
        assertThrows(() -> GenerateLong.Values.createGeneratorFor(annotation))
            .isInstanceOf(IllegalArgumentException.class);

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