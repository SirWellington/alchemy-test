package tech.sirwellington.alchemy.test.kotlin

import tech.sirwellington.alchemy.test.junit.ThrowableAssertion


/**
 * Convenience function for [ThrowableAssertion.assertThrows].
 *
 * @author SirWellington
 */
fun assertThrows(block: () -> Unit) : ThrowableAssertion
{
    return ThrowableAssertion.assertThrows(block)
}