package com.fyrkantig.pacman;

import com.fyrkantig.term.Color;

public class FieldObject {

    protected int xCoord;
    protected int yCoord;
    protected Style style;

    public FieldObject(int x, int y, Style style) {
        xCoord = x;
        yCoord = y;
        this.style = style;
    }

    public FieldObject(){}

    public Style getStyle() {return style;}

    public int getxCoord() {return xCoord;}

    public int getyCoord() {return yCoord;}

    public void setxCoord(int xCoord) {this.xCoord = xCoord;}

    public void setyCoord(int yCoord) {this.yCoord = yCoord;}

}

enum Style {

    WALL(' ', Color.BLUE, Color.BLUE),
    PLAYER('P', Color.YELLOW, Color.BLACK),
    ENEMY('E', Color.WHITE, Color.BLACK),
    COIN('$', Color.MAGENTA, Color.BLACK),
    EMPTY(' ', Color.BLACK, Color.BLACK);

    public final char chr;
    public final Color fgColor;
    public final Color bgColor;

    Style(char chr, Color fgColor, Color bgColor) {
        this.chr = chr;
        this.fgColor = fgColor;
        this.bgColor = bgColor;
    }

}
