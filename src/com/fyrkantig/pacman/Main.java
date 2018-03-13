package com.fyrkantig.pacman;


import java.util.concurrent.ScheduledThreadPoolExecutor;

public class Main {

    public static void main(String[] args) {
	    Field field = new Field();
        Player player = new Player(field);
        ScheduledThreadPoolExecutor enemies = Enemy.releaseEnemies(field, player);
        player.startGame();
        enemies.shutdown();
    }
}
