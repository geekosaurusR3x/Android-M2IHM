package com.skad.android.androidm2ihm.utils;

import android.content.Context;
import android.graphics.BitmapFactory;
import com.skad.android.androidm2ihm.model.*;

import java.io.*;
import java.util.ArrayList;
import java.util.List;

/**
 * Parser Class witch read the level file
 * Created by pschmitt on 1/9/14.
 */
public class LevelParser {
    private LevelParser() {
    }

    /**
     * Read File and update level singleton
     *
     * @param context
     * @param levelDir
     * @param levelNum
     * @param screenWidth
     * @param screenHeight
     * @return Level singleton
     * @see com.skad.android.androidm2ihm.model.Level
     */
    public static Level getLevelFromFile(Context context, String levelDir, int levelNum, int screenWidth, int screenHeight) {
        double ratioWidth = 1;
        double ratioHeight = 1;
        Level level = Level.getInstance();

        // Determine correct level resource file
        level.setPath(context, levelDir);
        String Path = level.getPath();
        File file = new File(FileUtils.getfileordefault(context, Path, "level.txt"));
        InputStream fileLevelStream = null;

        try {
            fileLevelStream = new FileInputStream(file);
        } catch (FileNotFoundException e) {
        }

        BufferedReader reader = new BufferedReader(new InputStreamReader(fileLevelStream));
        String line;
        Ball ball = null;
        Target end = null;
        List<Hole> holeList = new ArrayList<Hole>();
        List<Wall> wallList = new ArrayList<Wall>();
        List<Gun> gunList = new ArrayList<Gun>();

        try {
            while ((line = reader.readLine()) != null) {
                if (!line.substring(0, 1).matches("#")) {
                    String[] temp = line.split("/");
                    String objectType = temp[0];
                    int xPos = Integer.parseInt(temp[1]);
                    int yPos = Integer.parseInt(temp[2]);
                    int width = Integer.parseInt(temp[3]);
                    int height = Integer.parseInt(temp[4]);
                    float angle = 0; //Default angle
                    if (temp.length > 5)
                        angle = Float.parseFloat(temp[5]);
                    int fireRate = 1000; // Default fire rate
                    if (temp.length > 6)
                        fireRate = Integer.parseInt(temp[6]);

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
                        ball.setSprite(BitmapFactory.decodeFile(FileUtils.getfileordefault(context, Path, "playership_shielded.png")));
                        ball.setAlternateSprite(BitmapFactory.decodeFile(FileUtils.getfileordefault(context, Path, "playership_shielded_red.png")));
                        ball.setDir(0, 0);
                        ball.rotate(angle);
                    } else if (objectType.equals("g")) { // gun
                        Gun gun = new Gun(xPos, yPos, width, height, fireRate);
                        gun.setSprite(BitmapFactory.decodeFile(FileUtils.getfileordefault(context, Path, "gun.png")));
                        gun.setBulletSprite(BitmapFactory.decodeFile(FileUtils.getfileordefault(context, Path, "bullet.png")));
                        gun.setRatioHeight(ratioHeight);
                        gun.setRatioWidth(ratioWidth);
                        gun.rotate(angle);
                        gunList.add(gun);
                    } else if (objectType.equals("e")) {
                        end = new Target(xPos, yPos, width, height);
                        end.setSprite(BitmapFactory.decodeFile(FileUtils.getfileordefault(context, Path, "cible.png")));
                        end.rotate(angle);
                    } else if (objectType.equals("h")) { // hole
                        Hole hole = new Hole(xPos, yPos, width, height);
                        hole.setSprite(BitmapFactory.decodeFile(FileUtils.getfileordefault(context, Path, "hole.png")));
                        hole.rotate(angle);
                        holeList.add(hole);
                    } else { // Walls
                        Wall wall = new Wall(xPos, yPos, width, height);
                        String drawableResName = "";
                        if (objectType.equals("w")) { // wall (straight)
                            wall = new Wall(xPos, yPos, width, height);
                            drawableResName = "wall";
                        } else if (objectType.equals("wa")) { // wall (curved)
                            wall = new WallArc(xPos, yPos, width, height);
                            drawableResName = "wall_arc";
                        }
                        wall.setSprite(BitmapFactory.decodeFile(FileUtils.getfileordefault(context, Path, drawableResName + ".png")));
                        wall.rotate(angle);
                        wallList.add(wall);
                    }
                }
            }
            level.setComponents(ball, end, wallList, holeList, gunList);
            level.setLevelNumber(levelNum);
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
