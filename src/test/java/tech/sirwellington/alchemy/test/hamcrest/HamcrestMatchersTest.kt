package tech.sirwellington.alchemy.test.hamcrest

/*
 * Copyright 2017 RedRoma, Inc.
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
import com.natpryce.hamkrest.isEmptyString
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
        var result = isNull.asPredicate()(nullReference)
        assertTrue(result)

        result = isNull.asPredicate()(string)
        assertFalse(result)
    }

    @Test
    fun testNotNull()
    {
        var result = notNull.asPredicate()(string)
        assertTrue(result)

        result = notNull.asPredicate()(nullReference)
        assertFalse(result)
    }

    @Test
    fun testIsEmptyCollection()
    {
        var result = isEmpty.asPredicate()(emptyCollection)
        assertTrue(result)

        result = isEmpty.asPredicate()(strings)
        assertFalse(result)
    }

    @Test
    fun testNonEmptyCollection()
    {
        var result = notEmpty.asPredicate()(strings)
        assertTrue(result)

        result = notEmpty.asPredicate()(emptyCollection)
        assertFalse(result)
    }

    @Test
    fun testIsEmptyString()
    {
        var result = emptyString.asPredicate()("")
        assertTrue(result)

        result = isEmptyString.asPredicate()(string)
        assertFalse(result)
    }

    @Test
    fun testNonEmptyString()
    {
        var result = nonEmptyString.asPredicate()(string)
        assertTrue(result)

        result = nonEmptyString.asPredicate()("")
        assertFalse(result)
    }
}