package com.flappybird.game;

import com.badlogic.gdx.ApplicationAdapter;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;

public class FlappyBird extends ApplicationAdapter {
    SpriteBatch batch;
    Texture background;
    Texture[] birds;
    int birdState = 0;
    int pause = 0;

    @Override
    public void create() {
        batch = new SpriteBatch();
        background = new Texture("bg.png");
        birds = new Texture[2];
        birds[0] = new Texture("bird.png");
        birds[1] = new Texture("bird2.png");

    }

    @Override
    public void render() {

        if (pause < 8){
            pause++;
        }
        else {
            pause = 0;
            if (birdState == 0) {
                birdState = 1;
            }
            else {
                birdState = 0;
            }
        }

        batch.begin();

        batch.draw(background, 0, 0, Gdx.graphics.getWidth(), Gdx.graphics.getHeight());
        batch.draw(birds[birdState], Gdx.graphics.getWidth()/2 - birds[birdState].getWidth()/2, Gdx.graphics.getHeight()/2 - birds[birdState].getHeight()/2);

        batch.end();
    }

    @Override
    public void dispose() {

    }
}
