/*
 * Copyright 2015 SirWellington Tech.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANYTIME KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package tech.sirwellington.alchemy.test.junit.runners;

import java.lang.annotation.Retention;
import java.lang.annotation.Target;
import java.util.Date;
import tech.sirwellington.alchemy.annotations.access.Internal;
import tech.sirwellington.alchemy.annotations.access.NonInstantiable;
import tech.sirwellington.alchemy.generator.AlchemyGenerator;
import tech.sirwellington.alchemy.generator.DateGenerators;

import static java.lang.annotation.ElementType.FIELD;
import static java.lang.annotation.RetentionPolicy.RUNTIME;
import static tech.sirwellington.alchemy.test.Checks.Internal.checkNotNull;
import static tech.sirwellington.alchemy.test.Checks.Internal.checkThat;

/**
 * Used in with the {@link AlchemyTestRunner}, this Annotations allows the Runtime Injection of Generated {@linkplain Date Dates}
 * using {@link DateGenerators} from the {@link AlchemyGenerator} library.
 *
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
 * @see GenerateString
 * @author SirWellington
 */
@Target(FIELD)
@Retention(RUNTIME)
public @interface GenerateDate
{

    Type value() default Type.ANYTIME;

    /**
     * If using the {@link Type#RANGE} type, specify a beginning date, in Epoch Millis.
     *
     * @return
     */
    long startDate() default 0;

    /**
     * If using the {@link Type#RANGE} type, specify an end date, in Epoch Millis.
     *
     * @return
     */
    long endDate() default 0;

    public enum Type
    {
        PAST,
        PRESENT,
        FUTURE,
        ANYTIME,
        RANGE
    }

    @Internal
    @NonInstantiable
    static class Values
    {

        private Values() throws IllegalAccessException
        {
            throw new IllegalAccessException("cannot instantiate");
        }

        static AlchemyGenerator<Date> createGeneratorFor(GenerateDate annotation) throws IllegalArgumentException
        {
            checkNotNull(annotation, "missing annotation");

            switch (annotation.value())
            {
                case PAST:
                    return DateGenerators.pastDates();
                case PRESENT:
                    return DateGenerators.presentDates();
                case FUTURE:
                    return DateGenerators.futureDates();
                case ANYTIME:
                    return DateGenerators.anyTime();
                case RANGE:
                    return datesInRange(annotation.startDate(), annotation.endDate());
                default:
                    return DateGenerators.anyTime();
            }
        }

        private static AlchemyGenerator<Date> datesInRange(long startDate, long endDate)
        {
            checkThat(startDate < endDate, "startDate must come before endDate");
            
            Date start = new Date(startDate);
            Date end = new Date(endDate);
            
            return DateGenerators.datesBetween(start, end);
        }
    }

}
