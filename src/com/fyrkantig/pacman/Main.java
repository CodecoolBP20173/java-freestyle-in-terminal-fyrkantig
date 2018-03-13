package com.fyrkantig.pacman;


import java.util.concurrent.ScheduledThreadPoolExecutor;

public class Main {

    public static void main(String[] args) {
	    Field field = new Field();
        ScheduledThreadPoolExecutor enemies = Enemy.releaseEnemies(field);
        Player pc = new Player(field);
        pc.startGame();
        enemies.shutdown();
    }
}
