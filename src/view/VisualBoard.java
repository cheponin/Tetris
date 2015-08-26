/*
 * TCSS 305
 * Assignment 6 - Tetris
 */

package view;

import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.util.List;
import java.util.Observable;
import java.util.Observer;

import javax.swing.JPanel;

/**
 * A class that is the visual representation of the Tetris game board.
 * 
 * @author Nina Chepovska
 * @version May 21, 2015
 */
public final class VisualBoard extends JPanel implements Observer {
    
    /** The serial version id. */
    private static final long serialVersionUID = 5366333624441599821L;
    
    /** The default color of this panel. */
    private static final Color DEFUALT_PANEL_COLOR = Color.WHITE;
    
    /** The size of the Tetris piece squares. */
    private static final int SQUARE_SIZE = 25;
    
    /** Value used to align rows on the board. */
    private static final int ROW_ALIGNMENT = 475;
    
    /** Game Over string font size. */
    private static final int GAME_OVER_FONT_SIZE = 46;
    
    /** Value used to align the Game Over string on the board. */
    private static final int GAME_OVER_ALIGNMENT = 250;
    
    /** The Game Over state. */
    private boolean myGameOver;
    
    /** The state of the grid, visible or not. */
    private boolean myGrid;
    
    /** A list of all 20 rows of the board. */
    private List<Color[]> myRows;

    /**
     * Constructs a new VisualBoard and initializes class fields. 
     */
    public VisualBoard() {
        super();
        
        setBackground(DEFUALT_PANEL_COLOR);
    }
    
    @Override
    public void paintComponent(final Graphics theGraphics) {
        super.paintComponent(theGraphics);
        final Graphics2D g2d = (Graphics2D) theGraphics;
        
        if (myRows != null) {
            paintRows(g2d);
            
            if (myGrid) {
                paintGrid(g2d);
            }
                        
            if (myGameOver) {
                setBackground(Color.GRAY);
                
                g2d.setColor(Color.WHITE);
                g2d.setFont(new Font("SansSerif", Font.BOLD, GAME_OVER_FONT_SIZE));
                g2d.drawString("Game Over", 0, GAME_OVER_ALIGNMENT);
            }
        }
    }    
    
    /**
     * Updates the VisualBoard when something has changed in the Observable class.
     * 
     * @param theObject the observable object.
     * @param theData the data that has changed.
     */
    public void update(final Observable theObject, final Object theData) {
        if (theData instanceof List<?>) {
            myRows = (List<Color[]>) theData;
        }
        
        repaint();
    }  
    
    /**
     * Sets the state of the grid, visible or not. 
     * 
     * @param theState is the state of the grid, true if visible, false otherwise.
     */
    protected void setGrid(final boolean theState) {
        myGrid = theState;
        repaint();
    }
    
    /**
     * Sets the game over state of the panel. 
     * 
     * @param theState is the game over state, true if game over, false otherwise.
     */
    protected void setGameOver(final boolean theState) {
        myGameOver = theState;
        repaint();
    }
    
    /**
     * Paints the rows of the board on the panel.
     * 
     * @param theGraphics is the graphics object to draw with.
     */
    private void paintRows(final Graphics theGraphics) {
        setBackground(DEFUALT_PANEL_COLOR);
        
        for (int r = myRows.size() - 1; r >= 0; r--) {
            final Color[] row = myRows.get(r);
            for (int i = 0; i < row.length; i++) {
                if (row[i] != null) {
                    if (myGameOver) {
                        theGraphics.setColor(Color.DARK_GRAY);
                    } else {
                        theGraphics.setColor(row[i]);
                    }
                    
                    theGraphics.fillRect(i * SQUARE_SIZE, ROW_ALIGNMENT - SQUARE_SIZE * r, 
                                 SQUARE_SIZE, SQUARE_SIZE);
                    
                    theGraphics.setColor(Color.WHITE);
                    theGraphics.drawRect(i * SQUARE_SIZE, ROW_ALIGNMENT - SQUARE_SIZE * r, 
                                 SQUARE_SIZE, SQUARE_SIZE);
                }
            }
        }
    }
    
    /**
     * Paints the grid on the panel.
     * 
     * @param theGraphics is the graphics object to draw with.
     */
    private void paintGrid(final Graphics theGraphics) {
        final int width = getWidth();
        final int height = getHeight();
        
        theGraphics.setColor(Color.GRAY);
        
        for (int i = 0; i < width; i += SQUARE_SIZE) {
            theGraphics.drawLine(i, 0, i, height);
        }
        
        for (int i = 0; i < height; i += SQUARE_SIZE) {
            theGraphics.drawLine(0, i, width, i);
        }
    }
}
