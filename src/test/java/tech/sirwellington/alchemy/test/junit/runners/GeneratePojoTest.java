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
import java.util.Date;
import org.junit.Before;
import org.junit.Test;
import tech.sirwellington.alchemy.generator.AlchemyGenerator;

import static org.hamcrest.Matchers.greaterThan;
import static org.hamcrest.Matchers.isEmptyOrNullString;
import static org.hamcrest.Matchers.not;
import static org.hamcrest.Matchers.notNullValue;
import static org.junit.Assert.assertThat;
import static tech.sirwellington.alchemy.test.junit.ThrowableAssertion.assertThrows;

/**
 *
 * @author SirWellington
 */
public class GeneratePojoTest 
{
    private GeneratePojoInstance annotation;

    @Before
    public void setUp()
    {
        annotation = new GeneratePojoInstance();
    }
    
    @Test
    public void testCannotInstantiate()
    {
        System.out.println("testCannotInstantiate");
        
        assertThrows(() -> GeneratePojo.Values.class.newInstance())
            .isInstanceOf(IllegalAccessException.class);
    }

    @Test
    public void testValues()
    {
        System.out.println("testValues");
        
        AlchemyGenerator<SamplePojo> generator = GeneratePojo.Values.createGeneratorFor(annotation, SamplePojo.class);
        assertThat(generator, notNullValue());
        
        SamplePojo result = generator.get();
        assertThat(result, notNullValue());
        assertThat(result.name, not(isEmptyOrNullString()));
        assertThat(result.age, greaterThan(0));
        assertThat(result.balance, greaterThan(0L));
        assertThat(result.birthday, notNullValue());
        
    }
    
    @Test
    public void testValuesEdgeCases()
    {
        System.out.println("testValuesEdgeCases");
        
        assertThrows(() -> GeneratePojo.Values.createGeneratorFor(annotation, null))
            .isInstanceOf(IllegalArgumentException.class);
        
        assertThrows(() -> GeneratePojo.Values.createGeneratorFor(null, SamplePojo.class))
            .isInstanceOf(IllegalArgumentException.class);
    }

    
    private static class SamplePojo
    {
        private String name;
        private int age;
        private Date birthday;
        private long balance;
    }
    
    private static class GeneratePojoInstance implements GeneratePojo
    {

        @Override
        public Class<? extends Annotation> annotationType()
        {
            return GeneratePojo.class;
        }
        
    }
}