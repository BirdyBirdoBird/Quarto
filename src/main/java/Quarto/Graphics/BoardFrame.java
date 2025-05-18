/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */

package Quarto.Graphics;

import java.awt.BorderLayout;
import java.awt.GridLayout;

import javax.swing.JFrame;
import javax.swing.JOptionPane;
import javax.swing.JPanel;

import Quarto.Globals;
import Quarto.Logics.GameLogic;
import Quarto.Utils.Move;
import Quarto.Utils.Utils;

public class BoardFrame extends JFrame
{
    private JPanel boardPanel;
    public Control control;
    private Square[][] board;
    public GameLogic gameLogic;
    private String name1, name2;
    private boolean isOneWin;
    public boolean isGameOver;

    public BoardFrame (String name1, String name2)
    {
        setTitle("Querto Game Board");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setSize(800, 800);

        this.name1 = name1;
        this.name2 = name2;
        isOneWin = true;
        isGameOver = false;

        board = new Square[4][4];
        boardPanel = new JPanel(new GridLayout(4, 4));
        gameLogic = new GameLogic();

        for (int i = 0; i < 4; i++) {
            for (int j = 0; j < 4; j++) {
                final int row = i;
                final int col = j;
                Square square = new Square(row, col, this, false);
                board[i][j] = square;
                boardPanel.add(square);
            }
        }

        add(boardPanel, BorderLayout.CENTER);
        setVisible(true);
    }
 
    public void setControl (Control control)
    {
        this.control = control;
        for (int i = 0; i < 4; i++) {
            for (int j = 0; j < 4; j++) {
                board[i][j].setControl(control);
            }
        }
    }

    public void addPiece(Move move){
        addPiece(move.getRow(), move.getCol());
    }
    
    public void addPiece(byte row, byte col){
        addPiece(row, col, board[row][col]);
    }

    private void addPiece (byte row, byte col, Square square)
    {
        if (control.getSelectedPiece() != null && square.isEmpty)
        {
            Globals.emptySquares.remove(new Move(row, col));
            Square controlSquare = control.getSelectedPiece();
            square.addPiece(controlSquare.isRed, controlSquare.isHollow, controlSquare.isBig, controlSquare.isRound);
            board[row][col] = square;
            control.useSelectedPiece();
            gameLogic.addPiece((byte)square.row, (byte) square.col, Utils.encodePiece(square));
            checkGameOver();
            isOneWin = !isOneWin;
        }
    }

    public void resetBoard() {
        boardPanel.removeAll();
        isOneWin = true;
        isGameOver = false;

        board = new Square[4][4];
        gameLogic = new GameLogic();

        for (int i = 0; i < 4; i++) {
            for (int j = 0; j < 4; j++) {
                int row = i;
                int col = j;
                Square square = new Square(row, col, this, false);
                square.setControl(control);
                board[i][j] = square;
                boardPanel.add(square);
            }
        }

        boardPanel.revalidate();
        boardPanel.repaint();
    }

    private void checkGameOver() {
        if(gameLogic.isDraw()){
            JOptionPane.showMessageDialog(this, "Game Over! " + "Its a Draw!","Alert", JOptionPane.INFORMATION_MESSAGE);
            isGameOver = true;
        }
        if (gameLogic.isGameOver(false)) {
            JOptionPane.showMessageDialog(this, "Game Over! " + ((isOneWin) ? name2 : name1) + " Won!","Alert", JOptionPane.INFORMATION_MESSAGE);
            isGameOver = true;
        }
    }

}

