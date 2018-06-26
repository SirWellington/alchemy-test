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

import org.junit.Before;
import org.junit.Test;
import tech.sirwellington.alchemy.generator.AlchemyGenerator;

import static org.hamcrest.Matchers.notNullValue;
import static org.junit.Assert.assertThat;

/**
 * @author SirWellington
 */
public class GenerateEnumTest
{

    private enum Role
    {
        DEVELOPER,
        MANAGER,
        QA,
        OWNER,
        ARCHITECT
    }

    private GenerateEnum annotation;

    @Before
    public void setUp()
    {
        annotation = new FakeAnnotation();
    }

    @Test(expected = IllegalAccessException.class)
    public void testCannotInstantiate() throws IllegalAccessException, InstantiationException
    {
        System.out.println("testCannotInstatiate");

        GenerateEnum.Values.class.newInstance();
    }


    @Test
    public void testValues()
    {
        System.out.println("testValues");

        AlchemyGenerator<Role> generator = GenerateEnum.Values.createGeneratorFor(annotation, Role.class);

        assertThat(generator, notNullValue());

        Role result = generator.get();
        assertThat(result, notNullValue());
    }


    @Test(expected = IllegalArgumentException.class)
    public void testValuesWithBadArgs()
    {
        System.out.println("testValuesWithBadArgs");
        GenerateEnum.Values.createGeneratorFor(null, null);
    }

    private static class FakeAnnotation implements GenerateEnum
    {

        @Override
        public Class<? extends Annotation> annotationType()
        {
            return GenerateEnum.class;
        }

    }

}