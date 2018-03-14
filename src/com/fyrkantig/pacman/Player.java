package com.fyrkantig.pacman;

import java.io.IOException;
import java.util.concurrent.atomic.AtomicInteger;

public class Player extends FieldObject {

    private AtomicInteger xCoord;
    private AtomicInteger yCoord;
    private Field field;
    private boolean isAlive;

    public Player(Field field) {
        super(Style.PLAYER);
        this.field = field;
        Coordinate coord = field.getPlayerSpawnPoint();
        xCoord = new AtomicInteger(coord.x);
        yCoord = new AtomicInteger(coord.y);
        field.createObject(xCoord.get(), yCoord.get(), this);
    }

    private void moveUp() {
        if (field.moveObject(xCoord.get(), yCoord.get(), xCoord.get(), yCoord.get() - 1, this)) {
            yCoord.decrementAndGet();
        }
    }

    private void moveDown() {
        if (field.moveObject(xCoord.get(), yCoord.get(), xCoord.get(), yCoord.get() + 1, this)) {
            yCoord.incrementAndGet();
        }
    }

    private void moveLeft() {
        if (field.moveObject(xCoord.get(), yCoord.get(), xCoord.get() - 1, yCoord.get(), this)) {
            xCoord.decrementAndGet();
        }
    }

    private void moveRight() {
        if (field.moveObject(xCoord.get(), yCoord.get(), xCoord.get() + 1, yCoord.get(), this)) {
            xCoord.incrementAndGet();
        }
    }

    private void move(char keyPressed) {
        switch (keyPressed) {
            case 'w':
                moveUp();
                break;
            case 's':
                moveDown();
                break;
            case 'a':
                moveLeft();
                break;
            case 'd':
                moveRight();
                break;
            case 27:
                isAlive = false;
                break;
            case 'c':
                Enemy.setEnemySpeed(1000);
                break;
            case 't':
                Enemy.setEnemySpeed(50);
                break;
            case 'n':
                Enemy.setEnemySpeed(200);
        }
    }

    public synchronized void terminate() {
        isAlive = false;
    }

    public synchronized void win() {
        Enemy.stopEnemies();
        field.printWin();
        isAlive = false;
    }

    public boolean isAlive() {return isAlive;}

    public int getxCoord() {
        return xCoord.get();
    }

    public int getyCoord() {
        return yCoord.get();
    }

    public void startGame() {
        isAlive = true;
        while (isAlive) {
            try {
                if (System.in.available() > 0) {
                    move((char)System.in.read());
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

}
