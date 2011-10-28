import java.sql.*;
import java.io.*;

public class saveGame{
	public saveGame()throws Exception{
		Class.forName("sun.jdbc.odbc.JdbcOdbcDriver");
		String url = "jdbc:odbc:TetrisDataBase";
		Connection con = DriverManager.getConnection(url);
		Statement stmt = con.createStatement();
	}
}
