// Definicija interfejsa za osnovne funkcionalnosti igre 2048.
package interfejs;

public interface GameInterface {

    /**
     * Ispisuje trenutnu ploču igre i rezultat.
     */
    void printBoard();

    /**
     * Provjerava da li je igra završena.
     * 
     * @return true ako je igra završena, inače false.
     */
    boolean isGameOver();

    /**
     * Provjerava da li je unijeti potez validan (npr. "w", "a", "s", "d").
     * 
     * @param move Unos poteza kao string ("w", "a", "s", "d").
     * @return true ako je potez validan, inače false.
     */
    boolean isValidMove(String move);

    /**
     * Izvršava potez na osnovu unijetog smjera (npr. "w", "a", "s", "d").
     * 
     * @param move Unos poteza kao string ("w", "a", "s", "d").
     */
    void makeMove(String move);

    /**
     * Vraća trenutni rezultat igre.
     * 
     * @return Trenutni rezultat.
     */
    int getScore();

	int[][] getBoard();
}
