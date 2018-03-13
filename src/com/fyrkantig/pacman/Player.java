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
        Coordinate coord = field.getPlayerCoordinates();
        xCoord = coord.x;
        yCoord = coord.y;
    }

    private void moveUp() {
        System.out.println("UP");
    }

    private void moveDown() {
        System.out.println("DOWN");
    }

    private void moveLeft() {
        System.out.println("LEFT");
    }

    private void moveRight() {
        System.out.println("RIGHT");
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
