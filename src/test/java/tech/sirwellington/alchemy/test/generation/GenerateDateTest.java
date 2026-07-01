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
import tech.sirwellington.alchemy.generator.DateGenerators;
import tech.sirwellington.alchemy.generator.EnumGenerators;
import tech.sirwellington.alchemy.test.generation.GenerateDate;

import java.lang.annotation.Annotation;
import java.util.Date;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.*;
import static tech.sirwellington.alchemy.generator.AlchemyGenerator.Get.one;
import static tech.sirwellington.alchemy.test.ThrowableAssertion.assertThrows;
import static tech.sirwellington.alchemy.test.generation.GenerateDate.Type.RANGE;

/**
 * @author SirWellington
 */
public class GenerateDateTest {

    private GenerateDate.Type type;
    private Date startDate;
    private Date endDate;

    private GenerateDate annotation;

    @BeforeEach
    public void setUp() {
        type = EnumGenerators.enumValueOf(GenerateDate.Type.class).get();
        startDate = one(DateGenerators.pastDates());
        endDate = one(DateGenerators.after(startDate));

        annotation = new GenerateDateInstance(type, startDate, endDate);
    }

    @Test
    public void testCannotInstantiate() throws IllegalAccessException, InstantiationException {
        assertThrows(
            () -> GenerateDate.Values.class.getDeclaredConstructor().newInstance()
        ).isInstanceOf(IllegalAccessException.class);
    }

    @Test
    public void testValue() {
        System.out.println("testValue");

        var generator = GenerateDate.Values.createGeneratorFor(annotation);
        assertThat(generator, notNullValue());

        var now = new Date();
        var result = generator.get();
        assertThat(result, notNullValue());

        switch (type) {
            case FUTURE -> assertThat(result.after(now), is(true));
            case PAST -> assertThat(result.before(now), is(true));
            case RANGE -> {
                assertThat(result.getTime(), greaterThanOrEqualTo(startDate.getTime()));
                assertThat(result.getTime(), lessThan(endDate.getTime()));
            }
            case PRESENT -> {
                long marginOfErrorMillis = 50;
                assertThat(result.getTime(), greaterThanOrEqualTo(now.getTime() - marginOfErrorMillis));
                assertThat(result.getTime(), lessThanOrEqualTo(now.getTime() + marginOfErrorMillis));
            }
        }

    }

    @Test
    public void testValueEdgeCases1() throws Exception {
        System.out.println("testValueEdgeCases1");

        assertThrows(
            () -> GenerateDate.Values.createGeneratorFor(null)
        ).isInstanceOf(IllegalArgumentException.class);
    }

    @Test
    public void testValueEdgeCases2() throws Exception {
        System.out.println("testValueEdgeCases2");

        type = RANGE;

        annotation = new GenerateDateInstance(type, endDate, startDate);
        assertThrows(
            () -> GenerateDate.Values.createGeneratorFor(annotation)
        ).isInstanceOf(IllegalArgumentException.class);
    }

    private static class GenerateDateInstance implements GenerateDate {
        private final Type type;
        private final Date startDate;
        private final Date endDate;

        private GenerateDateInstance(Type type, Date startDate, Date endDate) {
            this.type = type;
            this.startDate = startDate;
            this.endDate = endDate;
        }

        @Override
        public Type value() {
            return type;
        }

        @Override
        public long startDate() {
            return startDate.getTime();
        }

        @Override
        public long endDate() {
            return endDate.getTime();
        }

        @Override
        public Class<? extends Annotation> annotationType() {
            return GenerateDate.class;
        }

    }

}
