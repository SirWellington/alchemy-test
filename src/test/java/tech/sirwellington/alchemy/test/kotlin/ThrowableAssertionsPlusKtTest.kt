/*
 * Copyright © 2018. Sir Wellington.
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

package tech.sirwellington.alchemy.test.kotlin

import org.junit.Assert.fail
import org.junit.Test
import org.junit.runner.RunWith
import tech.sirwellington.alchemy.test.junit.ExceptionNotThrownException
import tech.sirwellington.alchemy.test.junit.runners.AlchemyTestRunner

@RunWith(AlchemyTestRunner::class)
class ThrowableAssertionsPlusKtTest
{

    @Test
    fun testKotlinAssertThrowsWhenExceptionDoesNotOccur()
    {
        try
        {
            assertThrows {
                print("No error happening in here")
            }

            fail("Expected a failure")
        }
        catch (ex: ExceptionNotThrownException)
        {
            //passed
        }
    }

    @Test
    fun testKotlinAssertThrowsWhenExceptionOccurs()
    {

        assertThrows {
            throw RuntimeException("any exception")
        }.isInstanceOf(RuntimeException::class.java)
    }

}