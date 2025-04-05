// Prijava.java
package interfejs;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

/**
 * Klasa `Prijava` omogućava unos dimenzija ploče i početak igre 2048.
 * Omogućava korisnicima da započnu novu igru s odabranim dimenzijama ploče ili pogledaju highscore.
 */
public class Prijava extends JFrame {

    /**
     * Konstruktor klase koji postavlja osnovne karakteristike sučelja za prijavu.
     * Kreira glavnu ploču, dugmadi za započinjanje igre i prikaz highscorea.
     */
    public Prijava() {
        // Postavke prozora
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setSize(400, 500);  // Veličina prozora
        setLocationRelativeTo(null);  // Pozicionira prozor na sredinu ekrana
        setBackground(new Color(0xCDC1B4));  // Postavlja pozadinsku boju
        setLayout(new BorderLayout());  // Postavljanje layout-a na BorderLayout

        // Kreiranje panela za sadržaj
        JPanel mainPanel = new JPanel();
        mainPanel.setBackground(new Color(0xCDC1B4));  // Pozadinska boja panela
        mainPanel.setLayout(null);  // Postavljanje layout-a na null za slobodno pozicioniranje elemenata

        // Labela za naziv igre
        JLabel naslov = new JLabel("IGRA 2048", JLabel.CENTER);
        naslov.setFont(new Font("Arial", Font.BOLD, 30));  // Postavljanje fonta i veličine
        naslov.setBounds(100, 50, 200, 50);  // Pozicioniranje labela
        mainPanel.add(naslov);

        // Dugme za početak nove igre
        JButton zapocni = new JButton("Zapocni novu igru!");
        zapocni.setBounds(100, 150, 200, 50);  // Pozicija i veličina dugmeta
        zapocni.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                // Ovdje se traži unos dimenzija ploče
                String input = JOptionPane.showInputDialog(
                    Prijava.this,
                    "Unesite dimenzije ploče (npr. 4 za 4x4):",
                    "4"  // Default vrijednost
                );

                // Provjera unosa i pokretanje igre s odgovarajućom dimenzijom
                if (input != null && input.matches("\\d+")) {
                    int size = Integer.parseInt(input);  // Pretvara unos u broj
                    SwingUtilities.invokeLater(() -> new IgricaGUI(size));  // Pokreće igru u novom threadu
                    dispose();  // Zatvara prozor prijave
                } else {
                    // Ako unos nije valjan, prikazuje grešku
                    JOptionPane.showMessageDialog(
                        Prijava.this,
                        "Unos mora biti pozitivan cijeli broj.",
                        "Greška",
                        JOptionPane.ERROR_MESSAGE
                    );
                }
            }
        });

        mainPanel.add(zapocni);  // Dodaje dugme na panel

        // Dugme za prikazivanje highscore liste
        JButton prikaziHighScores = new JButton("Prikazi highscore");
        prikaziHighScores.setBounds(100, 250, 200, 50);  // Pozicija i veličina dugmeta
        prikaziHighScores.addActionListener(e -> {
            // Kada se pritisne dugme, otvara se prozor za highscore i zatvara trenutni
            new PrikaziHighscore();
            dispose();
        });
        mainPanel.add(prikaziHighScores);  // Dodaje dugme na panel

        // Dodaje panel u glavni prozor
        add(mainPanel, BorderLayout.CENTER);
        setVisible(true);  // Čini prozor vidljivim
    }

    /**
     * Metoda main koja pokreće prijavu i otvara glavni prozor igre.
     * @param args argumenti komandne linije
     */
    public static void main(String[] args) {
        new Prijava();
    }
}
