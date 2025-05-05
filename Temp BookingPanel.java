package HotelReservationSystem;

import java.awt.*;
import java.awt.event.*;
import java.sql.*;
import java.util.*;
import javax.swing.*;
import java.util.HashSet;
import java.util.Random;
import java.util.Set;

public class BookingPanel extends JPanel {
    private final JLabel TopMessage1;
    private final JLabel TopMessage2;
    private final JLabel TopMessage3;
    private JLabel roomTypeLabel;
    private JPanel panpan, panpan2, panpan3, panpan4;
    private Connection conn;

    private String arrivalDate = "";
    private String departureDate = "";
    private String guestCount = "";

    private final Set<Integer> selectedRooms = new HashSet<>();
    private final Map<Integer, String> roomTypeMap = new HashMap<>();

    public BookingPanel() {
        connectToDatabase();

        setBackground(new Color(0xFFE5B4));
        setLayout(null);

        JPanel TopMessagePanel = new JPanel();
        TopMessagePanel.setBounds(0, 0, 1280, 70);
        TopMessagePanel.setBackground(new Color(0x780000));
        TopMessagePanel.setLayout(null);
        add(TopMessagePanel);

        TopMessage1 = new JLabel("Arrival Date: ");
        TopMessage1.setBounds(50, 5, 300, 15);
        TopMessage1.setForeground(new Color(0xDDA15E));
        TopMessagePanel.add(TopMessage1);

        TopMessage2 = new JLabel("Departure Date: ");
        TopMessage2.setBounds(50, 25, 300, 15);
        TopMessage2.setForeground(new Color(0xDDA15E));
        TopMessagePanel.add(TopMessage2);

        TopMessage3 = new JLabel("Number of Guests: ");
        TopMessage3.setBounds(50, 45, 300, 15);
        TopMessage3.setForeground(new Color(0xDDA15E));
        TopMessagePanel.add(TopMessage3);

        JButton logoutButton = new JButton("Log Out");
        logoutButton.setBounds(1120, 20, 100, 30);
        logoutButton.setBackground(new Color(0x003049));
        logoutButton.setForeground(new Color(0xFDF0D5));
        logoutButton.setFont(new Font("Arial", Font.BOLD, 14));
        logoutButton.setFocusPainted(false);
        TopMessagePanel.add(logoutButton);

        logoutButton.addActionListener(e -> {
            int confirm = JOptionPane.showConfirmDialog(this, "Are you sure you want to log out?", "Log Out", JOptionPane.YES_NO_OPTION);
            if (confirm == JOptionPane.YES_OPTION) {
                SignUpPanel.isLoggedIn = false;
                JOptionPane.showMessageDialog(this, "You have been logged out.");
                Container parent = getParent();
                if (parent instanceof JPanel) {
                    CardLayout cl = (CardLayout) parent.getLayout();
                    cl.show(parent, "home");
                }
            }
        });

        roomTypeLabel = new JLabel("Single Rooms");
        roomTypeLabel.setBounds(565, 140, 300, 30);
        roomTypeLabel.setFont(new Font("Arial", Font.BOLD, 24));
        roomTypeLabel.setForeground(new Color(0xDDA15E));
        add(roomTypeLabel);

        // Single Rooms Panel
        panpan = createRoomPanel(new int[]{3, 4, 5}, 8, "single");
        JButton nextToDouble = createNavButton("Next →");
        nextToDouble.setBounds(525, 370, 100, 30);
        nextToDouble.addActionListener(e -> switchToPanel("double"));
        panpan.add(nextToDouble);
        panpan.add(createLegendPanel());
        add(panpan);
        
        JButton nextToPaymentFromSingle = new JButton("Proceed to Payment");
        nextToPaymentFromSingle.setBounds(800, 370, 160, 30);
        nextToPaymentFromSingle.setBackground(new Color(0xfff3e0));
        nextToPaymentFromSingle.setForeground(new Color(0xDDA15E));
        nextToPaymentFromSingle.addActionListener(e -> switchToPanel("payment"));
        panpan.add(nextToPaymentFromSingle);

        // Double Rooms Panel
        panpan2 = createRoomPanel(new int[]{6, 7, 8}, 4, "double");
        JButton prevToSingle = createNavButton("← Prev");
        prevToSingle.setBounds(415, 370, 100, 30);
        prevToSingle.addActionListener(e -> switchToPanel("single"));
        JButton nextToSuite = createNavButton("Next →");
        nextToSuite.setBounds(525, 370, 100, 30);
        nextToSuite.addActionListener(e -> switchToPanel("suite"));
        panpan2.add(prevToSingle);
        panpan2.add(nextToSuite);
        panpan2.add(createLegendPanel());
        panpan2.setVisible(false);
        add(panpan2);
                
        JButton nextToPaymentFromDouble = new JButton("Proceed to Payment");
        nextToPaymentFromDouble.setBounds(800, 370, 160, 30);
        nextToPaymentFromDouble.setBackground(new Color(0xfff3e0));
        nextToPaymentFromDouble.setForeground(new Color(0xDDA15E));
        nextToPaymentFromDouble.addActionListener(e -> switchToPanel("payment"));
        panpan2.add(nextToPaymentFromDouble);

        // Suite Rooms Panel
        panpan3 = createRoomPanel(new int[]{9, 10}, 2, "suite");
        JButton prevToDouble = createNavButton("← Prev");
        prevToDouble.setBounds(415, 370, 100, 30);
        prevToDouble.addActionListener(e -> switchToPanel("double"));
        JButton proceedToPaymentButton = new JButton("Proceed to Payment");
        proceedToPaymentButton.setBounds(800, 370, 160, 30);
        proceedToPaymentButton.setBackground(new Color(0xfff3e0));
        proceedToPaymentButton.setForeground(new Color(0xDDA15E));
        proceedToPaymentButton.addActionListener(e -> switchToPanel("payment"));
        panpan3.add(prevToDouble);
        panpan3.add(proceedToPaymentButton);
        panpan3.add(createLegendPanel());
        panpan3.setVisible(false);
        add(panpan3);

        // Payment Panel
        panpan4 = new JPanel();
        panpan4.setBounds(120, 120, 1040, 430);
        panpan4.setBackground(new Color(0xfaedcd));
        panpan4.setLayout(null);

        JLabel paymentLabel = new JLabel("   Payment Method");
        paymentLabel.setBounds(335, 110, 180, 25);
        paymentLabel.setBackground(new Color(0x669bbc));
        paymentLabel.setForeground(new Color(0xFDF0D5));
        paymentLabel.setOpaque(true);
        paymentLabel.setFont(new Font("Arial", Font.PLAIN, 14));
        panpan4.add(paymentLabel);

        String[] paymentOptions = {"Credit Card", "PayPal", "GCash"};
        JComboBox<String> paymentDropdown = new JComboBox<>(paymentOptions);
        paymentDropdown.setBounds(525, 110, 180, 25);
        panpan4.add(paymentDropdown);

        JLabel accountLabel = new JLabel("  Phone/Card Number");
        accountLabel.setBounds(335, 160, 180, 25);
        accountLabel.setBackground(new Color(0x669bbc));
        accountLabel.setForeground(new Color(0xFDF0D5));
        accountLabel.setOpaque(true);
        accountLabel.setFont(new Font("Arial", Font.PLAIN, 14));
        panpan4.add(accountLabel);

        JTextField accountField = new JTextField();
        accountField.setBounds(525, 160, 180, 25);
        panpan4.add(accountField);
        
        JLabel RefNum = new JLabel("  Referal Number");
        RefNum.setBounds(335, 210, 180, 25);
        RefNum.setBackground(new Color(0x669bbc));
        RefNum.setForeground(new Color(0xFDF0D5));
        RefNum.setOpaque(true);
        RefNum.setFont(new Font("Arial", Font.PLAIN, 14));
        panpan4.add(RefNum);
        
        JLabel EmailLabel = new JLabel("  Email");
        EmailLabel.setBounds(335, 260, 180, 25);
        EmailLabel.setBackground(new Color(0x669bbc));
        EmailLabel.setForeground(new Color(0xFDF0D5));
        EmailLabel.setOpaque(true);
        EmailLabel.setFont(new Font("Arial", Font.PLAIN, 14));
        panpan4.add(EmailLabel);

        JButton finishButton = createFinishButton();
        finishButton.setBounds(800, 370, 160, 30);  
        finishButton.setFocusable(false);
        panpan4.add(finishButton);

        JButton backToSuite = createNavButton("← Back");
        backToSuite.setBounds(415, 370, 100, 30);
        backToSuite.addActionListener(e -> switchToPanel("suite"));
        panpan4.add(backToSuite);
        panpan4.setVisible(false);
        add(panpan4);
    }

    private void connectToDatabase() {
        try {
            String url = "jdbc:mysql://localhost:3306/user_management";
            String user = "root";
            String password = "Zyture#15"; // Change to your actual password
            conn = DriverManager.getConnection(url, user, password);
        } catch (SQLException e) {
            e.printStackTrace();
            JOptionPane.showMessageDialog(this, "Database connection failed!", "Error", JOptionPane.ERROR_MESSAGE);
        }
    }

    private JPanel createRoomPanel(int[] floorNumbers, int roomsPerFloor, String roomType) {
        JPanel panel = new JPanel();
        panel.setBounds(120, 120, 1040, 430);
        panel.setBackground(new Color(0xfaedcd));
        panel.setLayout(null);

        int boxWidth = 110;
        int boxHeight = 50;
        int gap = 10;
        int floorY = 60;

        switch (roomType) {
            case "double":
                boxWidth = 150;
                boxHeight = 60;
                break;
            case "suite":
                boxWidth = 200;
                boxHeight = 90;
                break;
        }

        for (int floor : floorNumbers) {
            JLabel floorLabel = new JLabel("Floor " + floor);
            floorLabel.setBounds(45, floorY, 100, 20);
            floorLabel.setFont(new Font("Arial", Font.BOLD, 14));
            panel.add(floorLabel);

            int x = 45;
            int y = floorY + 25;

            for (int j = 1; j <= roomsPerFloor; j++) {
                int roomNumber = floor * 100 + j;
                boolean available = isRoomAvailable(roomNumber, roomType);

                JLabel box = new JLabel("Room " + roomNumber, SwingConstants.CENTER);
                box.setBounds(x, y, boxWidth, boxHeight);
                box.setOpaque(true);
                box.setBackground(available ? new Color(0xfff3e0) : new Color(0xffcccc));
                box.setBorder(BorderFactory.createLineBorder(Color.DARK_GRAY));
                box.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
                box.addMouseListener(new MouseAdapter() {
                    public void mouseClicked(MouseEvent evt) {
                        if (available) {
                            if (selectedRooms.contains(roomNumber)) {
                                selectedRooms.remove(roomNumber);
                                box.setBackground(new Color(0xfff3e0));
                            } else {
                                selectedRooms.add(roomNumber);
                                roomTypeMap.put(roomNumber, roomType);
                                box.setBackground(new Color(0xa5d6a7)); // green for selected
                            }
                        } else {
                            JOptionPane.showMessageDialog(panel, "Room " + roomNumber + " is unavailable.");
                        }
                    }
                });
                panel.add(box);
                x += boxWidth + gap;
            }

            floorY = y + boxHeight + 20;
        }

        return panel;
    }

    private boolean isRoomAvailable(int roomNumber, String roomType) {
        String query = "SELECT available FROM rooms WHERE room_number = ? AND room_type = ?";
        try (PreparedStatement stmt = conn.prepareStatement(query)) {
            stmt.setInt(1, roomNumber);
            stmt.setString(2, roomType);
            ResultSet rs = stmt.executeQuery();
            if (rs.next()) {
                return rs.getBoolean("available");
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return false;
    }

    private JButton createNavButton(String text) {
        JButton button = new JButton(text);
        button.setBackground(new Color(0xfff3e0));
        button.setForeground(new Color(0xDDA15E));
        return button;
    }

    private JButton createFinishButton() {
        JButton finish = new JButton("Finished Booking");
        finish.setBackground(new Color(0xfff3e0));
        finish.setForeground(new Color(0xDDA15E));
        finish.addActionListener(e -> saveBookingToDatabase());
        return finish;
    }

    private void saveBookingToDatabase() {
        if (selectedRooms.isEmpty()) {
            JOptionPane.showMessageDialog(this, "Please select at least one room to book.");
            return;
        }

        try {
            conn.setAutoCommit(false);
            for (Integer roomNumber : selectedRooms) {
                String type = roomTypeMap.get(roomNumber);

                String insertSQL = "INSERT INTO reservations (room_number, room_type, arrival_date, departure_date, guests) VALUES (?, ?, ?, ?, ?)";
                try (PreparedStatement stmt = conn.prepareStatement(insertSQL)) {
                    stmt.setInt(1, roomNumber);
                    stmt.setString(2, type);
                    stmt.setString(3, arrivalDate);
                    stmt.setString(4, departureDate);
                    stmt.setInt(5, Integer.parseInt(guestCount));
                    stmt.executeUpdate();
                }

                String updateSQL = "UPDATE rooms SET available = false WHERE room_number = ?";
                try (PreparedStatement stmt = conn.prepareStatement(updateSQL)) {
                    stmt.setInt(1, roomNumber);
                    stmt.executeUpdate();
                }
            }

            conn.commit();
            selectedRooms.clear();
            roomTypeMap.clear();
            JOptionPane.showMessageDialog(this, "Booking completed!");
            switchToPanel("single");
        } catch (SQLException ex) {
            try {
                conn.rollback();
            } catch (SQLException rollbackEx) {
                rollbackEx.printStackTrace();
            }
            ex.printStackTrace();
            JOptionPane.showMessageDialog(this, "Failed to complete booking.");
        }
    }
    
    private int generateUniqueRandomNumber() {
        int number;
        do {
            number = 100000 + random.nextInt(900000); // Generates 6-digit number
        } while (generatedNumbers.contains(number));
        generatedNumbers.add(number);
        return number;
    }

    private JPanel createLegendPanel() {
        JPanel legendPanel = new JPanel();
        legendPanel.setBounds(45, 365, 300, 50);
        legendPanel.setLayout(new GridLayout(1, 3));

        JLabel availableLabel = new JLabel("Available", JLabel.CENTER);
        availableLabel.setBackground(new Color(0xfff3e0));
        availableLabel.setOpaque(true);
        legendPanel.add(availableLabel);

        JLabel bookedLabel = new JLabel("Booked", JLabel.CENTER);
        bookedLabel.setBackground(new Color(0xffcccc));
        bookedLabel.setOpaque(true);
        legendPanel.add(bookedLabel);

        JLabel selectedLabel = new JLabel("Selected", JLabel.CENTER);
        selectedLabel.setBackground(new Color(0xa5d6a7));
        selectedLabel.setOpaque(true);
        legendPanel.add(selectedLabel);

        return legendPanel;
    }

    private void switchToPanel(String panelName) {
        panpan.setVisible(false);
        panpan2.setVisible(false);
        panpan3.setVisible(false);
        panpan4.setVisible(false);

        switch (panelName) {
            case "single":
                roomTypeLabel.setText("Single Rooms");
                panpan.setVisible(true);
                break;
            case "double":
                roomTypeLabel.setText("Double Rooms");
                panpan2.setVisible(true);
                break;
            case "suite":
                roomTypeLabel.setText("Suite Rooms");
                panpan3.setVisible(true);
                break;
            case "payment":
                roomTypeLabel.setText("    Payment");
                panpan4.setVisible(true);
                break;
        }
    }
}
