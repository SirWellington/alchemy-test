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

package tech.sirwellington.alchemy.test.hamcrest

/*
 * Copyright 2018 RedRoma, Inc.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

import com.natpryce.hamkrest.isEmpty
import org.junit.Assert.assertFalse
import org.junit.Assert.assertTrue
import org.junit.Before
import org.junit.Test
import tech.sirwellington.alchemy.generator.CollectionGenerators
import tech.sirwellington.alchemy.generator.StringGenerators.Companion.alphabeticStrings
import tech.sirwellington.alchemy.generator.one

class HamcrestMatchersTest
{

    private val nullReference: Any? = null
    private val emptyCollection = emptyList<Any>()
    private lateinit var string: String
    private lateinit var strings: List<String>

    @Before
    fun setUp()
    {

        setupData()
        setupMocks()
    }

    private fun setupData()
    {
        string = one(alphabeticStrings())
        strings = CollectionGenerators.listOf(alphabeticStrings())
    }

    private fun setupMocks()
    {

    }

    @Test
    fun testIsNull()
    {
        val matcher = isNull.asPredicate()
        assertTrue(matcher(nullReference))
        assertFalse(matcher(string))
    }

    @Test
    fun testNotNull()
    {
        val matcher = notNull.asPredicate()
        assertFalse(matcher(null))
        assertFalse(matcher(nullReference))
        assertTrue(matcher(string))
    }

    @Test
    fun testIsEmptyCollection()
    {
        val matcher = isEmpty.asPredicate()

        assertTrue(matcher(emptyCollection))
        assertFalse(matcher(strings))
    }

    @Test
    fun testIsNullOrEmptyCollection()
    {
        val matcher = isNullOrEmpty.asPredicate()

        assertTrue(matcher(null))
        assertTrue(matcher(emptyCollection))
        assertFalse(matcher(strings))
    }

    @Test
    fun testNonEmptyCollection()
    {
        val matcher = notEmpty.asPredicate()

        assertTrue(matcher(strings))
        assertFalse(matcher(emptyCollection))
    }

    @Test
    fun testNotNullOrEmptyCollection()
    {
        val matcher = notNullOrEmpty.asPredicate()

        assertTrue(matcher(strings))
        assertFalse(matcher(null))
        assertFalse(matcher(emptyCollection))
    }

    @Test
    fun testEmptyString()
    {
        val matcher = emptyString.asPredicate()

        assertTrue(matcher(""))
        assertFalse(matcher(string))
        assertFalse(matcher(null))
    }

    @Test
    fun testIsNullOrEmptyString()
    {
        val matcher = isNullOrEmptyString.asPredicate()

        assertTrue(matcher(null))
        assertTrue(matcher(""))
        assertFalse(matcher(string))
    }

    @Test
    fun testNonEmptyString()
    {
        val matcher = nonEmptyString.asPredicate()
        assertTrue(matcher(string))
        assertFalse(matcher(""))
        assertFalse(matcher(null))
    }

    @Test
    fun testNotNullOrEmptyString()
    {
        val matcher = notNullOrEmptyString.asPredicate()

        assertTrue(matcher(string))
        assertFalse(matcher(null))
        assertFalse(matcher(""))
    }

    @Test
    fun testIsTrue()
    {
        val matcher = isTrue.asPredicate()
        assertTrue(matcher(true))
        assertFalse(matcher(false))
        assertFalse(matcher(null))
    }

    @Test
    fun testIsFalse()
    {
        val matcher = isFalse.asPredicate()
        assertTrue(matcher(false))
        assertFalse(matcher(true))
        assertFalse(matcher(null))
    }

    @Test
    fun testCollectionHasSize()
    {
        val list = strings
        val actualSize = list.size

        assertTrue(hasSize(actualSize).asPredicate()(list))
        assertFalse(hasSize(0).asPredicate()(list))
        assertFalse(hasSize(actualSize).asPredicate()(null as Collection<*>?))
    }

}