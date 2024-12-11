import java.io.*;
import java.net.*;

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;

import org.json.simple.*;
import org.json.simple.parser.*;

import java.util.Arrays;

public class WeatherForecastClient extends JFrame implements ActionListener {

	public static void main(String[] args) {
		new WeatherForecastClient();
	}
	
	JTextArea textArea = new JTextArea(5, 20);
	JButton button = new JButton("予報を取得する");
	
	public WeatherForecastClient() {
		setTitle("test");
		
		JScrollPane scrollPane = new JScrollPane(textArea);
		getContentPane().add(scrollPane);
		getContentPane().add(BorderLayout.SOUTH, button);
		button.addActionListener(this);
		
		setDefaultCloseOperation(EXIT_ON_CLOSE);
		setSize(600, 400);
		setVisible(true);
	}
	
	public void actionPerformed(ActionEvent ae) {
		try {
			Socket socket = new Socket("127.0.0.1", 5006);
			BufferedReader reader = new BufferedReader(new InputStreamReader(socket.getInputStream()));
			StringBuilder response = new StringBuilder();
            String s;
            while ((s = reader.readLine()) != null) {
                response.append(s);
            }
            reader.close();
            
            JSONParser parser = new JSONParser();
            System.out.println(response);
            JSONObject weatherData = new JSONObject();
            weatherData = (JSONObject) parser.parse(response.toString());
            System.out.println(weatherData);
            String title = (String) weatherData.get("title"); 
            String[] cityArray = title.split("の");
            String city = cityArray[0];
            System.out.println("City: " + city);
            
            JSONArray forecasts = (JSONArray) weatherData.get("forecasts"); 
            JSONObject firstForecast = (JSONObject) forecasts.get(0); 
            JSONObject detail = (JSONObject) firstForecast.get("detail"); 
            String weather = (String) detail.get("weather"); 
            System.out.println("Weather: " + weather);

			textArea.append("情報源：https://weather.tsukumijima.net/api/forecast/city/280010\r\n場所：" 
			+ city + "\r\n天気予報：『" + weather + "』\r\n");
			
			System.out.println(response);
			reader.close();
			socket.close();
		} catch (Exception e) {
			System.out.println(e);
		}
	}

}
