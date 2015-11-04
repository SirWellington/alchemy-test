Alchemy Test
==============================================

<img src="https://raw.githubusercontent.com/SirWellington/alchemy/develop/Graphics/Logo/Alchemy-Logo-v3-name.png" width="200">

## "Testing your code should be as fun and simple as writing it."

[![Build Status](https://travis-ci.org/SirWellington/alchemy-test.svg)](https://travis-ci.org/SirWellington/alchemy-test)


# Purpose

We write so many tests in our day; it should be easier.
Part of the [Alchemy Collection](https://github.com/SirWellington/alchemy), **Alchemy Test** is a Unit Test Library that makes it easier to test your code by providing syntactic sugar for unit-testing and mocking.


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

# [Javadocs](http://www.javadoc.io/doc/tech.sirwellington.alchemy/alchemy-test/)


API
==============================================
# Throwable Assertions
In conjunction with JUnit, Throwable assertions make it really easy to write _Exception Statements_.

Instead of writing all this crap:

``` java
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

# Alchemy Test Runner

#### Let's take some of the drudgery out of Unit Testing


## Free Print Statements

So often in tests we do this:
```java
@Test
public void testSomeMethod()
{
	//Print out the test name at the beginning
	System.out.println("testSomeMethod");

	//Rest of the Test Case
}
```

Who wants to write the same thing twice? It's especially troublesome when you decide to rename your test name, since
you have to remember to update the print statement as well.

`AlchemyTestRunner` makes this automatic.

```java
@RunWith(AlchemyTestRunner.class)
public class HttpTests
{
	@Test
	public void testReponse()
	{
		//Automatic
		//System.out.println("testResponse()");
		//FOR FREE
	}
}
```

We even tell you information about the test run:
```java
testRequestReadyEdgeCases()
  Duration: 59ms
  Runs: 100

testValidContentType()
  Duration: 7ms
  Runs: 100
```

## Repeat Tests
Used in conjunction with [Alchemy Generator](https://github.com/SirWellington/alchemy-generator),
repeat tests can make for powerful test quality.

```java
@Repeat(1000)
@RunWith(AlchemyTestRunner.class)
public class StringEncryptorTests
{
	private String text;
	private StringEncryptor instance;

	@Before
	public void setup()
	{
		text = one(hexadecimalStrings(10));
	}

	@Test
	public void testDecrypt()
	{
		String result = instance.decrypt(text);
		//Blah checks
	}
}
```

You can also set the number of repeats on a case by case basis

```java
@Repeat(100)
@RunWith(AlchemyTestRunner.class)
public class StringEncryptorTests
{
	private String text;
	private StringEncryptor instance;

	@Before
	public void setup()
	{
		text = one(strings(10));
	}

	//Repeat less often
	@Repeat(10)
	@Test
	public void testEncrypt()
	{
		String result = instance.encrypt(text);
		//Blah checks
	}
}
```
You can also use the `@DontRepeat` annotation to prevent a testCase from being repeated entirely.

## Mockito Initialization

By default, we also init your Mockito Mocks for free

```java
@RunWith(AlchemyTestRunner.class)
public class MyTest
{
	@Mock
	private AlchemyHttp http;

	@Mock
	private HttpResponse response;

	@Before
	private void setup()
	{
		//Mocks are already instantiated

		when(http.get())
		.thenReturn(response);
	}
}
```

If you want to disable this behavior, simply add:
```java
@InitMocks(false)
```
to your test class.



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


# Requirements
+ Java 8
+ Maven 3+


# Building
This project builds with maven. Just run a `mvn clean install` to compile and install to your local maven repository.

# Feature Requests
Feature Requests are definitely welcomed! **Please drop a note in [Issues](https://github.com/SirWellington/alchemy-test/issues).**

# Release Notes

## 1.2
+ Alchemy Test Runner

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
