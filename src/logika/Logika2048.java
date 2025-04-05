package logika;

import interfejs.GameInterface;
import java.io.*;
import java.util.Random;

/**
 * Klasa `Logika2048` implementira logiku igre 2048 koristeći interfejs `GameInterface`.
 * Ona sadrži sve funkcionalnosti potrebne za igru kao što su generisanje pločica, provjera validnosti poteza, 
 * pomjeranje pločica, i čuvanje stanja igre u datoteku.
 */
public class Logika2048 implements GameInterface {
    private int SIZE; // Dimenzija ploče igre
    private int[][] board; // Pločica
    private int score; // Trenutni rezultat igre

    /**
     * Konstruktor inicijalizira ploču igre i dodaje dvije nasumične pločice.
     */
    public Logika2048(int size) {
        this.SIZE = size; // Postavite veličinu ploče prema korisničkom unosu
        board = new int[SIZE][SIZE]; // Inicijalizacija ploče prema novoj veličini
        score = 0;
        spawnRandomTile();
        spawnRandomTile();
    }

    /**
     * Vraća trenutnu ploču igre.
     *
     * @return Dvodimenzionalni niz koji predstavlja stanje ploče igre.
     */
    public int[][] getBoard() {
        return board;
    }

    /**
     * Ispisuje trenutnu ploču igre i rezultat.
     */
    @Override
    public void printBoard() {
        System.out.println("Rezultat: " + score);
        for (int[] row : board) {
            for (int tile : row) {
                System.out.print((tile == 0 ? "-" : tile) + "\t"); // Prikazuje '-' za prazne pločice
            }
            System.out.println(); // Nova linija nakon svakog reda
        }
        System.out.println(); // Dodatni razmak za bolju čitljivost
    }

    /**
     * Provjerava da li je igra završena.
     *
     * @return `true` ako je postignut rezultat 2048 ili ako nema više mogućih poteza, `false` inače.
     */
    @Override
    public boolean isGameOver() {
        if (hasTile2048()) { // Ako postoji pločica sa vrijednošću 2048, igra je pobijeđena
            System.out.println("Čestitamo! Dostigli ste pločicu 2048.");
            return true;
        }
        // Igra je završena ako nema praznih pločica i nijedna pločica se ne može spojiti
        return !hasEmptyTile() && !canMergeTiles();
    }

    /**
     * Provjerava da li je potez validan (w, a, s, d).
     *
     * @param move Unos korisnika ('w', 'a', 's', 'd' za gore, lijevo, dolje, desno).
     * @return `true` ako je potez validan, `false` inače.
     */
    @Override
    public boolean isValidMove(String move) {
        return move.equals("w") || move.equals("a") || move.equals("s") || move.equals("d");
    }

    /**
     * Obrađuje potez na osnovu unosa korisnika.
     *
     * @param move Unos poteza ('w', 'a', 's', 'd').
     */
    @Override
    public void makeMove(String move) {
        int[][] prevBoard = deepCopyBoard(); // Pravljenje kopije ploče za poređenje
        switch (move) {
            case "w":
                moveUp(); // Pomjeranje pločica prema gore
                break;
            case "a":
                moveLeft(); // Pomjeranje pločica lijevo
                break;
            case "s":
                moveDown(); // Pomjeranje pločica prema dolje
                break;
            case "d":
                moveRight(); // Pomjeranje pločica desno
                break;
            default:
                System.out.println("Neispravan potez. Koristite 'w', 'a', 's' ili 'd'.");
                return;
        }
        // Ako se ploča promijenila, dodaje novu nasumičnu pločicu i ispisuje stanje ploče
        if (!boardsAreEqual(prevBoard, board)) {
            spawnRandomTile();
            printBoard();
        }
        // Provjerava da li je igra završena nakon poteza
        if (isGameOver()) {
            System.out.println("Igra je završena!");
        }
    }

    /**
     * Vraća trenutni rezultat igre.
     *
     * @return Trenutni rezultat igre.
     */
    @Override
    public int getScore() {
        return score;
    }

    /**
     * Resetuje igru na početno stanje.
     */
    public void reset() {
        board = new int[SIZE][SIZE]; // Resetuje ploču igre
        score = 0; // Postavlja rezultat na 0
        spawnRandomTile(); // Dodaje novu nasumičnu pločicu
        spawnRandomTile();
        System.out.println("Igra je resetovana.");
    }

    /**
     * Pomjeranje pločica prema gore.
     */
    private void moveUp() {
        for (int col = 0; col < SIZE; col++) {
            int[] merged = new int[SIZE]; // Oznaka za praćenje spojenih pločica
            int target = 0; // Ciljana pozicija za pomjeranje pločica
            for (int row = 0; row < SIZE; row++) {
                if (board[row][col] != 0) {
                    if (target > 0 && board[target - 1][col] == board[row][col] && merged[target - 1] == 0) {
                        board[target - 1][col] *= 2; // Spajanje pločica
                        score += board[target - 1][col]; // Ažuriranje rezultata
                        board[row][col] = 0; // Uklanjanje stare pločice
                        merged[target - 1] = 1; // Oznaka da je pločica spojena
                    } else {
                        if (target != row) {
                            board[target][col] = board[row][col]; // Pomjeranje pločice
                            board[row][col] = 0;
                        }
                        target++;
                    }
                }
            }
        }
    }

    /**
     * Pomjeranje pločica lijevo.
     */
    private void moveLeft() {
        for (int row = 0; row < SIZE; row++) {
            int[] merged = new int[SIZE];
            int target = 0;
            for (int col = 0; col < SIZE; col++) {
                if (board[row][col] != 0) {
                    if (target > 0 && board[row][target - 1] == board[row][col] && merged[target - 1] == 0) {
                        board[row][target - 1] *= 2;
                        score += board[row][target - 1];
                        board[row][col] = 0;
                        merged[target - 1] = 1;
                    } else {
                        if (target != col) {
                            board[row][target] = board[row][col];
                            board[row][col] = 0;
                        }
                        target++;
                    }
                }
            }
        }
    }

    /**
     * Pomjeranje pločica prema dolje.
     */
    private void moveDown() {
        for (int col = 0; col < SIZE; col++) {
            int[] merged = new int[SIZE];
            int target = SIZE - 1;
            for (int row = SIZE - 1; row >= 0; row--) {
                if (board[row][col] != 0) {
                    if (target < SIZE - 1 && board[target + 1][col] == board[row][col] && merged[target + 1] == 0) {
                        board[target + 1][col] *= 2;
                        score += board[target + 1][col];
                        board[row][col] = 0;
                        merged[target + 1] = 1;
                    } else {
                        if (target != row) {
                            board[target][col] = board[row][col];
                            board[row][col] = 0;
                        }
                        target--;
                    }
                }
            }
        }
    }

    /**
     * Pomjeranje pločica desno.
     */
    private void moveRight() {
        for (int row = 0; row < SIZE; row++) {
            int[] merged = new int[SIZE];
            int target = SIZE - 1;
            for (int col = SIZE - 1; col >= 0; col--) {
                if (board[row][col] != 0) {
                    if (target < SIZE - 1 && board[row][target + 1] == board[row][col] && merged[target + 1] == 0) {
                        board[row][target + 1] *= 2;
                        score += board[row][target + 1];
                        board[row][col] = 0;
                        merged[target + 1] = 1;
                    } else {
                        if (target != col) {
                            board[row][target] = board[row][col];
                            board[row][col] = 0;
                        }
                        target--;
                    }
                }
            }
        }
    }

    /**
     * Provjerava da li na ploči postoji prazna pločica.
     *
     * @return `true` ako postoji prazna pločica, `false` inače.
     */
    private boolean hasEmptyTile() {
        for (int[] row : board) {
            for (int tile : row) {
                if (tile == 0) return true;
            }
        }
        return false;
    }

    /**
     * Provjerava da li se dvije pločice mogu spojiti.
     *
     * @return `true` ako se mogu spojiti, `false` inače.
     */
    private boolean canMergeTiles() {
        for (int row = 0; row < SIZE; row++) {
            for (int col = 0; col < SIZE; col++) {
                if (col < SIZE - 1 && board[row][col] == board[row][col + 1]) return true;
                if (row < SIZE - 1 && board[row][col] == board[row + 1][col]) return true;
            }
        }
        return false;
    }

    /**
     * Provjerava da li je postignut rezultat 2048.
     *
     * @return `true` ako postoji pločica sa vrijednošću 2048, `false` inače.
     */
    private boolean hasTile2048() {
        for (int[] row : board) {
            for (int tile : row) {
                if (tile == 2048) return true;
            }
        }
        return false;
    }

    /**
     * Dodaje nasumičnu pločicu na prazno mjesto.
     */
    private void spawnRandomTile() {
        if (!hasEmptyTile()) return;
        Random random = new Random();
        int row, col;
        do {
            row = random.nextInt(SIZE);
            col = random.nextInt(SIZE);
        } while (board[row][col] != 0);
        board[row][col] = random.nextInt(10) < 9 ? 2 : 4;
        System.out.println("Dodana pločica: " + board[row][col] + " na poziciji [" + row + ", " + col + "]");
    }

    /**
     * Kreira duboku kopiju ploče.
     *
     * @return Duboka kopija ploče igre.
     */
    private int[][] deepCopyBoard() {
        int[][] copy = new int[SIZE][SIZE];
        for (int i = 0; i < SIZE; i++) {
            System.arraycopy(board[i], 0, copy[i], 0, SIZE);
        }
        return copy;
    }

    /**
     * Provjerava da li su dvije ploče identične.
     *
     * @param board1 Prva ploča.
     * @param board2 Druga ploča.
     * @return `true` ako su ploče identične, `false` inače.
     */
    private boolean boardsAreEqual(int[][] board1, int[][] board2) {
        for (int i = 0; i < SIZE; i++) {
            for (int j = 0; j < SIZE; j++) {
                if (board1[i][j] != board2[i][j]) return false;
            }
        }
        return true;
    }

    /**
     * Sprema trenutno stanje igre u datoteku.
     *
     * @param filePath Putanja do datoteke u koju se spremaju podaci o stanju igre.
     */
    public void saveState(String filePath) {
        try (ObjectOutputStream oos = new ObjectOutputStream(new FileOutputStream(filePath))) {
            oos.writeObject(board);
            oos.writeInt(score);
            System.out.println("Igra je uspješno spremljena.");
        } catch (IOException e) {
            System.err.println("Greška pri spremanju igre: " + e.getMessage());
        }
    }

    /**
     * Učitava prethodno spremljeno stanje igre iz datoteke.
     *
     * @param filePath Putanja do datoteke iz koje se učitavaju podaci o stanju igre.
     */
    public void loadState(String filePath) {
        try (ObjectInputStream ois = new ObjectInputStream(new FileInputStream(filePath))) {
            board = (int[][]) ois.readObject();
            score = ois.readInt();
            System.out.println("Igra je uspješno učitana.");
        } catch (IOException | ClassNotFoundException e) {
            System.err.println("Greška pri učitavanju igre: " + e.getMessage());
        }
    }
}

