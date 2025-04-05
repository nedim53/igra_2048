package baza;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

/**
 * Klasa `BazaPodataka` pruža funkcionalnost za povezivanje sa bazom podataka koristeći JDBC.
 */
public class BazaPodataka {
    
    // URL za konekciju sa bazom podataka
    private static final String URL = "jdbc:mysql://bazepodataka.ba:7306/student391IT"; 
    
    // Korisničko ime za pristup bazi podataka
    private static final String USER = "student391IT"; 
    
    // Lozinka za pristup bazi podataka
    private static final String PASSWORD = "10948";

    /**
     * Povezuje se sa bazom podataka koristeći JDBC konektor.
     * 
     * @return Objekat `Connection` koji predstavlja otvorenu konekciju sa bazom podataka,
     *         ili `null` ukoliko se nije moglo uspostaviti povezivanje.
     */
    public static Connection connect() {
        Connection connection = null;
        try {
            connection = DriverManager.getConnection(URL, USER, PASSWORD);
            System.out.println("Povezivanje uspešno!"); // Ispisuje poruku u konzoli ukoliko je povezivanje uspešno
        } catch (SQLException e) {
            System.out.println("Greška pri povezivanju: " + e.getMessage()); // Ispisuje poruku sa greškom ukoliko se javi problem pri povezivanju
        }
        return connection;
    }
}
