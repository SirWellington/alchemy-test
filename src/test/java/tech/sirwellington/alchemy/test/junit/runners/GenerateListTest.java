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

import java.lang.annotation.Annotation;
import java.util.List;

import org.junit.Before;
import org.junit.Test;
import tech.sirwellington.alchemy.generator.AlchemyGenerator;
import tech.sirwellington.alchemy.generator.StringGenerators;

import static org.hamcrest.Matchers.*;
import static org.junit.Assert.assertThat;
import static tech.sirwellington.alchemy.generator.AlchemyGenerator.Get.one;
import static tech.sirwellington.alchemy.generator.NumberGenerators.integers;
import static tech.sirwellington.alchemy.generator.NumberGenerators.negativeIntegers;

/**
 * @author SirWellington
 */
public class GenerateListTest
{

    private GenerateListAnnotation annotation;

    private int size;
    private final Class<String> genericType = String.class;
    private final Class<? extends AlchemyGenerator<?>> customGenerator = CustomStringGenerator.class;

    private static String randomString = "";

    @Before
    public void setUp()
    {
        size = one(integers(5, 100));
        annotation = new GenerateListAnnotation(genericType, size, null);

        randomString = StringGenerators.alphanumericStrings().get();
    }

    @Test(expected = IllegalAccessException.class)
    public void testCannotInstantiate() throws IllegalAccessException, InstantiationException
    {
        System.out.println("testCannotInstantiate");

        GenerateList.Values.class.newInstance();
    }

    @Test
    public void testValues()
    {
        System.out.println("testValues");

        AlchemyGenerator<List<?>> generator = GenerateList.Values.createGeneratorFor(annotation);
        assertThat(generator, notNullValue());

        List<?> list = generator.get();
        assertThat(list, notNullValue());
        assertThat(list, not(empty()));

        for (Object element : list)
        {
            assertThat(element, is(instanceOf(genericType)));
        }
    }

    @Test
    public void testValuesWithCustomGenerator() throws Exception
    {
        System.out.println("testValuesWithCustomGenerator");

        annotation.customGenerator = this.customGenerator;

        AlchemyGenerator<List<?>> generator = GenerateList.Values.createGeneratorFor(annotation);
        assertThat(generator, notNullValue());

        List<?> list = generator.get();
        assertThat(list, notNullValue());
        assertThat(list, not(empty()));

        for (Object element: list)
        {
            assertThat(element, instanceOf(String.class));
            assertThat(element.toString(), equalTo(randomString));
        }
    }

    @Test(expected = IllegalArgumentException.class)
    public void testValuesWithNullAnnotation()
    {
        System.out.println("testValuesEdgeCases1");

        GenerateList.Values.createGeneratorFor(null);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testValuesWithNegativeSize()
    {
        System.out.println("testValuesEdgeCases2");

        annotation.size = one(negativeIntegers());
        GenerateList.Values.createGeneratorFor(annotation);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testValuesWithCustomGeneratorThatCannotBeInstantiated() throws Exception
    {
        System.out.println("testValuesWithCustomGeneratorThatCannotBeInstantiated");

        annotation.customGenerator = GeneratorThatCannotBeInstantiated.class;

        GenerateList.Values.createGeneratorFor(annotation);
    }

    private static class GenerateListAnnotation<T> implements GenerateList
    {

        private Class<T> generictype;
        private int size;
        private Class<? extends AlchemyGenerator<?>> customGenerator;

        private GenerateListAnnotation(Class<T> generictype, int size)
        {
            this(generictype, size, null);
        }

        private GenerateListAnnotation(Class<T> generictype, int size, Class<? extends AlchemyGenerator<?>> customGenerator)
        {
            this.generictype = generictype;
            this.size = size;
            this.customGenerator = customGenerator;
        }

        @Override
        public Class<? extends Annotation> annotationType()
        {
            return GenerateList.class;
        }

        @Override
        public Class<?> value()
        {
            return generictype;
        }

        @Override
        public int size()
        {
            return size;
        }

        @Override
        public Class<? extends AlchemyGenerator<?>> customGenerator()
        {
            return customGenerator;
        }

        @Override
        public String toString()
        {
            return "GenerateListAnnotation{" +
                    "generictype=" + generictype +
                    ", size=" + size +
                    ", customGenerator=" + customGenerator +
                    '}';
        }
    }


    private static class CustomStringGenerator implements AlchemyGenerator<String>
    {
        public CustomStringGenerator() {}

        @Override
        public String get()
        {
            return randomString;
        }
    }

    private static class GeneratorThatCannotBeInstantiated implements AlchemyGenerator<String>
    {
        private String string;

        public GeneratorThatCannotBeInstantiated(String string)
        {
            this.string = string;
        }

        @Override
        public String get()
        {
            return string;
        }
    }


}
