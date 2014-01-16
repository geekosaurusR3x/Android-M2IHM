package com.skad.android.androidm2ihm.model;

import com.skad.android.androidm2ihm.utils.MathUtils;

/**
 * Created by skad on 27/12/13.
 */
public class Wall extends SpriteObject {
    public Wall(int x, int y, int width, int height) {
        super(x, y, width, height);
        this.mType = "w";
    }

    public Vector2D getRebondVector(Vector2D colisionPoint, Vector2D direction) {
        Vector2D wallVectorNormalised = MathUtils.vectorFromPoint(getXPos(), getYPos(), getXPos() + getWidth(), getYPos() + getHeight());
        return calculRebondVector(colisionPoint, direction, wallVectorNormalised);
    }

    protected Vector2D calculRebondVector(Vector2D colisionPoint, Vector2D direction, Vector2D i) {
        Vector2D j = i.getNormalVector();
        Vector2D v = new Vector2D(0, 0);
        v.setX(direction.scalaire(i));
        v.setY(direction.scalaire(j));
        return v.getNormalVector();
    }
}
