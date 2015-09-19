package sir.wellington.commons.test.junit;

import com.google.common.base.Preconditions;
import static org.hamcrest.Matchers.containsString;
import static org.hamcrest.Matchers.is;
import static org.hamcrest.Matchers.isA;
import static org.hamcrest.Matchers.notNullValue;
import static org.hamcrest.Matchers.nullValue;
import static org.junit.Assert.assertThat;

/**
 * Makes it easier syntactically using Java 8 to assert an Exception is thrown by a section of code. You can also
 * perform additional verification on the exception that is thrown.
 *
 * Example:
 *
 * <pre>
 * {@code
 * assertThrows(() -> someFunctionThatThrows())
 * .isIntanceOf(RuntimeException.class)
 * .hasNoCause();
 * }
 * </pre>
 *
 * @author SirWellington
 */
public final class ThrowableAssertion
{

    /**
     * Assert that a function throws an exception.
     *
     * @param operation The Lambda function that encapsulates code you expect to throw an exception.
     *
     * @return
     *
     * @throws ExceptionNotThrownException If no exception is thrown.
     */
    public static ThrowableAssertion assertThrows(ExceptionOperation operation) throws ExceptionNotThrownException
    {
        Preconditions.checkNotNull(operation, "missing operation");
        return new ThrowableAssertion(operation)
                .execute();
    }

    private Throwable caught;
    private final ExceptionOperation operation;

    private ThrowableAssertion(ExceptionOperation operation)
    {
        this.operation = operation;
    }

    private ThrowableAssertion execute() throws ExceptionNotThrownException
    {
        try
        {
            operation.call();
        }
        catch (Throwable ex)
        {
            this.caught = ex;
            return this;
        }
        throw new ExceptionNotThrownException("Expected an exception");
    }

    /**
     * Check that the Exception is of a particular type.
     *
     * @param exceptionClass The expected type of the Exception.
     *
     * @return
     */
    public ThrowableAssertion isInstanceOf(Class<? extends Throwable> exceptionClass)
    {
        assertThat(caught, isA((Class<Throwable>) exceptionClass));
        return this;
    }

    /**
     * Checks to make sure the exception contains a certain message.
     *
     * @param expectedMessage The exact message expected
     *
     * @return
     */
    public ThrowableAssertion hasMessage(String expectedMessage)
    {
        assertThat(caught.getMessage(), is(expectedMessage));
        return this;
    }

    /**
     * Assert that the exception contains a string in its message.
     *
     * @param messageString The partial message to expected.
     *
     * @return
     */
    public ThrowableAssertion containsInMessage(String messageString)
    {
        assertThat(caught.getMessage(), containsString(messageString));
        return this;
    }

    /**
     * Assert that the exception has no causing exception
     *
     * @return
     */
    public ThrowableAssertion hasNoCause()
    {
        assertThat(caught.getCause(), nullValue());
        return this;
    }

    /**
     * Asserts that the Exception has a cause of a particular type.
     *
     * @param exceptionClass The type expected.
     *
     * @return
     */
    public ThrowableAssertion hasCauseInstanceOf(Class<? extends Throwable> exceptionClass)
    {
        assertThat(caught.getCause(), notNullValue());
        assertThat(caught.getCause(), isA((Class<Throwable>) exceptionClass));
        return this;
    }
}
