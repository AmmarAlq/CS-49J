import javax.swing.*;
import javax.swing.plaf.LayerUI;

public class Window {
    public static void main(String[] args) {
        // Prompts user to pick a difficulty
        Start start = new Start();

        //Begins the game
        new Game(start.getDifficulty());
    }
}