import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.sql.*;
import java.text.SimpleDateFormat;
import java.util.Date;

public class HotelManagementSystem extends JFrame {
    // Database connection details
    private static final String DB_URL = "jdbc:mysql://localhost:3306/hotel_db";
    private static final String DB_USER = "root";
    private static final String DB_PASSWORD = "";

    public HotelManagementSystem() {
        setTitle("Taj_Group");
        setSize(800, 600);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);

        // Initialize database tables
        initializeDatabase();

        // Show login panel
        showLoginPanel();
    }

    // Show login panel
    private void showLoginPanel() {
        getContentPane().removeAll(); // Clear the current content
        JPanel loginPanel = new JPanel(new GridBagLayout());
        loginPanel.setBackground(new Color(240, 240, 240));
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(10, 10, 10, 10);

        // Title Label
        JLabel titleLabel = new JLabel("Hotel Management System");
        titleLabel.setFont(new Font("Arial", Font.BOLD, 24));
        titleLabel.setForeground(new Color(0, 102, 204));
        gbc.gridx = 0;
        gbc.gridy = 0;
        gbc.gridwidth = 2;
        gbc.anchor = GridBagConstraints.CENTER;
        loginPanel.add(titleLabel, gbc);

        // Username Field
        JLabel usernameLabel = new JLabel("Username:");
        usernameLabel.setFont(new Font("Arial", Font.PLAIN, 14));
        gbc.gridx = 0;
        gbc.gridy = 1;
        gbc.gridwidth = 1;
        gbc.anchor = GridBagConstraints.LINE_END;
        loginPanel.add(usernameLabel, gbc);

        JTextField usernameField = new JTextField(20);
        usernameField.setFont(new Font("Arial", Font.PLAIN, 14));
        gbc.gridx = 1;
        gbc.gridy = 1;
        gbc.anchor = GridBagConstraints.LINE_START;
        loginPanel.add(usernameField, gbc);

        // Password Field
        JLabel passwordLabel = new JLabel("Password:");
        passwordLabel.setFont(new Font("Arial", Font.PLAIN, 14));
        gbc.gridx = 0;
        gbc.gridy = 2;
        gbc.anchor = GridBagConstraints.LINE_END;
        loginPanel.add(passwordLabel, gbc);

        JPasswordField passwordField = new JPasswordField(20);
        passwordField.setFont(new Font("Arial", Font.PLAIN, 14));
        gbc.gridx = 1;
        gbc.gridy = 2;
        gbc.anchor = GridBagConstraints.LINE_START;
        loginPanel.add(passwordField, gbc);

        // Login Button
        JButton loginButton = new JButton("Login");
        styleButton(loginButton);
        loginButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                String username = usernameField.getText();
                String password = new String(passwordField.getPassword());

                if (username.isEmpty() || password.isEmpty()) {
                    JOptionPane.showMessageDialog(HotelManagementSystem.this, "Please enter both username and password", "Error", JOptionPane.ERROR_MESSAGE);
                    return;
                }

                try (Connection conn = DriverManager.getConnection(DB_URL, DB_USER, DB_PASSWORD)) {
                    String query = "SELECT * FROM users WHERE username = ? AND password = ?";
                    PreparedStatement stmt = conn.prepareStatement(query);
                    stmt.setString(1, username);
                    stmt.setString(2, password);
                    ResultSet rs = stmt.executeQuery();

                    if (rs.next()) {
                        String role = rs.getString("role");
                        int userId = rs.getInt("id");
                        if (role.equals("admin")) {
                            new AdminDashboard().setVisible(true);
                        } else {
                            new UserDashboard(userId).setVisible(true);
                        }
                        dispose(); // Close the login window
                    } else {
                        JOptionPane.showMessageDialog(HotelManagementSystem.this, "Invalid username or password", "Error", JOptionPane.ERROR_MESSAGE);
                    }
                } catch (SQLException ex) {
                    ex.printStackTrace();
                }
            }
        });
        gbc.gridx = 0;
        gbc.gridy = 3;
        gbc.gridwidth = 2;
        gbc.anchor = GridBagConstraints.CENTER;
        loginPanel.add(loginButton, gbc);

        // Register Button
        JButton registerButton = new JButton("Register");
        styleButton(registerButton);
        registerButton.addActionListener(e -> showRegistrationPanel());
        gbc.gridx = 0;
        gbc.gridy = 4;
        gbc.gridwidth = 2;
        gbc.anchor = GridBagConstraints.CENTER;
        loginPanel.add(registerButton, gbc);

        add(loginPanel);
        revalidate();
        repaint();
    }

    // Show registration panel
    private void showRegistrationPanel() {
        getContentPane().removeAll(); // Clear the current content
        JPanel registerPanel = new JPanel(new GridBagLayout());
        registerPanel.setBackground(new Color(240, 240, 240));
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(10, 10, 10, 10);

        // Title Label
        JLabel titleLabel = new JLabel("User Registration");
        titleLabel.setFont(new Font("Arial", Font.BOLD, 24));
        titleLabel.setForeground(new Color(0, 102, 204));
        gbc.gridx = 0;
        gbc.gridy = 0;
        gbc.gridwidth = 2;
        gbc.anchor = GridBagConstraints.CENTER;
        registerPanel.add(titleLabel, gbc);

        // Username Field
        JLabel usernameLabel = new JLabel("Username:");
        usernameLabel.setFont(new Font("Arial", Font.PLAIN, 14));
        gbc.gridx = 0;
        gbc.gridy = 1;
        gbc.gridwidth = 1;
        gbc.anchor = GridBagConstraints.LINE_END;
        registerPanel.add(usernameLabel, gbc);

        JTextField usernameField = new JTextField(20);
        usernameField.setFont(new Font("Arial", Font.PLAIN, 14));
        gbc.gridx = 1;
        gbc.gridy = 1;
        gbc.anchor = GridBagConstraints.LINE_START;
        registerPanel.add(usernameField, gbc);

        // Password Field
        JLabel passwordLabel = new JLabel("Password:");
        passwordLabel.setFont(new Font("Arial", Font.PLAIN, 14));
        gbc.gridx = 0;
        gbc.gridy = 2;
        gbc.anchor = GridBagConstraints.LINE_END;
        registerPanel.add(passwordLabel, gbc);

        JPasswordField passwordField = new JPasswordField(20);
        passwordField.setFont(new Font("Arial", Font.PLAIN, 14));
        gbc.gridx = 1;
        gbc.gridy = 2;
        gbc.anchor = GridBagConstraints.LINE_START;
        registerPanel.add(passwordField, gbc);

        // Register Button
        JButton registerButton = new JButton("Register");
        styleButton(registerButton);
        registerButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                String username = usernameField.getText();
                String password = new String(passwordField.getPassword());

                if (username.isEmpty() || password.isEmpty()) {
                    JOptionPane.showMessageDialog(HotelManagementSystem.this, "Please fill all fields", "Error", JOptionPane.ERROR_MESSAGE);
                    return;
                }

                try (Connection conn = DriverManager.getConnection(DB_URL, DB_USER, DB_PASSWORD)) {
                    String query = "INSERT INTO users (username, password, role) VALUES (?, ?, 'guest')";
                    PreparedStatement stmt = conn.prepareStatement(query);
                    stmt.setString(1, username);
                    stmt.setString(2, password);
                    stmt.executeUpdate();
                    JOptionPane.showMessageDialog(HotelManagementSystem.this, "Registration successful", "Success", JOptionPane.INFORMATION_MESSAGE);
                    showLoginPanel(); // Return to login panel
                } catch (SQLException ex) {
                    ex.printStackTrace();
                }
            }
        });
        gbc.gridx = 0;
        gbc.gridy = 3;
        gbc.gridwidth = 2;
        gbc.anchor = GridBagConstraints.CENTER;
        registerPanel.add(registerButton, gbc);

        // Back Button
        JButton backButton = new JButton("Back to Login");
        styleButton(backButton);
        backButton.addActionListener(e -> showLoginPanel());
        gbc.gridx = 0;
        gbc.gridy = 4;
        gbc.gridwidth = 2;
        gbc.anchor = GridBagConstraints.CENTER;
        registerPanel.add(backButton, gbc);

        add(registerPanel);
        revalidate();
        repaint();
    }

    // Style buttons
    private void styleButton(JButton button) {
        button.setFont(new Font("Arial", Font.BOLD, 14));
        button.setBackground(new Color(0, 102, 204));
        button.setForeground(Color.WHITE);
        button.setFocusPainted(false);
    }

    // Initialize database tables
    private void initializeDatabase() {
        try (Connection conn = DriverManager.getConnection(DB_URL, DB_USER, DB_PASSWORD);
             Statement stmt = conn.createStatement()) {

            // Create users table
            stmt.executeUpdate("CREATE TABLE IF NOT EXISTS users (" +
                    "id INT AUTO_INCREMENT PRIMARY KEY, " +
                    "username VARCHAR(50) NOT NULL, " +
                    "password VARCHAR(50) NOT NULL, " +
                    "role ENUM('admin', 'guest') NOT NULL)");

            // Create rooms table
            stmt.executeUpdate("CREATE TABLE IF NOT EXISTS rooms (" +
                    "id INT AUTO_INCREMENT PRIMARY KEY, " +
                    "room_type VARCHAR(50) NOT NULL, " +
                    "price DECIMAL(10, 2) NOT NULL, " +
                    "availability BOOLEAN NOT NULL DEFAULT TRUE, " +
                    "image_path VARCHAR(255), " +
                    "description TEXT, " +
                    "amenities TEXT)");

            // Create bookings table
            stmt.executeUpdate("CREATE TABLE IF NOT EXISTS bookings (" +
                    "id INT AUTO_INCREMENT PRIMARY KEY, " +
                    "user_id INT NOT NULL, " +
                    "room_id INT NOT NULL, " +
                    "check_in DATE NOT NULL, " +
                    "check_out DATE NOT NULL, " +
                    "FOREIGN KEY (user_id) REFERENCES users(id), " +
                    "FOREIGN KEY (room_id) REFERENCES rooms(id))");

            // Create reviews table
            stmt.executeUpdate("CREATE TABLE IF NOT EXISTS reviews (" +
                    "id INT AUTO_INCREMENT PRIMARY KEY, " +
                    "user_id INT NOT NULL, " +
                    "room_id INT NOT NULL, " +
                    "rating INT NOT NULL, " +
                    "comment TEXT, " +
                    "FOREIGN KEY (user_id) REFERENCES users(id), " +
                    "FOREIGN KEY (room_id) REFERENCES rooms(id))");

            // Insert default admin user if not exists
            ResultSet rs = stmt.executeQuery("SELECT * FROM users WHERE username = 'admin'");
            if (!rs.next()) {
                stmt.executeUpdate("INSERT INTO users (username, password, role) VALUES ('admin', 'admin123', 'admin')");
            }

            // Insert sample rooms if not exists
            rs = stmt.executeQuery("SELECT * FROM rooms");
            if (!rs.next()) {
                stmt.executeUpdate("INSERT INTO rooms (room_type, price, availability, image_path, description, amenities) VALUES " +
                        "('Single Room', 100.00, TRUE, 'C:/hotel_images/single_room.jpg', 'A cozy single room with a comfortable bed.', 'Wi-Fi, TV, AC'), " +
                        "('Double Room', 150.00, TRUE, 'C:/hotel_images/double_room.jpg', 'A spacious double room with two beds.', 'Wi-Fi, TV, AC, Mini Fridge'), " +
                        "('Suite', 250.00, TRUE, 'C:/hotel_images/suite.jpg', 'A luxurious suite with a living area and balcony.', 'Wi-Fi, TV, AC, Mini Bar, Jacuzzi')");
            }
        } catch (SQLException ex) {
            ex.printStackTrace();
        }
    }

    // Admin Dashboard
    class AdminDashboard extends JFrame {
        public AdminDashboard() {
            setTitle("Admin Dashboard");
            setSize(1000, 700);
            setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
            setLocationRelativeTo(null);

            JTabbedPane tabbedPane = new JTabbedPane();
            tabbedPane.addTab("Manage Rooms", new ManageRoomsPanel());
            tabbedPane.addTab("View Reservations", new ViewReservationsPanel());
            tabbedPane.addTab("View Reviews", new ViewReviewsPanel());

            add(tabbedPane);
        }

        class ManageRoomsPanel extends JPanel {
            private JTextField roomTypeField, priceField, roomIdField, imagePathField, amenitiesField;
            private JTextArea descriptionArea;
            private JButton addButton, updateButton, deleteButton;

            public ManageRoomsPanel() {
                setLayout(new GridBagLayout());
                setBackground(new Color(240, 240, 240));
                GridBagConstraints gbc = new GridBagConstraints();
                gbc.insets = new Insets(10, 10, 10, 10);

                // Room ID Field
                JLabel roomIdLabel = new JLabel("Room ID (for update/delete):");
                roomIdLabel.setFont(new Font("Arial", Font.PLAIN, 14));
                gbc.gridx = 0;
                gbc.gridy = 0;
                gbc.anchor = GridBagConstraints.LINE_END;
                add(roomIdLabel, gbc);

                roomIdField = new JTextField(20);
                roomIdField.setFont(new Font("Arial", Font.PLAIN, 14));
                gbc.gridx = 1;
                gbc.gridy = 0;
                gbc.anchor = GridBagConstraints.LINE_START;
                add(roomIdField, gbc);

                // Room Type Field
                JLabel roomTypeLabel = new JLabel("Room Type:");
                roomTypeLabel.setFont(new Font("Arial", Font.PLAIN, 14));
                gbc.gridx = 0;
                gbc.gridy = 1;
                gbc.anchor = GridBagConstraints.LINE_END;
                add(roomTypeLabel, gbc);

                roomTypeField = new JTextField(20);
                roomTypeField.setFont(new Font("Arial", Font.PLAIN, 14));
                gbc.gridx = 1;
                gbc.gridy = 1;
                gbc.anchor = GridBagConstraints.LINE_START;
                add(roomTypeField, gbc);

                // Price Field
                JLabel priceLabel = new JLabel("Price:");
                priceLabel.setFont(new Font("Arial", Font.PLAIN, 14));
                gbc.gridx = 0;
                gbc.gridy = 2;
                gbc.anchor = GridBagConstraints.LINE_END;
                add(priceLabel, gbc);

                priceField = new JTextField(20);
                priceField.setFont(new Font("Arial", Font.PLAIN, 14));
                gbc.gridx = 1;
                gbc.gridy = 2;
                gbc.anchor = GridBagConstraints.LINE_START;
                add(priceField, gbc);

                // Image Path Field
                JLabel imagePathLabel = new JLabel("Image Path:");
                imagePathLabel.setFont(new Font("Arial", Font.PLAIN, 14));
                gbc.gridx = 0;
                gbc.gridy = 3;
                gbc.anchor = GridBagConstraints.LINE_END;
                add(imagePathLabel, gbc);

                imagePathField = new JTextField(20);
                imagePathField.setFont(new Font("Arial", Font.PLAIN, 14));
                gbc.gridx = 1;
                gbc.gridy = 3;
                gbc.anchor = GridBagConstraints.LINE_START;
                add(imagePathField, gbc);

                // Description Field
                JLabel descriptionLabel = new JLabel("Description:");
                descriptionLabel.setFont(new Font("Arial", Font.PLAIN, 14));
                gbc.gridx = 0;
                gbc.gridy = 4;
                gbc.anchor = GridBagConstraints.LINE_END;
                add(descriptionLabel, gbc);

                descriptionArea = new JTextArea(5, 20);
                descriptionArea.setFont(new Font("Arial", Font.PLAIN, 14));
                JScrollPane scrollPane = new JScrollPane(descriptionArea);
                gbc.gridx = 1;
                gbc.gridy = 4;
                gbc.anchor = GridBagConstraints.LINE_START;
                add(scrollPane, gbc);

                // Amenities Field
                JLabel amenitiesLabel = new JLabel("Amenities:");
                amenitiesLabel.setFont(new Font("Arial", Font.PLAIN, 14));
                gbc.gridx = 0;
                gbc.gridy = 5;
                gbc.anchor = GridBagConstraints.LINE_END;
                add(amenitiesLabel, gbc);

                amenitiesField = new JTextField(20);
                amenitiesField.setFont(new Font("Arial", Font.PLAIN, 14));
                gbc.gridx = 1;
                gbc.gridy = 5;
                gbc.anchor = GridBagConstraints.LINE_START;
                add(amenitiesField, gbc);

                // Add Button
                addButton = new JButton("Add Room");
                styleButton(addButton);
                addButton.addActionListener(e -> addRoom());
                gbc.gridx = 0;
                gbc.gridy = 6;
                gbc.gridwidth = 2;
                gbc.anchor = GridBagConstraints.CENTER;
                add(addButton, gbc);

                // Update Button
                updateButton = new JButton("Update Room");
                styleButton(updateButton);
                updateButton.addActionListener(e -> updateRoom());
                gbc.gridx = 0;
                gbc.gridy = 7;
                gbc.gridwidth = 2;
                gbc.anchor = GridBagConstraints.CENTER;
                add(updateButton, gbc);

                // Delete Button
                deleteButton = new JButton("Delete Room");
                styleButton(deleteButton);
                deleteButton.addActionListener(e -> deleteRoom());
                gbc.gridx = 0;
                gbc.gridy = 8;
                gbc.gridwidth = 2;
                gbc.anchor = GridBagConstraints.CENTER;
                add(deleteButton, gbc);
            }

            private void addRoom() {
                String roomType = roomTypeField.getText();
                String priceText = priceField.getText();
                String imagePath = imagePathField.getText();
                String description = descriptionArea.getText();
                String amenities = amenitiesField.getText();

                if (roomType.isEmpty() || priceText.isEmpty() || imagePath.isEmpty() || description.isEmpty() || amenities.isEmpty()) {
                    JOptionPane.showMessageDialog(this, "Please fill all fields", "Error", JOptionPane.ERROR_MESSAGE);
                    return;
                }

                try {
                    double price = Double.parseDouble(priceText);
                    try (Connection conn = DriverManager.getConnection(DB_URL, DB_USER, DB_PASSWORD)) {
                        String query = "INSERT INTO rooms (room_type, price, image_path, description, amenities) VALUES (?, ?, ?, ?, ?)";
                        PreparedStatement stmt = conn.prepareStatement(query);
                        stmt.setString(1, roomType);
                        stmt.setDouble(2, price);
                        stmt.setString(3, imagePath);
                        stmt.setString(4, description);
                        stmt.setString(5, amenities);
                        stmt.executeUpdate();
                        JOptionPane.showMessageDialog(this, "Room added successfully", "Success", JOptionPane.INFORMATION_MESSAGE);
                        clearFields();
                    } catch (SQLException ex) {
                        ex.printStackTrace();
                    }
                } catch (NumberFormatException ex) {
                    JOptionPane.showMessageDialog(this, "Invalid price format", "Error", JOptionPane.ERROR_MESSAGE);
                }
            }

            private void updateRoom() {
                String roomIdText = roomIdField.getText();
                String roomType = roomTypeField.getText();
                String priceText = priceField.getText();
                String imagePath = imagePathField.getText();
                String description = descriptionArea.getText();
                String amenities = amenitiesField.getText();

                if (roomIdText.isEmpty() || roomType.isEmpty() || priceText.isEmpty() || imagePath.isEmpty() || description.isEmpty() || amenities.isEmpty()) {
                    JOptionPane.showMessageDialog(this, "Please fill all fields", "Error", JOptionPane.ERROR_MESSAGE);
                    return;
                }

                try {
                    int roomId = Integer.parseInt(roomIdText);
                    double price = Double.parseDouble(priceText);
                    try (Connection conn = DriverManager.getConnection(DB_URL, DB_USER, DB_PASSWORD)) {
                        String query = "UPDATE rooms SET room_type = ?, price = ?, image_path = ?, description = ?, amenities = ? WHERE id = ?";
                        PreparedStatement stmt = conn.prepareStatement(query);
                        stmt.setString(1, roomType);
                        stmt.setDouble(2, price);
                        stmt.setString(3, imagePath);
                        stmt.setString(4, description);
                        stmt.setString(5, amenities);
                        stmt.setInt(6, roomId);
                        int rowsUpdated = stmt.executeUpdate();
                        if (rowsUpdated > 0) {
                            JOptionPane.showMessageDialog(this, "Room updated successfully", "Success", JOptionPane.INFORMATION_MESSAGE);
                            clearFields();
                        } else {
                            JOptionPane.showMessageDialog(this, "Room not found", "Error", JOptionPane.ERROR_MESSAGE);
                        }
                    } catch (SQLException ex) {
                        ex.printStackTrace();
                    }
                } catch (NumberFormatException ex) {
                    JOptionPane.showMessageDialog(this, "Invalid room ID or price format", "Error", JOptionPane.ERROR_MESSAGE);
                }
            }

            private void deleteRoom() {
                String roomIdText = roomIdField.getText();

                if (roomIdText.isEmpty()) {
                    JOptionPane.showMessageDialog(this, "Please enter Room ID", "Error", JOptionPane.ERROR_MESSAGE);
                    return;
                }

                try {
                    int roomId = Integer.parseInt(roomIdText);
                    try (Connection conn = DriverManager.getConnection(DB_URL, DB_USER, DB_PASSWORD)) {
                        String query = "DELETE FROM rooms WHERE id = ?";
                        PreparedStatement stmt = conn.prepareStatement(query);
                        stmt.setInt(1, roomId);
                        int rowsDeleted = stmt.executeUpdate();
                        if (rowsDeleted > 0) {
                            JOptionPane.showMessageDialog(this, "Room deleted successfully", "Success", JOptionPane.INFORMATION_MESSAGE);
                            clearFields();
                        } else {
                            JOptionPane.showMessageDialog(this, "Room not found", "Error", JOptionPane.ERROR_MESSAGE);
                        }
                    } catch (SQLException ex) {
                        ex.printStackTrace();
                    }
                } catch (NumberFormatException ex) {
                    JOptionPane.showMessageDialog(this, "Invalid room ID format", "Error", JOptionPane.ERROR_MESSAGE);
                }
            }

            private void clearFields() {
                roomIdField.setText("");
                roomTypeField.setText("");
                priceField.setText("");
                imagePathField.setText("");
                descriptionArea.setText("");
                amenitiesField.setText("");
            }
        }

        class ViewReservationsPanel extends JPanel {
            public ViewReservationsPanel() {
                setLayout(new BorderLayout());
                setBackground(new Color(240, 240, 240));

                JTextArea textArea = new JTextArea();
                textArea.setEditable(false);
                textArea.setFont(new Font("Arial", Font.PLAIN, 14));
                JScrollPane scrollPane = new JScrollPane(textArea);
                add(scrollPane, BorderLayout.CENTER);

                try (Connection conn = DriverManager.getConnection(DB_URL, DB_USER, DB_PASSWORD)) {
                    String query = "SELECT * FROM bookings";
                    PreparedStatement stmt = conn.prepareStatement(query);
                    ResultSet rs = stmt.executeQuery();

                    while (rs.next()) {
                        textArea.append("Booking ID: " + rs.getInt("id") + "\n");
                        textArea.append("User ID: " + rs.getInt("user_id") + "\n");
                        textArea.append("Room ID: " + rs.getInt("room_id") + "\n");
                        textArea.append("Check-In: " + rs.getDate("check_in") + "\n");
                        textArea.append("Check-Out: " + rs.getDate("check_out") + "\n\n");
                    }
                } catch (SQLException ex) {
                    ex.printStackTrace();
                }
            }
        }

        class ViewReviewsPanel extends JPanel {
            public ViewReviewsPanel() {
                setLayout(new BorderLayout());
                setBackground(new Color(240, 240, 240));

                JTextArea textArea = new JTextArea();
                textArea.setEditable(false);
                textArea.setFont(new Font("Arial", Font.PLAIN, 14));
                JScrollPane scrollPane = new JScrollPane(textArea);
                add(scrollPane, BorderLayout.CENTER);

                try (Connection conn = DriverManager.getConnection(DB_URL, DB_USER, DB_PASSWORD)) {
                    String query = "SELECT * FROM reviews";
                    PreparedStatement stmt = conn.prepareStatement(query);
                    ResultSet rs = stmt.executeQuery();

                    while (rs.next()) {
                        textArea.append("Review ID: " + rs.getInt("id") + "\n");
                        textArea.append("User ID: " + rs.getInt("user_id") + "\n");
                        textArea.append("Room ID: " + rs.getInt("room_id") + "\n");
                        textArea.append("Rating: " + rs.getInt("rating") + "\n");
                        textArea.append("Comment: " + rs.getString("comment") + "\n\n");
                    }
                } catch (SQLException ex) {
                    ex.printStackTrace();
                }
            }
        }
    }

    // User Dashboard
    class UserDashboard extends JFrame {
        private int userId;

        public UserDashboard(int userId) {
            this.userId = userId;
            setTitle("User Dashboard");
            setSize(1000, 700);
            setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
            setLocationRelativeTo(null);

            JTabbedPane tabbedPane = new JTabbedPane();
            tabbedPane.addTab("Book Room", new BookRoomPanel());
            tabbedPane.addTab("My Reservations", new MyReservationsPanel());
            tabbedPane.addTab("Leave a Review", new LeaveReviewPanel());

            add(tabbedPane);
        }

        class BookRoomPanel extends JPanel {
            private JComboBox<String> roomComboBox;
            private JTextField checkInField, checkOutField;
            private JLabel roomImageLabel;
            private JTextArea roomDescriptionArea;

            public BookRoomPanel() {
                setLayout(new GridBagLayout());
                setBackground(new Color(240, 240, 240));
                GridBagConstraints gbc = new GridBagConstraints();
                gbc.insets = new Insets(10, 10, 10, 10);

                // Room Selection
                JLabel roomLabel = new JLabel("Select Room:");
                roomLabel.setFont(new Font("Arial", Font.PLAIN, 14));
                gbc.gridx = 0;
                gbc.gridy = 0;
                gbc.anchor = GridBagConstraints.LINE_END;
                add(roomLabel, gbc);

                roomComboBox = new JComboBox<>();
                roomComboBox.setFont(new Font("Arial", Font.PLAIN, 14));
                roomComboBox.addActionListener(e -> updateRoomDetails());
                gbc.gridx = 1;
                gbc.gridy = 0;
                gbc.anchor = GridBagConstraints.LINE_START;
                add(roomComboBox, gbc);

                // Room Image
                roomImageLabel = new JLabel();
                roomImageLabel.setPreferredSize(new Dimension(300, 200)); // Set image size
                gbc.gridx = 0;
                gbc.gridy = 1;
                gbc.gridwidth = 2;
                gbc.anchor = GridBagConstraints.CENTER;
                add(roomImageLabel, gbc);

                // Room Description
                roomDescriptionArea = new JTextArea(5, 30);
                roomDescriptionArea.setEditable(false);
                roomDescriptionArea.setFont(new Font("Arial", Font.PLAIN, 14));
                JScrollPane scrollPane = new JScrollPane(roomDescriptionArea);
                gbc.gridx = 0;
                gbc.gridy = 2;
                gbc.gridwidth = 2;
                gbc.anchor = GridBagConstraints.CENTER;
                add(scrollPane, gbc);

                // Check-In Date
                JLabel checkInLabel = new JLabel("Check-In Date (YYYY-MM-DD):");
                checkInLabel.setFont(new Font("Arial", Font.PLAIN, 14));
                gbc.gridx = 0;
                gbc.gridy = 3;
                gbc.anchor = GridBagConstraints.LINE_END;
                add(checkInLabel, gbc);

                checkInField = new JTextField(20);
                checkInField.setFont(new Font("Arial", Font.PLAIN, 14));
                gbc.gridx = 1;
                gbc.gridy = 3;
                gbc.anchor = GridBagConstraints.LINE_START;
                add(checkInField, gbc);

                // Check-Out Date
                JLabel checkOutLabel = new JLabel("Check-Out Date (YYYY-MM-DD):");
                checkOutLabel.setFont(new Font("Arial", Font.PLAIN, 14));
                gbc.gridx = 0;
                gbc.gridy = 4;
                gbc.anchor = GridBagConstraints.LINE_END;
                add(checkOutLabel, gbc);

                checkOutField = new JTextField(20);
                checkOutField.setFont(new Font("Arial", Font.PLAIN, 14));
                gbc.gridx = 1;
                gbc.gridy = 4;
                gbc.anchor = GridBagConstraints.LINE_START;
                add(checkOutField, gbc);

                // Book Button
                JButton bookButton = new JButton("Book Room");
                styleButton(bookButton);
                bookButton.addActionListener(e -> bookRoom());
                gbc.gridx = 0;
                gbc.gridy = 5;
                gbc.gridwidth = 2;
                gbc.anchor = GridBagConstraints.CENTER;
                add(bookButton, gbc);

                // Load available rooms
                loadAvailableRooms();
            }

            private void loadAvailableRooms() {
                try (Connection conn = DriverManager.getConnection(DB_URL, DB_USER, DB_PASSWORD)) {
                    String query = "SELECT * FROM rooms WHERE availability = TRUE";
                    PreparedStatement stmt = conn.prepareStatement(query);
                    ResultSet rs = stmt.executeQuery();

                    while (rs.next()) {
                        roomComboBox.addItem("Room ID: " + rs.getInt("id") + " - " + rs.getString("room_type") + " - $" + rs.getDouble("price"));
                    }
                } catch (SQLException ex) {
                    ex.printStackTrace();
                }
            }

            private void updateRoomDetails() {
                String selectedRoom = (String) roomComboBox.getSelectedItem();
                if (selectedRoom == null) return;

                int roomId = Integer.parseInt(selectedRoom.split(" ")[2]); // Extract room ID from combo box
                try (Connection conn = DriverManager.getConnection(DB_URL, DB_USER, DB_PASSWORD)) {
                    String query = "SELECT image_path, description FROM rooms WHERE id = ?";
                    PreparedStatement stmt = conn.prepareStatement(query);
                    stmt.setInt(1, roomId);
                    ResultSet rs = stmt.executeQuery();

                    if (rs.next()) {
                        String imagePath = rs.getString("image_path");
                        String description = rs.getString("description");

                        // Load and display room image
                        if (imagePath != null && !imagePath.isEmpty()) {
                            ImageIcon imageIcon = new ImageIcon(imagePath);
                            Image image = imageIcon.getImage().getScaledInstance(300, 200, Image.SCALE_SMOOTH); // Resize image
                            roomImageLabel.setIcon(new ImageIcon(image));
                        } else {
                            roomImageLabel.setIcon(null); // Clear image if no path is provided
                        }

                        // Display room description
                        roomDescriptionArea.setText(description);
                    }
                } catch (SQLException ex) {
                    ex.printStackTrace();
                }
            }

            private void bookRoom() {
                String selectedRoom = (String) roomComboBox.getSelectedItem();
                String checkInDate = checkInField.getText();
                String checkOutDate = checkOutField.getText();

                if (selectedRoom == null || checkInDate.isEmpty() || checkOutDate.isEmpty()) {
                    JOptionPane.showMessageDialog(this, "Please fill all fields", "Error", JOptionPane.ERROR_MESSAGE);
                    return;
                }

                try {
                    int roomId = Integer.parseInt(selectedRoom.split(" ")[2]); // Extract room ID from combo box
                    SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
                    Date checkIn = dateFormat.parse(checkInDate);
                    Date checkOut = dateFormat.parse(checkOutDate);

                    if (checkIn.after(checkOut)) {
                        JOptionPane.showMessageDialog(this, "Check-In date must be before Check-Out date", "Error", JOptionPane.ERROR_MESSAGE);
                        return;
                    }

                    try (Connection conn = DriverManager.getConnection(DB_URL, DB_USER, DB_PASSWORD)) {
                        // Insert booking
                        String query = "INSERT INTO bookings (user_id, room_id, check_in, check_out) VALUES (?, ?, ?, ?)";
                        PreparedStatement stmt = conn.prepareStatement(query);
                        stmt.setInt(1, userId);
                        stmt.setInt(2, roomId);
                        stmt.setDate(3, new java.sql.Date(checkIn.getTime()));
                        stmt.setDate(4, new java.sql.Date(checkOut.getTime()));
                        stmt.executeUpdate();

                        // Update room availability
                        query = "UPDATE rooms SET availability = FALSE WHERE id = ?";
                        stmt = conn.prepareStatement(query);
                        stmt.setInt(1, roomId);
                        stmt.executeUpdate();

                        JOptionPane.showMessageDialog(this, "Room booked successfully", "Success", JOptionPane.INFORMATION_MESSAGE);
                        clearFields();
                    } catch (SQLException ex) {
                        ex.printStackTrace();
                    }
                } catch (Exception ex) {
                    JOptionPane.showMessageDialog(this, "Invalid date format", "Error", JOptionPane.ERROR_MESSAGE);
                }
            }

            private void clearFields() {
                checkInField.setText("");
                checkOutField.setText("");
                roomComboBox.setSelectedIndex(0);
                roomImageLabel.setIcon(null); // Clear image
                roomDescriptionArea.setText(""); // Clear description
            }
        }

        class MyReservationsPanel extends JPanel {
            public MyReservationsPanel() {
                setLayout(new BorderLayout());
                setBackground(new Color(240, 240, 240));

                JTextArea textArea = new JTextArea();
                textArea.setEditable(false);
                textArea.setFont(new Font("Arial", Font.PLAIN, 14));
                JScrollPane scrollPane = new JScrollPane(textArea);
                add(scrollPane, BorderLayout.CENTER);

                try (Connection conn = DriverManager.getConnection(DB_URL, DB_USER, DB_PASSWORD)) {
                    String query = "SELECT b.id, b.room_id, b.check_in, b.check_out, r.room_type, r.price " +
                            "FROM bookings b " +
                            "JOIN rooms r ON b.room_id = r.id " +
                            "WHERE b.user_id = ?";
                    PreparedStatement stmt = conn.prepareStatement(query);
                    stmt.setInt(1, userId);
                    ResultSet rs = stmt.executeQuery();

                    while (rs.next()) {
                        textArea.append("Booking ID: " + rs.getInt("id") + "\n");
                        textArea.append("Room ID: " + rs.getInt("room_id") + "\n");
                        textArea.append("Room Type: " + rs.getString("room_type") + "\n");
                        textArea.append("Price: $" + rs.getDouble("price") + "\n");
                        textArea.append("Check-In: " + rs.getDate("check_in") + "\n");
                        textArea.append("Check-Out: " + rs.getDate("check_out") + "\n\n");
                    }
                } catch (SQLException ex) {
                    ex.printStackTrace();
                }
            }
        }

        class LeaveReviewPanel extends JPanel {
            private JComboBox<String> roomComboBox;
            private JSlider ratingSlider;
            private JTextArea commentArea;

            public LeaveReviewPanel() {
                setLayout(new GridBagLayout());
                setBackground(new Color(240, 240, 240));
                GridBagConstraints gbc = new GridBagConstraints();
                gbc.insets = new Insets(10, 10, 10, 10);

                // Room Selection
                JLabel roomLabel = new JLabel("Select Room:");
                roomLabel.setFont(new Font("Arial", Font.PLAIN, 14));
                gbc.gridx = 0;
                gbc.gridy = 0;
                gbc.anchor = GridBagConstraints.LINE_END;
                add(roomLabel, gbc);

                roomComboBox = new JComboBox<>();
                roomComboBox.setFont(new Font("Arial", Font.PLAIN, 14));
                gbc.gridx = 1;
                gbc.gridy = 0;
                gbc.anchor = GridBagConstraints.LINE_START;
                add(roomComboBox, gbc);

                // Load rooms booked by the user
                loadBookedRooms();

                // Rating Slider
                JLabel ratingLabel = new JLabel("Rating (1-5):");
                ratingLabel.setFont(new Font("Arial", Font.PLAIN, 14));
                gbc.gridx = 0;
                gbc.gridy = 1;
                gbc.anchor = GridBagConstraints.LINE_END;
                add(ratingLabel, gbc);

                ratingSlider = new JSlider(1, 5, 3);
                ratingSlider.setMajorTickSpacing(1);
                ratingSlider.setPaintTicks(true);
                ratingSlider.setPaintLabels(true);
                gbc.gridx = 1;
                gbc.gridy = 1;
                gbc.anchor = GridBagConstraints.LINE_START;
                add(ratingSlider, gbc);

                // Comment Field
                JLabel commentLabel = new JLabel("Comment:");
                commentLabel.setFont(new Font("Arial", Font.PLAIN, 14));
                gbc.gridx = 0;
                gbc.gridy = 2;
                gbc.anchor = GridBagConstraints.LINE_END;
                add(commentLabel, gbc);

                commentArea = new JTextArea(5, 20);
                commentArea.setFont(new Font("Arial", Font.PLAIN, 14));
                JScrollPane scrollPane = new JScrollPane(commentArea);
                gbc.gridx = 1;
                gbc.gridy = 2;
                gbc.anchor = GridBagConstraints.LINE_START;
                add(scrollPane, gbc);

                // Submit Button
                JButton submitButton = new JButton("Submit Review");
                styleButton(submitButton);
                submitButton.addActionListener(e -> submitReview());
                gbc.gridx = 0;
                gbc.gridy = 3;
                gbc.gridwidth = 2;
                gbc.anchor = GridBagConstraints.CENTER;
                add(submitButton, gbc);
            }

            private void loadBookedRooms() {
                try (Connection conn = DriverManager.getConnection(DB_URL, DB_USER, DB_PASSWORD)) {
                    String query = "SELECT DISTINCT r.id, r.room_type FROM bookings b " +
                            "JOIN rooms r ON b.room_id = r.id " +
                            "WHERE b.user_id = ?";
                    PreparedStatement stmt = conn.prepareStatement(query);
                    stmt.setInt(1, userId);
                    ResultSet rs = stmt.executeQuery();

                    while (rs.next()) {
                        roomComboBox.addItem("Room ID: " + rs.getInt("id") + " - " + rs.getString("room_type"));
                    }
                } catch (SQLException ex) {
                    ex.printStackTrace();
                }
            }

            private void submitReview() {
                String selectedRoom = (String) roomComboBox.getSelectedItem();
                int rating = ratingSlider.getValue();
                String comment = commentArea.getText();

                if (selectedRoom == null || comment.isEmpty()) {
                    JOptionPane.showMessageDialog(this, "Please fill all fields", "Error", JOptionPane.ERROR_MESSAGE);
                    return;
                }

                try {
                    int roomId = Integer.parseInt(selectedRoom.split(" ")[2]); // Extract room ID from combo box
                    try (Connection conn = DriverManager.getConnection(DB_URL, DB_USER, DB_PASSWORD)) {
                        String query = "INSERT INTO reviews (user_id, room_id, rating, comment) VALUES (?, ?, ?, ?)";
                        PreparedStatement stmt = conn.prepareStatement(query);
                        stmt.setInt(1, userId);
                        stmt.setInt(2, roomId);
                        stmt.setInt(3, rating);
                        stmt.setString(4, comment);
                        stmt.executeUpdate();
                        JOptionPane.showMessageDialog(this, "Review submitted successfully", "Success", JOptionPane.INFORMATION_MESSAGE);
                        clearFields();
                    } catch (SQLException ex) {
                        ex.printStackTrace();
                    }
                } catch (NumberFormatException ex) {
                    JOptionPane.showMessageDialog(this, "Invalid room ID format", "Error", JOptionPane.ERROR_MESSAGE);
                }
            }

            private void clearFields() {
                roomComboBox.setSelectedIndex(0);
                ratingSlider.setValue(3);
                commentArea.setText("");
            }
        }
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> new HotelManagementSystem().setVisible(true));
    }
}