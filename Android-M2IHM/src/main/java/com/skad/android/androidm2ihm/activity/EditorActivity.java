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

import java.io.*;

/**
 * Created by skad on 09/01/14.
 */
public class EditorActivity extends ActionBarActivity implements View.OnTouchListener, View.OnLongClickListener, View.OnClickListener {
    private static final String TAG = "EditorActivity";
    // Views
    private EditorView mEditeurView;
    private int mCurrentTag;
    private float mXTouch;
    private float mYTouch;
    private int mIdSelected;
    private Handler repeatUpdateHandler = new Handler();
    private boolean mButtonRepeatLock = false;
    private String mLevelDir = null;
    private Level mLevel;
    DisplayMetrics mMetrics;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_editeur);

        mCurrentTag = 0;
        mIdSelected = -1;

        mLevelDir = getIntent().getStringExtra(getString(R.string.extra_key_level_dir));

        mMetrics = getResources().getDisplayMetrics();

        mEditeurView = (EditorView) findViewById(R.id.editeur_lvl);
        mEditeurView.setmLevelDir(mLevelDir);
        mEditeurView.setOnTouchListener(this);
        mEditeurView.setOnLongClickListener(this);

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
                mEditeurView.removeElement(mIdSelected);
            }
        });

        Button leftButton = (Button) findViewById(R.id.editeur_left_button);
        leftButton.setOnClickListener(this);
        leftButton.setOnLongClickListener(this);
        leftButton.setOnTouchListener(this);

        Button rightButton = (Button) findViewById(R.id.editeur_right_button);
        rightButton.setOnClickListener(this);
        rightButton.setOnLongClickListener(this);
        rightButton.setOnTouchListener(this);

        Button upButton = (Button) findViewById(R.id.editeur_up_button);
        upButton.setOnClickListener(this);
        upButton.setOnLongClickListener(this);
        upButton.setOnTouchListener(this);

        Button downButton = (Button) findViewById(R.id.editeur_down_button);
        downButton.setOnClickListener(this);
        downButton.setOnLongClickListener(this);
        downButton.setOnTouchListener(this);

        Button widthMinusButton = (Button) findViewById(R.id.editeur_widthminus_button);
        widthMinusButton.setOnClickListener(this);
        widthMinusButton.setOnLongClickListener(this);
        widthMinusButton.setOnTouchListener(this);

        Button widthPlusButton = (Button) findViewById(R.id.editeur_widthplus_button);
        widthPlusButton.setOnClickListener(this);
        widthPlusButton.setOnLongClickListener(this);
        widthPlusButton.setOnTouchListener(this);

        Button heightMinusButton = (Button) findViewById(R.id.editeur_heightminus_button);
        heightMinusButton.setOnClickListener(this);
        heightMinusButton.setOnLongClickListener(this);
        heightMinusButton.setOnTouchListener(this);

        Button heightPlusButton = (Button) findViewById(R.id.editeur_heightplus_button);
        heightPlusButton.setOnClickListener(this);
        heightPlusButton.setOnLongClickListener(this);
        heightPlusButton.setOnTouchListener(this);

        Button rotateButton = (Button) findViewById(R.id.editeur_rotate);
        rotateButton.setOnClickListener(this);
        rotateButton.setOnLongClickListener(this);
        rotateButton.setOnTouchListener(this);

        // Hide ActionBar
        ActionBar actionBar = getSupportActionBar();
        if (actionBar != null) {
            actionBar.hide();
        }
        mLevel = LevelParser.getLevelFromFile(this, mLevelDir, 0, mMetrics.widthPixels, mMetrics.heightPixels);
        showSaveDialog(mLevelDir);
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

    private void performAction(int action) {
        switch (action) {
            case R.id.editeur_left_button:
                mEditeurView.moveLeft(mIdSelected);
                break;
            case R.id.editeur_right_button:
                mEditeurView.moveRight(mIdSelected);
                break;
            case R.id.editeur_up_button:
                mEditeurView.moveUp(mIdSelected);
                break;
            case R.id.editeur_down_button:
                mEditeurView.moveDown(mIdSelected);
                break;
            case R.id.editeur_widthminus_button:
                mEditeurView.widthMinus(mIdSelected);
                break;
            case R.id.editeur_widthplus_button:
                mEditeurView.widthPlus(mIdSelected);
                break;
            case R.id.editeur_heightminus_button:
                mEditeurView.heightMinus(mIdSelected);
                break;
            case R.id.editeur_heightplus_button:
                mEditeurView.heightPlus(mIdSelected);
                break;
            case R.id.editeur_rotate:
                mEditeurView.rotate(mIdSelected);
                break;
        }
    }

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
        if (view.equals(mEditeurView)) {
            mIdSelected = mEditeurView.getElementId(mXTouch, mYTouch);
            if (mIdSelected != -1) {
                Toast.makeText(EditorActivity.this, "Item Selected " + mIdSelected, Toast.LENGTH_LONG).show();
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
        if (view.equals(mEditeurView)) {
            switch (action) {
                case MotionEvent.ACTION_DOWN:
                    if (mCurrentTag != 0) {
                        Toast.makeText(EditorActivity.this, String.format("Placer %d a x: %s y: %s", mCurrentTag, motionEvent.getX(), motionEvent.getY()), Toast.LENGTH_LONG).show();
                        mEditeurView.addElement(mCurrentTag, motionEvent.getX(), motionEvent.getY());
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
                        mEditeurView.moveElementById(mIdSelected, motionEvent.getX(), motionEvent.getY());
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
                        mLevelDir = ((EditText) ((AlertDialog) dialog).findViewById(R.id.editeur_filename)).getText().toString();
                    }
                })
                .setNegativeButton(android.R.string.cancel, null)
                .setMessage(R.string.editeur_save_name_file);

        Dialog dialog = builder.create();
        dialog.show();
    }

    public void save() {
        String toastMsg = null;
        if (FileUtils.isExternalStorageWritable()) {
            String Path = getExternalFilesDir(null)+File.separator+mLevelDir;
            FileUtils.makeDir(Path);

            try {
                //first line explain the syntax
                String temp =  getString(R.string.editeur_file_first_line);
                //write displaymetric
                temp += String.format(getString(R.string.editeur_file_screen_info), mMetrics.widthPixels, mMetrics.heightPixels);
                //write lvl
                temp += mLevel.toString();
                InputStream in = new ByteArrayInputStream(temp.getBytes());
                FileUtils.writeFile(in,Path,"level.txt");
                toastMsg = getString(R.string.editeur_save_succes_file, mLevelDir);
            } catch (IOException e) {
                toastMsg = String.format(getString(R.string.editeur_save_error_file), mLevelDir, e.toString());
            }
        } else {
            toastMsg = String.format(getString(R.string.editeur_save_error_file), mLevelDir, getString(R.string.editeur_save_error_sdcard_file));
        }
        Toast.makeText(this, toastMsg, Toast.LENGTH_LONG).show();
    }

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