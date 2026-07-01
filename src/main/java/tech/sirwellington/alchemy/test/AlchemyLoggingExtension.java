package tech.sirwellington.alchemy.test;

import org.junit.jupiter.api.extension.BeforeTestExecutionCallback;
import org.junit.jupiter.api.extension.ExtensionContext;

class AlchemyLoggingExtension implements BeforeTestExecutionCallback {
    @Override
    public void beforeTestExecution(ExtensionContext context) throws Exception {
        String testName = context.getDisplayName();
        System.out.println("🧪 Running test: " + testName);
    }
}
