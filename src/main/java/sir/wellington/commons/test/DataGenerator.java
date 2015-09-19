package sir.wellington.commons.test;

import static com.google.common.base.Charsets.UTF_8;
import com.google.common.base.Preconditions;
import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.UUID;
import java.util.function.Supplier;
import org.apache.commons.lang3.RandomStringUtils;
import org.apache.commons.lang3.RandomUtils;
import org.apache.commons.math3.random.RandomDataGenerator;

/**
 * A Data Generator generates a series of Objects for use in testing scenarios. Common generators
 * exist for:
 * <pre>
 * + Integers
 * + Longs
 * + Doubles
 * + Booleans
 * + Binary
 * + Strings
 *      + Alphabetics
 *      + Hexadecimal
 *      + UUIDs
 * + Enum Values
 * + Lists of the above
 * + Maps of the above
 * </pre>
 *
 * Examples:
 *
 * <pre>
 * Get a positive integer:
 *
 * {@code
 *  int positive = oneOf(positiveIntegers());
 * }
 * </pre>
 *
 * @author SirWellington
 *
 * @param <T> The type of the Object to generate.
 */
@FunctionalInterface
public interface DataGenerator<T> extends Supplier<T>
{

    /**
     * Generate a non-null value of type {@code T}
     * <p>
     * @return
     */
    @Override
    T get();

    /**
     * Calls the generator once to get the ones of its values.
     *
     * @param <T>
     * @param generator
     *
     * @return Only one value from the generator.
     */
    static <T> T oneOf(DataGenerator<T> generator)
    {
        Preconditions.checkNotNull(generator);
        return generator.get();
    }

    /**
     * Creates a series of integer values.
     *
     * @param inclusiveLowerBound The inclusive lower bound
     * @param exclusiveUpperBound The exclusive upper bound
     *
     * @return
     *
     * @throws IllegalArgumentException If {@code lowerBound >= upperBound}
     */
    static DataGenerator<Integer> integers(int inclusiveLowerBound, int exclusiveUpperBound) throws IllegalArgumentException
    {
        Preconditions.checkArgument(inclusiveLowerBound <= exclusiveUpperBound, "Upper Bound must be greater than Lower Bound");
        final boolean negativeLowerBound = inclusiveLowerBound < 0;
        final boolean negativeUpperBound = exclusiveUpperBound < 0;

        return () ->
        {

            if (negativeLowerBound && negativeUpperBound)
            {
                return -RandomUtils.nextInt(-exclusiveUpperBound + 1, -inclusiveLowerBound + 1);
            }
            else if (negativeLowerBound)
            {
                boolean shouldProduceNegative = booleans().get();
                if (shouldProduceNegative)
                {
                    return -RandomUtils.nextInt(0, -inclusiveLowerBound + 1);
                }
                else
                {
                    return RandomUtils.nextInt(0, exclusiveUpperBound);
                }
            }
            else //Positive bounds
            {
                return RandomUtils.nextInt(inclusiveLowerBound, exclusiveUpperBound);
            }
        };

    }

    /**
     * Creates a series of positive integer values from 1 to Integer.MAX_VALUE
     *
     * @return
     * @see #smallPositiveIntegers()
     * @see #positiveLongs()
     */
    static DataGenerator<Integer> positiveIntegers()
    {
        return integers(1, Integer.MAX_VALUE);
    }

    /**
     * Creates a series of small positive integers from 1 to 1000.
     *
     * @return
     * @see #positiveIntegers()
     */
    static DataGenerator<Integer> smallPositiveIntegers()
    {
        return integers(1, 1000);
    }

    /**
     * Creates a series of negative integer values from Integer.MIN_VALUE to -1
     *
     * @return
     */
    static DataGenerator<Integer> negativeIntegers()
    {
        return () ->
        {
            int value = positiveIntegers().get();
            return value < 0 ? value : -value;
        };
    }

    /**
     * Produces long values within the specified Range
     *
     * @param inclusiveLowerBound inclusive lower bound
     * @param exclusiveUpperBound exclusive upper bound
     *
     * @return
     *
     * @throws IllegalArgumentException If {@code lowerBound >= upperBound}
     */
    static DataGenerator<Long> longs(long inclusiveLowerBound, long exclusiveUpperBound) throws IllegalArgumentException
    {
        Preconditions.checkArgument(inclusiveLowerBound <= exclusiveUpperBound, "Upper Bound must be greater than Lower Bound");
        final boolean negativeLowerBound = inclusiveLowerBound < 0;
        final boolean negativeUpperBound = exclusiveUpperBound < 0;

        return () ->
        {

            if (negativeLowerBound && negativeUpperBound)
            {
                return -RandomUtils.nextLong(-exclusiveUpperBound + 1, -inclusiveLowerBound + 1);
            }
            else if (negativeLowerBound)
            {
                boolean shouldProduceNegative = booleans().get();
                if (shouldProduceNegative)
                {
                    return -RandomUtils.nextLong(0, -inclusiveLowerBound + 1);
                }
                else
                {
                    return RandomUtils.nextLong(0, exclusiveUpperBound);
                }
            }
            else //Positive bounds
            {
                return RandomUtils.nextLong(inclusiveLowerBound, exclusiveUpperBound);
            }
        };
    }

    /**
     * Produces a series of positive values from {@code 1} to {@code Long.MAX_VALUE}
     *
     * @return
     * @see #smallPositiveLongs()
     * @see #positiveIntegers()
     */
    static DataGenerator<Long> positiveLongs()
    {
        return longs(1L, Long.MAX_VALUE);
    }

    /**
     * Produces a series of positive values from 1 to 10,000
     *
     * @return
     *
     * @see #positiveLongs()
     * @see #positiveLongs()
     */
    static DataGenerator<Long> smallPositiveLongs()
    {
        return longs(1L, 10_000L);
    }

    /**
     * Creates a series of double values within the specified range
     *
     * @param inclusiveLowerBound The inclusive lower bound
     * @param inclusiveUpperBound The inclusive upper bound
     *
     * @return
     *
     * @throws IllegalArgumentException If {@code lowerBound >= upperBound}
     */
    static DataGenerator<Double> doubles(double inclusiveLowerBound, double inclusiveUpperBound) throws IllegalArgumentException
    {
        Preconditions.checkArgument(inclusiveLowerBound <= inclusiveUpperBound, "Upper Bound must be greater than Lower Bound");
        final boolean negativeLowerBound = inclusiveLowerBound < 0;
        final boolean negativeUpperBound = inclusiveUpperBound < 0;

        return () ->
        {
            if (negativeLowerBound && negativeUpperBound)
            {
                return -RandomUtils.nextDouble(-inclusiveUpperBound, -inclusiveLowerBound);
            }
            else if (negativeLowerBound)
            {
                boolean shouldProduceNegative = booleans().get();
                if (shouldProduceNegative)
                {
                    return -RandomUtils.nextDouble(0, -inclusiveLowerBound);
                }
                else
                {
                    return RandomUtils.nextDouble(0, inclusiveUpperBound);
                }
            }
            else //Positive bounds
            {
                return RandomUtils.nextDouble(inclusiveLowerBound, inclusiveUpperBound);
            }
        };
    }

    /**
     * Creates a series of positive double values from 0 to Double.MAX_VALUE.
     *
     * @return
     * @see #smallPositiveDoubles()
     * @see #positiveIntegers()
     */
    static DataGenerator<Double> positiveDoubles()
    {
        return doubles(0.1, Double.MAX_VALUE);
    }

    /**
     * Creates a series of positive doubles from 0.1 to 1000.0
     *
     * @return
     *
     * @see #positiveDoubles()
     * @see #positiveIntegers()
     */
    static DataGenerator<Double> smallPositiveDoubles()
    {
        return doubles(0.1, 1000);
    }

    /**
     * Generates a series of booleans.
     *
     * @return
     */
    static DataGenerator<Boolean> booleans()
    {
        return () -> RandomUtils.nextInt(0, 2) == 1;
    }

    //=========================Strings=====================================
    /**
     * Generates a random string of specified length. Characters are included from all sets.
     *
     * @param length The length of the String, must be at least 1.
     *
     * @return
     */
    static DataGenerator<String> strings(int length)
    {
        Preconditions.checkArgument(length > 0, "Length must be at least 1");
        return () -> RandomStringUtils.random(length);
    }

    /**
     * Generates a random hexadecimal string.
     *
     * @param length The length of the String, must be at least 1.
     *
     * @return
     */
    static DataGenerator<String> hexadecimalString(int length)
    {
        Preconditions.checkArgument(length > 0, "Length must be at least 1");
        return () -> new RandomDataGenerator().nextHexString(length);
    }

    /**
     * Generates a random alphabetic string.
     *
     * @param length The length of the String, must be at least 1.
     *
     * @return
     * @see #alphabeticString()
     */
    static DataGenerator<String> alphabeticString(int length)
    {
        Preconditions.checkArgument(length > 0, "Length must be at least 1");
        return () -> RandomStringUtils.randomAlphabetic(length);
    }

    /**
     * Generates a random alphabetic string anywhere between {@code 5 - 20} characters. Well suited
     * for the case when you don't really care for the size of the string returned.
     *
     * @return
     * @see #alphabeticString(int)
     */
    static DataGenerator<String> alphabeticString()
    {
        return alphabeticString(oneOf(integers(5, 20)));
    }

    /**
     * Generates random {@linkplain UUID UUIDs}.
     */
    static DataGenerator<String> uuids = () -> UUID.randomUUID().toString();

    //======================From Fixed targets=============================
    /**
     * Generates a string value from the specified set.
     *
     * @param values
     *
     * @return
     */
    static DataGenerator<String> stringsFromFixedList(List<String> values)
    {
        Preconditions.checkNotNull(values);
        Preconditions.checkArgument(!values.isEmpty(), "No values specified");
        return () ->
        {
            int index = integers(0, values.size()).get();
            return values.get(index);
        };
    }

    /**
     * Generates a string value from the specified set.
     *
     * @param values
     *
     * @return
     */
    static DataGenerator<String> stringsFromFixedList(String... values)
    {
        Preconditions.checkNotNull(values);
        Preconditions.checkArgument(values.length != 0, "No values specified");
        return stringsFromFixedList(Arrays.asList(values));
    }

    /**
     * Generates an integer value from the specified set.
     *
     * @param values
     *
     * @return
     */
    static DataGenerator<Integer> integersFromFixedList(List<Integer> values)
    {
        Preconditions.checkNotNull(values);
        Preconditions.checkArgument(!values.isEmpty(), "No values specified");
        return () ->
        {
            int index = integers(0, values.size()).get();
            return values.get(index);
        };
    }

    /**
     * Generates a double value from the specified set.
     *
     * @param values
     *
     * @return
     */
    static DataGenerator<Double> doublesFromFixedList(List<Double> values)
    {
        Preconditions.checkNotNull(values);
        Preconditions.checkArgument(!values.isEmpty(), "No values specified");
        return () ->
        {
            int index = integers(0, values.size()).get();
            return values.get(index);
        };
    }

    /**
     * Generates binary of the specified length
     *
     * @param bytes The size of the byte arrays created.
     *
     * @return A binary generator
     */
    static DataGenerator<byte[]> binaryGenerator(int bytes)
    {
        Preconditions.checkArgument(bytes > 0, "bytes must be at least 1");
        return () ->
        {
            String string = alphabeticString(bytes).get();
            return string.getBytes(UTF_8);
        };
    }

    /**
     * Returns a list of Data using the specified generator of varying size.
     *
     * @param <T>
     * @param generator The generator that produces values
     *
     * @return A list of random values, the length of which will vary.
     */
    static <T> List<T> listOf(DataGenerator<T> generator)
    {
        return listOf(generator, integers(5, 200).get());
    }

    /**
     * Retrieves a list of Data using the specified generator.
     *
     * @param <T>       The type to generate
     * @param generator The generator that produces values
     * @param size      The size of the list
     *
     * @return A list of random values with length {code size}
     *
     * @throws IllegalArgumentException if size is less than 1.
     */
    static <T> List<T> listOf(DataGenerator<T> generator, int size)
    {
        Preconditions.checkArgument(size > 0, "Size must be at least 1");
        Preconditions.checkNotNull(generator, "generator is null");
        List<T> list = Lists.newArrayListWithExpectedSize(size);
        for (int i = 0; i < size; ++i)
        {
            list.add(generator.get());
        }
        return list;
    }

    /**
     * Creates a Map using the Keys and Values generated by the provided Generators.
     *
     * @param <K>            The Type of the Keys
     * @param <V>            The Type of the Values
     *
     * @param keyGenerator   Generates keys for the Map
     * @param valueGenerator Generates values for the Map
     * @param size           The exact size of the created map
     *
     * @return Map generated from the parameters specified.Ï
     */
    static <K, V> Map<K, V> mapOf(DataGenerator<K> keyGenerator, DataGenerator<V> valueGenerator, int size)
    {
        Preconditions.checkArgument(size > 0, "size must be at least 1");
        Preconditions.checkNotNull(keyGenerator);
        Preconditions.checkNotNull(valueGenerator);
        Map<K, V> map = Maps.newHashMapWithExpectedSize(size);

        for (int i = 0; i < size; ++i)
        {
            K key = keyGenerator.get();
            V vaue = valueGenerator.get();
            map.put(key, vaue);
        }

        return map;
    }

    /**
     * Returns sequence of Enum values from the supplied arguments.
     * <pre>
     * Example:
     *
     * {@code
     * enum Fruit {APPLE, ORANGE, PEAR}
     *
     * Fruit someFruit = enumValueOf(Fruit.class).get();
     * }
     * </pre>
     *
     * @param <E>       The type of the Enum
     * @param enumClass The {@code class} of the Enum.
     *
     * @return A generator that produces values of the supplied enum type.
     */
    static <E extends Enum> Supplier<E> enumValueOf(Class<E> enumClass)
    {
        Preconditions.checkNotNull(enumClass);
        E[] constants = enumClass.getEnumConstants();
        Preconditions.checkArgument(constants != null, "Class is not an Enum: " + enumClass.getName());
        Preconditions.checkArgument(constants.length > 0, "Enum has no values");
        return () ->
        {
            int index = integers(0, constants.length).get();
            return constants[index];
        };
    }
}
