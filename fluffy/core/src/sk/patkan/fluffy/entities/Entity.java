package sk.patkan.fluffy.entities;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.math.Vector3;

/**
 * Created by juraj on 6. 9. 2016.
 */

public abstract class Entity {

    long id;
    float mass = 1f;
    float elasticity = 1f;
    Vector2 pos = new Vector2();
    Vector2 vel = new Vector2();
    Vector2 acc = new Vector2();
    Vector2 touchedAt;

    InputControler inputControler;
    public World world;

    public abstract void render(SpriteBatch batch);
    public abstract void render(ShapeRenderer shapeRenderer);

    public void update(float delta) {
        if (inputControler !=null)
            inputControler.updateInput(delta, this);
        pos.add(vel);
        vel.add(acc);
        //gravity
        Vector2 tmp = new Vector2(world.gravity);
        vel.add(tmp.scl(delta));
        //floor
        if (pos.y<0 && vel.y<0) {
            vel.y = -vel.y * 0.4f;
            vel.x *= 0.8;
            pos.y=0;
        }
        //energy dampening
        //vel.x = (vel.x*0.9f);
        vel.scl(0.990f);
    };


    public void addControler(InputControler inputControler) {
        this.inputControler = inputControler;
    }

    public static void inputToVec2(Vector2 vec, float delta) {
        if (Gdx.input.isKeyPressed(Input.Keys.UP)) {
            vec.y+=delta;
        }

        if (Gdx.input.isKeyPressed(Input.Keys.DOWN)) {
            vec.y-=delta;
        }

        if (Gdx.input.isKeyPressed(Input.Keys.LEFT)) {
            vec.x-=delta;
        }

        if (Gdx.input.isKeyPressed(Input.Keys.RIGHT)) {
            vec.x+=delta;
        }
    }

    public abstract void interaction(Entity e2);

    public Rectangle getRect() {
        return new Rectangle(pos.x, pos.y, 0, 0);
    }

    public boolean isOverlaping(Entity e2) {
        return getRect().overlaps(e2.getRect());
    }

    public void onTouchDown(Vector2 where) {
        touchedAt = where.sub(pos);
    }

    public void onTouchDragged(Vector2 where) {
        if (inputControler!=null) {
            pos.x = where.x - touchedAt.x;
            pos.y = where.y - touchedAt.y;
            vel.x = 0;
            vel.y = 0;
        }
    }

    public void onTouchUp(Vector2 where, int button) {
        if (button == Input.Buttons.LEFT) {
            if (touchedAt!=null) {
                where.sub(touchedAt);
                vel.add(where.sub(pos));
            }
        }
    }

    public float getMomentum() {
        return vel.len()*mass;
    }

    public float getEnergy() {
        return 0.5f*vel.len2()*mass;
    }
}
