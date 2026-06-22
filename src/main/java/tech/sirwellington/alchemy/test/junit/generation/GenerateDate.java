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
import tech.sirwellington.alchemy.generator.DateGenerators;

import java.lang.annotation.Retention;
import java.lang.annotation.Target;
import java.util.Date;

import static java.lang.annotation.ElementType.FIELD;
import static java.lang.annotation.RetentionPolicy.RUNTIME;
import static tech.sirwellington.alchemy.test.internal.Checks.checkNotNull;
import static tech.sirwellington.alchemy.test.internal.Checks.checkThat;

/**
 * Used in conjunction with the {@link AlchemyTestRunner}, this Annotations allows the Runtime Injection of Generated {@linkplain Date Dates}
 * using {@link DateGenerators} from the {@link AlchemyGenerator} library.
 * <p>
 * Example:
 * <pre>
 * {@code
 * `@RunWith(AlchemyTestRunner.class)
 * public class ExampleTest
 * {
 *   `@GenerateDate(ANYTIME)
 *    private Date dateOfOrder;
 *
 *    ...
 * }
 * }
 * </pre>
 * Note, ticks (`) used to escape Javadocs.
 *
 * @author SirWellington
 * @see GenerateString
 */
@Target(FIELD)
@Retention(RUNTIME)
public @interface GenerateDate {

    Type value() default Type.ANYTIME;

    /**
     * If using the {@link Type#RANGE} type, specify a beginning date, in Epoch Millis.
     */
    long startDate() default 0;

    /**
     * If using the {@link Type#RANGE} type, specify an end date, in Epoch Millis.
     */
    long endDate() default 0;

    enum Type {
        PAST,
        PRESENT,
        FUTURE,
        ANYTIME,
        RANGE
    }

    @Internal
    @NonInstantiable
    class Values {

        private Values() throws IllegalAccessException {
            throw new IllegalAccessException("cannot instantiate");
        }

        static AlchemyGenerator<Date> createGeneratorFor(GenerateDate annotation) throws IllegalArgumentException {
            checkNotNull(annotation, "missing annotation");

            return switch (annotation.value()) {
                case PAST -> DateGenerators.pastDates();
                case PRESENT -> DateGenerators.presentDates();
                case FUTURE -> DateGenerators.futureDates();
                case ANYTIME -> DateGenerators.anyTime();
                case RANGE -> datesInRange(annotation.startDate(), annotation.endDate());
            };
        }

        private static AlchemyGenerator<Date> datesInRange(long startDate, long endDate) {
            checkThat(startDate < endDate, "startDate must come before endDate");
            var start = new Date(startDate);
            var end = new Date(endDate);
            return DateGenerators.datesBetween(start, end);
        }
    }

}
