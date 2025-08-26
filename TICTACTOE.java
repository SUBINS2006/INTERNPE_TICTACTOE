import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.util.List;   // ✅ correct List
import java.util.ArrayList;
import java.util.Random;


public class TicTacToeAI extends JFrame implements ActionListener {
    private JButton[][] buttons = new JButton[3][3];
    private boolean xTurn = true;  // Human = X, Computer = O
    private JLabel statusLabel;
    private int moves = 0;
    private Random rand = new Random();

    public TicTacToeAI() {
        setTitle("Tic-Tac-Toe (Play vs Computer)");
        setDefaultCloseOperation(EXIT_ON_CLOSE);
        setLayout(new BorderLayout());
        JPanel board = new JPanel(new GridLayout(3, 3, 5, 5));
        board.setBorder(BorderFactory.createEmptyBorder(10,10,10,10));

        Font btnFont = new Font(Font.SANS_SERIF, Font.BOLD, 48);
        for (int r = 0; r < 3; r++) {
            for (int c = 0; c < 3; c++) {
                JButton b = new JButton("");
                b.setFont(btnFont);
                b.setFocusPainted(false);
                b.addActionListener(this);
                buttons[r][c] = b;
                board.add(b);
            }
        }

        statusLabel = new JLabel("Your Turn (X)");
        statusLabel.setHorizontalAlignment(SwingConstants.CENTER);
        statusLabel.setBorder(BorderFactory.createEmptyBorder(8,8,8,8));

        JButton restart = new JButton("Restart");
        restart.addActionListener(e -> resetGame());

        JPanel bottom = new JPanel(new BorderLayout());
        bottom.add(statusLabel, BorderLayout.CENTER);
        bottom.add(restart, BorderLayout.EAST);

        add(board, BorderLayout.CENTER);
        add(bottom, BorderLayout.SOUTH);

        setSize(400, 450);
        setLocationRelativeTo(null);
        setVisible(true);
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        if (!xTurn) return; // ignore clicks when it's computer's turn
        JButton src = (JButton)e.getSource();
        if (!src.getText().isEmpty()) return;
        src.setText("X");
        moves++;
        if (checkGameOver()) return;
        xTurn = false;
        statusLabel.setText("Computer's Turn (O)");
        SwingUtilities.invokeLater(() -> computerMove());
    }

    private void computerMove() {
        List<JButton> emptyCells = new ArrayList<>();
        for (int r=0;r<3;r++) {
            for (int c=0;c<3;c++) {
                if (buttons[r][c].getText().isEmpty()) {
                    emptyCells.add(buttons[r][c]);
                }
            }
        }
        if (!emptyCells.isEmpty()) {
            JButton choice = emptyCells.get(rand.nextInt(emptyCells.size()));
            choice.setText("O");
            moves++;
        }
        if (checkGameOver()) return;
        xTurn = true;
        statusLabel.setText("Your Turn (X)");
    }

    private boolean checkGameOver() {
        String winner = getWinner();
        if (winner != null) {
            statusLabel.setText("Winner: " + winner + " — Click Restart to play again");
            disableBoard();
            return true;
        } else if (moves == 9) {
            statusLabel.setText("Draw! — Click Restart to play again");
            return true;
        }
        return false;
    }

    private String getWinner() {
        String[][] grid = new String[3][3];
        for (int r=0; r<3; r++) for (int c=0; c<3; c++) grid[r][c] = buttons[r][c].getText();

        for (int i=0; i<3; i++) {
            if (!grid[i][0].isEmpty() && grid[i][0].equals(grid[i][1]) && grid[i][1].equals(grid[i][2])) return grid[i][0];
            if (!grid[0][i].isEmpty() && grid[0][i].equals(grid[1][i]) && grid[1][i].equals(grid[2][i])) return grid[0][i];
        }
        if (!grid[0][0].isEmpty() && grid[0][0].equals(grid[1][1]) && grid[1][1].equals(grid[2][2])) return grid[0][0];
        if (!grid[0][2].isEmpty() && grid[0][2].equals(grid[1][1]) && grid[1][1].equals(grid[2][0])) return grid[0][2];

        return null;
    }

    private void disableBoard() {
        for (int r=0;r<3;r++) for (int c=0;c<3;c++) buttons[r][c].setEnabled(false);
    }

    private void resetGame() {
        xTurn = true;
        moves = 0;
        for (int r=0;r<3;r++) for (int c=0;c<3;c++) {
            buttons[r][c].setText("");
            buttons[r][c].setEnabled(true);
        }
        statusLabel.setText("Your Turn (X)");
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(TicTacToeAI::new);
    }
}
