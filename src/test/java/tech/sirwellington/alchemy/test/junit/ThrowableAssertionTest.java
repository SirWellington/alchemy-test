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
package tech.sirwellington.alchemy.test.junit;

import java.io.IOException;

import com.google.common.base.Function;
import org.junit.*;
import org.junit.runner.RunWith;
import org.mockito.runners.MockitoJUnitRunner;

import static org.junit.Assert.fail;
import static tech.sirwellington.alchemy.test.junit.ThrowableAssertion.*;

/**
 * @author SirWellington
 */
@RunWith(MockitoJUnitRunner.class)
public class ThrowableAssertionTest
{

    @Before
    public void setUp()
    {
    }

    @After
    public void tearDown()
    {
    }

    @Test
    public void testAssertThrown()
    {
        System.out.println("testAssertThrown");


        ExceptionOperation op = new ExceptionOperation()
        {
            @Override
            public void call() throws Throwable
            {
                throw new RuntimeException();
            }
        };

        assertThrows(op).isInstanceOf(RuntimeException.class)
                        .hasNoCause();

        boolean passed = true;
        try
        {
            op = new ExceptionOperation()
            {
                @Override
                public void call() throws Throwable
                {

                }
            };

            assertThrows(op);

            passed = false;
        }
        catch (AssertionError ex)
        {
            passed = true;
        }

        if (!passed)
        {
            fail("Expected AssertionError");
        }

        final Function<String, String> function = new Function<String, String>()
        {
            @Override
            public String apply(String input)
            {
                throw new RuntimeException(input);

            }
        };

        final String message = "some;";
        op = new ExceptionOperation()
        {
            @Override
            public void call() throws Throwable
            {
                function.apply(message);
            }
        };

        assertThrows(op).hasMessage(message);
    }

    @Test
    public void testIsInstanceOf()
    {
        System.out.println("testIsInstanceOf");

        ExceptionOperation op = new ExceptionOperation()
        {
            @Override
            public void call() throws Throwable
            {
                throw new IllegalArgumentException();
            }
        };

        assertThrows(op).isInstanceOf(IllegalArgumentException.class);
    }

    @Test
    public void testHasMessage()
    {
        System.out.println("testHasMessage");

        final String message = "some message";

        ExceptionOperation op = new ExceptionOperation()
        {
            @Override
            public void call() throws Throwable
            {
                throw new IllegalArgumentException(message);
            }
        };

        assertThrows(op)
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessage(message);
    }

    @Test
    public void testHasNoCause()
    {
        System.out.println("testHasNoCause");

        ExceptionOperation op = new ExceptionOperation()
        {
            @Override
            public void call() throws Throwable
            {
                throw new IllegalArgumentException();
            }
        };

        assertThrows(op).isInstanceOf(IllegalArgumentException.class)
                        .hasNoCause();
    }

    @Test
    public void testHasCauseInstanceOf()
    {
        System.out.println("testHasCauseInstanceOf");

        ExceptionOperation op = new ExceptionOperation()
        {
            @Override
            public void call() throws Throwable
            {
                throw new IllegalArgumentException(new IOException());
            }
        };

        assertThrows(op).isInstanceOf(IllegalArgumentException.class)
                        .hasCauseInstanceOf(IOException.class);
    }

}
