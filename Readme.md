Commons Library for Unit Testing
==============================================

# Purpose
The purpose of this library is to make it easier to test your code by providing unit-test helpers, fake data generators, and mock helpers.

# Requirements

* JDK 8
* Maven installation

# License

This Software is licensed under the Apache 2.0 License


# Building
This project builds with maven. Just run a `mvn clean install` to compile and install to your local maven repository


# Download

To use, simply add the following maven depedencncy from artifactory.

## Release
```xml
<dependency>
	<groupId>sir.wellington.commons</groupId>
	<artifactId>commons-test</artifactId>
	<version>1.0.0</version>
</dependency>
```

## Snapshot


# Examples

## Number Generation

>Examples assume static imports`

```java
//Using static imports
int someNumber = integers(-50, 50).get();

long somePositiveNumber = positiveLongs().get();

somePositiveNumber = oneOf(positiveLongs());

DataGenerator<Double> doubleGenerator = doubles(0.1, 1999.0);
for(int i = 0; i < 100; ++i)
{
	LOG.info("Received double {}", doubleGenerator.get());
}

List<Integer> thirtyNumbers = listOf(positiveIntegers(), 30);

```
## Strings
```java
String anyCharacterString = strings(30).get();
assertThat(anyCharacterString.lenght(), is(30));

String hex = hexadecimalString(32).get();
String alphabetical = alphabeticString(5).get();

List<String> uuids = Lists.newArrayList();
for(int i = 0; i < 40; ++i)
{
	uuids.add(DataGenerator.uuids.get());
}

//Shorter way
uuids = listOf(uuids, 20);
List<String> strings = listOf(alphabeticString(20), 100);

String stringFromList = stringsFromFixedList("one", "something else", "Java").get();
```

## Enums

Let's say you have an enum called Fruits defined as:
```java
enum Fruit
{
	APPLE,
	ORANGE,
	BANANA,
	GUAVA
}
```
and you want to get one of the values at random. All you have to do is

```java
Fruit fruit = enumValueOf(Fruit.class).get();
```


# JUnit Helpers
## Throwable Assertions
Throwable assertions make it easy to assert that certain parts of code throw an exception and of a particualr type.

Instead of 

``` java
try
{
	instance.call("badArg");
	fail("Expected exception");
}
catch(Exception ex)
{
	//Pass
}
```
You can now just do: 

``` java

assertThrows(() -> instance.call("badArg"))
.isInstanceOf(RuntimeException.class); //Optional further assertion
```

This is possible thanks to the new Java 8 lambdas. It makes it much easier to make excpetion assertions on your code.

# Mockito Helpers
## Answers

```java
when(someMock).call(anyString(), anyString(), anyString()).then(Answers.returnFirst());
String result = mock.call("arg1", "arg2", "arg3");
assertThat(result, is("arg1"));
```

# Release Notes

## 1.0.0
* New Throwable Assertions
* Suite of Mockito Answers
* Simple Data Generation API