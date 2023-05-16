import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.util.Random;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

public class Game extends JFrame {

    //Button array for game board
    private JButton[][] buttons;

    //Gives each cell a flag to denote mine placement
    private boolean[][] mines;

    //Gives a value for how many mines each cell is adjacent to
    private int[][] adjacentMines;

    //Counter variable to keep track of how many cells have been revealed
    private int openedCells;

    //Game clock
    private int time = 0;

    //Number of rows in the game board
    private static int ROWS;

    //Number of columns in the game board
    private static int COLS;

    //Number of mines in the game board
    private static int TOTAL_MINES;

    //Sets the font size of the numbers on the board
    private Font font;

    //Sets the icon size for mines on the board
    private ImageIcon mine;

    //Sets the icon size for flags on the board
    private ImageIcon spartanFlag;

    //Flag to denote if the game has started
    private boolean hasGameStarted = false;

    //Flag to denote if the game has ended
    private boolean hasGameEnded = false;

    //Constructor - initializes settings, game board, and initializes JFrame and its components
    public Game(int difficulty) {

        setSettings(difficulty);
        setTitle("SJSU Minesweeper");
        setDefaultCloseOperation(EXIT_ON_CLOSE);

        //Initial panels for top bar with logo and timer
        JPanel top = new JPanel();
        JPanel icon = new JPanel();
        JPanel timer = new JPanel();

        //Configuration of the top bar
        top.setLayout(new GridLayout(1, 2));
        top.setPreferredSize(new Dimension(800, 80));
        top.setBackground(Color.decode("#E5A823"));
        icon.setBackground(Color.decode("#E5A823"));
        timer.setBackground(Color.decode("#E5A823"));
        timer.setLayout(new BorderLayout());
        icon.setLayout(new BorderLayout());

        //Elements of the top bar
        JLabel clock = new JLabel();
        JLabel logo = new JLabel();

        //Set logo in the top bar
        ImageIcon spartan = new ImageIcon(getClass().getResource("Spartan Logo.png"));
        logo.setIcon(spartan);

        //Configures the timer in the top bar, remains at 0 until first click
        icon.add(logo);
        timer.add(clock, BorderLayout.LINE_END);
        clock.setText(time + " sec");
        clock.setForeground(Color.decode("#0055A2"));
        Font timerFont = new Font("Courier", Font. BOLD,24);
        clock.setFont(timerFont);

        // Add to frame
        top.add(timer);
        top.add(icon);


        //Initialize the panel containing the game board
        JPanel gamePanel = new JPanel();
        gamePanel.setLayout(new GridLayout(ROWS, COLS));
        setPreferredSize(new Dimension(800, 600));

        //Initial state of the game board
        openedCells = 0;
        buttons = new JButton[ROWS][COLS];
        mines = new boolean[ROWS][COLS];
        adjacentMines = new int[ROWS][COLS];

        //Populate game board matrices
        for (int i = 0; i < ROWS; i++) {
            for (int j = 0; j < COLS; j++) {
                JButton button = new JButton();
                button.setBorder(BorderFactory.createLineBorder(Color.decode("#FFFFFF")));;
                button.setOpaque(true);
                button.setBackground(Color.decode("#0055A2"));
                buttons[i][j] = button;
                buttons[i][j].addMouseListener(new MinesweeperMouseListener(i, j));
                gamePanel.add(buttons[i][j]);
            }
        }

        //Add to game panel
        add(top, BorderLayout.BEFORE_FIRST_LINE);
        add(gamePanel, BorderLayout.CENTER);

        pack();
        setVisible(true);
        setResizable(false);

        //Starts the timer upon first click; freezes when game is over
        Runnable clockRunnable = new Runnable() {
            public void run() {
                if (hasGameStarted && !hasGameEnded) {
                    time++;
                    clock.setText(time + " sec");
                }
            }
        };

        ScheduledExecutorService executor = Executors.newScheduledThreadPool(1);
        executor.scheduleAtFixedRate(clockRunnable, 0, 1, TimeUnit.SECONDS);

    }

    /**
     * Creates the game board based on difficulty level input from the user
     * @param difficulty
     */
    private void setSettings(int difficulty) {

        switch(difficulty) {
            case 0:
                ROWS = 5;
                COLS = 5;
                TOTAL_MINES = 5;
                font = new Font("Courier", Font. BOLD,42);
                mine = new ImageIcon(getClass().getResource("Bomb Icon Easy.png"));
                spartanFlag = new ImageIcon(getClass().getResource("SJSU Flag Easy.png"));
                break;

            case 1:
                ROWS = 8;
                COLS = 10;
                TOTAL_MINES = 10;
                font = new Font("Courier", Font. BOLD,33);
                mine = new ImageIcon(getClass().getResource("Bomb Icon Medium.png"));
                spartanFlag = new ImageIcon(getClass().getResource("SJSU Flag Medium.png"));
                break;

            case 2:
                ROWS = 10;
                COLS = 12;
                TOTAL_MINES = 15;
                font = new Font("Courier", Font. BOLD,24);
                mine = new ImageIcon(getClass().getResource("Bomb Icon Hard.png"));
                spartanFlag = new ImageIcon(getClass().getResource("SJSU Flag Hard.png"));
                break;

            default:
                break;
        }
    }

    /**
     * Places mines on the board, ensures first selection is not a mine
     * @param startingRow
     * @param startingCol
     */
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

    /**
     * Counts the number of mines adjacent to each cell
     */
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

    /**
     * Reveals the value of the cell
     * @param i
     * @param j
     */
    private void openCell(int i, int j) {
        //If button has flag, do not proceed
        if (buttons[i][j].getIcon() != null) {
            return;
        }

        //If this is the first click of the game, set mines to ensure that none are placed on this cell
        if (!hasGameStarted) {
            setMines(i, j);
            countAdjacentMines();
            hasGameStarted = true;
        }

        //If the user has clicked a mine, player has lost
        if (mines[i][j]) {
            lose();
        }
        else {
            //Increment open cells, disable button, and set styling for an open cell
            openedCells++;
            buttons[i][j].setText(String.valueOf(adjacentMines[i][j]));
            buttons[i][j].setEnabled(false);
            buttons[i][j].setBackground(Color.decode("#E5A823"));
            buttons[i][j].setFont(font);
            if (adjacentMines[i][j] == 0) {
                buttons[i][j].setText("");
                openAdjacentCells(i, j);
            }

            //If user has uncovered all non-mine cells, win game
            int unopenedCells = (ROWS * COLS) - openedCells;
            if (unopenedCells == TOTAL_MINES) {
                win();
            }
        }
    }

    /**
     * Reveals all the cells adjacent to a cell; only called if the cell being passed in is not touching any mines
     * @param i
     * @param j
     */
    private void openAdjacentCells(int i, int j) {
        if (i > 0 && buttons[i - 1][j].isEnabled()) openCell(i - 1, j);
        if (i < ROWS - 1 && buttons[i + 1][j].isEnabled()) openCell(i + 1, j);
        if (j > 0 && buttons[i][j - 1].isEnabled()) openCell(i, j - 1);
        if (j < COLS - 1 && buttons[i][j + 1].isEnabled()) openCell(i, j + 1);
        if (i > 0 && j > 0 && buttons[i - 1][j - 1].isEnabled()) openCell(i - 1, j - 1);
        if (i < ROWS - 1 && j < COLS - 1 && buttons[i + 1][j + 1].isEnabled()) openCell(i + 1, j + 1);
        if (i > 0 && j < COLS - 1 && buttons[i - 1][j + 1].isEnabled()) openCell(i - 1, j + 1);
        if (i < ROWS - 1 && j > 0 && buttons[i + 1][j - 1].isEnabled()) openCell(i + 1, j - 1);
    }

    /**
     * Shows the congratulatory message after a victory along with time elapsed
     */
    private void win() {
        hasGameEnded = true;
        JOptionPane.showMessageDialog(this, "You won!\n" + "Time: " + time + " seconds");
        System.exit(0);
    }

    /**
     * If mine is clicked, game ends and window is triggered to display the losing message
     */
    private void lose() {
        hasGameEnded = true;
        for (int i = 0; i < ROWS; i++) {
            for (int j = 0; j < COLS; j++) {
                if (mines[i][j]) {
                    buttons[i][j].setIcon(mine);
                    buttons[i][j].setBackground(Color.decode("#E5A823"));
                }
            }
        }

        JOptionPane.showMessageDialog(this, "You lost.");
        System.exit(0);
    }

    /**
     * Class for handling mouse events
     */
    private class MinesweeperMouseListener implements MouseListener {
        private int row;
        private int col;
        private boolean isFlagSet;

        public void mousePressed(java.awt.event.MouseEvent evt) {

        }
        public void mouseReleased(java.awt.event.MouseEvent evt) {

        }
        public void mouseEntered(java.awt.event.MouseEvent evt)
        {
            Component button = evt.getComponent();
            if (button.isEnabled()) {
                button.setBackground(Color.decode("#014685"));
            }
        }

        public void mouseExited(java.awt.event.MouseEvent evt)
        {
            Component button = evt.getComponent();

            if (button.isEnabled()) {
                button.setBackground(Color.decode("#0055A2"));
            }
        }
        public void mouseClicked(java.awt.event.MouseEvent evt) {
            //Right click
            if (evt.getModifiersEx() == 256) {
                //If there is no flag and the cell is still unrevealed, place a flag
                if (!isFlagSet && buttons[row][col].isEnabled()) {
                    isFlagSet = true;
                    buttons[row][col].setIcon(spartanFlag);
                }
                //If there is a flag and the cell is still unrevealed, remove the flag
                else if (isFlagSet && buttons[row][col].isEnabled()) {
                    isFlagSet = false;
                    buttons[row][col].setIcon(null);
                }
            }
            //Left click
            if (evt.getModifiersEx() == 0) {
                openCell(row, col);
            }
        }
        //Constructor
        public MinesweeperMouseListener(int i, int j) {
            this.row = i;
            this.col = j;
            isFlagSet = false;
        }
    }
}