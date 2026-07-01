/*
 * Copyright © 2026. Sir Wellington.
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

import tech.sirwellington.alchemy.annotations.access.Internal;
import tech.sirwellington.alchemy.annotations.access.NonInstantiable;
import tech.sirwellington.alchemy.generator.AlchemyGenerator;
import tech.sirwellington.alchemy.test.junit.AlchemyTest;

import java.lang.annotation.Retention;
import java.lang.annotation.Target;

import static java.lang.annotation.ElementType.FIELD;
import static java.lang.annotation.RetentionPolicy.RUNTIME;
import static tech.sirwellington.alchemy.generator.NumberGenerators.*;
import static tech.sirwellington.alchemy.test.internal.Checks.checkNotNull;
import static tech.sirwellington.alchemy.test.internal.Checks.checkThat;
import static tech.sirwellington.alchemy.test.junit.generation.GenerateInteger.Type.POSITIVE;
import static tech.sirwellington.alchemy.test.junit.generation.GenerateInteger.Type.RANGE;

/**
 * Used in conjunction with the {@link AlchemyTest}, this Annotation allows the
 * Runtime Injection of Generated Integers from the {@link AlchemyGenerator} library.
 * <p>
 * Example:
 * {@snippet :
 * @AlchemyTest
 * public class ExampleTest {
 *   @GenerateInteger(POSITIVE)
 *   private int size;
 * }
 * }
 *
 * @author SirWellington
 * @see GenerateString
 * @see GenerateLong
 * @see GenerateDouble
 */
@Target(FIELD)
@Retention(RUNTIME)
public @interface GenerateInteger {

    Type value() default POSITIVE;

    int min() default 0;

    int max() default 0;

    enum Type {
        POSITIVE,
        NEGATIVE,
        ANY,
        RANGE;
    }

    @Internal
    @NonInstantiable
    class Values {

        private Values() throws IllegalAccessException {
            throw new IllegalAccessException("cannot instantiate");
        }

        static AlchemyGenerator<Integer> createGeneratorFor(GenerateInteger annotation) {
            checkNotNull(annotation, "missing annotation");

            Type type = annotation.value();
            checkNotNull(type, "@GenerateInteger missing value");

            if (type == RANGE) {
                int min = annotation.min();
                int max = annotation.max();
                checkThat(min < max, "@GenerateInteger: min must be less than max");
                return integers(min, max);
            }

            //Cover remaining cases
            return switch (type) {
                case POSITIVE -> positiveIntegers();
                case NEGATIVE -> negativeIntegers();
                default       -> integers(Integer.MIN_VALUE, Integer.MAX_VALUE);
            };
        }

    }

}
