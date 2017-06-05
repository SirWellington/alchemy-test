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

import org.junit.runners.model.FrameworkMethod;
import org.junit.runners.model.Statement;
import tech.sirwellington.alchemy.annotations.access.Internal;
import tech.sirwellington.alchemy.annotations.concurrency.Immutable;

import static tech.sirwellington.alchemy.test.Checks.Internal.checkNotNull;
import static tech.sirwellington.alchemy.test.Checks.Internal.checkThat;

/**
 * @author SirWellington
 */
@Internal
@Immutable
class RepeatStatement extends Statement
{

    private final int timesToRepeat;
    private final Provider<Statement> statementFactory;
    private final FrameworkMethod method;

    public RepeatStatement(int timesToRepeat, Provider<Statement> statementFactory, FrameworkMethod method)
    {
        checkThat(timesToRepeat > 0, "timesToRepeat must be > 0");
        checkNotNull(statementFactory);
        checkNotNull(statementFactory.get(), "statementFactory returned null");
        checkNotNull(method);

        this.timesToRepeat = timesToRepeat;
        this.statementFactory = statementFactory;
        this.method = method;
    }

    @Override
    public void evaluate() throws Throwable
    {
        //Print blank line
        System.out.println();

        //Print test name
        String methodName = method.getName();
        System.out.println(methodName + "()");

        //Time the test run
        long start = System.currentTimeMillis();
        for (int i = 0; i < timesToRepeat; ++i)
        {
            Statement delegateStatement = statementFactory.get();
            checkNotNull(delegateStatement, "statementFactory returned null Statement");
            delegateStatement.evaluate();
        }
        long end = System.currentTimeMillis();

        System.out.printf("  Duration: %dms\n", end - start);

        if (timesToRepeat > 1)
        {
            System.out.printf("  Runs: %d\n", timesToRepeat);
        }
    }

    @Override
    public String toString()
    {
        return "RepeatStatement{" + "timesToRepeat=" + timesToRepeat + ", statementFactory=" + statementFactory + ", method=" + method + '}';
    }

}
