package com.mygdx.puigbros;

import com.badlogic.gdx.Screen;
import com.badlogic.gdx.assets.AssetManager;
import com.badlogic.gdx.audio.Music;
import com.badlogic.gdx.audio.Sound;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.utils.ScreenUtils;

public class LoadingScreen implements Screen {

    PuigBros game;
    float loadProgress;

    LoadingScreen(PuigBros game)
    {
        this.game = game;
        AssetManager  manager = game.manager;

        // Add assets for loading

        // Tiles
        for(int i = 1; i < 19; i++)
            manager.load("tiles/"+i+".png", Texture.class);

        // Background image
        manager.load("BG.png", Texture.class);

        //GUI
        manager.load("gui/Button-off.png", Texture.class);
        manager.load("gui/Button-on.png", Texture.class);
        manager.load("gui/Left-off.png", Texture.class);
        manager.load("gui/Left-on.png", Texture.class);
        manager.load("gui/Right-off.png", Texture.class);
        manager.load("gui/Right-on.png", Texture.class);
        manager.load("gui/Jump-off.png", Texture.class);
        manager.load("gui/Jump-on.png", Texture.class);
        manager.load("gui/Pause-off.png", Texture.class);
        manager.load("gui/Pause-on.png", Texture.class);
        manager.load("gui/fire_bullets_off.png", Texture.class);
        manager.load("gui/fire_bullets_on.png", Texture.class);
        manager.load("bullets/bullet (1).png", Texture.class);


        // Player
        for (int i = 0; i < 10; i++)
        {
            manager.load("player/Idle (" +(i+1)+").png", Texture.class);
        }
        for (int i = 0; i < 8; i++)
        {
            manager.load("player/Run (" +(i+1)+").png", Texture.class);
        }
        for (int i = 0; i < 12; i++)
        {
            manager.load("player/Jump (" +(i+1)+").png", Texture.class);
        }
        for (int i = 0; i < 10; i++)
        {
            manager.load("player/Dead (" +(i+1)+").png", Texture.class);
        }
        for (int i = 0; i < 5; i++){
            manager.load("player/Slide (" + (i+1) + ").png", Texture.class);
        }

        //Dino
        for (int i = 0; i < 10; i++)
        {
            manager.load("dino/Walk (" +(i+1)+").png", Texture.class);
        }
        for (int i = 0; i < 8; i++)
        {
            manager.load("dino/Dead (" +(i+1)+").png", Texture.class);
        }

        //PowerUp
        for (int i = 0; i < 7; i++)
        {
            manager.load("powerup/frame000" +i+".png", Texture.class);
        }

        //Bullets
        for (int i = 1; i < 3; i++){
            manager.load("bullets/bullet (" + i + ").png", Texture.class);
        }

        // Sounds
        manager.load("sound/music.mp3", Music.class);
        manager.load("sound/loselife.wav", Sound.class);
        manager.load("sound/kill.wav", Sound.class);
        manager.load("sound/jump.wav", Sound.class);
        manager.load("sound/powerup.wav", Sound.class);
        manager.load("sound/levelcomplete.wav", Sound.class);

        loadProgress = 0f;
    }

    @Override
    public void show() {

    }

    @Override
    public void render(float delta) {

        // Render step =============================================
        float currentLoadProgress = game.manager.getProgress();
        if(currentLoadProgress > loadProgress + 0.05f)
        {
            loadProgress = currentLoadProgress;

            game.camera.update();
            game.batch.setProjectionMatrix(game.camera.combined);
            game.textBatch.setProjectionMatrix(game.textCamera.combined);
            game.shapeRenderer.setProjectionMatrix(game.camera.combined);

            ScreenUtils.clear(Color.BLACK);

            // Progress bar
            game.shapeRenderer.begin(ShapeRenderer.ShapeType.Filled);
            game.shapeRenderer.setColor(Color.YELLOW);
            game.shapeRenderer.rect(90, 290, 620, 100);
            game.shapeRenderer.setColor(Color.BLACK);
            game.shapeRenderer.rect(100, 300, 600, 80);
            game.shapeRenderer.setColor(Color.ORANGE);
            game.shapeRenderer.rect(110, 310, 580 * loadProgress, 60);
            game.shapeRenderer.end();

            game.textBatch.begin();
            game.bigFont.draw(game.textBatch, "CARREGANT...", 120, 340);
            game.mediumFont.draw(game.textBatch, (int) (loadProgress * 100.f) + "%", 360, 160);
            game.textBatch.end();

        }

        // Update step ====================================
        if(game.manager.update())
        {
            game.setScreen(new MainMenuScreen(game));
            this.dispose();
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
