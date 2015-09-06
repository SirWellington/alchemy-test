package sir.wellington.commons.test.junit;

import static sir.wellington.commons.test.junit.ThrowableAssertion.assertThrows;
import java.io.IOException;
import java.util.function.Function;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.runners.MockitoJUnitRunner;

import static org.junit.Assert.*;
import static sir.wellington.commons.test.DataGenerator.alphabeticString;

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
