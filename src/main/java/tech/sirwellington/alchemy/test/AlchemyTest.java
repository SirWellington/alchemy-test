package tech.sirwellington.alchemy.test;

import org.junit.jupiter.api.extension.ExtendWith;
import tech.sirwellington.alchemy.test.generation.AlchemyDataExtension;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Target(ElementType.TYPE)
@Retention(RetentionPolicy.RUNTIME)
@ExtendWith({
    AlchemyDataExtension.class,      // Handles @GenerateString, @GenerateInteger, etc.
    AlchemyLoggingExtension.class    // Prints test name to console
})
public @interface AlchemyTest {}