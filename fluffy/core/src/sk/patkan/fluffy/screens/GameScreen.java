package sk.patkan.fluffy.screens;

import com.badlogic.gdx.Application;
import com.badlogic.gdx.Game;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.InputAdapter;
import com.badlogic.gdx.InputMultiplexer;
import com.badlogic.gdx.InputProcessor;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.input.GestureDetector;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.math.Vector3;

import sk.patkan.fluffy.entities.JesusEntity;
import sk.patkan.fluffy.entities.World;

import static com.badlogic.gdx.graphics.GL20.GL_ONE_MINUS_SRC_ALPHA;

/**
 * Created by juraj on 6. 9. 2016.
 */

public class GameScreen extends BaseScreen {

    private static final String TAG = GameScreen.class.getSimpleName();
    SpriteBatch batch;
    World world = new World();
    private OrthographicCamera camera;
    BitmapFont font = new BitmapFont();
    public static String debugText;
    ShapeRenderer shapeRenderer;
    private static GameScreen instance;

    public GameScreen(Game game) {
        super(game);
    }

    public static GameScreen getInstance() {
        return instance;
    }

    public Vector2 unproject(float screenX, float screenY) {
        Vector3 camSpace = new Vector3(screenX, screenY, 0);
        Vector3 wrlSpace = camera.unproject(camSpace);
        return new Vector2(wrlSpace.x, wrlSpace.y);
    }

    private void setupInput() {
        InputProcessor ia = new InputAdapter() {
            Vector3 lastPos = new Vector3();
            @Override
            public boolean keyDown(int keycode) {

                if ((keycode == Input.Keys.ESCAPE) || (keycode == Input.Keys.BACK) )
                    // Maybe perform other operations before exiting
                    Gdx.app.exit();
                return false;
            }

            @Override
            public boolean touchUp(int screenX, int screenY, int pointer, int button) {
                world.onTouchUp(unproject(screenX, screenY), button);
                return super.touchUp(screenX, screenY, pointer, button);
            }

            @Override
            public boolean touchDown(int screenX, int screenY, int pointer, int button) {
                Vector3 worldCoordinates = new Vector3(screenX, screenY, 0);
                Vector3 camSpace = camera.unproject(worldCoordinates);
                world.onTouchDown(camSpace, button);
                System.out.println("touch " + camSpace.x + " " + camSpace.y);
                lastPos = camSpace;
                return false;// true;
            }
        };
        GestureDetector gd = new GestureDetector(new GestureDetector.GestureAdapter() {
            @Override
            public boolean tap(float x, float y, int count, int button) {
                if (count>1)
                    if (button == Input.Buttons.LEFT)
                        world.addJesus(unproject(x, y));
                return true;
            }
        });
        gd.setTapSquareSize(100);
        InputMultiplexer inputMultiplexer = new InputMultiplexer();
        inputMultiplexer.addProcessor(ia);
        inputMultiplexer.addProcessor(gd);
        Gdx.input.setInputProcessor(inputMultiplexer);
    }

    @Override
    public void show () {
        Gdx.app.debug(TAG, "show");
        instance = this;
        // create the camera and the SpriteBatch
        camera = new OrthographicCamera();
        camera.setToOrtho(false, 1024, 640);
        batch = new SpriteBatch();
        shapeRenderer = new ShapeRenderer();
        world.init();
        setupInput();
    }

    @Override
    public void render(float delta) {
        world.update(delta);
        Gdx.gl.glClearColor(0.1f, 0.1f, 0.2f, 1);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);

        camera.update();
        // tell the SpriteBatch to render in the
        // coordinate system specified by the camera.
        batch.setProjectionMatrix(camera.combined);
       // batch.setBlendFunction(GL20.GL_BLEND_SRC_ALPHA, GL20.GL_BLEND_COLOR);
        batch.begin();
        batch.setBlendFunction(GL20.GL_SRC_ALPHA, GL20.GL_ONE_MINUS_SRC_ALPHA);
       // batch.draw(img, 0, 0);
        debugText = Gdx.input.getX() + " " + Gdx.input.getY();
        world.render(batch);
        font.draw(batch, debugText, 10, 20);
        batch.end();
        shapeRenderer.setProjectionMatrix(camera.combined);
        shapeRenderer.begin(ShapeRenderer.ShapeType.Line);
        shapeRenderer.setColor(Color.CORAL);
        world.render(shapeRenderer);
        shapeRenderer.end();
    }

    @Override
    public void hide () {
        Gdx.app.debug(TAG, "hide");
        batch.dispose();
    }

    @Override
    public void dispose() {
        Gdx.app.debug(TAG, "dispose");
        super.dispose();
        world.dispose();
    }
}
