/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */

package Quarto.Graphics;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

import javax.swing.BorderFactory;
import javax.swing.JPanel;
import javax.swing.border.Border;

import Quarto.Globals;
import Quarto.Utils.GameState;

/**
 *
 * @author david
 */
public class Square extends JPanel {

    public boolean isEmpty;
    public boolean isBig, isRound, isRed, isHollow;
    public int row, col;
    private boolean isControl;
    private BoardFrame frame;
    private Control control;
    private Click click;

    public Square(int x, int y, BoardFrame frame, boolean isControl) {
        isEmpty = true;
        isBig = false;
        isRound = false;
        isRed = false;
        isHollow = false;
        this.isControl = isControl;
        row = x;
        col = y;
        this.frame = frame;
        click = new Click();
        addMouseListener(click);
        setBackground(Color.CYAN);
    }

    public Click getMouseClick ()
    {
        return click;
    }

    public void setControl(Control control) {
        this.control = control;
    }

    public void addPiece (boolean isRed, boolean isHollow, boolean isBig, boolean isRound)
    {
        this.isEmpty = false;
        this.isBig = isBig;
        this.isRound = isRound;
        this.isRed = isRed;
        this.isHollow = isHollow;
        repaint();
    }

    @Override
    public void paint(Graphics g) {
        super.paint(g);

        Border border = BorderFactory.createLineBorder(Color.BLACK, 2);
        setBorder(border);

        // If the square is empty, do not draw anything else
        if (isEmpty) {
            return;
        }

        if (isControl)
        {
            drawControl(g);
            return;
        }

        // Set piece color
        g.setColor(isRed ? Color.RED : Color.BLUE);

        // Determine size of the piece
        int size = isBig ? 160 : 80;
        int offset = (200 - size) / 2;

        if (isRound) {
            // Draw a circle
            g.fillOval(offset, offset, size, size);

            if (isHollow) {
                g.setColor(getBackground());
                g.fillOval(offset + 20, offset + 20, size - 40, size - 40);
            }
        } else {
            // Draw a square
            g.fillRect(offset, offset, size, size);

            if (isHollow) {
                g.setColor(getBackground());
                g.fillRect(offset + 20, offset + 20, size - 40, size - 40);
            }
        }
    }

    private void drawControl (Graphics g) {
        // Set piece color
        g.setColor(isRed ? Color.RED : Color.BLUE);

        // Determine size of the piece
        int size = isBig ? 80 : 50;
        int offset = (100 - size) / 2;

        if (isRound) {
            // Draw a circle
            g.fillOval(offset - 5, offset - 5, size - 5, size - 5);

            if (isHollow) {
                g.setColor(getBackground());
                g.fillOval(offset + 20 - 5, offset + 20 - 5, size - 40 - 5, size - 40 - 5);
            }
        } else {
            // Draw a square
            g.fillRect(offset - 5, offset - 5, size - 5, size - 5);

            if (isHollow) {
                g.setColor(getBackground());
                g.fillRect(offset + 20 - 5, offset + 20 - 5, size - 40 - 5, size - 40 - 5);
            }
        }
    }

    private Square getThis ()
    {
        return this;
    }

    private class Click extends MouseAdapter {

        @Override
        public void mousePressed(MouseEvent e) {
            if (frame.isGameOver) return;

            if(Globals.botPLaying){

                if (isEmpty && !isControl && Globals.gameState == GameState.PLAYER_PLACE_MOVE) {
                    frame.addPiece(row, col);
                    Globals.gameState = GameState.PLAYER_SELECT_MOVE;
                    return;
                }
    
                if (isControl && Globals.gameState == GameState.PLAYER_SELECT_MOVE)
                {
                    control.setSelectedPiece(getThis());
                    Globals.gameState = GameState.BOT_PLACE_MOVE;
                    setBackground(Color.YELLOW);
                    repaint();
                }
            }
            else{
                
                if (isEmpty && !isControl) {
                    frame.addPiece(row, col);
                    return;
                }
    
                if (isControl)
                {
                    control.setSelectedPiece(getThis());
                    setBackground(Color.YELLOW);
                    repaint();
                }
            }



        }
    }

}
