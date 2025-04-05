package app;

// KonzolnaIgrica.java

/**
 * Klasa KonzolnaIgrica implementira igru "2048" u konzolnom okruženju.
 * Omogućava kretanje pločica u četiri smjera (gore, dolje, lijevo, desno),
 * spajanje pločica iste vrijednosti i dodavanje novih pločica nakon svakog validnog poteza.
 * Igra završava kada više nema validnih poteza.
 */

import java.util.Random;
import java.util.Scanner;

public class KonzolnaIgrica {

    private int[][] ploca; // Dvo-dimenzionalna matrica koja predstavlja ploču igre.
    private int velicina;  // Dimenzija ploče (npr. 4x4).
    private Random random; // Generator slučajnih brojeva za dodavanje novih pločica.

    /**
     * Konstruktor inicijalizira ploču i dodaje dvije početne pločice.
     *
     * @param velicina Dimenzija ploče (npr. 4 za 4x4 ploču).
     */
    public KonzolnaIgrica(int velicina) {
        this.velicina = velicina;
        this.ploca = new int[velicina][velicina];
        this.random = new Random();
        dodajNovuPlocicu();
        dodajNovuPlocicu();
    }

    /**
     * Metoda dodaje novu pločicu s vrijednošću 2 ili 4 na nasumično prazno polje.
     */
    private void dodajNovuPlocicu() {
        int praznihPolja = 0;
        for (int i = 0; i < velicina; i++) {
            for (int j = 0; j < velicina; j++) {
                if (ploca[i][j] == 0) praznihPolja++;
            }
        }

        if (praznihPolja > 0) {
            int indeks = random.nextInt(praznihPolja);
            int brojac = 0;
            for (int i = 0; i < velicina; i++) {
                for (int j = 0; j < velicina; j++) {
                    if (ploca[i][j] == 0) {
                        if (brojac == indeks) {
                            ploca[i][j] = random.nextDouble() < 0.9 ? 2 : 4;
                            return;
                        }
                        brojac++;
                    }
                }
            }
        }
    }

    /**
     * Metoda pomiče i spaja pločice u zadatom smjeru ako je potez validan.
     *
     * @param smjer Smjer kretanja ("w" - gore, "a" - lijevo, "s" - dolje, "d" - desno).
     * @return True ako je potez promijenio stanje ploče, false inače.
     */
    public boolean pomakni(String smjer) {
        // Implementacija logike za pomicanje i spajanje pločica.
        return false; // Placeholder za stvarnu logiku.
    }

    /**
     * Provjerava je li igra završena.
     *
     * @return True ako nema više validnih poteza, false inače.
     */
    public boolean igraGotova() {
        // Implementacija provjere završetka igre.
        return false; // Placeholder za stvarnu logiku.
    }

    /**
     * Prikazuje trenutno stanje ploče u konzoli.
     */
    public void prikaziPlocu() {
        for (int i = 0; i < velicina; i++) {
            for (int j = 0; j < velicina; j++) {
                System.out.printf("|%4d", ploca[i][j]);
            }
            System.out.println("|");
        }
        System.out.println();
    }

    /**
     * Glavna metoda koja pokreće igru i omogućava korisnički unos.
     *
     * @param args Argumenti komandne linije (ne koriste se).
     */
    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);

        System.out.print("Unesite velicinu ploče (npr. 4 za 4x4): ");
        int velicina = scanner.nextInt();
        KonzolnaIgrica igra = new KonzolnaIgrica(velicina);

        while (true) {
            igra.prikaziPlocu();
            if (igra.igraGotova()) {
                System.out.println("Igra je završena! Nema više poteza.");
                break;
            }

            System.out.print("Unesite potez (W=Gore, A=Lijevo, S=Dolje, D=Desno, Q=Izlaz): ");
            String potez = scanner.next().toLowerCase();

            if (potez.equals("q")) {
                System.out.println("Napustili ste igru.");
                break;
            }

            if (!igra.pomakni(potez)) {
                System.out.println("Potez nije validan. Pokušajte ponovo.");
            }
        }

        scanner.close();
    }
}
