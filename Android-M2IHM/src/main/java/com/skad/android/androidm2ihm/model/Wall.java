package com.skad.android.androidm2ihm.model;

import android.util.Log;
import com.skad.android.androidm2ihm.utils.MathUtils;

/**
 * Wall class
 * Extend SpriteObject
 * Created by skad on 27/12/13.
 */
public class Wall extends SpriteObject {
    /**
     * Constructor
     *
     * @param x
     * @param y
     * @param width
     * @param height set the type to "w"
     */
    public Wall(int x, int y, int width, int height) {
        super(x, y, width, height);
        this.mType = "w";
    }

    /**
     * Comput the Rebound vector2D for the wall with a colisionPoint and a Vector2D direction
     * this one compute the vector i from the wall et call calculRebondVector
     * <p/>
     * (very buggy)
     *
     * @param colisionPoint
     * @param direction
     * @return a new Vector2D wich is the rebound vector
     * @see com.skad.android.androidm2ihm.model.Vector2D
     * @see com.skad.android.androidm2ihm.model.Wall#calculRebondVector(Vector2D, Vector2D, Vector2D)
     */
    public Vector2D getReboundVector(Vector2D colisionPoint, Vector2D direction) {
        Vector2D wallVectorNormalised = MathUtils.vectorFromPoint(getXPos(), getYPos(), getXPos() + getWidth(), getYPos());
        Log.d("direction ", direction.toString());
        Log.d("wallVectorNormalised ", wallVectorNormalised.toString());
        Vector2D retour = calculRebondVector(colisionPoint, direction, wallVectorNormalised);
        //retour.mult(new Vector2D(10.0,10.0));
        Log.d("retour ", retour.toString());

        return retour;
    }

    /**
     * Comput the Rebound vector2D from a vector i with a colisionPoint and a Vector2D direction
     * @param colisionPoint
     * @param direction
     * @param i
     * @return
     */
    protected Vector2D calculRebondVector(Vector2D colisionPoint, Vector2D direction, Vector2D i) {
        Vector2D j = i.getNormalVector();
        Vector2D v = new Vector2D(0, 0);
        v.setX(direction.scalaire(i));
        v.setY(direction.scalaire(j));
        return v.getNormalVector();
    }
}
