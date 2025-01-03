package io.peter.wrapwrangler.assets.actors;

import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.Body;
import com.badlogic.gdx.physics.box2d.BodyDef;
import com.badlogic.gdx.physics.box2d.FixtureDef;
import com.badlogic.gdx.physics.box2d.PolygonShape;
import com.badlogic.gdx.physics.box2d.World;

import io.peter.wrapwrangler.assets.AssetManager;
import io.peter.wrapwrangler.screens.Level;

public class Baddie {

    boolean movingRight;

    float timer;
    float reset;
    Sprite sprite;

    Body body;

    Vector2 origin;

    //Can use for ANY static object! Pass in the texture, generates static object.
    public Baddie(Vector2 origin, World world) {

        timer = 0f;

        reset = 8f;

        movingRight = true;
        this.origin = origin;

        sprite = new Sprite(AssetManager.baddieSprite);
        sprite.setOriginCenter();
        sprite.setPosition(origin.x, origin.y);
        sprite.setSize(AssetManager.baddieSprite.getWidth(), AssetManager.baddieSprite.getHeight());

        BodyDef bodyDef = new BodyDef();
        bodyDef.type = BodyDef.BodyType.StaticBody; //changed to static - easier to make levels.

        bodyDef.position.set(sprite.getX()/ Level.PPM, sprite.getY()/ Level.PPM);

        body = world.createBody(bodyDef);

        PolygonShape shape = new PolygonShape();
        shape.setAsBox(((sprite.getWidth() - 8) /2), ((sprite.getHeight() -8) /2));

        FixtureDef fixtureDef = new FixtureDef();
        fixtureDef.shape = shape;
        fixtureDef.density = 100;
        fixtureDef.restitution = 0f;
        fixtureDef.friction = 1f;
        fixtureDef.isSensor = true;

        body.createFixture(fixtureDef);
        body.setUserData(sprite);

        shape.dispose();

    }

    public void move(float delta){
        //timer?
        timer += delta;

        if(timer < reset){
            //move
            if(movingRight){
                body.setTransform(body.getPosition().x -0.1f, body.getPosition().y, body.getAngle());
                //body.applyLinearImpulse(10f*body.getMass(), body.getWorld().getGravity().y * (float) Math.sqrt(body.getMass()), body.getPosition().x,0, true);
            }
            else {
                //body.applyLinearImpulse(-10f*body.getMass(), body.getWorld().getGravity().y * (float) Math.sqrt(body.getMass()), body.getPosition().x,0, true);
                body.setTransform(body.getPosition().x + 0.1f, body.getPosition().y, body.getAngle());

            }
        }
        else{
            movingRight = !movingRight;
            timer = 0f;
        }

    }

    public void render(SpriteBatch spriteBatch){
        //Sprite bodySprite = (Sprite) body.getUserData();
        sprite.setPosition((body.getPosition().x - sprite.getWidth()/2), (body.getPosition().y - sprite.getHeight()/2));
        sprite.draw(spriteBatch);
    }
}
