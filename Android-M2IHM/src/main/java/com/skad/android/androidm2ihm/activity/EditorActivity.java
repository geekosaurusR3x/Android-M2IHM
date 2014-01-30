package com.skad.android.androidm2ihm.activity;


import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.os.Bundle;
import android.os.Handler;
import android.os.Vibrator;
import android.support.v7.app.ActionBar;
import android.support.v7.app.ActionBarActivity;
import android.support.v7.widget.PopupMenu;
import android.util.DisplayMetrics;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;
import com.skad.android.androidm2ihm.R;
import com.skad.android.androidm2ihm.model.Level;
import com.skad.android.androidm2ihm.utils.FileUtils;
import com.skad.android.androidm2ihm.utils.LevelParser;
import com.skad.android.androidm2ihm.view.EditorView;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.*;

/**
 * Created by skad on 09/01/14.
 */
public class EditorActivity extends ActionBarActivity implements View.OnTouchListener, View.OnLongClickListener, View.OnClickListener, Observer {
    private static final String TAG = "EditorActivity";
    DisplayMetrics mMetrics;
    // Views
    private EditorView mEditorView;
    private List<Button> mLevelButtons;
    private int mCurrentTag;
    private float mXTouch;
    private float mYTouch;
    private int mIdSelected;
    private Handler repeatUpdateHandler = new Handler();
    private boolean mButtonRepeatLock = false;
    private Level mLevel;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_editeur);

        mCurrentTag = 0;
        mIdSelected = -1;

        mMetrics = getResources().getDisplayMetrics();

        String levelDir = getIntent().getStringExtra(getString(R.string.extra_key_level_dir));
        mLevel = LevelParser.getLevelFromFile(this, levelDir, 0, mMetrics.widthPixels, mMetrics.heightPixels);
        mLevel.addObserver(this);

        // Retain views
        mEditorView = (EditorView) findViewById(R.id.editeur_lvl);
        mEditorView.setOnTouchListener(this);
        mEditorView.setOnLongClickListener(this);
        Button leftButton = (Button) findViewById(R.id.editeur_left_button);
        Button rightButton = (Button) findViewById(R.id.editeur_right_button);
        Button upButton = (Button) findViewById(R.id.editeur_up_button);
        Button downButton = (Button) findViewById(R.id.editeur_down_button);
        Button widthMinusButton = (Button) findViewById(R.id.editeur_widthminus_button);
        Button widthPlusButton = (Button) findViewById(R.id.editeur_widthplus_button);
        Button heightMinusButton = (Button) findViewById(R.id.editeur_heightminus_button);
        Button heightPlusButton = (Button) findViewById(R.id.editeur_heightplus_button);
        Button rotatePlusButton = (Button) findViewById(R.id.editeur_rotate_plus);
        Button rotateMinusButton = (Button) findViewById(R.id.editeur_rotate_minus);
        mLevelButtons = new ArrayList<Button>(Arrays.asList(leftButton, rightButton, upButton, downButton, widthPlusButton, widthMinusButton, heightPlusButton, heightMinusButton, rotatePlusButton, rotateMinusButton));

        // Assign listeners
        findViewById(R.id.editeur_add_button).setOnClickListener(new Button.OnClickListener() {
            @Override
            public void onClick(View view) {
                showPopupMenu(view);
            }
        });
        findViewById(R.id.editeur_save_button).setOnClickListener(new Button.OnClickListener() {
            @Override
            public void onClick(View view) {
                save();
            }
        });
        findViewById(R.id.editeur_remove_button).setOnClickListener(new Button.OnClickListener() {
            @Override
            public void onClick(View view) {
                mEditorView.removeElement(mIdSelected);
                mIdSelected--;
            }
        });
        for (Button btn : mLevelButtons) {
            btn.setVisibility(mLevel.getAllSprites().isEmpty() ? View.INVISIBLE : View.VISIBLE);
            btn.setOnClickListener(this);
            btn.setOnLongClickListener(this);
            btn.setOnTouchListener(this);
        }

        // Hide ActionBar
        ActionBar actionBar = getSupportActionBar();
        if (actionBar != null) {
            actionBar.hide();
        }

        // Hide buttons if Level is empty
        findViewById(R.id.editeur_remove_button).setVisibility(mLevel.getAllSprites().isEmpty() ? View.INVISIBLE : View.VISIBLE);

        // Prompt user for a file name
        showSaveDialog(levelDir);
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        overridePendingTransition(R.anim.push_left_in, R.anim.push_left_out);
    }

    @Override
    public void onClick(View view) {
        performAction(view.getId());
    }

    /**
     * What to do when a button is pressed/held down
     *
     * @param action
     */
    private void performAction(int action) {
        switch (action) {
            case R.id.editeur_left_button:
                mEditorView.moveLeft(mIdSelected);
                break;
            case R.id.editeur_right_button:
                mEditorView.moveRight(mIdSelected);
                break;
            case R.id.editeur_up_button:
                mEditorView.moveUp(mIdSelected);
                break;
            case R.id.editeur_down_button:
                mEditorView.moveDown(mIdSelected);
                break;
            case R.id.editeur_widthminus_button:
                mEditorView.widthMinus(mIdSelected);
                break;
            case R.id.editeur_widthplus_button:
                mEditorView.widthPlus(mIdSelected);
                break;
            case R.id.editeur_heightminus_button:
                mEditorView.heightMinus(mIdSelected);
                break;
            case R.id.editeur_heightplus_button:
                mEditorView.heightPlus(mIdSelected);
                break;
            case R.id.editeur_rotate_plus:
                mEditorView.rotatePlus(mIdSelected);
                break;
            case R.id.editeur_rotate_minus:
                mEditorView.rotateMinus(mIdSelected);
                break;
        }
    }

    /**
     * Displays a popup menu holding all items that a user can add to his level
     *
     * @param v Parent view
     */
    private void showPopupMenu(View v) {
        PopupMenu popupMenu = new PopupMenu(this, v);
        popupMenu.getMenuInflater().inflate(R.menu.editeur_menu, popupMenu.getMenu());

        popupMenu.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {
            @Override
            public boolean onMenuItemClick(MenuItem menuItem) {
                mCurrentTag = menuItem.getItemId();
                Toast.makeText(EditorActivity.this, String.format(getString(R.string.editeur_item_select), menuItem.getItemId()), Toast.LENGTH_LONG).show();
                return false;
            }
        });

        popupMenu.show();
    }

    @Override
    public boolean onLongClick(View view) {
        if (view.equals(mEditorView)) {
            mIdSelected = mEditorView.getElementId(mXTouch, mYTouch);
            if (mIdSelected != -1) {
                //Toast.makeText(EditorActivity.this, "Item Selected " + mIdSelected, Toast.LENGTH_LONG).show();
                Vibrator v = (Vibrator) getSystemService(Context.VIBRATOR_SERVICE);
                v.vibrate(50);
                findViewById(R.id.editeur_frame_button).setVisibility(View.VISIBLE);
            }

            return false;
        } else {
            mButtonRepeatLock = true;
            repeatUpdateHandler.post(new RepeatingUpdater(view.getId()));

            return false;
        }
    }

    @Override
    public boolean onTouch(View view, MotionEvent motionEvent) {
        int action = motionEvent.getAction();
        if (view.equals(mEditorView)) {
            switch (action) {
                case MotionEvent.ACTION_DOWN:
                    if (mCurrentTag != 0) {
                        Toast.makeText(EditorActivity.this, String.format("Placer %d a x: %s y: %s", mCurrentTag, motionEvent.getX(), motionEvent.getY()), Toast.LENGTH_LONG).show();
                        mIdSelected = mEditorView.addElement(mCurrentTag, motionEvent.getX(), motionEvent.getY());
                        mCurrentTag = 0;
                    } else if (mIdSelected == -1) {
                        mXTouch = motionEvent.getX();
                        mYTouch = motionEvent.getY();
                    } else {
                        mIdSelected = -1;
                        findViewById(R.id.editeur_frame_button).setVisibility(View.INVISIBLE);
                    }
                    break;
                case MotionEvent.ACTION_MOVE:
                    if (mIdSelected != -1) {
                        mEditorView.moveElementById(mIdSelected, motionEvent.getX(), motionEvent.getY());
                    }
                    break;
            }
            return false;
        } else {
            if ((action == MotionEvent.ACTION_UP || action == MotionEvent.ACTION_CANCEL)
                    && mButtonRepeatLock) {
                mButtonRepeatLock = false;
            }
        }
        return false;
    }

    /**
     * Prompt the user for a file name where his level will be stored
     *
     * @param name The default file name
     */
    public void showSaveDialog(String name) {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);

        LayoutInflater inflater = getLayoutInflater();

        View textEntrySaveName = inflater.inflate(R.layout.activity_editeur_save, null);
        EditText textView = ((EditText) textEntrySaveName.findViewById(R.id.editeur_filename));
        textView.setText(name);

        builder.setView(textEntrySaveName)
                .setPositiveButton(android.R.string.ok, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int id) {
                        String LevelDir = ((EditText) ((AlertDialog) dialog).findViewById(R.id.editeur_filename)).getText().toString();
                        mLevel.setPath(getApplicationContext(), LevelDir);
                    }
                })
                .setNegativeButton(android.R.string.cancel, null)
                .setMessage(R.string.editeur_save_name_file);

        Dialog dialog = builder.create();
        dialog.show();
    }

    /**
     * Save the currently displayed level to a file
     */
    public void save() {
        String toastMsg = null;
        if (FileUtils.isExternalStorageWritable()) {
            String Path = mLevel.getPath();
            FileUtils.makeDir(Path);

            try {
                // First line explain the syntax
                String temp = getString(R.string.editeur_file_first_line);
                // Write display metrics
                temp += String.format(getString(R.string.editeur_file_screen_info), mMetrics.widthPixels, mMetrics.heightPixels);
                // Write lvl
                temp += mLevel.toString();
                InputStream in = new ByteArrayInputStream(temp.getBytes());
                FileUtils.writeFile(in, mLevel.getPath(), "level.txt");
                toastMsg = getString(R.string.editeur_save_succes_file, "");
            } catch (IOException e) {
                toastMsg = String.format(getString(R.string.editeur_save_error_file), "", e.toString());
            }
        } else {
            toastMsg = String.format(getString(R.string.editeur_save_error_file), "", getString(R.string.editeur_save_error_sdcard_file));
        }
        Toast.makeText(this, toastMsg, Toast.LENGTH_LONG).show();
    }

    @Override
    public void update(Observable observable, Object data) {
        if (observable instanceof Level) {
            // Hide remove button if Level is empty
            findViewById(R.id.editeur_remove_button).setVisibility(mLevel.getAllSprites().isEmpty() ? View.INVISIBLE : View.VISIBLE);
            for (Button btn : mLevelButtons) {
                btn.setVisibility(mLevel.getAllSprites().isEmpty() ? View.INVISIBLE : View.VISIBLE);
            }
        }
    }

    /**
     * This inner class is responsible for triggering the right action when a button is held down
     */
    private class RepeatingUpdater implements Runnable {
        private int mAction;

        public RepeatingUpdater(int action) {
            mAction = action;
        }

        public void run() {
            if (mButtonRepeatLock) {
                performAction(mAction);
                repeatUpdateHandler.postDelayed(new RepeatingUpdater(mAction), 10);
            }
        }
    }
}