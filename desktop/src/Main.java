import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.Statement;

public class Main {
    public static void main(String[] args) {
        String url = "jdbc:mysql://localhost:3306/washpoint";
        String user = "root";
        String password = "tiago";

        try {
            Connection conn = DriverManager.getConnection(url, user, password);
            System.out.println("✔ Ligado ao MySQL com sucesso!");

            Statement stmt = conn.createStatement();
            ResultSet rs = stmt.executeQuery("SHOW TABLES");

            System.out.println("Tabelas na base de dados:");

            while (rs.next()) {
                System.out.println("- " + rs.getString(1));
            }

            conn.close();

        } catch (Exception e) {
            System.out.println("Erro:");
            e.printStackTrace();
        }
    }
}
