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
public class GenerateEnumTest {

    private enum Role {
        DEVELOPER,
        MANAGER,
        QA,
        OWNER,
        ARCHITECT
    }

    private GenerateEnum annotation;

    @BeforeEach
    public void setUp() {
        annotation = new FakeAnnotation();
    }

    @Test
    public void testCannotInstantiate() throws IllegalAccessException, InstantiationException {
        System.out.println("testCannotInstantiate");
        assertThrows(
            () -> GenerateEnum.Values.class.newInstance()
        ).isInstanceOf(IllegalAccessException.class);
    }


    @Test
    public void testValues() {
        System.out.println("testValues");

        var generator = GenerateEnum.Values.createGeneratorFor(annotation, Role.class);

        assertThat(generator, notNullValue());

        var result = generator.get();
        assertThat(result, notNullValue());
    }

    @Test
    public void testValuesWithBadArgs() {
        System.out.println("testValuesWithBadArgs");
        assertThrows(
            () -> GenerateEnum.Values.createGeneratorFor(null, null)
        ).isInstanceOf(IllegalArgumentException.class);
    }

    private static class FakeAnnotation implements GenerateEnum {

        @Override
        public Class<? extends Annotation> annotationType() {
            return GenerateEnum.class;
        }

    }
}