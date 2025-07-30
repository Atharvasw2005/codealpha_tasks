package Hotel;

import java.sql.*;
import java.util.*;

public class Hotel {
    private Connection conn;

    public Hotel(String dbUrl) throws SQLException {
        conn = DriverManager.getConnection(dbUrl);
        createTables();
    }

    private void createTables() throws SQLException {
        Statement stmt = conn.createStatement();

        // Create rooms table
        stmt.executeUpdate("CREATE TABLE IF NOT EXISTS rooms (" +
                "id INTEGER PRIMARY KEY AUTOINCREMENT," +
                "number TEXT," +
                "category TEXT," +
                "available BOOLEAN)");

        // Create bookings table
        stmt.executeUpdate("CREATE TABLE IF NOT EXISTS bookings (" +
                "id INTEGER PRIMARY KEY AUTOINCREMENT," +
                "room_id INTEGER," +
                "guest_name TEXT," +
                "check_in DATE," +
                "check_out DATE," +
                "paid BOOLEAN," +
                "FOREIGN KEY(room_id) REFERENCES rooms(id))");

        stmt.close();
    }

    public void addRoom(String number, String category, boolean available) throws SQLException {
        PreparedStatement ps = conn.prepareStatement(
                "INSERT INTO rooms (number, category, available) VALUES (?, ?, ?)");
        ps.setString(1, number);
        ps.setString(2, category);
        ps.setBoolean(3, available);
        ps.executeUpdate();
        ps.close();
    }

    public List<Integer> searchRooms(String category) throws SQLException {
        List<Integer> availableRooms = new ArrayList<>();
        PreparedStatement ps = conn.prepareStatement(
                "SELECT id FROM rooms WHERE category=? AND available=1");
        ps.setString(1, category);
        ResultSet rs = ps.executeQuery();
        while (rs.next()) {
            availableRooms.add(rs.getInt("id"));
        }
        rs.close();
        ps.close();
        return availableRooms;
    }

    public boolean bookRoom(int roomId, String guest, String checkIn, String checkOut) throws SQLException {
        PreparedStatement ps = conn.prepareStatement(
                "INSERT INTO bookings (room_id, guest_name, check_in, check_out, paid) VALUES (?, ?, ?, ?, 0)");
        ps.setInt(1, roomId);
        ps.setString(2, guest);
        ps.setString(3, checkIn);
        ps.setString(4, checkOut);
        int rows = ps.executeUpdate();
        ps.close();

        if (rows > 0) {
            PreparedStatement updateRoom = conn.prepareStatement(
                    "UPDATE rooms SET available=0 WHERE id=?");
            updateRoom.setInt(1, roomId);
            updateRoom.executeUpdate();
            updateRoom.close();
            return true;
        }
        return false;
    }

    public boolean cancelBooking(int bookingId) throws SQLException {
        PreparedStatement ps = conn.prepareStatement(
                "SELECT room_id FROM bookings WHERE id=?");
        ps.setInt(1, bookingId);
        ResultSet rs = ps.executeQuery();
        if (rs.next()) {
            int roomId = rs.getInt("room_id");

            PreparedStatement del = conn.prepareStatement(
                    "DELETE FROM bookings WHERE id=?");
            del.setInt(1, bookingId);
            del.executeUpdate();
            del.close();

            PreparedStatement updateRoom = conn.prepareStatement(
                    "UPDATE rooms SET available=1 WHERE id=?");
            updateRoom.setInt(1, roomId);
            updateRoom.executeUpdate();
            updateRoom.close();
            rs.close();
            ps.close();
            return true;
        }
        rs.close();
        ps.close();
        return false;
    }

    public boolean payForBooking(int bookingId) throws SQLException {
        PreparedStatement ps = conn.prepareStatement(
                "UPDATE bookings SET paid=1 WHERE id=?");
        ps.setInt(1, bookingId);
        int rows = ps.executeUpdate();
        ps.close();
        return rows > 0;
    }

    public void viewBooking(int bookingId) throws SQLException {
        PreparedStatement ps = conn.prepareStatement(
                "SELECT * FROM bookings WHERE id=?");
        ps.setInt(1, bookingId);
        ResultSet rs = ps.executeQuery();
        if (rs.next()) {
            System.out.println("Booking ID: " + rs.getInt("id"));
            System.out.println("Room ID: " + rs.getInt("room_id"));
            System.out.println("Guest: " + rs.getString("guest_name"));
            System.out.println("Check-in: " + rs.getString("check_in"));
            System.out.println("Check-out: " + rs.getString("check_out"));
            System.out.println("Paid: " + (rs.getBoolean("paid") ? "Yes" : "No"));
        } else {
            System.out.println("Booking not found.");
        }
        rs.close();
        ps.close();
    }
}

