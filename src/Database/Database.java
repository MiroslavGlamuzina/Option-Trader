package Database;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;

import tools.VerifyTickValue;
import Nexus.MasterAlgorithm;
import Nexus.OptionTrader;
import Nexus.Tick;

public class Database
{
	public static int CURRENTITTERATIONS;

	public static void sad(String[] args)
	{
		try
		{
			// get connection
			Connection con = DriverManager.getConnection(
					"jdbc:mysql://localhost:3306/optiontrader", "", "");
			// create query
			Statement query = con.createStatement();
			// exe query
			con.createStatement()
					.execute(
							"insert into eurusd values ('inject1', 'inject2', 'inject3')");

			ResultSet res = query.executeQuery("select * from demo");
			// process results
			while (res.next())
			{
				System.out.println(res.getString("colone") + ", "
						+ res.getString("coltwo") + ", "
						+ res.getString("colthree"));
			}

			System.out.println("connectin established");
		} catch (Exception e)
		{
			System.out.println("error");
			e.printStackTrace();
		}

	}

	public static void insertEURUS(String[] s)
	{
		try
		{
			int test = Integer.parseInt(s[1].split(":")[1].trim());
		} catch (Exception e)
		{
			s[1] = "00:00";
		}

		try
		{
			// testing string conversion
			// System.out.println(VerifyTickValue.verifyPrice(s[0]));
			s[0] = VerifyTickValue.verifyPrice(s[0]); // convert to readable
//			System.out.println(s[2]);									// string
			double test = Double.parseDouble(s[0]);
			// BOUNDARY for INSERT -- should add parameter!!!!
			if (test > 10)
			{
				return;
			}
			// get connection
			Connection con = DriverManager.getConnection(
					"jdbc:mysql://localhost:3306/optiontrader", "root", "");
			// create query
			con.createStatement().execute(
					"insert into eurusd values (null,'" + s[0] + "', '" + s[1]
							+ "', '" + s[2] + "', '" + s[3] + "')");
			con.close();
			CURRENTITTERATIONS++;
			if (CURRENTITTERATIONS > 1000)
			{
				CURRENTITTERATIONS = 1000;
			}
			// System.out.println("DataBase:51: Inserted Value into Databse");
		} catch (SQLException | NumberFormatException e)
		{
			// System.out.println("DataBase:53: Error, can not parse Current Price");
			// e.printStackTrace();
			// e.printStackTrace();
		}

	}

	public static void insertTradingHistory(Tick tick)
	{

		try
		{
			// get connection
			Connection con = DriverManager.getConnection(
					"jdbc:mysql://localhost:3306/optiontrader", "root", "");
			// create query
			con.createStatement().execute(
					"insert into history values (null,'"
							+ String.valueOf(tick.price) + "', '"
							+ String.valueOf(tick.endprice) + "', '"
							+ String.valueOf(tick.time) + "', '"
							+ String.valueOf(tick.CallorPut) + "', '"
							+ String.valueOf(tick.strength) + "', '"
							+ String.valueOf(tick.winloss) + "', "
							+ MasterAlgorithm.VERSION + ")");
			con.close();
			// System.out.println("DataBase:51: Inserted Value into Databse");
		} catch (SQLException | NumberFormatException e)
		{
			// System.out.println("DataBase:53: Error, can not parse Current Price");
			// e.printStackTrace();
			// e.printStackTrace();
		}
	}

	public static void UpdateMobileValues()
	{
		try
		{
			String tempBank = OptionTrader.BANKROLL.trim().substring(1).trim();
			// get connection
			Connection con = DriverManager.getConnection(
					"jdbc:mysql://localhost:3306/optiontrader", "root", "");
			// create query
			con.createStatement().execute(
					"UPDATE mobile SET STATUS='"+String.valueOf(OptionTrader.READY)+
					"', TIMETOBUY='"+String.valueOf(OptionTrader.timetobuy.get(OptionTrader.timetobuy.size()-1))+
					"', CURRENTPRICE='"+String.valueOf(OptionTrader.test.get(OptionTrader.test.size()-1))+
					"', BANKROLL='"+String.valueOf(tempBank)+
					"', ALGORITHM='"+String.valueOf(MasterAlgorithm.VERSION_NAME[MasterAlgorithm.VERSION])+
					"', CONSOLE='"+String.valueOf(OptionTrader.CONSOLE)+
					"', INVESTMENTS='"+String.valueOf(OptionTrader.investments.size())+
					"'  WHERE PID=1");
			con.close();
			// System.out.println("DataBase:51: Inserted Value into Databse");			
			//System.out.println("Update mobile table");

		} catch (Exception e)
		{
			//System.out.println("error updating mobile table");
			// System.out.println("DataBase:53: Error, can not parse Current Price");
			// e.printStackTrace();
			// e.printStackTrace();
		}
	}

	public static void queryCurrentVersion(int version)
	{
		
		ArrayList<String> winloss = new ArrayList<String>();

		try
		{
			// get connection
			Connection con = DriverManager.getConnection(
					"jdbc:mysql://localhost:3306/optiontrader", "root", "");

			// create query
			Statement st = con.createStatement();
			String sql = ("SELECT WINLOSS FROM history WHERE VERSION="
					+ version + "");
			ResultSet rs = st.executeQuery(sql);
			while (rs.next())
			{
				java.sql.ResultSetMetaData rsmd = rs.getMetaData();
				int numberOfColumns = rsmd.getColumnCount();
				for (int columnIndex = 1; columnIndex <= numberOfColumns; columnIndex++)
				{
					winloss.add(rs.getString("WINLOSS"));
					//System.out.println(rs.getString("WINLOSS"));
					break;
				}
			}
			int wincount = 0;
			int losscount = 0;
			for (int i = 0; i < winloss.size(); i++)
			{
				if (winloss.get(i).toLowerCase()
						.equals(new String("WIN").toLowerCase()))
				{
					wincount++;
				}
				if (winloss.get(i).toLowerCase()
						.equals(new String("LOSS").toLowerCase()))
				{
					losscount++;
				}
			}
			MasterAlgorithm.VERSION_WIN_COUNT = wincount;
			MasterAlgorithm.VERSION_LOSS_COUNT = losscount;
			MasterAlgorithm.VERSION_WINLOSS_RATIO = (int)((double)wincount/((double)losscount+(double)wincount)*100);
		} catch (SQLException | NumberFormatException e)
		{
			// System.out.println("DataBase:53: Error, can not parse Current Price");
			// e.printStackTrace();
			// e.printStackTrace();
		}

	}

	public static ArrayList<Double> getDBCurentPrice(int length)
	{
		ArrayList<Double> price = new ArrayList<Double>();
		try
		{
			// get connection
			Connection con = DriverManager.getConnection(
					"jdbc:mysql://localhost:3306/optiontrader", "root", "");
			// create query

			Statement st = con.createStatement();
			String sql = ("SELECT CURRENTPRICE FROM eurusd ORDER BY PID DESC LIMIT 1000;");
			ResultSet rs = st.executeQuery(sql);
			// if(rs.next()) {
			// price.add(Double.parseDouble(rs.getString("CURRENTPRICE")));
			// }
			while (rs.next())
			{
				java.sql.ResultSetMetaData rsmd = rs.getMetaData();
				int numberOfColumns = rsmd.getColumnCount();
				for (int columnIndex = 1; columnIndex <= numberOfColumns; columnIndex++)
				{
					price.add(Double.parseDouble(rs.getString("CURRENTPRICE")));
					break;
				}
			}
			con.close();

			// System.out.println("DataBase:85: Fetched Values");
		} catch (SQLException | NumberFormatException e)
		{
			System.out.println("DataBase:86: Error, could not fetch values");
			e.printStackTrace();
			// e.printStackTrace();
		}
		return price;
	}
	public static ArrayList<Integer> getMobileCommads()
	{
		ArrayList<Integer> commands = new ArrayList<Integer>();
		try
		{
			// get connection
			Connection con = DriverManager.getConnection(
					"jdbc:mysql://localhost:3306/optiontrader", "root", "");
			// create query

			Statement st = con.createStatement();
			String sql = ("SELECT COMMAND FROM mobile;");
			ResultSet rs = st.executeQuery(sql);
			
			while (rs.next())
			{
				java.sql.ResultSetMetaData rsmd = rs.getMetaData();
				int numberOfColumns = rsmd.getColumnCount();
				for (int columnIndex = 1; columnIndex <= numberOfColumns; columnIndex++)
				{
					commands.add(Integer.parseInt(rs.getString("COMMAND")));
					break;
				}
			}
			con.close();

			// System.out.println("DataBase:85: Fetched Values");
		} catch (SQLException | NumberFormatException e)
		{
			System.out.println("DataBase:86: Error, could not fetch values");
			e.printStackTrace();
			// e.printStackTrace();
		}
		return commands;
	}
	public static ArrayList<Integer> removeMobileCommands()
	{
		ArrayList<Integer> commands = new ArrayList<Integer>();
		try
		{
			// get connection
			Connection con = DriverManager.getConnection(
					"jdbc:mysql://localhost:3306/optiontrader", "root", "");
			// create query
			con.createStatement().execute("UPDATE mobile SET COMMAND=0 WHERE PID=1");
			con.close();

			// System.out.println("DataBase:85: Fetched Values");
		} catch (SQLException | NumberFormatException e)
		{
			//System.out.println("DataBase:86: Error, could not fetch values");
			e.printStackTrace();
			// e.printStackTrace();
		}
		return commands;
	}
	public static ArrayList<Integer> removeMobileCommandsOffline()
	{
		ArrayList<Integer> commands = new ArrayList<Integer>();
		try
		{
			// get connection
			Connection con = DriverManager.getConnection(
					"jdbc:mysql://localhost:3306/optiontrader", "root", "");
			// create query
			con.createStatement().execute("UPDATE mobile SET COMMAND=99 WHERE PID=1");
			con.close();

			// System.out.println("DataBase:85: Fetched Values");
		} catch (SQLException | NumberFormatException e)
		{
			//System.out.println("DataBase:86: Error, could not fetch values");
			e.printStackTrace();
			// e.printStackTrace();
		}
		return commands;
	}

	public static ArrayList<String> getDBTimeToBuy(int length)
	{
		ArrayList<String> price = new ArrayList<String>();
		try
		{
			// get connection
			Connection con = DriverManager.getConnection(
					"jdbc:mysql://localhost:3306/optiontrader", "root", "");
			// create query

			Statement st = con.createStatement();
			String sql = ("SELECT TIMETOBUY FROM eurusd ORDER BY PID DESC LIMIT 1000;");
			ResultSet rs = st.executeQuery(sql);

			while (rs.next())
			{
				java.sql.ResultSetMetaData rsmd = rs.getMetaData();
				int numberOfColumns = rsmd.getColumnCount();
				for (int columnIndex = 1; columnIndex <= numberOfColumns; columnIndex++)
				{
					price.add(rs.getString("TIMETOBUY"));
					break;
				}
			}
			con.close();

			// System.out.println("DataBase:85: Fetched Values");
		} catch (SQLException | NumberFormatException e)
		{
			System.out.println("DataBase:86: Error, could not fetch values");
			e.printStackTrace();
			// e.printStackTrace();
		}
		return price;
	}

	public static ArrayList<String> getDBSystemTime(int length)
	{
		ArrayList<String> price = new ArrayList<String>();
		try
		{
			// get connection
			Connection con = DriverManager.getConnection(
					"jdbc:mysql://localhost:3306/optiontrader", "root", "");
			// create query

			Statement st = con.createStatement();
			String sql = ("SELECT TIME FROM eurusd ORDER BY PID DESC LIMIT 1000;");
			ResultSet rs = st.executeQuery(sql);

			while (rs.next())
			{
				java.sql.ResultSetMetaData rsmd = rs.getMetaData();
				int numberOfColumns = rsmd.getColumnCount();
				for (int columnIndex = 1; columnIndex <= numberOfColumns; columnIndex++)
				{
					price.add(rs.getString("TIME"));
					break;
				}
			}
			con.close();

			// System.out.println("DataBase:85: Fetched Values");
		} catch (SQLException | NumberFormatException e)
		{
			System.out.println("DataBase:86: Error, could not fetch values");
			e.printStackTrace();
			// e.printStackTrace();
		}
		return price;
	}

}