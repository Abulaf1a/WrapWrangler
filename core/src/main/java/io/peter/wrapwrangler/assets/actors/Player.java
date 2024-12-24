package io.peter.wrapwrangler.assets.actors;

import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.Body;
import com.badlogic.gdx.physics.box2d.BodyDef;
import com.badlogic.gdx.physics.box2d.FixtureDef;
import com.badlogic.gdx.physics.box2d.PolygonShape;
import com.badlogic.gdx.physics.box2d.World;

import io.peter.wrapwrangler.assets.AssetManager;

public class Player {
    //sprite
    //physics etc.

    Sprite sprite;

    int jumpForce;

    float speed;

    Body body;

    public boolean isJumping;
    public boolean isOnFloor;


    public Player(int jumpForce, float speed, World world) {

        sprite = new Sprite(AssetManager.actorTex);
        sprite.setPosition(20, 50); //setting above floor to prevent initial collision
        sprite.setSize(20, 50);

        this.jumpForce = jumpForce;
        this.speed = speed;
        BodyDef actorBodyDef = new BodyDef();

        actorBodyDef.type = BodyDef.BodyType.DynamicBody;

        actorBodyDef.position.set(sprite.getX(), sprite.getY());

        body = world.createBody(actorBodyDef);

        PolygonShape shape = new PolygonShape();

        shape.setAsBox(sprite.getWidth()/2, sprite.getHeight()/2);

        FixtureDef fixtureDef = new FixtureDef();
        fixtureDef.shape = shape;
        fixtureDef.density = 4f;
        fixtureDef.restitution = 0f;
        fixtureDef.friction = 0.6f;

        body.createFixture(fixtureDef);

        body.setUserData(sprite);

        shape.dispose();
    }

    public void setPos(float x, float y){

    }

    public Vector2 getPos(){
        return body.getPosition();
    }


    public Sprite getSprite() {
        return sprite;
    }

    public void setSprite(Sprite sprite) {
        this.sprite = sprite;
    }

    public int getJumpForce() {
        return jumpForce;
    }

    public void setJumpForce(int jumpForce) {
        this.jumpForce = jumpForce;
    }

    public float getSpeed() {
        return speed;
    }

    public void setSpeed(float speed) {
        this.speed = speed;
    }

    public Body getBody() {
        return body;
    }

    public void setBody(Body body) {
        this.body = body;
    }
}
