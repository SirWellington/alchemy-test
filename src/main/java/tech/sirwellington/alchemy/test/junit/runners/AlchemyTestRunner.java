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

import java.util.function.Supplier;
import org.junit.runner.notification.RunNotifier;
import org.junit.runners.BlockJUnit4ClassRunner;
import org.junit.runners.model.FrameworkMethod;
import org.junit.runners.model.InitializationError;
import org.junit.runners.model.Statement;
import org.junit.runners.model.TestClass;
import org.mockito.MockitoAnnotations;
import org.mockito.internal.runners.util.FrameworkUsageValidator;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import static org.junit.Assert.fail;

/**
 *
 * @author SirWellington
 */
public class AlchemyTestRunner extends BlockJUnit4ClassRunner
{

    private final static Logger LOG = LoggerFactory.getLogger(AlchemyTestRunner.class);

    //If false, we won't initialize Mockito's mocks
    private boolean shouldInitMocks = true;

    public AlchemyTestRunner(Class<?> klass) throws InitializationError
    {
        super(klass);
        shouldInitMocks = shouldInitMockitoMocks();
    }

    @Override
    protected Statement withBefores(FrameworkMethod method, Object target, Statement statement)
    {
        Statement superStatement = super.withBefores(method, target, statement);

        return new Statement()
        {
            @Override
            public void evaluate() throws Throwable
            {
                if (shouldInitMocks)
                {
                    MockitoAnnotations.initMocks(target);
                }
                superStatement.evaluate();
            }
        };
    }

    @Override
    protected Statement methodBlock(FrameworkMethod method)
    {
        int timesToRun = determineTimesToRun(method);
        Supplier<Statement> statementFactory = () -> super.methodBlock(method);

        return new RepeatStatement(timesToRun, statementFactory, method);
    }

    @Override
    public void run(RunNotifier notifier)
    {
        if (shouldInitMocks)
        {
            //Allow Mockito to do its verification
            notifier.addListener(new FrameworkUsageValidator(notifier));
        }

        super.run(notifier);
    }

    private int determineTimesToRun(FrameworkMethod method)
    {
        Times methodAnnotation = method.getAnnotation(Times.class);

        if (methodAnnotation != null)
        {
            int value = methodAnnotation.value();

            if (value <= 0)
            {
                fail(method.getName() + " annotated with a negative @Times");
            }

            return value;
        }

        Times testAnnotation = getTestClass().getAnnotation(Times.class);

        if (testAnnotation != null)
        {
            int value = testAnnotation.value();

            if (value <= 0)
            {
                fail(getTestClass().getName() + " annotated with a negative @Times");
            }

            return value;
        }

        return 1;
    }

    private boolean shouldInitMockitoMocks()
    {
        TestClass testClass = this.getTestClass();
        InitMocks initMocks = testClass.getAnnotation(InitMocks.class);

        if (initMocks != null)
        {
            return initMocks.value();
        }

        return true;
    }

}
