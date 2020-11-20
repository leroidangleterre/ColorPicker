/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package colorpicker;

import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;

/**
 *
 * @author arthu
 */
public class MyKeyListener implements KeyListener {

    private ColorDisplay display;

    public MyKeyListener(ColorDisplay newDisplay) {
        display = newDisplay;
    }

    @Override
    public void keyTyped(KeyEvent e) {
    }

    @Override
    public void keyPressed(KeyEvent e) {
        switch (e.getKeyChar()) {
            case '+':
                System.out.println("Slower");
                display.increasePeriod(true);
                break;
            case '-':
                System.out.println("Faster");
                display.increasePeriod(false);
                break;
            default:
                System.out.println("Press '+' or '-' to change speed.");
                break;
        }
    }

    @Override
    public void keyReleased(KeyEvent e) {
    }

}
