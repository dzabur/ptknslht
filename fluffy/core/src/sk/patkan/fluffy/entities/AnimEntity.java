package sk.patkan.fluffy.entities;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;

import sk.patkan.fluffy.Utils;

/**
 * Created by juraj on 6. 9. 2016.
 */

public class AnimEntity extends Entity {

    private final Animation anim;
    private float stateTime;

    public AnimEntity(Texture img, int rows, int cols) {
        anim = Utils.loadAnimation(img, rows, cols, 0.05f);
        stateTime = 0f;
    }

    @Override
    public void update(float delta) {
        super.update(delta);
        stateTime += delta;
    }

    @Override
    public void render(SpriteBatch batch) {
        //stateTime += Gdx.graphics.getDeltaTime();           // #15
        TextureRegion currentFrame = anim.getKeyFrame(stateTime, true);
       //  batch.end();
       // batch.setBlendFunction(GL20.GL_BLEND_SRC_ALPHA, GL20.GL_ONE_MINUS_DST_COLOR);

      //  batch.begin();
      //  batch.setBlendFunction(GL20.GL_ONE_MINUS_SRC_ALPHA, GL20.GL_ZERO);
        batch.enableBlending();
        //batch.setBlendFunction(GL20.GL_ONE_MINUS_DST_COLOR, GL20.GL_ONE_MINUS_SRC_COLOR);
        //batch.setColor(Color.FIREBRICK);
        batch.draw(currentFrame, pos.x, pos.y);
        batch.flush();
        batch.setBlendFunction(GL20.GL_SRC_ALPHA, GL20.GL_ONE_MINUS_SRC_ALPHA);
      //  batch.flush();
      //  batch.end();
     //   batch.begin();
    }

    @Override
    public void render(ShapeRenderer shapeRenderer) {

    }

    @Override
    public void interaction(Entity e2) {

    }
}
