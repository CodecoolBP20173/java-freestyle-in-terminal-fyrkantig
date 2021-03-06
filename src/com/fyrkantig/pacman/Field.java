package com.fyrkantig.pacman;

import com.fyrkantig.term.Color;
import com.fyrkantig.term.Terminal;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.util.Collections;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.Scanner;

public class Field {

    private double coinRatio;
    private final int rows = 31;
    private final int columns = 53;
    private FieldObject[][] map;
    private Terminal term = new Terminal();

    private LinkedList<Coordinate> spawnPoints = new LinkedList<>();
    private Coordinate playerSpawnPoint;
    private int numberOfCoins;
    private final String SCORE_BOARD = "Remaining Coins: ";

    public Field(double coinRatio) {
        map = new FieldObject[rows][columns];
        this.coinRatio = coinRatio;
        initMap();
        initScoreBoard();
    }

    private void initScoreBoard() {
        term.resetStyle();
        term.putString(60, 3, SCORE_BOARD + numberOfCoins);
    }

    public void clearTerminal() {
        term.resetStyle();
        term.moveTo(1, 1);
        term.clearScreen();
        term.hideCursor(false);
    }

    public void printWin() {
        term.setFgColor(Color.BLACK);
        term.setFgColor(Color.GREEN);
        term.clearScreen();
        for (int i = 0; i < AsciiArts.WIN.length; i++) {
            term.putString(20, 5 + i, AsciiArts.WIN[i]);
        }
        try {
            Thread.sleep(5000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
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
        int numberOfCoins = (int) (coinList.size() * coinRatio);
        for (int i = 0; i < numberOfCoins; i++) {
            coinList.set(i, CoinRegister.COIN);
        }
        Collections.shuffle(coinList);
        return coinList;
    }


    public void initMap() {
        Scanner sc = null;
        Iterator coinIterator = null;
        try {
            sc = new Scanner(new BufferedReader(new FileReader("map")));
            LinkedList<CoinRegister> coinRegister = shuffleCoins();
            coinIterator = coinRegister.iterator();
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }

        while (sc.hasNextLine()) {
            for (int i = 0; i < map.length; i++) {
                String[] line = sc.nextLine().trim().split(" ");
                for (int j = 0; j < line.length; j++) {
                    switch (line[j]) {
                        case "0":
                            map[i][j] = new FieldObject(Style.WALL);
                            break;
                        case "1":
                            if (coinIterator.hasNext() && coinIterator.next() == CoinRegister.COIN) {
                                map[i][j] = new FieldObject(Style.COIN);
                                numberOfCoins++;
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
        if (obj.getStyle() == Style.ENEMY && ((Enemy) obj).isStandingOnCoin()) {
            ((Enemy) obj).setStandingOnCoin(false);
            createObject(prevX, prevY, new FieldObject(Style.COIN));
        } else {
            createObject(prevX, prevY, new FieldObject(Style.EMPTY));
        }
        FieldObject overriddenObject = map[nextY][nextX];
        createObject(nextX, nextY, obj);

        if (overriddenObject.getStyle() == Style.PLAYER) {
            ((Player) overriddenObject).terminate();
        } else if (overriddenObject.getStyle() == Style.COIN) {
            if (obj.getStyle() == Style.PLAYER) {
                changeScore((Player) obj);
            } else {
                ((Enemy) obj).setStandingOnCoin(true);
            }
        }
        return true;
    }

    private void changeScore(Player obj) {
        numberOfCoins--;
        term.resetStyle();
        term.putString(60+SCORE_BOARD.length(),3,Integer.toString(numberOfCoins) + "  ");
        if (numberOfCoins == 0) {
            obj.win();
        }
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
    COIN, EMPTY
}