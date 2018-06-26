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

import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith
import tech.sirwellington.alchemy.generator.NumberGenerators
import tech.sirwellington.alchemy.test.junit.runners.AlchemyTestRunner
import tech.sirwellington.alchemy.test.junit.runners.GenerateDouble
import tech.sirwellington.alchemy.test.junit.runners.Repeat
import java.lang.AssertionError

@RunWith(AlchemyTestRunner::class)
@Repeat(50)
class KotlinAssertsKtTest
{

    @GenerateDouble(min = 0.0, max = 1000.0)
    private var double: Double = 0.0

    @Before
    fun setUp()
    {

        setupData()
        setupMocks()

    }

    @Test
    fun testAssertDoubleEquals()
    {
        assertDoubleEquals(double, double, 0.0)
        assertDoubleEquals(double, double, 0.1)
        assertDoubleEquals(double, double, 0.2)
    }

    @Test
    fun testAssertDoubleEqualsWhenFails()
    {
        val margin = NumberGenerators.smallPositiveDoubles().get()
        val result = double + (margin * 2)

        assertThrows { assertDoubleEquals(result = result, expected = double, marginOfError = margin) }
                .isInstanceOf(AssertionError::class.java)
    }

    @Test
    fun testAssertDoubleWithDifferentValues()
    {
        val otherDouble = NumberGenerators.smallPositiveDoubles().get()

        assertThrows { assertDoubleEquals(otherDouble, double, 0.1) }
                .isInstanceOf(AssertionError::class.java)
    }

    private fun setupData()
    {
        double = NumberGenerators.smallPositiveDoubles().get()
    }

    private fun setupMocks()
    {

    }
}