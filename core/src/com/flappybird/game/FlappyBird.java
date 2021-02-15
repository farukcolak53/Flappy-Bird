package com.flappybird.game;

import com.badlogic.gdx.ApplicationAdapter;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.math.Circle;
import com.badlogic.gdx.math.Intersector;
import com.badlogic.gdx.math.Rectangle;

import java.util.Random;

public class FlappyBird extends ApplicationAdapter {
    SpriteBatch batch;
    Texture background;
    Texture[] birds;
    Texture topTube, bottomTube;
    Texture gameOver;

    Circle birdCircle;
    ShapeRenderer shapeRenderer;

    Rectangle[] topPipeRectangles;
    Rectangle[] bottomPipeRectangles;

    int birdState = 0;
    int pause = 0;
    float birdY = 0;
    float velocity = 0;
    float gravity = 2;
    int gameState = 0;
    int score = 0;
    int scoringTube = 0;
    float gap = 400;
    float[] tubeOffset;
    float tubeVelocity = 4; //tubes move to left
    float[] tubeX; //x coordinate of the tube
    int numberOfTubes = 4;
    float distBetweenTubes;

    Random random;
    BitmapFont bitmapFont;

    public void gameStart() {
        birdY = Gdx.graphics.getHeight() / 2 - birds[0].getHeight() / 2;

        for (int i = 0; i < numberOfTubes; i++) {
            tubeX[i] = Gdx.graphics.getWidth() / 2 - topTube.getWidth() / 2 + Gdx.graphics.getWidth() + (i * distBetweenTubes);
            topPipeRectangles[i] = new Rectangle();
            bottomPipeRectangles[i] = new Rectangle();
        }
    }

    @Override
    public void create() {
        batch = new SpriteBatch();
        background = new Texture("bg.png");
        birds = new Texture[2];
        birds[0] = new Texture("bird.png");
        birds[1] = new Texture("bird2.png");
        topTube = new Texture("toptube.png");
        bottomTube = new Texture("bottomtube.png");
        gameOver = new Texture("gameover.png");

        random = new Random();
        distBetweenTubes = Gdx.graphics.getWidth() / 2;
        tubeX = new float[numberOfTubes];
        tubeOffset = new float[numberOfTubes];

        birdCircle = new Circle();
        shapeRenderer = new ShapeRenderer();

        bitmapFont = new BitmapFont();
        bitmapFont.setColor(Color.WHITE);
        bitmapFont.getData().setScale(10);

        topPipeRectangles = new Rectangle[numberOfTubes];
        bottomPipeRectangles = new Rectangle[numberOfTubes];
        gameStart();
    }

    @Override
    public void render() {
        batch.begin();
        batch.draw(background, 0, 0, Gdx.graphics.getWidth(), Gdx.graphics.getHeight());
        batch.end();
        if (gameState == 1) {
            if (tubeX[scoringTube] < Gdx.graphics.getWidth() / 2) {
                score++;
                if (scoringTube < numberOfTubes - 1)
                    scoringTube++;
                else
                    scoringTube = 0;
            }
            if (Gdx.input.justTouched()) {
                velocity = -30;
            }
            if (pause < 8) {
                pause++;
            } else {
                pause = 0;
                if (birdState == 0) {
                    birdState = 1;
                } else {
                    birdState = 0;
                }
            }

            for (int i = 0; i < numberOfTubes; i++) {

                if (tubeX[i] < (-topTube.getWidth())) {
                    tubeX[i] = tubeX[i] + numberOfTubes * distBetweenTubes;
                    tubeOffset[i] = (random.nextFloat() - 0.5f) * (Gdx.graphics.getHeight() - gap - 200);
                } else {
                    tubeX[i] = tubeX[i] - tubeVelocity;
                }
                batch.begin();

                batch.draw(topTube, tubeX[i], Gdx.graphics.getHeight() / 2 + gap / 2 + tubeOffset[i]);
                batch.draw(bottomTube, tubeX[i], Gdx.graphics.getHeight() / 2 - (gap / 2 + bottomTube.getHeight() - tubeOffset[i]));//+ tubeOffset

                topPipeRectangles[i] = new Rectangle(tubeX[i], Gdx.graphics.getHeight() / 2 + gap / 2 + tubeOffset[i], topTube.getWidth(), topTube.getHeight());
                bottomPipeRectangles[i] = new Rectangle(tubeX[i], Gdx.graphics.getHeight() / 2 - (gap / 2 + bottomTube.getHeight() - tubeOffset[i]), bottomTube.getWidth(), bottomTube.getHeight());

                batch.end();
            }

            if (birdY > 0) {
                velocity = velocity + gravity;
                birdY -= velocity;// birdY=-velocity

            } else if (birdY <= 0) {
                birdY = 0;
                gameState = 2;
            }
            if (birdY >= Gdx.graphics.getHeight() - birds[birdState].getHeight())  //Prevent go above screen
                birdY = Gdx.graphics.getHeight() - birds[birdState].getHeight();
        } else if (gameState == 0) {
            if (Gdx.input.justTouched())
                gameState = 1;
        } else if (gameState == 2) {
            batch.begin();
            batch.draw(gameOver, Gdx.graphics.getWidth() / 2 - gameOver.getWidth() / 2, Gdx.graphics.getHeight() / 2 - gameOver.getHeight() / 2);
            batch.end();
            if (Gdx.input.justTouched()) { //restart the game
                scoringTube = 0;
                score = 0;
                velocity = 0;
                gameStart();
                gameState = 1;
            }
        }
        batch.begin();
        batch.draw(birds[birdState], Gdx.graphics.getWidth() / 2 - birds[birdState].getWidth() / 2, birdY);
        bitmapFont.draw(batch, String.valueOf(score), 100, 200);
        batch.end();

        birdCircle.set(Gdx.graphics.getWidth() / 2, birdY + birds[birdState].getHeight() / 2, birds[birdState].getWidth() / 2);
        //shapeRenderer.begin(ShapeRenderer.ShapeType.Filled);
        //shapeRenderer.setColor(Color.BLACK);
        //shapeRenderer.circle(Gdx.graphics.getWidth() / 2, birdY + birds[birdState].getHeight() / 2, birds[birdState].getWidth() / 2);

        for (int i = 0; i < numberOfTubes; i++) {
            //shapeRenderer.rect(tubeX[i], Gdx.graphics.getHeight() / 2 + gap / 2 + tubeOffset[i], topTube.getWidth(), topTube.getHeight());
            //shapeRenderer.rect(tubeX[i], Gdx.graphics.getHeight() / 2 - (gap / 2 + bottomTube.getHeight() - tubeOffset[i]), bottomTube.getWidth(), bottomTube.getHeight());
            if (Intersector.overlaps(birdCircle, topPipeRectangles[i]) || Intersector.overlaps(birdCircle, bottomPipeRectangles[i])) {
                gameState = 2;
            }
        }

        //shapeRenderer.end();

    }

    @Override
    public void dispose() {

    }
}
