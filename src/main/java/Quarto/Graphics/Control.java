/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */

package Quarto.Graphics;
import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Component;
import java.awt.GridLayout;
import java.util.ArrayList;
import java.util.List;

import javax.swing.ButtonGroup;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JPanel;

import Quarto.Constants;
import Quarto.Utils.Utils;

public class Control extends JFrame
{
    private Square selectedPiece;
    public JPanel selectionPanel;
    private BoardFrame frame;
    private StartMenu startMenu;

    public Control (BoardFrame _frame, StartMenu menu)
    {
        setTitle("Piece Selection");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setSize(400, 800);
        setLocation(800, 0);

        startMenu = menu;
        this.frame = _frame;
        selectionPanel = new JPanel(new GridLayout(8, 2));
        ButtonGroup group = new ButtonGroup();
        frame.setControl(this);

        for (int i = 0; i < 16; i++) {
            boolean isRed = i < 8;
            boolean isBig = i % 2 == 0;
            boolean isRound = (i / 2) % 2 == 0;
            boolean isHollow = (i / 4) % 2 == 0;

            Square piece = new Square(i / 2, i % 2, this.frame, true);
            piece.addPiece(isRed, isBig, isRound, isHollow);
            piece.setControl(this);
            selectionPanel.add(piece);
            
            Constants.logicControl.add(Utils.encodePiece(piece));
        }

        JButton newGameButton = new JButton("New Game");
        newGameButton.setBackground(Color.GREEN);
        newGameButton.addActionListener(e -> {
            this.frame.resetBoard();
            resetSelection();
        });

        JButton backToMenuButton = new JButton("Back to Menu");
        backToMenuButton.setBackground(Color.RED);
        backToMenuButton.addActionListener(e -> {
            startMenu.setVisible(true);
            frame.dispose();
            dispose();
        });

        JPanel buttonPanel = new JPanel();
        buttonPanel.setLayout(new GridLayout(2, 1));
        buttonPanel.add(newGameButton);
        buttonPanel.add(backToMenuButton);

        add(selectionPanel, BorderLayout.CENTER);
        add(buttonPanel, BorderLayout.SOUTH);

        setVisible(true);
    }


    public void findPieceFromEncoded(int encoded) {
        for (int i = 0; i < selectionPanel.getComponentCount(); i++) {
            Square comp = (Square) selectionPanel.getComponent(i);
                int encodedValue = Utils.encodePiece(comp);
                if (encodedValue == encoded) {
                    setSelectedPiece(comp);
                }
        }
    }

    public void setSelectedPiece (Square piece)
    {
        if(!piece.isEmpty){
        for (int i = 0; i < 16; i++)
            {
                Square square = (Square) selectionPanel.getComponent(i);
                selectionPanel.getComponent(i).setBackground(Color.cyan);
                if (selectionPanel.getComponent(i).equals(piece)){
                    // square.isEmpty = true;
                    square.setBackground(Color.yellow);
                }
                selectionPanel.getComponent(i).repaint();
            }

        selectedPiece = piece;
        selectionPanel.repaint();
        }

    }

    public void setFrame(BoardFrame frame) {
        this.frame = frame;
    }

    public Square getSelectedPiece() {
        return selectedPiece;
    }

    public void useSelectedPiece() {
        if (selectedPiece != null) {
            for (int i = 0; i < 16; i++) {
                if (selectionPanel.getComponent(i).equals(selectedPiece))
                {
                    selectedPiece.isEmpty = true;
                    selectionPanel.getComponent(i).removeMouseListener(selectedPiece.getMouseClick());
                }
            }
            Constants.logicControl.removeFirstOccurrence(Utils.encodePiece(selectedPiece));
            selectedPiece = null;
        }
    }

    private void resetSelection() {
        selectionPanel.removeAll();
        for (int i = 0; i < 16; i++) {
            boolean isRed = i < 8;
            boolean isBig = i % 2 == 0;
            boolean isRound = (i / 2) % 2 == 0;
            boolean isHollow = (i / 4) % 2 == 0;

            Square piece = new Square(i / 2, i % 2, frame, true);
            piece.addPiece(isRed, isBig, isRound, isHollow);
            piece.setControl(this);
            selectionPanel.add(piece);
        }
        selectionPanel.revalidate();
        selectionPanel.repaint();
    }

    public List<Integer> getAvailablePieces() {
        List<Integer> availablePieces = new ArrayList<>();
    
        for (int i = 0; i < selectionPanel.getComponentCount(); i++) {
            if (selectionPanel.getComponent(i) instanceof Square) {
                Square piece = (Square) selectionPanel.getComponent(i);
                if (!piece.isEmpty) {
                    availablePieces.add(Utils.encodePiece(piece));
                }
            }
        }
    
        return availablePieces;
    }
    
}