import java.io.*;

import java.net.*;
import org.json.simple.*;
import org.json.simple.parser.*;
import java.util.ArrayList;

public class WeatherForecastServer3Buttons {
	public static int cityCode;
	
	public static void main(String[] args) {
		try {
			ServerSocket serverSocket = new ServerSocket(5008);
			while(true) {
				Socket socket = serverSocket.accept();
				System.out.println("server ready");
				BufferedReader reader = new BufferedReader(new InputStreamReader(socket.getInputStream()));
	            String s = reader.readLine();
	            System.out.println(s);
	            reader.close();
	            
				PrintWriter writer =  new PrintWriter(socket.getOutputStream());
				
				JSONObject payload = getWeatherForecast(s);
				writer.print(payload);
				writer.close();
				System.out.println("payload was sent");

			}
		} catch (Exception e) {
			System.out.println(e);
		}
	}
	
	private static JSONObject getWeatherForecast(String cityName) {
		JSONObject weatherData = new JSONObject();
		
		if(cityName == "kobe") cityCode = 280010;
		if(cityName == "osaka") cityCode = 270000;
		if(cityName == "tokyo") cityCode = 130010;
		
		try {
			String apiUrl = "https://weather.tsukumijima.net/api/forecast/city/" + cityCode;
			final URL url = new URL(apiUrl);
			HttpURLConnection connection = (HttpURLConnection) url.openConnection();
            connection.setRequestMethod("GET");

            BufferedReader reader = new BufferedReader(new InputStreamReader(connection.getInputStream()));
            StringBuilder response = new StringBuilder();
            String s;
            while ((s = reader.readLine()) != null) {
                response.append(s);
            }
            reader.close();

            JSONParser parser = new JSONParser();
            System.out.println(response);
            weatherData = (JSONObject) parser.parse(response.toString());

//            String title = (String) weatherData.get("title"); 
//            System.out.println("Title: " + title);
//            
//            JSONArray forecasts = (JSONArray) weatherData.get("forecasts"); 
//            JSONObject firstForecast = (JSONObject) forecasts.get(0); 
//            JSONObject detail = (JSONObject) firstForecast.get("detail"); 
//            String weather = (String) detail.get("weather"); 
//            System.out.println("Weather: " + weather);
                                    
		} catch (MalformedURLException e) {
			System.out.println("MalformedURLException");
			e.printStackTrace();
		} catch (IOException e) {
			System.out.println("IOException");
            e.printStackTrace();
		} catch (ParseException e) {
			System.out.println("ParseException");
            e.printStackTrace();
        }
		return weatherData;
	}
	
}
