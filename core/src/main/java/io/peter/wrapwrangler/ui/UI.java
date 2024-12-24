package io.peter.wrapwrangler.ui;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.Pixmap;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Button;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;
import com.badlogic.gdx.scenes.scene2d.utils.ChangeListener;
import com.badlogic.gdx.utils.viewport.Viewport;

public class UI {

    Button leftBtn;

    Button rightBtn;

    Button jumpBtn;

    Skin skin;

    Stage stage;

    Table table;

    public void uiSetup(Viewport viewport) {
        //a stage is an area to draw UI elements, a stage contains a table,
        stage = new Stage(viewport);

        //usually, the table takes up the entire stage, but a stage can contain multiple tables
        //https://libgdx.com/wiki/graphics/2d/scene2d/scene2d#stage
        Gdx.input.setInputProcessor(stage);
        //https://libgdx.com/wiki/graphics/2d/scene2d/table
        table = new Table();

        table.left().bottom();

        table.pad(10f);


        //set the table to fill its parent and add it to the stage (parent)
        table.setFillParent(true);
        stage.addActor(table);

        //A skin is a handy way to store all the information to apply to the UI elements
        //a skin can also be loaded via JSON (possibly hyperlap2d?)
        //https://libgdx.com/wiki/graphics/2d/scene2d/skin
        skin = new Skin(Gdx.files.internal("ui/uiskin.json"));

        //create a white colour pixmap
        Pixmap pixmap = new Pixmap(1, 1, Pixmap.Format.RGB565);
        pixmap.setColor(Color.WHITE);
        pixmap.fill();

        //adds a white colour pixmap to the skin
        skin.add("white", new Texture(pixmap));

        //adds a default font to the skin
        skin.add("defualt", new BitmapFont());

        TextButton.TextButtonStyle buttonStyle = new TextButton.TextButtonStyle();
        buttonStyle.up = skin.newDrawable("white", Color.SLATE);
        buttonStyle.down = skin.newDrawable("white", Color.RED);
        buttonStyle.checked = skin.newDrawable("white", Color.FOREST);
        buttonStyle.over = skin.newDrawable("white", Color.CORAL);

        skin.add("default", buttonStyle);

        leftBtn = new Button(skin);
        rightBtn = new Button(skin);
        jumpBtn = new Button(skin);

        table.add(leftBtn).width(10f).height(10f).pad(2.5f);
        table.add(rightBtn).width(10f).height(10f).pad(2.5f);
        table.add(jumpBtn).width(10f).height(10f).pad(0f,25f, 2.5f, 0f);

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

        debugSetup();
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

    public void drawUi(){
        stage.act(Gdx.graphics.getDeltaTime());
        stage.draw();
    }

}
