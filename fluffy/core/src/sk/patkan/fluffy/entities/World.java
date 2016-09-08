package sk.patkan.fluffy.entities;

import com.badlogic.gdx.Input;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.math.Vector3;

import java.util.ArrayList;
import java.util.List;

import sk.patkan.fluffy.screens.GameScreen;

/**
 * Created by juraj on 6. 9. 2016.
 */

public class World {
    private List<Entity> entities = new ArrayList<>();

    Texture jesusImg = new Texture("JesusPixelArt.png");
    Texture dudeImg = new Texture("DudeFull.png");
    BitmapFont font = new BitmapFont();
    Vector2 gravity = new Vector2(0,-5);
    float momentum;
    float energy;

    public void add(Entity e) {
        entities.add(e);
        e.world = this;
    }

    public void del(Entity e) {
        entities.remove(e);
    }

    public void init() {
        add(new JesusEntity(jesusImg));
        add(new JesusEntity(jesusImg));
        add(new JesusEntity(jesusImg));
        JesusEntity jesus = new JesusEntity(jesusImg);
        add(jesus);
        jesus.addControler(new JesusEntity.PlayerInput());
        add(new AnimEntity(dudeImg, 11, 11));
    }

    public void render(SpriteBatch batch) {
        for (Entity e : entities)
            e.render(batch);
        font.draw(batch, "momentum " + momentum, 70, 20);
        font.draw(batch, "energy   " + energy, 70, 40);
    }

    public void render(ShapeRenderer shapeRenderer) {
        for (Entity e : entities)
            e.render(shapeRenderer);
    }

    public void update(float delta) {
        energy=0f;
        momentum=0f;

        for (Entity e : entities) {
            e.update(delta);
            momentum+=e.getMomentum();
            energy+=e.getEnergy();
        }
        updateAllInteractions();
    }

    private void updateAllInteractions() {
        for (int e1pos = 0; e1pos < entities.size(); e1pos++) {
            for (int e2pos = e1pos + 1; e2pos < entities.size(); e2pos++) {
                updateInteraction(entities.get(e1pos),entities.get(e2pos));
            }
        }
    }

    public void updateInteraction(Entity e1, Entity e2) {
        e1.interaction(e2);
    }


    public void dispose() {
        jesusImg.dispose();
    }


    public void onTouchDown(Vector3 where, int button) {
        if (button == Input.Buttons.LEFT)
            for (Entity e : entities) {
                e.addControler(null);
                if (e.getRect().contains(where.x, where.y)) {
                    e.onTouchDown(new Vector2(where.x, where.y));
                    e.addControler(new JesusEntity.PlayerInput());
                }
            }
        if (button == Input.Buttons.RIGHT) {
            add(new JesusEntity(jesusImg, where));
        }
    }

    public void onTouchDragged(Vector3 camSpace) {
        for (Entity e : entities) {
            if (e.inputControler!=null) {
                e.pos.x = camSpace.x - e.touchedAt.x;
                e.pos.y = camSpace.y - e.touchedAt.y;
                return;
            }
        }
    }

    public void onTouchUp(Vector2 where, int button) {
        for (Entity e : entities) {
            if (e.getRect().contains(where)) {
                e.onTouchUp(where, button);
            }
        }
    }

    public void addJesus(Vector2 unproject) {
        add(new JesusEntity(jesusImg, unproject));
    }
}
