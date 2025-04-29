package com.semih.survivorbird;

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

import java.util.Random;

public class SurvivorBird extends ApplicationAdapter {

    // Grafik ve fontlar
    SpriteBatch batch;
    Texture background;
    Texture bird;
    Texture fireBall1;
    Texture fireBall2;
    Texture fireBall3;

    // Kuş konumu ve hareket
    float birdX = 0;
    float birdY = 0;
    float velocity = 0;
    float gravity = 0.3f;

    // Oyun durumu
    int gameState = 0;
    int score = 0;
    int scoredEnemy = 0;

    // Düşmanlar
    int numberOfEnemies = 4;
    float[] enemyX = new float[numberOfEnemies];
    float[] enemyOffSet = new float[numberOfEnemies];
    float[] enemyOffSet2 = new float[numberOfEnemies];
    float[] enemyOffSet3 = new float[numberOfEnemies];
    float distance = 0;
    float enemyVelocity = 8;

    // Çarpışma
    Circle birdCircle;
    Circle[] enemyCircles1;
    Circle[] enemyCircles2;
    Circle[] enemyCircles3;
    ShapeRenderer shapeRenderer;

    // Rastgelelik
    Random random;

    // Yazılar
    BitmapFont font;
    BitmapFont font2;

    @Override
    public void create() {
        batch = new SpriteBatch();
        background = new Texture("backGround.jpg");
        bird = new Texture("bird.png");
        fireBall1 = new Texture("Fireball.png");
        fireBall2 = new Texture("Fireball.png");
        fireBall3 = new Texture("Fireball.png");

        random = new Random();

        birdX = Gdx.graphics.getWidth() / 5;
        birdY = Gdx.graphics.getHeight() / 2 - 200;
        distance = Gdx.graphics.getWidth() / 2;

        birdCircle = new Circle();
        enemyCircles1 = new Circle[numberOfEnemies];
        enemyCircles2 = new Circle[numberOfEnemies];
        enemyCircles3 = new Circle[numberOfEnemies];
        shapeRenderer = new ShapeRenderer();

        for (int i = 0; i < numberOfEnemies; i++) {
            enemyOffSet[i] = random.nextFloat() * Gdx.graphics.getHeight();
            enemyOffSet2[i] = random.nextFloat() * Gdx.graphics.getHeight();
            enemyOffSet3[i] = random.nextFloat() * Gdx.graphics.getHeight();

            enemyX[i] = Gdx.graphics.getWidth() / fireBall1.getWidth() / 2 + i * distance;

            enemyCircles1[i] = new Circle();
            enemyCircles2[i] = new Circle();
            enemyCircles3[i] = new Circle();
        }

        font = new BitmapFont();
        font.setColor(Color.WHITE);
        font.getData().setScale(4);

        font2 = new BitmapFont();
        font2.setColor(Color.RED);
        font2.getData().setScale(5);
    }

    @Override
    public void render() {
        batch.begin();
        batch.draw(background, 0, 0, Gdx.graphics.getWidth(), Gdx.graphics.getHeight());

        if (gameState == 1) {
            // Skor güncelleme
            if (enemyX[scoredEnemy] < Gdx.graphics.getWidth() / 5) {
                score++;
                scoredEnemy = (scoredEnemy < numberOfEnemies - 1) ? scoredEnemy + 1 : 0;
            }

            // Kuş zıplatma
            if (Gdx.input.justTouched()) {
                velocity = -8;
            }

            // Düşmanları hareket ettirme ve yeniden konumlandırma
            for (int i = 0; i < numberOfEnemies; i++) {
                if (enemyX[i] < Gdx.graphics.getWidth() / 15) {
                    enemyX[i] += numberOfEnemies * distance;

                    enemyOffSet[i] = (random.nextFloat() - 0.5f) * (Gdx.graphics.getHeight() - 200);
                    enemyOffSet2[i] = (random.nextFloat() - 0.5f) * (Gdx.graphics.getHeight() - 200);
                    enemyOffSet3[i] = (random.nextFloat() - 0.5f) * (Gdx.graphics.getHeight() - 200);
                } else {
                    enemyX[i] -= enemyVelocity;
                }

                // Düşmanları çiz
                batch.draw(fireBall1, enemyX[i], Gdx.graphics.getHeight() / 2 + enemyOffSet[i], Gdx.graphics.getWidth() / 15, Gdx.graphics.getHeight() / 8);
                batch.draw(fireBall2, enemyX[i], Gdx.graphics.getHeight() / 2 + enemyOffSet2[i], Gdx.graphics.getWidth() / 15, Gdx.graphics.getHeight() / 8);
                batch.draw(fireBall3, enemyX[i], Gdx.graphics.getHeight() / 2 + enemyOffSet3[i], Gdx.graphics.getWidth() / 15, Gdx.graphics.getHeight() / 8);

                // Düşman çarpışma alanları
                enemyCircles1[i] = new Circle(enemyX[i] + Gdx.graphics.getWidth() / 30, Gdx.graphics.getHeight() / 2 + enemyOffSet[i] + Gdx.graphics.getHeight() / 16, Gdx.graphics.getWidth() / 30);
                enemyCircles2[i] = new Circle(enemyX[i] + Gdx.graphics.getWidth() / 30, Gdx.graphics.getHeight() / 2 + enemyOffSet2[i] + Gdx.graphics.getHeight() / 16, Gdx.graphics.getWidth() / 30);
                enemyCircles3[i] = new Circle(enemyX[i] + Gdx.graphics.getWidth() / 30, Gdx.graphics.getHeight() / 2 + enemyOffSet3[i] + Gdx.graphics.getHeight() / 16, Gdx.graphics.getWidth() / 30);
            }

            // Kuş hareketi
            if (birdY > 0) {
                velocity += gravity;
                birdY -= velocity;
            } else {
                gameState = 2;
            }

        } else if (gameState == 0) {
            if (Gdx.input.justTouched()) {
                gameState = 1;
            }

        } else if (gameState == 2) {
            font2.draw(batch, "Game Over! Tap To Play AGAIN!", 100, Gdx.graphics.getHeight() / 2);
            font2.draw(batch,"Semih Sahin",100,Gdx.graphics.getHeight() / 3);

            if (Gdx.input.justTouched()) {
                gameState = 1;
                birdY = Gdx.graphics.getHeight() / 2 - 200;

                for (int i = 0; i < numberOfEnemies; i++) {
                    enemyOffSet[i] = random.nextFloat() * Gdx.graphics.getHeight();
                    enemyOffSet2[i] = random.nextFloat() * Gdx.graphics.getHeight();
                    enemyOffSet3[i] = random.nextFloat() * Gdx.graphics.getHeight();

                    enemyX[i] = Gdx.graphics.getWidth() / fireBall1.getWidth() / 2 + i * distance;

                    enemyCircles1[i] = new Circle();
                    enemyCircles2[i] = new Circle();
                    enemyCircles3[i] = new Circle();
                }

                velocity = 0;
                scoredEnemy = 0;
                score = 0;
            }
        }

        // Kuşu çiz
        batch.draw(bird, birdX, birdY, Gdx.graphics.getWidth() / 15, Gdx.graphics.getHeight() / 8);
        font.draw(batch, String.valueOf(score), 100, 200);
        batch.end();

        // Kuş çemberi
        birdCircle.set(birdX + Gdx.graphics.getWidth() / 30, birdY + Gdx.graphics.getHeight() / 16, Gdx.graphics.getWidth() / 30);

        // Çarpışma kontrolü
        for (int i = 0; i < numberOfEnemies; i++) {
            if (Intersector.overlaps(birdCircle, enemyCircles1[i])
                || Intersector.overlaps(birdCircle, enemyCircles2[i])
                || Intersector.overlaps(birdCircle, enemyCircles3[i])) {
                gameState = 2;
            }
        }
    }

    @Override
    public void dispose() {
        // Kaynak temizliği yapılabilir
    }
}
