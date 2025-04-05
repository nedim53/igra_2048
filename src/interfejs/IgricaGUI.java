// Paket koji sadrži klasu IgricaGUI
package interfejs;

import java.awt.*;
import java.awt.event.*;
import java.sql.*;
import javax.swing.*;
import logika.Logika2048;

/**
 * Klasa koja implementira grafičko korisničko sučelje za igru 2048.
 * Omogućava igranje igre, prikaz rezultata i funkcije za čuvanje
 * i učitavanje stanja igre.
 */
public class IgricaGUI {
    /** Glavni prozor igre. */
    private JFrame frame;

    /** Panel za prikaz igrališta igre. */
    private JPanel boardPanel;

    /** Matrica za prikaz pločica na igralištu. */
    private JLabel[][] tiles;

    /** Labela za prikaz trenutnog rezultata. */
    private JLabel scoreLabel;

    /** Instanca logike igre 2048. */
    private Logika2048 game;

    /** Boje za različite vrijednosti pločica. */
    private static final Color[] TILE_COLORS = {
        new Color(0xCDC1B4), // Prazna pločica
        new Color(0xEEE4DA), // 2
        new Color(0xEDE0C8), // 4
        new Color(0xF2B179), // 8
        new Color(0xF59563), // 16
        new Color(0xF67C5F), // 32
        new Color(0xF65E3B), // 64
        new Color(0xEDCF72), // 128
        new Color(0xEDCC61), // 256
        new Color(0xEDC850), // 512
        new Color(0xEDC53F), // 1024
        new Color(0xEDC22E)  // 2048
    };

    /** Veličina igrališta. */
    private int size;

    /**
     * Konstruktor klase koji inicijalizira grafički interfejs igre.
     * Postavlja elemente sučelja, događaje i ažurira ploču.
     * @param size Veličina igrališta (npr. 4 za 4x4).
     */
    public IgricaGUI(int size) {
        this.size = size;
        game = new Logika2048(size); // Inicijalizacija igre sa prilagođenim dimenzijama

        // Kreiranje glavnog prozora
        frame = new JFrame("2048 Igrica - " + size + "x" + size);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setSize(400, 550);
        frame.setLocationRelativeTo(null);
        frame.setLayout(new BorderLayout());

        // Kreira labelu za prikaz rezultata
        scoreLabel = new JLabel("Score: 0", SwingConstants.CENTER);
        scoreLabel.setFont(new Font("Arial", Font.BOLD, 24));
        frame.add(scoreLabel, BorderLayout.NORTH);

        // Kreira panel za igralište
        boardPanel = new JPanel(new GridLayout(size, size, 10, 10)); // Dinamične dimenzije
        boardPanel.setBackground(new Color(0xBBADA0));
        boardPanel.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));

        // Inicijalizuje pločice prema veličini igre
        tiles = new JLabel[size][size];
        for (int row = 0; row < size; row++) {
            for (int col = 0; col < size; col++) {
                tiles[row][col] = new JLabel("", SwingConstants.CENTER);
                tiles[row][col].setOpaque(true);
                tiles[row][col].setBackground(TILE_COLORS[0]);
                tiles[row][col].setFont(new Font("Arial", Font.BOLD, 24));
                tiles[row][col].setForeground(Color.DARK_GRAY);
                boardPanel.add(tiles[row][col]);
            }
        }

        frame.add(boardPanel, BorderLayout.CENTER);

        // Kreira dugmad za čuvanje i učitavanje igre
        JPanel buttonPanel = new JPanel(new GridLayout(1, 2, 10, 10));
        JButton saveButton = new JButton("Save Game");
        JButton loadButton = new JButton("Load Game");

        saveButton.addActionListener(e -> saveGame());
        loadButton.addActionListener(e -> loadGame());

        buttonPanel.add(saveButton);
        buttonPanel.add(loadButton);

        frame.add(buttonPanel, BorderLayout.SOUTH);

        configureKeyBindings(); // Postavlja događaje za strelice
        updateBoard(); // Ažurira prikaz ploče
        frame.setFocusable(true);
        frame.setVisible(true);
    }

    /**
     * Postavlja događaje za tipke W, A, S, D koji omogućavaju pomjeranje u igri.
     */
    private void configureKeyBindings() {
        InputMap inputMap = frame.getRootPane().getInputMap(JComponent.WHEN_IN_FOCUSED_WINDOW);
        ActionMap actionMap = frame.getRootPane().getActionMap();

        inputMap.put(KeyStroke.getKeyStroke("W"), "moveUp");
        inputMap.put(KeyStroke.getKeyStroke("A"), "moveLeft");
        inputMap.put(KeyStroke.getKeyStroke("S"), "moveDown");
        inputMap.put(KeyStroke.getKeyStroke("D"), "moveRight");

        actionMap.put("moveUp", new AbstractAction() {
            @Override
            public void actionPerformed(ActionEvent e) {
                if (game.isValidMove("w")) {
                    game.makeMove("w");
                    updateBoard();
                }
            }
        });

        actionMap.put("moveLeft", new AbstractAction() {
            @Override
            public void actionPerformed(ActionEvent e) {
                if (game.isValidMove("a")) {
                    game.makeMove("a");
                    updateBoard();
                }
            }
        });

        actionMap.put("moveDown", new AbstractAction() {
            @Override
            public void actionPerformed(ActionEvent e) {
                if (game.isValidMove("s")) {
                    game.makeMove("s");
                    updateBoard();
                }
            }
        });

        actionMap.put("moveRight", new AbstractAction() {
            @Override
            public void actionPerformed(ActionEvent e) {
                if (game.isValidMove("d")) {
                    game.makeMove("d");
                    updateBoard();
                }
            }
        });
    }

    /**
     * Ažurira prikaz igre na osnovu trenutnog stanja ploče.
     * Prikazuje trenutni rezultat i provjerava kraj igre.
     */
    private void updateBoard() {
        int[][] board = game.getBoard();
        for (int row = 0; row < size; row++) {
            for (int col = 0; col < size; col++) {
                int value = board[row][col];
                tiles[row][col].setText(value == 0 ? "" : String.valueOf(value));
                tiles[row][col].setBackground(getTileColor(value));
            }
        }
        scoreLabel.setText("Score: " + game.getScore());

        if (game.isGameOver()) {
            gameOver();
        }
    }

    /**
     * Prikazuje poruku o završetku igre i omogućava ponovno pokretanje.
     */
    private void gameOver() {
        String input = JOptionPane.showInputDialog(frame, "Game Over! Unesite svoje ime:");
        if (input != null && !input.trim().isEmpty()) {
            storeHighScore(input);
        }
        int option = JOptionPane.showConfirmDialog(frame, "Nova igra?");
        if (option == JOptionPane.YES_OPTION) {
            resetGame(size);  // Prosljeđuje veličinu igre
        } else {
            System.exit(0);
        }
    }

    /**
     * Čuva trenutni rezultat u bazu podataka.
     *
     * @param name Ime igrača.
     */
    private void storeHighScore(String name) {
        String url = "jdbc:mysql://bazepodataka.ba:7306/student391IT";
        String username = "student391IT";
        String password = "10948";

        try (Connection connection = DriverManager.getConnection(url, username, password)) {
            String query = "INSERT INTO highscore (ime, skor) VALUES (?, ?)";
            try (PreparedStatement statement = connection.prepareStatement(query)) {
                statement.setString(1, name);
                statement.setInt(2, game.getScore());
                statement.executeUpdate();
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    /**
     * Sprema trenutno stanje igre u datoteku.
     */
    private void saveGame() {
        try {
            game.saveState("game_save.txt");
            JOptionPane.showMessageDialog(frame, "Igra je sačuvana.");
        } catch (Exception e) {
            e.printStackTrace();
            JOptionPane.showMessageDialog(frame, "Greška prilikom čuvanja igre.", "Greška", JOptionPane.ERROR_MESSAGE);
        }
    }

    /**
     * Učitava sačuvano stanje igre iz datoteke.
     */
    private void loadGame() {
        try {
            game.loadState("game_save.txt");
            updateBoard();
            JOptionPane.showMessageDialog(frame, "Igra je učitana.");
        } catch (Exception e) {
            e.printStackTrace();
            JOptionPane.showMessageDialog(frame, "Greška prilikom učitavanja igre.", "Greška", JOptionPane.ERROR_MESSAGE);
        }
    }

    /**
     * Resetuje igru na početno stanje.
     * 
     * @param size Veličina igrališta.
     */
    private void resetGame(int size) {
        game = new Logika2048(size);  // Koristi veličinu kao parametar
        updateBoard();
    }

    /**
     * Vraća boju pločice na osnovu njene vrijednosti.
     *
     * @param value Vrijednost pločice.
     * @return Boja pločice.
     */
    private Color getTileColor(int value) {
        if (value == 0) return TILE_COLORS[0];
        int index = (int) (Math.log(value) / Math.log(2));
        return index < TILE_COLORS.length ? TILE_COLORS[index] : TILE_COLORS[TILE_COLORS.length - 1];
    }
}
