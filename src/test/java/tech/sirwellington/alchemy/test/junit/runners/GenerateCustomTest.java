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

import java.lang.annotation.Annotation;
import java.util.Objects;

import org.junit.Before;
import org.junit.Test;
import tech.sirwellington.alchemy.generator.AlchemyGenerator;
import tech.sirwellington.alchemy.generator.PeopleGenerators;

import static org.hamcrest.Matchers.*;
import static org.junit.Assert.assertThat;

/**
 * @author SirWellington
 */
public class GenerateCustomTest
{
    private GenerateCustomAnnotation annotation;

    @Before
    public void setup()
    {
        annotation = new GenerateCustomAnnotation(PersonGenerator.class);
    }

    @Test(expected = IllegalAccessException.class)
    public void testCannotInstantiateValues() throws Exception
    {
        System.out.println("testCannotInstantiateValues");

        GenerateCustom.Values.class.newInstance();
    }

    @Test
    public void testValues() throws Exception
    {
        System.out.println("testValues");

        AlchemyGenerator<?> generator = GenerateCustom.Values.createGeneratorFor(annotation);
        assertThat(generator, notNullValue());

        Object object = generator.get();
        assertThat(object, notNullValue());
        assertThat(object, instanceOf(Person.class));

        Person person = (Person) object;
        assertThat(person.name, not(isEmptyOrNullString()));
        assertThat(person.age, greaterThan(0));
    }

    private static class Person
    {
        private String name;
        private int age;

        Person(String name, int age)
        {
            this.name = name;
            this.age = age;
        }

        @Override
        public boolean equals(Object o)
        {
            if (this == o)
            {
                return true;
            }
            if (o == null || getClass() != o.getClass())
            {
                return false;
            }
            Person person = (Person) o;
            return age == person.age &&
                    Objects.equals(name, person.name);
        }

        @Override
        public int hashCode()
        {
            return Objects.hash(name, age);
        }

        @Override
        public String toString()
        {
            return "Person{" +
                    "name='" + name + '\'' +
                    ", age=" + age +
                    '}';
        }
    }

    static class PersonGenerator implements AlchemyGenerator<Person>
    {
        @Override
        public Person get()
        {
            String name = PeopleGenerators.names().get();
            int age = PeopleGenerators.ages().get();

            return new Person(name, age);
        }
    }

    private static class GenerateCustomAnnotation implements GenerateCustom
    {
        private Class<? extends AlchemyGenerator<?>> generator;

        GenerateCustomAnnotation(Class<? extends AlchemyGenerator<?>> generator)
        {
            this.generator = generator;
        }

        @Override
        public Class<? extends AlchemyGenerator<?>> value()
        {
            return this.generator;
        }

        @Override
        public Class<? extends Annotation> annotationType()
        {
            return GenerateCustom.class;
        }

        @Override
        public String toString()
        {
            return "GenerateCustomAnnotation{" +
                    "generator=" + generator +
                    '}';
        }
    }

}
