package com.mygdx.puigbros;

import com.badlogic.gdx.assets.AssetManager;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.mygdx.puigbros.jsonloaders.LevelJson;

public class TileMap {

    public static final int TILE_SIZE = 64;
    public static int width;
    static int height;
    static byte[][] tiles;
    AssetManager manager;
    SpriteBatch batch;

    public int scrollX;

    public TileMap(AssetManager manager, SpriteBatch batch)
    {
        this.manager = manager;
        this.batch = batch;
    }

    void loadFromLevel(LevelJson l)
    {
        // Load from json file
        width = l.getMapWidth();
        height = l.getMapHeight();

        tiles = new byte[height][];

        for(int i = 0; i < height; i++)
        {
            tiles[i] = new byte[width];
        }

        for(int i = 0; i < height; i++) {
            for (int j = 0; j < width; j++) {
                tiles[i][j] = l.getTileMap()[i][j];
            }
        }
    }

    // Old render with color squares
    public void render(ShapeRenderer shapeRenderer) {

        shapeRenderer.begin(ShapeRenderer.ShapeType.Filled);
        for(int j = 0; j < height; j++)
            for(int i = 0; i < width; i++)
            {
                if(tiles[j][i] != 0)
                {
                    shapeRenderer.setColor(Color.OLIVE);
                    shapeRenderer.rect(TILE_SIZE * i - scrollX, TILE_SIZE * j, TILE_SIZE, TILE_SIZE);
                    shapeRenderer.setColor(Color.FIREBRICK);
                    shapeRenderer.rect(TILE_SIZE * i + 6 - scrollX, TILE_SIZE * j + 6, TILE_SIZE - 12, TILE_SIZE - 12);
                }
            }
        shapeRenderer.end();
    }

    public void render()
    {

        batch.begin();

        Texture bgTexture = manager.get("BG.png", Texture.class);

        // Parallax scroll
        int bgWidth = bgTexture.getWidth();
        int bgHeight = bgTexture.getHeight();
        int scrollXPos = 0 - ((scrollX/2) % bgWidth);

        batch.draw(bgTexture, scrollXPos, 0, bgWidth, bgHeight, 0, 0, bgWidth, bgHeight, false, true);
        batch.draw(bgTexture, scrollXPos + bgWidth, 0, bgWidth, bgHeight, 0, 0, bgWidth, bgHeight, false, true);

        // Tile map
        for(int j = 0; j < height; j++)
            for(int i = 0; i < width; i++)
            {
                if(tiles[j][i] != 0)
                {
                    batch.draw(manager.get("tiles/"+tiles[j][i]+".png", Texture.class), TILE_SIZE * i - scrollX, TILE_SIZE * j, TILE_SIZE, TILE_SIZE, 0, 0, 128, 128, false, true);
                }
            }
        batch.end();
    }

    boolean isSolid(int x, int y)
    {
        int mapX = x / TILE_SIZE;
        int mapY = y / TILE_SIZE;

        if(mapX < 0) mapX = 0;
        if(mapY < 0) mapY = 0;
        if(mapX >= width) mapX = width - 1;
        if(mapY >= height) mapY = height - 1;

        return tiles[mapY][mapX] != 0 && tiles[mapY][mapX] < 17;
    }

    public static int nearestFloor(int x, int y)
    {
        int mapX = x / TILE_SIZE;
        int mapY = y / TILE_SIZE;

        if(mapX < 0) mapX = 0;
        if(mapY < 0) mapY = 0;
        if(mapX >= width) mapX = width - 1;
        if(mapY >= height) mapY = height - 1;

        while(mapY < height && (tiles[mapY][mapX] == 0 || tiles[mapY][mapX] >= 17))
        {
            mapY++;
        }

        if(mapY >= height)
        {
            return 9999;
        }
        else
        {
            return mapY * TILE_SIZE;
        }
    }

    int nearestCeiling(int x, int y)
    {
        int mapX = x / TILE_SIZE;
        int mapY = y / TILE_SIZE;

        if(mapX < 0) mapX = 0;
        if(mapY < 0) mapY = 0;
        if(mapX >= width) mapX = width - 1;
        if(mapY >= height) mapY = height - 1;

        while(mapY >= 0 && (tiles[mapY][mapX] == 0 || tiles[mapY][mapX] >= 17))
        {
            mapY--;
        }

        if(mapY < 0)
        {
            return -9999;
        }
        else
        {
            return ((mapY + 1) * TILE_SIZE) - 1;
        }
    }
}
