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
import tech.sirwellington.alchemy.generator.AlchemyGenerator;
import tech.sirwellington.alchemy.generator.PeopleGenerators;
import tech.sirwellington.alchemy.test.junit.ThrowableAssertion;

import java.lang.annotation.Annotation;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.*;

/**
 * @author SirWellington
 */
public class GenerateCustomTest {
    private GenerateCustomAnnotation annotation;

    @BeforeEach
    public void setup() {
        annotation = new GenerateCustomAnnotation(PersonGenerator.class);
    }

    @Test
    public void testCannotInstantiateValues() throws Exception {
        System.out.println("testCannotInstantiateValues");

        ThrowableAssertion.assertThrows(
            () -> GenerateCustom.Values.class.getDeclaredConstructor().newInstance()
        ).isInstanceOf(IllegalAccessException.class);
    }

    @Test
    public void testValues() throws Exception {
        System.out.println("testValues");

        var generator = GenerateCustom.Values.createGeneratorFor(annotation);
        assertThat(generator, notNullValue());

        var object = generator.get();
        assertThat(object, notNullValue());
        assertThat(object, instanceOf(Person.class));

        var person = (Person) object;
        assertThat(person.name, not(isEmptyOrNullString()));
        assertThat(person.age, greaterThan(0));
    }

    record Person(String name, int age) {}
    static class PersonGenerator implements AlchemyGenerator<Person> {
        @Override
        public Person get() {
            var name = PeopleGenerators.fullNames().get();
            var age = PeopleGenerators.ages().get();

            return new Person(name, age);
        }
    }

    private static class GenerateCustomAnnotation implements GenerateCustom {
        private Class<? extends AlchemyGenerator<?>> generator;

        GenerateCustomAnnotation(Class<? extends AlchemyGenerator<?>> generator) {
            this.generator = generator;
        }

        @Override
        public Class<? extends AlchemyGenerator<?>> value() {
            return this.generator;
        }

        @Override
        public Class<? extends Annotation> annotationType() {
            return GenerateCustom.class;
        }

        @Override
        public String toString() {
            return "GenerateCustomAnnotation{" +
                "generator=" + generator +
                '}';
        }
    }

}
