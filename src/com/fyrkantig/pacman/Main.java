package com.fyrkantig.pacman;

import com.fyrkantig.term.Color;
import com.fyrkantig.term.Terminal;

public class Main {

    public static void main(String[] args) {
	    Terminal term = new Terminal();
	    term.setBgColor(Color.BLACK);
	    term.setFgColor(Color.WHITE);
	    term.putString(10, 10, "YO GUYS");
        try {
            Thread.sleep(3000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        term.hideCursor(false);
    }
}
