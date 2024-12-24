package io.peter.wrapwrangler.screens;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.World;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.utils.ChangeListener;
import com.badlogic.gdx.utils.ScreenUtils;
import com.badlogic.gdx.utils.viewport.ScreenViewport;

import io.peter.wrapwrangler.ui.UI;

/** First screen of the application. Displayed after the application is created. */
public class FirstScreen implements Screen {

    Texture background;
    Texture actor;
    SpriteBatch spriteBatch;
    ScreenViewport viewport;
    Sprite actorSprite;
    Vector2 touchPos;

//    final float GRAVITY = -9.8f;
//
//    Vector2 gravityDir;

//    World world;
    UI ui;

    @Override
    public void show() {

//        gravityDir = new Vector2(0, GRAVITY);
//
//        world = new World(gravityDir, true);

        ui = new UI();

        viewSetup();

        ui.uiSetup(viewport);

        sceneSetup();
    }

    private void viewSetup() {

        //viewport types https://libgdx.com/wiki/graphics/viewports ScreenViewport extends, no black bars
        viewport = new ScreenViewport();
        viewport.setUnitsPerPixel(0.1f);
    }



    private void sceneSetup() {

        touchPos = new Vector2(); //unused - for detecting user presses NOT on buttons

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
        input(delta);

        logic(delta);

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


    //TODO create input handler
    private void input(float delta){
        float speed = 60f;

        if(ui.getButtonLeft().isPressed()){
            actorSprite.translateX(-speed * delta);
        }
        if(ui.getButtonRight().isPressed()){
            actorSprite.translateX(speed * delta);
        }

        //gravity dealt with in logic method
        if(ui.getButtonJump().addListener(new ChangeListener() {
            @Override
            public void changed(ChangeEvent event, Actor actor) {
                //actorSprite.(speed * delta);
            }
        }));
    }

    private void logic(float delta) {
        //called by render
        //game logic goes here!
        if(actorSprite.getY() > 0){
            actorSprite.translateY(-40 * delta);
        }
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

        //call ui draw method to draw ui buttons over the top
        ui.drawUi();
    }
}
