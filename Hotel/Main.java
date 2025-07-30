package Hotel;

import java.sql.*;
import java.util.*;

public class Main {
    public static void main(String[] args) {
        try {
            Hotel hotel = new Hotel("jdbc:sqlite:atharva_hotel.db");

            hotel.addRoom("101", "Deluxe", true);
            hotel.addRoom("102", "Standard", true);
            hotel.addRoom("103", "Deluxe", true);

            List<Integer> rooms = hotel.searchRooms("Deluxe");
            System.out.println("Available Deluxe Room IDs: " + rooms);

            if (!rooms.isEmpty()) {
                int roomId = rooms.get(0);
                boolean booked = hotel.bookRoom(roomId, "Atharva", "2025-08-01", "2025-08-05");
                if (booked) {
                    System.out.println("Room " + roomId + " booked successfully.");
                }
            }

            hotel.viewBooking(1);

            
            if (hotel.payForBooking(1)) {
                System.out.println("Booking paid successfully.");
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
}

