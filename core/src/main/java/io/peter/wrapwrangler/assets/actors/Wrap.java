package io.peter.wrapwrangler.assets.actors;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.Pixmap;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.Body;
import com.badlogic.gdx.physics.box2d.BodyDef;
import com.badlogic.gdx.physics.box2d.Fixture;
import com.badlogic.gdx.physics.box2d.FixtureDef;
import com.badlogic.gdx.physics.box2d.PolygonShape;
import com.badlogic.gdx.physics.box2d.World;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import io.peter.wrapwrangler.assets.AssetManager;
import io.peter.wrapwrangler.physics.Physics;
import io.peter.wrapwrangler.screens.Level;

public class Wrap {

    Texture spriteSheet;
    Animation<TextureRegion> animation;
    float animTime;
    Body body;
    Vector2 origin;

    boolean isCollected;

    public Wrap(Vector2 origin, World world){
        spriteSheet = AssetManager.wrapSpriteSheet;

        this.origin = origin;

        TextureRegion[][] temp = TextureRegion.split(spriteSheet,
            32,
            32);

        List<TextureRegion> actualList = new ArrayList<>();

        for(TextureRegion[] col : temp){
            actualList.addAll(Arrays.asList(col));
        }

        TextureRegion[] frames = actualList.toArray(new TextureRegion[actualList.size()]);

        animation = new Animation<>(0.125f, frames);

        animation.setPlayMode(Animation.PlayMode.LOOP_PINGPONG);

        animTime = 0f;


        BodyDef bodyDef = new BodyDef();
        bodyDef.type = BodyDef.BodyType.StaticBody;


        bodyDef.fixedRotation = true;

        bodyDef.position.set(origin.x/ Level.PPM, origin.y/ Level.PPM);

        body = world.createBody(bodyDef);

        PolygonShape shape = new PolygonShape();

        //smaller box - so player hits the centre of burrito.
        shape.setAsBox((float) animation.getKeyFrames()[0].getRegionWidth() /10, (float) animation.getKeyFrames()[0].getRegionHeight() /10);

        FixtureDef fixtureDef = new FixtureDef();
        fixtureDef.shape = shape;
        fixtureDef.density = 40f;
        fixtureDef.restitution = 0f;
        fixtureDef.friction = 0.6f;
        fixtureDef.isSensor = true;

        body.createFixture(fixtureDef);

        body.setUserData(this);

        Physics.addSubscriber(this);

        isCollected = false;

        shape.dispose();
    }

    public void render(SpriteBatch spriteBatch){

        animTime += Gdx.graphics.getDeltaTime();

        TextureRegion currentFrame = animation.getKeyFrame(animTime, true);

        if(currentFrame != null){
            spriteBatch.draw(currentFrame,
                body.getPosition().x - currentFrame.getRegionWidth()/2,
                body.getPosition().y - currentFrame.getRegionHeight()/2);
        }


    }

    public void dispose(){

        Pixmap pixmap = new Pixmap(1, 1, Pixmap.Format.Alpha);
        pixmap.setColor(Color.CLEAR);

        spriteSheet = new Texture(pixmap);
        TextureRegion clearRegion = new TextureRegion(spriteSheet);
        animation = new Animation<>(1000f, clearRegion);

        isCollected = true;

    }

    public boolean isCollected(){
        return isCollected;
    }




}
