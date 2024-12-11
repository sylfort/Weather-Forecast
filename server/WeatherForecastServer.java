import java.io.*;

import java.net.*;
import org.json.simple.*;
import org.json.simple.parser.*;
import java.util.ArrayList;

public class WeatherForecastServer {
	
	
	public static void main(String[] args) {
		try {
			ServerSocket serverSocket = new ServerSocket(5006);
			while(true) {
				Socket socket = serverSocket.accept();
				System.out.println("server ready");
				PrintWriter writer =  new PrintWriter(socket.getOutputStream());
				
				JSONObject payload = getWeatherForecast();
				writer.print(payload);
				writer.close();
				System.out.println("payload was sent");
//				serverSocket.close();
			}
		} catch (Exception e) {
			System.out.println(e);
		}
	}
	
	private static JSONObject getWeatherForecast() {
		JSONObject weatherData = new JSONObject();
		ArrayList<String> arr = new ArrayList<>();
		try {
			String apiUrl = "https://weather.tsukumijima.net/api/forecast/city/280010";
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
            System.out.println(weatherData);
            String title = (String) weatherData.get("title"); 
            System.out.println("Title: " + title);
            
            JSONArray forecasts = (JSONArray) weatherData.get("forecasts"); 
            JSONObject firstForecast = (JSONObject) forecasts.get(0); 
            JSONObject detail = (JSONObject) firstForecast.get("detail"); 
            String weather = (String) detail.get("weather"); 
            System.out.println("Weather: " + weather);
            
            arr.add(title);
            arr.add(weather);
                        
		} catch (MalformedURLException e) {
			e.printStackTrace();
		} catch (IOException e) {
            e.printStackTrace();
		} catch (ParseException e) {
            e.printStackTrace();
        }
		return weatherData;
	}
	
}
