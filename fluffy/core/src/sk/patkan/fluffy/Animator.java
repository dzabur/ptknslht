package sk.patkan.fluffy;

import com.badlogic.gdx.ApplicationListener;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;

/**
 * Created by juraj on 6. 9. 2016.
 */

public class Animator implements ApplicationListener {

    private static final int        FRAME_COLS = 6;         // #1
    private static final int        FRAME_ROWS = 5;         // #2

    Animation walkAnimation;          // #3
    Texture walkSheet;              // #4
    SpriteBatch spriteBatch;            // #6
    TextureRegion currentFrame;           // #7

    float stateTime;                                        // #8

    @Override
    public void create() {
        walkSheet = new Texture(Gdx.files.internal("sprite-animation4.png")); // #9
        walkAnimation = Utils.loadAnimation(walkSheet, FRAME_COLS, FRAME_ROWS, 0.025f);
        spriteBatch = new SpriteBatch();
        stateTime = 0f;
    }

    @Override
    public void resize(int width, int height) {

    }

    @Override
    public void render() {
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT | GL20.GL_DEPTH_BUFFER_BIT);                        // #14
        stateTime += Gdx.graphics.getDeltaTime();           // #15
        currentFrame = walkAnimation.getKeyFrame(stateTime, true);  // #16
        spriteBatch.begin();
        spriteBatch.draw(currentFrame, 50, 50);             // #17
        spriteBatch.end();
    }

    @Override
    public void pause() {

    }

    @Override
    public void resume() {

    }

    @Override
    public void dispose() {

    }
}