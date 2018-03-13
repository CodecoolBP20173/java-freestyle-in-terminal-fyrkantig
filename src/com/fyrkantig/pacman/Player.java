package com.fyrkantig.pacman;

import java.io.IOException;

public class Player extends FieldObject {

    private int xCoord;
    private int yCoord;
    private Field field;
    private boolean isAlive = true;

    public Player(Field field) {
        super(Style.PLAYER);
        this.field = field;
        Coordinate coord = field.getPlayerSpawnPoint();
        xCoord = coord.x;
        yCoord = coord.y;
        field.createObject(xCoord, yCoord, this);
    }

    private void moveUp() {
        if (field.moveObject(xCoord, yCoord, xCoord, yCoord - 1, this)) {
            yCoord --;
        }
    }

    private void moveDown() {
        if (field.moveObject(xCoord, yCoord, xCoord, yCoord + 1, this)) {
            yCoord ++;
        }
    }

    private void moveLeft() {
        if (field.moveObject(xCoord, yCoord, xCoord - 1, yCoord, this)) {
            xCoord --;
        }
    }

    private void moveRight() {
        if (field.moveObject(xCoord, yCoord, xCoord + 1, yCoord, this)) {
            xCoord ++;
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
        }
    }

    public synchronized void terminate() {
        isAlive = false;
    }

    public synchronized void win() {}

    public boolean isAlive() {return isAlive;}

    public void startGame() {
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
