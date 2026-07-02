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

import java.lang.annotation.Annotation;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import tech.sirwellington.alchemy.generator.AlchemyGenerator;
import tech.sirwellington.alchemy.generator.StringGenerators;
import tech.sirwellington.alchemy.test.generation.GenerateList;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.*;
import static tech.sirwellington.alchemy.generator.AlchemyGenerator.Get.one;
import static tech.sirwellington.alchemy.generator.NumberGenerators.integers;
import static tech.sirwellington.alchemy.generator.NumberGenerators.negativeIntegers;
import static tech.sirwellington.alchemy.test.ThrowableAssertion.assertThrows;

/**
 * @author SirWellington
 */
public class GenerateListTest {

    private GenerateListAnnotation annotation;

    private int size;
    private final Class<String> genericType = String.class;
    private final Class<? extends AlchemyGenerator<?>> customGenerator = CustomStringGenerator.class;

    private static String randomString = "";

    @BeforeEach
    public void setUp() {
        size = one(integers(5, 100));
        annotation = new GenerateListAnnotation(genericType, size, null);
        randomString = StringGenerators.alphanumericStrings().get();
    }

    @Test
    public void testCannotInstantiate() {
        System.out.println("testCannotInstantiate");

        assertThrows(
            () -> GenerateList.Values.class.getDeclaredConstructor().newInstance()
        ).isInstanceOf(IllegalAccessException.class);
    }

    @Test
    public void testValues() {
        System.out.println("testValues");
        var generator = GenerateList.Values.createGeneratorFor(annotation);
        assertThat(generator, notNullValue());

        var list = generator.get();
        assertThat(list, notNullValue());
        assertThat(list, not(empty()));

        for (Object element : list) {
            assertThat(element, is(instanceOf(genericType)));
        }
    }

    @Test
    public void testValuesWithCustomGenerator() throws Exception {
        System.out.println("testValuesWithCustomGenerator");

        annotation.customGenerator = this.customGenerator;

        var generator = GenerateList.Values.createGeneratorFor(annotation);
        assertThat(generator, notNullValue());

        var list = generator.get();
        assertThat(list, notNullValue());
        assertThat(list, not(empty()));

        for (Object element : list) {
            assertThat(element, instanceOf(String.class));
            assertThat(element.toString(), equalTo(randomString));
        }
    }

    @Test
    public void testValuesWithNullAnnotation() {
        System.out.println("testValuesEdgeCases1");

        assertThrows(
            () -> GenerateList.Values.createGeneratorFor(null)
        ).isInstanceOf(IllegalArgumentException.class);
    }

    @Test
    public void testValuesWithNegativeSize() {
        System.out.println("testValuesEdgeCases2");

        annotation.size = one(negativeIntegers());
        assertThrows(
            () -> GenerateList.Values.createGeneratorFor(annotation)
        ).isInstanceOf(IllegalArgumentException.class);
    }

    @Test
    public void testValuesWithCustomGeneratorThatCannotBeInstantiated() throws Exception {
        System.out.println("testValuesWithCustomGeneratorThatCannotBeInstantiated");

        annotation.customGenerator = GeneratorThatCannotBeInstantiated.class;

        assertThrows(
            () -> GenerateList.Values.createGeneratorFor(annotation)
        ).isInstanceOf(IllegalArgumentException.class);
    }

    private static class GenerateListAnnotation<T> implements GenerateList {
        private Class<T> generictype;
        private int size;
        private Class<? extends AlchemyGenerator<?>> customGenerator;

        private GenerateListAnnotation(
            Class<T> generictype, int size, Class<? extends AlchemyGenerator<?>> customGenerator) {
            this.generictype = generictype;
            this.size = size;
            this.customGenerator = customGenerator;
        }

        @Override
        public Class<? extends Annotation> annotationType() {
            return GenerateList.class;
        }

        @Override
        public Class<?> value() {
            return generictype;
        }

        @Override
        public int size() {
            return size;
        }

        @Override
        public Class<? extends AlchemyGenerator<?>> customGenerator() {
            return customGenerator;
        }

        @Override
        public String toString() {
            return "GenerateListAnnotation{" +
                "generictype=" + generictype +
                ", size=" + size +
                ", customGenerator=" + customGenerator +
                '}';
        }
    }

    private static class CustomStringGenerator implements AlchemyGenerator<String> {
        public CustomStringGenerator() {
        }

        @Override
        public String get() {
            return randomString;
        }
    }

    private static class GeneratorThatCannotBeInstantiated implements AlchemyGenerator<String> {
        private String string;

        public GeneratorThatCannotBeInstantiated(String string) {
            this.string = string;
        }

        @Override
        public String get() {
            return string;
        }
    }


}
