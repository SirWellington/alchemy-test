package sir.wellington.commons.test.mockito;

import com.google.common.base.Preconditions;
import org.mockito.stubbing.Answer;

/**
 * This class contains a variety of useful answers for use in combination with Mockito.
 *
 * @author SirWellington
 */
public class MoreAnswers
{

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
    public static <T> Answer<T> returnArgumentAtIndex(int index)
    {
        Preconditions.checkArgument(index >= 0, "Index is out of bounds.");
        return (i) ->
        {
            if (index >= i.getArguments().length)
            {
                throw new IllegalArgumentException("Received an index of " + index + " but only " + i.getArguments().length + " arguments");
            }
            return (T) i.getArguments()[index];
        };
    }
}
