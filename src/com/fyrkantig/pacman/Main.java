package com.fyrkantig.pacman;


import java.util.concurrent.ScheduledThreadPoolExecutor;

public class Main {

    public static void main(String[] args) {
        ScheduledThreadPoolExecutor enemies = Enemy.releaseEnemies(new Field(), 4);
        Player pc = new Player(new Field());
        pc.startGame();
	    Field field = new Field();
    }
}
