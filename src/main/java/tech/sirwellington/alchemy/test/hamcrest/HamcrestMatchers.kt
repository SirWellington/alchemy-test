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

@file:JvmName("HamcrestMatchers")

package tech.sirwellington.alchemy.test.hamcrest

import com.natpryce.hamkrest.Matcher
import com.natpryce.hamkrest.isEmpty
import com.natpryce.hamkrest.isNullOrEmptyString
import java.util.Objects


/**
 * Fails if the object is not `null`.
 * @author SirWellington
 */
val isNull = Matcher(Objects::isNull)

/**
 * Fails if the object is `null`.
 * @author SirWellington
 */
val notNull = !isNull

/**
 * Fails if the collection is not empty (has elements present).
 * @author SirWellington
 */
val notEmpty = !isEmpty

/**
 * Fails if the string is empty or `null`.
 * @author SirWellington
 */

val emptyString = isNullOrEmptyString
/**
 * Fails if the string is empty or null.
 * @author SirWellington
 */
val nonEmptyString = !emptyString