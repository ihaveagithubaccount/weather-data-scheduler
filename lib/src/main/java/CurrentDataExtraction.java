
import java.io.IOException;

import java.sql.*;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.Scanner;
import java.util.TimeZone;

import org.bitpipeline.lib.owm.WeatherData.WeatherCondition;
import org.json.JSONException;
import org.json.JSONObject;
import org.quartz.Job;
import org.quartz.JobExecutionContext;
import org.quartz.JobExecutionException;


public class CurrentDataExtraction implements Job{
	
	public void execute (JobExecutionContext context) throws JobExecutionException {
		OwmClient client = new OwmClient();

			
				try {
					extractDataForVransko(client);
				} catch (IOException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				} catch (JSONException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			
		}

	static String idMontpellier = "2992166";
	static String idVransko = "3187336";


	public static void extractDataForVransko(OwmClient client) throws IOException, JSONException {

		JSONObject weatherVransko = client.currentWeatherAtLocation(46.2445, 14.9512, 2);
		WeatherData d = new WeatherData(weatherVransko);
		updateDB(d,idVransko,"vranskoempty" );

	}

	public static void updateDB(WeatherData d, String id, String db) {

		
		try{  
			String dateInitial = new java.text.SimpleDateFormat("MM/dd/yyyy HH:mm:ss").format(new java.util.Date (d.getDateTime()*1000));
			Scanner sc = new Scanner(dateInitial);
			String date = sc.next();
			String time = sc.next();
			Class.forName("com.mysql.jdbc.Driver");
			//Connection con=DriverManager.getConnection("jdbc:mysql://localhost:3306/websystique","root","root");
			Connection con=DriverManager.getConnection("jdbc:mysql://zwgaqwfn759tj79r.chr7pe7iynqr.eu-west-1.rds.amazonaws.com:3306/t0i3cmz2i5xsawlh?autoReconnect=true&useSSL=false","nqaswcug1dveh8g6","fedjocik2pbwf9ez");  
			Statement stmt=con.createStatement(); 
			stmt.executeUpdate("INSERT INTO " + db +"(year, month, day, hour, minute, temperature, humidity, sea_level_pressure, precipitation, snowfall, total_cloud_cover, high_cloud_cover, medium_cloud_cover, low_cloud_cover, shortwave_radiation, wind_speed_10, wind_direction_10, wind_speed_80, wind_direction_80, wind_speed_900, wind_direction_900, wind_gust_10)"
							   + "VALUES (17,null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,null );" );
			/*stmt.executeUpdate("INSERT INTO " + db +" (id, dt, clouds, wind_speed, wind_deg, humidity, pressure, temp, rain, snow, precipitation, month, day, year, time)"
					+ "VALUES (" + id +"," + d.getDateTime() + "," + d.getClouds().getAll() + ","+ d.getWindSpeed() + ","+ 
					d.getWindDeg()+ "," + d.getHumidity() + "," + d.getPressure() + "," + d.getTemp() + "," + 
					(d.hasRain() ? 1 : 0 ) + "," + (d.hasSnow() ? 1 : 0) + "," + (d.getPrecipitation()>Integer.MIN_VALUE ? d.getPrecipitation() : 0) +
					"," + date.substring(0, 2) + "," + date.substring(3, 5) + "," + date.substring(6, 10) + "," + "'" + time + "'" + ");" );  */
			
			System.out.println("DB updated");
			con.close();  
		}catch(Exception e){ System.out.println(e);}
	}

}