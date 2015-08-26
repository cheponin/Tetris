/*
 * TCSS 305
 * Assignment 6 - Tetris
 */

package view;

import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.awt.event.WindowEvent;
import java.util.Observable;
import java.util.Observer;

import javax.swing.Box;
import javax.swing.BoxLayout;
import javax.swing.ImageIcon;
import javax.swing.JCheckBoxMenuItem;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.Timer;

import model.Board;

/**
 * This class represents the graphical user interface of the Tetris program.
 * 
 * @author Nina Chepovska
 * @version May 21, 2015
 */
public final class TetrisGUI implements Observer {
    
    /** The icon of the frame. */
    private static final ImageIcon FRAME_ICON = new ImageIcon("images//tetris-bw.png");
    
    /** The icon for the About dialog. */
    private static final ImageIcon ABOUT_ICON = new ImageIcon("images//tetris-icon.png");

    /** The default size of this panel. */
    private static final Dimension DEFAULT_FRAME_SIZE = new Dimension(455, 550);
    
    /** The delay of the timer in milliseconds. */
    private static final int INITIAL_TIMER_DELAY = 1000; 
    
    /** The decrement step of the timer when game levels up. */
    private static final int TIMER_LEVEL_UP_DECREMENT = 150;
    
    /** The size of the two east panels. */
    private static final Dimension EAST_PANELS_SIZE = new Dimension(200, 200);
    
    /** The padding between the two east panels. */
    private static final int EAST_PANEL_PADDING = 100;
    
    /** The number of scoring labels in the scoring panel. */
    private static final int NUMBER_SCORING_LABELS = 4;
    
    /** The number of points awarded for clearing one line. */
    private static final int POINTS_PER_LINE = 10;
    
    /** The score string used for the score label. */
    private static final String SCORE_STRING = "Score: ";
    
    /** The level string used for the level label. */
    private static final String LEVEL_STRING = "Level: ";
    
    /** The total lines string used for the total lines label. */
    private static final String TOTAL_LINES_STRING = "Total Lines: ";
    
    /** The level up string used for the level up label. */
    private static final String LEVEL_UP_STRING = "Level Up in: ";
    
    /** The frame of this GUI. */
    private final JFrame myFrame;
    
    /** The timer used to animate and step through the game. */
    private final Timer myTimer;
    
    /** The board object that represents the Tetris game. */
    private final Board myBoard;
    
    /** A panel which is a visual representation of the board object. */
    private VisualBoard myVisualBoard;
    
    /** A panel which displays the next piece in the game. */
    private PiecePreview myPiecePreview;
    
    /** The label showing the current level. */
    private JLabel myLevelLabel;
    
    /** The label showing the current score. */
    private JLabel myScoreLabel;
    
    /** The label showing the total lines cleared. */
    private JLabel myLinesLabel;
    
    /** The label showing the lines until level up. */
    private JLabel myLevelUpLabel;
    
    /** The Game Over state. */
    private boolean myGameOver;
    
    /** The Pause state. */
    private boolean myPaused;
    
    /** The current level of the game. */
    private int myLevel;
    
    /** The current score of the game. */
    private int myScore;
    
    /** The number of lines to clear until level up. */
    private int myLevelUp;
    
    /** Total lines cleared in the game so far. */
    private int myTotalLinesCleared;

    /**
     * Creates a new instance of this class and initializes class fields.
     */
    public TetrisGUI() {
        myFrame = new JFrame("Tetris");
        myBoard = new Board();
        myTimer = new Timer(INITIAL_TIMER_DELAY, new TimerActionListener());
        
        myFrame.addKeyListener(new GameKeyListener());
    }
    
    /**
     * The start point for the creation of the GUI.
     */
    public void start() {
        myFrame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        myFrame.setIconImage(FRAME_ICON.getImage());
        
        addComponents();
        
        myFrame.setPreferredSize(DEFAULT_FRAME_SIZE);
        myFrame.pack();
        myFrame.setMinimumSize(myFrame.getSize());
        myFrame.setResizable(false);
        
        myFrame.setLocationRelativeTo(null);
        
        myFrame.setVisible(true);
                
        myBoard.addObserver(this);
        myBoard.addObserver(myVisualBoard);
        myBoard.addObserver(myPiecePreview);
    }
    
    /**
     * Updates the GUI when something has changed in the Observable class.
     * 
     * @param theObject the observable object.
     * @param theData the data that has changed.
     */
    public void update(final Observable theObject, final Object theData) {
        if (theData instanceof Boolean) {
            myGameOver = true;
            myVisualBoard.setGameOver(myGameOver);
            myPiecePreview.setGameOver(myGameOver);
        } else if (theData instanceof Integer[]) {
            final Integer[] linesCleared = (Integer[]) theData;
            
            myScore += linesCleared.length * POINTS_PER_LINE;
            myScoreLabel.setText(SCORE_STRING + myScore);
                        
            myTotalLinesCleared += linesCleared.length;
            myLinesLabel.setText(TOTAL_LINES_STRING + myTotalLinesCleared);
            
            myLevel = (myTotalLinesCleared / POINTS_PER_LINE) + 1;
            myLevelLabel.setText(LEVEL_STRING + myLevel);
            
            myTimer.setDelay(INITIAL_TIMER_DELAY - (myLevel * TIMER_LEVEL_UP_DECREMENT) 
                             + TIMER_LEVEL_UP_DECREMENT);
            
            myLevelUp = (POINTS_PER_LINE * myLevel) - myTotalLinesCleared;
            myLevelUpLabel.setText(LEVEL_UP_STRING + myLevelUp);
        }
    }
    
    /**
     * Adds components to the frame.
     */
    private void addComponents() {        
        myVisualBoard = new VisualBoard();      
        final JPanel infoPanel = createInfoPanel();
        final JMenuBar menuBar = createMenuBar();
        
        myFrame.setJMenuBar(menuBar);
        myFrame.add(myVisualBoard, BorderLayout.CENTER);
        myFrame.add(infoPanel, BorderLayout.EAST);
    }
    
    /**
     * Creates a menu bar for the frame which has Game, Options, and Help menus.
     * 
     * @return the menu bar.
     */
    private JMenuBar createMenuBar() {
        final JMenuBar menuBar = new JMenuBar();
        
        final JMenu gameMenu = createGameMenu();
        final JMenu optionsMenu = createOptionsMenu();
        final JMenu helpMenu = createHelpMenu();
        
        menuBar.add(gameMenu);
        menuBar.add(optionsMenu);
        menuBar.add(helpMenu);
        
        return menuBar;
    }
    
    /**
     * Creates the game menu with New Game and End Game options.
     * 
     * @return the game menu.
     */
    private JMenu createGameMenu() {
        final JMenu gameMenu = new JMenu("Game");
        
        final JMenuItem newGameMenuItem = new JMenuItem("New Game");
        final JMenuItem endGameMenuItem = new JMenuItem("End Game");
        final JMenuItem exitMenuItem = new JMenuItem("Exit");
        
        newGameMenuItem.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(final ActionEvent theEvent) {
                endGameMenuItem.setEnabled(true);
                newGameMenuItem.setEnabled(false);
                
                myScoreLabel.setText(SCORE_STRING + 0);
                myLevelLabel.setText(LEVEL_STRING + 1);
                myLinesLabel.setText(TOTAL_LINES_STRING + 0);
                myLevelUpLabel.setText(LEVEL_UP_STRING + POINTS_PER_LINE);
                
                myTimer.setDelay(INITIAL_TIMER_DELAY);
                myGameOver = false;
                myVisualBoard.setGameOver(myGameOver);
                myPiecePreview.setGameOver(myGameOver);
                myBoard.newGame();
                myTimer.start();
            }
        });        
        
        endGameMenuItem.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(final ActionEvent theEvent) {
                endGameMenuItem.setEnabled(false);
                newGameMenuItem.setEnabled(true);
                
                myScore = 0;
                myLevel = 0;
                myTotalLinesCleared = 0;
                myLevelUp = 0;
                
                myGameOver = true;
                myVisualBoard.setGameOver(myGameOver);
                myPiecePreview.setGameOver(myGameOver);
                myTimer.stop();
            }
        });        
        
        exitMenuItem.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(final ActionEvent theEvent) {
                myFrame.dispatchEvent(new WindowEvent(myFrame, WindowEvent.WINDOW_CLOSING));
            }
        });
        
        endGameMenuItem.setEnabled(false);
        
        gameMenu.add(newGameMenuItem);
        gameMenu.add(endGameMenuItem);
        gameMenu.addSeparator();
        gameMenu.add(exitMenuItem);
        
        return gameMenu;
    }
    
    /**
     * Creates the options menu with a Grid option.
     * 
     * @return the options menu.
     */
    private JMenu createOptionsMenu() {
        final JMenu optionsMenu = new JMenu("Options");
        
        final JCheckBoxMenuItem gridCheckBox = new JCheckBoxMenuItem("Grid");
        gridCheckBox.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(final ActionEvent theEvent) {
                myVisualBoard.setGrid(gridCheckBox.isSelected());
            }
        });
        
        optionsMenu.add(gridCheckBox);
        
        return optionsMenu;
    }
    
    /**
     * Creates the help menu with an About option.
     * 
     * @return the help menu.
     */
    private JMenu createHelpMenu() {
        final JMenu helpMenu = new JMenu("Help");
        
        final JMenuItem aboutMenuItem = new JMenuItem("About...");
        aboutMenuItem.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(final ActionEvent theEvent) {
                final String m = "TCSS 305 Spring 2015 -- Tetris\n\n" + getControlKeysString() 
                                + getScoringString();
                JOptionPane.showMessageDialog(myFrame, m, "About", 
                                              JOptionPane.INFORMATION_MESSAGE, ABOUT_ICON);
            }
        });
        
        helpMenu.add(aboutMenuItem);
        
        return helpMenu;
    }
    
    /**
     * Creates an information panel which will hold next piece and scoring sub-panels.
     * 
     * @return the info panel.
     */
    private JPanel createInfoPanel() {
        final JPanel infoPanel = new JPanel();
        infoPanel.setLayout(new BoxLayout(infoPanel, BoxLayout.PAGE_AXIS));
        
        final JPanel nextPiecePanel = new JPanel();
        myPiecePreview = new PiecePreview();
        nextPiecePanel.add(myPiecePreview);
        nextPiecePanel.setPreferredSize(EAST_PANELS_SIZE);
        
        final JPanel scoringPanel = new JPanel();
        scoringPanel.setLayout(new GridLayout(NUMBER_SCORING_LABELS, 1));
        myLevelLabel = new JLabel(LEVEL_STRING + myLevel);
        myScoreLabel = new JLabel(SCORE_STRING + myScore);
        myLinesLabel = new JLabel(TOTAL_LINES_STRING + myTotalLinesCleared);
        myLevelUpLabel = new JLabel(LEVEL_UP_STRING + myLevelUp);
        scoringPanel.add(myLevelLabel);
        scoringPanel.add(myScoreLabel);
        scoringPanel.add(myLinesLabel);
        scoringPanel.add(myLevelUpLabel);
        scoringPanel.setPreferredSize(EAST_PANELS_SIZE);
        
        final Box infoBox = new Box(BoxLayout.PAGE_AXIS);
        infoBox.add(nextPiecePanel);
        infoBox.add(Box.createVerticalStrut(EAST_PANEL_PADDING));
        infoBox.add(scoringPanel);
        
        infoPanel.add(infoBox);
        
        return infoPanel;
    }
    
    /**
     * Gets the String describing the keys used to control the game.
     * 
     * @return a String describing the control keys.
     */
    private String getControlKeysString() {
        final StringBuilder ret = new StringBuilder(150);
        
        ret.append("Left = Left Arrow\nRight = Right Arrow\nDown = Down Arrow\n" 
                   + "Drop = Space\nRotate CW = Z\nRotate CCW = X\nPause = P\n\n");
        
        return ret.toString();
    }
    
    /**
     * Gets the String describing the scoring of the game.
     * 
     * @return a String describing the scoring.
     */
    private String getScoringString() {
        final StringBuilder ret = new StringBuilder(150);
        
        ret.append("Scoring:\n10 points for each cleared line.\n" 
                   + "Level up after clearing 10 lines.\n");
        
        return ret.toString();
    }
    
    /**
     * A key listener class that listens for key presses to determine how to which way to move 
     * a Tetris piece.
     * 
     * @author Nina Chepovska
     * @version May 21, 2015
     */
    private class GameKeyListener extends KeyAdapter {
        @Override
        public void keyPressed(final KeyEvent theEvent) {
            if (!myGameOver) {
                final int key = theEvent.getKeyCode();
                
                if (!myPaused) {                    
                    switch (key) {
                        case KeyEvent.VK_RIGHT:
                            myBoard.right();
                            break;
                        case KeyEvent.VK_LEFT:
                            myBoard.left();
                            break;
                        case KeyEvent.VK_DOWN:
                            myBoard.down();
                            break;
                        case KeyEvent.VK_SPACE: 
                            myBoard.drop();
                            break;
                        case KeyEvent.VK_Z:
                            myBoard.rotateCW();
                            break;
                        case KeyEvent.VK_X:
                            myBoard.rotateCCW();
                            break;
                        default: 
                            // do nothing
                    }
                }
                
                if (key == KeyEvent.VK_P) {
                    setPaused();
                }
            }
        }
        
        /**
         * Pauses or resumes the game. 
         */
        private void setPaused() {
            if (myTimer.isRunning()) {
                myTimer.stop();
                myPaused = true;
            } else {
                myTimer.start();
                myPaused = false;
            }
        }
    }
    
    /**
     * An action listener class that handles events created by the timer.
     * 
     * @author Nina Chepovska
     * @version May 21, 2015
     */
    private class TimerActionListener implements ActionListener {
        @Override
        public void actionPerformed(final ActionEvent theEvent) {
            if (!myGameOver) {
                myBoard.step();
            }
        }
    }
}
