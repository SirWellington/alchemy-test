/*
 * Copyright © 2018. Sir Wellington.
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

import java.lang.annotation.Retention;
import java.lang.annotation.Target;

import tech.sirwellington.alchemy.annotations.access.Internal;
import tech.sirwellington.alchemy.annotations.access.NonInstantiable;
import tech.sirwellington.alchemy.generator.AlchemyGenerator;

import static java.lang.annotation.ElementType.FIELD;
import static java.lang.annotation.RetentionPolicy.RUNTIME;
import static tech.sirwellington.alchemy.generator.NumberGenerators.*;
import static tech.sirwellington.alchemy.test.Checks.Internal.checkNotNull;
import static tech.sirwellington.alchemy.test.Checks.Internal.checkThat;
import static tech.sirwellington.alchemy.test.junit.runners.GenerateDouble.Type.POSITIVE;
import static tech.sirwellington.alchemy.test.junit.runners.GenerateDouble.Type.RANGE;

/**
 * Used in with the {@link AlchemyTestRunner}, this Annotations allows the
 * Runtime Injection of Generated Doubles from the {@link AlchemyGenerator} library.
 * <p>
 * Example:
 * <pre>
 * {@code
 * `@RunWith(AlchemyTestRunner.class)
 * public class ExampleTest
 * {
 *   `@GenerateDouble(POSITIVE)
 *    private double percentage;
 *
 *    ...
 * }
 * }
 * </pre>
 * Note, ticks (`) used to escape Javadocs.
 *
 * @author SirWellington
 * @see GenerateInteger
 * @see GenerateLong
 * @see GenerateString
 */
@Target(FIELD)
@Retention(RUNTIME)
public @interface GenerateDouble
{

    Type value() default POSITIVE;

    double min() default 0.0;

    double max() default 1.0;

    public enum Type
    {
        POSITIVE,
        NEGATIVE,
        ANY,
        RANGE;
    }

    @Internal
    @NonInstantiable
    class Values
    {

        private Values() throws IllegalAccessException
        {
            throw new IllegalAccessException("cannot instantiate");
        }

        static AlchemyGenerator<Double> createGeneratorFor(GenerateDouble annotation)
        {
            checkNotNull(annotation, "missing annotation");

            Type type = annotation.value();
            checkNotNull(type, "@GenerateDouble missing value");

            if (type == RANGE)
            {
                double min = annotation.min();
                double max = annotation.max();
                checkThat(min < max, "@GenerateDouble: min must be less than max");
                return doubles(min, max);
            }

            //Cover remaining cases
            switch (type)
            {
                case POSITIVE:
                    return positiveDoubles();
                case NEGATIVE:
                    return negativeDoubles();
                default:
                    return anyDoubles();
            }
        }

    }

}
