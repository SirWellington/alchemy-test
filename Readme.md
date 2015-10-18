Alchemy Test
==============================================
### "Testing your code should be as fun and simple as writing it."

[![Build Status](https://travis-ci.org/SirWellington/alchemy-test.svg)](https://travis-ci.org/SirWellington/alchemy-test)


# Purpose

Part of the Alchemy collection, **Alchemy Test** is a Unit Test Library that makes it easier to test your code by providing syntactic sugar for unit-testing and mocking.



# Requirements

+ Java 8
+ Maven 3+


# Building
This project builds with maven. Just run a `mvn clean install` to compile and install to your local maven repository.

# Download

To use, simply add the following maven dependency.

## Release
```xml
<dependency>
	<groupId>tech.sirwellington.alchemy</groupId>
	<artifactId>alchemy-test</artifactId>
	<version>1.1</version>
</dependency>
```

## Snapshot

>First add the Snapshot Repository
```xml
<repository>
	<id>ossrh</id>
    <url>https://oss.sonatype.org/content/repositories/snapshots</url>
</repository>
```

```xml
<dependency>
	<groupId>tech.sirwellington.alchemy</groupId>
	<artifactId>alchemy-test</artifactId>
	<version>1.2-SNAPSHOT</version>
</dependency>
```


==============================================
# Throwable Assertions
In conjunction with JUnit, Throwable assertions make it really easy to write _Exception Statements_.

Instead of writing all this:

>``` java
try
{
	instance.call("badArg");
	fail("Expected exception");
}
catch(IllegalArgumentException ex)
{
	//This means the test passed
}
```

You can now just do:

``` java
assertThrows(() -> instance.call("badArg"))
.isInstanceOf(IllegalArgumentException.class); //Optional further assertion
```

# Mockito Answers

## Returning arguments back
```java
// Return the first argument back when the mock is called
when(someMock)
	.call(anyString(), anyString())
	then(returnFirst());

//verify it works
String result = mock.call("arg1", "arg2");
assertThat(result, is("arg1"));
```

# More coming soon...

# Release Notes

## 1.1
+ Initial Public Release

## 1.0.3
+ Bug-fixes to number generation

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

http://www.apache.org/licenses/LICENSE-2.0
