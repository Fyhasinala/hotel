package com.hotel.databases;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;

public class CardRoom
{
    private String roomNumber;
    private String roomType;
    private int roomPrice;
    private String status;

    public CardRoom (String roomNumber, String roomType, int roomPrice)
    {
        this.roomNumber = roomNumber;
        this.roomType = roomType;
        this.roomPrice = roomPrice;
    }

    public String getRoomNumber() { return roomNumber; }
    public String getRoomType() { return roomType; }
    public int getRoomPrice() { return roomPrice; }

    public Result createRoomCard()
    {
        String query = "INSERT INTO chambre (numChambr, design, prixNuite) VALUES (?, ?, ?)";

        try (Connection butler = Vatis.prepare().ready(); PreparedStatement ticket = butler.prepareStatement(query);)
        {
            ticket.setString(1, roomNumber);
            ticket.setString(2, roomType);
            ticket.setInt(3, roomPrice);
            int rowAffected = ticket.executeUpdate();

            if (rowAffected > 0)
            {
                return Result.success("La construction du nouveau chambre est terminée.");
            }
            return Result.failure("La construction est impossible pour le moment.");
        }
        catch (SQLException ex)
        {
            ex.printStackTrace();
            return Result.exception();
        }
    }

    public Result updateRoomCard()
    {
        String query = "UPDATE chambre SET design = ?, prixNuite = ? WHERE numChambr = ?";

        try (Connection butler = Vatis.prepare().ready(); PreparedStatement ticket = butler.prepareStatement(query);)
        {
            ticket.setString(1, roomType);
            ticket.setInt(2, roomPrice);
            ticket.setString(3, roomNumber);
            int rowAffected = ticket.executeUpdate();

            if (rowAffected > 0)
            {
                return Result.success("La rénovation de la chambre est terminée.");
            }
            return Result.failure("La rénovation est impossible pour le moment.");
        }
        catch (SQLException ex)
        {
            ex.printStackTrace();
            return Result.exception();
        }
    }
}
