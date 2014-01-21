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
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.ScrollView;
import com.skad.android.androidm2ihm.R;
import com.skad.android.androidm2ihm.model.Score;
import com.skad.android.androidm2ihm.task.MoveSdCard;
import com.skad.android.androidm2ihm.task.UploadLvl;
import com.skad.android.androidm2ihm.utils.FileUtils;

import java.io.File;
import java.util.ArrayList;

public class MainActivity extends ActionBarActivity implements Button.OnClickListener, DialogInterface.OnClickListener {

    private static final String TAG = "MainActivity";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        SharedPreferences sharedPreferences = PreferenceManager.getDefaultSharedPreferences(this);
        boolean assetsAlreadyMoved = sharedPreferences.getBoolean("Content Moved", false);

        //testing if files are present and ifnot remove
        if (!FileUtils.fileExist(getExternalFilesDir(null) + File.separator + "default")) {
            assetsAlreadyMoved = false;
        }
        if (!assetsAlreadyMoved) {
            MoveSdCard movetosd = new MoveSdCard(this);
            movetosd.execute(null);
        } else {
            fecthLvl();
        }

        Button btn = (Button) findViewById(R.id.button_editeur);
        btn.setTag(R.id.main_lvl_num_tag, 0);
        btn.setTag(R.id.main_lvl_dir_tag, "");
        btn.setOnClickListener(this);

    }

    public void fecthLvl() {
        ScrollView listButtonLvl = (ScrollView) findViewById(R.id.main_list_button);
        //clean the view
        listButtonLvl.removeAllViews();
        LinearLayout linearLayout = new LinearLayout(this);
        linearLayout.setOrientation(LinearLayout.VERTICAL);

        ArrayList<String> listLvl = FileUtils.listLvl(this);
        int numlvl = 1;
        for (String lvl : listLvl) {
            LinearLayout linearLayoutsub = new LinearLayout(this);
            linearLayoutsub.setOrientation(LinearLayout.HORIZONTAL);
            //TODO
            //Make the sublayout fill the parent
            //linearLayout.setLayoutParams(new LinearLayout.LayoutParams(LinearLayout.LayoutParams.FILL_PARENT));
            //playing button
            Button buttonplay = new Button(this);
            buttonplay.setText(lvl);
            buttonplay.setTag(R.id.main_lvl_num_tag, numlvl);
            buttonplay.setTag(R.id.main_lvl_dir_tag, lvl);
            buttonplay.setOnClickListener(this);
            //edit button
            ImageButton buttonedit = new ImageButton(this);
            buttonedit.setImageResource(R.drawable.ic_action_edit);
            buttonedit.setTag(R.id.main_lvl_num_tag, 0);
            buttonedit.setTag(R.id.main_lvl_dir_tag, lvl);
            buttonedit.setOnClickListener(this);
            //remove button
            ImageButton buttonsup = new ImageButton(this);
            buttonsup.setImageResource(R.drawable.ic_action_remove);
            buttonsup.setTag(R.id.main_lvl_num_tag, -1);
            buttonsup.setTag(R.id.main_lvl_dir_tag, lvl);
            buttonsup.setOnClickListener(this);
            //share button
            ImageButton buttonup = new ImageButton(this);
            buttonup.setImageResource(R.drawable.ic_action_share);
            buttonup.setTag(R.id.main_lvl_num_tag, -2);
            buttonup.setTag(R.id.main_lvl_dir_tag, lvl);
            buttonup.setOnClickListener(this);

            linearLayoutsub.addView(buttonplay);
            linearLayoutsub.addView(buttonedit);
            linearLayoutsub.addView(buttonsup);
            linearLayoutsub.addView(buttonup);

            linearLayout.addView(linearLayoutsub);
            numlvl++;
        }
        listButtonLvl.addView(linearLayout);
    }

    public void onFinishMoveFile() {
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
    protected void onResume() {
        super.onResume();
        fecthLvl();
    }

    @Override
    public void onClick(View view) {
        int numtag = Integer.parseInt(view.getTag(R.id.main_lvl_num_tag).toString());
        String dirtag = view.getTag(R.id.main_lvl_dir_tag).toString();

        Intent gameIntent = null;
        if (numtag == -2) {
            UploadLvl uploadLvl = new UploadLvl(this);
            uploadLvl.execute(dirtag);
        } else if (numtag == -1) {
            FileUtils.deleteLvl(this, dirtag);
            fecthLvl();
        } else if (numtag == 0) {
            gameIntent = new Intent(this, EditorActivity.class);
            gameIntent.putExtra(getString(R.string.extra_key_level_dir), dirtag);
        } else {
            gameIntent = new Intent(this, LevelActivity.class);
            gameIntent.putExtra(getString(R.string.extra_key_level), numtag);
            gameIntent.putExtra(getString(R.string.extra_key_level_dir), dirtag);
        }

        if (gameIntent != null) {
            startActivity(gameIntent);
        }
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
