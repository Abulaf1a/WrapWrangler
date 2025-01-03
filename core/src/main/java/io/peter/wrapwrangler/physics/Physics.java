package io.peter.wrapwrangler.physics;

import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.Body;
import com.badlogic.gdx.physics.box2d.Box2D;
import com.badlogic.gdx.physics.box2d.Box2DDebugRenderer;
import com.badlogic.gdx.physics.box2d.Contact;
import com.badlogic.gdx.physics.box2d.ContactImpulse;
import com.badlogic.gdx.physics.box2d.ContactListener;
import com.badlogic.gdx.physics.box2d.Manifold;
import com.badlogic.gdx.physics.box2d.World;

import java.util.ArrayList;
import java.util.List;

import io.peter.wrapwrangler.assets.actors.Player;
import io.peter.wrapwrangler.assets.actors.Spike;
import io.peter.wrapwrangler.assets.actors.Wrap;
import io.peter.wrapwrangler.screens.Level;
import io.peter.wrapwrangler.ui.UI;

public class Physics {
    Vector2 gravityDir;
    float GRAVITY = -98f;
    World world;
    Box2DDebugRenderer debugRenderer;
    Level level;

    static List<Wrap> subscribers;

    public void physicsSetup(Level level, float PPM) {

        subscribers = new ArrayList<>();

        Box2D.init();

        this.level = level;

        gravityDir = new Vector2(0, GRAVITY);

        world = new World(gravityDir, true);

        world.setContactListener(new ContactListener() {
            @Override
            public void beginContact(Contact contact) {
                System.out.println(contact.getFixtureA().getBody().getType() + " has contacted" + contact.getFixtureB().getBody().getType());
                if(contact.getFixtureA().isSensor() || contact.getFixtureB().isSensor()){
                    //wrap is always FixtureB
                    System.out.println("player has collided with wrap");

                    Object contacted = contact.getFixtureB().getBody().getUserData();

                    if(contacted instanceof Wrap ){
                        Wrap contactedWrap = (Wrap) contact.getFixtureB().getBody().getUserData();
                        for(Wrap wrap : subscribers){
                            if(wrap.hashCode() == contactedWrap.hashCode()){
                                if(!wrap.isCollected()){
                                    UI.updateScore();
                                    wrap.dispose();
                                }
                            }
                        }
                    }
                    else if(contacted instanceof Spike){
                        Spike contactedSpike = (Spike) contact.getFixtureB().getBody().getUserData();


                        contactedSpike.onHit(level.getPlayer());

                        }


                }
                else{
                    level.getPlayer().isJumping = false;
                    level.getPlayer().isOnFloor = true;
                }
            }

            @Override
            public void endContact(Contact contact) {
                System.out.println(contact.getFixtureA().getBody().getType() + " has stopped contacting" + contact.getFixtureB().getBody().getType());
                if(level.getPlayer().isJumping){
                    level.getPlayer().isOnFloor = false;
                }
            }

            @Override
            public void preSolve(Contact contact, Manifold oldManifold) {

            }

            @Override
            public void postSolve(Contact contact, ContactImpulse impulse) {

            }
        });
        debugRenderer = new Box2DDebugRenderer();
    }

    public void physicsProcess(float delta) {
        //float frameTime = Math.min(delta, 0.25f); //this avoids complete freezing on slow devices
        //if the game is running at 4 frames per second or less, the minimum physics process is 4fps

        //https://gafferongames.com/post/fix_your_timestep/
        //https://libgdx.com/wiki/extensions/physics/box2d
        //velocity and position iterations are apparently to do with physics simulation accuracy
        //higher = more accurate and slower to process.
        world.step(1/60f, 6, 2);
        debugRenderer.render(world, Level.viewport.getCamera().combined);
    }

    public World getWorld(){
        if(world != null)  return world;
        return new World(new Vector2(0, GRAVITY), true);
    }

    public static void addSubscriber(Wrap wrap){
        subscribers.add(wrap);
    }
}
