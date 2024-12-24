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
import com.badlogic.gdx.physics.box2d.Fixture;
import com.badlogic.gdx.physics.box2d.FixtureDef;
import com.badlogic.gdx.physics.box2d.PolygonShape;
import com.badlogic.gdx.physics.box2d.World;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.utils.ChangeListener;
import com.badlogic.gdx.utils.ScreenUtils;
import com.badlogic.gdx.utils.viewport.ScreenViewport;

import io.peter.wrapwrangler.ui.UI;

/** First screen of the application. Displayed after the application is created. */
public class FirstScreen implements Screen {

    ScreenViewport viewport;

    //TODO Separate into AssetManager
    Texture backgroundTex;
    Texture actorTex;
    Texture floorTex;
    SpriteBatch spriteBatch;
    Sprite actorSprite;

    Sprite floorSprite;

    //Unused - get touch anywhere on screen
    Vector2 touchPos;

    //Physics
    Body actorBody;
    final float GRAVITY = -9.8f;
    Vector2 gravityDir;

    Vector2 jumpForce;
    World world;
    Box2DDebugRenderer debugRenderer;

    Body floorBody;

    //UI
    UI ui;

    @Override
    public void show() {

        ui = new UI();

        viewSetup(); //set up the viewport

        ui.uiSetup(viewport); //set the UI up - NOTE, UI is drawn over the scene due to render call order in draw method

        sceneSetup(); //Place assets in the scene

        physicsSetup(); //set up Box2D physics - after scene as bodyDef relies on actorSprite location
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
        backgroundTex = new Texture("com/badlogic/gdx/sprites/Background_2.png");

        actorTex = new Texture("com/badlogic/gdx/sprites/cowboy_placeholder.png");

        floorTex = new Texture("com/badlogic/gdx/sprites/dirt_tile_placeholder.png");

        actorSprite = new Sprite(actorTex);

        actorSprite.setPosition(20, 50); //setting above floor to prevent initial collision
        //floor init at y = 20

        actorSprite.setSize(20, 50);

        floorSprite = new Sprite(floorTex);

        floorSprite.setPosition(0, -20);

        floorSprite.setSize(400, 40);
    }

    private void physicsSetup() {
        gravityDir = new Vector2(0, GRAVITY);

        jumpForce = new Vector2(0, 2f);

        world = new World(gravityDir, true);

        //TODO - REMOVE DEBUG
        debugRenderer = new Box2DDebugRenderer();


        //Actor physics setup
        BodyDef actorBodyDef = new BodyDef();

        actorBodyDef.type = BodyDef.BodyType.DynamicBody;

        actorBodyDef.position.set(actorSprite.getX(), actorSprite.getY());

        actorBody = world.createBody(actorBodyDef);

        //https://gamefromscratch.com/libgdx-tutorial-13-physics-with-box2d-part-1-a-basic-physics-simulations/
        //collision object
        PolygonShape shape = new PolygonShape();

        shape.setAsBox(actorSprite.getWidth()/2, actorSprite.getHeight()/2);

        FixtureDef fixtureDef = new FixtureDef();
        fixtureDef.shape = shape;
        fixtureDef.density = 4f;

        actorBody.createFixture(fixtureDef);

        //disposing of things that are used to construct other objects
        //it's important to dispose of the right things to prevent memory leaks.
        shape.dispose();

        //Floor setup
        BodyDef floorBodyDef = new BodyDef();

        floorBodyDef.type = BodyDef.BodyType.StaticBody;

        floorBodyDef.position.set(floorSprite.getX(), floorSprite.getY());

        floorBody = world.createBody(floorBodyDef);

        PolygonShape floorShape = new PolygonShape();

        floorShape.setAsBox(floorSprite.getWidth()/2, floorSprite.getHeight()/2);

        FixtureDef floorFixtureDef = new FixtureDef();
        floorFixtureDef.shape = floorShape;
        floorFixtureDef.density = 1f;

        floorBody.createFixture(floorFixtureDef);

        floorShape.dispose();

    }

    @Override
    public void render(float delta) {
        // Draw your screen here. "delta" is the time since last render in seconds.
        // Organise this code into input() logic() and draw() methods
        input(delta);

        logic(delta);

        draw();

        //It is recommended to draw graphics before physics processing
        //Also its recommended
        physicsProcess(delta);

    }


    //TODO create input handler
    private void input(float delta){
        float speed = 60f;

        if(ui.getButtonLeft().isPressed()){
            actorBody.applyForce(-10f*actorBody.getMass(), 0, 0,0, true);
        }
        else if(ui.getButtonRight().isPressed()){
            actorBody.applyForce(10f*actorBody.getMass(), 0,0,0, true);
        }

        //gravity dealt with in logic method
        if(ui.getButtonJump().addListener(new ChangeListener() {
            @Override
            public void changed(ChangeEvent event, Actor actor) {

                actorBody.applyForce(0, jumpForce.y*actorBody.getMass(), 0,0, true);
            }
        }));


    }

    private void physicsProcess(float delta) {

        float frameTime = Math.min(delta, 0.25f); //this avoids complete freezing on slow devices
        //if the game is running at 4 frames per second or less, the minimum physics process is 4fps

        //https://gafferongames.com/post/fix_your_timestep/
        //https://libgdx.com/wiki/extensions/physics/box2d
        world.step(1/60f, 6, 2);
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

        //sets the sprite to the physics body's position
        actorSprite.setPosition(actorBody.getPosition().x, actorBody.getPosition().y);

        //sprites should ONLY ever be drawn in between SpriteBatch begin() and end() methods
        spriteBatch.begin();

        //I set the width and height to 8 and 5 respectively in the initialisation of the viewport.
        //This draws the background starting at 0,0, with a width of 8 and a height of 5
        spriteBatch.draw(backgroundTex, 0,0, (float) Gdx.graphics.getWidth() /10, (float) Gdx.graphics.getHeight() /10);

        floorSprite.draw(spriteBatch);

        //batch.draw(sprite, sprite.getX(), sprite.getY()); -- TODO should be fine as currently is
        actorSprite.draw(spriteBatch);

        spriteBatch.end();

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

}
