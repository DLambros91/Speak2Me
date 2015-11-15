package com.example.dlambros.speak2me;

import android.content.res.Configuration;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;

public class MainActivity extends AppCompatActivity
{
    // String for logging
    private final String TAG = this.getClass().getSimpleName();

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
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

        Log.i(TAG, "onStop");
    }

    @Override
    protected void onDestroy()
    {
        super.onDestroy();

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
        Log.i(TAG, "onResume");
    }
}
