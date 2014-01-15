package com.skad.android.androidm2ihm.activity;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.v7.app.ActionBarActivity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;
import com.skad.android.androidm2ihm.R;
import com.skad.android.androidm2ihm.model.Score;
import com.skad.android.androidm2ihm.task.MoveSdCard;
import com.skad.android.androidm2ihm.utils.FileUtils;

import java.io.File;

public class MainActivity extends ActionBarActivity implements Button.OnClickListener, DialogInterface.OnClickListener {

    private static final String TAG = "MainActivity";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        SharedPreferences sharedPreferences = PreferenceManager.getDefaultSharedPreferences(this);
        boolean assetsAlreadyMoved = sharedPreferences.getBoolean("Content Moved", false);

        //testing if files are present and ifnot remove
        if(!FileUtils.fileExist(getExternalFilesDir(null)+ File.separator+"default"))
        {
            assetsAlreadyMoved = false;
        }
        if(!assetsAlreadyMoved)
        {
            MoveSdCard movetosd = new MoveSdCard(this);
            movetosd.execute(null);
        }
        else
        {
            fecthLvl();
        }

    }

    public void fecthLvl()
    {
        findViewById(R.id.button_level_1).setVisibility(View.VISIBLE);
        findViewById(R.id.button_level_2).setVisibility(View.VISIBLE);
        findViewById(R.id.button_level_3).setVisibility(View.VISIBLE);
        findViewById(R.id.button_editeur).setVisibility(View.VISIBLE);

        findViewById(R.id.button_level_1).setOnClickListener(this);
        findViewById(R.id.button_level_3).setOnClickListener(this);
        findViewById(R.id.button_level_2).setOnClickListener(this);
        findViewById(R.id.button_editeur).setOnClickListener(this);
    }
    public void onFinishMoveFile()
    {
        SharedPreferences sharedPreferences = PreferenceManager.getDefaultSharedPreferences(this);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putBoolean("Content Moved", true);
        editor.commit();
        fecthLvl();
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
        String dirtag = "lvl"+numtag;

        Intent gameIntent;
        if (numtag == 0) {
            gameIntent = new Intent(this, EditorActivity.class);
        } else {
            gameIntent = new Intent(this, LevelActivity.class);
            gameIntent.putExtra(getString(R.string.extra_key_level), numtag);
            gameIntent.putExtra(getString(R.string.extra_key_level_dir), dirtag);
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
