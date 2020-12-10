<p align="center" >
  <img src="https://cloud.githubusercontent.com/assets/1431421/16471739/4e28eec8-3e24-11e6-8ee1-39d36bbf679e.png" alt="Wootric" title="Wootric">
</p>

<p align="center" >
  <img src="https://cloud.githubusercontent.com/assets/1431421/16593017/083d1298-42a9-11e6-8e58-25aaaadee5d3.gif" alt="Wootric survey" title="Wootric">
</p>


[![GitHub license](https://img.shields.io/badge/license-MIT-lightgrey.svg)](https://raw.githubusercontent.com/Wootric/WootricSDK-Android/master/LICENSE.md) [![GitHub release](https://img.shields.io/github/release/Wootric/WootricSDK-Android.svg)](https://github.com/Wootric/WootricSDK-Android/releases) [![Maven Central](https://img.shields.io/maven-central/v/com.wootric/wootric-sdk-android.svg)](https://img.shields.io/maven-central/v/com.wootric/wootric-sdk-android.svg) [![Build Status](https://img.shields.io/circleci/project/Wootric/WootricSDK-Android.svg)](https://img.shields.io/circleci/project/Wootric/WootricSDK-Android.svg) [![Twitter](https://img.shields.io/badge/twitter-@WootricSDK-blue.svg?style=flat)](http://twitter.com/Wootric)

## Requirements
- Android 16+

*This library is tested to  support Android SDK version 16 onwards. Please let us know if you need assistance for lower Android SDK version by emailing support@wootric.com*

## Demo
View the Android demo [here.](http://cl.ly/0h112M290m04)

## Installation

This library is distributed as Android library project so it can be included by referencing it as a library project.

### Using Maven
If you use Maven, you can include this library as a dependency:

```xml
<dependency>
    <groupId>com.wootric</groupId>
    <artifactId>wootric-sdk-android</artifactId>
    <version>2.18.1</version>
</dependency>
```

### Using Gradle

```xml
implementation 'com.wootric:wootric-sdk-android:2.18.1'
```

## Initializing Wootric
WootricSDK task is to present a fully functional survey view with just a few lines of code.

1. Add permissions:

    Add the internet permissions to the AndroidManifest.xml file:
    
    ```xml
    <uses-permission android:name="android.permission.INTERNET" />
    ```

2. Add ProGuard rules:

    Add the following to your ProGuard rules:
    
    ```ProGuard
    -keepattributes *Annotation*, Signature
    
    ##== Wootric ==
    -keep class com.wootric.** { *; }
    
    ##== Retrofit ==
    -keep class retrofit.** { *; }
    -keepclassmembernames interface * {
        @retrofit.http.* <methods>;
    }
    ```

3. Import the SDK's header:

    First import the SDK into your Activity of choosing:
    
    ```java
    import com.wootric.androidsdk.Wootric;
    ```

4. Configure the SDK with your client ID and account token:

    All you need to do is to add this code to your Activity's `onCreate` method:
    
    ```java
    Wootric wootric = Wootric.init(this, CLIENT_ID, ACCOUNT_TOKEN);
    ```

5. To display the survey (if user is eligible - this check is built in the method) use:
    ```java
    wootric.survey();
    ```

And that's it! You're good to go and start receiving customer's feedback from your Android app.

*For a working implementation of this project see the `app/` folder.*

## Example

```java
// Import Wootric
import com.wootric.androidsdk.Wootric;

// Inside your Activity's onCreate method

Wootric wootric = Wootric.init(this, YOUR_CLIENT_ID, YOUR_ACCOUNT_TOKEN);
wootric.setEndUserEmail("nps@example.com");
// Use only for testing
wootric.setSurveyImmediately(true);
// show survey
wootric.survey();

```

## License

The WootricSDK is released under the MIT license. See LICENSE for details.

## Contribute

If you want to contribute, report a bug or request a feature, please follow CONTRIBUTING for details.
