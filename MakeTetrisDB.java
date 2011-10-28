import java.sql.*;
import java.io.*;

public class MakeTetrisDB
{
	public static void main(String[] args) throws Exception
	{
		Class.forName("sun.jdbc.odbc.JdbcOdbcDriver");
		String url = "jdbc:odbc:TetrisDataBase";
		Connection con = DriverManager.getConnection(url);
		Statement stmt = con.createStatement();

		System.out.println("Dropping table...");

		/*try
		{
			stmt.executeUpdate("DROP TABLE HighScoreTable");
		}

		catch(Exception e)
		{
			System.out.println("Fail " + e.getMessage());
		}

		System.out.println("Creating HighScoreTable...");
		try
		{
			stmt.executeUpdate("CREATE TABLE HighScoreTable(ID COUNTER, Username TEXT(30) NOT NULL, Password TEXT(30) NOT NULL, Highscore NUMBER)");
		}

		catch(Exception e)
		{
			System.out.println("Fail " + e.getMessage());
		}*/
		
		try{
			stmt.executeUpdate("DROP TABLE highRow");
			stmt.executeUpdate("CREATE TABLE highRow (user STRING, rows INT)");
			
			System.out.println("Table created");
		}
		
		catch(Exception e){
			System.out.println("Fail "+e.getMessage());
		}
		
	}

}