package com.skad.android.androidm2ihm.activity;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import com.skad.android.androidm2ihm.R;

public class MainActivity extends Activity implements Button.OnClickListener {

    private static final String TAG = "MainActivity";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // Setup button click listener
        findViewById(R.id.button_level_1).setOnClickListener(this);
        findViewById(R.id.button_level_3).setOnClickListener(this);
        findViewById(R.id.button_level_2).setOnClickListener(this);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        /*
        Handle action bar item clicks here. The action bar will
        automatically handle clicks on the Home/Up button, so long
        as you specify a parent activity in AndroidManifest.xml.
        */
        switch (item.getItemId()) {
            case R.id.action_settings:
                startActivity(new Intent(this, SettingsActivity.class));
                return true;
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onClick(View view) {
        Intent gameIntent = new Intent(this, LevelActivity.class);
        gameIntent.putExtra(getString(R.string.extra_key_level), Integer.parseInt(view.getTag().toString()));
        startActivity(gameIntent);
    }
}
