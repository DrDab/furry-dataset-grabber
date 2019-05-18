import java.sql.Connection;
import java.sql.DatabaseMetaData;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class SQLUtils
{
	private static Connection sqlConnection = null;
	
	public static void initConnection(String filePath) throws SQLException
	{
		boolean needReinit = false;
		if (sqlConnection == null)
		{
			needReinit = true;
		}
		else
		{
			if (sqlConnection.isClosed())
			{
				needReinit = true;
			}
		}
		if (needReinit) 
		{
			try
			{
				Class.forName("org.sqlite.JDBC");
			}
			catch (ClassNotFoundException cnfe)
			{
				cnfe.printStackTrace();
			}
			
			sqlConnection = DriverManager.getConnection("jdbc:sqlite:" + filePath);
		}
	}
	
	public static void initTables() throws SQLException
	{
		DatabaseMetaData dbm = sqlConnection.getMetaData();
		ResultSet tables = dbm.getTables(null, null, "postdb", null);
		if (!tables.next()) 
		{
			String query = "CREATE TABLE postdb(id INT, tags VARCHAR(255) NULL, artist VARCHAR(255) NULL, rating INT NULL, score INT NULL, sources VARCHAR(255) NULL, created_at BIGINT NULL);";
			PreparedStatement stmt = sqlConnection.prepareStatement(query);
			stmt.execute();
		}
	}
	
	public static void addEntry(int id, String tags, String artist, int rating, int score, String sources, long creationDate) throws SQLException
	{
		String query = "INSERT INTO postdb(id, tags, artist, rating, score, sources, created_at) VALUES (?, ?, ?, ?, ?, ?, ?);";
		PreparedStatement stmt = sqlConnection.prepareStatement(query);
		stmt.setInt(1, id);
		stmt.setString(2, tags);
		stmt.setString(3, artist);
		stmt.setInt(4, rating);
		stmt.setInt(5, score);
		stmt.setString(6, sources);
		stmt.setLong(7, creationDate);
		stmt.execute();
	}

}
