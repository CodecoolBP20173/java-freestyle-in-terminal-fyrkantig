package com.fyrkantig.pacman;

import com.fyrkantig.term.Terminal;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.util.Collection;
import java.util.Collections;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.Scanner;

public class Field {

    private final int rows = 31;
    private final int columns = 53;
    private FieldObject[][] map;
    private Terminal term = new Terminal();

    private LinkedList<Coordinate> spawnPoints = new LinkedList<>();
    private Coordinate playerSpawnPoint;
    private int numberOfCoins;

    public Field() {
        map = new FieldObject[rows][columns];
        initMap();
    }

    private LinkedList<CoinRegister> shuffleCoins() throws FileNotFoundException {
        Scanner sc = new Scanner(new BufferedReader(new FileReader("map")));
        LinkedList<CoinRegister> coinList = new LinkedList<>();
        while (sc.hasNextLine()) {
            String[] line = sc.nextLine().trim().split(" ");
            for (String item : line) {
                if (item.equals("1")) {
                    coinList.add(CoinRegister.EMPTY);
                }
            }
        }
        int numberOfCoins = (int)(coinList.size() * 0.30);
        for (int i=0;i<numberOfCoins;i++) {
            coinList.set(i, CoinRegister.COIN);
        }
        Collections.shuffle(coinList);
        return coinList;
    }


    public void initMap() {
        Scanner sc = null;
        try {
            sc = new Scanner(new BufferedReader(new FileReader("map")));
            LinkedList<CoinRegister> coinRegister = shuffleCoins();
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }

        Iterator coinIterator = coinRegister.iterator();

        while (sc.hasNextLine()) {
            for (int i = 0; i < map.length; i++) {
                String[] line = sc.nextLine().trim().split(" ");
                for (int j = 0; j < line.length; j++) {
                    switch (line[j]) {
                        case "0":
                            map[i][j] = new FieldObject(Style.WALL);
                            break;
                        case "1":
                            if (coinIterator.hasNext() && coinIterator.next() == COIN) {
                                map[i][j] = new FieldObject(Style.COIN);
                                numberOfCoins ++;
                            } else {
                                map[i][j] = new FieldObject(Style.EMPTY);
                            }
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
        term.setChar(x + 1, y + 1, object.chr);

    }

    public LinkedList<Coordinate> getSpawnPoints() {
        return spawnPoints;
    }

    public Coordinate getPlayerSpawnPoint() {
        return playerSpawnPoint;
    }

    public synchronized void createObject(int x, int y, FieldObject obj) {
        // Check here
        map[y][x] = obj;
        renderObject(x, y);
    }

    public synchronized boolean moveObject(int prevX, int prevY, int nextX, int nextY, FieldObject obj) {
        // Check here
        if (!checkPosition(nextX, nextY)) {
            return false;
        }
        if (obj.getStyle() == Style.ENEMY && ((Enemy)obj).isStandingOnCoin()) {
            ((Enemy)obj).setStandingOnCoin(false);
            createObject(prevX, prevY, new FieldObject(Style.COIN));
        } else {
            createObject(prevX, prevY, new FieldObject(Style.EMPTY));
        }
        FieldObject overriddenObject = map[nextY][nextX];
        createObject(nextX, nextY, obj);

        if (overriddenObject.getStyle() == Style.PLAYER) {
            ((Player)overriddenObject).terminate();
        } else if (overriddenObject.getStyle() == Style.COIN) {
            if (obj.getStyle() == Style.PLAYER) {
                numberOfCoins--;
                if (numberOfCoins == 0) {((Player)obj).win();}
            } else {
                ((Enemy)obj).setStandingOnCoin(true);
            }
        }
        return true;
    }

    private boolean checkPosition(int x, int y) {
        return (x >= 0 && x < columns)
                && (y >= 0 && y < rows)
                && (map[y][x].getStyle() != Style.ENEMY && map[y][x].getStyle() != Style.WALL);
    }

    public synchronized LinkedList<MoveDirection> getValidDirections(int x, int y) {
        LinkedList<MoveDirection> validDirections = new LinkedList<>();
        if (checkPosition(x, y - 1 )) {validDirections.add(MoveDirection.UP);}
        if (checkPosition(x, y + 1 )) {validDirections.add(MoveDirection.DOWN);}
        if (checkPosition(x - 1, y )) {validDirections.add(MoveDirection.LEFT);}
        if (checkPosition(x + 1, y )) {validDirections.add(MoveDirection.RIGHT);}
        return validDirections;
    }

}

final class Coordinate {
    final int x;
    final int y;

    Coordinate(int x, int y) {
        this.x = x;
        this.y = y;
    }
}

enum CoinRegister {
    COIN,EMPTY
}