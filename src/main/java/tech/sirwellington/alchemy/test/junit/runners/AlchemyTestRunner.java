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
package tech.sirwellington.alchemy.test.junit.runners;

import org.junit.runner.notification.RunNotifier;
import org.junit.runners.BlockJUnit4ClassRunner;
import org.junit.runners.model.*;
import org.mockito.*;
import org.mockito.internal.junit.UnnecessaryStubbingsReporter;
import org.mockito.internal.runners.util.FailureDetector;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Alchemy Test Runner Features:
 * <p>
 * <ul>
 * <li> Initializes Mockito {@linkplain Mock @Mocks}
 * <li> Prints out the testName to the console using {@code System.out.println()}
 * <li> Can repeat your tests using the {@linkplain  Repeat @Repeat} annotation
 * <li> Initialize generated Data Using {@link GenerateString}, {@link GenerateInteger}, etc..
 * <p>
 * </ul>
 *
 * @author SirWellington
 * @see <a href="https://github.com/SirWellington/alchemy-test">https://github.com/SirWellington/alchemy-test</a>
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
    protected Statement withBefores(FrameworkMethod method, final Object target, Statement statement)
    {
        final Statement superStatement = super.withBefores(method, target, statement);

        return new Statement()
        {
            @Override
            public void evaluate() throws Throwable
            {
                if (shouldInitMocks)
                {
                    MockitoAnnotations.initMocks(target);
                }

                TestClassInjectors.populateGeneratedFields(getTestClass(), target);

                superStatement.evaluate();
            }
        };
    }

    @Override
    protected Statement methodBlock(final FrameworkMethod method)
    {
        int timesToRun = determineTimesToRun(method);

        Provider<Statement> statementFactory = new Provider<Statement>()
        {
            @Override
            public Statement get()
            {
                return AlchemyTestRunner.super.methodBlock(method);
            }
        };

        return new RepeatStatement(timesToRun, statementFactory, method);
    }

    @Override
    public void run(RunNotifier notifier)
    {
        if (shouldInitMocks)
        {
            //Allow Mockito to do its verification
            UnnecessaryStubbingsReporter reporter = new UnnecessaryStubbingsReporter();
            FailureDetector listener = new FailureDetector();

            Mockito.framework().addListener(reporter);

            try
            {
                notifier.addListener(listener);
            }
            finally
            {
                Mockito.framework().removeListener(reporter);
            }
        }

        super.run(notifier);
    }

    private int determineTimesToRun(FrameworkMethod method)
    {

        DontRepeat dontRepeatAnnotation = method.getAnnotation(DontRepeat.class);

        if (dontRepeatAnnotation != null)
        {
            return 1;
        }

        Repeat repeatAnnotationOnMethod = method.getAnnotation(Repeat.class);

        if (repeatAnnotationOnMethod != null)
        {
            int value = repeatAnnotationOnMethod.value();

            if (value <= 0)
            {
                LOG.error(method.getName() + " annotated with a negative @Times. Defaulting to 1");
                value = 1;
            }

            return value;
        }

        Repeat repeatAnnotationOnClass = getTestClass().getAnnotation(Repeat.class);

        if (repeatAnnotationOnClass != null)
        {
            int value = repeatAnnotationOnClass.value();

            if (value <= 0)
            {
                LOG.error(getTestClass().getName() + " annotated with a negative @Times. Defaulting to 1");
                value = 1;
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
