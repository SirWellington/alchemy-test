/*
 * Copyright 2015 SirWellington Tech.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package tech.sirwellington.alchemy.test.junit.runners;

import java.lang.reflect.Field;
import java.util.List;
import org.junit.runners.model.FrameworkField;
import org.junit.runners.model.TestClass;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import tech.sirwellington.alchemy.annotations.access.Internal;
import tech.sirwellington.alchemy.annotations.access.NonInstantiable;
import tech.sirwellington.alchemy.generator.AlchemyGenerator;

import static tech.sirwellington.alchemy.test.Checks.Internal.checkNotNull;

/**
 *
 * @author SirWellington
 */
@Internal
@NonInstantiable
class AlchemyGeneratorAnnotations
{

    private final static Logger LOG = LoggerFactory.getLogger(AlchemyGeneratorAnnotations.class);

    static void populateGeneratedFields(TestClass testClass, Object target) throws IllegalArgumentException, IllegalAccessException
    {
        List<FrameworkField> stringGeneratedFields = testClass.getAnnotatedFields(GenerateString.class);

        for (FrameworkField field : stringGeneratedFields)
        {
            inflateString(field, target);
        }
    }

    static void inflateString(FrameworkField field, Object target) throws IllegalArgumentException, IllegalAccessException
    {
        GenerateString annotation = field.getAnnotation(GenerateString.class);
        checkNotNull(annotation, "missing annotation");

        AlchemyGenerator<String> generator = GenerateString.Values.createGeneratorFor(annotation);

        Field javaField = field.getField();
        boolean originalAccessibility = javaField.isAccessible();

        try
        {
            javaField.setAccessible(true);
            javaField.set(target, generator.get());
        }
        finally
        {
            javaField.setAccessible(originalAccessibility);
        }
    }

}
