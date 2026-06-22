package tech.sirwellington.alchemy.test.junit;

import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;
import tech.sirwellington.alchemy.test.junit.generation.AlchemyDataExtension;

import java.lang.annotation.*;

@Target(ElementType.TYPE)
@Retention(RetentionPolicy.RUNTIME)
@ExtendWith({
    MockitoExtension.class,          // Handles @Mock, @InjectMocks, & unnecessary stubbings
    AlchemyDataExtension.class,      // Handles @GenerateString, @GenerateInteger, etc.
    AlchemyLoggingExtension.class    // Prints test name to console
})
public @interface AlchemyTest {}