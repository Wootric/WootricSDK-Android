# Wootric SDK for Android

Installation
=========================
This library is distributed as Android library project so it can be included by referencing it as a library project.

If you use Maven, you can include this library as a dependency:

```xml
<dependency>
    <groupId>com.wootric</groupId>
    <artifactId>wootric-sdk-android</artifactId>
    <version>1</version>
</dependency>
```
	
For Gradle users:

```xml
compile 'com.wootric:wootric-sdk-android:1'
```

Usage
=====

*For a working implementation of this project see the `app/` folder.*

All you need to do is to add this code to you Activity's `onCreate` method:

```java
Wootric.with(this)
  .user(CLIENT_ID, CLIENT_SECRET, ACCOUNT_TOKEN)
  .endUser(END_USER_EMAIL, ORIGIN_URL)
  .survey();
```

You should also stop Wootric in your Activity's `onStop` method:

```java
Wootric.stop();
```

Additional parameters
====

### End user properties ###
End user properties can be provided to `endUser()` method as a `HashMap<String, String>` object.

```java
HashMap endUserProperties = new HashMap<String, String>();
properties.put("Company", "Wootric");
properties.put("Platform", "Android");

Wootric.with(this)
    .user(CLIENT_ID, CLIENT_SECRET, ACCOUNT_TOKEN)
    .endUser(END_USER_EMAIL, ORIGIN_URL, endUserProperties)
    .survey();      
```

### Custom messages ###
Wootric provides designated class for providing custom messages -`WootricCustomMessages`

```java
WootricCustomMessage myCustomMessage = WootricCustomMessage.create()
                .recommendTo("Recommend to")
                .placeholder("Default placeholder")
                .detractorPlaceholder("Detractors placeholder")
                .passivePlaceholder("Passives placeholder")
                .promoterPlaceholder("Promoters placeholder")
                .followupQuestion("Followup question")
                .detractorFollowupQuestion("Detractors question")
                .passiveFollowupQuestion("Passives question")
                .promoterFollowupQuestion("Promoters question");
```

Then simply:
```java
Wootric.with(this)
    .user(CLIENT_ID, CLIENT_SECRET, ACCOUNT_TOKEN)
    .endUser(END_USER_EMAIL, ORIGIN_URL, endUserProperties)
    .customMessage(myCustomMessage)
    .survey();               
```

### Other parameters ###
There are many other parameters which can be set in Wootric:

```java
Wootric.with(this)
  .user(CLIENT_ID, CLIENT_SECRET, ACCOUNT_TOKEN)
  .endUser(END_USER_EMAIL, ORIGIN_URL)
  .productName(<PRODUCT NAME>)
  .createdAt(<END USER CREATED AT>)
  .dailyResponseCap(<DAILY RESPONSE CAP>)
  .registeredPercent(<REGISTERED PERCENT>)
  .resurveyThrottle(<RESURVEY THROTTLE>)
  .visitorPercent(<VISITOR PERCENT>)
  .surveyImmediately()
  .survey();
```
