import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.Random;
import java.util.concurrent.Flow;
public class Game extends JFrame {
    private JButton[][] buttons;
    private boolean[][] mines;
    private int[][] adjacentMines;
    private int openedCells;

    private static final int ROWS = 5;

    private static final  int COLS = 5;

    private static final int TOTAL_MINES = 5;

    private int score;

    private boolean hasGameStarted = false;
    public Game() {
        setTitle("SJSU Minesweeper");
        setLayout(new GridLayout(ROWS, COLS));
        setDefaultCloseOperation(EXIT_ON_CLOSE);

        openedCells = 0;
        score = 0;
        buttons = new JButton[ROWS][COLS];
        mines = new boolean[ROWS][COLS];
        adjacentMines = new int[ROWS][COLS];

        for (int i = 0; i < ROWS; i++) {
            for (int j = 0; j < COLS; j++) {
                JButton button = new JButton();
                button.setBackground(Color.decode("#0055A2"));
                button.setOpaque(true);
                buttons[i][j] = button;
                buttons[i][j].addActionListener(new OpenCellListener(i, j));
                add(buttons[i][j]);
            }
        }

        pack();
        setVisible(true);
    }
    private void setMines(int startingRow, int startingCol) {
        Random num = new Random();
        int minesPlaced = 0;

        while (minesPlaced < TOTAL_MINES) {
            int i = num.nextInt(ROWS);
            int j = num.nextInt(COLS);

            if (!mines[i][j] && (i != startingRow && j != startingCol)) {
                mines[i][j] = true;
                minesPlaced++;
            }
        }
    }
    private void countAdjacentMines() {
        for (int i = 0; i < ROWS; i++) {
            for (int j = 0; j < COLS; j++) {
                if (!mines[i][j]) {
                    int total = 0;
                    if (i > 0 && mines[i - 1][j]) total++;
                    if (i < ROWS - 1 && mines[i + 1][j]) total++;
                    if (j > 0 && mines[i][j - 1]) total++;
                    if (j < COLS - 1 && mines[i][j + 1]) total++;
                    if (i > 0 && j > 0 && mines[i - 1][j - 1]) total++;
                    if (i < ROWS - 1 && j < COLS - 1 && mines[i + 1][j + 1]) total++;
                    if (i > 0 && j < COLS - 1 && mines[i - 1][j + 1]) total++;
                    if (i < ROWS - 1 && j > 0 && mines[i + 1][j - 1]) total++;
                    adjacentMines[i][j] = total;
                }
            }
        }
    }

    private void openCell(int i, int j, boolean isClicked) {
        if (!hasGameStarted) {
            setMines(i, j);
            countAdjacentMines();
            hasGameStarted = true;
        }
        if (isClicked) {
            score++;
            System.out.println(score);
        }
        if (mines[i][j]) {
            lose();
        }
        else {
            openedCells++;
            buttons[i][j].setText(String.valueOf(adjacentMines[i][j]));
            buttons[i][j].setEnabled(false);
            if (adjacentMines[i][j] == 0) {
                buttons[i][j].setText("");
                openAdjacentCells(i, j);
            }

            int unopenedCells = (ROWS * COLS) - openedCells;
            if (unopenedCells == TOTAL_MINES) {
                win();
            }
        }
    }

    private void openAdjacentCells(int i, int j) {
        if (i > 0 && buttons[i - 1][j].isEnabled()) openCell(i - 1, j, false);
        if (i < ROWS - 1 && buttons[i + 1][j].isEnabled()) openCell(i + 1, j, false);
        if (j > 0 && buttons[i][j - 1].isEnabled()) openCell(i, j - 1, false);
        if (j < COLS - 1 && buttons[i][j + 1].isEnabled()) openCell(i, j + 1, false);
        if (i > 0 && j > 0 && buttons[i - 1][j - 1].isEnabled()) openCell(i - 1, j - 1, false);
        if (i < ROWS - 1 && j < COLS - 1 && buttons[i + 1][j + 1].isEnabled()) openCell(i + 1, j + 1, false);
        if (i > 0 && j < COLS - 1 && buttons[i - 1][j + 1].isEnabled()) openCell(i - 1, j + 1, false);
        if (i < ROWS - 1 && j > 0 && buttons[i + 1][j - 1].isEnabled()) openCell(i + 1, j - 1, false);
    }

    private void win() {
        JOptionPane.showMessageDialog(this, "You won!");
        System.exit(0);
    }
    private void lose() {
        for (int i = 0; i < ROWS; i++) {
            for (int j = 0; j < COLS; j++) {
                if (mines[i][j]) {
                    buttons[i][j].setText("X");
                }
                buttons[i][j].setEnabled(false);
            }
        }

        JOptionPane.showMessageDialog(this, "You lost.");
        System.exit(0);
    }
    private class OpenCellListener implements ActionListener {
        private int row;
        private int col;

        public OpenCellListener(int i, int j) {
            this.row = i;
            this.col = j;
        }

        public void actionPerformed(ActionEvent event) {
            openCell(row, col, true);
        }
    }
}