## Testing the ContentProvider with adb

The app publishes quiz entries through a ContentProvider.

Authority:
`com.example.quizapp.provider`

Path:
`entries`

Full URI:
`content://com.example.quizapp.provider/entries`

To test the ContentProvider, I used this adb command:

```bash
adb shell content query --uri content://com.example.quizapp.provider/entries
```
### Result: 
Row: 0 name=Giga Chad, URI=android.resource://com.example.quizapp/2131165319

Row: 1 name=Slay, URI=android.resource://com.example.quizapp/2131165417

Row: 2 name=Hest, URI=content://com.android.providers.downloads.documents/document/msf%3A1000000035

# UI Tests
### Test framework
The tests use:
- Espresso for the XML-based main menu
- Jetpack Compose UI testing for the Compose screens
- Espresso-Intents for stubbing the image picker intent in Gallery tests

### Test cases

#### 1. Main menu navigation test
This test checks that pressing a button in the main menu opens the correct activity.

Example:
- pressing the **Quiz** button opens the **Quiz** activity

#### 2. Quiz score test
This test checks that the score is updated correctly after:
- one wrong answer
- one correct answer

The test verifies that the score text changes as expected.

#### 3. Gallery add/delete test
This test checks that the number of registered pictures/persons is correct after:
- adding an entry
- deleting an entry

For the add test, Intent Stubbing is used to return image data without requiring manual user interaction.

### How to run the tests

Run instrumented tests from Android Studio:

- right click the test class in `app/src/androidTest/java`
- choose **Run**