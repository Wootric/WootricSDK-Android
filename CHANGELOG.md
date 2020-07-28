## 2.17.0 (2020-07-30)

- Add support to show Footer section in Landscape mode
- Fix Wootric Sampling Rule property
- Add log message
- Fix opt-out button to retrieve text from server translations
- Change Enter button action on feedback screen to advance to next screen

## 2.16.0 (2020-06-11)

- Add support for SamplingGroups
- Remove clientSecret initializer
- Fix keyboard on tablets
- Fix exceptions

## 2.15.0 (2020-04-28)

- Add support for event based sampling

## 2.14.2 (2020-02-28)

- Fix bug when thank_you_link_url_settings is not present
- Migrate to AndroidX

## 2.14.1 (2019-12-18)

- Fix bug while writing to parcel

## 2.14.0 (2019-11-14)

- Add support of admin panel values for 3rd screen

## 2.13.0 (2019-08-09)

- Add callbacks
- Remove tracking pixel
- Fix timeDelay bug
- Fix ThankYou dialog dismiss bug
- Add skipFeebackScreen

## 2.12.0 (2019-07-17)

- Add support for custom scale

## 2.11.0 (2019-02-04)

- Add new method showSurveyInActivity

## 2.10.0 (2018-12-18)

- Add support for Property Based Sampling

## 2.9.0 (2018-12-06)

- Add back support for Activity (fixes [#4](https://github.com/Wootric/segment-android-integration-wootric/issues/4) & [#5](https://github.com/Wootric/segment-android-integration-wootric/issues/5))
- Fix RTL issue (fixes [#48](https://github.com/Wootric/WootricSDK-Android/issues/48))

## 2.8.0 (2018-05-18)

- Add new method to show opt out link

## 2.7.0 (2018-05-08)

- Add new method that just needs client_id & doesn't require the client_secret

## 2.6.1 (2018-04-09)

- Fix Layout for short followup question

## 2.6.0 (2018-02-01)

- Update Gradle and Build Libraries
- Use Fragment from Support Library v4
- Add `SURVEY_DIALOG_TAG` to Fragment Transaction
- Fix Survey Layout hides from bottom if Survey question is long

## 2.5.2 (2017-06-14)

- Fix space in between DISMISS and SEND button
- Update text Edit score to new position
- Update RelativeLayout to ConstraintLayout

## 2.5.1 (2017-05-26)

- Fix white text over white background

## 2.5.0 (2017-04-11)

- CES and CSAT support

## 2.4.14 (2017-01-26)

- Enable 'Send' button when no feedback text is provided on second screen

## 2.4.13 (2016-10-26)

- Fix 'DONE' button text

## 2.4.12 (2016-10-12)

- Intercept click event on tablet survey

## 2.4.11 (2016-09-27)

- Fix error catching

## 2.4.10 (2016-09-08)

- Pass external_id & phone_number
- Update tests

## 2.4.9 (2016-08-31)

- Accept resurvey_throttle and decline_resurvey_throttle
- Update tests
- Add tests for PreferencesUtils
- Add log for last_seen and surveyed 'cookie' values
- Clean code

## 2.4.8 (2016-08-25)

### Fix:

- Catch NullPointerException
- Update gradle

## 2.4.7 (2016-06-30)

### Fix:

- Null check for activity

## 2.4.6 (2016-06-29)

### Added:

- Send Wootric SDK, OS version and OS name

## 2.4.5 (2016-06-22)

### Fix:

- Fix null pointer exception

## 2.4.4 (2016-06-22)

### Fix:

- User-Agent header field

## 2.4.3 (2016-06-10)

### Fix:

- Add end_user_last_seen on requests to eligibility
- Fix first_survey_delay bug
- Fix Log crash bug
- Add resources for new Wootric logo
- Add documentation & license on files
- Code cleanup

## 2.4.2 (2016-05-23)

### Add:

- Concurrent Survey Processing support
- Use https on survey.wootric.com

## 2.4.1 (2016-05-10)

### Fixed:

- Move Edit Score, Dismiss & Send buttons up on Feedback view to prevent being covered by keyboard
- Fix score view layout on tablets
- Remove buttons in last view if custom Thank You message and Social links not set (tablets)
- Fix custom thank you text to show correct text (tablet)
- Add CHANGELOG.md & LICENSE.md
- Update gitignore file to ignore *.iml files, which are IDE files
- Fix http response decoding on WootricRemoteRequestTask.java to show correct characters unicode characters
- Add log to requests and responses on WootricRemoteRequestTask.java
- Add Edit Score to LocalizedTexts
- Fix Edit Score button to show localized text
- Fix Submit text on tablets to show localized text
- Add getters for social share question and social share decline on Settings.java
- Fix final Thank You message on both phones & tablets to show correct message
- Use default hint color for feedback placeholder on wootric_survey_layout.xml
- Set buttons textSize from dimens files on wootric_survey_layout.xml
- Add dimens.xml to change font size for different screen densities
- Update settings test
