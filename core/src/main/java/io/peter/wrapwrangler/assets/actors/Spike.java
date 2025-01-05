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
import io.peter.wrapwrangler.assets.DmgObject;
import io.peter.wrapwrangler.screens.Level;

public class Spike extends DmgObject {


    Body body;

    Sprite sprite;

    Vector2 origin;

    public Spike(Vector2 origin, World world){

        sprite = new Sprite( AssetManager.spikeSprite);

        this.origin = origin;

        BodyDef bodyDef = new BodyDef();
        bodyDef.type = BodyDef.BodyType.StaticBody;

        bodyDef.fixedRotation = true;

        bodyDef.position.set(origin.x/ Level.PPM, origin.y/ Level.PPM);

        body = world.createBody(bodyDef);

        PolygonShape shape = new PolygonShape();

        shape.setAsBox((float) sprite.getWidth()/Level.PPM, (float) sprite.getHeight()/Level.PPM);

        FixtureDef fixtureDef = new FixtureDef();
        fixtureDef.shape = shape;
        fixtureDef.density = 40f;
        fixtureDef.restitution = 0f;
        fixtureDef.friction = 0.6f;
        fixtureDef.isSensor = true; //collision detected but object doesn't bounce/ change velocity.

        body.createFixture(fixtureDef);

        body.setUserData(this);

        shape.dispose();
    }

    public void render(SpriteBatch spriteBatch){

        sprite.setPosition((body.getPosition().x - sprite.getWidth()/2), (body.getPosition().y - sprite.getHeight()/2));
        sprite.draw(spriteBatch);
    }


}
