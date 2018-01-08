package tech.sirwellington.alchemy.test.kotlin

import org.junit.Assert


/**
 *
 * @author SirWellington
 */

/**
 * Convenient shortcut for [Assert.assertEquals] with [Double] parameters
 */
fun assertDoubleEquals(result: Double, expected: Double, marginOfError: Double = 0.0)
{
    Assert.assertEquals(expected, result, marginOfError)
}