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

public class Floor {

    Sprite sprite;

    Body body;

    Vector2 origin;

    //Can use for ANY static object! Pass in the texture, generates static object.
    public Floor(Vector2 origin, World world) {
        this.origin = origin;

        sprite = new Sprite(AssetManager.floorTex);
        sprite.setOriginCenter();
        sprite.setPosition(origin.x, origin.y);
        sprite.setSize(AssetManager.floorTex.getWidth(), AssetManager.floorTex.getHeight());

        BodyDef bodyDef = new BodyDef();
        bodyDef.type = BodyDef.BodyType.StaticBody;
        bodyDef.position.set(sprite.getX()/ Level.PPM, sprite.getY()/ Level.PPM);

        body = world.createBody(bodyDef);

        PolygonShape shape = new PolygonShape();
        shape.setAsBox(((sprite.getWidth() - 8) /2), ((sprite.getHeight() -8) /2));

        FixtureDef fixtureDef = new FixtureDef();
        fixtureDef.shape = shape;
        fixtureDef.density = 100;
        fixtureDef.restitution = 0f;
        fixtureDef.friction = 1f;

        body.createFixture(fixtureDef);
        body.setUserData(sprite);

        shape.dispose();

    }

    public void render(SpriteBatch spriteBatch){
        //Sprite bodySprite = (Sprite) body.getUserData();
        sprite.setPosition((body.getPosition().x - sprite.getWidth()/2), (body.getPosition().y - sprite.getHeight()/2));
        sprite.draw(spriteBatch);
    }
}
