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

package tech.sirwellington.alchemy.test.junit.runners;


import java.lang.annotation.Retention;
import java.lang.annotation.Target;

import tech.sirwellington.alchemy.annotations.access.Internal;
import tech.sirwellington.alchemy.annotations.access.NonInstantiable;
import tech.sirwellington.alchemy.generator.AlchemyGenerator;

import static java.lang.annotation.ElementType.FIELD;
import static java.lang.annotation.RetentionPolicy.RUNTIME;
import static tech.sirwellington.alchemy.test.Checks.Internal.checkNotNull;

/**
 * Used in with the {@link AlchemyTestRunner}, this Annotations allows the
 * Runtime Injection of Custom Objects using the {@link AlchemyGenerator} library.
 * <p>
 * Example:
 * <pre>
 * {@code
 * `@RunWith(AlchemyTestRunner.class)
 * public class ExampleTest
 * {
 *   `@GenerateCustom(type=Book.class, generator=BookGenerator.class)
 *   private Book book;
 *
 * }
 * }
 * </pre>
 * <p>
 * Note, '`' (ticks) used to escape Javadocs.
 *
 * @author SirWellington
 * @author SirWellington
 */
@Target(FIELD)
@Retention(RUNTIME)
public @interface GenerateCustom
{

    /**
     * Specify the Java Class to use to generate values. This class must
     * be an {@link AlchemyGenerator}.
     */
    Class<? extends AlchemyGenerator<?>> value();

    @Internal
    @NonInstantiable
    static class Values
    {

        private Values() throws IllegalAccessException
        {
            throw new IllegalAccessException("cannot instantiate");
        }

        static AlchemyGenerator<?> createGeneratorFor(GenerateCustom annotation) throws IllegalArgumentException
        {
            checkNotNull(annotation, "missing annotation");

            Class<? extends AlchemyGenerator<?>> generatorClass = annotation.value();

            final AlchemyGenerator<?> generator = tryToInstantiate(generatorClass);

            return generator;
        }

        private static AlchemyGenerator<?> tryToInstantiate(Class<? extends AlchemyGenerator<?>> generatorClass)
        {
            try
            {
                return generatorClass.newInstance();
            }
            catch (Throwable ex)
            {
                throw new IllegalArgumentException("Cannot instantiate Alchemy Generator | " + generatorClass, ex);
            }
        }

    }
}