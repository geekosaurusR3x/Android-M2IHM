package com.skad.android.androidm2ihm.activity;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import com.skad.android.androidm2ihm.R;
import com.skad.android.androidm2ihm.model.Score;

public class MainActivity extends ActionBarActivity implements Button.OnClickListener, DialogInterface.OnClickListener {

    private static final String TAG = "MainActivity";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // Setup button click listener
        findViewById(R.id.button_level_1).setOnClickListener(this);
        findViewById(R.id.button_level_3).setOnClickListener(this);
        findViewById(R.id.button_level_2).setOnClickListener(this);
        findViewById(R.id.button_editeur).setOnClickListener(this);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.main, menu);
        return super.onCreateOptionsMenu(menu);
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
            case R.id.action_highscore:
                showHighScores();
                return true;
            case R.id.action_about:
                startActivity(new Intent(this, AboutActivity.class));
                return true;
        }
        return super.onOptionsItemSelected(item);
    }

    private void showHighScores() {
        Score score = new Score(1);
        AlertDialog.Builder ald = new AlertDialog.Builder(this);
        ald.setTitle(getString(R.string.dialog_highscores_title));
        ald.setMessage(String.format(getString(R.string.dialog_highscores_msg), score.getHighScore(this), score.getHighScore(this, 2), score.getHighScore(this, 3)));
        ald.setNeutralButton(android.R.string.cancel, null);
        ald.setNegativeButton(R.string.dialog_highscores_btn_reset, this);
        ald.create().show();
    }

    @Override
    public void onClick(View view) {
        int numtag = Integer.parseInt(view.getTag().toString());
        Intent gameIntent;
        if (numtag == 0) {
            gameIntent = new Intent(this, EditorActivity.class);
        } else {
            gameIntent = new Intent(this, LevelActivity.class);
            gameIntent.putExtra(getString(R.string.extra_key_level), numtag);
        }
        startActivity(gameIntent);
    }

    @Override
    public void onClick(DialogInterface dialog, int which) {
        switch (which) {
            case DialogInterface.BUTTON_NEGATIVE:
                Score.resetHighScores(this);
                break;
        }
    }
}
