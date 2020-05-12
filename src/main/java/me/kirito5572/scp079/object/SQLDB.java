package me.kirito5572.scp079.object;

import me.kirito5572.scp079.App;

import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.sql.*;
import java.text.SimpleDateFormat;
import java.util.Date;

public class SQLDB {
    private Connection connection;
    private Statement statement;
    private ResultSet resultSet;
    private int caseID;
    private String driverName;
    private String url;
    private String user;
    private String password;

    public SQLDB() {
        //init
        StringBuilder SQLPassword = new StringBuilder();
        StringBuilder endPoint = new StringBuilder();
        File SQLPasswordFile;
        File SQLEndPointFile;
        if(App.getInstance().getOsInfo() == App.windows) {
            SQLPasswordFile = new File("C:\\DiscordServerBotSecrets\\SCP-079\\SQLPassword.txt");
            SQLEndPointFile = new File("C:\\DiscordServerBotSecrets\\SCP-079\\endPoint.txt");
        } else if(App.getInstance().getOsInfo() == App.linux) {
            SQLPasswordFile = new File("root\\SQLPassword.txt");
            SQLEndPointFile = new File("root\\endPoint.txt");
        } else {
            return;
        }
        try {
            FileReader fileReader = new FileReader(SQLPasswordFile);
            int singalCh;
            while ((singalCh = fileReader.read()) != -1) {
                SQLPassword.append((char) singalCh);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        try {
            FileReader fileReader = new FileReader(SQLEndPointFile);
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
            connection = DriverManager.getConnection(url, user, password);
            statement = connection.createStatement();
            resultSet = statement.executeQuery("SELECT * FROM `079_config`.last_caseID");
            if(resultSet.next()) {
                caseID = resultSet.getInt("caseID");
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public void SQLupload(String SteamID, String time, String reason, String server, String serverID) {
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

            statement = connection.createStatement();
            statement.executeUpdate(queryString);
            statement.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public String[] SQLdownload(String SteamID) throws SQLException, ClassNotFoundException {
        String[] data = new String[]{
                null, null, null, null, null,
                null, null, null, null, null
        };

        String queryString = "SELECT * FROM mainDB.Sanction_Information WHERE SteamID =" + SteamID;

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

    private void caseIDup() {
        caseID++;
        try {
            statement = connection.createStatement();
            statement.executeUpdate("UPDATE `079_config`.last_caseID SET caseID=" + caseID);
            statement.close();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public String[] SQLdownload(int caseID) throws SQLException, ClassNotFoundException {
        String[] data = new String[7];

        String queryString = "SELECT * FROM mainDB.Sanction_Information WHERE caseID =" + caseID;

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

    public String[] GreenDBDownload(String steamID) throws ClassNotFoundException, SQLException {
        String[] data = new String[]{
                null, null, null, null, null,
                null, null, null, null, null
        };

        String queryString = "SELECT * FROM ritobotDB.Sanction_Infor WHERE SteamID =" + steamID;

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

    public String[] GreenDBDownload(int caseID, String steamID) throws ClassNotFoundException, SQLException {
        String[] data = new String[]{
                null, null, null, null, null,
                null, null, null, null, null
        };

        String queryString = "SELECT * FROM ritobotDB.Sanction_Infor WHERE SteamID =" + steamID;

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

    public Connection getConnection() {
        return connection;
    }

    public void setConnection(Connection connection) {
        this.connection = connection;
    }

    public void reConnect() {
        try {

            setConnection(DriverManager.getConnection(url, user, password));
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
}
