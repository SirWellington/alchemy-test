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
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.mockito.internal.runners.util.FrameworkUsageValidator;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Alchemy Test Runner Features:
 *
 * <ul>
 * <li> Initializes Mockito {@linkplain Mock @Mocks}
 * <li> Prints out the testName to the console using {@code System.out.println()}
 * <li> Can repeat your tests using the {@linkplain  Repeat @Repeat} annotation.
 *
 * </ul>
 *
 * @see <a href="https://github.com/SirWellington/alchemy-test">https://github.com/SirWellington/alchemy-test</a>
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
                
                AlchemyGeneratorAnnotations.populateGeneratedFields(getTestClass(), target);
                
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
