package io.peter.wrapwrangler;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.Pixmap;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.InputListener;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Button;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;
import com. badlogic. gdx. scenes. scene2d. ui. TextButton. TextButtonStyle;
import com.badlogic.gdx.scenes.scene2d.utils.ChangeListener;
import com.badlogic.gdx.scenes.scene2d.utils.FocusListener;
import com.badlogic.gdx.utils.ScreenUtils;
import com.badlogic.gdx.utils.viewport.FitViewport;
import com.badlogic.gdx.utils.viewport.ScreenViewport;

import jdk.internal.org.jline.utils.Log;

/** First screen of the application. Displayed after the application is created. */
public class FirstScreen implements Screen {

    Texture background;

    Texture actor;

    SpriteBatch spriteBatch;

    ScreenViewport viewport;

    Sprite actorSprite;

    Vector2 touchPos;

    //UI classes
    Button leftBtn;

    Button rightBtn;

    Skin skin;

    Stage stage;

    Table table;

    @Override
    public void show() {

        viewSetup();

        uiSetup();

        sceneSetup();
    }

    private void viewSetup() {

        //viewport types https://libgdx.com/wiki/graphics/viewports ScreenViewport extends, no black bars
        viewport = new ScreenViewport();
        viewport.setUnitsPerPixel(0.1f);
    }

    private void uiSetup() {
        //a stage is an area to draw UI elements, a stage contains a table,
        stage = new Stage(viewport);

        //usually, the table takes up the entire stage, but a stage can contain multiple tables
        //https://libgdx.com/wiki/graphics/2d/scene2d/scene2d#stage
        Gdx.input.setInputProcessor(stage);
        //https://libgdx.com/wiki/graphics/2d/scene2d/table
        table = new Table();

        table.left().bottom();

        table.pad(10f);

        table.setDebug(true);

        //set the table to fill its parent and add it to the stage (parent)
        table.setFillParent(true);
        stage.addActor(table);

        //A skin is a handy way to store all the information to apply to the UI elements
        //a skin can also be loaded via JSON (possibly hyperlap2d?)
        //https://libgdx.com/wiki/graphics/2d/scene2d/skin
        skin = new Skin(Gdx.files.internal("ui/uiskin.json"));

        //create a white colour pixmap
        Pixmap pixmap = new Pixmap(1, 1, Pixmap.Format.RGB565);
        pixmap.setColor(Color.WHITE);
        pixmap.fill();

        //adds a white colour pixmap to the skin
        skin.add("white", new Texture(pixmap));

        //adds a default font to the skin
        skin.add("defualt", new BitmapFont());

        TextButtonStyle buttonStyle = new TextButtonStyle();
        buttonStyle.up = skin.newDrawable("white", Color.SLATE);
        buttonStyle.down = skin.newDrawable("white", Color.RED);
        buttonStyle.checked = skin.newDrawable("white", Color.FOREST);
        buttonStyle.over = skin.newDrawable("white", Color.CORAL);

        skin.add("default", buttonStyle);

        leftBtn = new Button(skin);
        rightBtn = new Button(skin);

        table.add(leftBtn).width(10f).height(10f).pad(2.5f);
        table.add(rightBtn).width(10f).height(10f).pad(2.5f);

        leftBtn.setVisible(true);
        rightBtn.setVisible(true);

        //NOT NECESSARY CURRENTLY!
        leftBtn.addListener(new ChangeListener() {
            @Override
            public void changed(ChangeEvent event, Actor actor) {
                Gdx.app.log("PETER","left button clicked!");

            }
        });

        rightBtn.addListener(new ChangeListener() {
            @Override
            public void changed(ChangeEvent event, Actor actor) {
                Gdx.app.log("PETER","right button clicked!");
            }
        });

    }

    private void sceneSetup() {

        touchPos = new Vector2();
        // Prepare your screen here.

        spriteBatch = new SpriteBatch();

        System.out.println("viewport size = " + Gdx.graphics.getWidth() + ", " + Gdx.graphics.getHeight());

        System.out.println("units per pixel = " + viewport.getUnitsPerPixel());

        //TODO, refactor textures into an AssetManager.
        background = new Texture("com/badlogic/gdx/sprites/Background_2.png");

        actor = new Texture("com/badlogic/gdx/sprites/cowboy_placeholder.png");

        actorSprite = new Sprite(actor);

        actorSprite.setSize(20, 50);
    }

    @Override
    public void render(float delta) {
        // Draw your screen here. "delta" is the time since last render in seconds.
        // Organise this code into input() logic() and draw() methods
        input();

        logic();

        draw();
    }



    @Override
    public void resize(int width, int height) {
        // Resize your screen here. The parameters represent the new window size.
        viewport.update(width, height, true);
    }

    @Override
    public void pause() {
        // Invoked when your application is paused.
    }

    @Override
    public void resume() {
        // Invoked when your application is resumed after pause.
    }

    @Override
    public void hide() {
        // This method is called when another screen replaces this one.
    }

    @Override
    public void dispose() {
        // Destroy screen's assets here.
    }


    private void input(){
        float speed = 60f;
        float delta = Gdx.graphics.getDeltaTime();

        if(leftBtn.isPressed()){
            actorSprite.translateX(-speed * delta);
        }
        if(rightBtn.isPressed()){
            actorSprite.translateX(speed * delta);
        }
    }

    private void logic() {


    }

    private void draw(){
        //clear the screen every frame to prevent artifacts
        ScreenUtils.clear(Color.BLACK);
        viewport.apply();

        //set how the sprites are applied to the screen based on size/projection
        spriteBatch.setProjectionMatrix(viewport.getCamera().combined);

        //sprites should ONLY ever be drawn in between SpriteBatch begin() and end() methods
        spriteBatch.begin();

        //I set the width and height to 8 and 5 respectively in the initialisation of the viewport.
        //This draws the background starting at 0,0, with a width of 8 and a height of 5
        spriteBatch.draw(background, 0,0, (float) Gdx.graphics.getWidth() /10, (float) Gdx.graphics.getHeight() /10);

        actorSprite.draw(spriteBatch);

        spriteBatch.end();

        stage.act(Gdx.graphics.getDeltaTime());
        stage.draw();
    }
}
