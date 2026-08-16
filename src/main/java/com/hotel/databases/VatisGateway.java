package com.hotel.databases;

import java.sql.Connection;
import java.sql.SQLException;

public interface VatisGateway
{
   Connection ready() throws SQLException;
}
