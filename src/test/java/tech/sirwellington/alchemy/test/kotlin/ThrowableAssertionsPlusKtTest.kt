package tech.sirwellington.alchemy.test.kotlin

import org.junit.Test
import org.junit.runner.RunWith
import tech.sirwellington.alchemy.test.junit.runners.*

import org.junit.Assert.*
import tech.sirwellington.alchemy.test.junit.ExceptionNotThrownException

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