package com.hotel.databases;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class CardRoom
{
    private String roomDesign;
    private String roomNumber;
    private String roomType;
    private int roomPrice;
    private String status;

    public CardRoom (String roomNumber, String roomType, int roomPrice, String roomDesign)
    {
        this.roomNumber = roomNumber;
        this.roomType = roomType;
        this.roomPrice = roomPrice;
        this.roomDesign = roomDesign;
        checkIfOccupied(roomNumber);
    }
    public CardRoom (String roomNumber)
    {
        String query = "SELECT design, prixNuite, type FROM chambre WHERE numChambr = ?";

        try (Connection butler = Vatis.prepare().ready(); PreparedStatement ticket = butler.prepareStatement(query))
        {
            ticket.setString(1, roomNumber);

            try ( ResultSet row = ticket.executeQuery())
            {
                if(row.next())
                {
                    this.roomNumber = roomNumber;
                    this.roomType = row.getString("design");
                    this.roomPrice = row.getInt("prixNuite");
                    this.roomDesign = row.getString("type");
                    checkIfOccupied(roomNumber);
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
    public String getRoomDesign() { return roomDesign; }

    public final Result buildRoom()
    {
        String query = "INSERT INTO chambre (numChambr, design, type, prixNuite) VALUES (?, ?, ?, ?)";

        try (Connection butler = Vatis.prepare().ready(); PreparedStatement ticket = butler.prepareStatement(query))
        {
            ticket.setString(1, roomNumber);
            ticket.setString(2, roomType);
            ticket.setString(3, roomDesign);
            ticket.setInt(4, roomPrice);
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
        String query = "UPDATE chambre SET design = ?, prixNuite = ?, type = ? WHERE numChambr = ?";

        try (Connection butler = Vatis.prepare().ready(); PreparedStatement ticket = butler.prepareStatement(query))
        {
            ticket.setString(1, roomDesign);
            ticket.setInt(2, roomPrice);
            ticket.setString(3, roomType);
            ticket.setString(4, roomNumber);
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

    public final void checkIfOccupied (String roomNumber)
    {
        String query = "SELECT numChambr FROM sejourner WHERE numChambr = ? AND current_date BETWEEN dateentreesejour AND dateentreesejour + (nbrjour * INTERVAL '1 day')";
        String query_2 = "SELECT numChambr FROM reserver WHERE numChambr = ? AND current_date BETWEEN dateentree AND dateentree + (nbrjour * INTERVAL '1 day')";

        try (Connection butler = Vatis.prepare().ready(); Connection butler_2 = Vatis.prepare().ready();
             PreparedStatement ticket = butler.prepareStatement(query); PreparedStatement ticket_2 = butler_2.prepareStatement(query_2))
        {
            ticket.setString(1, roomNumber);
            ticket_2.setString(1, roomNumber);

            try (ResultSet row = ticket.executeQuery(); ResultSet row_2 = ticket_2.executeQuery())
            {
                if (row.next() || row_2.next()) { this.status = "O"; }
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
        List<CardRoom> roomList = new ArrayList<>();
        String query = getQuery(filter);

        try (Connection butler = Vatis.prepare().ready(); PreparedStatement ticket = butler.prepareStatement(query); ResultSet row = ticket.executeQuery())
        {
            while(row.next())
            {
                String roomNumber = row.getString("numChambr");
                String roomType = row.getString("design");
                int roomPrice = row.getInt("prixNuite");
                String roomDesign = row.getString("type");
                CardRoom room = new CardRoom (roomNumber, roomType, roomPrice, roomDesign);
                roomList.add(room);
            }
        }
        catch (SQLException ex)
        {
            ex.printStackTrace();
        }
        return roomList;
    }

    private static String getQuery(String filter)
    {
        String query = "SELECT * FROM chambre";
        if (filter.equals("Occuper"))
        {
            query = "SELECT c.numChambr, c.design, c.prixNuite, c.type FROM chambre c " +
                        "WHERE EXISTS (SELECT 1 FROM reserver r WHERE r.numChambr = c.numChambr AND CURRENT_DATE BETWEEN r.dateentree AND r.dateentree + (r.nbrjour * INTERVAL '1 day')) " +
                        "OR EXISTS (SELECT 1 FROM sejourner s WHERE s.numChambr = c.numChambr AND CURRENT_DATE BETWEEN s.dateentreesejour AND s.dateentreesejour + (s.nbrjour * INTERVAL '1 day'))";
        }
        else if (filter.equals("Libre"))
        {
            query = "SELECT c.numChambr, c.design, c.prixNuite, c.type FROM chambre c WHERE NOT EXISTS (SELECT 1 FROM sejourner s WHERE s.numChambr = c.numChambr) AND " +
                    "NOT EXISTS (SELECT 1 FROM reserver r WHERE r.numChambr = c.numChambr AND CURRENT_DATE BETWEEN r.dateentree AND r.dateentree + (r.nbrjour * INTERVAL '1 day'))";
        }
        return query;
    }
}
