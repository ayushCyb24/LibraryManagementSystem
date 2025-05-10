import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;

public class BookManager {
    public static void addBook(String title, String author, int year) {
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement("INSERT INTO Books (title, author, year) VALUES (?, ?, ?)")) {
            
            stmt.setString(1, title);
            stmt.setString(2, author);
            stmt.setInt(3, year);
            stmt.executeUpdate();
            System.out.println("Book Added Successfully!");
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }

    public static void showBooks() {
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement("SELECT * FROM Books");
             ResultSet rs = stmt.executeQuery()) {

            System.out.println("Books in Library:");
            while (rs.next()) {
                System.out.println(rs.getString("title") + " - " + rs.getString("author") + " (" + rs.getInt("year") + ")");
            }
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }

    static String[][] getBooks() {
        throw new UnsupportedOperationException("Not supported yet.");
    }
}
