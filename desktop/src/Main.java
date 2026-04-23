import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.sql.*;

public class Main {

    static String url = "jdbc:mysql://localhost:3306/washpoint";
    static String utilizador = "root";
    static String palavra_passe = "tiago";

    static DefaultTableModel model;
    static JTable table;

    public static void main(String[] args) {

        JFrame frame = new JFrame("Gestor base de dados");
        frame.setSize(700, 400);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setLayout(new BorderLayout());

        model = new DefaultTableModel();
        model.addColumn("ID");
        model.addColumn("Email");
        model.addColumn("Nome de Utilizador");

        table = new JTable(model);
        JScrollPane scrollPane = new JScrollPane(table);

        frame.add(scrollPane, BorderLayout.CENTER);

        loadData();

        JPanel panel = new JPanel();

        JTextField emailField = new JTextField(10);
        JTextField nomeField = new JTextField(10);
        JPasswordField passwordField = new JPasswordField(10);

        JButton addButton = new JButton("Inserir");
        JButton deleteButton = new JButton("Apagar");

        panel.add(new JLabel("Email:"));
        panel.add(emailField);

        panel.add(new JLabel("Nome:"));
        panel.add(nomeField);

        panel.add(new JLabel("Password:"));
        panel.add(passwordField);

        panel.add(addButton);
        panel.add(deleteButton);

        frame.add(panel, BorderLayout.SOUTH);

        addButton.addActionListener(e -> {
            try {
                String email = emailField.getText();
                String nome = nomeField.getText();
                String pass = new String(passwordField.getPassword());

                if (email.isEmpty() || nome.isEmpty() || pass.isEmpty()) {
                    JOptionPane.showMessageDialog(frame, "Preenche todos os campos!");
                    return;
                }

                Connection conn = DriverManager.getConnection(url, utilizador, palavra_passe);

                String sql = "INSERT INTO utilizadores (email, nome_utilizador, palavra_passe) VALUES (?, ?, ?)";
                PreparedStatement ps = conn.prepareStatement(sql);

                ps.setString(1, email);
                ps.setString(2, nome);
                ps.setString(3, pass);

                ps.executeUpdate();

                conn.close();

                emailField.setText("");
                nomeField.setText("");
                passwordField.setText("");

                loadData();

            } catch (Exception ex) {
                ex.printStackTrace();
            }
        });

        deleteButton.addActionListener(e -> {
            int selectedRow = table.getSelectedRow();

            if (selectedRow != -1) {
                int id = (int) model.getValueAt(selectedRow, 0);

                try {
                    Connection conn = DriverManager.getConnection(url, utilizador, palavra_passe);

                    String sql = "DELETE FROM utilizadores WHERE id = ?";
                    PreparedStatement ps = conn.prepareStatement(sql);

                    ps.setInt(1, id);
                    ps.executeUpdate();

                    conn.close();

                    loadData();

                } catch (Exception ex) {
                    ex.printStackTrace();
                }
            }
        });

        frame.setVisible(true);
    }

    static void loadData() {
        try {
            model.setRowCount(0);

            Connection conn = DriverManager.getConnection(url, utilizador, palavra_passe);
            Statement stmt = conn.createStatement();

            ResultSet rs = stmt.executeQuery("SELECT * FROM utilizadores");

            while (rs.next()) {
                model.addRow(new Object[]{
                    rs.getInt("id"),
                    rs.getString("email"),
                    rs.getString("nome_utilizador")
                });
            }

            conn.close();

        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}