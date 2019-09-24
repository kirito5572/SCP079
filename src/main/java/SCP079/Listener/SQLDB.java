package SCP079.Listener;

import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.sql.*;
import java.text.SimpleDateFormat;
import java.util.Date;

public class SQLDB {
    private static Statement statement;
    private static ResultSet resultSet;
    private static int caseID;
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
        String driverName = "com.mysql.cj.jdbc.Driver";
        String url = "jdbc:mysql://" + endPoint.toString() + "/mainDB?serverTimezone=UTC";
        String user = "admin";
        String password = SQLPassword.toString();

        try {
            Class.forName(driverName);

            Connection connection = DriverManager.getConnection(url, user, password);
            statement = connection.createStatement();
        } catch (ClassNotFoundException | SQLException e) {
            e.printStackTrace();
        }
    }
    public static void SQLupload(String SteamID, String time, String reason, String server, String serverID) {
        caseIDup();

        Date date = new Date();
        SimpleDateFormat dayTime = new SimpleDateFormat("yyyy-mm-dd hh:mm:ss");
        String sanctionTime = dayTime.format(date);
        String queryString = "INSERT INTO Sanction_Information VALUE (\"" + caseID + "\",\""+ SteamID + "\", \"" + sanctionTime + "\", \"" + time + "\", \"" + reason + "\", \"" + server + "\" , \"" + serverID + "\"" + ");";

        System.out.println(queryString);
        try {
            statement.executeUpdate(queryString);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
    public static String[][] SQLdownload(String SteamID) {
        String[][] data = new String[5][7];

        String queryString = "SELECT * FROM Sanction_Information WHERE SteamID =\"" + SteamID +"\";";

        try {
            resultSet = statement.executeQuery(queryString);
        } catch (SQLException e) {
            e.printStackTrace();
        }
        try {
            int i = 0;
            while (resultSet.next()) {
                data[i][0] = resultSet.getString("caseID");
                data[i][1] = resultSet.getString("SteamID");
                data[i][2] = resultSet.getString("sanctionTime");
                data[i][3] = resultSet.getString("endTime");
                data[i][4] = resultSet.getString("reason");
                data[i][5] = resultSet.getString("sendServer");
                data[i][6] = resultSet.getString("serverID");
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
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

            if (writer != null) writer.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
