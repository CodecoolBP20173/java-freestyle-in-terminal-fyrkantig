package com.fyrkantig.pacman;

import java.util.Collections;
import java.util.LinkedList;
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

    public static ScheduledThreadPoolExecutor releaseEnemies(Field field) {
        LinkedList<Coordinate> spawnPoints = field.getSpawnPoints();
        int numberOfEnemies = spawnPoints.size();
        enemies = new ScheduledThreadPoolExecutor(numberOfEnemies);

        for (int i = 0; i < numberOfEnemies; i++) {
            Coordinate coord = spawnPoints.get(i);
            enemies.scheduleAtFixedRate(new Enemy(coord.x, coord.y, field), initialDelay, speed, MILLISECONDS);
        }
        return enemies;
    }

    public Enemy(int x, int y, Field field) {
        super(Style.ENEMY);
        xCoord = x;
        yCoord = y;
        this.field = field;
        field.createObject(x, y, this);
        searchNewRoute();
        // Add dinamically to pool
    }

    private void searchNewRoute() {
        LinkedList<MoveDirection> validDirections = field.getValidDirections(xCoord, yCoord);
        if (validDirections.contains(currentDirection)) {
            move(currentDirection);
        } else {
            Collections.shuffle(validDirections);
            currentDirection = validDirections.get(0);
            move(currentDirection);
        }
    }

    private void move(MoveDirection directino) {
        switch (directino) {
            case UP:
                field.moveObject(xCoord, yCoord, xCoord, --yCoord, this);
                break;
            case DOWN:
                field.moveObject(xCoord, yCoord, xCoord, ++yCoord, this);
                break;
            case LEFT:
                field.moveObject(xCoord, yCoord, --xCoord, yCoord, this);
                break;
            case RIGHT:
                field.moveObject(xCoord, yCoord, ++xCoord, yCoord, this);
        }
    }

    @Override
    public void run() {
        searchNewRoute();
    }
}

enum MoveDirection {
    UP, DOWN, LEFT, RIGHT
}
