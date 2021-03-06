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

import java.lang.annotation.Retention;
import java.lang.annotation.Target;

import org.mockito.Mock;

import static java.lang.annotation.ElementType.TYPE;
import static java.lang.annotation.RetentionPolicy.RUNTIME;


/**
 * {@link AlchemyTestRunner} inits your Mockito {@linkplain Mock Mocks} for you, exactly the same
 * way as {@link org.mockito.junit.MockitoJUnitRunner} does.
 * Apply this annotation with a value of {@code false} to
 * your Test Class to disable this behavior.
 *
 * @author SirWellington
 */
@Retention(RUNTIME)
@Target({TYPE})
public @interface InitMocks
{
    boolean value() default true;
}
