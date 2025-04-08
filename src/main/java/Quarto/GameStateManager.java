/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */

package Quarto;

import java.awt.event.ActionEvent;

import javax.swing.Timer;

import Quarto.Graphics.BoardFrame;
import Quarto.Graphics.Control;
import Quarto.Graphics.StartMenu;
import Quarto.Utils.GameState;

/**
 *
 * @author david
 */
public class GameStateManager {
    private final BoardFrame frame;
    private final Control control;
    private Timer timer;
    public GameStateManager(boolean isBotPlaying, StartMenu startMenu){
        Constants.botPLaying = isBotPlaying;
        if(isBotPlaying){
            this.frame = new BoardFrame("player1", "bot");
        }
        else{
            this.frame = new BoardFrame("player1", "player2");
        }

        this.control = new Control(frame, startMenu);
        timer = new Timer(1000, (ActionEvent e) -> {
            // ((Timer) e.getSource()).stop();
            System.out.println(Constants.gameState);
            game(); // proceed to player's turn
        });
        timer.start();
    }

    public void game(){
        if(Constants.gameState == GameState.BOT_PLACE_MOVE){
            /// 
            Constants.gameState = GameState.BOT_SELECT_MOVE;
        }
        if(Constants.gameState == GameState.BOT_SELECT_MOVE){
            ///
            Constants.gameState = GameState.PLAYER_PLACE_MOVE;
        }
    }
    
}
