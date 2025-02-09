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

public class StartMenu extends JFrame implements ActionListener {

    private MenuPanel contentPane;
    private int selection;
    private JButton newGame, settings, exit;
    private JButton fightHuman, fightComputer, back;
    private JTextField player1, player2;
    private String name1, name2;
    private JLabel player1Label, player2Label;

    public StartMenu() {
        super("Start Menu");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setSize(1000, 600);
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

        newGame.setBounds(380, 170, 200, 70);
        settings.setBounds(380, 280, 200, 70);
        exit.setBounds(380, 390, 200, 70);
        fightHuman.setBounds(380, 170, 200, 70);
        fightComputer.setBounds(370, 280, 220, 70);
        back.setBounds(380, 390, 200, 70);

        player1Label.setBounds(200, 170, 200, 70);
        player2Label.setBounds(200, 280, 200, 70);
        player1.setBounds(450, 170, 400, 70);
        player2.setBounds(450, 280, 400, 70);

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
            g.setFont(new Font("Monospaced", Font.BOLD, 40));
            g.setColor(Color.BLACK);
            g.drawString("Querto", 410, 100);
            g.drawLine(345, 110, 630, 110);
            g.drawLine(375, 114, 600, 114);
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
            BoardFrame boardFrame = new BoardFrame(name1, name2);
            Control control = new Control(boardFrame, this);
            dispose();
        }

        if (e.getSource() == fightComputer) {
            BoardFrame boardFrame = new BoardFrame(name1, name2);
            Control control = new Control(boardFrame, this);
            dispose();
        }

        if (e.getSource() == back) {
            showStartMenu();
        }

    }
}
