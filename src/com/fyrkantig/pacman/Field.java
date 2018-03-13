package com.fyrkantig.pacman;

import com.fyrkantig.term.Terminal;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.util.Scanner;

public class Field {

    private final int rows = 31;
    private final int columns = 28;
    private FieldObject [][] map;
    private Terminal term = new Terminal();

    public Field (){
        initMap();
    }

    public void initMap() {
        Scanner sc = null;
        try {
            sc = new Scanner(new BufferedReader(new FileReader("map")));
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
        map = new FieldObject[rows][columns];
        while(sc.hasNextLine()) {
            for (int i=0; i<map.length; i++) {
                String[] line = sc.nextLine().trim().split(" ");
                for (int j=0; j<line.length; j++) {
                    switch (line[j]) {
                        case "0": map[i][j] = new FieldObject(Style.WALL);
                            break;
                        case "1": map[i][j] = new FieldObject(Style.EMPTY);
                            break;
                        case "2": map[i][j] = new FieldObject(Style.SPAWN);
                            break;
                    }
                    renderObject(i, j);
                }
            }
        }
    }

    private void renderObject(int x, int y) {
        Style style = map[x][y].getStyle();
        term.setFgColor(style.fgColor);
        term.setBgColor(style.bgColor);
        term.setChar(x, y, style.chr);

    }
}