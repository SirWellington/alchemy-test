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
import org.mockito.junit.MockitoJUnitRunner;
import tech.sirwellington.alchemy.generator.AlchemyGenerator;

import static org.hamcrest.Matchers.*;
import static org.junit.Assert.assertThat;
import static tech.sirwellington.alchemy.generator.AlchemyGenerator.Get.one;
import static tech.sirwellington.alchemy.generator.EnumGenerators.enumValueOf;
import static tech.sirwellington.alchemy.generator.NumberGenerators.floats;
import static tech.sirwellington.alchemy.test.junit.runners.GenerateFloat.Type.RANGE;

/**
 * @author SirWellington
 */
@RunWith(MockitoJUnitRunner.class)
public class GenerateFloatTest
{

    private GenerateFloat.Type type;
    private float min;
    private float max;

    private GenerateFloatInstance annotation;

    @Before
    public void setUp()
    {
        type = enumValueOf(GenerateFloat.Type.class).get();
        min = one(floats(Float.MIN_VALUE, 1000));
        max = one(floats(1000, Float.MAX_VALUE));
        annotation = new GenerateFloatInstance(type, min, max);
    }

    @Test(expected = IllegalAccessException.class)
    public void testCannotInstantiate() throws IllegalAccessException, InstantiationException
    {
        System.out.println("testCannotInstantiate");

        GenerateFloat.Values.class.newInstance();
    }

    @Test
    public void testValues()
    {
        System.out.println("testValues");

        AlchemyGenerator<Float> result = GenerateFloat.Values.createGeneratorFor(annotation);
        assertThat(result, notNullValue());

        Float value = result.get();
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
                    assertThat(value, greaterThan(0.0f));
                    break;
                case NEGATIVE:
                    assertThat(value, lessThan(0.0f));
                    break;
            }
        }

    }

    @Test(expected = IllegalArgumentException.class)
    public void testValuesEdgeCases1() throws Exception
    {
        System.out.println("testValuesEdgeCases1");

        GenerateFloat.Values.createGeneratorFor(null);

    }

    @Test(expected = IllegalArgumentException.class)
    public void testValuesEdgeCases2() throws Exception
    {
        System.out.println("testValuesEdgeCases1");


        annotation = new GenerateFloatInstance(null, min, max);
        GenerateFloat.Values.createGeneratorFor(annotation);

    }

    @Test(expected = IllegalArgumentException.class)
    public void testValuesEdgeCases3() throws Exception
    {
        System.out.println("testValuesEdgeCases1");

        float badMin = max;
        float badMax = min;
        type = RANGE;
        annotation = new GenerateFloatInstance(type, badMin, badMax);
        GenerateFloat.Values.createGeneratorFor(annotation);

    }

    private static class GenerateFloatInstance implements GenerateFloat
    {

        private final Type type;
        private final float min;
        private final float max;

        private GenerateFloatInstance(Type type, float min, float max)
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
        public float min()
        {
            return min;
        }

        @Override
        public float max()
        {
            return max;
        }

        @Override
        public Class<? extends Annotation> annotationType()
        {
            return GenerateFloat.class;
        }
    }

}
