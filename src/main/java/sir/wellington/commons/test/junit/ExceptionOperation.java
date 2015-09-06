package sir.wellington.commons.test.junit;

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
