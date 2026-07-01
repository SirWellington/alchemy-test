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

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import tech.sirwellington.alchemy.test.generation.GenerateInteger;

import java.lang.annotation.Annotation;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.*;
import static tech.sirwellington.alchemy.generator.AlchemyGenerator.Get.one;
import static tech.sirwellington.alchemy.generator.EnumGenerators.enumValueOf;
import static tech.sirwellington.alchemy.generator.NumberGenerators.integers;
import static tech.sirwellington.alchemy.test.ThrowableAssertion.assertThrows;
import static tech.sirwellington.alchemy.test.generation.GenerateInteger.Type.RANGE;

/**
 * @author SirWellington
 */
public class GenerateIntegerTest {
    private GenerateInteger.Type type;
    private int min;
    private int max;

    private GenerateIntegerInstance annotation;

    @BeforeEach
    public void setUp() {
        type = enumValueOf(GenerateInteger.Type.class).get();
        min = one(integers(-1000, 1000));
        max = one(integers(1000, 100_000));
        annotation = new GenerateIntegerInstance(type, min, max);
    }

    @Test
    public void testCannotInstantiate() throws IllegalAccessException, InstantiationException {
        System.out.println("testCannotInstantiate");

        assertThrows(
            () -> GenerateInteger.Values.class.getDeclaredConstructor().newInstance()
        ).isInstanceOf(IllegalAccessException.class);
    }

    @Test
    public void testValues() {
        System.out.println("testValues");

        var result = GenerateInteger.Values.createGeneratorFor(annotation);
        assertThat(result, notNullValue());

        var integer = result.get();
        assertThat(integer, notNullValue());

        if (type == RANGE) {
            assertThat(integer, greaterThanOrEqualTo(min));
            assertThat(integer, lessThan(max));
        }
        else {
            switch (type) {
                case POSITIVE -> assertThat(integer, greaterThan(0));
                case NEGATIVE -> assertThat(integer, lessThan(0));
            }
        }

    }

    @Test
    public void testValuesEdgeCases1() throws Exception {
        assertThrows(
            () -> GenerateInteger.Values.createGeneratorFor(null)
        ).isInstanceOf(IllegalArgumentException.class);
    }

    @Test
    public void testValuesEdgeCases2() throws Exception {
        annotation = new GenerateIntegerInstance(null, min, max);
        assertThrows(
            () -> GenerateInteger.Values.createGeneratorFor(annotation)
        ).isInstanceOf(IllegalArgumentException.class);
    }

    @Test
    public void testValuesEdgeCases3() throws Exception {
        int badMin = max;
        int badMax = min;
        type = RANGE;
        annotation = new GenerateIntegerInstance(type, badMin, badMax);

        assertThrows(
            () -> GenerateInteger.Values.createGeneratorFor(annotation)
        ).isInstanceOf(IllegalArgumentException.class);
    }

    private record GenerateIntegerInstance(Type type, int min, int max) implements GenerateInteger {

        @Override
        public Type value() {
            return type;
        }

        @Override
        public Class<? extends Annotation> annotationType() {
            return GenerateInteger.class;
        }
    }

}
