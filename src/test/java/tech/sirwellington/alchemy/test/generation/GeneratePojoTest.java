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

package tech.sirwellington.alchemy.test.generation;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import tech.sirwellington.alchemy.test.generation.GeneratePojo;

import java.lang.annotation.Annotation;
import java.util.Date;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.*;
import static tech.sirwellington.alchemy.test.ThrowableAssertion.assertThrows;

/**
 * @author SirWellington
 */
public class GeneratePojoTest {
    private GeneratePojoInstance annotation;

    @BeforeEach
    public void setUp() {
        annotation = new GeneratePojoInstance();
    }

    @Test
    public void testCannotInstantiate() throws IllegalAccessException, InstantiationException {
        System.out.println("testCannotInstantiate");
        assertThrows(
            () -> GeneratePojo.Values.class.getDeclaredConstructor().newInstance()
        ).isInstanceOf(IllegalAccessException.class);
    }

    @Test
    public void testValues() {
        System.out.println("testValues");

        var generator = GeneratePojo.Values.createGeneratorFor(annotation, SamplePojo.class);
        assertThat(generator, notNullValue());

        var result = generator.get();
        assertThat(result, notNullValue());
        assertThat(result.name, not(isEmptyOrNullString()));
        assertThat(result.age, greaterThan(0));
        assertThat(result.balance, greaterThan(0L));
        assertThat(result.birthday, notNullValue());

    }

    @Test
    public void testValuesEdgeCases1() {
        System.out.println("testValuesEdgeCases");

        assertThrows(
            () -> GeneratePojo.Values.createGeneratorFor(null, SamplePojo.class)
        ).isInstanceOf(IllegalArgumentException.class);
    }

    @Test
    public void testValuesEdgeCases2() {
        System.out.println("testValuesEdgeCases");

        assertThrows(
            () -> GeneratePojo.Values.createGeneratorFor(annotation, null)
        ).isInstanceOf(IllegalArgumentException.class);
    }

    private static class SamplePojo {
        private String name;
        private int age;
        private Date birthday;
        private long balance;
    }

    private static class GeneratePojoInstance implements GeneratePojo {
        @Override
        public Class<? extends Annotation> annotationType() {
            return GeneratePojo.class;
        }
    }
}