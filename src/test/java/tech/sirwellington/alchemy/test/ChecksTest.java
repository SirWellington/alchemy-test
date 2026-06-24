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

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import tech.sirwellington.alchemy.test.internal.Checks;
import tech.sirwellington.alchemy.test.junit.AlchemyTest;

import static tech.sirwellington.alchemy.test.junit.ThrowableAssertion.assertThrows;

/**
 * @author SirWellington
 */
@AlchemyTest
public class ChecksTest {

    private String message;

    @BeforeEach
    public void setUp() {
        message = "some message";
    }

    @Test
    public void testCannotInstantiate() {
        assertThrows(() -> Checks.class.getDeclaredConstructor().newInstance())
            .isInstanceOf(IllegalAccessException.class);
    }

    @Test
    public void testCheckNotNull() {
        Object object = new Object();
        Checks.checkNotNull(object);
        Checks.checkNotNull(object, message);
    }

    @Test
    public void testCheckNotNullExpecting() {
        assertThrows(() -> Checks.checkNotNull(null))
            .isInstanceOf(IllegalArgumentException.class);
    }

    @Test
    public void testCheckNotNullExpectingWithMessage() {
        assertThrows(() -> Checks.checkNotNull(null, message))
            .isInstanceOf(IllegalArgumentException.class);
    }

    @Test
    public void testCheckThat() {
        Checks.checkThat(true);
        Checks.checkThat(true, message);
    }

    @Test
    public void testCheckThatExpecting() {

        assertThrows(() -> Checks.checkThat(false))
            .isInstanceOf(IllegalArgumentException.class);
    }

    @Test
    public void testCheckThatExpectingWithMessage() {
        assertThrows(() -> Checks.checkThat(false, message))
            .isInstanceOf(IllegalArgumentException.class);
    }
}
