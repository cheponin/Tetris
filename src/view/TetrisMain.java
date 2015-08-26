/*
 * TCSS 305
 * Assignment 6 - Tetris
 */

package view;

import java.awt.EventQueue;

/**
 * Runs the Tetris program by instantiating and starting the TetrisGUI. 
 * 
 * @author Nina Chepovska
 * @version May 21, 2015
 */
public final class TetrisMain {
    
    /**
     * Private constructor, to prevent instantiation of this class.
     */
    private TetrisMain() {
        throw new IllegalStateException();
    }

    /**
     * The starting point for this program. 
     * 
     * @param theArgs are the command line arguments, ignored in this program.
     */
    public static void main(final String[] theArgs) {
        EventQueue.invokeLater(new Runnable() {
            @Override
            public void run() {
                new TetrisGUI().start();
            }
        });
    }
}
