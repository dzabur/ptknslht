package sk.patkan.fluffy;

import com.badlogic.gdx.math.Rectangle;

/**
 * Created by juraj on 6. 9. 2016.
 */

public class MyRect extends Rectangle {

    public MyRect(Rectangle rect) {
        super(rect);
    }

    public float minX() {
        return x;
    }

    public float maxX() {
        return x + getWidth();
    }

    public float maxY() {
        return y + getHeight();
    }

    public float minY() {
        return y;
    }

    public boolean isBetweenX(float test) {
        return minX() < test && test < maxX();
    }

    public boolean isBetweenY(float test) {
        return minY() < test && test < maxY();
    }
}
