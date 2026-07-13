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

import java.lang.annotation.Retention;
import java.lang.annotation.Target;

import tech.sirwellington.alchemy.annotations.access.Internal;
import tech.sirwellington.alchemy.annotations.access.NonInstantiable;
import tech.sirwellington.alchemy.generator.AlchemyGenerator;
import tech.sirwellington.alchemy.test.AlchemyTest;

import static java.lang.annotation.ElementType.FIELD;
import static java.lang.annotation.RetentionPolicy.RUNTIME;
import static tech.sirwellington.alchemy.generator.NumberGenerators.longs;
import static tech.sirwellington.alchemy.generator.NumberGenerators.positiveLongs;
import static tech.sirwellington.alchemy.test.generation.GenerateLong.Type.POSITIVE;
import static tech.sirwellington.alchemy.test.generation.GenerateLong.Type.RANGE;
import static tech.sirwellington.alchemy.test.internal.Checks.checkNotNull;
import static tech.sirwellington.alchemy.test.internal.Checks.checkThat;

/**
 * Used in conjunction with the {@link AlchemyTest}, this Annotation allows the
 * Runtime Injection of Generated Longs from the {@link AlchemyGenerator} library.
 * <p>
 * Example:
 * {@snippet :
 * @AlchemyTest
 * public class ExampleTest {
 *   @GenerateLongs(POSITIVE)
 *   private long hits;
 * }
 *}
 *
 * @author SirWellington
 * @see GenerateInteger
 * @see GenerateString
 */
@Target(FIELD)
@Retention(RUNTIME)
public @interface GenerateLong {

    Type value() default POSITIVE;

    long min() default 0;

    long max() default 0;

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

        static AlchemyGenerator<Long> createGeneratorFor(GenerateLong annotation) {
            checkNotNull(annotation, "missing annotation");

            Type type = annotation.value();
            checkNotNull(type, "@GenerateLong missing value");

            if (type == RANGE) {
                long min = annotation.min();
                long max = annotation.max();
                checkThat(min < max, "@GenerateLong: min must be less than max");
                return longs(min, max);
            }

            //Cover remaining cases
            return switch (type) {
                case POSITIVE -> positiveLongs();
                case NEGATIVE -> longs(Long.MIN_VALUE, 0);
                default       -> longs(Long.MIN_VALUE, Long.MAX_VALUE);
            };
        }

    }

}
