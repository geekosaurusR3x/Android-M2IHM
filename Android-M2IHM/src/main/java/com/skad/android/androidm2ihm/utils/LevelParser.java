package com.skad.android.androidm2ihm.utils;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.util.Log;
import com.skad.android.androidm2ihm.R;
import com.skad.android.androidm2ihm.model.*;

import java.io.*;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by pschmitt on 1/9/14.
 */
public class LevelParser {
    private LevelParser() {
    }

    public static Level getLevelFromFile(Context context, String levelDir,int levelNum, int screenWidth, int screenHeight) {
        double ratioWidth = 1;
        double ratioHeight = 1;
        // Determine correct level resource file

        String Path = context.getExternalFilesDir(null)+File.separator+levelDir ;
        File file = new File(Path, "level.txt");
        InputStream fileLevelStream = null;

        try {
            fileLevelStream = new FileInputStream(file);
            Log.d("tag", file.toString());
        } catch (FileNotFoundException e) {
        }

        BufferedReader reader = new BufferedReader(new InputStreamReader(fileLevelStream));
        String line;
        Ball ball = null;
        Target end = null;
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
                    float angle = 0; //Default angle
                    if (temp.length > 5)
                            Float.parseFloat(temp[5]);
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
                        ball.setSprite(getBitmapOrStandar(context, Path, "playership_shielded.png"));
                        ball.setSprite(getBitmapOrStandar(context,Path,"playership_shielded_red.png"));
                        ball.setmAngle(angle);
                    } else if (objectType.equals("g")) { // gun
                        Gun gun = new Gun(xPos, yPos, width, height, fireRate);
                        gun.setSprite(getBitmapOrStandar(context,Path,"gun.png"));
                        gun.setBulletSprite(getBitmapOrStandar(context, Path, "bullet.png"));
                        gun.setRatioHeight(ratioHeight);
                        gun.setRatioWidth(ratioWidth);
                        gun.setmAngle(angle);
                        gunList.add(gun);
                    } else if (objectType.equals("e")) {
                        end = new Target(xPos, yPos, width, height);
                        end.setSprite(getBitmapOrStandar(context, Path, "cible.png"));
                        end.setmAngle(angle);
                    } else if (objectType.equals("h")) { // hole
                        Hole hole = new Hole(xPos, yPos, width, height);
                        hole.setSprite(getBitmapOrStandar(context, Path, "hole.png"));
                        hole.setmAngle(angle);
                        holeList.add(hole);
                    } else { // Walls
                        Wall wall = new Wall(xPos, yPos, width, height);
                        String drawableResName = "";
                        if (objectType.equals("w")) { // wall (straight)
                            wall = new Wall(xPos, yPos, width, height);
                            drawableResName = "wall_grey_texture";
                        } else if (objectType.equals("abl")) { // wall (curved - bottom left)
                            wall = new WallArcBottomLeft(xPos, yPos, width, height);
                            drawableResName = "arcwall_bottom_left";
                        } else if (objectType.equals("abr")) { // wall (curved - bottom right)
                            wall = new WallArcBottomRight(xPos, yPos, width, height);
                            drawableResName = "arcwall_bottom_right";
                        } else if (objectType.equals("atl")) { // wall (curved - top left)
                            wall = new WallArcTopLeft(xPos, yPos, width, height);
                            drawableResName = "arcwall_top_left";
                        } else if (objectType.equals("atr")) { // wall (curved - top right)
                            wall = new WallArcTopRight(xPos, yPos, width, height);
                            drawableResName = "arcwall_top_right";
                        }
                        wall.setmAngle(angle);
                        wall.setSprite(getBitmapOrStandar(context, Path, drawableResName + ".png"));
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

    public static Bitmap getBitmapOrStandar(Context context,String path, String fileName)
    {
        String file = path+File.separator+fileName;
        if(!FileUtils.fileExist(file))
        {
            file = context.getExternalFilesDir(null)+File.separator+"default"+File.separator+fileName;
        }

        return BitmapFactory.decodeFile(file);
    }
}
