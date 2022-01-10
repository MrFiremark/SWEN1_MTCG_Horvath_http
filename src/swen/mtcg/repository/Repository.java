package swen.mtcg.repository;

import java.io.BufferedReader;

import java.io.FileReader;
import java.io.IOException;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public abstract class Repository {

    String url;
    String user;
    String pw;

    protected Connection getConnection() throws SQLException, IOException {
        readConfig();
        Connection connection = DriverManager.getConnection(
                url,
                user,
                pw
        );

        return connection;
    }

    private void readConfig () throws IOException {
        String row;
        BufferedReader csvReader = new BufferedReader(new FileReader("C:\\Users\\Raffy\\Desktop\\SWEN1_MTCG_Horvath_ http\\src\\DBConfig"));
        while ((row = csvReader.readLine()) != null) {
            String[] data = row.split(";");
            url = data[0];
            user = data[1];
            pw = data[2];
        }
    }
}
