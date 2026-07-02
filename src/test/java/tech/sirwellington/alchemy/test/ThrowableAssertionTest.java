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
package tech.sirwellington.alchemy.test;

import org.junit.jupiter.api.Test;

import java.io.IOException;
import java.util.function.Function;

import static org.junit.jupiter.api.Assertions.fail;
import static tech.sirwellington.alchemy.test.ThrowableAssertion.assertThrows;

/**
 * @author SirWellington
 */
@AlchemyTest
public class ThrowableAssertionTest {

    @Test
    public void testAssertThrown() {
        ExceptionOperation op = () -> {
            throw new RuntimeException();
        };

        assertThrows(op).isInstanceOf(RuntimeException.class)
                        .hasNoCause();

        boolean passed = true;
        try {
            op = () -> {};
            assertThrows(op);

            passed = false;
        } catch (AssertionError ex) {
            passed = true;
        }

        if (!passed) {
            fail("Expected AssertionError");
        }

        final Function<String, String> function = input -> {
            throw new RuntimeException(input);
        };

        final String message = "some;";
        op = () -> function.apply(message);

        assertThrows(op).hasMessage(message);
    }

    @Test
    public void testIsInstanceOf() {
        ExceptionOperation op = () -> {
            throw new IllegalArgumentException();
        };

        assertThrows(op).isInstanceOf(IllegalArgumentException.class);
    }

    @Test
    public void testHasMessage() {
        final String message = "some message";

        ExceptionOperation op = () -> {
            throw new IllegalArgumentException(message);
        };

        assertThrows(op)
            .isInstanceOf(IllegalArgumentException.class)
            .hasMessage(message);
    }

    @Test
    public void testHasNoCause() {
        System.out.println("testHasNoCause");

        ExceptionOperation op = () -> {
            throw new IllegalArgumentException();
        };

        assertThrows(op).isInstanceOf(IllegalArgumentException.class)
                        .hasNoCause();
    }

    @Test
    public void testHasCauseInstanceOf() {
        System.out.println("testHasCauseInstanceOf");

        ExceptionOperation op = () -> {
            throw new IllegalArgumentException(new IOException());
        };

        assertThrows(op).isInstanceOf(IllegalArgumentException.class)
                        .hasCauseInstanceOf(IOException.class);
    }

}
