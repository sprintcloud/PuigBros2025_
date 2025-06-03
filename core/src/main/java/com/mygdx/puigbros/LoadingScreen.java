package com.mygdx.puigbros;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.assets.AssetDescriptor;
import com.badlogic.gdx.assets.AssetErrorListener;
import com.badlogic.gdx.assets.AssetManager;
import com.badlogic.gdx.assets.loaders.FileHandleResolver;
import com.badlogic.gdx.assets.loaders.TextureLoader;
import com.badlogic.gdx.assets.loaders.resolvers.InternalFileHandleResolver;
import com.badlogic.gdx.audio.Music;
import com.badlogic.gdx.audio.Sound;
import com.badlogic.gdx.files.FileHandle;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.GlyphLayout;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.utils.Array;
import com.badlogic.gdx.utils.Json;
import com.badlogic.gdx.utils.JsonReader;
import com.badlogic.gdx.utils.JsonValue;
import com.badlogic.gdx.utils.viewport.FillViewport;
import com.badlogic.gdx.utils.viewport.Viewport;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

public class LoadingScreen implements Screen, AssetErrorListener {

    PuigBros game;
    AssetManager assetManager;
    Viewport viewport;
    SpriteBatch batch;
    ShapeRenderer shapeRenderer;
    BitmapFont bigFont, mediumFont;
    private GlyphLayout glyphLayout;
    float progress;
    boolean loadingComplete;
    Json json;
    LoadingScreen(PuigBros game)
    {
        this.game = game;
        this.assetManager = new AssetManager();
        this.assetManager.setErrorListener(this);
        this.viewport = new FillViewport(800, 480, new OrthographicCamera());
        this.batch = new SpriteBatch();
        this.shapeRenderer = new ShapeRenderer();
        this.bigFont = new BitmapFont();
        this.glyphLayout = new GlyphLayout();
        this.mediumFont = new BitmapFont();
        this.json = new Json();

        loadConfig();
        loadAssets();

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

        manager.load("bulltes/bullet.png", Texture.class);
        manager.load("bulltes/bullet box.png", Texture.class);


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

        // Sounds
        manager.load("sound/music.mp3", Music.class);
        manager.load("sound/loselife.wav", Sound.class);
        manager.load("sound/kill.wav", Sound.class);
        manager.load("sound/jump.wav", Sound.class);
        manager.load("sound/powerup.wav", Sound.class);
        manager.load("sound/levelcomplete.wav", Sound.class);

    }

    private void loadAssets() {
        assetManager.load("resources.json", JsonValue.class);
    }

    @Override
    public void show() {
    }

    @Override
    public void render(float delta) {

    }

    @Override
    public void resize(int width, int height) {
        viewport.update(width, height, true);

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
        batch.dispose();
        shapeRenderer.dispose();
        bigFont.dispose();
        mediumFont.dispose();
    }

    public void update(float delta){
        assetManager.update();
        progress = calculateProgress();

        if (assetManager.isFinished() && !loadingComplete){
            loadingComplete = true;
            game.setScreen(new MainMenuScreen(game));
            dispose();
        }
    }

    private float calculateProgress() {
        Array<String> allAssets = new Array<>(assetManager.getAssetNames());
        int totalAssets = allAssets.size;

        if (totalAssets == 0) return 1f; // 无资源需要加载

        Array<String> loadedAssets = new Array<>(assetManager.getLoadedAssets());
        int loadedCount = loadedAssets.size;

        return Math.min((float) loadedCount / totalAssets, 1f); // 计算进度（0-1）
    }

    private void draw() {
        Gdx.gl.glClearColor(0, 0, 0, 1);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);

        viewport.apply();
        batch.setProjectionMatrix(viewport.getCamera().combined);
        shapeRenderer.setProjectionMatrix(viewport.getCamera().combined);

        // 绘制背景（从已加载的资源中获取）
        Texture bg = assetManager.get("BG.png", Texture.class);
        batch.begin();
        batch.draw(bg, 0, 0, viewport.getWorldWidth(), viewport.getWorldHeight());
        batch.end();

        // 绘制进度条和文字
        drawProgressBar();
        drawProgressText();
    }

    private void drawProgressBar() {
        float progressBarWidth = 600;
        float progressBarHeight = 60;
        float progressX = viewport.getWorldWidth() / 2 - progressBarWidth / 2;
        float progressY = viewport.getWorldHeight() / 2 - progressBarHeight / 2;

        // 进度条背景
        shapeRenderer.setColor(Color.DARK_GRAY);
        shapeRenderer.rect(progressX, progressY, progressBarWidth, progressBarHeight);

        // 进度填充
        float progressAmount = progressBarWidth * progress;
        shapeRenderer.setColor(Color.GREEN);
        shapeRenderer.rect(progressX, progressY, progressAmount, progressBarHeight);
    }

    private void drawProgressText() {
        String text = "LOADING... " + (int) (progress * 100) + "%";

        // 使用 GlyphLayout 测量文本尺寸（修正后的逻辑）
        glyphLayout.setText(bigFont, text); // 必须先设置文本
        float textWidth = glyphLayout.width; // 直接获取宽度（无需通过 Rectangle）
        float textHeight = glyphLayout.height; // 获取高度

        // 计算文本居中位置
        float textX = viewport.getWorldWidth() / 2 - textWidth / 2;
        float textY = viewport.getWorldHeight() - 50;

        bigFont.draw(batch, text, textX, textY);
    }

    @Override
    public void error(AssetDescriptor asset, Throwable throwable) {
        Gdx.app.error("Asset Loading Error", "Failed to load: " + asset.fileName, throwable);
        game.setScreen(new MainMenuScreen(game));
        dispose();
    }

    private void loadConfig() {
        JsonValue config = json.fromJson(JsonValue.class,
            Gdx.files.internal("resources.json"));

        JsonValue textures = config.get("textures");
        if (textures != null && textures.isObject()){
            for (String key : textures.asStringArray()){
                JsonValue value = textures.get(key);
                if (value.isString()){
                    assetManager.load(value.asString(), Texture.class);
                } else if (value.isObject()) {
                    parseObject(value, key);
                }

            }
        }

        JsonValue sounds = config.get("sounds");
        if (sounds != null && sounds.isObject()){
            for (String key : sounds.asStringArray()){
                JsonValue value = sounds.get(key);
                if (value.isString()){
                    String path = value.asString();
                    if (key.equals("music")){
                        assetManager.load(path, Music.class);
                    }else {
                        assetManager.load(path, Sound.class);
                    }
                }
            }
        }
    }

    private void parseObject(JsonValue object, String parentKey){
        for (String key : object.asStringArray()){
            JsonValue value = object.get(key);
            if(value.isArray()){
                parseArray(value, parentKey + "/" + key);
            } else if (value.isObject()) {
                parseObject(value, parentKey + "/" + key);
            }
        }
    }

    private void parseArray(JsonValue array, String basePath){
        for (int i = 0; i < array.size; i++){
            JsonValue item = array.get(i);
            if (item.isString()){
                String pathTemplate = item.asString();

                String path = pathTemplate.replace("{index}", String.format("%03d", i + 1));
                assetManager.load(path, Texture.class);
            }
        }
    }
}
