package io.peter.wrapwrangler.assets.actors;

import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.math.Vector3;
import com.badlogic.gdx.physics.box2d.Body;
import com.badlogic.gdx.physics.box2d.BodyDef;
import com.badlogic.gdx.physics.box2d.Fixture;
import com.badlogic.gdx.physics.box2d.FixtureDef;
import com.badlogic.gdx.physics.box2d.PolygonShape;
import com.badlogic.gdx.physics.box2d.World;

import io.peter.wrapwrangler.assets.AssetManager;
import io.peter.wrapwrangler.screens.FirstScreen;

public class Floor {

    Sprite sprite;

    Body body;

    Vector2 origin;


    public Floor(Vector2 origin, World world) {
        this.origin = origin;

        sprite = new Sprite(AssetManager.floorTex);
        sprite.setOriginCenter();
        sprite.setPosition(origin.x, origin.y);
        sprite.setSize(480/FirstScreen.PPM, 70/FirstScreen.PPM);

        BodyDef bodyDef = new BodyDef();
        bodyDef.type = BodyDef.BodyType.StaticBody;
        bodyDef.position.set(sprite.getX()/FirstScreen.PPM, sprite.getY()/FirstScreen.PPM);

        body = world.createBody(bodyDef);

        PolygonShape shape = new PolygonShape();
        shape.setAsBox((sprite.getWidth()/2), (sprite.getHeight()/2));

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
        Sprite bodySprite = (Sprite) body.getUserData();
        bodySprite.setPosition((body.getPosition().x - sprite.getWidth()/2), (body.getPosition().y - sprite.getHeight()/2));
        bodySprite.draw(spriteBatch);
    }
}
