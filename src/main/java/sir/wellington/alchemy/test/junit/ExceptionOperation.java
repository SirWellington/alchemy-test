/*
 * Copyright 2015 SirWellington.
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

package sir.wellington.alchemy.test.junit;

/**
 * Intended to be used in conjunction with {@link ThrowableAssertion} and Lambdas to make asserting Throwables much
 * easier syntactically.
 *
 * @author SirWellington
 */
@FunctionalInterface
public interface ExceptionOperation
{

    /**
     * Calls the code that may throw an exception.
     *
     * @throws Throwable
     */
    void call() throws Throwable;
}
