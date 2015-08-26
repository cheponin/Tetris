/*
 * TCSS 305
 * Assignment 6 - Tetris
 */

package view;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.util.Observable;
import java.util.Observer;

import javax.swing.JPanel;

import model.Point;
import model.TetrisPiece;

/**
 * A class that represents the preview of the next piece to fall in the Tetris game.
 * 
 * @author Nina Chepovska
 * @version May 23, 2015
 */
public final class PiecePreview extends JPanel implements Observer {
    
    /** The serial version id. */
    private static final long serialVersionUID = -2998591967701257920L;

    /** The size of this panel. */
    private static final Dimension PANEL_SIZE = new Dimension(150, 150);
    
    /** The color of this panel. */
    private static final Color BACKGROUND_COLOR = Color.WHITE;
    
    /** The size of the Tetris piece squares. */
    private static final int SQUARE_SIZE = 25;
    
    /** The x value used to offset the Tetris piece within the panel. */
    private static final int X_OFFSET = 40;
    
    /** The y value used to offset the Tetris piece within the panel. */
    private static final int Y_OFFSET = 100;
    
    /** The TetrisPiece to draw in this panel. */
    private TetrisPiece myTetrisPiece;
    
    /** The Game Over state. */
    private boolean myGameOver;

    /**
     * Constructs a new piece preview panel.
     */
    public PiecePreview() {
        super();
        
        setPreferredSize(PANEL_SIZE);
        setBackground(BACKGROUND_COLOR);
    }
    
    @Override
    public void paintComponent(final Graphics theGraphics) {
        super.paintComponent(theGraphics);
        final Graphics2D g2d = (Graphics2D) theGraphics;
        
        if (myGameOver) {
            setBackground(Color.GRAY);
        } else {
            setBackground(BACKGROUND_COLOR);
        }
        
        if (myTetrisPiece != null) {
            if (myGameOver) {
                g2d.setColor(Color.DARK_GRAY);
            } else {
                g2d.setColor(myTetrisPiece.getColor());
            }
            
            for (final Point point : myTetrisPiece.getPoints()) {
                g2d.fillRect(point.x() * SQUARE_SIZE + X_OFFSET, 
                             point.y() * -SQUARE_SIZE + Y_OFFSET,  SQUARE_SIZE, SQUARE_SIZE);
            }
            
            theGraphics.setColor(Color.WHITE);
            for (final Point point : myTetrisPiece.getPoints()) {
                theGraphics.drawRect(point.x() * SQUARE_SIZE + X_OFFSET, 
                                     point.y() * -SQUARE_SIZE + Y_OFFSET,  
                                     SQUARE_SIZE, SQUARE_SIZE);
            }
        }
    }
    
    /**
     * Updates the PiecePreview when something has changed in the Observable class.
     * 
     * @param theObject the observable object.
     * @param theData the data that has changed.
     */
    public void update(final Observable theObject, final Object theData) {
        if (theData instanceof TetrisPiece) {
            myTetrisPiece = (TetrisPiece) theData;
        }
        
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
}
