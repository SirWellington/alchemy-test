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