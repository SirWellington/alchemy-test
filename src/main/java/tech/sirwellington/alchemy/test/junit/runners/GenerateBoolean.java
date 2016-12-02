/*
 * Copyright 2016 RedRoma, Inc..
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
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
import tech.sirwellington.alchemy.generator.BooleanGenerators;

import static java.lang.annotation.ElementType.FIELD;
import static java.lang.annotation.RetentionPolicy.RUNTIME;

/**
 * Used in with the {@link AlchemyTestRunner}, this Annotations allows the 
 * Runtime Injection of Generated Booleans from the {@link AlchemyGenerator} library.
 * <p>
 * Example:
 * <pre>
 * {@code 
 * `@RunWith(AlchemyTestRunner.class)
 * public class ExampleTest
 * {
 *   `@GenerateBoolean
 *   private Boolean openNow;
 * 
 * }
 * }
 * </pre>
 * 
 * Note, '`' (ticks) used to escape Javadocs. 
 * @see GenerateInteger
 * @author SirWellington
 */
@Target(FIELD)
@Retention(RUNTIME)
public @interface GenerateBoolean 
{

    @Internal
    @NonInstantiable
    static class Values
    {
        private Values() throws IllegalAccessException
        {
            throw new IllegalAccessException("cannot instantiate");
        }
        
        static AlchemyGenerator<Boolean> createGeneratorFor(GenerateBoolean annotation)
        {
            return BooleanGenerators.booleans();
        }
    }
}
