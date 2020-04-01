package me.kirito5572.scp079.object;

import java.sql.ResultSet;
import java.sql.SQLException;

public interface ResultSetComparator {
    boolean compare(ResultSet resultSet) throws SQLException;
}
