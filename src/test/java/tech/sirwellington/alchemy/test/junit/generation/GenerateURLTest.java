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

package tech.sirwellington.alchemy.test.junit.generation;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import tech.sirwellington.alchemy.generator.StringGenerators;

import java.lang.annotation.Annotation;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.notNullValue;
import static org.hamcrest.Matchers.startsWith;
import static tech.sirwellington.alchemy.generator.AlchemyGenerator.Get.one;
import static tech.sirwellington.alchemy.generator.StringGenerators.hexadecimalString;
import static tech.sirwellington.alchemy.test.junit.ThrowableAssertion.assertThrows;

/**
 * @author SirWellington
 */
public class GenerateURLTest {

    private GenerateURL annotation;
    private String protocol;

    @BeforeEach
    public void setUp() {
        protocol = StringGenerators.stringsFromFixedList("http", "https", "ftp", "file").get();
        annotation = new GenerateURLInstance(protocol);
    }

    @Test
    public void testCannotInstatiate() throws IllegalAccessException, InstantiationException {
        System.out.println("testCannotInstatiate");
        assertThrows(
            () -> GenerateURL.Values.class.newInstance()
        ).isInstanceOf(IllegalAccessException.class);
    }

    @Test
    public void testValues() {
        System.out.println("testValues");

        var result = GenerateURL.Values.createGeneratorFor(annotation);
        assertThat(result, notNullValue());

        var url = result.get();
        assertThat(url.toString(), startsWith(protocol));
    }

    @Test
    public void testValuesEdgeCases1() throws Exception {
        assertThrows(
            () -> GenerateURL.Values.createGeneratorFor(null)
        ).isInstanceOf(IllegalArgumentException.class);
    }

    @Test
    public void testValuesEdgeCases2() throws Exception {
        annotation = new GenerateURLInstance("");
        assertThrows(
            () -> GenerateURL.Values.createGeneratorFor(annotation)
        ).isInstanceOf(IllegalArgumentException.class);
    }

    @Test
    public void testValuesEdgeCases3() throws Exception {
        var badProtocol = one(hexadecimalString(3));
        annotation = new GenerateURLInstance(badProtocol);
        assertThrows(
            () -> GenerateURL.Values.createGeneratorFor(annotation)
        ).isInstanceOf(IllegalArgumentException.class);
    }

    private record GenerateURLInstance(String protocol) implements GenerateURL {

        @Override
        public Class<? extends Annotation> annotationType() {
            return GenerateURL.class;
        }
    }

}
