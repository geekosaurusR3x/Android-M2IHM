package com.skad.android.androidm2ihm.view;

import android.content.Context;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.util.AttributeSet;
import android.view.View;
import com.skad.android.androidm2ihm.R;
import com.skad.android.androidm2ihm.model.*;
import com.skad.android.androidm2ihm.utils.FileUtils;

/**
 * Created by skad on 19/12/13.
 */
public class EditorView extends View {

    private static final String TAG = "EditorView";

    // Bitmap background;
    private Level mLevel;

    public EditorView(Context context, AttributeSet attributeSet) {
        super(context, attributeSet);
        mLevel = Level.getInstance();
        // Reset level so that if the user played a level, it does not show on the editor
        mLevel.reset();
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);

        for (final SpriteObject mObject : mLevel.getAllSprites()) {
            canvas.drawBitmap(mObject.getScaledSprite(), (int) (mObject.getXPos()), (int) (mObject.getYPos()), null);
        }
        invalidate();
    }

    public int addElement(int tag, float x, float y) {
        SpriteObject spriteObject = null;
        switch (tag) {
            case R.id.editeur_start:
                spriteObject = new Ball((int) x, (int) y, 92, 92);
                spriteObject.setSprite(BitmapFactory.decodeFile(FileUtils.getfileordefault(super.getContext(), mLevel.getPath(), "playership.png")));
                spriteObject.setId(tag);
                mLevel.add(spriteObject);
                break;
            case R.id.editeur_end:
                spriteObject = new Target((int) x, (int) y, 128, 128);
                spriteObject.setSprite(BitmapFactory.decodeFile(FileUtils.getfileordefault(super.getContext(), mLevel.getPath(), "cible.png")));
                spriteObject.setId(tag);
                mLevel.add(spriteObject);
                break;
            case R.id.editeur_hole:
                spriteObject = new Hole((int) x, (int) y, 92, 92);
                spriteObject.setSprite(BitmapFactory.decodeFile(FileUtils.getfileordefault(super.getContext(), mLevel.getPath(), "hole.png")));
                spriteObject.setId(tag);
                mLevel.add(spriteObject);
                break;
            case R.id.editeur_wall:
                spriteObject = new Wall((int) x, (int) y, 64, 32);
                spriteObject.setSprite(BitmapFactory.decodeFile(FileUtils.getfileordefault(super.getContext(), mLevel.getPath(), "wall.png")));
                spriteObject.setId(tag);
                mLevel.add(spriteObject);
                break;
            case R.id.editeur_wall_arc:
                spriteObject = new WallArc((int) x, (int) y, 64, 32);
                spriteObject.setSprite(BitmapFactory.decodeFile(FileUtils.getfileordefault(super.getContext(), mLevel.getPath(), "wall_arc.png")));
                spriteObject.setId(tag);
                mLevel.add(spriteObject);
                break;
        }

        invalidate();
        return mLevel.getId(spriteObject);
    }

    public void removeElement(int id) {
        mLevel.remove(mLevel.get(id));
    }

    public int getElementId(float x, float y) {
        for (final SpriteObject mSpriteObject : mLevel.getAllSprites()) {
            if (mSpriteObject.isInto((int) x, (int) y)) {
                return mLevel.getId(mSpriteObject);
            }
        }
        return -1;
    }

    public void moveLeft(int id) {
        mLevel.get(id).setXPos((int) (mLevel.get(id).getXPos() - 1));
    }

    public void moveRight(int id) {
        mLevel.get(id).setXPos((int) (mLevel.get(id).getXPos() + 1));
    }

    public void moveUp(int id) {
        mLevel.get(id).setYPos((int) (mLevel.get(id).getYPos() - 1));
    }

    public void moveDown(int id) {
        mLevel.get(id).setYPos((int) (mLevel.get(id).getYPos() + 1));
    }

    public void widthPlus(int id) {
        mLevel.get(id).setWidth(mLevel.get(id).getWidth() + 1);
        //mLevel.get(id).resize();
        mLevel.get(id).rotate(mLevel.get(id).getAngle());
    }

    public void widthMinus(int id) {
        SpriteObject object = mLevel.get(id);
        if (object.getWidth() - 1 > 0) {
            object.setWidth(object.getWidth() - 1);
            //object.resize();
            object.rotate(object.getAngle());
        }
    }

    public void heightPlus(int id) {
        mLevel.get(id).setHeight(mLevel.get(id).getHeight() + 1);
        //mLevel.get(id).resize();
        mLevel.get(id).rotate(mLevel.get(id).getAngle());
    }

    public void heightMinus(int id) {
        SpriteObject object = mLevel.get(id);
        if (object.getHeight() - 1 > 0) {
            object.setHeight(object.getHeight() - 1);
            object.resize();
            object.rotate(object.getAngle());
        }
    }

    public void rotatePlus(int id) {
        float angle = mLevel.get(id).getAngle() + 1;
        mLevel.get(id).rotate(angle);
    }

    public void rotateMinus(int id) {
        float angle = mLevel.get(id).getAngle() - 1;
        mLevel.get(id).rotate(angle);
    }

    public void moveElementById(int id, float x, float y) {
        int X = (int) x - (mLevel.get(id).getWidth() / 2);
        int Y = (int) y - (mLevel.get(id).getHeight() / 2);
        mLevel.get(id).setXPos(X);
        mLevel.get(id).setYPos(Y);
    }
}