package com.fyrkantig.pacman;

import com.fyrkantig.term.Terminal;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.util.LinkedList;
import java.util.Scanner;

public class Field {

    private final int rows = 31;
    private final int columns = 53;
    private FieldObject [][] map;
    private Terminal term = new Terminal();

    private LinkedList<Coordinate> spawnPoints = new LinkedList<>();
    private Coordinate playerSpawnPoint;

    public Field (){
        map = new FieldObject[rows][columns];
        initMap();
    }

    public void initMap() {
        Scanner sc = null;
        try {
            sc = new Scanner(new BufferedReader(new FileReader("map")));
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
        while(sc.hasNextLine()) {
            for (int i=0; i<map.length; i++) {
                String[] line = sc.nextLine().trim().split(" ");
                for (int j=0; j<line.length; j++) {
                    switch (line[j]) {
                        case "0":
                            map[i][j] = new FieldObject(Style.WALL);
                            break;
                        case "1":
                            map[i][j] = new FieldObject(Style.EMPTY);
                            break;
                        case "2":
                            map[i][j] = new FieldObject(Style.EMPTY);
                            spawnPoints.add(new Coordinate(j, i));
                            break;
                        case "3":
                            map[i][j] = new FieldObject(Style.EMPTY);
                            playerSpawnPoint = new Coordinate(i, j);
                    }
                    renderObject(j, i);
                }
            }
        }
    }

    private void renderObject(int x, int y) {
        Style object = map[y][x].getStyle();
        term.setFgColor(object.fgColor);
        term.setBgColor(object.bgColor);
        term.setChar(x+1, y+1, object.chr);

    }

    public LinkedList<Coordinate> getSpawnPoints() {
        return spawnPoints;
    }

    public Coordinate getPlayerSpawnPoint() {
        return playerSpawnPoint;
    }

    public void createObject(int x, int y, FieldObject obj) {
        // Check here
        map[y][x] = obj;
        renderObject(x, y);
    }

    public boolean moveObject(int prevX, int prevY, int nextX, int nextY, FieldObject obj) {
        // Check here
        if (!checkPosition(nextX, nextY, obj)) {return false;}
        createObject(prevX, prevY, new FieldObject(Style.EMPTY));  // Pop coin back here
        createObject(nextX, nextY, obj);
        return true;
    }

    private boolean checkPosition(int x, int y, FieldObject obj) {
        if ((x >= 0 && x < columns) && (y >= 0 && y < rows)) {

            switch (map[y][x].getStyle()) {
                case EMPTY:
                    return true;
                case WALL:
                    return false;
                case ENEMY:
                    if (obj.getStyle() == Style.PLAYER) {
                        ((Player)obj).terminate();
                    }
                    return false;
                case PLAYER:
                    if (obj.getStyle() == Style.ENEMY) {
                        ((Player)map[y][x]).terminate();
                    }
                    return true;
                case COIN:
                    return true;
            }

        }
        return false;
    }

}

class Coordinate {
    final int x;
    final int y;

    Coordinate(int x, int y) {
        this.x = x;
        this.y = y;
    }
}