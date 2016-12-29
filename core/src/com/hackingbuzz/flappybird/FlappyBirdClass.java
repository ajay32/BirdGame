package com.hackingbuzz.flappybird;

import com.badlogic.gdx.ApplicationAdapter;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.math.Circle;
import com.badlogic.gdx.math.Intersector;
import com.badlogic.gdx.math.Rectangle;

import java.util.Random;

public class FlappyBirdClass extends ApplicationAdapter {
	SpriteBatch batch;
	Texture background;
	Texture[] birds;
	int flapState = 0;
	float birdY = 0;
	float velocity = 0;
	int gameState = 0;
	float gravity = 2;
	Texture toptube;
	Texture bottomtube;
	float gap = 400;
	float maxtubeoffset;
	Random randomGenerator;

	int tubeVelocity = 4;

	int noOfTubes = 4;
	float[] tubeX = new float[noOfTubes];
	float[] tubeOffset = new float[noOfTubes];
	float distanceBetweenTubes;
	//ShapeRenderer shapeRenderer;
	Circle birdCircle;
	Rectangle[] toptubeRectangles;
	Rectangle[] bottomtubeRectangles;
	int score = 0;
	int scoringTube = 0;
	BitmapFont font;
	Texture gameover;


	@Override
	public void create() {
		batch = new SpriteBatch();
		background = new Texture("bg.png");
		birds = new Texture[2];
		birds[0] = new Texture("bird.png");
		birds[1] = new Texture("bird2.png");

		toptube = new Texture("toptube.png");
		bottomtube = new Texture("bottomtube.png");
		maxtubeoffset = Gdx.graphics.getHeight() / 2 - gap / 2 - 100;
		randomGenerator = new Random();
		distanceBetweenTubes = Gdx.graphics.getWidth() * 3 / 4;
		font = new BitmapFont();
		font.setColor(Color.WHITE);
		font.getData().setScale(10);
		gameover = new Texture("gameover.png");
		//shapeRenderer = new ShapeRenderer();
		birdCircle = new Circle();
		toptubeRectangles = new Rectangle[noOfTubes];
		bottomtubeRectangles = new Rectangle[noOfTubes];

		startGame();

	}


	public void startGame() {


		birdY = Gdx.graphics.getHeight() / 2 - birds[0].getHeight() / 2;
		for (int i = 0; i < noOfTubes; i++) {
			tubeOffset[i] = (randomGenerator.nextFloat() - 0.5f) * (Gdx.graphics.getHeight() - gap - 200);

			tubeX[i] = Gdx.graphics.getWidth() / 2 - toptube.getWidth() / 2 + Gdx.graphics.getWidth() + i * distanceBetweenTubes;
			toptubeRectangles[i] = new Rectangle();
			bottomtubeRectangles[i] = new Rectangle();
		}

	}

	@Override
	public void render() {


		batch.begin();
		batch.draw(background, 0, 0, Gdx.graphics.getWidth(), Gdx.graphics.getHeight());


		if (gameState == 1) {

			if (tubeX[scoringTube] < Gdx.graphics.getWidth() / 2) {
				score++;
				Gdx.app.log("Score", String.valueOf(score));

				if (scoringTube < noOfTubes - 1) {          // why we took (-1) ..suppose  3 < 4  and scoringTube++   ...it will get to 4 ...but we have only 0,1,2,3   ..so will crete probelem so..it want it to go max 3 even if its incrementing so took (-1)
					scoringTube++;
				} else {
					scoringTube = 0;
				}
			}

			if (Gdx.input.justTouched()) {

				velocity = -18;


			}
			for (int i = 0; i < noOfTubes; i++) {

				if (tubeX[i] < -toptube.getWidth()) {

					tubeX[i] += noOfTubes * distanceBetweenTubes;
					tubeOffset[i] = (randomGenerator.nextFloat() - 0.5f) * (Gdx.graphics.getHeight() - gap - 200);

				} else {

					tubeX[i] = tubeX[i] - tubeVelocity;
				}

				batch.draw(toptube, tubeX[i], Gdx.graphics.getHeight() / 2 + gap / 2 + tubeOffset[i]);   // its for position we got the centre position n the more value we add (gap) ..it will effect the centre position (positive vertical axis )  200 gap means its getting up by 200 +Ve vertical direction
				batch.draw(bottomtube, tubeX[i], Gdx.graphics.getHeight() / 2 - gap / 2 - bottomtube.getHeight() + tubeOffset[i]);
				toptubeRectangles[i] = new Rectangle(tubeX[i], Gdx.graphics.getHeight() / 2 + gap / 2 + tubeOffset[i], toptube.getWidth(), toptube.getHeight());
				bottomtubeRectangles[i] = new Rectangle(tubeX[i], Gdx.graphics.getHeight() / 2 - gap / 2 - bottomtube.getHeight() + tubeOffset[i], bottomtube.getWidth(), bottomtube.getHeight());

			}
			if (birdY > 0) {
				velocity = velocity + gravity;
				birdY -= velocity;
			} else {
				gameState = 2;

			}


		} else if (gameState == 0) {
			if (Gdx.input.justTouched()) {

				gameState = 1;
			}


		} else if (gameState == 2) {
			batch.draw(gameover, Gdx.graphics.getWidth() / 2 - gameover.getWidth() / 2, Gdx.graphics.getHeight() / 2 - gameover.getHeight() / 2);
			if (Gdx.input.justTouched()) {

				gameState = 1;
				startGame();
				score = 0;
				scoringTube = 0;
				velocity = 0;
			}
		}
			if (flapState == 0) {
				flapState = 1;
			} else {
				flapState = 0;
			}


			batch.draw(birds[flapState], Gdx.graphics.getWidth() / 2 - birds[flapState].getWidth() / 2, birdY);
			font.draw(batch, String.valueOf(score), 80, 150);

			//shapeRenderer.begin(ShapeRenderer.ShapeType.Filled);
			//shapeRenderer.setColor(Color.RED);

			for (int i = 0; i < noOfTubes; i++) {

				birdCircle.set(Gdx.graphics.getWidth() / 2, birdY + birds[flapState].getHeight() / 2, birds[flapState].getHeight() / 2);

				//shapeRenderer.circle(birdCircle.x, birdCircle.y, birdCircle.radius);
				//shapeRenderer.rect(tubeX[i],Gdx.graphics.getHeight() / 2 + gap / 2 + tubeOffset[i], toptube.getWidth(), toptube.getHeight());
				//shapeRenderer.rect(tubeX[i], Gdx.graphics.getHeight() / 2 - gap / 2 - bottomtube.getHeight() + tubeOffset[i],bottomtube.getWidth(), bottomtube.getHeight());

				if (Intersector.overlaps(birdCircle, toptubeRectangles[i]) || Intersector.overlaps(birdCircle, bottomtubeRectangles[i])) {
					gameState = 2;
				}
			}
			batch.end();


			//	shapeRenderer.end();



	}
}
// the concept of pipes are starting from center and going upware..you want them to go more upward ..add value....if you want them to go download ... subtract value..
// for toptube and bottom tube...if you want to get space upward (vertical ) all value (ex 200 )
// if you want to get space in download ( -y )  you have to subtract some value
// also you gotta subtract the height of bottom tube..coz it will start from center - gap ...and go upward if you want it to toch the bottm of screen you gotta subtract tube height to make it come below
