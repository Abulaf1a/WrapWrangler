package io.peter.wrapwrangler.physics;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.Body;
import com.badlogic.gdx.physics.box2d.BodyDef;
import com.badlogic.gdx.physics.box2d.Box2DDebugRenderer;
import com.badlogic.gdx.physics.box2d.Contact;
import com.badlogic.gdx.physics.box2d.ContactImpulse;
import com.badlogic.gdx.physics.box2d.ContactListener;
import com.badlogic.gdx.physics.box2d.FixtureDef;
import com.badlogic.gdx.physics.box2d.Manifold;
import com.badlogic.gdx.physics.box2d.PolygonShape;
import com.badlogic.gdx.physics.box2d.World;

import io.peter.wrapwrangler.assets.AssetManager;
import io.peter.wrapwrangler.assets.actors.Player;
import io.peter.wrapwrangler.screens.FirstScreen;

public class Physics {

    Vector2 gravityDir;
    float GRAVITY = -9.8f;
    World world;
    boolean isOnFloor;
    Box2DDebugRenderer debugRenderer;
    Texture floorTex;
    Sprite floorSprite;
    Body floorBody;
    int playerSpeed = 10;
    int playerJump = 5;

    FirstScreen firstScreen;

    public void physicsSetup(FirstScreen firstScreen) {

        this.firstScreen = firstScreen;

        gravityDir = new Vector2(0, GRAVITY);

        world = new World(gravityDir, true);

        world.setContactListener(new ContactListener() {
            @Override
            public void beginContact(Contact contact) {
                System.out.println(contact.getFixtureA() + " has contacted" + contact.getFixtureB());

                isOnFloor = true;
            }

            @Override
            public void endContact(Contact contact) {

                System.out.println(contact.getFixtureA() + " has stopped contacting" + contact.getFixtureB());

                isOnFloor = false;

            }

            @Override
            public void preSolve(Contact contact, Manifold oldManifold) {

            }

            @Override
            public void postSolve(Contact contact, ContactImpulse impulse) {

            }
        });

        //TODO - REMOVE DEBUG
        debugRenderer = new Box2DDebugRenderer();

        floorSetup();
    }

    public void floorSetup(){

        floorSprite = firstScreen.getFloorSprite();
        BodyDef floorBodyDef = new BodyDef();
        floorBodyDef.type = BodyDef.BodyType.StaticBody;
        floorBodyDef.position.set(floorSprite.getX(), floorSprite.getY());

        floorBody = world.createBody(floorBodyDef);

        PolygonShape floorShape = new PolygonShape();
        floorShape.setAsBox(floorSprite.getWidth()/2, floorSprite.getHeight()/2);

        FixtureDef floorFixtureDef = new FixtureDef();
        floorFixtureDef.shape = floorShape;
        floorFixtureDef.density = 1f;
        floorFixtureDef.restitution = 0f;
        floorFixtureDef.friction = 1f;

        floorBody.createFixture(floorFixtureDef);

        floorBody.setUserData(floorSprite);

        floorShape.dispose();
    }

    public void physicsProcess(float delta) {
        //float frameTime = Math.min(delta, 0.25f); //this avoids complete freezing on slow devices
        //if the game is running at 4 frames per second or less, the minimum physics process is 4fps

        //https://gafferongames.com/post/fix_your_timestep/
        //https://libgdx.com/wiki/extensions/physics/box2d
        world.step(1/60f, 6, 2);
    }

    public World getWorld(){
        if(world != null)  return world;
        return new World(new Vector2(0, -9.8f), true);
    }
}
