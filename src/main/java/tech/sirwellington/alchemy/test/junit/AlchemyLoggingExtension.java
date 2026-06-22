package tech.sirwellington.alchemy.test.junit;

import org.junit.jupiter.api.extension.BeforeTestExecutionCallback;
import org.junit.jupiter.api.extension.ExtensionContext;

public class AlchemyLoggingExtension implements BeforeTestExecutionCallback {
    @Override
    public void beforeTestExecution(ExtensionContext context) throws Exception {
        String testName = context.getDisplayName();
        System.out.println("🧪 Running test: " + testName);
    }
}
