package me.kirito5572.scp079.object;

import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.sql.*;
import java.text.SimpleDateFormat;
import java.util.Date;

public class SQLDB {
    private static Connection connection;
    private static Statement statement;
    private static ResultSet resultSet;
    private static int caseID;
    private static String driverName;
    private static String url;
    private static String user;
    private static String password;

    public SQLDB() {
        //init
        StringBuilder SQLPassword = new StringBuilder();
        try {
            File file = new File("C:\\DiscordServerBotSecrets\\SCP-079\\SQLPassword.txt");
            FileReader fileReader = new FileReader(file);
            int singalCh;
            while ((singalCh = fileReader.read()) != -1) {
                SQLPassword.append((char) singalCh);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        StringBuilder endPoint = new StringBuilder();
        try {
            File file = new File("C:\\DiscordServerBotSecrets\\SCP-079\\endPoint.txt");
            FileReader fileReader = new FileReader(file);
            int singalCh;
            while ((singalCh = fileReader.read()) != -1) {
                endPoint.append((char) singalCh);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        driverName = "com.mysql.cj.jdbc.Driver";
        url = "jdbc:mysql://" + endPoint.toString() + "/mainDB?serverTimezone=UTC";
        user = "admin";
        password = SQLPassword.toString();

        try {
            Class.forName(driverName);

            connection = DriverManager.getConnection(url, user, password);
            statement = connection.createStatement();
            resultSet = statement.executeQuery("SELECT * FROM `079_config`.last_caseID");
            if(resultSet.next()) {
                caseID = resultSet.getInt("caseID");
            }
        } catch (ClassNotFoundException | SQLException e) {
            e.printStackTrace();
        }
    }

    public static void SQLupload(String SteamID, String time, String reason, String server, String serverID) {
        if (serverID.equals("600010501266866186")) {
            return;
        }
        caseIDup();

        Date date = new Date();
        SimpleDateFormat dayTime = new SimpleDateFormat("yyyy-MM-dd kk:mm:ss");
        String sanctionTime = dayTime.format(date);
        String queryString = "INSERT INTO mainDB.Sanction_Information VALUES (" + caseID + "," + SteamID + ", '" + sanctionTime + "', '" + time + "', '" + reason + "', '" + server + "' , " + serverID + ");";

        System.out.println(queryString);
        try {
            Class.forName(driverName);

            statement = connection.createStatement();
            statement.executeUpdate(queryString);
            statement.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static String[] SQLdownload(String SteamID) throws SQLException, ClassNotFoundException {
        String[] data = new String[]{
                null, null, null, null, null,
                null, null, null, null, null
        };

        String queryString = "SELECT * FROM mainDB.Sanction_Information WHERE SteamID =" + SteamID;
        Class.forName(driverName);

        statement = connection.createStatement();
        resultSet = statement.executeQuery(queryString);
        int i = 0;
        while (resultSet.next()) {
            data[i] = resultSet.getString("caseID");
            i++;
        }
        statement.close();
        return data;
    }

    private static void caseIDup() {
        caseID++;
        try {
            statement = connection.createStatement();
            statement.executeUpdate("UPDATE `079_config`.last_caseID SET caseID=" + caseID);
            statement.close();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public static String[] SQLdownload(int caseID) throws SQLException, ClassNotFoundException {
        String[] data = new String[7];

        String queryString = "SELECT * FROM mainDB.Sanction_Information WHERE caseID =" + caseID;
        Class.forName(driverName);

        statement = connection.createStatement();
        resultSet = statement.executeQuery(queryString);
        while (resultSet.next()) {
            data[0] = resultSet.getString("caseID");
            data[1] = resultSet.getString("SteamID");
            data[2] = resultSet.getString("sanctionTime");
            data[3] = resultSet.getString("endTime");
            data[4] = resultSet.getString("reason");
            data[5] = resultSet.getString("sendServer");
            data[6] = resultSet.getString("serverID");
        }
        statement.close();
        return data;
    }

    public static String[] GreenDBDownload(String steamID) throws ClassNotFoundException, SQLException {
        String[] data = new String[]{
                null, null, null, null, null,
                null, null, null, null, null
        };

        String queryString = "SELECT * FROM ritobotDB.Sanction_Infor WHERE SteamID =" + steamID;
        Class.forName(driverName);

        statement = connection.createStatement();
        resultSet = statement.executeQuery(queryString);
        int i = 0;
        while (resultSet.next()) {
            data[i] = resultSet.getString("caseID");
            i++;
        }
        statement.close();
        return data;
    }

    public static String[] GreenDBDownload(int caseID, String steamID) throws ClassNotFoundException, SQLException {
        String[] data = new String[]{
                null, null, null, null, null,
                null, null, null, null, null
        };

        String queryString = "SELECT * FROM ritobotDB.Sanction_Infor WHERE SteamID =" + steamID;
        Class.forName(driverName);

        statement = connection.createStatement();
        resultSet = statement.executeQuery(queryString);
        while (resultSet.next()) {
            String a = resultSet.getString("caseID");
            if (caseID == Integer.parseInt(a)) {
                data[0] = a;
                data[1] = resultSet.getString("SteamID");
                data[2] = resultSet.getString("DBWriteTime");
                data[3] = resultSet.getString("time");
                data[4] = resultSet.getString("reason");
                data[5] = resultSet.getString("confirmUser");
                data[6] = "600010501266866186";
            }
        }
        statement.close();
        return data;
    }

    public static Connection getConnection() {
        return connection;
    }

    public static void setConnection(Connection connection) {
        SQLDB.connection = connection;
    }

    public static void reConnect() {
        try {
            Class.forName(driverName);

            setConnection(DriverManager.getConnection(url, user, password));
        } catch (ClassNotFoundException | SQLException e) {
            e.printStackTrace();
        }
    }
}
