package io.peter.wrapwrangler.ui;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.Pixmap;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Button;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;
import com.badlogic.gdx.scenes.scene2d.utils.ChangeListener;
import com.badlogic.gdx.utils.viewport.Viewport;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

import io.peter.wrapwrangler.WW;
import io.peter.wrapwrangler.assets.actors.Player;
import io.peter.wrapwrangler.screens.Level;

public class UI {



    Button debugReset;

    Button leftBtn;

    Button rightBtn;

    Button jumpBtn;

    static Label scoreLabel;

    static int score;

    Skin skin;

    public Stage stage;

    public float btnScale;

    Table table;

    static float PPM;
    public UI(Viewport viewport, SpriteBatch spriteBatch, WW game) {
        score = 0;
        PPM = Level.PPM;
        btnScale = 40f;

        //https://libgdx.com/wiki/graphics/2d/scene2d/scene2d
        stage = new Stage(viewport, spriteBatch);
        Gdx.input.setInputProcessor(stage);
        table = new Table();
        table.left().bottom();
        table.pad(10f/PPM);
        table.setFillParent(true);

        stage.addActor(table);

        skin = new Skin(Gdx.files.internal("ui/uiskin.json"));


        Pixmap pixmap = new Pixmap(1, 1, Pixmap.Format.RGB565);
        pixmap.setColor(Color.WHITE);
        pixmap.fill();
        skin.add("white", new Texture(pixmap));
        skin.add("default", new BitmapFont());

        table.setSkin(skin);

        //Buttons
        TextButton.TextButtonStyle buttonStyle = new TextButton.TextButtonStyle();
        buttonStyle.up = skin.newDrawable("white", Color.RED);
        buttonStyle.down = skin.newDrawable("white", Color.FOREST);
        buttonStyle.checked = skin.newDrawable("white", Color.RED);
        buttonStyle.over = skin.newDrawable("white", Color.CORAL);

        skin.add("default", buttonStyle);

        leftBtn = new Button(skin);
        leftBtn.setStyle(buttonStyle);

        rightBtn = new Button(skin);
        rightBtn.setStyle(buttonStyle);

        jumpBtn = new Button(skin);
        jumpBtn.setStyle(buttonStyle);

        debugReset = new Button(skin);
        debugReset.setStyle(buttonStyle);

        scoreLabel = new Label("Score: 0", skin);


        table.add(leftBtn).width(btnScale/PPM).height(btnScale/PPM).pad(20f/PPM);
        table.add(rightBtn).width(btnScale/PPM).height(btnScale/PPM).pad(20f/PPM);
        table.add(jumpBtn).width(btnScale/PPM).height(btnScale/PPM).pad(0f,200f/PPM, 0f, 0f);
        //table.add(debugReset).width(btnScale/PPM).height(btnScale/PPM).pad(0f, 100f/PPM,0f, 0f);
        table.add(scoreLabel).maxWidth(100f).height(20f/PPM).pad(0f, 50f, 0f, 0f);


        leftBtn.setVisible(true);
        rightBtn.setVisible(true);
        jumpBtn.setVisible(true);

        //NOT NECESSARY CURRENTLY!
        leftBtn.addListener(new ChangeListener() {
            @Override
            public void changed(ChangeEvent event, Actor actor) {
                Gdx.app.log("PETER","left button clicked!");
            }
        });

        rightBtn.addListener(new ChangeListener() {
            @Override
            public void changed(ChangeEvent event, Actor actor) {
                Gdx.app.log("PETER","right button clicked!");
            }
        });

        //debugSetup();
    }

    private void debugSetup() {
        table.setDebug(true);
    }

    public Button getButtonLeft(){
        return leftBtn;
    }

    public Button getButtonRight(){
        return rightBtn;
    }

    public Button getButtonJump(){
        return jumpBtn;
    }

    public Button getDebugReset() {
        return debugReset;
    }

    public void drawUi(Player player){
        stage.act(Gdx.graphics.getDeltaTime());

        stage.getRoot().setX(player.getPos().x - stage.getWidth()/2);
        stage.getRoot().setY(player.getPos().y > 0 ? player.getPos().y - stage.getHeight()/2 : 0 - stage.getHeight()/2) ;
        stage.draw();
    }

    public static void updateScore(){

        score += 1;

        scoreLabel.setText( "Score: " + score);
    }

    public static int getScore(){
        return score;
    }



}
