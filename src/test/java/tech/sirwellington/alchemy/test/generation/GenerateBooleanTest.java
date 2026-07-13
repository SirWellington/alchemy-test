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


package tech.sirwellington.alchemy.test.generation;


import java.lang.annotation.Annotation;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.notNullValue;
import static tech.sirwellington.alchemy.test.ThrowableAssertion.assertThrows;

/**
 * @author SirWellington
 */
public class GenerateBooleanTest {
    private GenerateBoolean annotation;


    @BeforeEach
    public void setUp() {
        annotation = new BasicAnnotation();
    }

    @Test
    public void testCannotInstantiate() throws IllegalAccessException, InstantiationException {
        System.out.println("testCannotInstantiate");

        assertThrows(
            () -> GenerateBoolean.Values.class.getDeclaredConstructor().newInstance()
        ).isInstanceOf(IllegalAccessException.class);
    }

    @Test
    public void testValues() {
        System.out.println("testValues");

        var generator = GenerateBoolean.Values.createGeneratorFor(annotation);
        assertThat(generator, notNullValue());

        var value = generator.get();
        assertThat(value, notNullValue());
    }

    @Test
    public void testValuesEdgeCases() {
        System.out.println("testValuesEdgeCases");

        assertThrows(
            () -> GenerateBoolean.Values.createGeneratorFor(null)
        ).isInstanceOf(IllegalArgumentException.class);
    }

    private static class BasicAnnotation implements GenerateBoolean {

        public BasicAnnotation() { }

        @Override
        public Class<? extends Annotation> annotationType() {
            return GenerateBoolean.class;
        }
    }

}
