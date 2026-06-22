package tech.sirwellington.alchemy.test.junit.generation;

import org.junit.jupiter.api.extension.BeforeEachCallback;
import org.junit.jupiter.api.extension.ExtensionContext;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import tech.sirwellington.alchemy.annotations.access.Internal;
import tech.sirwellington.alchemy.annotations.access.NonInstantiable;
import tech.sirwellington.alchemy.generator.DateGenerators;

import java.lang.annotation.Annotation;
import java.lang.reflect.Field;
import java.util.Arrays;
import java.util.List;

import static tech.sirwellington.alchemy.test.internal.Checks.checkNotNull;
import static tech.sirwellington.alchemy.test.internal.Checks.checkThat;

public class AlchemyDataExtension implements BeforeEachCallback {
    @Override
    public void beforeEach(ExtensionContext context) {
        var testInstance = context.getRequiredTestInstance();
        try {
            TestClassInjectors.populateGeneratedFields(testInstance.getClass(), testInstance);
        } catch (IllegalAccessException ex) {
            throw new RuntimeException("Failed to setup test: " + testInstance, ex);
        }
    }

    @Internal
    @NonInstantiable
    static final class TestClassInjectors {

        private final static Logger LOG = LoggerFactory.getLogger(TestClassInjectors.class);

        static <A extends Annotation> List<Field> getFieldsAnnotatedWith(Class<A> annotation, Object target) {
                checkNotNull(target);
                var clazz = target.getClass();
                return Arrays.stream(clazz.getFields())
                    .filter(f -> f.isAnnotationPresent(annotation))
                    .toList();
        }
        
        static void populateGeneratedFields(Class<?> testClass, Object target) throws IllegalArgumentException,
                                                                                      IllegalAccessException {
            //@GenerateString
            var stringGeneratedFields = getFieldsAnnotatedWith(GenerateString.class, target);
            for (Field field : stringGeneratedFields) {
                inflateString(field, target);
            }

            //@GenerateBoolean
            var booleanGeneratedFields =  getFieldsAnnotatedWith(GenerateBoolean.class, target);
            for (Field field : booleanGeneratedFields) {
                inflateBoolean(field, target);
            }

            //@GenerateInteger
            var integerGeneratedFields = getFieldsAnnotatedWith(GenerateInteger.class, target);
            for (Field field : integerGeneratedFields) {
                inflateInteger(field, target);
            }

            //@GenerateLong
            var longGeneratedFields = getFieldsAnnotatedWith(GenerateLong.class, target);
            for (Field field : longGeneratedFields) {
                inflateLong(field, target);
            }

            //@GenerateFloat
            var floatGeneratedFields = getFieldsAnnotatedWith(GenerateFloat.class, target);
            for (Field field : floatGeneratedFields) {
                inflateFloat(field, target);
            }

            //@GenerateDouble
            var doubleGeneratedFields = getFieldsAnnotatedWith(GenerateDouble.class, target);
            for (Field field : doubleGeneratedFields) {
                inflateDouble(field, target);
            }

            //@GenerateDate
            var dateGeneratedFields = getFieldsAnnotatedWith(GenerateDate.class, target);
            for (Field field : dateGeneratedFields) {
                inflateDate(field, target);
            }

            //@GenerateURL
            var urlGeneratedFields = getFieldsAnnotatedWith(GenerateURL.class, target);
            for (Field field : urlGeneratedFields) {
                inflateUrl(field, target);
            }

            //@GeneratePojo
            var pojoGeneratedFields = getFieldsAnnotatedWith(GeneratePojo.class, target);
            for (Field field : pojoGeneratedFields) {
                inflatePojo(field, target);
            }

            //@GenerateEnum
            var enumGeneratedFields = getFieldsAnnotatedWith(GenerateEnum.class, target);
            for (Field field : enumGeneratedFields) {
                inflateEnum(field, target);
            }

            //@GenerateList
            var listGeneratedFields = getFieldsAnnotatedWith(GenerateList.class, target);
            for (Field field : listGeneratedFields) {
                inflateList(field, target);
            }

            //@GenerateCustom
            var customGeneratedFields = getFieldsAnnotatedWith(GenerateCustom.class, target);
            for (Field field : customGeneratedFields) {
                inflateCustom(field, target);
            }
        }

        private static void inflateString(Field field, Object target) throws IllegalArgumentException,
                                                                                      IllegalAccessException {
            var annotation = field.getAnnotation(GenerateString.class);
            checkNotNull(annotation, "missing annotation");

            var generator = GenerateString.Values.createGeneratorFor(annotation);
            var value = generator.get();
            inflate(field, target, value);
        }

        private static void inflateBoolean(Field field, Object target) throws IllegalArgumentException,
                                                                                       IllegalAccessException {
            var annotation = field.getAnnotation(GenerateBoolean.class);
            checkNotNull(annotation, "missing annotation: @GenerateBoolean");

            var generator = GenerateBoolean.Values.createGeneratorFor(annotation);
            var value = generator.get();
            inflate(field, target, value);
        }

        private static void inflateInteger(Field field, Object target) throws IllegalArgumentException,
                                                                                       IllegalAccessException {
            var annotation = field.getAnnotation(GenerateInteger.class);
            checkNotNull(annotation, "missing annotation");

            var generator = GenerateInteger.Values.createGeneratorFor(annotation);
            var value = generator.get();
            inflate(field, target, value);

        }

        private static void inflateLong(Field field, Object target) throws IllegalAccessException {
            var annotation = field.getAnnotation(GenerateLong.class);
            checkNotNull(annotation, "missing annotation");

            var generator = GenerateLong.Values.createGeneratorFor(annotation);
            var value = generator.get();
            inflate(field, target, value);
        }

        private static void inflateFloat(Field field, Object target) throws IllegalAccessException {
            var annotation = field.getAnnotation(GenerateFloat.class);
            checkNotNull(annotation, "missing annotation");

            var generator = GenerateFloat.Values.createGeneratorFor(annotation);
            var value = generator.get();
            inflate(field, target, value);
        }

        private static void inflateDouble(Field field, Object target) throws IllegalAccessException {
            var annotation = field.getAnnotation(GenerateDouble.class);
            checkNotNull(annotation, "missing annotation");

            var  generator = GenerateDouble.Values.createGeneratorFor(annotation);
            var value = generator.get();
            inflate(field, target, value);
        }

        private static void inflateDate(Field field, Object target) throws IllegalArgumentException,
                                                                                    IllegalAccessException {
            var annotation = field.getAnnotation(GenerateDate.class);
            checkNotNull(annotation, "missing annotation");

            var generator = GenerateDate.Values.createGeneratorFor(annotation);

            Object value;

            if (field.getType() == java.sql.Timestamp.class) {
                var  timestampGenerator = DateGenerators.toSqlTimestampGenerator(generator);
                value = timestampGenerator.get();
            }
            else if (field.getType() == java.sql.Date.class) {
                var sqlGenerator = DateGenerators.toSqlTimestampGenerator(generator);
                value = sqlGenerator.get();
            }
            else {
                value = generator.get();
            }

            inflate(field, target, value);
        }

        private static void inflateUrl(Field field, Object target) throws IllegalAccessException {
            var annotation = field.getAnnotation(GenerateURL.class);
            checkNotNull(annotation, "missing annotation");

            var generator = GenerateURL.Values.createGeneratorFor(annotation);
            var value = generator.get();
            inflate(field, target, value);
        }

        private static void inflatePojo(Field field, Object target) throws IllegalArgumentException,
                                                                                    IllegalAccessException {
            var typeOfPojo = field.getType();
            var annotation = field.getAnnotation(GeneratePojo.class);
            var generator = GeneratePojo.Values.createGeneratorFor(annotation, typeOfPojo);
            var value = generator.get();
            inflate(field, target, value);
        }

        private static void inflateEnum(Field field, Object target) throws IllegalArgumentException,
                                                                                    IllegalAccessException {
            var typeOfField = field.getType();
            checkThat(typeOfField.isEnum(), "@GenerateEnum can only be used on Enum Types");
            var typeOfEnum = (Class<? extends Enum>) typeOfField;
            var annotation = field.getAnnotation(GenerateEnum.class);
            var generator = GenerateEnum.Values.createGeneratorFor(annotation, typeOfEnum);
            var value = generator.get();
            inflate(field, target, value);
        }

        private static void inflateList(Field field, Object target) throws IllegalArgumentException,
                                                                                    IllegalAccessException {
            var typeOfField = field.getType();
            checkThat(List.class.isAssignableFrom(typeOfField), "@GenerateList can only be used on List Types");

            var annotation = field.getAnnotation(GenerateList.class);
            var generator = GenerateList.Values.createGeneratorFor(annotation);
            var value = generator.get();
            inflate(field, target, value);
        }

        private static void inflateCustom(Field field, Object target) throws IllegalArgumentException,
                                                                                      IllegalAccessException {
            var annotation = field.getAnnotation(GenerateCustom.class);
            var generator = GenerateCustom.Values.createGeneratorFor(annotation);
            var value = generator.get();
            inflate(field, target, value);
        }

        private static void inflate(
            Field field,
            Object target,
            Object value
        ) throws IllegalArgumentException, IllegalAccessException {
            field.setAccessible(true);
            field.set(target, value);
        }

    }

}

