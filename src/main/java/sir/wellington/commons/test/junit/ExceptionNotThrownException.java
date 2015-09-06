package sir.wellington.commons.test.junit;

/**
 *
 * @author SirWellington
 */
class ExceptionNotThrownException extends RuntimeException
{

    public ExceptionNotThrownException()
    {
    }

    public ExceptionNotThrownException(String message)
    {
        super(message);
    }

    public ExceptionNotThrownException(String message, Throwable cause)
    {
        super(message, cause);
    }

    public ExceptionNotThrownException(Throwable cause)
    {
        super(cause);
    }

}
