package com.skad.android.androidm2ihm.activity;


import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.os.Vibrator;
import android.support.v7.app.ActionBar;
import android.support.v7.app.ActionBarActivity;
import android.support.v7.widget.PopupMenu;
import android.util.Log;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.Toast;
import com.skad.android.androidm2ihm.R;
import com.skad.android.androidm2ihm.view.BackgroundView;
import com.skad.android.androidm2ihm.view.EditeurView;
import com.skad.android.androidm2ihm.view.LevelView;

/**
 * Created by skad on 09/01/14.
 */
public class EditeurActivity extends ActionBarActivity implements View.OnTouchListener, View.OnLongClickListener, View.OnClickListener, DialogInterface.OnClickListener, DialogInterface.OnCancelListener {
    private static final String TAG = "EditeurActivity";

    // Views
    private EditeurView mEditeurView;
    private int mTagCourent;
    private float mXTouch;
    private float mYTouch;
    private int mIdSelected;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        mEditeurView = new EditeurView(this);
        setContentView(R.layout.activity_editeur);

        mTagCourent = 0;
        mIdSelected = -1;

        findViewById(R.id.editeur_lvl).setOnTouchListener(this);
        findViewById(R.id.editeur_lvl).setOnLongClickListener(this);

        findViewById(R.id.editeur_add_button).setOnClickListener(new Button.OnClickListener()
        {
            @Override
            public void onClick(View view) {
                showPopupMenu(view);
            }
        });

        findViewById(R.id.editeur_left_button).setOnClickListener(this);
        findViewById(R.id.editeur_right_button).setOnClickListener(this);
        findViewById(R.id.editeur_up_button).setOnClickListener(this);
        findViewById(R.id.editeur_down_button).setOnClickListener(this);
        findViewById(R.id.editeur_widthminus_button).setOnClickListener(this);
        findViewById(R.id.editeur_widthplus_button).setOnClickListener(this);
        findViewById(R.id.editeur_heightminus_button).setOnClickListener(this);
        findViewById(R.id.editeur_heightplus_button).setOnClickListener(this);

        // Hide ActionBar
        ActionBar actionBar = getSupportActionBar();
        if (actionBar != null) {
            actionBar.hide();
        }

        View containerLvl = findViewById(R.id.editeur_lvl);
        ((ViewGroup) containerLvl).addView(mEditeurView);
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
        switch(view.getId())
        {
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

    private void showPopupMenu(View v){
        PopupMenu popupMenu = new PopupMenu(this, v);
        popupMenu.getMenuInflater().inflate(R.menu.editeur_menu, popupMenu.getMenu());

        popupMenu.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {

            @Override
            public boolean onMenuItemClick(MenuItem menuItem) {
                mTagCourent = menuItem.getItemId();
                Toast.makeText(EditeurActivity.this, String.format(getString(R.string.editeur_item_select), menuItem.getItemId()), Toast.LENGTH_LONG).show();
                return false;
            }
        });

        popupMenu.show();
    }

    @Override
    public boolean onLongClick(View view) {
        mIdSelected = mEditeurView.getIdElement(mXTouch,mYTouch);
        if (mIdSelected != -1)
        {
            Toast.makeText(EditeurActivity.this, "Item Selected "+mIdSelected, Toast.LENGTH_LONG).show();
            Vibrator v = (Vibrator) getSystemService(this.VIBRATOR_SERVICE);
            v.vibrate(50);
            findViewById(R.id.editeur_frame_button).setVisibility(View.VISIBLE);
        }
        return false;
    }

    @Override
    public boolean onTouch(View view, MotionEvent motionEvent) {
        if (motionEvent.getAction() == MotionEvent.ACTION_DOWN) {
            if (mTagCourent != 0)
            {
                Toast.makeText(EditeurActivity.this, String.format("Placer %d a x: %s y: %s", mTagCourent, motionEvent.getX(), motionEvent.getY()), Toast.LENGTH_LONG).show();
                mEditeurView.addElement(mTagCourent,motionEvent.getX(),motionEvent.getY());
                mTagCourent = 0;
            }
            else if(mIdSelected == -1)
            {
                mXTouch = motionEvent.getX();
                mYTouch = motionEvent.getY();
            }
            else {
                mIdSelected = -1;
                findViewById(R.id.editeur_frame_button).setVisibility(View.INVISIBLE);
            }

        }
        if (motionEvent.getAction() == MotionEvent.ACTION_MOVE)
        {
            if (mIdSelected != -1)
            {
                mEditeurView.moveElementById(mIdSelected,motionEvent.getX(),motionEvent.getY());
            }
        }
        return false;
    }
}