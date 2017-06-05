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

package tech.sirwellington.alchemy.test.mockito;

import org.mockito.invocation.InvocationOnMock;
import org.mockito.stubbing.Answer;
import tech.sirwellington.alchemy.annotations.access.NonInstantiable;
import static tech.sirwellington.alchemy.test.Checks.Internal.checkThat;

/**
 * This class contains a variety of useful answers for use in combination with Mockito.
 *
 * @author SirWellington
 */
@NonInstantiable
public final class MoreAnswers
{

    MoreAnswers() throws IllegalAccessException
    {
        throw new IllegalAccessException("cannot instantiate class");
    }
    
    
    /**
     * For example:
     *
     * <pre>
     *
     * {@code
     * when(object.call("firstArg", 3)).then(returnFirst());
     * }
     * Will return the {@code "firstArg"} string when it is called.
     * </pre>
     *
     * @param <T>
     *
     * @return
     *
     * @see #returnArgumentAtIndex(int)
     */
    public static <T> Answer<T> returnFirst()
    {
        return returnArgumentAtIndex(0);
    }

    /**
     * An answer that returns one of the parameters as the return value.
     *
     * Example:
     * <pre>
     * when(someMock.call(anyString(), anyString()).then(returnArgumentAtIndex(1));
     *
     * String result = someMock.call("arg1", "arg2");
     * assertThat(result, is("arg2"));
     * </pre>
     *
     * @param <T>
     * @param index zero-based index which determines which parameter to return as an answer.
     *
     * @return
     *
     * @see #returnFirst()
     */
    public static <T> Answer<T> returnArgumentAtIndex(final int index)
    {
        checkThat(index >= 0, "Index is out of bounds.");

        return new Answer<T>()
        {
            @Override
            public T answer(InvocationOnMock invocation) throws Throwable
            {
                if (index >= invocation.getArguments().length)
                {
                    throw new IllegalArgumentException("Received an index of " + index + " but only " + invocation.getArguments().length + " arguments");
                }

                return (T) invocation.getArguments()[index];
            }
        };
    }
}
