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
import static org.hamcrest.Matchers.*;
import static tech.sirwellington.alchemy.generator.AlchemyGenerator.one;
import static tech.sirwellington.alchemy.generator.EnumGenerators.enumValueOf;
import static tech.sirwellington.alchemy.generator.NumberGenerators.longs;
import static tech.sirwellington.alchemy.test.ThrowableAssertion.assertThrows;
import static tech.sirwellington.alchemy.test.generation.GenerateLong.Type.RANGE;

/**
 * @author SirWellington
 */
public class GenerateLongTest {

    private GenerateLong.Type type;
    private long min;
    private long max;

    private GenerateLongInstance annotation;

    @BeforeEach
    public void setUp() {
        type = enumValueOf(GenerateLong.Type.class).get();
        min = one(longs(Long.MIN_VALUE, 1000));
        max = one(longs(1000, Long.MAX_VALUE));
        annotation = new GenerateLongInstance(type, min, max);
    }

    @Test
    public void testCannotInstantiate() throws IllegalAccessException, InstantiationException {
        System.out.println("testCannotInstantiate");

        assertThrows(
            () -> GenerateLong.Values.class.getDeclaredConstructor().newInstance()
        ).isInstanceOf(IllegalAccessException.class);
    }

    @Test
    public void testValues() {
        System.out.println("testValues");

        var result = GenerateLong.Values.createGeneratorFor(annotation);
        assertThat(result, notNullValue());

        var value = result.get();
        assertThat(value, notNullValue());

        if (type == RANGE) {
            assertThat(value, greaterThanOrEqualTo(min));
            assertThat(value, lessThan(max));
        }
        else {
            switch (type) {
                case POSITIVE -> assertThat(value, greaterThan(0L));
                case NEGATIVE -> assertThat(value, lessThan(0L));
            }
        }

    }

    @Test
    public void testValuesEdgeCases1() {
        System.out.println("testValuesEdgeCases");

        assertThrows(
            () -> GenerateLong.Values.createGeneratorFor(null)
        ).isInstanceOf(IllegalArgumentException.class);

    }

    @Test
    public void testValuesEdgeCases2() {
        System.out.println("testValuesEdgeCases");

        annotation = new GenerateLongInstance(null, min, max);
        assertThrows(
            () -> GenerateLong.Values.createGeneratorFor(annotation)
        ).isInstanceOf(IllegalArgumentException.class);
    }

    @Test
    public void testValuesEdgeCases3() {
        System.out.println("testValuesEdgeCases");

        long badMin = max;
        long badMax = min;
        type = RANGE;
        annotation = new GenerateLongInstance(type, badMin, badMax);
        assertThrows(
            () -> GenerateLong.Values.createGeneratorFor(annotation)
        ).isInstanceOf(IllegalArgumentException.class);
    }


    private record GenerateLongInstance(Type type, long min, long max) implements GenerateLong {
        @Override
        public Type value() {
            return type;
        }

        @Override
        public Class<? extends Annotation> annotationType() {
            return GenerateLong.class;
        }
    }
}
