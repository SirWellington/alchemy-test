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
import java.nio.ByteBuffer;
import java.time.Instant;
import java.util.Date;
import java.util.List;

import tech.sirwellington.alchemy.annotations.access.Internal;
import tech.sirwellington.alchemy.annotations.access.NonInstantiable;
import tech.sirwellington.alchemy.generator.*;

import static java.lang.annotation.ElementType.FIELD;
import static java.lang.annotation.RetentionPolicy.RUNTIME;
import static tech.sirwellington.alchemy.generator.CollectionGenerators.listOf;
import static tech.sirwellington.alchemy.generator.NumberGenerators.positiveIntegers;
import static tech.sirwellington.alchemy.generator.NumberGenerators.positiveLongs;
import static tech.sirwellington.alchemy.generator.ObjectGenerators.pojos;
import static tech.sirwellington.alchemy.generator.StringGenerators.alphanumericStrings;
import static tech.sirwellington.alchemy.test.Checks.Internal.checkNotNull;
import static tech.sirwellington.alchemy.test.Checks.Internal.checkThat;

/**
 * Used in with the {@link AlchemyTestRunner}, this Annotations allows the Runtime Injection of {@link List} values, using
 * {@link CollectionGenerators} from the {@link AlchemyGenerator} library.
 * <p>
 * Example:
 * <pre>
 * {@code
 * `@RunWith(AlchemyTestRunner.class)
 *  public class ExampleTest
 *  {
 *
 *    `@GenerateList(String.class)
 *     private List<String> ids;
 *
 *    ...
 *  }
 * }
 * </pre> Note, ticks (`) used to escape Javadocs.
 *
 * @author SirWellington
 * @see GenerateEnum
 * @see GeneratePojo
 */
@Target(FIELD)
@Retention(RUNTIME)
public @interface GenerateList
{

    /**
     * Specify the Generic Type of the List. This is necessary since the type information is erased at Runtime.
     *
     * @return
     */
    Class<?> value();

    /**
     * The number of elements to include in the list. Defaults to 10. This number must be {@code > 0}.
     *
     * @return
     */
    int size() default 10;

    @Internal
    @NonInstantiable
    static class Values
    {

        private Values() throws IllegalAccessException
        {
            throw new IllegalAccessException("cannot instantiate");
        }

        static AlchemyGenerator<List<?>> createGeneratorFor(GenerateList annotation) throws IllegalArgumentException
        {
            checkNotNull(annotation, "missing annotation");
            final int size = annotation.size();
            checkThat(size > 0, "size must be > 0");

            Class<?> genericType = annotation.value();
            checkNotNull(genericType, "annotation is missing generic type information");

            final AlchemyGenerator<?> generator = determineGeneratorFor(genericType);

            return new AlchemyGenerator<List<?>>()
            {
                @Override
                public List<?> get()
                {
                    return listOf(generator, size);
                }
            };
        }

        private static AlchemyGenerator<?> determineGeneratorFor(Class<?> genericType)
        {
            if (genericType == String.class)
            {
                return alphanumericStrings();
            }

            if (genericType == Integer.class)
            {
                return positiveIntegers();
            }

            if (genericType == Long.class)
            {
                return positiveLongs();
            }

            if (genericType == Double.class)
            {
                return NumberGenerators.doubles(0, 10000);
            }

            if (Date.class.isAssignableFrom(genericType))
            {
                return DateGenerators.anyTime();
            }

            if (genericType == Instant.class)
            {
                return TimeGenerators.anytime();
            }

            if (genericType == Boolean.class)
            {
                return BooleanGenerators.booleans();
            }

            if (ByteBuffer.class.isAssignableFrom(genericType))
            {
                return BinaryGenerators.byteBuffers(1024);
            }

            return pojos(genericType);
        }
    }

}
