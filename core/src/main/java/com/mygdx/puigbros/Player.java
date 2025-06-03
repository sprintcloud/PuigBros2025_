package com.mygdx.puigbros;

import com.badlogic.gdx.assets.AssetManager;
import com.badlogic.gdx.audio.Sound;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;

public class Player extends WalkingCharacter
{

    static final float JUMP_IMPULSE = -600f;
    static final float RUN_SPEED = 240f;
    static final float BRAKE_SPEED = 120f;
    static final float STOP_SPEED = 5f;
    static final float RUN_ACCELERATION = 200f;
    static final float INVULNERABILITY_DURATION = 20f;

    AssetManager manager;
    ButtonLayout joypad;

    Texture currentFrame;

    float animationFrame = 0;
    float invulnerability = 0f;
    boolean isOnGround;
    boolean leftPressed;   // 键盘左键按下状态
    boolean rightPressed;  // 键盘右键按下状态
    boolean jumpPressed;   // 键盘跳跃键按下状态

    int bulletCount = 0;
    static final int MAX_BULLETS = 10;
    boolean isAttacking = false;

    public Player(AssetManager manager)
    {
        setBounds(400,40,48, 112);
        this.manager = manager;
        currentFrame = manager.get("player/Idle (1).png", Texture.class);
        invulnerability = 0.f;
    }

    public void setJoypad(ButtonLayout joypad) {
        this.joypad = joypad;
    }
    @Override
    public void act(float delta) {
        super.act(delta);

        // Fall too low
        if(getY() > map.height * TileMap.TILE_SIZE)
        {
            kill();
        }

        // Left bounds of the level
        if(getX() < getWidth() / 2)
        {
            setX(getWidth() / 2);
        }

        updateGroundState();

        if(dead)
        {
            // Death animation
            animationFrame += 10.f*delta;
            int frameTexture = (int)animationFrame+1;
            if(frameTexture > 10)
                frameTexture = 10;
            currentFrame = manager.get("player/Dead ("+frameTexture+").png", Texture.class);

            speed.x = 0f;
        }
        else
        {
            boolean useKeyboardLeft = leftPressed || joypad.isPressed("Left");   // 键盘左键或摇杆左
            boolean useKeyboardRight = rightPressed || joypad.isPressed("Right");// 键盘右键或摇杆右
            boolean useKeyboardJump = jumpPressed && isOnGround;

            if(invulnerability > 0.f)
                invulnerability -= delta;

            if(falling)
            {
                if(speed.y < 0)
                {
                    // Start jumping
                    float base_impulse = -JUMP_IMPULSE;
                    float current_impulse = -speed.y;
                    animationFrame = 0 + ((base_impulse - current_impulse) / 32);
                    if (animationFrame > 8) animationFrame = 8;
                }
                else
                {
                    // Start falling
                    animationFrame = 9 + (speed.y / 64);
                    if (animationFrame > 11) animationFrame = 11;
                }
                currentFrame = manager.get("player/Jump ("+(int)(animationFrame+1)+").png", Texture.class);

            }
            else if((speed.x < 0.1f && speed.x > -0.1f))
            {
                // Idle
                animationFrame += 10 * delta;
                if (animationFrame >= 10.f) animationFrame -= 10.f;
                currentFrame = manager.get("player/Idle ("+(int)(animationFrame+1)+").png", Texture.class);

            }
            else
            {
                // Walk
                animationFrame += 10 * delta;
                if (animationFrame >= 8.f) animationFrame -= 8.f;
                currentFrame = manager.get("player/Run ("+(int)(animationFrame+1)+").png", Texture.class);

            }

            if (!falling && useKeyboardJump) {
                jump(1.f);
                manager.get("sound/jump.wav", Sound.class).play();
            }

            if(!falling)
            {
                // On the ground
                if (useKeyboardLeft)
                {
                    // Accelerate right
                    lookLeft = true;
                    speed.x -= RUN_ACCELERATION * delta;
                    if (speed.x < -RUN_SPEED) speed.x = -RUN_SPEED;
                }
                else if (useKeyboardRight)
                {
                    // Accelerate left
                    lookLeft = false;
                    speed.x += RUN_ACCELERATION * delta;
                    if (speed.x > RUN_SPEED) speed.x = RUN_SPEED;
                }
                else
                {
                    // Reduce speed and stop
                    if(speed.x < 0f)
                    {
                        if(speed.x < -STOP_SPEED) {
                            speed.x += delta * BRAKE_SPEED;
                        }
                        else
                        {
                            speed.x = 0f;
                        }
                    }
                    else if (speed.x > 0f)
                    {
                        if(speed.x > STOP_SPEED) {
                            speed.x -= delta * BRAKE_SPEED;
                        }
                        else
                        {
                            speed.x = 0f;
                        }
                    }
                }
            }
        }
    }

    public void jump(float strength)
    {
        speed.y = JUMP_IMPULSE * strength;

        /*if(joypad.isPressed("Right"))
        {
            speed.x = RUN_SPEED * 05f;
        }
        else if (joypad.isPressed("Left"))
        {
            speed.x = -RUN_SPEED * 05f;
        }*/
    }

    @Override
    public void kill()
    {
        if(!dead) {
            super.kill();
            animationFrame = 0;
        }
    }

    public void getInvulnerability()
    {
        invulnerability = INVULNERABILITY_DURATION;
    }

    public boolean hasInvulnerability()
    {
        return invulnerability > 0.f;
    }

    float getAnimationFrame()
    {
        return animationFrame;
    }

    @Override
    public void draw(Batch batch, float parentAlpha) {
        super.draw(batch, parentAlpha);

        // Blink effect when invulnerable
        if(invulnerability > 0.f && (int)(invulnerability/0.125f)%2 == 0)
            return;

        batch.draw(currentFrame, getX() - getWidth()*0.5f - map.scrollX - (lookLeft ? 28 : 50), getY() - getHeight()*0.5f, 128, 128, 0, 0, 669, 569, lookLeft, true);
    }

    // Draw collision box
    public void drawDebug(ShapeRenderer shapes) {
        //super.drawDebug(shapes);

        shapes.begin(ShapeRenderer.ShapeType.Filled);
        shapes.setColor(Color.NAVY);
        shapes.rect(getX() - getWidth()*0.5f - map.scrollX, getY() - getHeight()*0.5f, getWidth(), getHeight());
        shapes.end();
    }

    public void updateGroundState() {
        float bottomY = getY() + getHeight() / 2f;
        int mapX = (int) (getX() / TileMap.TILE_SIZE);
        mapX = Math.max(0, Math.min(mapX, TileMap.width - 1));
        int floorY = TileMap.nearestFloor((int) (getX() - getWidth() / 2), (int) bottomY);
        isOnGround = (floorY <= bottomY);
    }
}
