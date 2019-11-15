package SCP079.Objects;

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
    private static String url2;
    private static String user;
    private static String password;
    public SQLDB() {
        //init
        StringBuilder caseIDBuilder = new StringBuilder();
        try {
            File file = new File("C:\\DiscordServerBotSecrets\\SCP-079\\caseID.txt");
            FileReader fileReader = new FileReader(file);
            int singalCh;
            while((singalCh = fileReader.read()) != -1) {
                caseIDBuilder.append((char) singalCh);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        caseID = Integer.parseInt(caseIDBuilder.toString());
        StringBuilder SQLPassword = new StringBuilder();
        try {
            File file = new File("C:\\DiscordServerBotSecrets\\SCP-079\\SQLPassword.txt");
            FileReader fileReader = new FileReader(file);
            int singalCh;
            while((singalCh = fileReader.read()) != -1) {
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
            while((singalCh = fileReader.read()) != -1) {
                endPoint.append((char) singalCh);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        driverName = "com.mysql.cj.jdbc.Driver";
        url = "jdbc:mysql://" + endPoint.toString() + "/mainDB?serverTimezone=UTC";
        url2 = "jdbc:mysql://" + endPoint.toString() + "/ritobotDB?serverTimezone=UTC";
        user = "admin";
        password = SQLPassword.toString();

        try {
            Class.forName(driverName);

            connection = DriverManager.getConnection(url, user, password);
        } catch (ClassNotFoundException | SQLException e) {
            e.printStackTrace();
        }
    }
    public static void SQLupload(String SteamID, String time, String reason, String server, String serverID) {
        caseIDup();

        Date date = new Date();
        SimpleDateFormat dayTime = new SimpleDateFormat("yyyy-MM-dd kk:mm:ss");
        String sanctionTime = dayTime.format(date);
        String queryString = "INSERT INTO Sanction_Information VALUE (\"" + caseID + "\",\""+ SteamID + "\", \"" + sanctionTime + "\", \"" + time + "\", \"" + reason + "\", \"" + server + "\" , \"" + serverID + "\"" + ");";

        System.out.println(queryString);
        try {
            Class.forName(driverName);

            connection = DriverManager.getConnection(url, user, password);
            statement = connection.createStatement();
            statement.executeUpdate(queryString);
            statement.close();
            connection.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
    public static String[] SQLdownload(String SteamID) throws SQLException, ClassNotFoundException {
        String[] data = new String[] {
                null, null, null, null, null,
                null, null, null, null, null
        };

        String queryString = "SELECT * FROM Sanction_Information WHERE SteamID =\"" + SteamID +"\";";
        Class.forName(driverName);

        connection = DriverManager.getConnection(url, user, password);
        statement = connection.createStatement();
        resultSet = statement.executeQuery(queryString);
        int i = 0;
        while (resultSet.next()) {
            data[i] = resultSet.getString("caseID");
            i++;
        }
        statement.close();
        connection.close();
        return data;
    }
    private static void caseIDup() {
        caseID++;
        try {
            String message = String.valueOf(caseID);

            File file = new File("C:\\DiscordServerBotSecrets\\SCP-079\\caseID.txt");
            FileWriter writer;


            writer = new FileWriter(file, false);
            writer.write(message);
            writer.flush();

            writer.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
    public static String[] SQLdownload(int caseID) throws SQLException, ClassNotFoundException {
        String[] data = new String[7];

        String queryString = "SELECT * FROM Sanction_Information WHERE caseID =\"" + caseID +"\";";
        Class.forName(driverName);

        connection = DriverManager.getConnection(url, user, password);
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
        connection.close();
        return data;
    }
    public static String[] GreenDBDownload(String steamID) throws ClassNotFoundException, SQLException {
        String[] data = new String[] {
                null, null, null, null, null,
                null, null, null, null, null
        };

        String queryString = "SELECT * FROM Sanction_Infor WHERE SteamID =\"" + steamID +"\";";
        Class.forName(driverName);

        connection = DriverManager.getConnection(url2, user, password);
        statement = connection.createStatement();
        resultSet = statement.executeQuery(queryString);
        int i = 0;
        while (resultSet.next()) {
            data[i] = resultSet.getString("caseID");
            i++;
        }
        statement.close();
        connection.close();
        return data;
    }
    public static String[] GreenDBDownload(int caseID, String steamID) throws ClassNotFoundException, SQLException {
        String[] data = new String[] {
                null, null, null, null, null,
                null, null, null, null, null
        };

        String queryString = "SELECT * FROM Sanction_Infor WHERE SteamID =\"" + steamID +"\";";
        Class.forName(driverName);

        connection = DriverManager.getConnection(url2, user, password);
        statement = connection.createStatement();
        resultSet = statement.executeQuery(queryString);
        while (resultSet.next()) {
            String a = resultSet.getString("caseID");
            if(caseID == Integer.parseInt(a)) {
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
        connection.close();
        return data;
    }
}
