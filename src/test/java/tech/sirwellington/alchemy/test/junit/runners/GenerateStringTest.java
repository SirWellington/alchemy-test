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
import org.apache.commons.lang3.StringUtils;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import tech.sirwellington.alchemy.generator.AlchemyGenerator;
import tech.sirwellington.alchemy.generator.EnumGenerators;

import static org.hamcrest.Matchers.is;
import static org.hamcrest.Matchers.isEmptyOrNullString;
import static org.hamcrest.Matchers.not;
import static org.hamcrest.Matchers.notNullValue;
import static org.junit.Assert.assertThat;
import static tech.sirwellington.alchemy.generator.AlchemyGenerator.one;
import static tech.sirwellington.alchemy.generator.NumberGenerators.integers;
import static tech.sirwellington.alchemy.generator.NumberGenerators.negativeIntegers;
import static tech.sirwellington.alchemy.test.junit.ThrowableAssertion.assertThrows;
import static tech.sirwellington.alchemy.test.junit.runners.GenerateString.Type.UUID;

/**
 *
 * @author SirWellington
 */
@Repeat(100)
@RunWith(AlchemyTestRunner.class)
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
        annotation = new FakeAnnotation(type, length);

    }

    @DontRepeat
    @Test
    public void testCannotInstatiate()
    {
        assertThrows(() -> new GenerateString.Values())
            .isInstanceOf(IllegalAccessException.class);

        assertThrows(() -> GenerateString.Values.class.newInstance())
            .isInstanceOf(IllegalAccessException.class);
    }

    @Test
    public void testValues()
    {
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
    
    @Test
    public void testValuesEdgeCases()
    {
        assertThrows(() -> GenerateString.Values.createGeneratorFor(null))
            .isInstanceOf(IllegalArgumentException.class);
        
        int badLength = one(negativeIntegers());
        annotation = new FakeAnnotation(type, badLength);
        assertThrows(() -> GenerateString.Values.createGeneratorFor(annotation))
            .isInstanceOf(IllegalArgumentException.class);
            
        annotation = new FakeAnnotation(null, length);
        assertThrows(() -> GenerateString.Values.createGeneratorFor(annotation))
            .isInstanceOf(IllegalArgumentException.class);
    }

    private static class FakeAnnotation implements GenerateString
    {

        private final GenerateString.Type type;
        private final int length;

        public FakeAnnotation(GenerateString.Type type, int length)
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
