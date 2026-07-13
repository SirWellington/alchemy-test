package tech.sirwellington.alchemy.test;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

import org.junit.jupiter.api.extension.ExtendWith;
import tech.sirwellington.alchemy.test.generation.AlchemyDataExtension;

/**
 * Apply this annotation to a JUnit 5+ test class to allow you to use
 * the Data generation annotations, such as {@link tech.sirwellington.alchemy.test.generation.GenerateString}.
 *
 * @see tech.sirwellington.alchemy.test.generation.GenerateString
 * @see tech.sirwellington.alchemy.test.generation.GenerateInteger
 * @see tech.sirwellington.alchemy.test.generation.GeneratePojo
 * @author SirWellington
 */
@Target(ElementType.TYPE)
@Retention(RetentionPolicy.RUNTIME)
@ExtendWith({
    AlchemyDataExtension.class,      // Handles @GenerateString, @GenerateInteger, etc.
    AlchemyLoggingExtension.class    // Prints test name to console
})
public @interface AlchemyTest {}