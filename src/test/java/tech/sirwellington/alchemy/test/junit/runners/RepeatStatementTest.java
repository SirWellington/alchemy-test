/*
 * Copyright 2015 SirWellington Tech.
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
package tech.sirwellington.alchemy.test.junit.runners;

import java.util.Random;
import java.util.function.Supplier;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.model.FrameworkMethod;
import org.junit.runners.model.Statement;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;

import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static tech.sirwellington.alchemy.test.junit.ThrowableAssertion.assertThrows;

/**
 *
 * @author SirWellington
 */
@RunWith(MockitoJUnitRunner.class)
public class RepeatStatementTest
{

    private int timesToRepeat;

    @Mock
    private Statement statement;

    @Mock
    private Supplier<Statement> statementFactory;

    @Mock
    private FrameworkMethod method;

    private String methodName;

    private Random random;

    private RepeatStatement instance;

    @Before
    public void setUp()
    {
        random = new Random();

        while (timesToRepeat == 0)
        {
            timesToRepeat = random.nextInt(100);
        }

        int nextInt = random.nextInt(100_000);
        methodName = "test" + String.valueOf(nextInt);

        when(method.getName()).thenReturn(methodName);
        when(statementFactory.get()).thenReturn(statement);

        instance = new RepeatStatement(timesToRepeat, statementFactory, method);
    }

    @Test
    public void testConstructor() throws Exception
    {
        assertThrows(() -> new RepeatStatement(0, statementFactory, method))
                .isInstanceOf(IllegalArgumentException.class);

        assertThrows(() -> new RepeatStatement(-1, statementFactory, method))
                .isInstanceOf(IllegalArgumentException.class);

        assertThrows(() -> new RepeatStatement(-5, statementFactory, method))
                .isInstanceOf(IllegalArgumentException.class);

        assertThrows(() -> new RepeatStatement(5, null, method))
                .isInstanceOf(IllegalArgumentException.class);

        assertThrows(() -> new RepeatStatement(5, () -> null, method))
                .isInstanceOf(IllegalArgumentException.class);

        assertThrows(() -> new RepeatStatement(5, statementFactory, null))
                .isInstanceOf(IllegalArgumentException.class);

    }

    @Test
    public void testEvaluate() throws Exception, Throwable
    {
        instance.evaluate();
        
        //+1 to include the call made during the constructor
        verify(statementFactory, times(timesToRepeat + 1)).get();
        verify(statement, times(timesToRepeat)).evaluate();
    }
    
    @Test
    public void testEvaluateWhenFactoryReturnsNull() throws Throwable
    {
        when(statementFactory.get()).thenReturn(null);
        
        assertThrows(() -> instance.evaluate())
                .isInstanceOf(IllegalArgumentException.class);
    }

    @Test
    public void testToString()
    {
    }

}
