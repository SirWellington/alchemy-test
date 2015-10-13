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
package sir.wellington.alchemy.test.mockito;

import java.util.function.BiFunction;
import java.util.function.Supplier;
import static org.hamcrest.CoreMatchers.is;
import org.junit.After;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertThat;
import static org.junit.Assert.fail;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import static org.mockito.Matchers.anyString;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;
import org.mockito.runners.MockitoJUnitRunner;
import org.mockito.stubbing.Answer;
import static sir.wellington.alchemy.test.junit.ThrowableAssertion.assertThrows;

/**
 *
 * @author SirWellington
 */
@RunWith(MockitoJUnitRunner.class)
public class AnswersTest
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
    public void testReturnFirst()
    {
        System.out.println("testReturnFirst");
        Answer instance = MoreAnswers.returnFirst();
        assertNotNull(instance);
        BiFunction<String, String, String> function = mock(BiFunction.class);
        when(function.apply(anyString(), anyString()))
                .then(instance);
        String expected = "arg1";
        String result = function.apply(expected, "arg2");
        assertThat(result, is(expected));

        Supplier supplier = mock(Supplier.class);
        when(supplier.get())
                .then(instance);
        assertThrows(() -> supplier.get())
                .isInstanceOf(IllegalArgumentException.class);
    }

    @Test
    public void testReturnArgumentAtIndex()
    {
        System.out.println("testReturnArgumentAtIndex");
        int index = 1;
        Answer instance = MoreAnswers.returnArgumentAtIndex(index);
        assertNotNull(instance);

        BiFunction<String, String, String> function = mock(BiFunction.class);
        when(function.apply(anyString(), anyString()))
                .then(instance);
        String expected = "args4";
        String result = function.apply("one", "args4");
        assertThat(result, is(expected));
    }

    @Test
    public void testReturnArgumentAtIndexWithBadIndex()
    {
        System.out.println("testReturnArgumentAtIndexWithBadIndex");
        int index = -1;
        try
        {
            MoreAnswers.returnArgumentAtIndex(index);
            fail("Exception expected here");
        }
        catch (IllegalArgumentException ex)
        {

        }
        index = 2;
        Answer instance = MoreAnswers.returnArgumentAtIndex(index);
        assertNotNull(instance);

        BiFunction<String, String, String> function = mock(BiFunction.class);
        when(function.apply(anyString(), anyString()))
                .then(instance);
        assertThrows(() -> function.apply("one", "args4"))
                .isInstanceOf(IllegalArgumentException.class);
    }

}
