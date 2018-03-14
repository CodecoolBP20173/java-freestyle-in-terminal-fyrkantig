package com.fyrkantig.pacman;


public class Main {

    public static void main(String[] args) {
	    Field field = new Field(0.1);
        Player player = new Player(field);
        Enemy.releaseEnemies(200, field, player);
        player.startGame();
        field.clearTerminal();
    }
}
