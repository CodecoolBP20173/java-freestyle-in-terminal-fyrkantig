package com.fyrkantig.pacman;

import com.fyrkantig.term.Color;

public class FieldObject {

    protected Style style;

    public FieldObject(Style style) {
        this.style = style;
    }

    public FieldObject(){}

    public Style getStyle() {return style;}

    public void setStyle(Style style) {this.style = style;}
}

enum Style {

    WALL(' ', Color.BLUE, Color.BLUE),
    PLAYER('C', Color.YELLOW, Color.BLACK),
    ENEMY('E', Color.WHITE, Color.BLACK),
    COIN('$', Color.GREEN, Color.BLACK),
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
