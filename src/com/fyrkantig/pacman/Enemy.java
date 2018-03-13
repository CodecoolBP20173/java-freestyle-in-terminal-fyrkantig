package com.fyrkantig.pacman;

import java.util.concurrent.ScheduledThreadPoolExecutor;
import static java.util.concurrent.TimeUnit.*;


public class Enemy extends FieldObject implements Runnable {

    private int xCoord;
    private int yCoord;
    private Field field;
    private MoveDirection currentDirection;

    private static ScheduledThreadPoolExecutor enemies;
    private static final int speed = 200;
    private static final int initialDelay = 1000;

    public static ScheduledThreadPoolExecutor releaseEnemies(Field field, int numberOfEnemies) {
        // noEnemies => public final variable spawnPoints from Field
        enemies = new ScheduledThreadPoolExecutor(numberOfEnemies);
        for (int i = 0; i < numberOfEnemies; i++) {
            enemies.scheduleAtFixedRate(new Enemy(field), initialDelay, speed, MILLISECONDS);
        }
        return enemies;
    }

    public Enemy(Field field) {
        super(Style.ENEMY);
        // Get spawn point coordinates from Field
        this.field = field;
        searchNewRoute();
        // Add dinamically to pool
    }

    private void searchNewRoute() {
        // Call field method to check potential routes; gets back a LinkedList<Dir> and chooses 1;
        // can only choose comingFrom if it's the only way -- aka turn back from a dead end
        // ! modifies the currentDirection field
    }

    private void moveUp() {
        // Call moveObject here
    }

    private void moveDown() {
        // Call moveObject here
    }

    private void moveLeft() {
        // Call moveObject here
    }

    private void moveRight() {
        // Call moveObject here
    }

    @Override
    public void run() {
        switch (currentDirection) {
            case UP:
                moveUp();
                break;
            case DOWN:
                moveDown();
                break;
            case LEFT:
                moveLeft();
                break;
            case RIGHT:
                moveRight();
        }
        searchNewRoute();
        System.out.println("BOOM");
    }
}

enum MoveDirection {
    UP, DOWN, LEFT, RIGHT
}
