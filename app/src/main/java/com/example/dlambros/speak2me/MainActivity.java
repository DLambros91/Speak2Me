package com.example.dlambros.speak2me;

import android.content.ClipData;
import android.content.ClipboardManager;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.speech.RecognitionListener;
import android.speech.RecognizerIntent;
import android.speech.SpeechRecognizer;
import android.speech.tts.TextToSpeech;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
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
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.memetix.mst.language.Language;
import com.memetix.mst.translate.Translate;

import java.util.ArrayList;
import java.util.Locale;

public class MainActivity extends AppCompatActivity
{
    // String for logging
    private final String TAG = this.getClass().getSimpleName();

    private TextToSpeech mTextToSpeech;

    private ImageButton mRecord;

    private String phrase;
    private String translation;

    private CheckBox mEncrypt;

    private ImageButton mSend;
    private ImageButton mSendTranslation;

    private ImageButton mCopy;
    private ImageButton mCopyTranslation;


    private ImageButton mView;
    private ImageButton mDelete;

    private Spinner inLang;
    private Spinner outLang;

    private TextView mTextView;

    private EditText mRecorded;
    private EditText mTranslated;

    private SpeechRecognizer mSpeechRecognizer;

   private Intent mSpeechRecognizerIntent;

    // Locales for Locale array
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

    private boolean pressed = false;

    //private String speechLan;
    private Locale speechLan;
    private Language translated;
    private int mposition;
    //Locale SPANISH = new Locale("es", "ES");

    // SQL Database for storage
    Database myDB;

class RecordTask extends AsyncTask<Void, Void, Void>
    {
        @Override
        protected Void doInBackground(Void... arg)
        {

            try
            {
                Translate.setClientId("DDRX");
                Translate.setClientSecret("AndHisNameIsJohnCena");
                // System.out.println("Phrase is " + phrase);
                translation = Translate.execute(phrase, translated).toString();
                speakTranslation(translation);
            }
            catch (Exception e) {
                e.printStackTrace();
            }

            return null;
        }

        protected void onPostExecute(Void result) {
            System.out.println("Translation is " + translation);
            mTranslated.setText(translation);
            super.onPostExecute(result);
        }

    }


    private void speakTranslation(String translation)
    {
        mTextToSpeech.setLanguage(language[mposition]);
        mTextToSpeech.speak(translation, TextToSpeech.QUEUE_FLUSH, null, "translation");
    }

    /**
     * Database Functionality
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

    public void viewLog()
    {
        // Set onClickListener for the view button to display
        // the logs stored in the SQLite database
        mView.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                // Load the database entries into the Cursor res
                Cursor res = myDB.getAllData();
                if (res.getCount() == 0)
                {
                    showMessage("Error", "Nothing Found");
                    return;
                }

                StringBuilder buffer = new StringBuilder();

                // While there are still more entries in the log, load them into the buffer
                while (res.moveToNext())
                {
                    buffer.append("Id : " + res.getString(0) + "\n");
                    buffer.append("Received Text : " + res.getString(1) + "\n");
                    buffer.append("Date : " + res.getString(2) + "\n");
                    buffer.append("Detected Language : " + res.getString(3) + "\n");
                    buffer.append("Translated Language : " + res.getString(4) + "\n");
                }

                // Show all data
                showMessage("Data", buffer.toString());
            }
        });
    }

    public void showMessage(String title, String message)
    {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setCancelable(true);
        builder.setTitle(title);
        builder.setMessage(message);
        builder.show();
    }
    /**
     * Life-cycle Functionality
     */
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        myDB = new Database(this);
        mRecorded = (EditText) findViewById(R.id.recorded);
        mTranslated = (EditText) findViewById(R.id.translated);

        mRecord = (ImageButton) findViewById(R.id.record);

        mCopy = (ImageButton) findViewById(R.id.copy);
        mCopyTranslation = (ImageButton) findViewById(R.id.copytranslate);

        mSend = (ImageButton) findViewById(R.id.send);
        mSendTranslation = (ImageButton) findViewById(R.id.sendtranslate);

        mSend.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent sendIntent = new Intent(Intent.ACTION_VIEW);
                sendIntent.setData(Uri.parse("sms:"));
                sendIntent.putExtra("sms_body", mRecorded.getText());
                startActivity(sendIntent);
            }
        });

        mCopy.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ClipboardManager clipboard = (ClipboardManager)   getSystemService(Context.CLIPBOARD_SERVICE);
                ClipData clip = ClipData.newPlainText("Copied", mTranslated.getText());
                clipboard.setPrimaryClip(clip);
                Toast.makeText(MainActivity.this, "Text copied to clipboard!", Toast.LENGTH_SHORT).show();
            }
        });

        mCopyTranslation.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ClipboardManager clipboard = (ClipboardManager)   getSystemService(Context.CLIPBOARD_SERVICE);
                ClipData clip = ClipData.newPlainText("Copied", mTranslated.getText());
                clipboard.setPrimaryClip(clip);
                Toast.makeText(MainActivity.this, "Translation copied to clipboard!", Toast.LENGTH_SHORT).show();
            }
        });

        mSpeechRecognizer = SpeechRecognizer.createSpeechRecognizer(this);

        mTextToSpeech = new TextToSpeech(getApplicationContext(), new TextToSpeech.OnInitListener() {
            @Override
            public void onInit(int status) {

            }
        });



        SpeechRecognitionListener listener = new SpeechRecognitionListener();
        mSpeechRecognizer.setRecognitionListener(listener);
        mRecord.setOnTouchListener(new View.OnTouchListener() {

            @Override
            public boolean onTouch(View v, MotionEvent event) {
                switch (event.getAction()) {
                    case MotionEvent.ACTION_DOWN: {
                        v.setPressed(true);
                        if (!pressed) {
                            /**mSpeechRecognizerIntent = new Intent(RecognizerIntent.ACTION_RECOGNIZE_SPEECH);
                            mSpeechRecognizerIntent.putExtra(RecognizerIntent.EXTRA_LANGUAGE, Locale.SIMPLIFIED_CHINESE);
                            mSpeechRecognizerIntent.putExtra(RecognizerIntent.EXTRA_LANGUAGE_MODEL,
                                    speechLan);
                            mSpeechRecognizerIntent.putExtra(RecognizerIntent.EXTRA_MAX_RESULTS, 1);
                            mSpeechRecognizerIntent.putExtra(RecognizerIntent.EXTRA_CALLING_PACKAGE, getClass().getPackage().getName());
                            pressed = true;
                            Toast.makeText(MainActivity.this, "Recording Started", Toast.LENGTH_SHORT).show();
                            mSpeechRecognizer.startListening(mSpeechRecognizerIntent);
                            //new RecordTask().execute();*/
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
       // mEncrypt = (CheckBox) findViewById(R.id.encrypt);
        mTextView = (TextView) findViewById(R.id.text);
        mSend = (ImageButton) findViewById(R.id.send);
        mView = (ImageButton) findViewById(R.id.view);
        mDelete = (ImageButton) findViewById(R.id.delete);
        inLang = (Spinner) findViewById(R.id.input);
        outLang = (Spinner) findViewById(R.id.output);
        ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(this, R.array.nLanguages, android.R.layout.simple_spinner_item);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        inLang.setAdapter(adapter);
        inLang.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                speechLan = language[position];
                /*switch (position)
                {
                    case 0:
                        speechLan = "af";
                        break;
                    case 1:
                        speechLan = "eu";
                        break;
                    case 2:
                        speechLan = "bg";
                        break;
                    case 3:
                        speechLan = "ca";
                        break;
                    case 4:
                        speechLan = "ar-EG";
                        break;
                    case 5:
                        speechLan = "ar-JO";
                        break;
                    case 6:
                        speechLan = "ar-KW";
                        break;
                    case 7:
                        speechLan = "ar-LB";
                        break;
                    case 8:
                        speechLan = "ar-QA";
                        break;
                    case 9:
                        speechLan = "ar-AE";
                        break;
                    case 10:
                        speechLan = "ar-MA";
                        break;
                    case 11:
                        speechLan = "ar-IQ";
                        break;
                    case 12:
                        speechLan = "ar-DZ";
                        break;
                    case 13:
                        speechLan = "ar-BH";
                        break;
                    case 14:
                        speechLan = "ar-LY";
                        break;
                    case 15:
                        speechLan = "ar-OM";
                        break;
                    case 16:
                        speechLan = "ar-SA";
                        break;
                    case 17:
                        speechLan = "ar-TN";
                        break;
                    case 18:
                        speechLan = "ar-YE";
                        break;
                    case 19:
                        speechLan = "cs";
                        break;
                    case 20:
                        speechLan = "nl-NL";
                        break;
                    case 21:
                        speechLan = "en-AU";
                        break;
                    case 22:
                        speechLan = "en-CA";
                        break;
                    case 23:
                        speechLan = "en-IN";
                        break;
                    case 24:
                        speechLan = "en-NZ";
                        break;
                    case 25:
                        speechLan = "en-ZA";
                        break;
                    case 26:
                        speechLan = "en-GB";
                        break;
                    case 27:
                        speechLan = "en-US";
                        break;
                    case 28:
                        speechLan = "fi";
                        break;
                    case 29:
                        speechLan = "fr-FR";
                        break;
                    case 30:
                        speechLan = "gl";
                        break;
                    case 31:
                        speechLan = "de-DE";
                        break;
                    case 32:
                        speechLan = "he";
                        break;
                    case 33:
                        speechLan = "hu";
                        break;
                    case 34:
                        speechLan = "is";
                        break;
                    case 35:
                        speechLan = "it-IT";
                        break;
                    case 36:
                        speechLan = "id";
                        break;
                    case 37:
                        speechLan = "ja";
                        break;
                    case 38:
                        speechLan = "ko";
                        break;
                    case 39:
                        speechLan = "la";
                        break;
                    case 40:
                        speechLan = "zh_CN";
                        break;
                    case 41:
                        speechLan = "zh-TW";
                        break;
                    case 42:
                        speechLan = "zh-CN";
                        break;
                    case 43:
                        speechLan = "zh-HK";
                        break;
                    case 44:
                        speechLan = "zh-yue";
                        break;
                    case 45:
                        speechLan = "ms-MY";
                        break;
                    case 46:
                        speechLan = "no-NO";
                        break;
                    case 47:
                        speechLan = "pl";
                        break;
                    case 48:
                        speechLan = "xx-piglatin";
                        break;
                    case 49:
                        speechLan = "pt-PT";
                        break;
                    case 50:
                        speechLan = "pt-BR";
                        break;
                    case 51:
                        speechLan = "ro-RO";
                        break;
                    case 52:
                        speechLan = "ru";
                        break;
                    case 53:
                        speechLan = "sr-SP";
                        break;
                    case 54:
                        speechLan = "sk";
                        break;
                    case 55:
                        speechLan = "es-AR";
                        break;
                    case 56:
                        speechLan = "es-BO";
                        break;
                    case 57:
                        speechLan = "es-CL";
                        break;
                    case 58:
                        speechLan = "es-CO";
                        break;
                    case 59:
                        speechLan = "es-CR";
                        break;
                    case 60:
                        speechLan = "es-DO";
                        break;
                    case 61:
                        speechLan = "es-EC";
                        break;
                    case 62:
                        speechLan = "es-SV";
                        break;
                    case 63:
                        speechLan = "es-GT";
                        break;
                    case 64:
                        speechLan = "es-HN";
                        break;
                    case 65:
                        speechLan = "es-MX";
                        break;
                    case 66:
                        speechLan = "es-NI";
                        break;
                    case 67:
                        speechLan = "es-PA";
                        break;
                    case 68:
                        speechLan = "es-PY";
                        break;
                    case 69:
                        speechLan = "es-PE";
                        break;
                    case 70:
                        speechLan = "es-PR";
                        break;
                    case 71:
                        speechLan = "es-ES";
                        break;
                    case 72:
                        speechLan = "es-US";
                        break;
                    case 73:
                        speechLan = "es-UY";
                        break;
                    case 74:
                        speechLan = "es-VE";
                        break;
                    case 75:
                        speechLan = "sv-SE";
                        break;
                    case 76:
                        speechLan = "tr";
                        break;
                    case 77:
                        speechLan = "zu";
                        break;
                }*/
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
                mposition = position;
                translated = transto[position];
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });
        viewLog();
        deleteDB();
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

