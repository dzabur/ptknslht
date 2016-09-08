package sk.patkan.fluffy;

import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.math.Rectangle;

/**
 * Created by juraj on 7. 9. 2016.
 */

public class Resources {

    private static Resources instance;

    public static Resources getInstance() {
        if (instance==null)
            instance = new Resources();
        return instance;
    }

    public BitmapFont font = new BitmapFont();
}
