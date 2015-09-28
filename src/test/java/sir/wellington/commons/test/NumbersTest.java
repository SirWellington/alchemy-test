/*
 * DO NOT ALTER OR REMOVE COPYRIGHT NOTICES OR THIS HEADER.
 *
 * Copyright 2015 Oracle and/or its affiliates. All rights reserved.
 *
 * Oracle and Java are registered trademarks of Oracle and/or its affiliates.
 * Other names may be trademarks of their respective owners.
 *
 * The contents of this file are subject to the terms of either the GNU
 * General Public License Version 2 only ("GPL") or the Common
 * Development and Distribution License("CDDL") (collectively, the
 * "License"). You may not use this file except in compliance with the
 * License. You can obtain a copy of the License at
 * http://www.netbeans.org/cddl-gplv2.html
 * or nbbuild/licenses/CDDL-GPL-2-CP. See the License for the
 * specific language governing permissions and limitations under the
 * License.  When distributing the software, include this License Header
 * Notice in each file and include the License file at
 * nbbuild/licenses/CDDL-GPL-2-CP.  Oracle designates this
 * particular file as subject to the "Classpath" exception as provided
 * by Oracle in the GPL Version 2 section of the License file that
 * accompanied this code. If applicable, add the following below the
 * License Header, with the fields enclosed by brackets [] replaced by
 * your own identifying information:
 * "Portions Copyrighted [year] [name of copyright owner]"
 *
 * If you wish your version of this file to be governed by only the CDDL
 * or only the GPL Version 2, indicate your decision by adding
 * "[Contributor] elects to include this software in this distribution
 * under the [CDDL or GPL Version 2] license." If you do not indicate a
 * single choice of license, a recipient has the option to distribute
 * your version of this file under either the CDDL, the GPL Version 2 or
 * to extend the choice of license to its licensees as provided above.
 * However, if you add GPL Version 2 code and therefore, elected the GPL
 * Version 2 license, then the option applies only if the new code is
 * made subject to such option by the copyright holder.
 *
 * Contributor(s):
 *
 * Portions Copyrighted 2015 Sun Microsystems, Inc.
 */
package sir.wellington.commons.test;

import static org.hamcrest.Matchers.is;
import static org.junit.Assert.assertThat;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.runners.MockitoJUnitRunner;
import static sir.wellington.commons.test.DataGenerator.integers;
import static sir.wellington.commons.test.DataGenerator.longs;
import static sir.wellington.commons.test.DataGenerator.oneOf;
import static sir.wellington.commons.test.Numbers.safeDecrement;
import static sir.wellington.commons.test.Numbers.safeIncrement;

/**
 *
 * @author SirWellington
 */
@RunWith(MockitoJUnitRunner.class)
public class NumbersTest
{

    @Before
    public void setUp()
    {
    }

    @Test
    public void testSafeIncrement_long()
    {
        System.out.println("testSafeIncrement_long");

        long value = oneOf(longs(-10_000L, 10_000L));
        long result = safeIncrement(value);
        assertThat(result, is(value + 1));

        value = Long.MAX_VALUE;
        result = safeIncrement(value);
        assertThat(result, is(value));

        value = Long.MIN_VALUE;
        result = safeIncrement(value);
        assertThat(result, is(value + 1));
    }

    @Test
    public void testSafeDecrement_long()
    {
        System.out.println("testSafeDecrement_long");

        long value = oneOf(longs(-10_000L, 10_000L));
        long result = safeDecrement(value);
        assertThat(result, is(value - 1));

        value = Long.MIN_VALUE;
        result = safeDecrement(value);
        assertThat(result, is(value));

        value = Long.MAX_VALUE;
        result = safeDecrement(value);
        assertThat(result, is(value - 1));
    }

    @Test
    public void testSafeIncrement_int()
    {
        System.out.println("testSafeIncrement_int");

        int value = oneOf(integers(-10_000, 10_000));
        int result = safeIncrement(value);
        assertThat(result, is(value + 1));

        value = Integer.MAX_VALUE;
        result = safeIncrement(value);
        assertThat(result, is(value));

        value = Integer.MIN_VALUE;
        result = safeIncrement(value);
        assertThat(result, is(value + 1));
    }

    @Test
    public void testSafeDecrement_int()
    {
        System.out.println("testSafeDecrement_int");

        int value = oneOf(integers(-10_000, 10_000));
        int result = safeDecrement(value);
        assertThat(result, is(value - 1));

        value = Integer.MIN_VALUE;
        result = safeDecrement(value);
        assertThat(result, is(value));

        value = Integer.MAX_VALUE;
        result = safeDecrement(value);
        assertThat(result, is(value - 1));
    }

}
