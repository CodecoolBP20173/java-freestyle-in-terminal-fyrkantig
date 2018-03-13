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
}

class Coordinate {
    final int x;
    final int y;

    Coordinate(int x, int y) {
        this.x = x;
        this.y = y;
    }
}