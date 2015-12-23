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
import java.util.Date;
import org.junit.Before;
import org.junit.Test;
import tech.sirwellington.alchemy.generator.AlchemyGenerator;
import tech.sirwellington.alchemy.generator.DateGenerators;
import tech.sirwellington.alchemy.generator.EnumGenerators;

import static org.hamcrest.Matchers.greaterThanOrEqualTo;
import static org.hamcrest.Matchers.is;
import static org.hamcrest.Matchers.lessThan;
import static org.hamcrest.Matchers.lessThanOrEqualTo;
import static org.hamcrest.Matchers.notNullValue;
import static org.junit.Assert.assertThat;
import static tech.sirwellington.alchemy.generator.AlchemyGenerator.one;
import static tech.sirwellington.alchemy.test.junit.ThrowableAssertion.assertThrows;
import static tech.sirwellington.alchemy.test.junit.runners.GenerateDate.Type.RANGE;

/**
 *
 * @author SirWellington
 */
public class GenerateDateTest
{

    private GenerateDate.Type type;
    private Date startDate;
    private Date endDate;

    private GenerateDate annotation;

    @Before
    public void setUp()
    {
        type = EnumGenerators.enumValueOf(GenerateDate.Type.class).get();
        startDate = one(DateGenerators.pastDates());
        endDate = one(DateGenerators.after(startDate));

        annotation = new GenerateDateInstance(type, startDate, endDate);
    }

    @Test
    public void testCannotInstatiate()
    {
        System.out.println("testCannotInstatiate");

        assertThrows(() -> GenerateDate.Values.class.newInstance())
            .isInstanceOf(IllegalAccessException.class);
    }

    @Test
    public void testValue()
    {
        System.out.println("testValue");

        AlchemyGenerator<Date> generator = GenerateDate.Values.createGeneratorFor(annotation);
        assertThat(generator, notNullValue());

        Date now = new Date();
        Date result = generator.get();
        assertThat(result, notNullValue());

        switch (type)
        {
            case FUTURE:
                assertThat(result.after(now), is(true));
                break;
            case PAST:
                assertThat(result.before(now), is(true));
                break;
            case RANGE:
                assertThat(result.getTime(), greaterThanOrEqualTo(startDate.getTime()));
                assertThat(result.getTime(), lessThan(endDate.getTime()));
                break;
            case PRESENT:
                long marginOfErrorMillis = 50;
                assertThat(result.getTime(), greaterThanOrEqualTo(now.getTime() - marginOfErrorMillis));
                assertThat(result.getTime(), lessThanOrEqualTo(now.getTime() + marginOfErrorMillis));
                break;
        }

    }

    @Test
    public void testValueEdgeCases()
    {
        System.out.println("testValueEdgeCases");

        assertThrows(() -> GenerateDate.Values.createGeneratorFor(null))
            .isInstanceOf(IllegalArgumentException.class);

        type = RANGE;
        annotation = new GenerateDateInstance(type, endDate, startDate);
        assertThrows(() -> GenerateDate.Values.createGeneratorFor(annotation))
            .isInstanceOf(IllegalArgumentException.class);
    }

    private static class GenerateDateInstance implements GenerateDate
    {

        private final Type type;
        private final Date startDate;
        private final Date endDate;

        private GenerateDateInstance(Type type, Date startDate, Date endDate)
        {
            this.type = type;
            this.startDate = startDate;
            this.endDate = endDate;
        }

        @Override
        public Type value()
        {
            return type;
        }

        @Override
        public long startDate()
        {
            return startDate.getTime();
        }

        @Override
        public long endDate()
        {
            return endDate.getTime();
        }

        @Override
        public Class<? extends Annotation> annotationType()
        {
            return GenerateDate.class;
        }

    }

}
