package io.peter.wrapwrangler.assets;

import com.badlogic.gdx.math.Vector2;

import io.peter.wrapwrangler.assets.actors.Player;

public class DmgObject {

    boolean isHit;

    public DmgObject(){
        isHit = false;
    }

    public void onHit(Player player){
        //increase player in x axis, make player fixture sensor.
        if(!isHit){
            player.getBody().applyLinearImpulse(new Vector2(0, player.getBody().getMass() * 20000f), player.getBody().getWorldCenter(), true);
            player.getBody().getFixtureList().get(0).setSensor(true); //probably a hacky way of removing player's collision
            isHit = true;
        }
    }
}
