package tikape.runko.database;

import java.net.URI;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class Database {

    private String databaseAddress;

    public Database(String databaseAddress) throws ClassNotFoundException {
        this.databaseAddress = databaseAddress;
        
        init();
    }
    
    
     public Connection getConnection() throws SQLException {
        if (this.databaseAddress.contains("postgres")) {
            try {
                URI dbUri = new URI(databaseAddress);

                String username = dbUri.getUserInfo().split(":")[0];
                String password = dbUri.getUserInfo().split(":")[1];
                String dbUrl = "jdbc:postgresql://" + dbUri.getHost() + ':' + dbUri.getPort() + dbUri.getPath();

                return DriverManager.getConnection(dbUrl, username, password);
            } catch (Throwable t) {
                System.out.println("Error: " + t.getMessage());
                t.printStackTrace();
            }
        }

        return DriverManager.getConnection(databaseAddress);
    }

    public void init() {
        List<String> lauseet = null;
        if (this.databaseAddress.contains("postgres")) {
            lauseet = postgreLauseet();
        } else {
            lauseet = sqliteLauseet();
        }

        try (Connection conn = getConnection()) {
            Statement st = conn.createStatement();

            for (String lause : lauseet) {
                System.out.println("Running command >> " + lause);
                st.executeUpdate(lause);
            }

        } catch (Throwable t) {
            System.out.println("Error >> " + t.getMessage());
        }
    }
    
    private List<String> postgreLauseet() {
        ArrayList<String> lista = new ArrayList<>();
        
        lista.add("DROP TABLE Aihealue; DROP TABLE Viestiketju; DROP TABLE Viesti;");
        lista.add("CREATE TABLE Aihealue (id SERIAL PRIMARY KEY, aiheenNimi varchar(100));");
        lista.add("CREATE TABLE Viestiketju (id SERIAL PRIMARY KEY, aihealue INTEGER,"
                + "nimi VARCHAR(100), "
                + "FOREIGN KEY (aihealue) REFERENCES Aihealue(id));");
        lista.add("CREATE TABLE Viesti (id SERIAL PRIMARY KEY, viestiketju INTEGER,"
                + "teksti varchar(500), lahettaja VARCHAR(50), lahetysaika TIMESTAMP WITH TIME ZONE NOT NULL,"
                + "otsikko VARCHAR(100), FOREIGN KEY (viestiketju) REFERENCES Viestiketju(id));");

        return lista;
    }

    private List<String> sqliteLauseet() {
        ArrayList<String> lista = new ArrayList<>();

        lista.add("DROP TABLE Aihealue; DROP TABLE Viestiketju; DROP TABLE Viesti;");
        lista.add("CREATE TABLE Aihealue (id INTEGER PRIMARY KEY, aiheenNimi varchar(100));");
        lista.add("CREATE TABLE Viestiketju (id INTEGER PRIMARY KEY, aihealue INTEGER, "
                + "nimi VARCHAR(100), "
                + "FOREIGN KEY (aihealue) REFERENCES Aihealue(id));");
        lista.add("CREATE TABLE Viesti (id INTEGER PRIMARY KEY, viestiketju INTEGER,"
                + "teksti varchar(500), lahettaja VARCHAR(50), lahetysaika DATE DEFAULT(datetime('now', 'localtime')),"
                + "otsikko VARCHAR(100), FOREIGN KEY (viestiketju) REFERENCES Viestiketju(id));");


        return lista;
    }
}
