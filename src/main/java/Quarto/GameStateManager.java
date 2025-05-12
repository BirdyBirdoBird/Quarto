/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */

package Quarto;

import java.awt.event.ActionEvent;

import javax.swing.Timer;

import Quarto.Bot.Bot;
import Quarto.Graphics.BoardFrame;
import Quarto.Graphics.Control;
import Quarto.Graphics.StartMenu;
import Quarto.Utils.GameState;

/**
 *
 * @author david
 */
public class GameStateManager {
    private final Bot bot;
    private final BoardFrame frame;
    private final Control control;
    private Timer timer;
    public GameStateManager(boolean isBotPlaying, StartMenu startMenu, String player1, String player2){
        bot = new Bot();
        Globals.botPLaying = isBotPlaying;
        if(isBotPlaying){
            this.frame = new BoardFrame(player1, "bot");
        }
        else{
            this.frame = new BoardFrame(player1, player2);
        }

        this.control = new Control(frame, startMenu);
        timer = new Timer(100, (ActionEvent e) -> {
            // ((Timer) e.getSource()).stop();
            System.out.println(Globals.gameState);
            game(); // proceed to player's turn
        });
        timer.start();
    }

    public void game(){
        if(Globals.gameState == GameState.BOT_PLACE_MOVE){
            frame.addPiece(bot.chooseMove());
            Globals.gameState = GameState.BOT_SELECT_MOVE;
        }
        if(Globals.gameState == GameState.BOT_SELECT_MOVE){
            control.findPieceFromEncoded(bot.choosePiece());
            Globals.gameState = GameState.PLAYER_PLACE_MOVE;
        }
    }
    
}
