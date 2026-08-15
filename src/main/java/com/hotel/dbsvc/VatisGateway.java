package com.hotel.dbsvc;

import java.sql.Connection;
import java.sql.SQLException;

public interface VatisGateway
{
   Connection ready() throws SQLException;
}
