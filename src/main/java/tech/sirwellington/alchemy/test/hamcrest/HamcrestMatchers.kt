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

@file:JvmName("HamcrestMatchers")

package tech.sirwellington.alchemy.test.hamcrest

import com.natpryce.hamkrest.*
import java.util.Objects


/**
 * Fails if the object is not `null`.
 * @author SirWellington
 */
val isNull: Matcher<Any?> = Matcher(Objects::isNull)


/**
 * Fails if the object is `null`.
 *
 * @author SirWellington
 */
val notNull: Matcher<Any?> = !isNull


/**
 * Fails if the collection is empty and has no elements in it.
 *
 * @author SirWellington
 */
val notEmpty: Matcher<Collection<Any>?> = present(!isEmpty)


/**
 * Fails if the collection is not empty (has elements present).
 *
 * @author SirWellington
 */
val isNullOrEmpty: Matcher<Collection<Any>?> = isNull.or(isEmpty as Matcher<Collection<Any>?>)

/**
 * Fails if the collection is null or empty.
 *
 * @author SirWellington
 */
val notNullOrEmpty: Matcher<Collection<Any>?> = present(notEmpty)


/**
 * Fails if the string is empty or `null`.
 *
 * @author SirWellington
 */

val emptyString: Matcher<CharSequence?> = notNull and isEmptyString as Matcher<CharSequence?>


/**
 * Fails if the string is empty or null.
 *
 * @author SirWellington
 */
val nonEmptyString: Matcher<CharSequence?> = notNull and !emptyString


val isNullOrEmptyString: Matcher<CharSequence?> = isNull or (isEmptyString as Matcher<CharSequence?>)

val notNullOrEmptyString: Matcher<CharSequence?> = !tech.sirwellington.alchemy.test.hamcrest.isNullOrEmptyString

/**
 * Fails if the [Boolean] value is `false`.
 */
val isTrue: Matcher<Boolean?> = notNull and equalTo(true)

/**
 * Fails if the [Boolean] value is `true`.
 */
val isFalse: Matcher<Boolean?> = notNull and equalTo(false)

/**
 * Fails if the [Collection] does not have a size of [size].
 */
fun hasSize(size: Int): Matcher<Collection<*>?> = Matcher(Collection<*>?::hasSize, size)

private fun Collection<*>?.hasSize(size: Int): Boolean
{
    return this?.size == size ?: false
}