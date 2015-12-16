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
import java.time.Instant;
import tech.sirwellington.alchemy.annotations.access.Internal;
import tech.sirwellington.alchemy.annotations.access.NonInstantiable;
import tech.sirwellington.alchemy.generator.AlchemyGenerator;
import tech.sirwellington.alchemy.generator.TimeGenerators;

import static java.lang.annotation.ElementType.FIELD;
import static java.lang.annotation.RetentionPolicy.RUNTIME;
import static tech.sirwellington.alchemy.test.Checks.Internal.checkNotNull;
import static tech.sirwellington.alchemy.test.Checks.Internal.checkThat;

/**
 * Used in with the {@link AlchemyTestRunner}, this Annotations allows the Runtime Injection of 
 * Generated {@linkplain Instant Instants}  using {@link TimeGenerators} from the {@link AlchemyGenerator} library.
 *
 * Example:
 * <pre>
 * {@code
 * `@RunWith(AlchemyTestRunner.class)
 * public class ExampleTest
 * {
 *   `@GenerateInstant(ANYTIME)
 *    private Instant timeOfOrder;
 *
 *    ...
 * }
 * }
 * </pre> 
 * Note, ticks (`) used to escape Javadocs. 
 * 
 * @see GenerateString
 * @see GenerateDate
 * 
 * @author SirWellington
 */
@Target(FIELD)
@Retention(RUNTIME)
public @interface GenerateInstant
{

    Type value() default Type.ANYTIME;

    /**
     * If using the {@link Type#RANGE} type, specify a beginning time, in Epoch Millis.
     *
     * @return
     */
    long startTime() default 0;

    /**
     * If using the {@link Type#RANGE} type, specify an end time, in Epoch Millis.
     *
     * @return
     */
    long endTime() default 0;

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

        static AlchemyGenerator<Instant> createGeneratorFor(GenerateInstant annotation) throws IllegalArgumentException
        {
            checkNotNull(annotation, "missing annotation");

            switch (annotation.value())
            {
                case PAST:
                    return TimeGenerators.pastInstants();
                case PRESENT:
                    return TimeGenerators.presentInstants();
                case FUTURE:
                    return TimeGenerators.futureInstants();
                case ANYTIME:
                    return TimeGenerators.anytime();
                case RANGE:
                    return timesInRange(annotation.startTime(), annotation.endTime());
                default:
                    return TimeGenerators.anytime();
            }
        }

        private static AlchemyGenerator<Instant> timesInRange(long startTime, long endDate)
        {
            checkThat(startTime < endDate, "startDate must come before endDate");
            
            Instant start = Instant.ofEpochMilli(startTime);
            Instant end = Instant.ofEpochMilli(endDate);
            
            return TimeGenerators.timesBetween(start, end);
        }
    }

}
