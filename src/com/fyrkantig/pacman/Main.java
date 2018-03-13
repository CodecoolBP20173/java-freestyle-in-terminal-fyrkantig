package com.fyrkantig.pacman;


public class Main {

    public static void main(String[] args) {
        Enemy.releaseEnemies(new Field(), 10);
        try {
            Thread.sleep(5000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        Player pc = new Player(new Field());
        pc.startGame();
    }
}
