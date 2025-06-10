package com.mygdx.puigbros;

import static com.badlogic.gdx.graphics.g3d.particles.ParticleChannels.TextureRegion;

import com.badlogic.gdx.assets.AssetManager;
import com.badlogic.gdx.audio.Sound;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.Pixmap;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.scenes.scene2d.Actor;

import java.util.Random;

public class Dino extends WalkingCharacter
{

    static final float RUN_SPEED = 120f;
    static final float RUN_ACCELERATION = 200f;
    static final float DISCOVER_DISTANCE = 500f;
    private static final Random random = new Random();
    AssetManager manager;
    Texture currentFrame, whitePixelTexture;
    Player player;
    boolean discovered;
    float animationFrame = 0;
    int health = 100;
    float healthBarWidth = 120f;
    float healthBarHeight = 15f;
    public Dino(int x, int y, AssetManager manager, Player player)
    {
        setBounds(x,y,64, 116);
        this.manager = manager;
        this.player = player;
        currentFrame = manager.get("dino/Walk (1).png", Texture.class);
        lookLeft = true;
        discovered = false;

        Pixmap pixmap = new Pixmap(1, 1, Pixmap.Format.RGBA8888);
        pixmap.setColor(0.2f, 0.8f, 0.2f, 1f);
        pixmap.fill();
        whitePixelTexture = new Texture(pixmap);
        pixmap.dispose();
    }

    @Override
    public void act(float delta) {

        if(Math.abs(player.getX() - getX()) < DISCOVER_DISTANCE) discovered = true;
        if(!discovered) return;

        super.act(delta);

        if (!dead && !falling) {
            for (Actor actor : getStage().getActors()) {
                if (actor instanceof Bullet) {
                    Bullet bullet = (Bullet) actor;

                    float dinoLeft = getX() - getWidth() / 2f;
                    float dinoBottom = getY() - getHeight() / 2f;
                    Rectangle dinoRect = new Rectangle(dinoLeft, dinoBottom, getWidth(), getHeight());

                    float bulletCenterX = bullet.getWorldX();
                    float bulletCenterY = bullet.getWorldY();
                    float bulletRadius = 16f;
                    Rectangle bulletRect = new Rectangle(
                        bulletCenterX - bulletRadius,
                        bulletCenterY - bulletRadius,
                        bulletRadius * 2f,
                        bulletRadius * 2f
                    );

                    if (dinoRect.overlaps(bulletRect)) {
                        int damage = random.nextInt(20) + 20;
                        takeDamage(damage);
                        bullet.remove();
                        break;
                    }
                }
            }
        }
        if(dead)
        {
            animationFrame += delta * 6.f;
            int textureFrame = (int) animationFrame+1;
            if(textureFrame >= 9)
                textureFrame = 8;
            currentFrame = manager.get("dino/Dead ("+textureFrame+").png", Texture.class);

            speed.x = 0f;

            if(animationFrame >= 15)
            {
                this.remove();
            }

        }
        else if(!falling)
        {
            // Walk animation
            animationFrame += delta * 6.f;
            if(animationFrame >= 10.f)
                animationFrame -= 10.f;
            currentFrame = manager.get("dino/Walk ("+(int)(animationFrame+1)+").png", Texture.class);

            if(!lookLeft)
            {
                // Accelerate right
                speed.x += RUN_ACCELERATION * delta;
                if(speed.x > RUN_SPEED)
                {
                    speed.x = RUN_SPEED;
                }

                // Collided with a wall
                if(map.isSolid((int)(getX() + getWidth()/2 + delta * speed.x), (int)(getY() - getHeight()*0.25f)) ||
                    map.isSolid((int)(getX() + getWidth()/2 + delta * speed.x), (int)(getY() + getHeight()*0.25f)) )
                {
                    lookLeft = true;
                }
            }
            else
            {
                // Accelerate left
                speed.x -= RUN_ACCELERATION * delta;
                if(speed.x < -RUN_SPEED)
                {
                    speed.x = -RUN_SPEED;
                }

                // Collided with a wall
                if(map.isSolid((int)(getX() - getWidth()/2 + delta * speed.x), (int)(getY() - getHeight()*0.25f)) ||
                    map.isSolid((int)(getX() - getWidth()/2 + delta * speed.x), (int)(getY() + getHeight()*0.25f)))
                {
                    lookLeft = false;
                }
            }
        }
        else
        {
            // Frame for falling
            currentFrame = manager.get("dino/Walk (1).png", Texture.class);
        }
    }

    @Override
    public void kill() {
        super.kill();
        animationFrame = 0f;
        manager.get("sound/kill.wav", Sound.class).play();
    }

    @Override
    public void draw(Batch batch, float parentAlpha) {
        super.draw(batch, parentAlpha);

        float healthPercent = Math.max(0, (float) health / 100f);

        float barX = getX() - healthBarWidth / 2 - map.scrollX;
        float barY = getY() + getHeight() / 2 + 20;

        batch.setColor(0.3f, 0.3f, 0.3f, 0.8f);
        batch.draw(
            whitePixelTexture,
            barX, barY,
            healthBarWidth, healthBarHeight,
            0,0,1,1,
            false,false
        );

        batch.setColor(0.2f, 0.8f, 0.2f, 0.9f);
        batch.draw(
            whitePixelTexture, // 或使用占位纹理
            barX, barY,
            healthBarWidth * healthPercent, healthBarHeight,
            0,0,1,1,
            false,false
        );

        batch.setColor(1f, 1f, 1f, 1f);

        batch.draw(currentFrame, getX() - getWidth()*0.5f - map.scrollX - (lookLeft ? 44 : 12), getY() - getHeight()*0.5f, 128, 128, 0, 0, 680, 472, lookLeft, true);
    }

    // Draw collision box
    @Override
    public void drawDebug(ShapeRenderer shapes) {
        //super.drawDebug(shapes);
        shapes.begin(ShapeRenderer.ShapeType.Filled);
        shapes.setColor(Color.GREEN);
        shapes.rect(getX() - getWidth()*0.5f - map.scrollX, getY() - getHeight()*0.5f, getWidth(), getHeight());
        shapes.end();
    }

    public void takeDamage(int damage){
        health -= damage;
        if (health <= 0){
            health = 0;
            kill();
        }
    }

    @Override
    public boolean remove() {
        whitePixelTexture.dispose();
        return super.remove();
    }
}
