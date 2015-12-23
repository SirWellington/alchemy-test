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

import java.lang.annotation.Annotation;
import java.util.List;
import org.junit.Before;
import org.junit.Test;
import tech.sirwellington.alchemy.generator.AlchemyGenerator;

import static org.hamcrest.Matchers.empty;
import static org.hamcrest.Matchers.instanceOf;
import static org.hamcrest.Matchers.is;
import static org.hamcrest.Matchers.not;
import static org.hamcrest.Matchers.notNullValue;
import static org.junit.Assert.assertThat;
import static tech.sirwellington.alchemy.generator.AlchemyGenerator.one;
import static tech.sirwellington.alchemy.generator.NumberGenerators.integers;
import static tech.sirwellington.alchemy.generator.NumberGenerators.negativeIntegers;
import static tech.sirwellington.alchemy.test.junit.ThrowableAssertion.assertThrows;

/**
 *
 * @author SirWellington
 */
public class GenerateListTest
{

    private GenerateListAnnotation annotation;

    private int size;

    private final Class<String> genericType = String.class;

    @Before
    public void setUp()
    {
        size = one(integers(5, 100));
        annotation = new GenerateListAnnotation(genericType, size);
    }

    @Test
    public void testCannotInstantiate()
    {
        System.out.println("testCannotInstantiate");

        assertThrows(() -> GenerateList.Values.class.newInstance())
            .isInstanceOf(IllegalAccessException.class);
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
        
        for(Object element : list)
        {
            assertThat(element, is(instanceOf(genericType)));
        }
    }

    @Test
    public void testValuesEdgeCases()
    {
        System.out.println("testValuesEdgeCases");

        assertThrows(() -> GenerateList.Values.createGeneratorFor(null))
            .isInstanceOf(IllegalArgumentException.class);

        annotation.size = one(negativeIntegers());
        
        assertThrows(() -> GenerateList.Values.createGeneratorFor(annotation))
            .isInstanceOf(IllegalArgumentException.class);
        

    }

    private static class GenerateListAnnotation<T> implements GenerateList
    {

        private Class<T> generictype;
        private int size;

        private GenerateListAnnotation(Class<T> generictype, int size)
        {
            this.generictype = generictype;
            this.size = size;
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
        public String toString()
        {
            return "GenerateListAnnotation{" + "generictype=" + generictype + ", size=" + size + '}';
        }

    }

}
