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
 * WITHOUT WARRANTIES OR CONDITIONS OF ANYTIME KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package tech.sirwellington.alchemy.test.junit.runners;

import java.lang.annotation.Annotation;
import java.time.Instant;
import org.junit.Before;
import org.junit.Test;
import tech.sirwellington.alchemy.generator.AlchemyGenerator;
import tech.sirwellington.alchemy.generator.EnumGenerators;
import tech.sirwellington.alchemy.generator.TimeGenerators;

import static org.hamcrest.Matchers.greaterThanOrEqualTo;
import static org.hamcrest.Matchers.is;
import static org.hamcrest.Matchers.lessThan;
import static org.hamcrest.Matchers.lessThanOrEqualTo;
import static org.hamcrest.Matchers.notNullValue;
import static org.junit.Assert.assertThat;
import static tech.sirwellington.alchemy.generator.AlchemyGenerator.one;
import static tech.sirwellington.alchemy.test.junit.ThrowableAssertion.assertThrows;
import static tech.sirwellington.alchemy.test.junit.runners.GenerateInstant.Type.RANGE;

/**
 *
 * @author SirWellington
 */
public class GenerateInstantTest
{
    private GenerateInstant.Type type;
    private Instant startTime;
    private Instant endTime;

    private GenerateInstant annotation;

    @Before
    public void setUp()
    {
        type = EnumGenerators.enumValueOf(GenerateInstant.Type.class).get();
        startTime = one(TimeGenerators.pastInstants());
        endTime = one(TimeGenerators.after(startTime));
        
        annotation = new GenerateInstantInstance(type, startTime, endTime);
    }

       @Test
    public void testCannotInstatiate()
    {
        System.out.println("testCannotInstatiate");
        
        assertThrows(() -> GenerateInstant.Values.class.newInstance())
            .isInstanceOf(IllegalAccessException.class);
    }

    
    @Test
    public void testValue()
    {
        System.out.println("testValue");
        
        AlchemyGenerator<Instant> generator = GenerateInstant.Values.createGeneratorFor(annotation);
        assertThat(generator, notNullValue());

        Instant now = Instant.now();
        Instant result = generator.get();
        assertThat(result, notNullValue());


        switch (type)
        {
            case FUTURE:
                assertThat(result.isAfter(now), is(true));
                break;
            case PAST:
                assertThat(result.isBefore(now), is(true));
                break;
            case RANGE:
                assertThat(result.toEpochMilli(), greaterThanOrEqualTo(startTime.toEpochMilli()));
                assertThat(result.toEpochMilli(), lessThan(endTime.toEpochMilli()));
                break;
            case PRESENT:
                long marginOfErrorMillis = 50;
                assertThat(result.toEpochMilli(), greaterThanOrEqualTo(now.toEpochMilli() - marginOfErrorMillis));
                assertThat(result.toEpochMilli(), lessThanOrEqualTo(now.toEpochMilli() + marginOfErrorMillis));
                break;
        }

    }
    

    @Test
    public void testValueEdgeCases()
    {
        System.out.println("testValueEdgeCases");
        
        assertThrows(() -> GenerateInstant.Values.createGeneratorFor(null))
            .isInstanceOf(IllegalArgumentException.class);
        
        type = RANGE;
        annotation = new GenerateInstantInstance(type, endTime, startTime);
        assertThrows(() -> GenerateInstant.Values.createGeneratorFor(annotation))
            .isInstanceOf(IllegalArgumentException.class);
    }

    private static class GenerateInstantInstance implements GenerateInstant
    {

        private final Type type;
        private final Instant startTime;
        private final Instant endTime;

        private GenerateInstantInstance(Type type, Instant startTime, Instant endTime)
        {
            this.type = type;
            this.startTime = startTime;
            this.endTime = endTime;
        }

        @Override
        public Type value()
        {
            return type;
        }

        @Override
        public long startTime()
        {
            return startTime.toEpochMilli();
        }

        @Override
        public long endTime()
        {
            return endTime.toEpochMilli();
        }

        @Override
        public Class<? extends Annotation> annotationType()
        {
            return GenerateInstant.class;
        }

    }

}
