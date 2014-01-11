package com.skad.android.androidm2ihm.utils;

import android.content.Context;
import android.graphics.BitmapFactory;
import com.skad.android.androidm2ihm.R;
import com.skad.android.androidm2ihm.model.*;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by pschmitt on 1/9/14.
 */
public class LevelParser {
    private LevelParser() {
    }

    public static Level getLevelFromFile(Context context, int levelNumber, int screenWidth, int screenHeight) {
        double ratioWidth = 1;
        double ratioHeight = 1;
        // Determine correct level resource file
        int levelResId = R.raw.lvl1;
        switch (levelNumber) {
            case 1:
                levelResId = R.raw.lvl1;
                break;
            case 2:
                levelResId = R.raw.lvl2;
                break;
            case 3:
                levelResId = R.raw.lvl3;
                break;
        }
        InputStream fileLevelStream = context.getResources().openRawResource(levelResId);
        BufferedReader reader = new BufferedReader(new InputStreamReader(fileLevelStream));
        String line;
        Ball ball = null;
        Hole end = null;
        List<Hole> holeList = new ArrayList<Hole>();
        List<Wall> wallList = new ArrayList<Wall>();
        List<Gun> gunList = new ArrayList<Gun>();
        Level level = Level.getInstance();
        try {
            while ((line = reader.readLine()) != null) {
                if (!line.substring(0, 1).matches("#")) {
                    String[] temp = line.split("/");
                    String objectType = temp[0];
                    int xPos = Integer.parseInt(temp[1]);
                    int yPos = Integer.parseInt(temp[2]);
                    int width = Integer.parseInt(temp[3]);
                    int height = Integer.parseInt(temp[4]);

                    // Ajusting for the screen size
                    xPos = (int) (xPos * ratioHeight);
                    yPos = (int) (yPos * ratioWidth);
                    width = (int) (width * ratioHeight);
                    height = (int) (height * ratioWidth);

                    if (objectType.equals("screen")) { // screensize and ratio
                        ratioWidth = (double) screenWidth / width;
                        ratioHeight = (double) screenHeight / height;
                    } else if (objectType.equals("p")) { // player (ball)
                        ball = new Ball(xPos, yPos, width, height);
                        ball.setSprite(BitmapFactory.decodeResource(context.getResources(), R.drawable.ball));
                    } else if (objectType.equals("g")) { // gun
                        Gun gun = new Gun(xPos, yPos, width, height);
                        gun.setSprite(BitmapFactory.decodeResource(context.getResources(), R.drawable.gun));
                        gun.setRatioHeight(ratioHeight);
                        gun.setRatioWidth(ratioWidth);
                        gunList.add(gun);
                    } else if (objectType.equals("e")) {
                        end = new Hole(xPos, yPos, width, height);
                        end.setSprite(BitmapFactory.decodeResource(context.getResources(), R.drawable.cible));
                    } else if (objectType.equals("h")) { // hole
                        Hole hole = new Hole(xPos, yPos, width, height);
                        hole.setSprite(BitmapFactory.decodeResource(context.getResources(), R.drawable.hole));
                        holeList.add(hole);
                    } else { // Walls
                        Wall wall = new Wall(xPos, yPos, width, height);
                        if (objectType.equals("w")) { // wall (straight)
                            wall.setSprite(BitmapFactory.decodeResource(context.getResources(), R.drawable.wall_grey_texture));
                        } else if (objectType.equals("abl")) { // wall (curved - bottom left)
                            wall.setSprite(BitmapFactory.decodeResource(context.getResources(), R.drawable.arcwall_bottom_left));
                        } else if (objectType.equals("abr")) { // wall (curved - bottom right)
                            wall.setSprite(BitmapFactory.decodeResource(context.getResources(), R.drawable.arcwall_bottom_right));
                        } else if (objectType.equals("atl")) { // wall (curved - top left)
                            wall.setSprite(BitmapFactory.decodeResource(context.getResources(), R.drawable.arcwall_top_left));
                        } else if (objectType.equals("atr")) { // wall (curved - top right)
                            wall.setSprite(BitmapFactory.decodeResource(context.getResources(), R.drawable.arcwall_top_right));
                        }
                        wallList.add(wall);
                    }
                }
            }
            level.setComponents(ball, end, wallList, holeList, gunList);
            level.setLevelNumber(levelNumber);
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            try {
                fileLevelStream.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        return level;
    }

}
