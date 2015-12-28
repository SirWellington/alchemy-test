/*
 * Copyright 2015 Aroma Tech.
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

import java.lang.annotation.Annotation;
import java.net.URL;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.runners.MockitoJUnitRunner;
import tech.sirwellington.alchemy.generator.AlchemyGenerator;
import tech.sirwellington.alchemy.generator.StringGenerators;

import static org.hamcrest.Matchers.*;
import static org.junit.Assert.*;
import static tech.sirwellington.alchemy.generator.AlchemyGenerator.one;
import static tech.sirwellington.alchemy.generator.StringGenerators.hexadecimalString;
import static tech.sirwellington.alchemy.test.junit.ThrowableAssertion.assertThrows;

/**
 *
 * @author SirWellington
 */
@RunWith(MockitoJUnitRunner.class)
public class GenerateURLTest
{

    private GenerateURL annotation;
    private String protocol;

    @Before
    public void setUp()
    {
        protocol = StringGenerators.stringsFromFixedList("http", "https", "ftp", "ssh").get();

        annotation = new GenerateURLInstance(protocol);

    }

    @Test
    public void testCannotInstatiate()
    {
        System.out.println("testCannotInstatiate");

        assertThrows(() -> GenerateURL.Values.class.newInstance())
            .isInstanceOf(IllegalAccessException.class);
    }

    @Test
    public void testValues()
    {
        System.out.println("testValues");

        AlchemyGenerator<URL> result = GenerateURL.Values.createGeneratorFor(annotation);
        assertThat(result, notNullValue());

        URL url = result.get();
        assertThat(url.toString(), startsWith(protocol));
    }

    @Test
    public void testValuesEdgeCases()
    {
        System.out.println("testValuesEdgeCases");

        assertThrows(() -> GenerateURL.Values.createGeneratorFor(null))
            .isInstanceOf(IllegalArgumentException.class);

        annotation = new GenerateURLInstance("");
        assertThrows(() -> GenerateURL.Values.createGeneratorFor(annotation))
            .isInstanceOf(IllegalArgumentException.class);

        String badProtocol = one(hexadecimalString(3));
        annotation = new GenerateURLInstance(badProtocol);
        assertThrows(() -> GenerateURL.Values.createGeneratorFor(annotation))
            .isInstanceOf(IllegalArgumentException.class);

    }

    private static class GenerateURLInstance implements GenerateURL
    {

        private final String protocol;

        private GenerateURLInstance(String protocol)
        {
            this.protocol = protocol;
        }

        @Override
        public String protocol()
        {
            return protocol;
        }

        @Override
        public Class<? extends Annotation> annotationType()
        {
            return GenerateURL.class;
        }

    }

}
