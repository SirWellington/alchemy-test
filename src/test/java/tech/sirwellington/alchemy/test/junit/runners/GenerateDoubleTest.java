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
import org.mockito.runners.MockitoJUnitRunner;
import tech.sirwellington.alchemy.generator.AlchemyGenerator;

import static org.hamcrest.Matchers.*;
import static org.junit.Assert.assertThat;
import static tech.sirwellington.alchemy.generator.AlchemyGenerator.Get.one;
import static tech.sirwellington.alchemy.generator.EnumGenerators.enumValueOf;
import static tech.sirwellington.alchemy.generator.NumberGenerators.doubles;
import static tech.sirwellington.alchemy.test.junit.ThrowableAssertion.*;
import static tech.sirwellington.alchemy.test.junit.runners.GenerateDouble.Type.RANGE;

/**
 * @author SirWellington
 */
@RunWith(MockitoJUnitRunner.class)
public class GenerateDoubleTest
{

    private GenerateDouble.Type type;
    private double min;
    private double max;

    private GenerateDoubleInstance annotation;

    @Before
    public void setUp()
    {
        type = enumValueOf(GenerateDouble.Type.class).get();
        min = one(doubles(Double.MIN_VALUE, 1000));
        max = one(doubles(1000, Double.MAX_VALUE));
        annotation = new GenerateDoubleInstance(type, min, max);
    }

    @Test(expected = IllegalAccessException.class)
    public void testCannotInstantiate() throws IllegalAccessException, InstantiationException
    {
        System.out.println("testCannotInstatiate");
        GenerateDouble.Values.class.newInstance();
    }

    @Test
    public void testValues()
    {
        System.out.println("testValues");

        AlchemyGenerator<Double> result = GenerateDouble.Values.createGeneratorFor(annotation);
        assertThat(result, notNullValue());

        Double value = result.get();
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
                    assertThat(value, greaterThan(0.0));
                    break;
                case NEGATIVE:
                    assertThat(value, lessThan(0.0));
                    break;
            }
        }

    }

    @Test(expected = IllegalArgumentException.class)
    public void testValuesEdgeCases1() throws Exception
    {
        System.out.println("testValuesEdgeCases1");

        GenerateDouble.Values.createGeneratorFor(null);

    }

    @Test(expected = IllegalArgumentException.class)
    public void testValuesEdgeCases2() throws Exception
    {
        System.out.println("testValuesEdgeCases1");


        annotation = new GenerateDoubleInstance(null, min, max);
        GenerateDouble.Values.createGeneratorFor(annotation);

    }

    @Test(expected = IllegalArgumentException.class)
    public void testValuesEdgeCases3() throws Exception
    {
        System.out.println("testValuesEdgeCases1");

        double badMin = max;
        double badMax = min;
        type = RANGE;
        annotation = new GenerateDoubleInstance(type, badMin, badMax);
        GenerateDouble.Values.createGeneratorFor(annotation);

    }

    private static class GenerateDoubleInstance implements GenerateDouble
    {

        private final GenerateDouble.Type type;
        private final double min;
        private final double max;

        private GenerateDoubleInstance(GenerateDouble.Type type, double min, double max)
        {
            this.type = type;
            this.min = min;
            this.max = max;
        }

        @Override
        public GenerateDouble.Type value()
        {
            return type;
        }

        @Override
        public double min()
        {
            return min;
        }

        @Override
        public double max()
        {
            return max;
        }

        @Override
        public Class<? extends Annotation> annotationType()
        {
            return GenerateDouble.class;
        }
    }

}
