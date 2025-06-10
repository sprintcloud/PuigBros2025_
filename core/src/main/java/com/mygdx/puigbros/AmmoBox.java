package com.mygdx.puigbros;

import com.badlogic.gdx.assets.AssetManager;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.scenes.scene2d.Actor;

public class AmmoBox extends Actor {
    private int MIN_AMMO;
    private int MAX_AMMO;
    private AssetManager manager;
    private TileMap map;

    public AmmoBox(float x, float y, AssetManager manager, int[] count){
        setBounds(x, y, 32, 32);
        MIN_AMMO = count[0];
        MAX_AMMO = count[1];
        this.manager = manager;
    }

    @Override
    public void draw(Batch batch, float parentAlpha) {
        super.draw(batch, parentAlpha);
        batch.draw(
            manager.get("bullets/bullet (2).png", Texture.class),
            getX() - getWidth() * 0.5f - map.scrollX,
            getY() - getHeight() * 0.5f,
            32, 32,
            0, 0,
            200, 196, false, true
        );

    }

    public int getAmmoCount(){
        return MIN_AMMO + (int) (Math.random() * (MAX_AMMO - MIN_AMMO + 1));
    }
    public void setMap(TileMap map) {
        this.map = map;
    }
}
