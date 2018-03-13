package com.fyrkantig.pacman;

import java.util.Collections;
import java.util.LinkedList;
import java.util.concurrent.ScheduledThreadPoolExecutor;
import java.util.concurrent.ThreadLocalRandom;

import static java.util.concurrent.TimeUnit.*;


public class Enemy extends FieldObject implements Runnable {

    private int xCoord;
    private int yCoord;
    private Field field;
    private Player target;
    private MoveDirection currentDirection;

    private static ScheduledThreadPoolExecutor enemies;
    private static final int speed = 200;
    private static final int initialDelay = 1000;

    public static ScheduledThreadPoolExecutor releaseEnemies(Field field, Player target) {
        LinkedList<Coordinate> spawnPoints = field.getSpawnPoints();
        int numberOfEnemies = spawnPoints.size();
        enemies = new ScheduledThreadPoolExecutor(numberOfEnemies);

        for (int i = 0; i < numberOfEnemies; i++) {
            Coordinate coord = spawnPoints.get(i);
            enemies.scheduleAtFixedRate(new Enemy(coord.x, coord.y, field, target), initialDelay, speed, MILLISECONDS);
        }
        return enemies;
    }

    public Enemy(int x, int y, Field field, Player target) {
        super(Style.ENEMY);
        xCoord = x;
        yCoord = y;
        this.field = field;
        this.target = target;
        field.createObject(x, y, this);
        searchNewRoute();
        // Add dinamically to pool
    }

    private void searchNewRoute() {
        LinkedList<MoveDirection> validDirections = field.getValidDirections(xCoord, yCoord);
        // Check needed for NPCs closing each other in
        if (validDirections.contains(currentDirection) && validDirections.size() < 3) {
            move(currentDirection);
        } else {
            if (currentDirection != null && validDirections.contains(currentDirection.opposite())
                    && validDirections.size() > 1) {
                            validDirections.remove(currentDirection.opposite());
            }

            LinkedList<MoveDirection> playerDirections = seekPlayer(validDirections);
            LinkedList<MoveDirection> optimalDirections = playerDirections.size() > 0 ?
                    playerDirections : validDirections;
            int randomDirection = ThreadLocalRandom.current().nextInt(optimalDirections.size());
            currentDirection = optimalDirections.get(randomDirection);
            move(currentDirection);
        }
    }

    private LinkedList<MoveDirection> seekPlayer(LinkedList<MoveDirection> validDirections) {
        LinkedList<MoveDirection> preferredDirections = new LinkedList<>();
        if (target.getyCoord() < yCoord) {preferredDirections.add(MoveDirection.UP);}
            else {preferredDirections.add(MoveDirection.DOWN);}
        if (target.getxCoord() < xCoord) {preferredDirections.add(MoveDirection.LEFT);}
            else {preferredDirections.add(MoveDirection.RIGHT);}

        preferredDirections.retainAll(validDirections);
        return preferredDirections;
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
    UP, DOWN, LEFT, RIGHT;

    final MoveDirection opposite() {
        switch (this) {
            case UP: return DOWN;
            case DOWN: return UP;
            case LEFT: return RIGHT;
            case RIGHT: return LEFT;
            default: return null;
        }
    }
}
