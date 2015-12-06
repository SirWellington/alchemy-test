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

import java.lang.annotation.Annotation;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.runners.MockitoJUnitRunner;
import tech.sirwellington.alchemy.generator.AlchemyGenerator;

import static org.hamcrest.Matchers.greaterThan;
import static org.hamcrest.Matchers.greaterThanOrEqualTo;
import static org.hamcrest.Matchers.lessThan;
import static org.hamcrest.Matchers.notNullValue;
import static org.junit.Assert.assertThat;
import static tech.sirwellington.alchemy.generator.AlchemyGenerator.one;
import static tech.sirwellington.alchemy.generator.EnumGenerators.enumValueOf;
import static tech.sirwellington.alchemy.generator.NumberGenerators.integers;
import static tech.sirwellington.alchemy.test.junit.ThrowableAssertion.assertThrows;
import static tech.sirwellington.alchemy.test.junit.runners.GenerateInteger.Type.RANGE;

/**
 *
 * @author SirWellington
 */
@RunWith(MockitoJUnitRunner.class)
public class GenerateIntegerTest
{

    private GenerateInteger.Type type;
    private int min;
    private int max;

    private FakeAnnotation annotation;

    @Before
    public void setUp()
    {
        type = enumValueOf(GenerateInteger.Type.class).get();
        min = one(integers(-1000, 1000));
        max = one(integers(1000, 100_000));
        annotation = new FakeAnnotation(type, min, max);
    }

    @Test
    public void testCannotInstatiate()
    {
        System.out.println("testCannotInstatiate");

        assertThrows(() -> new GenerateInteger.Values())
            .isInstanceOf(IllegalAccessException.class);

        assertThrows(() -> GenerateInteger.Values.class.newInstance())
            .isInstanceOf(IllegalAccessException.class);
    }

    @Test
    public void testValues()
    {
        System.out.println("testValues");

        AlchemyGenerator<Integer> result = GenerateInteger.Values.createGeneratorFor(annotation);
        assertThat(result, notNullValue());

        Integer integer = result.get();
        assertThat(integer, notNullValue());

        if (type == RANGE)
        {
            assertThat(integer, greaterThanOrEqualTo(min));
            assertThat(integer, lessThan(max));
        }
        else
        {
            switch (type)
            {
                case POSITIVE:
                    assertThat(integer, greaterThan(0));
                    break;
                case NEGATIVE:
                    assertThat(integer, lessThan(0));
                    break;
            }
        }

    }

    @Test
    public void testValuesEdgeCases()
    {
        System.out.println("testValuesEdgeCases");

        assertThrows(() -> GenerateInteger.Values.createGeneratorFor(null))
            .isInstanceOf(IllegalArgumentException.class);

        annotation = new FakeAnnotation(null, min, max);
        assertThrows(() -> GenerateInteger.Values.createGeneratorFor(annotation))
            .isInstanceOf(IllegalArgumentException.class);

        int badMin = max;
        int badMax = min;
        type = RANGE;
        annotation = new FakeAnnotation(type, badMin, badMax);
        assertThrows(() -> GenerateInteger.Values.createGeneratorFor(annotation))
            .isInstanceOf(IllegalArgumentException.class);

    }

    private class FakeAnnotation implements GenerateInteger
    {

        private final Type type;
        private final int min;
        private final int max;

        private FakeAnnotation(Type type, int min, int max)
        {
            this.type = type;
            this.min = min;
            this.max = max;
        }

        @Override
        public Type value()
        {
            return type;
        }

        @Override
        public int min()
        {
            return min;
        }

        @Override
        public int max()
        {
            return max;
        }

        @Override
        public Class<? extends Annotation> annotationType()
        {
            return GenerateInteger.class;
        }
    }

}
