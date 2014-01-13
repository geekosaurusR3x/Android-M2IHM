package com.skad.android.androidm2ihm.activity;


import android.content.DialogInterface;
import android.os.Bundle;
import android.os.Handler;
import android.os.Vibrator;
import android.support.v7.app.ActionBar;
import android.support.v7.app.ActionBarActivity;
import android.support.v7.widget.PopupMenu;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;
import com.skad.android.androidm2ihm.R;
import com.skad.android.androidm2ihm.view.EditeurView;

/**
 * Created by skad on 09/01/14.
 */
public class EditeurActivity extends ActionBarActivity implements View.OnTouchListener, View.OnLongClickListener, View.OnClickListener, DialogInterface.OnClickListener, DialogInterface.OnCancelListener {
    private static final String TAG = "EditeurActivity";

    // Views
    private EditeurView mEditeurView;
    private int mCurrentTag;
    private float mXTouch;
    private float mYTouch;
    private int mIdSelected;
    private Handler repeatUpdateHandler = new Handler();
    private boolean mButtonRepeatLock = false;

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

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_editeur);

        mCurrentTag = 0;
        mIdSelected = -1;

        mEditeurView = (EditeurView) findViewById(R.id.editeur_lvl);
        mEditeurView.setOnTouchListener(this);
        mEditeurView.setOnLongClickListener(this);

        findViewById(R.id.editeur_add_button).setOnClickListener(new Button.OnClickListener() {
            @Override
            public void onClick(View view) {
                showPopupMenu(view);
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

        // Hide ActionBar
        ActionBar actionBar = getSupportActionBar();
        if (actionBar != null) {
            actionBar.hide();
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
    }

    @Override
    protected void onPause() {
        super.onPause();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
    }


    @Override
    public void onBackPressed() {
        super.onBackPressed();
        overridePendingTransition(R.anim.push_left_in, R.anim.push_left_out);
    }

    @Override
    public void onCancel(DialogInterface dialogInterface) {

    }

    @Override
    public void onClick(DialogInterface dialogInterface, int i) {

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
        }
    }

    private void showPopupMenu(View v) {
        PopupMenu popupMenu = new PopupMenu(this, v);
        popupMenu.getMenuInflater().inflate(R.menu.editeur_menu, popupMenu.getMenu());

        popupMenu.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {

            @Override
            public boolean onMenuItemClick(MenuItem menuItem) {
                mCurrentTag = menuItem.getItemId();
                Toast.makeText(EditeurActivity.this, String.format(getString(R.string.editeur_item_select), menuItem.getItemId()), Toast.LENGTH_LONG).show();
                return false;
            }
        });

        popupMenu.show();
    }

    @Override
    public boolean onLongClick(View view) {
        if (view.equals(mEditeurView)) {
            mIdSelected = mEditeurView.getIdElement(mXTouch, mYTouch);
            if (mIdSelected != -1) {
                Toast.makeText(EditeurActivity.this, "Item Selected " + mIdSelected, Toast.LENGTH_LONG).show();
                Vibrator v = (Vibrator) getSystemService(this.VIBRATOR_SERVICE);
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
        if (view.equals(mEditeurView)) {
            if (motionEvent.getAction() == MotionEvent.ACTION_DOWN) {
                if (mCurrentTag != 0) {
                    Toast.makeText(EditeurActivity.this, String.format("Placer %d a x: %s y: %s", mCurrentTag, motionEvent.getX(), motionEvent.getY()), Toast.LENGTH_LONG).show();
                    mEditeurView.addElement(mCurrentTag, motionEvent.getX(), motionEvent.getY());
                    mCurrentTag = 0;
                } else if (mIdSelected == -1) {
                    mXTouch = motionEvent.getX();
                    mYTouch = motionEvent.getY();
                } else {
                    mIdSelected = -1;
                    findViewById(R.id.editeur_frame_button).setVisibility(View.INVISIBLE);
                }

            }
            if (motionEvent.getAction() == MotionEvent.ACTION_MOVE) {
                if (mIdSelected != -1) {
                    mEditeurView.moveElementById(mIdSelected, motionEvent.getX(), motionEvent.getY());
                }
            }
            return false;
        } else {
            if ((motionEvent.getAction() == MotionEvent.ACTION_UP || motionEvent.getAction() == MotionEvent.ACTION_CANCEL)
                    && mButtonRepeatLock) {
                mButtonRepeatLock = false;
            }
        }
        return false;
    }
}