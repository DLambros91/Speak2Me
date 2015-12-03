/************************************************************************************
 *      Project:  Speak2Me: Speech to Speech Translation
 *       Author:  David Lambropoulos, Demetrios Lambropoulos, Ryan Sanichar, Xuan Li
 *
 *
 *       Course:  14:332:453
 *   Instructor:  Janne Lindqvist
 *           TA:  Can Liu
 *     Due Date:  Thursday December 3rd at 11:59PM
 *
 *  Description:    Android application that does the following:
 *                  Speech translation between different languages
 *                  Takes spoken phrase, converts to text, then translate the text and speaks it back
 *
 *
 *    File Name:  MainActivity.java
 *  Description:  The main .java file.
 *
 *************************************************************************************/

package com.example.dlambros.speak2me;

import android.content.ClipData;
import android.content.ClipboardManager;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.os.AsyncTask;
import android.os.Bundle;
import android.speech.RecognitionListener;
import android.speech.RecognizerIntent;
import android.speech.SpeechRecognizer;
import android.speech.tts.TextToSpeech;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.telephony.SmsManager;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.memetix.mst.language.Language;
import com.memetix.mst.translate.Translate;

import java.util.ArrayList;
import java.util.Locale;

public class MainActivity extends AppCompatActivity
{
    /**
     * String for logging
     */
    private final String TAG = this.getClass().getSimpleName();

    /**
     * TextToSpeech variable
     */
    private TextToSpeech mTextToSpeech;

    /**
     * Buttons used for the following functions:
     * Recording the voice
     * Swap the values between the 2 spinners
     * Send what was spoken through text message (original or translated)
     * Copy what was said to the user clipboard (original or translated)
     * View the database which contains the history of translations
     * Delete the database of translations
     */
    private ImageButton mRecord;
    private ImageButton mSwitchSpin;
    private ImageButton mSend;
    private ImageButton mSendTranslation;
    private ImageButton mCopy;
    private ImageButton mCopyTranslation;
    private ImageButton mView;
    private ImageButton mDelete;


    /**
     * Phrase holds the phrase the user speaks
     * Translation is the translation of the phrase
     */
    private String phrase;
    private String translation;

    private CheckBox mEncrypt;

    /**
     * Spinner inLang holds the language for what the user will speak
     * Spinner outLang holds the language for what will be translated
     */
    private Spinner inLang;
    private Spinner outLang;

    /**
     * Textview tellings the user to press the button to speak
     */
    private TextView mTextView;

    /**
     * EditText
     * One to hold what was recorded
     * One to hold what was translated
     */
    private EditText mRecorded;
    private EditText mTranslated;

    /**
     * Speech Recognizer
     */
    private SpeechRecognizer mSpeechRecognizer;

    /**
     * Intent for Speech Recognizer
     */
    private Intent mSpeechRecognizerIntent;

    /**
     * Locales for all the different languages used by the app
     */
    private Locale locArabic = new Locale("ar","EG");
    private Locale locBulgarian = new Locale("bg", "BG");
    private Locale locCatalan = new Locale("ca","ES");
    private Locale locCzech = new Locale("cs", "CZ");
    private Locale locDanish = new Locale("da", "DK");
    private Locale locDutch = new Locale("nl", "BE");
    private Locale locEstonian = new Locale("et", "EE");
    private Locale locFinnish = new Locale("fi", "FI");
    private Locale locGreek = new Locale("el","GR");
    private Locale locHaitianCreole = new Locale("ht");
    private Locale locHebrew = new Locale("iw","IL");
    private Locale locHindi = new Locale("hi","IN");
    private Locale locHmongdaw = new Locale("hmn");
    private Locale locHungarian = new Locale("hu","HU");
    private Locale locIndonesian = new Locale("in","ID");
    private Locale locLatvian = new Locale("lv","LV");
    private Locale locLithuanian = new Locale("lt","LT");
    private Locale locMalay = new Locale("ms");
    private Locale locNorwegian = new Locale("nb","NO");
    private Locale locPersian = new Locale("fa");
    private Locale locPolish = new Locale("pl","PL");
    private Locale locPortuguese = new Locale("pt","PT");
    private Locale locRomanian = new Locale("ro","RO");
    private Locale locRussian = new Locale("ru","RU");
    private Locale locSlovak = new Locale("sk","SK");
    private Locale locSlovenian = new Locale("sl","SI");
    private Locale locSpanish = new Locale("es","US");
    private Locale locSwedish = new Locale("sv","SE");
    private Locale locTurkish = new Locale("tr","TR");
    private Locale locUkrainian = new Locale("uk","UA");
    private Locale locUrdu = new Locale("ur");
    private Locale locVietnamese = new Locale("vi","VN");

    /**
     * Array for all the different locales
     */
    private Locale[] language = {locArabic, locBulgarian, locCatalan,
                                 Locale.SIMPLIFIED_CHINESE, Locale.TRADITIONAL_CHINESE,
                                 locCzech, locDanish, locDutch,Locale.ENGLISH,
                                 locEstonian, locFinnish, Locale.FRENCH, Locale.GERMAN,
                                 locGreek, locHaitianCreole, locHebrew, locHindi,
                                 locHmongdaw, locHungarian, locIndonesian, Locale.ITALIAN,
                                 Locale.JAPANESE, Locale.KOREAN, locLatvian, locLithuanian,
                                 locMalay, locNorwegian, locPersian, locPolish,
                                 locPortuguese, locRomanian, locRussian, locSlovak,
                                 locSlovenian, locSpanish, locSwedish, Locale.TAIWAN,
                                 locTurkish, locUkrainian, locUrdu, locVietnamese};


    /**
     * Array for all the different languages
     */
    private Language[] transto = {Language.ARABIC, Language.BULGARIAN, Language.CATALAN,
                                  Language.CHINESE_SIMPLIFIED, Language.CHINESE_TRADITIONAL,
                                  Language.CZECH, Language.DANISH, Language.DUTCH, Language.ENGLISH,
                                  Language.ESTONIAN, Language.FINNISH, Language.FRENCH, Language.GERMAN,
                                  Language.GREEK, Language.HAITIAN_CREOLE, Language.HEBREW, Language.HINDI,
                                  Language.HMONG_DAW, Language.HUNGARIAN, Language.INDONESIAN, Language.ITALIAN,
                                  Language.JAPANESE, Language.KOREAN, Language.LATVIAN, Language.LITHUANIAN,
                                  Language.MALAY, Language.NORWEGIAN, Language.PERSIAN, Language.POLISH,
                                  Language.PORTUGUESE, Language.ROMANIAN, Language.RUSSIAN, Language.SLOVAK,
                                  Language.SLOVENIAN, Language.SPANISH, Language.SWEDISH, Language.THAI,
                                  Language.TURKISH, Language.UKRAINIAN, Language.URDU, Language.VIETNAMESE};

    /**
     * Boolean pressed for the translation button
     */
    private boolean pressed = false;


    private Locale speechLan;
    private Language translated;
    private int mpositionTranslation, mpositionRecorded;

    /**
     * SQL Database for storing the history of translations
     */
    Database myDB;


    /**
     * Async tasks to execute the translation
     */
class RecordTask extends AsyncTask<Void, Void, Void>
    {
        /**
         * Translation must be done in the background
         */
        protected Void doInBackground(Void... arg)
        {

            try
            {
                Translate.setClientId("DDRX"); // Set the Translation API Client Key
                Translate.setClientSecret("AndHisNameIsJohnCena"); // Set the Translation API Secret Key
                // System.out.println("Phrase is " + phrase);
                translation = Translate.execute(phrase, translated).toString(); // Execute the translation
                speakTranslation(translation); // Speak
            }
            catch (Exception e) {
                e.printStackTrace();
            }

            return null;
        }

        /**
         * Once translation is done, show it on the EditText view
         */
        protected void onPostExecute(Void result) {
            // System.out.println("Translation is " + translation);
            mTranslated.setText(translation);
            myDB.insertData(phrase,translation,language[mpositionRecorded].getDisplayName(),language[mpositionTranslation].getDisplayName());
            super.onPostExecute(result);
        }

    }

    /**
     * Sets the language for the text to speech to whatever the translation spinner is set as
     * And then performs the speech
     * @param phrase The string that holds the translation of the phrase
     * @exception No ne
     * @return No return value.
     */
    private void speakPhrase(String phrase)
    {
        mTextToSpeech.setLanguage(language[mpositionRecorded]);
        mTextToSpeech.speak(phrase, TextToSpeech.QUEUE_FLUSH, null, "phrase");
    }

    /**
     * Sets the language for the text to speech to whatever the translation spinner is set as
     * And then performs the speech
     * @param translation The string that holds the translation of the phrase
     * @exception None
     * @return No return value.
     */
    private void speakTranslation(String translation)
    {
        mTextToSpeech.setLanguage(language[mpositionTranslation]);
        mTextToSpeech.speak(translation, TextToSpeech.QUEUE_FLUSH, null, "translation");
    }

    /**
     * Deletes the history of translations from the database
     * @param arg None
     * @exception None
     * @return No return value.
     */
    public void deleteDB()
    {
        mDelete.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                // Delete all entries from the database
                myDB.deleteAllData();
            }
        });
    }

    /**
     * Views the history of translations from the databases
     * @param None
     * @exception None
     * @return No return value.
     */
    public void viewLog()
    {
        // Set onClickListener for the view button to display
        // the logs stored in the SQLite database
        mView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Load the database entries into the Cursor res
                Cursor res = myDB.getAllData();
                if (res.getCount() == 0) {
                    showMessage("Error", "Nothing Found");
                    return;
                }

                StringBuilder buffer = new StringBuilder();

                // While there are still more entries in the log, load them into the buffer
                while (res.moveToNext()) {
                    //buffer.append("Id : " + res.getString(0) + "\n");
                    buffer.append("\n");
                    buffer.append("Spoken Phrase : " + res.getString(1) + "\n");
                    buffer.append("Translation : " + res.getString(2) + "\n");
                    buffer.append("Detected Language : " + res.getString(3) + "\n");
                    buffer.append("Translated Language : " + res.getString(4) + "\n");
                    buffer.append("\n");
                }

                // Show all data
                showMessage("Data", buffer.toString());
            }
        });
    }

    /**
     * Shows the contents of the database
     * @param title A string that holds the title
     * @param message A string that holds a message to display to the user
     * @exception None
     * @return No return value.
     */
    public void showMessage(String title, String message)
    {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setCancelable(true);
        builder.setTitle(title);
        builder.setMessage(message);
        builder.show();
    }

    /**
     *
     */
    public void sendMessageDialogBox(String sample)
    {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setCancelable(true);
        builder.setTitle("Send Message");

        final EditText phoneNumber = new EditText(this);
        final EditText Message = new EditText(this);

        phoneNumber.setHint("xxxxxxxxxx");

        LinearLayout layout = new LinearLayout(getApplicationContext());

        layout.setOrientation(LinearLayout.VERTICAL);
        layout.addView(phoneNumber);
        layout.addView(Message);
        builder.setView(layout);

        builder.setPositiveButton("Send", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int whichButton) {

            }
        });

        builder.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int whichButton){

            }
        });

        builder.show();
    }

    /**
     * Life-cycle Functionality
     */
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        /**
         * Initialize the values of the buttons, edittext, database, spinner
         */

        myDB = new Database(this);
        mRecorded = (EditText) findViewById(R.id.recorded);
        mTranslated = (EditText) findViewById(R.id.translated);
        mRecord = (ImageButton) findViewById(R.id.record);
        mCopy = (ImageButton) findViewById(R.id.copy);
        mCopyTranslation = (ImageButton) findViewById(R.id.copytranslate);
        mSend = (ImageButton) findViewById(R.id.send);
        mSendTranslation = (ImageButton) findViewById(R.id.sendtranslate);
        mSwitchSpin = (ImageButton) findViewById(R.id.switchSpinner);
        mTextView = (TextView) findViewById(R.id.text);
        mSend = (ImageButton) findViewById(R.id.send);
        mView = (ImageButton) findViewById(R.id.view);
        mDelete = (ImageButton) findViewById(R.id.delete);
        inLang = (Spinner) findViewById(R.id.input);
        outLang = (Spinner) findViewById(R.id.output);

        // Speech recognizer initialized

        // Swap the values of the two spinners in order to do reserve translation
        mSwitchSpin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                inLang.setSelection(mpositionTranslation);
                outLang.setSelection(mpositionRecorded);

                String temp = translation;
                translation = phrase;
                phrase = temp;

                mRecorded.setText(phrase);
                mTranslated.setText(translation);

                int temp2 = mpositionTranslation;
                mpositionTranslation = mpositionRecorded;
                mpositionRecorded = temp2;

                speechLan = language[mpositionRecorded];
                translated = transto[mpositionTranslation];

            }
        });

        // Allows user to send a premade text message with the recently spoken phrase
        mSend.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //Intent sendIntent = new Intent(Intent.ACTION_VIEW);
                //sendIntent.setData(Uri.parse("sms:"));
                //sendIntent.putExtra("sms_body", mRecorded.getText());
                //startActivity(sendIntent);
                sendMessageDialogBox("Message");
                //sendSms();
            }
        });

        // Copy contents of the text to the clipboard
        mCopy.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ClipboardManager clipboard = (ClipboardManager) getSystemService(Context.CLIPBOARD_SERVICE);
                ClipData clip = ClipData.newPlainText("Copied", mRecorded.getText());
                clipboard.setPrimaryClip(clip);
                Toast.makeText(MainActivity.this, "Speech copied to clipboard!", Toast.LENGTH_SHORT).show();
            }
        });


        // Copy contents of the text to the clipboard
        mCopyTranslation.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ClipboardManager clipboard = (ClipboardManager) getSystemService(Context.CLIPBOARD_SERVICE);
                ClipData clip = ClipData.newPlainText("Copied", mTranslated.getText());
                clipboard.setPrimaryClip(clip);
                Toast.makeText(MainActivity.this, "Translation copied to clipboard!", Toast.LENGTH_SHORT).show();
            }
        });

        // On the touch of the translated phrase, the texttospeech will speak the translation
        mTranslated.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                if (MotionEvent.ACTION_UP == event.getAction()) {
                    mTranslated.requestFocus();
                    speakTranslation(translation);
                }
                return true; // return
            }
        });

        // On the touch of the recorded phrase, the texttospeech will speak the phrase
        mRecorded.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                if (MotionEvent.ACTION_UP == event.getAction()) {
                    mRecorded.requestFocus();
                    speakPhrase(phrase);
                }
                return true; // return
            }
        });

        /*
        mRecorded.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                speakTranslation(phrase);
            }
        });
        */

        // Speech recognizer
        mSpeechRecognizer = SpeechRecognizer.createSpeechRecognizer(this);
        // Text to Speech listener
        mTextToSpeech = new TextToSpeech(getApplicationContext(), new TextToSpeech.OnInitListener() {
            @Override
            public void onInit(int status) {

            }
        });

        // Speech listener
        SpeechRecognitionListener listener = new SpeechRecognitionListener();
        mSpeechRecognizer.setRecognitionListener(listener);
        mRecord.setOnTouchListener(new View.OnTouchListener() {

            @Override
            public boolean onTouch(View v, MotionEvent event) {
                switch (event.getAction()) {
                    case MotionEvent.ACTION_DOWN: {
                        v.setPressed(true);
                        if (!pressed) {
                            mSpeechRecognizerIntent = new Intent(RecognizerIntent.ACTION_RECOGNIZE_SPEECH);
                            mSpeechRecognizerIntent.putExtra(RecognizerIntent.EXTRA_LANGUAGE, speechLan.toString());
                            mSpeechRecognizerIntent.putExtra(RecognizerIntent.EXTRA_LANGUAGE_MODEL, RecognizerIntent.LANGUAGE_MODEL_FREE_FORM);
                            mSpeechRecognizerIntent.putExtra(RecognizerIntent.EXTRA_LANGUAGE_PREFERENCE, speechLan);
                            mSpeechRecognizerIntent.putExtra(RecognizerIntent.EXTRA_SUPPORTED_LANGUAGES, speechLan);
                            mSpeechRecognizerIntent.putExtra(RecognizerIntent.EXTRA_ONLY_RETURN_LANGUAGE_PREFERENCE, speechLan);
                            mSpeechRecognizerIntent.putExtra(RecognizerIntent.EXTRA_CALLING_PACKAGE, speechLan);
                            mSpeechRecognizerIntent.putExtra(RecognizerIntent.EXTRA_MAX_RESULTS, 1);
                            mSpeechRecognizerIntent.putExtra(RecognizerIntent.EXTRA_RESULTS, speechLan);
                            pressed = true;
                            Toast.makeText(MainActivity.this, "Recording Started", Toast.LENGTH_SHORT).show();
                            mSpeechRecognizer.startListening(mSpeechRecognizerIntent);
                        }

                        break;
                    }
                    case MotionEvent.ACTION_CANCEL:

                        break;
                    case MotionEvent.ACTION_UP: {
                        v.setPressed(false);
                        Toast.makeText(MainActivity.this, "Recording Stopped", Toast.LENGTH_SHORT).show();
                        mSpeechRecognizer.stopListening();
                        pressed = false;
                        break;
                    }
                }
                return true;
            }
        });

        ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(this, R.array.nLanguages, android.R.layout.simple_spinner_item);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        inLang.setAdapter(adapter);
        inLang.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                mpositionRecorded = position;
                speechLan = language[position];
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });
        outLang.setAdapter(adapter);
        outLang.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                //Toast.makeText(MainActivity.this, "you selected language at position " + Integer.toString(position), Toast.LENGTH_SHORT).show();
                mpositionTranslation = position;
                translated = transto[position];
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });
        viewLog();
        deleteDB();
    }

    protected void sendSms()
    {
        SmsManager manager = SmsManager.getDefault();
        manager.sendTextMessage("9085481212",null,"EAT my BALLS",null,null);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu)
    {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_speak2me, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item)
    {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    @Override
    protected void onRestart()
    {
        super.onRestart();

        // Notification that the activity will be started
        Log.i(TAG, "onStart");
    }

    @Override
    protected void onStart()
    {
        super.onStart();

        // Notification that the activity will be started
        Log.i(TAG, "onStart");
    }

    @Override
    protected void onPause()
    {
        super.onPause();

        // Notification that the activity will stop interacting with the user
        Log.i(TAG, "onPause" + (isFinishing() ? " Finishing" : ""));
    }

    @Override
    protected void onStop()
    {
        super.onStop();

        // Notification that the activity is no longer visible
        Log.i(TAG, "onStop");
    }

    @Override
    protected void onDestroy()
    {
        super.onDestroy();

        // Check if speech recognizer is existing, if so destroy the recognizer
        if (mSpeechRecognizer != null)
        {
            mSpeechRecognizer.destroy();
        }

        // Notification that the activity will be destroyed
        Log.i(TAG,
                "onDestroy " // Log which, if any, configuration changed
                        + Integer.toString(getChangingConfigurations(), 16));
    }

    ///////////////////////////////////////////////////////////////////////////////
    // Called during the lifecycle, when instance state should be saved/restored //
    ///////////////////////////////////////////////////////////////////////////////
    @Override
    protected void onSaveInstanceState(Bundle outstate)
    {
        outstate.putString("Saved", "Instance");
        super.onSaveInstanceState(outstate);

        Log.i(TAG, "onSaveInstanceState");
    }

    @Override
    protected void onRestoreInstanceState(Bundle state)
    {
        super.onRestoreInstanceState(state);
    }

    @Override
    public void onResume()
    {
        super.onResume();

        // Notification that the application is resumed.
        Log.i(TAG, "onResume");
    }

    protected class SpeechRecognitionListener implements RecognitionListener
    {

        @Override
        public void onReadyForSpeech(Bundle params)
        {

        }

        @Override
        public void onBeginningOfSpeech()
        {
            mSpeechRecognizer.startListening(mSpeechRecognizerIntent);
        }

        @Override
        public void onRmsChanged(float rmsdB)
        {

        }

        @Override
        public void onBufferReceived(byte[] buffer)
        {

        }

        @Override
        public void onEndOfSpeech()
        {

        }

        @Override
        public void onError(int error)
        {
           // mSpeechRecognizer.startListening(mSpeechRecognizerIntent);
        }

        @Override
        public void onResults(Bundle results)
        {
            ArrayList<String> result = results.getStringArrayList(SpeechRecognizer.RESULTS_RECOGNITION);
            if (result != null) {
                phrase = result.get(0).toString();
                mRecorded.setText(phrase);
                new RecordTask().execute();
            }
            mSpeechRecognizer.stopListening();
        }

        @Override
        public void onPartialResults(Bundle partialResults)
        {

        }

        @Override
        public void onEvent(int eventType, Bundle params)
        {

        }
    }
}

