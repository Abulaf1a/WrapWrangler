package io.peter.wrapwrangler;

import com.badlogic.gdx.Game;

import io.peter.wrapwrangler.screens.FirstScreen;

/** {@link com.badlogic.gdx.ApplicationListener} implementation shared by all platforms. */
public class Main extends Game {
    @Override
    public void create() {
        setScreen(new FirstScreen());
    }
}
