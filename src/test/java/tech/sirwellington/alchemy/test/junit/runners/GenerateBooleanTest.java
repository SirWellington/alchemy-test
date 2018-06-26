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

import static org.hamcrest.Matchers.notNullValue;
import static org.junit.Assert.assertThat;

/**
 * @author SirWellington
 */
@RunWith(MockitoJUnitRunner.class)
public class GenerateBooleanTest
{
    private GenerateBoolean annotation;


    @Before
    public void setUp()
    {
        annotation = new BasicAnnotation();
    }

    @Test(expected = IllegalAccessException.class)
    public void testCannotInstantiate() throws IllegalAccessException, InstantiationException
    {
        System.out.println("testCannotInstantiate");

        GenerateBoolean.Values.class.newInstance();
    }

    @Test
    public void testValues()
    {
        System.out.println("testValues");

        AlchemyGenerator<Boolean> generator = GenerateBoolean.Values.createGeneratorFor(annotation);
        assertThat(generator, notNullValue());

        Boolean value = generator.get();
        assertThat(value, notNullValue());
    }

    @Test(expected = IllegalArgumentException.class)
    public void testValuesEdgeCases()
    {
        System.out.println("testValuesEdgeCases");

        GenerateBoolean.Values.createGeneratorFor(null);
    }

    private static class BasicAnnotation implements GenerateBoolean
    {

        public BasicAnnotation()
        {
        }

        @Override
        public Class<? extends Annotation> annotationType()
        {
            return GenerateBoolean.class;
        }
    }

}
