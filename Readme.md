Alchemy Test for Unit Testing
==============================================

[![Build Status](https://travis-ci.org/SirWellington/alchemy-test.svg)](https://travis-ci.org/SirWellington/alchemy-test)

# Purpose
Part of the Alchemy collection, this library makes it easier to test your code by providing unit-test helpers, fake data generators, and mock helpers.

# Requirements

* JDK 8
* Maven installation



# Building
This project builds with maven. Just run a `mvn clean install` to compile and install to your local maven repository


# Download

> This library is not yet available on Maven Central

To use, simply add the following maven dependency.

## Release
```xml
<dependency>
	<groupId>tech.sirwellington.alchemy</groupId>
	<artifactId>alchemy-test</artifactId>
	<version>1.0.3</version>
</dependency>
```


## JitPack 

You can also use [JitPack.io](https://jitpack.io/#SirWellington/alchemy-test/v1.0.0).

```xml
<repository>
    <id>jitpack.io</id>
    <url>https://jitpack.io</url>
</repository>
```

```xml
<dependency>
    <groupId>com.github.SirWellington</groupId>
    <artifactId>alchemy-test</artifactId>
    <version>v1.0.3</version>
</dependency>
```

## Snapshot

==============================================

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


# Data Generation

Using randomly generated data sets helps improve test quality by assuring that your code can work over a wide range of data, 
and not just what you hard-code in. This library makes it painless to generate primitive types, 
and you can even supply your own Data Generators for use in conjunction with this library.


>Examples assume static imports`

## Numbers

```java
//A number in the range [-50, 50)
int someNumber = integers(-50, 50).get();

//Get any positive long
long somePositiveNumber = positiveLongs().get();

//alternative way to get a single value
somePositiveNumber = oneOf(positiveLongs());

//A double in the range [0.1, 1999.0]
DataGenerator<Double> doubleGenerator = doubles(0.1, 1999.0);
for(int i = 0; i < 100; ++i)
{
	LOG.info("Received double {}", doubleGenerator.get());
}

//A list of 30 randomly selected positive integers
List<Integer> thirtyNumbers = listOf(positiveIntegers(), 30);

```
## Strings
```java
//May have unicode characters as well
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

//The generated strings can only be one of the supplied ones.
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

# Mockito Helpers
## Answers

```java
// Return the first argument back when the mock is called
when(someMock).call(anyString(), anyString(), anyString())
	.then(returnFirst());

//verify it works
String result = mock.call("arg1", "arg2", "arg3");
assertThat(result, is("arg1"));
```

# Release Notes

## 1.1

## 1.0.3
+ Bugfixes to number generation

## 1.0.2
+ Expanding `positiveIntegers()` and `positiveLongs()` to include larger ranges from `1...Integer/Long.MAX_VALUE`
+ Adding `smallPositiveIntegers()` to allow explicit creation of "small" positive integer values. This is useful in conjunction with creating arrays, or other types of functions that require a workable size.

## 1.0.1
+ Adding convenience method for alphabeticStrings


## 1.0.0
+ New Throwable Assertions
+ Suite of Mockito Answers
+ Simple Data Generation API

# License

This Software is licensed under the Apache 2.0 License
