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

package tech.sirwellington.alchemy.test.generation;

import tech.sirwellington.alchemy.annotations.access.Internal;
import tech.sirwellington.alchemy.annotations.access.NonInstantiable;
import tech.sirwellington.alchemy.generator.AlchemyGenerator;
import tech.sirwellington.alchemy.test.AlchemyTest;

import java.lang.annotation.Retention;
import java.lang.annotation.Target;

import static java.lang.annotation.ElementType.FIELD;
import static java.lang.annotation.RetentionPolicy.RUNTIME;
import static tech.sirwellington.alchemy.generator.NumberGenerators.*;
import static tech.sirwellington.alchemy.test.internal.Checks.checkNotNull;
import static tech.sirwellington.alchemy.test.internal.Checks.checkThat;

/**
 * Used with the {@link AlchemyTest}, this Annotation allows the
 * Runtime Injection of Generated Doubles from the {@link AlchemyGenerator} library.
 * <p>
 * Example:
 * {@snippet :
 * import tech.sirwellington.alchemy.test.AlchemyTest;@AlchemyTest
 * public class ExampleTest {
 *   @GenerateDouble(POSITIVE)
 *   private double percentage;
 * }
 *}
 *
 * @author SirWellington
 * @see GenerateInteger
 * @see GenerateLong
 * @see GenerateString
 */
@Target(FIELD)
@Retention(RUNTIME)
public @interface GenerateFloat {

    Type value() default Type.POSITIVE;

    float min() default 0.0f;

    float max() default 1.0f;

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

        static AlchemyGenerator<Float> createGeneratorFor(GenerateFloat annotation) {
            checkNotNull(annotation, "missing annotation");

            Type type = annotation.value();
            checkNotNull(type, "@GenerateDouble missing value");

            if (type == Type.RANGE) {
                float min = annotation.min();
                float max = annotation.max();
                checkThat(min < max, "@GenerateDouble: min must be less than max");

                final var doubles = doubles(min, max);

                return () -> doubles.get().floatValue();
            }

            //Cover remaining cases
            return switch (type) {
                case POSITIVE -> positiveFloats();
                case NEGATIVE -> negativeFloats();
                default       -> anyFloats();
            };
        }

    }

}
