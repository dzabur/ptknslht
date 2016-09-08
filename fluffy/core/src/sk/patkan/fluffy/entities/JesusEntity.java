package sk.patkan.fluffy.entities;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.math.Vector3;

import sk.patkan.fluffy.MyRect;
import sk.patkan.fluffy.Resources;
import sk.patkan.fluffy.Utils;
import sk.patkan.fluffy.screens.GameScreen;

/**
 * Created by juraj on 6. 9. 2016.
 */

public class JesusEntity extends Entity {

    Texture img;
    float friction = 1f;

    public JesusEntity(Texture img) {
        this.img = img;
        pos.x = Utils.randInt(0, Gdx.graphics.getWidth());
        pos.y = Utils.randInt(0, Gdx.graphics.getHeight());
    }

    public JesusEntity(Texture img, Vector3 pos3) {
        this.img = img;
        this.pos.x = pos3.x;
        this.pos.y = pos3.y;
    }

    public JesusEntity(Texture img, Vector2 unproject) {
        this.img = img;
        pos = unproject;
    }

    @Override
    public void update(float delta) {
       super.update(delta);
    }

    @Override
    public void interaction(Entity e2) {
        if (isOverlaping(e2)) {
          //  System.out.println("is overlaping");
            if (e2 instanceof JesusEntity)
                onOverlapWithJesus((JesusEntity) e2);
        }

    }

    public void onOverlapWithJesus(JesusEntity e2) {
     //   Vector2 v = getCenter().sub(e2.getCenter()).scl(0.001f);
      //  vel.add(v);
      //  e2.vel.sub(v);
     //   if (true) return;
        MyRect r1 = new MyRect(getRect());
        MyRect r2 = new MyRect(e2.getRect());

        float depthX = 10000000;
        if (r1.isBetweenX(r2.minX()) && r1.isBetweenX(r2.maxX())) {
            //nic
            //depthX = 0;
        } else if (r1.isBetweenX(r2.minX())) {
            depthX = r1.maxX() - r2.minX();
        } else if (r1.isBetweenX(r2.maxX())) {
            depthX = r1.minX() - r2.maxX();
        }
        float depthY = 10000000;
        if (r1.isBetweenY(r2.minY()) && r1.isBetweenY(r2.maxY())) {
            //nic
            //depthY = 0;
        } else if (r1.isBetweenY(r2.minY())) {
            depthY = r1.maxY() - r2.minY();
        } else if (r1.isBetweenY(r2.maxY())) {
            depthY =  r1.minY() - r2.maxY();
        }

        Vector2 depVec = new Vector2();
        float momentumBefore = getMomentum() + e2.getMomentum();
        float energy0 = getEnergy() + e2.getEnergy();
        float sumMass = mass + e2.mass;

        //cestou nizsieho odporu, kde je mensia depth, tam riesim
        if (Math.abs(depthX)<=Math.abs(depthY)) {
            if (depthX==10000000)
                depthX=1;
            depVec.set(-depthX,0);
            pos.x -= depthX * (e2.mass/sumMass);
            e2.pos.x += depthX * (mass/sumMass);

            vel.x -= depthX * (e2.mass/sumMass) * 2;
            e2.vel.x += depthX * (mass/sumMass) * 2;

            //trenie
            float diff = - e2.vel.y + vel.y;
            float transfered = diff * (friction + e2.friction)/4;
            e2.vel.y += transfered;
            vel.y -= transfered;
        } else {
            if (depthY==10000000)
                depthY=1;
            depVec.set(0,-depthY);
            pos.y -= depthY * (e2.mass/sumMass);
            e2.pos.y += depthY * (mass/sumMass);

            vel.y -= depthY * (e2.mass/sumMass) * 2;
            e2.vel.y += depthY * (mass/sumMass) * 2;

            //trenie
            float diff = - e2.vel.x + vel.x;
            float transfered = diff * (friction + e2.friction)/4;
            e2.vel.x += transfered;
            vel.x -= transfered;
        }

        float energy1 = getEnergy() + e2.getEnergy();
        float momentum = getMomentum() + e2.getMomentum();
        float factor = momentumBefore / momentum;
        float bounciness = (this.elasticity + e2.elasticity) * 0.5f;
        factor*=bounciness;
        vel.scl(factor);
        e2.vel.scl(factor);
        //depVec.set(-depthX/50,-depthY/50);
        //vel.add(depVec.scl(0.5f));
        //e2.vel.sub(depVec);

    }

    private boolean between(float a, float x, float b) {
        return a < x && x < b;
    }

    public Vector2 getCenter() {
        return getRect().getCenter(new Vector2());
    }

    @Override
    public Rectangle getRect() {
        return new Rectangle(pos.x, pos.y, img.getWidth(), img.getHeight());
    }

    @Override
    public void render(SpriteBatch batch) {
        batch.draw(img, pos.x, pos.y);
        Resources.getInstance().font.draw(batch, ""+mass, pos.x, pos.y);
    }

    @Override
    public void render(ShapeRenderer shapeRenderer) {
        if (inputControler!=null) {
            shapeRenderer.setColor(Color.GREEN);
            shapeRenderer.rect(pos.x, pos.y, img.getWidth(), img.getHeight());
        } else {
            shapeRenderer.setColor(Color.DARK_GRAY);
        }

    }

    public static class PlayerInput implements InputControler {

        @Override
        public void updateInput(float delta, Entity e) {

            Entity.inputToVec2(e.vel, delta*10);
            if(Gdx.input.isButtonPressed(Input.Buttons.LEFT)){
                Vector2 worldTouched = GameScreen.getInstance().unproject(Gdx.input.getX(), Gdx.input.getY());
                e.onTouchDragged(worldTouched);
            }

            if(Gdx.input.isKeyPressed(Input.Keys.PLUS))
                e.mass+=0.01;
            if(Gdx.input.isKeyPressed(Input.Keys.MINUS))
                e.mass-=0.01;
            if(Gdx.input.isKeyPressed(Input.Keys.DEL))
                e.world.del(e);
        }
    }

}
