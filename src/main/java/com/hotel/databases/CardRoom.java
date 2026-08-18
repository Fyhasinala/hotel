package com.hotel.databases;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;

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
        this.status = "L";
    }
    public CardRoom (String roomNumber)
    {
        String query = "SELECT design, prixNuite FROM chambre WHERE numChambr = ?";

        try (Connection butler = Vatis.prepare().ready(); PreparedStatement ticket = butler.prepareStatement(query))
        {
            ticket.setString(1, roomNumber);

            try ( ResultSet row = ticket.executeQuery())
            {
                if(row.next())
                {
                    this.status = "L";
                    this.roomNumber = roomNumber;
                    this.roomType = row.getString("design");
                    this.roomPrice = row.getInt("prixNuite");
                }
            }
        }
        catch (SQLException ex)
        {
            ex.printStackTrace();
        }

    }

    public String getRoomNumber() { return roomNumber; }
    public String getRoomType() { return roomType; }
    public int getRoomPrice() { return roomPrice; }
    public String getStatus() { return status; }

    public final Result buildRoom()
    {
        String query = "INSERT INTO chambre (numChambr, design, prixNuite) VALUES (?, ?, ?)";

        try (Connection butler = Vatis.prepare().ready(); PreparedStatement ticket = butler.prepareStatement(query))
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

    public final Result upgradeRoom()
    {
        String query = "UPDATE chambre SET design = ?, prixNuite = ? WHERE numChambr = ?";

        try (Connection butler = Vatis.prepare().ready(); PreparedStatement ticket = butler.prepareStatement(query))
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

    public void checkIfOccupied (String roomNumber)
    {
        String query = "SELECT numChambr FROM occuper WHERE numChambr = ?";

        try (Connection butler = Vatis.prepare().ready(); PreparedStatement ticket = butler.prepareStatement(query))
        {
            ticket.setString(1, roomNumber);

            try (ResultSet row = ticket.executeQuery())
            {
                if (row.next()) { this.status = "O"; }
                else this.status = "L";
            }
        }
        catch (SQLException ex)
        {
            ex.printStackTrace();
        }
    }

    public Result destroyRoom ()
    {
        String query = "DELETE FROM chambre WHERE numChambr = ?";

        try (Connection butler = Vatis.prepare().ready(); PreparedStatement ticket = butler.prepareStatement(query))
        {
            ticket.setString(1, roomNumber);
            int rowAffected = ticket.executeUpdate();

            if (rowAffected > 0)
            {
                return Result.success("La démolition du chambre est terminée");
            }
            return Result.failure("La démolition du chambre est impossible pour le moment");
        }
        catch (SQLException ex)
        {
            ex.printStackTrace();
            return Result.exception();
        }
    }

    public static List<CardRoom> listAllRooms (String filter)
    {
        String query = "SELECT * FROM chambre";
        if (filter.equals("Occuper"))
        {
            query = "SELECT c.numChambr, c.design, c.prixNuite FROM occuper o JOIN reserver r ON r.idreserv = o.idreserv JOIN chambre c ON r.numchambr = c.numChambr";
        }
    }
}
