package io.peter.wrapwrangler;

import com.badlogic.gdx.Game;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Camera;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.utils.viewport.FitViewport;
import com.badlogic.gdx.utils.viewport.Viewport;

import io.peter.wrapwrangler.screens.MainMenu;
import io.peter.wrapwrangler.screens.TextScreen;

/** {@link com.badlogic.gdx.ApplicationListener} implementation shared by all platforms. */
public class WW extends Game {

    public static float PPM = 5f;

    public Camera gamecam;
    public SpriteBatch spriteBatch;
    public BitmapFont font;
    public Viewport viewport;
    @Override
    public void create() {

        gamecam = new OrthographicCamera();

        spriteBatch = new SpriteBatch();
        font = new BitmapFont();
        viewport = new FitViewport(800, 480);

        font.setColor(Color.CORAL);
        //scale font based on viewport size
        font.setUseIntegerPositions(false);

        font.getData().setScale(viewport.getWorldHeight() / Gdx.graphics.getHeight());

        setScreen(new TextScreen(this, "WRAP WRANGLER, press anywhere to continue"));
    }
}
