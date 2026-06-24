/*
 * Copyright © 2019. Sir Wellington.
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

package tech.sirwellington.alchemy.test.junit.generation;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import tech.sirwellington.alchemy.generator.EnumGenerators;

import java.lang.annotation.Annotation;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.*;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static tech.sirwellington.alchemy.generator.AlchemyGenerator.Get.one;
import static tech.sirwellington.alchemy.generator.NumberGenerators.integers;
import static tech.sirwellington.alchemy.generator.NumberGenerators.negativeIntegers;
import static tech.sirwellington.alchemy.test.junit.ThrowableAssertion.assertThrows;
import static tech.sirwellington.alchemy.test.junit.generation.GenerateString.Type.UUID;

/**
 * @author SirWellington
 */
public class GenerateStringTest {
    private GenerateString annotation;
    private GenerateString.Type type;
    private int length;

    @BeforeEach
    public void setUp() {
        type = EnumGenerators.enumValueOf(GenerateString.Type.class).get();
        length = one(integers(5, 500));
        annotation = new GenerateStringInstance(type, length);

    }

    @Test
    public void testCannotInstantiate() {
        System.out.println("testCannotInstantiate");

        assertThrows(
            () -> GenerateString.Values.class.getDeclaredConstructor().newInstance()
        ).isInstanceOf(IllegalAccessException.class);
    }

    @Test
    public void testValues() {
        System.out.println("testValues");

        var result = GenerateString.Values.createGeneratorFor(annotation);
        assertThat(result, notNullValue());

        var string = result.get();
        assertThat(string, not(isEmptyOrNullString()));

        if (type == UUID) {
            int uuidLength = java.util.UUID.randomUUID().toString().length();
            assertThat(string.length(), is(uuidLength));
        }
        else {
            assertThat(string.length(), is(length));
        }

        switch (type) {
            case ALPHABETIC:
                assertTrue(isAlphabetic(string));
                break;
            case ALPHANUMERIC:
                assertTrue(isAlphanumeric(string));
                break;
            case HEXADECIMAL:
                assertThat(string.matches("[A-Fa-f0-9]+"), is(true));
                break;
        }

    }

    private boolean isAlphabetic(String string) {
        if (string == null) return false;
        return string.chars().allMatch(Character::isAlphabetic);
    }

    private boolean isAlphanumeric(String string) {
        if (string == null) return false;
        return string.chars().allMatch(Character::isLetterOrDigit);
    }

    @Test
    public void testValuesEdgeCases1() {
        System.out.println("testValuesEdgeCases");
        assertThrows(
            () -> GenerateString.Values.createGeneratorFor(null)
        ).isInstanceOf(IllegalArgumentException.class);
    }

    @Test
    public void testValuesEdgeCases2() {
        System.out.println("testValuesEdgeCases");

        int badLength = one(negativeIntegers());
        annotation = new GenerateStringInstance(type, badLength);
        assertThrows(
            () -> GenerateString.Values.createGeneratorFor(annotation)
        ).isInstanceOf(IllegalArgumentException.class);
    }

    @Test
    public void testValuesEdgeCases3() {
        System.out.println("testValuesEdgeCases");

        annotation = new GenerateStringInstance(null, length);
        assertThrows(
            () -> GenerateString.Values.createGeneratorFor(annotation)
        ).isInstanceOf(IllegalArgumentException.class);
    }


    private record GenerateStringInstance(Type type, int length) implements GenerateString {

        @Override
            public Type value() {
                return type;
            }

            @Override
            public Class<? extends Annotation> annotationType() {
                return GenerateString.class;
            }
        }

}
