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

import org.apache.commons.lang3.StringUtils;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.runners.MockitoJUnitRunner;
import tech.sirwellington.alchemy.generator.AlchemyGenerator;
import tech.sirwellington.alchemy.generator.EnumGenerators;

import static org.hamcrest.Matchers.*;
import static org.junit.Assert.assertThat;
import static tech.sirwellington.alchemy.generator.AlchemyGenerator.Get.one;
import static tech.sirwellington.alchemy.generator.NumberGenerators.integers;
import static tech.sirwellington.alchemy.generator.NumberGenerators.negativeIntegers;
import static tech.sirwellington.alchemy.test.junit.runners.GenerateString.Type.UUID;

/**
 * @author SirWellington
 */
@RunWith(MockitoJUnitRunner.class)
public class GenerateStringTest
{
    private GenerateString annotation;
    private GenerateString.Type type;
    private int length;

    @Before
    public void setUp()
    {
        type = EnumGenerators.enumValueOf(GenerateString.Type.class).get();
        length = one(integers(5, 500));
        annotation = new GenerateStringInstance(type, length);

    }

    @Test(expected = IllegalAccessException.class)
    public void testCannotInstantiate() throws IllegalAccessException, InstantiationException
    {
        System.out.println("testCannotInstatiate");

        GenerateString.Values.class.newInstance();
    }

    @Test
    public void testValues()
    {
        System.out.println("testValues");

        AlchemyGenerator<String> result = GenerateString.Values.createGeneratorFor(annotation);
        assertThat(result, notNullValue());

        String string = result.get();
        assertThat(string, not(isEmptyOrNullString()));

        if (type == UUID)
        {
            int uuidLength = java.util.UUID.randomUUID().toString().length();
            assertThat(string.length(), is(uuidLength));
        }
        else
        {
            assertThat(string.length(), is(length));
        }

        switch (type)
        {
            case ALPHABETIC:
                assertThat(StringUtils.isAlpha(string), is(true));
                break;
            case ALPHANUMERIC:
                assertThat(StringUtils.isAlphanumeric(string), is(true));
                break;
            case HEXADECIMAL:
                assertThat(string.matches("[A-Fa-f0-9]+"), is(true));
                break;
            //No additional assertions
        }

    }

    @Test(expected = IllegalArgumentException.class)
    public void testValuesEdgeCases1()
    {
        System.out.println("testValuesEdgeCases");

        GenerateString.Values.createGeneratorFor(null);

    }


    @Test(expected = IllegalArgumentException.class)
    public void testValuesEdgeCases2()
    {
        System.out.println("testValuesEdgeCases");

        int badLength = one(negativeIntegers());
        annotation = new GenerateStringInstance(type, badLength);
        GenerateString.Values.createGeneratorFor(annotation);
    }


    @Test(expected = IllegalArgumentException.class)
    public void testValuesEdgeCases3()
    {
        System.out.println("testValuesEdgeCases");

        annotation = new GenerateStringInstance(null, length);
        GenerateString.Values.createGeneratorFor(annotation);
    }


    private static class GenerateStringInstance implements GenerateString
    {

        private final GenerateString.Type type;
        private final int length;

        public GenerateStringInstance(GenerateString.Type type, int length)
        {
            this.type = type;
            this.length = length;
        }

        @Override
        public Type value()
        {
            return type;
        }

        @Override
        public int length()
        {
            return length;
        }

        @Override
        public Class<? extends Annotation> annotationType()
        {
            return GenerateString.class;
        }

    }

}
