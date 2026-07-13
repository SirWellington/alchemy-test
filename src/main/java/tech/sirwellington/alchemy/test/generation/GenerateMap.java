package tech.sirwellington.alchemy.test.generation;

import tech.sirwellington.alchemy.annotations.access.Internal;
import tech.sirwellington.alchemy.annotations.access.NonInstantiable;
import tech.sirwellington.alchemy.generator.*;

import java.lang.annotation.Retention;
import java.lang.annotation.Target;
import java.nio.ByteBuffer;
import java.time.Instant;
import java.util.Date;
import java.util.Map;

import static java.lang.annotation.ElementType.FIELD;
import static java.lang.annotation.RetentionPolicy.RUNTIME;
import static tech.sirwellington.alchemy.test.internal.Checks.checkNotNull;
import static tech.sirwellington.alchemy.test.internal.Checks.checkThat;

/**
 * Used in conjunction with {@link tech.sirwellington.alchemy.test.AlchemyTest}.
 * This annotation allows the Runtime injection of {@link java.util.Map} values
 * using {@link tech.sirwellington.alchemy.generator.CollectionGenerators} from the {@link tech.sirwellington.alchemy.generator.AlchemyGenerator}
 * library.
 * <br>
 * Example:
 * {@snippet :
 * @AlchemyTest
 * public class ExampleTest {
 *   @GenerateMap(String.class, String.class)
 *   private Map<String, String> map;
 * }
 * }
 * @author SirWellington
 * @see GenerateList
 * @see GeneratePojo
 */
@Target(FIELD)
@Retention(RUNTIME)
public @interface GenerateMap {
    /**
     * Specify the generic type of the key. This is necessary since the type information is erased at Runtime.
     */
    Class<?> keyType();

    /**
     * Specify the generic type of the key. This is necessary since the type information is erased at Runtime.
     */
    Class<?> valueType();

    /**
     * The number of elements to include in the list. Defaults to 10. This number must be {@code > 0};
     */
    int size() default 10;

    @Internal
    @NonInstantiable
    class Values {
        private Values() throws IllegalAccessException {
            throw new IllegalAccessException("cannot directly instantiate");
        }

        static AlchemyGenerator<? extends Map<?,?>> createGeneratorFor(GenerateMap annotation) {
            checkNotNull(annotation, "missing annotation");
            final int size = annotation.size();
            checkThat(size > 0, "size must be > 0");
            var keyType = annotation.keyType();
            var valueType = annotation.valueType();
            checkNotNull(keyType, "keyType cannot be null");
            checkNotNull(valueType, "valueType cannot be null");

            var keyGenerator = determineGeneratorForType(keyType);
            var valueGenerator = determineGeneratorForType(valueType);
            return CollectionGenerators.mapGeneratorOf(
                keyGenerator,
                valueGenerator,
                size
            );
        }

        private static AlchemyGenerator<?> determineGeneratorForType(Class<?> type) {
            checkNotNull(type, "type cannot be null");
            return switch (type) {
                case Class<?> cls when cls == String.class -> StringGenerators.alphabeticStrings();
                case Class<?> cls when cls == Integer.class -> NumberGenerators.smallPositiveIntegers();
                case Class<?> cls when cls == Long.class -> NumberGenerators.positiveLongs();
                case Class<?> cls when cls == Double.class -> NumberGenerators.positiveDoubles();
                case Class<?> cls when cls == Date.class -> DateGenerators.anyTime();
                case Class<?> cls when cls == Instant.class -> TimeGenerators.anyTime();
                case Class<?> cls when cls == Boolean.class -> BooleanGenerators.booleans();
                case Class<?> cls when cls == ByteBuffer.class -> BinaryGenerators.byteBuffers(1024);
                default -> ObjectGenerators.pojos(type);
            };
        }
    }
}
