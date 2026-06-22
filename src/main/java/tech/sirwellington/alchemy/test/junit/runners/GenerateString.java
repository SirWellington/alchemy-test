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

package tech.sirwellington.alchemy.test.junit.runners;

import tech.sirwellington.alchemy.annotations.access.Internal;
import tech.sirwellington.alchemy.annotations.access.NonInstantiable;
import tech.sirwellington.alchemy.generator.AlchemyGenerator;

import java.lang.annotation.Retention;
import java.lang.annotation.Target;
import java.util.UUID;

import static java.lang.annotation.ElementType.FIELD;
import static java.lang.annotation.RetentionPolicy.RUNTIME;
import static tech.sirwellington.alchemy.generator.StringGenerators.*;
import static tech.sirwellington.alchemy.test.internal.Checks.checkNotNull;
import static tech.sirwellington.alchemy.test.internal.Checks.checkThat;

/*
 * <pre>
 *
 * {@code
 * `@RunWith(AlchemyTestRunner.class)
 * public class ExampleTest
 * {
 *   `@GenerateString(HEXADECIMAL)
 *   private String username;
 *
 *  ...
 * }
 *
 * </pre>
 */

/**
 * Used in conjunction with the {@link AlchemyTestRunner}, this Annotations allows the
 * Runtime Injection of Generated Strings from the {@link AlchemyGenerator} library.
 * <p>
 * Example:
 * <pre>
 * {@code
 * `@RunWith(AlchemyTestRunner.class)
 * public class ExampleTest
 * {
 *   `@GenerateString(HEXADECIMAL)
 *   private String username;
 *
 * }
 * }
 * </pre>
 * <p>
 * Note, '`' (ticks) used to escape Javadocs.
 *
 * @author SirWellington
 * @see GenerateInteger
 */
@Target(FIELD)
@Retention(RUNTIME)
public @interface GenerateString {

    /*
     * Named value because it allows for @StringGenerator(ALPHABETIC) instead of @StringGenerator(type = ALPHABETIC)
     */

    /**
     * The type of String to Generate
     */
    Type value() default Type.ALPHABETIC;

    /**
     * The length of the string, must be {@code > 0}.
     */
    int length() default 10;

    enum Type {
        ALPHABETIC,
        ALPHANUMERIC,
        HEXADECIMAL,
        NUMERIC,
        /**
         * For UUIDS, the {@link #length() } property will be ignored, and instead the standard UUID size from {@link UUID#randomUUID()
         * } is used.
         */
        UUID

    }

    @Internal
    @NonInstantiable
    class Values {

        private Values() throws IllegalAccessException {
            throw new IllegalAccessException("cannot instantiate");
        }

        static AlchemyGenerator<String> createGeneratorFor(GenerateString annotation) {
            checkNotNull(annotation, "annotation is missing");

            var length = annotation.length();
            checkThat(length > 0, "Invalid @GenerateString use, length must be positive");

            var type = annotation.value();
            checkNotNull(type, "@GenerateString Annotation missing type");

            return switch (type) {
                case ALPHABETIC   -> alphabeticStrings(length);
                case ALPHANUMERIC -> alphanumericStrings(length);
                case HEXADECIMAL  -> hexadecimalString(length);
                case NUMERIC      -> numericStrings(length);
                case UUID         -> UUIDS;
            };

        }
    }

}
