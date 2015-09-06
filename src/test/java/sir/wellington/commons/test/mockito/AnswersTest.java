package sir.wellington.commons.test.mockito;

import sir.wellington.commons.test.mockito.MoreAnswers;
import static sir.wellington.commons.test.junit.ThrowableAssertion.assertThrows;
import java.util.function.BiFunction;
import java.util.function.Supplier;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.runners.MockitoJUnitRunner;

import static org.junit.Assert.*;
import static org.hamcrest.CoreMatchers.*;
import org.hamcrest.Matchers;
import static org.mockito.Mockito.*;
import org.mockito.stubbing.Answer;

/**
 *
 * @author jmoreno
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

    /**
     * Test of returnFirst method, of class MoreAnswers.
     */
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

    /**
     * Test of returnArgumentAtIndex method, of class MoreAnswers.
     */
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

    /**
     * Test of returnArgumentAtIndex method, of class MoreAnswers.
     */
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

    @FunctionalInterface
    private static interface VargsFunction<T>
    {

        T call(T... args);
    }
}
