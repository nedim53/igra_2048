package interfejs;

import java.awt.BorderLayout;
import java.awt.Font;
import java.awt.event.ActionListener;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JOptionPane;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;

import baza.BazaPodataka;

/**
 * Klasa koja prikazuje listu najboljih rezultata (highscore) iz baze podataka.
 * Ova klasa kreira GUI prozor za prikaz rezultata i omogućava korisniku povratak na prethodni ekran.
 */
public class PrikaziHighscore extends JFrame {
    
    /** Tekstualno polje za prikaz rezultata. */
    private JTextArea highscoreArea;

    /**
     * Konstruktor koji inicijalizuje GUI za prikaz najboljih rezultata.
     * Postavlja osnovne elemente prozora, učitava rezultate iz baze podataka
     * i dodaje dugme za povratak.
     */
    public PrikaziHighscore() {
        // Postavljanje osnovnih svojstava prozora
        setTitle("Highscores"); // Naslov prozora
        setSize(400, 500); // Dimenzije prozora
        setLocationRelativeTo(null); // Centriranje prozora na ekranu
        setLayout(new BorderLayout()); // Postavljanje rasporeda

        // Kreiranje tekstualnog polja za prikaz highscore-ova
        highscoreArea = new JTextArea();
        highscoreArea.setFont(new Font("Arial", Font.PLAIN, 16)); // Font za tekstualno polje
        highscoreArea.setEditable(false); // Onemogućeno uređivanje
        add(new JScrollPane(highscoreArea), BorderLayout.CENTER); // Dodavanje sa skrolom

        // Učitavanje highscore-ova iz baze podataka
        loadHighscores();
        
        // Dodavanje dugmeta za povratak
        JButton nazad = new JButton("Vrati se nazad");
        add(nazad, BorderLayout.SOUTH);
        
        // Akcija za dugme "Nazad"
        nazad.addActionListener(vrati_nazad(this));

        setVisible(true); // Prikazivanje prozora
    }

    /**
     * Kreira akciju koja omogućava povratak na prethodni ekran.
     * 
     * @param parentFrame Trenutni prozor koji treba zatvoriti nakon povratka.
     * @return ActionListener koji otvara ekran za prijavu i zatvara trenutni prozor.
     */
    private ActionListener vrati_nazad(JFrame parentFrame) {
        return e -> {
            new Prijava(); // Otvaranje ekrana za prijavu
            parentFrame.dispose(); // Zatvaranje trenutnog prozora
        };
    }

    /**
     * Učitava listu najboljih rezultata iz baze podataka i prikazuje ih
     * u tekstualnom polju. Rezultati su sortirani po najvećem rezultatu.
     * U slučaju greške, prikazuje odgovarajuću poruku korisniku.
     */
    private void loadHighscores() {
        // Konekcija sa bazom podataka
        Connection connection = BazaPodataka.connect();
        if (connection != null) {
            try (Statement statement = connection.createStatement();
                 ResultSet resultSet = statement.executeQuery("SELECT * FROM highscore order by skor desc")) {
                StringBuilder highscores = new StringBuilder();
                // Prolazak kroz rezultate upita
                while (resultSet.next()) {
                    highscores.append(resultSet.getString("ime")) // Ime igrača
                              .append(": ")
                              .append(resultSet.getInt("skor")) // Njegov rezultat
                              .append("\n");
                }
                // Prikaz u tekstualnom polju
                highscoreArea.setText(highscores.toString());
            } catch (SQLException e) {
                // Greška prilikom učitavanja
                JOptionPane.showMessageDialog(this, "Greška pri učitavanju visoko skora: " + e.getMessage(), "Greška", JOptionPane.ERROR_MESSAGE);
            }
        }
    }
}
