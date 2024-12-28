package io.peter.wrapwrangler.screens;

import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.Camera;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.Body;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.utils.ChangeListener;
import com.badlogic.gdx.utils.ScreenUtils;
import com.badlogic.gdx.utils.viewport.FitViewport;
import com.badlogic.gdx.utils.viewport.Viewport;

import io.peter.wrapwrangler.assets.AssetManager;
import io.peter.wrapwrangler.assets.actors.Floor;
import io.peter.wrapwrangler.assets.actors.Player;
import io.peter.wrapwrangler.physics.Physics;
import io.peter.wrapwrangler.ui.UI;

/** First screen of the application. Displayed after the application is created. */
public class FirstScreen implements Screen {

    public static float PPM = 5f;
    public static Viewport viewport;
    Texture backgroundTex;
    Texture actorTex;
    Texture floorTex;

    //TODO put intensive spritebatch into main game class. https://www.youtube.com/watch?v=mCdnFa3U9vs
    SpriteBatch spriteBatch;
    //Sprite actorSprite;
    Sprite floorSprite;
    Player player;

    Floor floor;

    Floor floor2;
    UI ui;
    Physics physics;

    Camera gamecam;

    @Override
    public void show() {
        ui = new UI();
        physics = new Physics();

        viewSetup(); //set up the viewport
        sceneSetup(); //Place assets in the scene

        ui.uiSetup(viewport, spriteBatch); //set up UI

        physics.physicsSetup(this, PPM); //set up Box2D physics

        player = new Player(new Vector2(100, 100), physics.getWorld(), 100000, 5);
        floor = new Floor(new Vector2(0,0), physics.getWorld());
        floor2 = new Floor(new Vector2(500, 50), physics.getWorld());
    }

    private void viewSetup() {
        gamecam = new OrthographicCamera();

        viewport = new FitViewport(800/PPM, 480/PPM, gamecam);

        viewport.setCamera(gamecam);

        gamecam.position.set(viewport.getScreenWidth() /2f, viewport.getScreenHeight()/2f, 0f);
    }

    private void sceneSetup() {
        spriteBatch = new SpriteBatch();
        spriteBatch.setProjectionMatrix(gamecam.combined);
        backgroundTex = AssetManager.backgroundTex;

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

        Vector2 current = player.getPos();

        //TODO - even with incredibly strong jump forces, the jump amt is limited - this metohd only calls ONCE when jump btn pressed.

        if(ui.getButtonJump().addListener(new ChangeListener() {
            @Override
            public void changed(ChangeEvent event, Actor actor) {
                if(player.isOnFloor) {
                    player.isOnFloor = false;
                    player.isJumping = true;
                    actorBody.applyLinearImpulse(new Vector2(0, actorBody.getMass() * player.getJumpForce() * player.getJumpForce()), actorBody.getWorldCenter(), true);
                }
            }
        }));

        //go left and right more slowly while jumping? also continue to fall while going left and right -- e.g. you can't fly!
        if(ui.getButtonLeft().isPressed()){
            player.getBody().applyLinearImpulse(-40f*player.getBody().getMass(), physics.getWorld().getGravity().y * (float) Math.sqrt(actorBody.getMass()), player.getBody().getPosition().x,0, true);
        }

        else if(ui.getButtonRight().isPressed()){
            actorBody.applyLinearImpulse(40f*actorBody.getMass(), physics.getWorld().getGravity().y * (float) Math.sqrt(actorBody.getMass()), actorBody.getPosition().x,0, true);
        }
        else{
            if(actorBody.getLinearVelocity().x != 0f){
                actorBody.applyForce(-actorBody.getLinearVelocity().x * actorBody.getMass() * 4, physics.getWorld().getGravity().y * actorBody.getMass(), actorBody.getPosition().x, 0 , true);
            }
        }

        if(ui.getDebugReset().isPressed()){
            actorBody.setTransform(2, 10, 0);
        }

        Vector2 actual = player.getPos();

        gamecam.position.x = player.getPos().x;

    }

    private void logic(float delta) {
        //called by render
        //game logic goes here!
    }

    private void draw(){
        //clear the screen every frame to prevent artifacts
        ScreenUtils.clear(Color.BLACK);
        viewport.apply();

        spriteBatch.setProjectionMatrix(gamecam.combined);

        //sprites should ONLY ever be drawn in between SpriteBatch begin() and end() methods
        spriteBatch.begin();

        spriteBatch.draw(backgroundTex, gamecam.position.x - ((float) gamecam.viewportWidth/2), 0, (float) gamecam.viewportWidth, (float) gamecam.viewportHeight);

        floor.render(spriteBatch);
        floor2.render(spriteBatch);
        player.render(spriteBatch);


        spriteBatch.setProjectionMatrix(ui.stage.getCamera().combined);

        //call ui draw method to draw ui buttons over the top
        ui.drawUi(player);

        spriteBatch.end();

    }

    @Override
    public void resize(int width, int height) {

        gamecam.position.set(gamecam.viewportWidth/2, gamecam.viewportHeight/2, 0);

        gamecam.update();

        spriteBatch = new SpriteBatch();

        spriteBatch.setProjectionMatrix(gamecam.combined);
        // Resize your screen here. The parameters represent the new window size.
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

    public Viewport getViewport() {
        return viewport;
    }

    public Player getPlayer() {
        return player;
    }
}
