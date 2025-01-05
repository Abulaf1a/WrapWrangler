package io.peter.wrapwrangler.screens;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.Pixmap;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.utils.ScreenUtils;

import io.peter.wrapwrangler.WW;

public class TextScreen implements Screen {

    WW game;

    Stage stage;

    Table table;

    Label label;

    public TextScreen(WW game, String message){
        this.game = game;

        stage = new Stage(game.viewport, game.spriteBatch);

        Gdx.input.setInputProcessor(stage);

        table = new Table();

        table.center().top();

        table.pad(10f/ WW.PPM);

        table.setFillParent(true);

        stage.addActor(table);

        Skin skin = new Skin(Gdx.files.internal("ui/uiskin.json"));

        Pixmap pixmap = new Pixmap(1, 1, Pixmap.Format.RGB565);
        pixmap.setColor(Color.WHITE);
        pixmap.fill();
        skin.add("white", new Texture(pixmap));
        skin.add("new skin", game.font);

        table.setSkin(skin);

        label = new Label(message, skin);

        label.setVisible(true);

        table.add(label).width(200f).height(100f).pad(300f);

        label.setFillParent(true);

    }

    @Override
    public void show() {

    }

    public void render(float delta) {

        ScreenUtils.clear(Color.SLATE);

        game.viewport.apply();

        stage.act(Gdx.graphics.getDeltaTime());
        stage.draw();

        input();
    }

    public void input(){
        if(Gdx.input.isTouched()){
            game.setScreen(new Level(game));
        }
    }

    @Override
    public void resize(int width, int height) {

    }

    @Override
    public void pause() {

    }

    @Override
    public void resume() {

    }

    @Override
    public void hide() {

    }

    @Override
    public void dispose() {

    }
}
