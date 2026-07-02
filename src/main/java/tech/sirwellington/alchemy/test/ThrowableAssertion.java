/*
 * Copyright © 2026. Sir Wellington.
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
package tech.sirwellington.alchemy.test;

import tech.sirwellington.alchemy.annotations.arguments.Required;
import tech.sirwellington.alchemy.annotations.designs.FluidAPIDesign;

import java.text.MessageFormat;
import java.util.Objects;

import static org.junit.jupiter.api.Assertions.*;
import static tech.sirwellington.alchemy.test.internal.Checks.checkNotNull;

/**
 * Makes it easier syntactically using Java 8 to assert an Exception is thrown by a section of code.
 * You can also perform additional verification on the exception that is thrown.
 * <p>
 * Example:
 * <p>
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
@FluidAPIDesign
public final class ThrowableAssertion {

    private Throwable caught;
    private final ExceptionOperation operation;

    /**
     * Assert that a function throws an exception.
     *
     * @param operation The Lambda function that encapsulates code you expect to throw an exception.
     * @throws ExceptionNotThrownException If no exception is thrown.
     */
    public static ThrowableAssertion assertThrows(
        @Required ExceptionOperation operation
    ) throws ExceptionNotThrownException {
        checkNotNull(operation, "missing operation");
        return new ThrowableAssertion(operation)
            .execute();
    }

    private ThrowableAssertion(ExceptionOperation operation) {
        this.operation = Objects.requireNonNull(operation);
    }

    private ThrowableAssertion execute() throws ExceptionNotThrownException {
        try {
            operation.call();
        } catch (Throwable ex) {
            this.caught = ex;
            return this;
        }
        throw new ExceptionNotThrownException("Expected an exception");
    }

    /**
     * Check that the Exception is of a particular type.
     *
     * @param exceptionClass The expected type of the Exception.
     */
    public ThrowableAssertion isInstanceOf(
        Class<? extends Throwable> exceptionClass
    ) {
        checkNotNull(exceptionClass);
        assertInstanceOf(exceptionClass, caught);
        return this;
    }

    /**
     * Checks to make sure the exception contains a certain message.
     *
     * @param expectedMessage The exact message expected
     */
    public ThrowableAssertion hasMessage(String expectedMessage) {
        checkNotNull(expectedMessage);
        assertNotNull(caught, "No exception was thrown");
        var message = caught.getMessage();
        assertNotNull(message, "No exception message was found");
        assertEquals(expectedMessage, message);
        return this;
    }

    /**
     * Assert that the exception contains a string in its message.
     *
     * @param messageString The partial message to expected.
     */
    public ThrowableAssertion containsInMessage(String messageString) {
        checkNotNull(messageString);
        var message = caught.getMessage();
        assertNotNull(message, "No exception message was found");
        var errorMessage = MessageFormat.format(
            "Exception message does not contain [{0}]. Full message: [{1}]",
            message,
            message
        );
        assertTrue(message.contains(messageString), errorMessage);
        return this;
    }

    /**
     * Assert that the exception has no causing exception
     */
    public ThrowableAssertion hasNoCause() {
        var errorMessage = MessageFormat.format(
          "Expected no cause, but got: [{0}]",
          caught.getCause()
        );
        assertNull(caught.getCause(), errorMessage);
        return this;
    }

    /**
     * Asserts that the Exception has a cause of a particular type.
     *
     * @param exceptionClass The type expected.
     */
    public ThrowableAssertion hasCauseInstanceOf(Class<? extends Throwable> exceptionClass) {
        var cause = caught.getCause();
        assertNotNull(cause, "No cause exception found in: " + caught);
        var errorMessage = MessageFormat.format(
            "Expected cause exception of type [{0}], but is [{1}]",
            exceptionClass,
            cause
        );
        assertInstanceOf(exceptionClass, cause, errorMessage);
        return this;
    }
}
