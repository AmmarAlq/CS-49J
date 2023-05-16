import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JTextField;

public class Start {
    private int difficulty;
    public Start() {
        Object[] options1 = { "Hard", "Medium",
                "Easy" };

        JPanel panel = new JPanel();
        panel.add(new JLabel("Select a difficulty:\n"));

        int result = JOptionPane.showOptionDialog(null, panel, "Welcome to SJSU Minesweeper!",
                JOptionPane.YES_NO_CANCEL_OPTION, JOptionPane.PLAIN_MESSAGE,
                null, options1, null);
        difficulty = -1 * (result - 2);

    }

    public int getDifficulty() {
        return difficulty;
    }
}