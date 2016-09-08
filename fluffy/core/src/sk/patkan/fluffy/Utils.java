package sk.patkan.fluffy;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.TextureRegion;

import java.util.Random;

/**
 * Created by juraj on 6. 9. 2016.
 */

public class Utils {

    private static final Random rand;
    static {
        rand = new Random();
    }

    public static int randInt(int min, int max) {
        int randomNum = rand.nextInt((max - min) + 1) + min;

        return randomNum;
    }

    public static Animation loadAnimation(Texture sheet, int cols, int rows, float frameDuration) {
        TextureRegion[][] frames2d = TextureRegion.split(sheet, sheet.getWidth()/cols, sheet.getHeight()/rows);
        TextureRegion[] frames = new TextureRegion[cols * rows];
        int index = 0;
        for (int i = 0; i < rows; i++) {
            for (int j = 0; j < cols; j++) {
                frames[index++] = frames2d[i][j];
            }
        }
        return new Animation(frameDuration, frames);
    }
}
