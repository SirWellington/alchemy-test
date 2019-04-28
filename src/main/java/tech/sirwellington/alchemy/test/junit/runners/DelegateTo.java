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

import org.junit.runner.Runner;
import org.junit.runners.BlockJUnit4ClassRunner;

import static java.lang.annotation.ElementType.TYPE;
import static java.lang.annotation.RetentionPolicy.RUNTIME;


/**
 * Helps to configure the {@link AlchemyTestRunner}.
 *
 * @author SirWellington
 */
@Retention(RUNTIME)
@Target({TYPE})
public @interface DelegateTo
{
    /**
     * Specifies the {@link Runner} to delegate to after initialization.
     *
     * @return
     */
    Class<? extends Runner> delegate() default BlockJUnit4ClassRunner.class;

    /**
     * If set to true, the {@link AlchemyTestRunner} will run the specified delegate without calling super.
     * If set to false, the {@link AlchemyTestRunner} will run both the delegate and the super.
     *
     * Defaults to {@code true}.
     */
    boolean skipSuper() default true;

}
