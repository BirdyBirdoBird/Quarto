/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */

package Quarto.Graphics;

import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextField;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;

import Quarto.GameStateManager;

public class StartMenu extends JFrame implements ActionListener {

    private MenuPanel contentPane;
    private JButton newGame, settings, exit;
    private JButton fightHuman, fightComputer, back;
    private JTextField player1, player2;
    private String name1, name2;
    private JLabel player1Label, player2Label;

    public StartMenu() {
        super("Start Menu");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setSize(700, 600);
        setLocationRelativeTo(null);
        setResizable(false);

        contentPane = new MenuPanel();
        contentPane.setLayout(null);
        contentPane.setBackground(Color.CYAN);

        init();
    }

    private void init ()
    {
        newGame = new JButton("New Game");
        settings = new JButton("Settings");
        exit = new JButton("Exit");

        fightHuman = new JButton("Player vs Player");
        fightComputer = new JButton("Player vs Computer");
        back = new JButton("Back");

        player1 = new JTextField(20);
        player2 = new JTextField(20);

        player1.setText("player 1");
        player2.setText("player 2");
        updateText1();
        updateText2();

        player1.setEditable(true);
        player2.setEditable(true);

        player1Label = new JLabel("Player 1: ");
        player2Label = new JLabel("Player 2: ");

        newGame.setFont(new Font("Arial", Font.BOLD, 20));
        settings.setFont(new Font("Arial", Font.BOLD, 20));
        exit.setFont(new Font("Arial", Font.BOLD, 20));
        fightHuman.setFont(new Font("Arial", Font.BOLD, 20));
        fightComputer.setFont(new Font("Arial", Font.BOLD, 20));
        back.setFont(new Font("Arial", Font.BOLD, 20));
        player1.setFont(new Font("Arial", Font.BOLD, 20));
        player2.setFont(new Font("Arial", Font.BOLD, 20));
        player1Label.setFont(new Font("Arial", Font.BOLD, 20));
        player2Label.setFont(new Font("Arial", Font.BOLD, 20));

        newGame.setBackground(Color.yellow);
        settings.setBackground(Color.yellow);
        exit.setBackground(Color.yellow);
        fightHuman.setBackground(Color.yellow);
        fightComputer.setBackground(Color.yellow);
        back.setBackground(Color.yellow);

        newGame.setBounds(250, 170, 200, 70);
        settings.setBounds(250, 280, 200, 70);
        exit.setBounds(250, 390, 200, 70);
        fightHuman.setBounds(260, 170, 200, 70);
        fightComputer.setBounds(250, 280, 220, 70);
        back.setBounds(260, 390, 200, 70);

        player1Label.setBounds(150, 170, 200, 70);
        player2Label.setBounds(150, 280, 200, 70);
        player1.setBounds(250, 170, 300, 70);
        player2.setBounds(250, 280, 300, 70);

        newGame.addActionListener(this);
        settings.addActionListener(this);
        exit.addActionListener(this);
        fightHuman.addActionListener(this);
        fightComputer.addActionListener(this);
        back.addActionListener(this);

        player1.getDocument().addDocumentListener(new DocumentListener() {
            @Override
            public void insertUpdate(DocumentEvent e) {
                updateText1();
            }

            @Override
            public void removeUpdate(DocumentEvent e) {
                updateText1();
            }

            @Override
            public void changedUpdate(DocumentEvent e) {
                updateText1();
            }
        });

        player2.getDocument().addDocumentListener(new DocumentListener() {
            @Override
            public void insertUpdate(DocumentEvent e) {
                updateText2();
            }

            @Override
            public void removeUpdate(DocumentEvent e) {
                updateText2();
            }

            @Override
            public void changedUpdate(DocumentEvent e) {
                updateText2();
            }
        });

        contentPane.add(newGame);
        contentPane.add(settings);
        contentPane.add(exit);
        contentPane.add(fightHuman);
        contentPane.add(fightComputer);
        contentPane.add(back);
        contentPane.add(player1Label);
        contentPane.add(player2Label);
        contentPane.add(player1);
        contentPane.add(player2);

        add(contentPane);
        setVisible(true);

        showStartMenu();
    }

    private void showStartMenu ()
    {
        newGame.setVisible(true);
        settings.setVisible(true);
        exit.setVisible(true);
        fightHuman.setVisible(false);
        fightComputer.setVisible(false);
        back.setVisible(false);
        player1.setVisible(false);
        player2.setVisible(false);
        player1Label.setVisible(false);
        player2Label.setVisible(false);
    }

    private void showNewGame ()
    {
        newGame.setVisible(false);
        settings.setVisible(false);
        exit.setVisible(false);
        fightHuman.setVisible(true);
        fightComputer.setVisible(true);
        back.setVisible(true);
    }

    private void showSettings ()
    {
        newGame.setVisible(false);
        settings.setVisible(false);
        exit.setVisible(false);
        player1.setVisible(true);
        player2.setVisible(true);
        player1Label.setVisible(true);
        player2Label.setVisible(true);
        back.setVisible(true);
    }

    private void updateText1 ()
    {
        name1 = player1.getText();
    }

    private void updateText2 ()
    {
        name2 = player2.getText();
    }

    private class MenuPanel extends JPanel {
        @Override
        public void paint(Graphics g) {
            super.paint(g);

            // Draw title
            g.setFont(new Font("Monospaced", Font.BOLD, 40));
            g.setColor(Color.BLACK);
            g.drawString("Quarto", 275, 50);

            // Draw 4 pieces below the title
            int startX = 160;  // starting x position
            int y = 60;

            drawPiece(g.create(startX, y, 100, 100), true, true, true, false);   // Big Red Round Solid
            drawPiece(g.create(startX + 100, y, 100, 100), false, true, false, false); // Small Red Square Solid
            drawPiece(g.create(startX + 200, y, 100, 100), true, false, true, true);   // Big Blue Round Hollow
            drawPiece(g.create(startX + 300, y, 100, 100), false, false, false, true); // Small Blue Square Hollow
        }

        /**
         * Draws a single Quarto piece using provided style flags
         * @param g Graphics (clipped to 100x100 box)
         * @param isBig true = big, false = small
         * @param isRed true = red, false = blue
         * @param isRound true = circle, false = square
         * @param isHollow true = hollow, false = solid
         */
        private void drawPiece(Graphics g, boolean isBig, boolean isRed, boolean isRound, boolean isHollow) {
            // Set piece color
            g.setColor(isRed ? Color.RED : Color.BLUE);

            // Determine size of the piece
            int size = isBig ? 50 : 30;
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
    }


    @Override
    public void actionPerformed(ActionEvent e) {

        if (e.getSource() == newGame) {
            showNewGame();
            return;
        }

        if (e.getSource() == settings) {
            showSettings();
            return;
        }

        if (e.getSource() == exit) {
            System.exit(42);
            return;
        }

        if (e.getSource() == fightHuman) {
            GameStateManager stateManager = new GameStateManager(false, this, name1, name2);
            dispose();
        }

        if (e.getSource() == fightComputer) {
            GameStateManager stateManager = new GameStateManager(true, this, name1, "bot");
            dispose();
        }

        if (e.getSource() == back) {
            showStartMenu();
        }

    }
}
