package io.peter.wrapwrangler.screens;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.Camera;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.Body;
import com.badlogic.gdx.physics.box2d.BodyDef;
import com.badlogic.gdx.physics.box2d.Box2DDebugRenderer;
import com.badlogic.gdx.physics.box2d.Contact;
import com.badlogic.gdx.physics.box2d.ContactImpulse;
import com.badlogic.gdx.physics.box2d.ContactListener;
import com.badlogic.gdx.physics.box2d.Fixture;
import com.badlogic.gdx.physics.box2d.FixtureDef;
import com.badlogic.gdx.physics.box2d.Manifold;
import com.badlogic.gdx.physics.box2d.PolygonShape;
import com.badlogic.gdx.physics.box2d.World;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.utils.ChangeListener;
import com.badlogic.gdx.utils.ScreenUtils;
import com.badlogic.gdx.utils.viewport.ScreenViewport;

import io.peter.wrapwrangler.assets.AssetManager;
import io.peter.wrapwrangler.assets.actors.Player;
import io.peter.wrapwrangler.physics.Physics;
import io.peter.wrapwrangler.ui.UI;

/** First screen of the application. Displayed after the application is created. */
public class FirstScreen implements Screen {

    ScreenViewport viewport;
    Texture backgroundTex;
    Texture actorTex;
    Texture floorTex;
    SpriteBatch spriteBatch;
    //Sprite actorSprite;
    Sprite floorSprite;
    Player player;
    UI ui;
    Physics physics;

    @Override
    public void show() {
        ui = new UI();
        physics = new Physics();

        viewSetup(); //set up the viewport
        ui.uiSetup(viewport); //set up UI
        sceneSetup(); //Place assets in the scene

        physics.physicsSetup(this); //set up Box2D physics

        player = new Player(1, 5, physics.getWorld());
    }

    private void viewSetup() {
        viewport = new ScreenViewport();
        viewport.setUnitsPerPixel(0.1f);
    }

    private void sceneSetup() {
        spriteBatch = new SpriteBatch();

        //TODO, refactor textures into an AssetManager.
        backgroundTex = AssetManager.backgroundTex;
        actorTex = AssetManager.actorTex;
        floorTex = AssetManager.floorTex;

        floorSprite = new Sprite(floorTex);
        floorSprite.setPosition(0, -20);
        floorSprite.setSize(400, 40);
    }

    @Override
    public void render(float delta) {
        //Called every frame
        input(delta);
        logic(delta);
        draw();
        //It is recommended to draw graphics before physics processing
        physics.physicsProcess(delta);

    }


    //TODO create input handler
    private void input(float delta){
        Body actorBody = player.getBody();
        float jumpForce = player.getJumpForce();

        if(ui.getButtonLeft().isPressed()){
            player.getBody().applyForce(-10f*player.getBody().getMass(), 0, player.getBody().getPosition().x,0, true);
        }
        else if(ui.getButtonRight().isPressed()){
            actorBody.applyForce(10f*actorBody.getMass(), 0, actorBody.getPosition().x,0, true);
        }
        //gravity dealt with in logic method
        if(ui.getButtonJump().addListener(new ChangeListener() {
            @Override
            public void changed(ChangeEvent event, Actor actor) {
                if(!player.isJumping) {
                    actorBody.applyForce(0, actorBody.getMass() * jumpForce * 1000, 0,0, true);
                    player.isJumping = true;
                }
            }
        }));
        else{
            player.isJumping = false;
        }
    }

    private void logic(float delta) {
        //called by render
        //game logic goes here!
    }

    private void draw(){
        //clear the screen every frame to prevent artifacts
        ScreenUtils.clear(Color.BLACK);
        viewport.apply();

        if(player != null){
            Sprite actorSprite = player.getSprite();
            //set how the sprites are applied to the screen based on size/projection
            spriteBatch.setProjectionMatrix(viewport.getCamera().combined);
            //sets the sprite to the physics body's position
            actorSprite.setPosition(player.getBody().getPosition().x, player.getBody().getPosition().y);

            //sprites should ONLY ever be drawn in between SpriteBatch begin() and end() methods
            spriteBatch.begin();
            //I set the width and height to 8 and 5 respectively in the initialisation of the viewport.
            //This draws the background starting at 0,0, with a width of 8 and a height of 5
            spriteBatch.draw(backgroundTex, 0,0, (float) Gdx.graphics.getWidth() /10, (float) Gdx.graphics.getHeight() /10);
            floorSprite.draw(spriteBatch);
            //batch.draw(sprite, sprite.getX(), sprite.getY()); -- TODO should be fine as currently is
            actorSprite.draw(spriteBatch);
            spriteBatch.end();
        }


        //call ui draw method to draw ui buttons over the top
        ui.drawUi();
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

        // Dispose of all the things I create here!!
    }

    public Sprite getFloorSprite() {
        if(floorSprite != null) return floorSprite;

        floorSprite = new Sprite(floorTex);
        floorSprite.setPosition(0, -20);
        floorSprite.setSize(400, 40);
        return floorSprite;
    }
}
