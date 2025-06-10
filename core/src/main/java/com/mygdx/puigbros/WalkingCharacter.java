package com.mygdx.puigbros;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.scenes.scene2d.Actor;

import java.awt.Rectangle;

public class WalkingCharacter extends GameEntity {

    static final float GRAVITY = 980.f;

    protected boolean falling;
    protected boolean dead;
    protected boolean lookLeft;

    public WalkingCharacter()
    {
        super();
        falling = false;
        dead = false;
    }

    @Override
    public void act(float delta) {

        int nearestFloor1 = map.nearestFloor((int)(getX() - getWidth()/2), (int)getY());
        int nearestFloor2 = map.nearestFloor((int)(getX() + getWidth()/2), (int)getY());

        if(falling)
        {
            if(speed.y > 0) {
                if (nearestFloor1 <= getY() + getHeight() / 2 && speed.y > 0) {
                    falling = false;
                    speed.y = 0;
                    setY(nearestFloor1 - getHeight() / 2);
                } else if (nearestFloor2 <= getY() + getHeight() / 2 && speed.y > 0) {
                    falling = false;
                    speed.y = 0;
                    setY(nearestFloor2 - getHeight() / 2);
                }
                else
                {
                    // Keep falling
                    speed.y += delta * GRAVITY;
                }
            }
            else if (speed.y < 0)
            {
                int nearestCeiling1 = map.nearestCeiling((int)(getX() - getWidth()/2), (int)getY());
                int nearestCeiling2 = map.nearestCeiling((int)(getX() + getWidth()/2), (int)getY());

                if(nearestCeiling1 > getY() - getHeight()/2 || nearestCeiling2 > getY() - getHeight()/2)
                {
                    speed.y = -speed.y;
                }
                else
                {
                    // Keep falling
                    speed.y += delta * GRAVITY;
                }
            }
            else
            {
                // Keep falling
                speed.y += delta * GRAVITY;
            }
        }
        else
        {
            if(nearestFloor1 > getY() + (getHeight() / 2) && nearestFloor2 > getY() + (getHeight() / 2))
            {
                falling = true;
            }
        }

        if(     speed.x > 0 && (
                map.isSolid((int)(getX() + getWidth()/2 + delta * speed.x), (int)(getY() - getHeight()*0.25f)) ||
                map.isSolid((int)(getX() + getWidth()/2 + delta * speed.x), (int)(getY() + getHeight()*0.25f))
                )
        )
        {
            speed.x = 0;
        }
        if(     speed.x < 0 && (
                map.isSolid((int)(getX() - getWidth()/2 + delta * speed.x), (int)(getY() - getHeight()*0.25f)) ||
                        map.isSolid((int)(getX() - getWidth()/2 + delta * speed.x), (int)(getY() + getHeight()*0.25f))
        )
        )
        {
            speed.x = 0;
        }
        super.act(delta);
    }

    @Override
    public void drawDebug(ShapeRenderer shapes) {
        super.drawDebug(shapes);

        shapes.begin(ShapeRenderer.ShapeType.Filled);
        shapes.setColor(Color.NAVY);
        shapes.rect(getX() - getWidth()*0.5f - map.scrollX, getY() - getHeight()*0.5f, getWidth(), getHeight());
        shapes.end();
    }

    public void kill()
    {
        dead = true;
    }

    public boolean isFalling() {
        return falling;
    }

    public boolean isDead() {
        return dead;
    }

    public void takeDamage (int damage){

    }


}
