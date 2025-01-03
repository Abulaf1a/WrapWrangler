package io.peter.wrapwrangler.screens;

import static jdk.xml.internal.SecuritySupport.getClassLoader;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.files.FileHandle;
import com.badlogic.gdx.graphics.Camera;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.Body;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.utils.ChangeListener;
import com.badlogic.gdx.utils.ScreenUtils;
import com.badlogic.gdx.utils.viewport.ExtendViewport;
import com.badlogic.gdx.utils.viewport.FitViewport;
import com.badlogic.gdx.utils.viewport.StretchViewport;
import com.badlogic.gdx.utils.viewport.Viewport;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Vector;
import java.util.regex.MatchResult;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.stream.Collectors;

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

    //TODO put intensive spritebatch into main game class. https://www.youtube.com/watch?v=mCdnFa3U9vs
    SpriteBatch spriteBatch;
    Player player;


    List<Floor> floorMap;
    List<Vector2> positions;

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


//        try {
//
//            String path = String.valueOf(Gdx.files.internal("com/badlogic/gdx/levelcoords/coordinates"));
//
//            FileHandle fh = Gdx.files.local("com/badlogic/gdx/levelcoords/coordinates.txt");
//
//            File f = new File(fh.path());
//
//            FileReader fr = new FileReader(fh.path());
//            BufferedReader br = new BufferedReader(fr);
//
//            while(br.readLine() != null){
//
//                List<Integer> results = new ArrayList<>();
//
//                Pattern pattern = Pattern.compile("([0-9]+)");
//
//                Matcher matcher = pattern.matcher(br.readLine());
//
//
//                while(matcher.find()){
//                    results.add(Integer.parseInt(matcher.group()));
//                }
//                positions.add(new Vector2(results.get(0), results.get(1)));
//            }
//
//
//            positions.forEach(e -> floorMap.add(new Floor(e, physics.getWorld())));
//
//        } catch (FileNotFoundException e) {
//            throw new RuntimeException(e);
//        } catch (IOException e) {
//            throw new RuntimeException(e);
//        }

        floorMap = new ArrayList<>();
        floorMap.add(new Floor(new Vector2(0,0), physics.getWorld()));
        floorMap.add(new Floor(new Vector2(500, 50), physics.getWorld()));
        floorMap.add(new Floor(new Vector2(1300, 50), physics.getWorld()));

        floorMap.add(new Floor(new Vector2(2000, 100), physics.getWorld()));

        floorMap.add(new Floor(new Vector2(2500, 500), physics.getWorld()));


        floorMap.add(new Floor(new Vector2(3000, 0), physics.getWorld()));
        floorMap.add(new Floor(new Vector2(4000, 75), physics.getWorld()));
        floorMap.add(new Floor(new Vector2(4000, 500), physics.getWorld()));

        player = new Player(new Vector2(100, 100), physics.getWorld(), 100000, 5);
        floor = new Floor(new Vector2(0,0), physics.getWorld());
        floor2 = new Floor(new Vector2(500, 50), physics.getWorld());
    }

    private void viewSetup() {
        gamecam = new OrthographicCamera();
        viewport = new ExtendViewport(800/PPM, 480/PPM, gamecam);
        //viewport = new FitViewport(800/PPM, 480/PPM, gamecam);

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

        if(ui.getButtonLeft().isPressed()){
            if(player.isOnFloor) player.getAnimation().setFrameDuration(0.125f);
           // if(!player.isLeft) player.isLeft = true;

            player.getBody().applyLinearImpulse(-40f*player.getBody().getMass(), physics.getWorld().getGravity().y * (float) Math.sqrt(actorBody.getMass()), player.getBody().getPosition().x,0, true);
        }

        else if(ui.getButtonRight().isPressed()){
            if(player.isOnFloor) player.getAnimation().setFrameDuration(0.125f);
            //if (player.isLeft) player.isLeft = false;

            actorBody.applyLinearImpulse(40f*actorBody.getMass(), physics.getWorld().getGravity().y * (float) Math.sqrt(actorBody.getMass()), actorBody.getPosition().x,0, true);
        }
        else{
            if(actorBody.getLinearVelocity().x != 0f){
                actorBody.applyForce(-actorBody.getLinearVelocity().x * actorBody.getMass() * 4, physics.getWorld().getGravity().y * actorBody.getMass(), actorBody.getPosition().x, 0 , true);
            }
            player.getAnimation().setFrameDuration(100000f);
        }

        if(ui.getDebugReset().isPressed()){
            actorBody.setTransform(2, 10, 0);
        }
    }

    private void logic(float delta) {
        //called by render
        //game logic goes here!
    }

    private void draw(){
        ScreenUtils.clear(Color.CYAN);
        viewport.apply();

        gamecam.position.x = player.getBody().getPosition().x;
        gamecam.position.y = player.getPos().y > 0 ? player.getPos().y : 0;

        spriteBatch.setProjectionMatrix(gamecam.combined);

        //sprites should ONLY ever be drawn in between SpriteBatch begin() and end() methods
        spriteBatch.begin();

        floorMap.forEach(floor -> floor.render(spriteBatch));
        floor.render(spriteBatch);
        floor2.render(spriteBatch);
        player.render(spriteBatch);

        spriteBatch.setProjectionMatrix(ui.stage.getCamera().combined); //doesn't seem necessary but for some reason doesn't render player without this line
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

        // TODO Dispose of all the things I create here to prevent memory leaks!
    }
    

    public Player getPlayer() {
        return player;
    }
}