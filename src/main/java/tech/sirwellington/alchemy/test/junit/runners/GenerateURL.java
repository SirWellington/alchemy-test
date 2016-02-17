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
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package tech.sirwellington.alchemy.test.junit.runners;

import java.lang.annotation.Retention;
import java.lang.annotation.Target;
import java.net.URL;
import tech.sirwellington.alchemy.annotations.access.Internal;
import tech.sirwellington.alchemy.annotations.access.NonInstantiable;
import tech.sirwellington.alchemy.generator.AlchemyGenerator;
import tech.sirwellington.alchemy.generator.NetworkGenerators;

import static java.lang.annotation.ElementType.FIELD;
import static java.lang.annotation.RetentionPolicy.RUNTIME;
import static tech.sirwellington.alchemy.test.Checks.Internal.checkNotNull;
import static tech.sirwellington.alchemy.test.Checks.Internal.checkThat;

/*
 * <pre>
 * 
 * {@code
 * `@RunWith(AlchemyTestRunner.class)
 * public class ExampleTest
 * {
 *   `@GenerateURL(HEXADECIMAL)
 *   private String username;
 * 
 *  ...
 * }
 * 
 * </pre>
 */
/**
 * Used in with the {@link AlchemyTestRunner}, this Annotations allows the 
 * Runtime Injection of Generated Strings from the {@link AlchemyGenerator} library.
 * <p>
 * Example:
 * <pre>
 * {@code 
 * `@RunWith(AlchemyTestRunner.class)
 * public class ExampleTest
 * {
 *   `@GenerateURL
 *   private URL weblink;
 * 
 * }
 * }
 * </pre>
 * 
 * Note, '`' (ticks) used to escape Javadocs. 
 * @see GenerateString
 * 
 * @author SirWellington
 */
@Target(FIELD)
@Retention(RUNTIME)
public @interface GenerateURL
{

   String protocol() default "http";

    @Internal
    @NonInstantiable
    static class Values
    {

        private Values() throws IllegalAccessException
        {
            throw new IllegalAccessException("cannot instantiate");
        }

        static AlchemyGenerator<URL> createGeneratorFor(GenerateURL annotation)
        {
            checkNotNull(annotation, "annotation is missing");
            
            String protocol = annotation.protocol();
            checkNotNull(protocol, "protocol cannot be null");
            checkThat(!protocol.isEmpty(), "protocol is empty");
            
            return NetworkGenerators.urlsWithProtocol(protocol);
        }
    }

}
