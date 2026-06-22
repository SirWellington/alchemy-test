/*
 * Copyright © 2026. Sir Wellington.
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

import org.junit.runners.model.FrameworkField;
import org.junit.runners.model.TestClass;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import tech.sirwellington.alchemy.annotations.access.Internal;
import tech.sirwellington.alchemy.annotations.access.NonInstantiable;
import tech.sirwellington.alchemy.generator.DateGenerators;

import java.util.List;

import static tech.sirwellington.alchemy.test.internal.Checks.checkNotNull;
import static tech.sirwellington.alchemy.test.internal.Checks.checkThat;

/**
 * @author SirWellington
 */
@Internal
@NonInstantiable
//TODO: This needs a better name
final class TestClassInjectors {

    private final static Logger LOG = LoggerFactory.getLogger(TestClassInjectors.class);

    static void populateGeneratedFields(TestClass testClass, Object target) throws IllegalArgumentException,
                                                                                   IllegalAccessException {
        //@GenerateString
        var stringGeneratedFields = testClass.getAnnotatedFields(GenerateString.class);
        for (FrameworkField field : stringGeneratedFields) {
            inflateString(field, target);
        }

        //@GenerateBoolean
        var booleanGeneratedFields = testClass.getAnnotatedFields(GenerateBoolean.class);
        for (FrameworkField field : booleanGeneratedFields) {
            inflateBoolean(field, target);
        }

        //@GenerateInteger
        var integerGeneratedFields = testClass.getAnnotatedFields(GenerateInteger.class);
        for (FrameworkField field : integerGeneratedFields) {
            inflateInteger(field, target);
        }

        //@GenerateLong
        var longGeneratedFields = testClass.getAnnotatedFields(GenerateLong.class);
        for (FrameworkField field : longGeneratedFields) {
            inflateLong(field, target);
        }

        //@GenerateFloat
        var floatGeneratedFields = testClass.getAnnotatedFields(GenerateFloat.class);
        for (FrameworkField field : floatGeneratedFields) {
            inflateFloat(field, target);
        }

        //@GenerateDouble
        var doubleGeneratedFields = testClass.getAnnotatedFields(GenerateDouble.class);
        for (FrameworkField field : doubleGeneratedFields) {
            inflateDouble(field, target);
        }

        //@GenerateDate
        var dateGeneratedFields = testClass.getAnnotatedFields(GenerateDate.class);
        for (FrameworkField field : dateGeneratedFields) {
            inflateDate(field, target);
        }

        //@GenerateURL
        var urlGeneratedFields = testClass.getAnnotatedFields(GenerateURL.class);
        for (FrameworkField field : urlGeneratedFields) {
            inflateUrl(field, target);
        }

        //@GeneratePojo
        var pojoGeneratedFields = testClass.getAnnotatedFields(GeneratePojo.class);
        for (FrameworkField field : pojoGeneratedFields) {
            inflatePojo(field, target);
        }

        //@GenerateEnum
        var enumGeneratedFields = testClass.getAnnotatedFields(GenerateEnum.class);
        for (FrameworkField field : enumGeneratedFields) {
            inflateEnum(field, target);
        }

        //@GenerateList
        var listGeneratedFields = testClass.getAnnotatedFields(GenerateList.class);
        for (FrameworkField field : listGeneratedFields) {
            inflateList(field, target);
        }

        //@GenerateCustom
        var customGeneratedFields = testClass.getAnnotatedFields(GenerateCustom.class);
        for (FrameworkField field : customGeneratedFields) {
            inflateCustom(field, target);
        }
    }

    private static void inflateString(FrameworkField field, Object target) throws IllegalArgumentException,
                                                                                  IllegalAccessException {
        var annotation = field.getAnnotation(GenerateString.class);
        checkNotNull(annotation, "missing annotation");

        var generator = GenerateString.Values.createGeneratorFor(annotation);
        var value = generator.get();
        inflate(field, target, value);
    }

    private static void inflateBoolean(FrameworkField field, Object target) throws IllegalArgumentException,
                                                                                   IllegalAccessException {
        var annotation = field.getAnnotation(GenerateBoolean.class);
        checkNotNull(annotation, "missing annotation: @GenerateBoolean");

        var generator = GenerateBoolean.Values.createGeneratorFor(annotation);
        var value = generator.get();
        inflate(field, target, value);
    }

    private static void inflateInteger(FrameworkField field, Object target) throws IllegalArgumentException,
                                                                                   IllegalAccessException {
        var annotation = field.getAnnotation(GenerateInteger.class);
        checkNotNull(annotation, "missing annotation");

        var generator = GenerateInteger.Values.createGeneratorFor(annotation);
        var value = generator.get();
        inflate(field, target, value);

    }

    private static void inflateLong(FrameworkField field, Object target) throws IllegalAccessException {
        var annotation = field.getAnnotation(GenerateLong.class);
        checkNotNull(annotation, "missing annotation");

        var generator = GenerateLong.Values.createGeneratorFor(annotation);
        var value = generator.get();
        inflate(field, target, value);
    }

    private static void inflateFloat(FrameworkField field, Object target) throws IllegalAccessException {
        var annotation = field.getAnnotation(GenerateFloat.class);
        checkNotNull(annotation, "missing annotation");

        var generator = GenerateFloat.Values.createGeneratorFor(annotation);
        var value = generator.get();
        inflate(field, target, value);
    }

    private static void inflateDouble(FrameworkField field, Object target) throws IllegalAccessException {
        var annotation = field.getAnnotation(GenerateDouble.class);
        checkNotNull(annotation, "missing annotation");

        var  generator = GenerateDouble.Values.createGeneratorFor(annotation);
        var value = generator.get();
        inflate(field, target, value);
    }

    private static void inflateDate(FrameworkField field, Object target) throws IllegalArgumentException,
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

    private static void inflateUrl(FrameworkField field, Object target) throws IllegalAccessException {
        var annotation = field.getAnnotation(GenerateURL.class);
        checkNotNull(annotation, "missing annotation");

        var generator = GenerateURL.Values.createGeneratorFor(annotation);
        var value = generator.get();
        inflate(field, target, value);
    }

    private static void inflatePojo(FrameworkField field, Object target) throws IllegalArgumentException,
                                                                                IllegalAccessException {
        var typeOfPojo = field.getType();
        var annotation = field.getAnnotation(GeneratePojo.class);
        var generator = GeneratePojo.Values.createGeneratorFor(annotation, typeOfPojo);
        var value = generator.get();
        inflate(field, target, value);
    }

    private static void inflateEnum(FrameworkField field, Object target) throws IllegalArgumentException,
                                                                                IllegalAccessException {
        var typeOfField = field.getType();
        checkThat(typeOfField.isEnum(), "@GenerateEnum can only be used on Enum Types");
        var typeOfEnum = (Class<? extends Enum>) typeOfField;

        var annotation = field.getAnnotation(GenerateEnum.class);

        var generator = GenerateEnum.Values.createGeneratorFor(annotation, typeOfEnum);
        var value = generator.get();
        inflate(field, target, value);
    }

    private static void inflateList(FrameworkField field, Object target) throws IllegalArgumentException,
                                                                                IllegalAccessException {
        var typeOfField = field.getType();
        checkThat(List.class.isAssignableFrom(typeOfField), "@GenerateList can only be used on List Types");

        var annotation = field.getAnnotation(GenerateList.class);
        var generator = GenerateList.Values.createGeneratorFor(annotation);
        var value = generator.get();
        inflate(field, target, value);
    }

    private static void inflateCustom(FrameworkField field, Object target) throws IllegalArgumentException,
                                                                                  IllegalAccessException {
        var annotation = field.getAnnotation(GenerateCustom.class);
        var generator = GenerateCustom.Values.createGeneratorFor(annotation);
        var value = generator.get();
        inflate(field, target, value);
    }

    private static void inflate(FrameworkField field, Object target, Object value) throws IllegalArgumentException,
                                                                                          IllegalAccessException {

        var javaField = field.getField();
        javaField.setAccessible(true);
        javaField.set(target, value);
    }

}
