/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package weatherdataxml;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.IOException;
import java.io.StringReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Scanner;
import javax.json.Json;
import javax.json.JsonArray;
import javax.json.JsonObject;
import javax.json.JsonReader;
import java.io.File;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.DocumentBuilder;
import org.w3c.dom.Document;
import org.w3c.dom.NodeList;
import org.w3c.dom.Node;
import org.w3c.dom.Element;
import org.xml.sax.InputSource;

public class WeatherDataXML {

    /**
     * @param args the command line arguments
     */
    public String makeRESTCall(String strURL) throws MalformedURLException, IOException {
        URL url = new URL(strURL);

        HttpURLConnection conn = (HttpURLConnection) url.openConnection();
        //Specify that we are expecting JSON data to be returned
        conn.setRequestMethod("GET");
        conn.setRequestProperty("Accept", "application/json");

        //200 is the 'OK' response code. This method may also return 401 for an unauthorised request, or -1 if the response is not valid HTTP
        if (conn.getResponseCode() != 200) {
            throw new RuntimeException("Failed : HTTP error code : " + conn.getResponseCode());
        }

        //Create reader to read response from the server
        BufferedReader br = new BufferedReader(new InputStreamReader((conn.getInputStream())));

        //Using a StringBuilder is more time and memory efficient, when the size of the concatenated String could be very large
        StringBuilder buffer = new StringBuilder();
        String output;
        while ((output = br.readLine()) != null) {
            buffer.append(output);
        }
        conn.disconnect();

        return buffer.toString();
    }

    static String result = null;

    static Node currentNode;
    static Element currentElement;
    static Node cityNode;
    static Element cityElement;
    static Node countryNode;
    static Element countryElement;
    static Node sunNode;
    static Element sunriseElement;
    static Element sunsetElement;
    static Node temperatureNode;
    static Element temperatureElement;
    static Node humidityNode;
    static Element humidityElement;
    static Node pressureNode;
    static Element pressureElement;
    static Node windSpeedNode;
    static Element windSpeedElement;
    static Node windDirectionNode;
    static Element windDirectionElement;
    static Node cloudsNode;
    static Element cloudsElement;
    static Node precipitationNode;
    static Element precipitationElement;
    static Node weatherNode;
    static Element weatherElement;
    static Node lastUpdateNode;
    static Element lastUpdateElement;
    static Document document;

    public static void main(String[] args) throws Exception {
        WeatherDataXML client = new WeatherDataXML();
        String cityName = null;
        String countryCode = null;
        String xCoord, yCoord;
        int choice = 0;

        try {
            //Request data by city
            //Request current weather
            cityName = getCity();
            result = client.makeRESTCall("http://api.openweathermap.org/data/2.5/weather?q=" + cityName + "&mode=xml&appid=8ab144a3ee970ae28957dcfd69a59ed6");
            System.out.println("\nResponse from Server...");
            //System.out.println(result);

            loadXMLFromString();
            getCityName();
            getCountry();
            getSunriseSunsetTime();
            getTemperature();
            getHumidity();
            getPressure();
            getWindSpeed();
            getWindDirection();
            getClouds();
            getPrecipitation();
            getWeather();
            getLastUpdateTime();
        } catch (MalformedURLException e) {
            System.out.println("Problem encountered with the URL");
            e.printStackTrace();
        } catch (IOException e) {
            System.out.println("Problem encountered during communication");
            e.printStackTrace();
        }

    }

    public static void loadXMLFromString() throws Exception {
        try {
            DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
            DocumentBuilder builder;
            builder = factory.newDocumentBuilder();
            StringReader sr = new StringReader(result);
            InputSource is = new InputSource(sr);
            document = builder.parse(is);
        } catch (Exception e) {
            System.out.println("Issue raised: Couldn't load XML");
            System.exit(1);
        }
    }

    public static String getCity() {
        Scanner in = new Scanner(System.in);
        String cityName = null;
        while (cityName == null) {
            System.out.print("Please type the city name: ");
            cityName = in.next();
            System.out.println();
        }
        return cityName;
    }

    public static void getCityName() {
        NodeList nList = document.getElementsByTagName("city");
        for (int temp = 0; temp < nList.getLength(); temp++) {
            cityNode = nList.item(temp);
            cityElement = (Element) cityNode;
            System.out.println("City Name: " + cityElement.getAttribute("name"));
        }
    }

    public static void getCountry() {
        NodeList nList = document.getElementsByTagName("country");
        for (int temp = 0; temp < nList.getLength(); temp++) {
            countryNode = nList.item(temp);
            countryElement = (Element) countryNode;
            System.out.println("Country: " + countryElement.getTextContent());
        }
    }

    public static void getSunriseSunsetTime() {
        NodeList nList = document.getElementsByTagName("sun");
        for (int temp = 0; temp < nList.getLength(); temp++) {
            sunNode = nList.item(temp);
            sunriseElement = (Element) sunNode;
            sunsetElement = (Element) sunNode;
            System.out.println("Sunrise time: " + sunriseElement.getAttribute("rise"));
            System.out.println("Sunset time: " + sunsetElement.getAttribute("set"));
        }
    }

    public static void getTemperature() {
        try {
            NodeList nList = document.getElementsByTagName("temperature");
            for (int temp = 0; temp < nList.getLength(); temp++) {
                temperatureNode = nList.item(temp);
                temperatureElement = (Element) temperatureNode;
                System.out.println("Temperature: " + (Double.parseDouble(temperatureElement.getAttribute("value")) - 273.15));
                System.out.println("Minimum Temperature: " + (Double.parseDouble(temperatureElement.getAttribute("min")) - 273.15));
                System.out.println("Maximum Temperature: " + (Double.parseDouble(temperatureElement.getAttribute("max")) - 273.15));
            }
        } catch (Exception e) {
            System.out.println("No temperature available");
        }
    }

    public static void getHumidity() {
        try {
            NodeList nList = document.getElementsByTagName("humidity");
            for (int temp = 0; temp < nList.getLength(); temp++) {
                humidityNode = nList.item(temp);
                humidityElement = (Element) humidityNode;
                System.out.println("Temperature: " + humidityElement.getAttribute("value") + humidityElement.getAttribute("unit"));
            }
        } catch (Exception e) {
            System.out.println("No humidity available");
        }
    }

    public static void getPressure() {
        try {
            NodeList nList = document.getElementsByTagName("pressure");
            for (int temp = 0; temp < nList.getLength(); temp++) {
                pressureNode = nList.item(temp);
                pressureElement = (Element) pressureNode;
                System.out.println("Temperature: " + pressureElement.getAttribute("value") + pressureElement.getAttribute("unit"));
            }
        } catch (Exception e) {
            System.out.println("No pressure available");
        }
    }

    public static void getWindSpeed() {
        try {
            NodeList nList = document.getElementsByTagName("speed");
            for (int temp = 0; temp < nList.getLength(); temp++) {
                windSpeedNode = nList.item(temp);
                windSpeedElement = (Element) windSpeedNode;
                System.out.println(windSpeedElement.getAttribute("name") + " of wind speed " + windSpeedElement.getAttribute("value"));
            }
        } catch (Exception e) {
            System.out.println("No wind speed available");
        }
    }

    public static void getWindDirection() {
        try {
            NodeList nList = document.getElementsByTagName("direction");
            for (int temp = 0; temp < nList.getLength(); temp++) {
                windDirectionNode = nList.item(temp);
                windDirectionElement = (Element) windDirectionNode;
                System.out.println("Wind Direction: " + windDirectionElement.getAttribute("name"));
            }
        } catch (Exception e) {
            System.out.println("No wind direction available");
        }
    }

    public static void getClouds() {
        try {
            NodeList nList = document.getElementsByTagName("clouds");
            for (int temp = 0; temp < nList.getLength(); temp++) {
                cloudsNode = nList.item(temp);
                cloudsElement = (Element) cloudsNode;
                System.out.println("Clouds: " + cloudsElement.getAttribute("name"));
            }
        } catch (Exception e) {
            System.out.println("No cloud information available");
        }
    }

    public static void getPrecipitation() {
        try {
            NodeList nList = document.getElementsByTagName("precipitation");
            for (int temp = 0; temp < nList.getLength(); temp++) {
                precipitationNode = nList.item(temp);
                precipitationElement = (Element) precipitationNode;
                System.out.println("Precipitation amount " + precipitationElement.getAttribute("value") + " in " + precipitationElement.getAttribute("mode") + " per " + precipitationElement.getAttribute("unit"));
            }
        } catch (Exception e) {
            System.out.println("No precipitation available");
        }
    }

    public static void getWeather() {
        try {
            NodeList nList = document.getElementsByTagName("weather");
            for (int temp = 0; temp < nList.getLength(); temp++) {
                weatherNode = nList.item(temp);
                weatherElement = (Element) weatherNode;
                System.out.println("Status: " + weatherElement.getAttribute("value"));
            }
        } catch (Exception e) {
            System.out.println("No weather information available");
        }
    }

    public static void getLastUpdateTime() {
        try {
            NodeList nList = document.getElementsByTagName("lastupdate");
            for (int temp = 0; temp < nList.getLength(); temp++) {
                lastUpdateNode = nList.item(temp);
                lastUpdateElement = (Element) lastUpdateNode;
                System.out.println("Last updated at: " + lastUpdateElement.getAttribute("value"));
            }
        } catch (Exception e) {
            System.out.println("No time of update available");
        }
    }
}
