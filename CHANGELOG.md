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
