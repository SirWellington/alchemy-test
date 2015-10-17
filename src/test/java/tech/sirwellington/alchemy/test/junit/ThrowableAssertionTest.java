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
package tech.sirwellington.alchemy.test.junit;

import java.io.IOException;
import java.util.function.Function;
import org.junit.After;
import static org.junit.Assert.fail;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.runners.MockitoJUnitRunner;
import static tech.sirwellington.alchemy.test.DataGenerator.alphabeticString;
import static tech.sirwellington.alchemy.test.junit.ThrowableAssertion.assertThrows;

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
        System.out.println("assertThrown");
        ThrowableAssertion.assertThrows(() ->
        {
            throw new RuntimeException();
        }).hasNoCause()
          .isInstanceOf(RuntimeException.class);

        try
        {
            ThrowableAssertion.assertThrows(() -> {} );
            
            fail("Expected exception here");
        }
        catch (ExceptionNotThrownException ex)
        {

        }

        Function<String, String> function = (s) ->
        {
            throw new RuntimeException(s);
        };

        assertThrows(() -> function.apply("some"))
                .containsInMessage("some");

    }

    @Test
    public void testIsInstanceOf()
    {
        System.out.println("isInstanceOf");
        assertThrows(() ->
        {
            throw new IllegalArgumentException();
        }).isInstanceOf(IllegalArgumentException.class);
    }

    @Test
    public void testHasMessage()
    {
        System.out.println("hasMessage");
        String message = alphabeticString(20).get();
        assertThrows(() ->
        {
            throw new IllegalArgumentException(message);
        }).isInstanceOf(IllegalArgumentException.class)
          .hasMessage(message);
    }

    @Test
    public void testHasNoCause()
    {
        System.out.println("hasNoCause");
        assertThrows(() ->
        {
            throw new IllegalArgumentException();
        }).isInstanceOf(IllegalArgumentException.class)
          .hasNoCause();
    }

    @Test
    public void testHasCauseInstanceOf()
    {
        System.out.println("hasCauseInstanceOf");
        assertThrows(() ->
        {
            throw new IllegalArgumentException(new IOException());
        }).isInstanceOf(IllegalArgumentException.class)
          .hasCauseInstanceOf(IOException.class);
    }

}
