# Wootric SDK for Android
Demo
=========================
View the Android demo [here.](https://cl.ly/dVMH)

Installation
=========================
This library is distributed as Android library project so it can be included by referencing it as a library project.

If you use Maven, you can include this library as a dependency:

```xml
<dependency>
    <groupId>com.wootric</groupId>
    <artifactId>wootric-sdk-android</artifactId>
    <version>2.3.2</version>
</dependency>
```

For Gradle users:

```xml
compile 'com.wootric:wootric-sdk-android:2.3.2'
```
Note: this library is tested to  support Android SDK version 16 onwards. Please let us know if you need assistance for lower Android SDK version by emailing support@wootric.com

Permissions
===========

Add the internet permissions to the AndroidManifest.xml file:

```xml
<uses-permission android:name="android.permission.INTERNET" />
```

ProGuard
========
Add the following to your ProGuard rules:

````ProGuard
-keepattributes *Annotation*, Signature

##== Wootric ==
-keep class com.wootric.** { *; }

##== Retrofit ==
-keep class retrofit.** { *; }
-keepclassmembernames interface * {
    @retrofit.http.* <methods>;
}
````

Usage
=====

*For a working implementation of this project see the `app/` folder.*

First import the SDK into your Activity of choosing:

```java
import com.wootric.androidsdk.Wootric;
```

All you need to do is to add this code to your Activity's `onCreate` method:

```java
Wootric wootric = Wootric.init(this, CLIENT_ID, CLIENT_SECRET, ACCOUNT_TOKEN);
wootric.survey();
```

Additional parameters
====
### End user email ###

End user email is optional. If not provided, the end user will be considered as "Unknown".

```java
wootric.setEndUserEmail(END_USER_EMAIL);
```

### End user properties ###
End user properties can be provided as a `HashMap<String, String>` object.

```java
HashMap<String, String> properties = new HashMap<String, String>();
properties.put("company", "Wootric");
properties.put("type", "awesome");
wootric.setProperties(properties);
```

### Custom messages ###
Wootric provides designated class for providing custom messages -`WootricCustomMessages`

```java
WootricCustomMessage customMessage = new WootricCustomMessage();
customMessage.setFollowupQuestion("custom followup");
customMessage.setDetractorFollowupQuestion("custom detractor");
customMessage.setPassiveFollowupQuestion("custom passive");
customMessage.setPromoterFollowupQuestion("custom promoter");
customMessage.setPlaceholderText("custom placeholder");
customMessage.setDetractorPlaceholderText("custom detractor placeholder");
customMessage.setPassivePlaceholderText("custom passive placeholder");
customMessage.setPromoterPlaceholderText("custom promoter placeholder");

wootric.setCustomMessage(customMessage);
```

### Custom thank you ###
Wootric provides designated class for providing custom thank you -`WootricCustomThankYou`

```java
WootricCustomThankYou customThankYou = new WootricCustomThankYou();
customThankYou.setText("Thank you!!");
customThankYou.setDetractorText("Detractor thank you");
customThankYou.setPassiveText("Passive thank you");
customThankYou.setPromoterText("Promoter thank you");
customThankYou.setLinkText("Thank you link text");
customThankYou.setDetractorLinkText("Detractor link text");
customThankYou.setPassiveLinkText("Passive link text");
customThankYou.setPromoterLinkText("Promoter link text");
customThankYou.setLinkUri(Uri.parse("http://wootric.com/thank_you"));
customThankYou.setDetractorLinkUri(Uri.parse("http://wootric.com/detractor_thank_you"));
customThankYou.setPassiveLinkUri(Uri.parse("http://wootric.com/passive_thank_you"));
customThankYou.setPromoterLinkUri(Uri.parse("http://wootric.com/promoter_thank_you"));
customThankYou.setScoreInUrl(true);
customThankYou.setCommentInUrl(true);
wootric.setCustomThankYou(customThankYou);
```

### Skip followup screen for promoters
The followup screen can be skipped for promoters, so they are taken straight to thank you screen.

```java
wootric.shouldSkipFollowupScreenForPromoters(true);
```

### Other parameters ###
There are many other parameters which can be set in Wootric:

```java
wootric.setLanguageCode("EN");
wootric.setSurveyImmediately(true);

wootric.setDailyResponseCap(10);
wootric.setRegisteredPercent(10);
wootric.setVisitorPercent(10);
wootric.setResurveyThrottle(10);

wootric.setProductName("Wootric");
wootric.setRecommendTarget("Best Friend");
wootric.setFacebookPageId("123456");
wootric.setTwitterPage("wootric");
```
