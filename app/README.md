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
