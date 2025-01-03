package io.peter.wrapwrangler.assets.actors;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.Body;
import com.badlogic.gdx.physics.box2d.BodyDef;
import com.badlogic.gdx.physics.box2d.FixtureDef;
import com.badlogic.gdx.physics.box2d.PolygonShape;
import com.badlogic.gdx.physics.box2d.World;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import io.peter.wrapwrangler.assets.AssetManager;
import io.peter.wrapwrangler.screens.Level;

public class Player {
    Texture spriteSheet;
    Animation<TextureRegion> animation;

   //public boolean isLeft;
    float animTime;
    Body body;
    int jumpForce;
    float speed;
    public boolean isJumping;
    public boolean isOnFloor;
    Vector2 origin;
    public Player(Vector2 origin,World world, int jumpForce, float speed) {

        spriteSheet = AssetManager.cowboySpriteSheet;

        this.origin = origin;

        TextureRegion[][] temp = TextureRegion.split(spriteSheet,
            32,
            32);

        List<TextureRegion> actualList = new ArrayList<>();

        for(TextureRegion[] col : temp){
            actualList.addAll(Arrays.asList(col));
        }

        TextureRegion[] frames = actualList.toArray(new TextureRegion[actualList.size()]);

        animation = new Animation<>(0.25f, frames);

        animTime = 0f;

        this.jumpForce = jumpForce;
        this.speed = speed;

        BodyDef bodyDef = new BodyDef();
        bodyDef.type = BodyDef.BodyType.DynamicBody;
        bodyDef.fixedRotation = true;

        bodyDef.position.set(animation.getKeyFrames()[0].getRegionWidth()/ Level.PPM, animation.getKeyFrames()[0].getRegionHeight()/ Level.PPM);

        body = world.createBody(bodyDef);

        PolygonShape shape = new PolygonShape();

        shape.setAsBox((float) animation.getKeyFrames()[0].getRegionWidth() /3, (float) animation.getKeyFrames()[0].getRegionHeight() /2);

        FixtureDef fixtureDef = new FixtureDef();
        fixtureDef.shape = shape;
        fixtureDef.density = 40f;
        fixtureDef.restitution = 0f;
        fixtureDef.friction = 0.6f;

        body.createFixture(fixtureDef);
        body.setUserData(animation);

        shape.dispose();
    }

    public void render(SpriteBatch spriteBatch){

        spriteBatch.end();

        animTime += Gdx.graphics.getDeltaTime(); //could pass in from screen render?

        try{
            TextureRegion currentFrame = animation.getKeyFrame(animTime, true);

            spriteBatch.begin();

            if(currentFrame != null && isOnFloor) {

                spriteBatch.draw(currentFrame,
                    body.getPosition().x - currentFrame.getRegionWidth()/2,
                    body.getPosition().y - currentFrame.getRegionWidth()/2
                );

            }
            else{
                TextureRegion frame = animation.getKeyFrames()[1];
                spriteBatch.draw(frame,
                    body.getPosition().x - frame.getRegionWidth()/2,
                    body.getPosition().y - frame.getRegionWidth()/2);
            }

        }
        catch(Exception e){

            Gdx.app.log("PETER", "Exception reading animation frame: " + e.getMessage());
            TextureRegion frame = animation.getKeyFrames()[1];
            spriteBatch.draw(animation.getKeyFrames()[1], body.getPosition().x - frame.getRegionWidth()/2, body.getPosition().y - frame.getRegionWidth()/2);

        }
    }

    public void setPos(float x, float y){

    }

    public Animation<TextureRegion> getAnimation() {
        return animation;
    }


    public Vector2 getPos(){
        return body.getPosition();
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
