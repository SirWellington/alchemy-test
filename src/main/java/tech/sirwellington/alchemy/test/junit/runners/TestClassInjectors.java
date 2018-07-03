/*
 * Copyright © 2018. Sir Wellington.
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

import java.lang.reflect.Field;
import java.net.URL;
import java.util.Date;
import java.util.List;

import org.junit.runners.model.FrameworkField;
import org.junit.runners.model.TestClass;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import tech.sirwellington.alchemy.annotations.access.Internal;
import tech.sirwellington.alchemy.annotations.access.NonInstantiable;
import tech.sirwellington.alchemy.generator.AlchemyGenerator;

import static tech.sirwellington.alchemy.test.Checks.Internal.checkNotNull;
import static tech.sirwellington.alchemy.test.Checks.Internal.checkThat;

/**
 * @author SirWellington
 */
@Internal
@NonInstantiable
//TODO: This needs a better name
final class TestClassInjectors
{

    private final static Logger LOG = LoggerFactory.getLogger(TestClassInjectors.class);

    static void populateGeneratedFields(TestClass testClass, Object target) throws IllegalArgumentException,
                                                                                   IllegalAccessException
    {
        //@GenerateString
        List<FrameworkField> stringGeneratedFields = testClass.getAnnotatedFields(GenerateString.class);

        for (FrameworkField field : stringGeneratedFields)
        {
            inflateString(field, target);
        }

        //@GenerateBoolean
        List<FrameworkField> booleanGeneratedFields = testClass.getAnnotatedFields(GenerateBoolean.class);
        for (FrameworkField field : booleanGeneratedFields)
        {
            inflateBoolean(field, target);
        }

        //@GenerateInteger
        List<FrameworkField> integerGeneratedFields = testClass.getAnnotatedFields(GenerateInteger.class);
        for (FrameworkField field : integerGeneratedFields)
        {
            inflateInteger(field, target);
        }

        //@GenerateLong
        List<FrameworkField> longGeneratedFields = testClass.getAnnotatedFields(GenerateLong.class);
        for (FrameworkField field : longGeneratedFields)
        {
            inflateLong(field, target);
        }

        //@GenerateDouble
        List<FrameworkField> doubleGeneratedFields = testClass.getAnnotatedFields(GenerateDouble.class);
        for (FrameworkField field : doubleGeneratedFields)
        {
            inflateDouble(field, target);
        }

        //@GenerateDate
        List<FrameworkField> dateGeneratedFields = testClass.getAnnotatedFields(GenerateDate.class);
        for (FrameworkField field : dateGeneratedFields)
        {
            inflateDate(field, target);
        }

        //@GenerateURL
        List<FrameworkField> urlGeneratedFields = testClass.getAnnotatedFields(GenerateURL.class);
        for (FrameworkField field : urlGeneratedFields)
        {
            inflateUrl(field, target);
        }

        //@GeneratePojo
        List<FrameworkField> pojoGeneratedFields = testClass.getAnnotatedFields(GeneratePojo.class);
        for (FrameworkField field : pojoGeneratedFields)
        {
            inflatePojo(field, target);
        }

        //@GenerateEnum
        List<FrameworkField> enumGeneratedFields = testClass.getAnnotatedFields(GenerateEnum.class);
        for (FrameworkField field : enumGeneratedFields)
        {
            inflateEnum(field, target);
        }

        //@GenerateList
        List<FrameworkField> listGeneratedFields = testClass.getAnnotatedFields(GenerateList.class);
        for (FrameworkField field : listGeneratedFields)
        {
            inflateList(field, target);
        }

        //@GenerateCustom
        List<FrameworkField> customGeneratedFields = testClass.getAnnotatedFields(GenerateCustom.class);
        for (FrameworkField field : customGeneratedFields)
        {
            inflateCustom(field, target);
        }
    }

    private static void inflateString(FrameworkField field, Object target) throws IllegalArgumentException, IllegalAccessException
    {
        GenerateString annotation = field.getAnnotation(GenerateString.class);
        checkNotNull(annotation, "missing annotation");

        AlchemyGenerator<String> generator = GenerateString.Values.createGeneratorFor(annotation);
        String value = generator.get();
        inflate(field, target, value);
    }

    private static void inflateBoolean(FrameworkField field, Object target) throws IllegalArgumentException,
                                                                                   IllegalAccessException
    {
        GenerateBoolean annotation = field.getAnnotation(GenerateBoolean.class);
        checkNotNull(annotation, "missing annotation: @GenerateBoolean");

        AlchemyGenerator<Boolean> generator = GenerateBoolean.Values.createGeneratorFor(annotation);
        Boolean value = generator.get();
        inflate(field, target, value);
    }

    private static void inflateInteger(FrameworkField field, Object target) throws IllegalArgumentException,
                                                                                   IllegalAccessException
    {
        GenerateInteger annotation = field.getAnnotation(GenerateInteger.class);
        checkNotNull(annotation, "missing annotation");

        AlchemyGenerator<Integer> generator = GenerateInteger.Values.createGeneratorFor(annotation);
        Integer value = generator.get();
        inflate(field, target, value);

    }

    private static void inflateLong(FrameworkField field, Object target) throws IllegalAccessException
    {
        GenerateLong annotation = field.getAnnotation(GenerateLong.class);
        checkNotNull(annotation, "missing annotation");

        AlchemyGenerator<Long> generator = GenerateLong.Values.createGeneratorFor(annotation);
        Long value = generator.get();
        inflate(field, target, value);
    }

    private static void inflateDouble(FrameworkField field, Object target) throws IllegalAccessException
    {
        GenerateDouble annotation = field.getAnnotation(GenerateDouble.class);
        checkNotNull(annotation, "missing annotation");

        AlchemyGenerator<Double> generator = GenerateDouble.Values.createGeneratorFor(annotation);
        Double value = generator.get();
        inflate(field, target, value);
    }

    private static void inflateDate(FrameworkField field, Object target) throws IllegalArgumentException, IllegalAccessException
    {
        GenerateDate annotation = field.getAnnotation(GenerateDate.class);
        checkNotNull(annotation, "missing annotation");

        AlchemyGenerator<Date> generator = GenerateDate.Values.createGeneratorFor(annotation);
        Date value = generator.get();
        inflate(field, target, value);
    }

    private static void inflateUrl(FrameworkField field, Object target) throws IllegalAccessException
    {
        GenerateURL annotation = field.getAnnotation(GenerateURL.class);
        checkNotNull(annotation, "missing annotation");

        AlchemyGenerator<URL> generator = GenerateURL.Values.createGeneratorFor(annotation);
        URL value = generator.get();
        inflate(field, target, value);
    }

    private static void inflatePojo(FrameworkField field, Object target) throws IllegalArgumentException, IllegalAccessException
    {
        Class<?> typeOfPojo = field.getType();
        GeneratePojo annotation = field.getAnnotation(GeneratePojo.class);

        AlchemyGenerator<?> generator = GeneratePojo.Values.createGeneratorFor(annotation, typeOfPojo);
        Object value = generator.get();
        inflate(field, target, value);
    }

    private static void inflateEnum(FrameworkField field, Object target) throws IllegalArgumentException, IllegalAccessException
    {
        Class<?> typeOfField = field.getType();
        checkThat(typeOfField.isEnum(), "@GenerateEnum can only be used on Enum Types");
        Class<? extends Enum> typeOfEnum = (Class<? extends Enum>) typeOfField;

        GenerateEnum annotation = field.getAnnotation(GenerateEnum.class);

        AlchemyGenerator<?> generator = GenerateEnum.Values.createGeneratorFor(annotation, typeOfEnum);
        Object value = generator.get();
        inflate(field, target, value);
    }

    private static void inflateList(FrameworkField field, Object target) throws IllegalArgumentException, IllegalAccessException
    {
        Class<?> typeOfField = field.getType();
        checkThat(List.class.isAssignableFrom(typeOfField), "@GenerateList can only be used on List Types");

        GenerateList annotation = field.getAnnotation(GenerateList.class);

        AlchemyGenerator<?> generator = GenerateList.Values.createGeneratorFor(annotation);
        Object value = generator.get();
        inflate(field, target, value);
    }

    private static void inflateCustom(FrameworkField field, Object target) throws IllegalArgumentException, IllegalAccessException
    {
        GenerateCustom annotation = field.getAnnotation(GenerateCustom.class);

        AlchemyGenerator<?> generator = GenerateCustom.Values.createGeneratorFor(annotation);
        Object value = generator.get();
        inflate(field, target, value);
    }

    private static void inflate(FrameworkField field, Object target, Object value) throws IllegalArgumentException,
                                                                                          IllegalAccessException
    {

        Field javaField = field.getField();
        boolean originalAccessibility = javaField.isAccessible();

        try
        {
            javaField.setAccessible(true);
            javaField.set(target, value);
        }
        finally
        {
            javaField.setAccessible(originalAccessibility);
        }
    }


}
