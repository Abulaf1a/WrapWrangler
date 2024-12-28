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
import io.peter.wrapwrangler.screens.FirstScreen;

public class Player {

    Sprite sprite;
    Body body;

    int jumpForce;

    float speed;

    public boolean isJumping;
    public boolean isOnFloor;

    Vector2 origin;

    public Player(Vector2 origin,World world, int jumpForce, float speed) {

        this.origin = origin;

        sprite = new Sprite(AssetManager.actorTex);
        sprite.setOriginCenter();
        sprite.setPosition(origin.x, origin.y); //setting above floor to prevent initial collision
        sprite.setSize(50/FirstScreen.PPM, 100/FirstScreen.PPM); // 1 by 2 metres

        this.jumpForce = jumpForce;
        this.speed = speed;

        BodyDef bodyDef = new BodyDef();
        bodyDef.type = BodyDef.BodyType.DynamicBody;
        bodyDef.fixedRotation = true;

        //TODO PROJECT SPRITE POSITION AND SET AS BODYDEF POSITION/
        bodyDef.position.set(sprite.getX()/FirstScreen.PPM, sprite.getY()/FirstScreen.PPM);

        body = world.createBody(bodyDef);

        PolygonShape shape = new PolygonShape();

        shape.setAsBox(sprite.getWidth()/2, sprite.getHeight()/2);

        FixtureDef fixtureDef = new FixtureDef();
        fixtureDef.shape = shape;
        fixtureDef.density = 40f;
        fixtureDef.restitution = 0f;
        fixtureDef.friction = 0.6f;


        body.createFixture(fixtureDef);

        body.setUserData(sprite);

        shape.dispose();
    }

    public void render(SpriteBatch spriteBatch){

        Sprite bodySprite = (Sprite) body.getUserData();

        bodySprite.setPosition((body.getPosition().x -  sprite.getWidth()/2), (body.getPosition().y - sprite.getHeight()/2));

        bodySprite.draw(spriteBatch);

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
