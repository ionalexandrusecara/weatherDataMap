/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package WeatherData;

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

public class WeatherData {

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
    static JsonReader JReader;
    static JsonObject jsonObject;
    static JsonObject cityJsonObject;
    static JsonArray listJsonObject;
    static JsonObject coordJsonObject;
    static JsonArray weatherJsonObject;
    static JsonObject mainJsonObject;
    static JsonObject windJsonObject;
    static JsonObject cloudsJsonObject;
    static JsonObject rainJsonObject;
    static JsonObject sysJsonObject;
    static double maxTemp;
    static String maxTempTime;
    static double minTemp;
    static String minTempTime;
    static double windSpeed = 0;
    static int day;
    static double maxAverageWind = 0;
    static int windiestDay;
    static double[] averageWinds = new double[7];
    static double[] noRecords = new double[7];

    public static void main(String[] args) {
        WeatherData client = new WeatherData();
        String cityName = null;
        String countryCode = null;
        String xCoord, yCoord;
        int choice = 0;

        try {
            if (cityOrCoord()) {
                //Request data by city
                if (infoType()) {
                    //Request current weather
                    cityName = getCity();
                    result = client.makeRESTCall("http://api.openweathermap.org/data/2.5/weather?q=" + cityName + "&appid=8ab144a3ee970ae28957dcfd69a59ed6");
                    System.out.println("\nResponse from Server...");
                    //System.out.println(result);
                    choice = 1;
                } else {
                    //Request 5-day weather
                    cityName = getCity();
                    countryCode = getCountryCode();
                    result = client.makeRESTCall("http://api.openweathermap.org/data/2.5/forecast?q=" + cityName + "," + countryCode + "&appid=8ab144a3ee970ae28957dcfd69a59ed6");
                    System.out.println("\nResponse from Server...");
                    //System.out.println(result);
                }
            } else if (infoType()) {
                //Request current weather
                xCoord = getXCoord();
                yCoord = getYCoord();
                result = client.makeRESTCall("http://api.openweathermap.org/data/2.5/weather?lat=" + xCoord + "&lon=" + yCoord + "&appid=8ab144a3ee970ae28957dcfd69a59ed6");
                System.out.println("\nResponse from Server...");
                //System.out.println(result);
                choice = 1;
            } else {
                //Request 5-day weather
                xCoord = getXCoord();
                yCoord = getYCoord();
                result = client.makeRESTCall("http://api.openweathermap.org/data/2.5/forecast?lat=" + xCoord + "&lon=" + yCoord + "&appid=8ab144a3ee970ae28957dcfd69a59ed6");
                System.out.println("\nResponse from Server...");
                //System.out.println(result);
            }

            //Start printing information to user
            if (choice == 1) {
                //Initializing Json Objects
                initializeJsonObject();
                initializeJsonCurrentWeather();
                System.out.println(getCityName());
                System.out.println(getCountryName());
                System.out.println(getTime());
                System.out.println(getStatus());
                System.out.println(getTemperature());
                System.out.println(getPressure());
                System.out.println(getHumidity());
                System.out.println(getWindSpeed());
                System.out.println(getWindDirection());
                System.out.println(getCloudiness());
                System.out.println(getRain());
                System.out.println(getSunriseTime());
                System.out.println(getSunsetTime());
                System.out.println(getMinimumTemperature());
                System.out.println(getMaximumTemperature());
            } else if (choice == 0) {
                //Initializing Json Objects
                initializeJsonObject();
                initializeJson5DayForecast();
                System.out.println(getCityNameForecast());
                System.out.println(getCountryNameForecast());
                System.out.println(getTimeForecast());
                System.out.println(getStatusForecast());
                System.out.println(getTemperatureForecast());
                System.out.println(getPressureForecast());
                System.out.println(getHumidityForecast());
                System.out.println(getWindSpeedForecast());
                System.out.println(getWindDirectionForecast());
                System.out.println(getCloudinessForecast());
                System.out.println(getRainForecast());
                System.out.println(getMinimumTemperatureForecast());
                System.out.println(getMaximumTemperatureForecast());
                day = getDayForecast();
                averageWinds[day] = averageWinds[day] + addToAverageDailyWindSpeedForecast();
                noRecords[day]++;

                //Maximum and minimum temperatures
                maxTemp = Double.parseDouble(getTemperatureForecast().substring(13, getTemperatureForecast().length() - 1));
                maxTempTime = getTimeForecast();
                minTemp = Double.parseDouble(getTemperatureForecast().substring(13, getTemperatureForecast().length() - 1));
                minTempTime = getTimeForecast();
                while (result.indexOf("},{") != -1) {
                    result = result.substring(result.indexOf("},{") + 2);
                    initializeJsonObject();
                    initializeJsonCurrentWeather();
                    System.out.println(getTime());
                    System.out.println(getStatus());
                    System.out.println(getTemperature());
                    System.out.println(getPressure());
                    System.out.println(getHumidity());
                    System.out.println(getWindSpeed());
                    System.out.println(getWindDirection());
                    System.out.println(getCloudiness());
                    System.out.println(getRain());
                    System.out.println(getMinimumTemperature());
                    System.out.println(getMaximumTemperature());

                    //Max and min temperatures
                    if (maxTemp < Double.parseDouble(getTemperature().substring(13, getTemperatureForecast().length() - 1))) {
                        maxTemp = Double.parseDouble(getTemperature().substring(13, getTemperatureForecast().length() - 1));
                        maxTempTime = getTime();
                    }
                    if (minTemp > Double.parseDouble(getTemperature().substring(13, getTemperatureForecast().length() - 1))) {
                        minTemp = Double.parseDouble(getTemperature().substring(13, getTemperatureForecast().length() - 1));
                        minTempTime = getTime();
                    }
                    //Windiest day
                    day = getDay();
                    averageWinds[day] = averageWinds[day] + addToAverageDailyWindSpeed();
                    noRecords[day]++;
                }
                System.out.println();
                System.out.println("--Extensions--");
                System.out.println();
                System.out.println("Maximum and minimum temperatures on 5-day forecast");
                System.out.println("Maximum temperature during 5 days: " + maxTemp);
                System.out.println(maxTempTime);

                System.out.println("Minimum temperature during 5 days: " + minTemp);
                System.out.println(minTempTime);

                System.out.println();
                System.out.println("Average Windiest Day");

                for (int i = 0; i < averageWinds.length; i++) {
                    averageWinds[i] = averageWinds[i] / noRecords[i];
                    if (maxAverageWind < averageWinds[i]) {
                        maxAverageWind = averageWinds[i];
                        windiestDay = i;
                    }
                }
                System.out.println("Maximum average wind: " + maxAverageWind);
                System.out.print("Day of highest wind speed average: ");
                switch (windiestDay) {
                    case 0:
                        System.out.println("Sunday");
                        break;
                    case 1:
                        System.out.println("Monday");
                        break;
                    case 2:
                        System.out.println("Tuesday");
                        break;
                    case 3:
                        System.out.println("Wednesday");
                        break;
                    case 4:
                        System.out.println("Thursday");
                        break;
                    case 5:
                        System.out.println("Friday");
                        break;
                    case 6:
                        System.out.println("Saturday");
                        break;
                    default:
                        break;
                }
            }
        } catch (MalformedURLException e) {
            System.out.println("Problem encountered with the URL");
            e.printStackTrace();
        } catch (IOException e) {
            System.out.println("Problem encountered during communication");
            e.printStackTrace();
        }

    }

    public static void initializeJsonObject() {
        try {
            JReader = Json.createReader(new StringReader(result));
            jsonObject = JReader.readObject();
        } catch (Exception e) {
            System.out.println("Issue raised: " + e.getMessage());
            System.exit(1);
        }
    }

    public static void initializeJsonCurrentWeather() {
        try {
            coordJsonObject = jsonObject.getJsonObject("coord");
            weatherJsonObject = jsonObject.getJsonArray("weather");
            mainJsonObject = jsonObject.getJsonObject("main");
            //System.out.println("--------------" + mainJsonObject);
            windJsonObject = jsonObject.getJsonObject("wind");
            cloudsJsonObject = jsonObject.getJsonObject("clouds");
            rainJsonObject = jsonObject.getJsonObject("rain");
            sysJsonObject = jsonObject.getJsonObject("sys");
        } catch (Exception e) {
            System.out.println("Issue raised: " + e.getMessage());
            System.exit(1);
        }
    }

    public static void initializeJson5DayForecast() {
        try {
            cityJsonObject = jsonObject.getJsonObject("city");
            listJsonObject = jsonObject.getJsonArray("list");
            coordJsonObject = jsonObject.getJsonObject("coord");
            JsonObject temp;
            temp = listJsonObject.getJsonObject(0);
            weatherJsonObject = temp.getJsonArray("weather");
            mainJsonObject = temp.getJsonObject("main");
            windJsonObject = temp.getJsonObject("wind");
            cloudsJsonObject = temp.getJsonObject("clouds");
            rainJsonObject = temp.getJsonObject("rain");
            sysJsonObject = jsonObject.getJsonObject("sys");
        } catch (Exception e) {
            System.out.println("Issue raised: " + e.getMessage());
            System.exit(1);
        }
    }

    public static boolean cityOrCoord() {
        Scanner in = new Scanner(System.in);
        String option = "0";
        while (!(option.equals("1") || option.equals("2"))) {
            System.out.print("Choose an option (1: City Name; 2: Geographical Coordinates): ");
            option = in.next();
            System.out.println();
        }
        if (option.equals("1")) {
            return true;
        }
        return false;
    }

    public static boolean infoType() {
        Scanner in = new Scanner(System.in);
        String option = "0";
        while (!(option.equals("1") || option.equals("2"))) {
            System.out.print("Choose an option (1: Current Weather; 2: 5-day Forecast): ");
            option = in.next();
            System.out.println();
        }
        if (option.equals("1")) {
            return true;
        }
        return false;
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

    public static String getCountryCode() {
        Scanner in = new Scanner(System.in);
        String countryCode = null;
        while (countryCode == null || countryCode.length() != 2) {
            System.out.print("Please type the country code: ");
            countryCode = in.next();
            System.out.println();
        }
        return countryCode;
    }

    public static String getXCoord() {
        Scanner in = new Scanner(System.in);
        String xCoord = null;
        while (xCoord == null) {
            System.out.print("Please type the x coordinate (latitude): ");
            xCoord = in.next();
            System.out.println();
        }
        return xCoord;
    }

    public static String getYCoord() {
        Scanner in = new Scanner(System.in);
        String yCoord = null;
        while (yCoord == null) {
            System.out.print("Please type the y coordinate (longitude): ");
            yCoord = in.next();
            System.out.println();
        }
        return yCoord;
    }

    public static String getCityName() {
        try {
            return "Weather Report for city: " + jsonObject.getString("name");
        } catch (Exception e) {
            return "No city name available";
        }
    }

    public static String getCountryName() {
        try {
            return "Country: " + sysJsonObject.getString("country");
        } catch (Exception e) {
            return "No country name available";
        }
    }

    public static String getTime() {
        try {
            DateFormat dateFormat = new SimpleDateFormat("yyyy/MM/dd HH:mm:ss");
            Date date = new Date((long) jsonObject.getJsonNumber("dt").longValue() * 1000);
            return "Reported at " + dateFormat.format(date);
        } catch (Exception e) {
            return "No date available";
        }
    }

    public static int getDay() {
        DateFormat dateFormat = new SimpleDateFormat("yyyy/MM/dd HH:mm:ss");
        Date date = new Date((long) jsonObject.getJsonNumber("dt").longValue() * 1000);
        return date.getDay();
    }

    public static String getStatus() {
        try {
            JsonObject temp;
            String descr;
            temp = weatherJsonObject.getJsonObject(0);
            descr = temp.getString("description");
            return "Status: " + descr;
        } catch (Exception e) {
            return "No status available";
        }
    }

    public static String getTemperature() { //Rounded to two decimal places
        try {
            return "Temperature: " + (Math.round((mainJsonObject.getJsonNumber("temp").doubleValue() - 273.15) * 100.0) / 100.0) + "C";
        } catch (Exception e) {
            return "No temperature available";
        }
    }

    public static String getPressure() {
        try {
            return "Pressure: " + mainJsonObject.getJsonNumber("pressure") + " hPA";
        } catch (Exception e) {
            return "No pressure available";
        }
    }

    public static String getHumidity() {
        try {
            return "Humidity: " + mainJsonObject.getJsonNumber("humidity") + "%";
        } catch (Exception e) {
            return "No humidity available";
        }
    }

    public static String getWindSpeed() {
        try {
            return "Wind Speed: " + windJsonObject.getJsonNumber("speed") + " metres/second";
        } catch (Exception e) {
            return "No wind speed available";
        }
    }

    public static double addToAverageDailyWindSpeed() {
        try {
            return windJsonObject.getJsonNumber("speed").doubleValue();
        } catch (Exception e) {
            return 0.0;
        }
    }

    public static String getWindDirection() {
        try {
            return "Wind Direction: " + windJsonObject.getJsonNumber("deg") + " degrees";
        } catch (Exception e) {
            return "No wind direction available";
        }
    }

    public static String getCloudiness() {
        try {
            return "Cloudiness: " + cloudsJsonObject.getJsonNumber("all") + "%";
        } catch (Exception e) {
            return "No cloud information available";
        }
    }

    public static String getRain() {
        try {
            return "Rain: " + rainJsonObject.getJsonNumber("3h") + " inches in the last 3 hours";
        } catch (Exception e) {
            return "Rain: No rain in the last 3 hours";
        }
    }

    public static String getSunriseTime() {
        try {
            DateFormat dateFormat = new SimpleDateFormat("yyyy/MM/dd HH:mm:ss");
            Date date = new Date((long) sysJsonObject.getJsonNumber("sunrise").longValue() * 1000);
            return "Sunrise Time: " + dateFormat.format(date);
        } catch (Exception e) {
            return "No sunrise time available";
        }
    }

    public static String getSunsetTime() {
        try {
            DateFormat dateFormat = new SimpleDateFormat("yyyy/MM/dd HH:mm:ss");
            Date date = new Date((long) sysJsonObject.getJsonNumber("sunset").longValue() * 1000);
            return "Sunset Time: " + dateFormat.format(date);
        } catch (Exception e) {
            return "No sunset time available";
        }
    }

    public static String getMinimumTemperature() { //Rounded to two decimal places
        try {
            return "Minimum Temperature: " + (Math.round((mainJsonObject.getJsonNumber("temp_min").doubleValue() - 273.15) * 100.0) / 100.0) + "C";
        } catch (Exception e) {
            return "No minimum temperature available";
        }
    }

    public static String getMaximumTemperature() { //Rounded to two decimal places
        try {
            return "Maximum Temperature: " + (Math.round((mainJsonObject.getJsonNumber("temp_max").doubleValue() - 273.15) * 100.0) / 100.0) + "C";
        } catch (Exception e) {
            return "No maximum temperature available";
        }
    }

    //Forecast
    public static String getCityNameForecast() {
        try {
            return "Weather Report for city: " + cityJsonObject.getString("name");
        } catch (Exception e) {
            return "No city name available";
        }
    }

    public static String getCountryNameForecast() {
        try {
            return "Country: " + cityJsonObject.getString("country");
        } catch (Exception e) {
            return "No country name available";
        }
    }

    public static String getTimeForecast() {
        try {
            DateFormat dateFormat = new SimpleDateFormat("yyyy/MM/dd HH:mm:ss");
            JsonObject temp = listJsonObject.getJsonObject(0);
            Date date = new Date((long) temp.getJsonNumber("dt").longValue() * 1000);
            return "Reported at " + dateFormat.format(date);
        } catch (Exception e) {
            return "No time of forecast available";
        }
    }

    public static int getDayForecast() {
        try {
            DateFormat dateFormat = new SimpleDateFormat("yyyy/MM/dd HH:mm:ss");
            JsonObject temp = listJsonObject.getJsonObject(0);
            Date date = new Date((long) temp.getJsonNumber("dt").longValue() * 1000);
            return date.getDay();
        } catch (Exception e) {
            return 0;
        }
    }

    public static String getStatusForecast() {
        try {
            JsonObject temp = null;
            String descr = "no status";
            temp = weatherJsonObject.getJsonObject(0);
            descr = temp.getString("description");

            return "Status: " + descr;
        } catch (Exception e) {
            return "No status available";
        }
    }

    public static String getTemperatureForecast() { //Rounded to two decimal places
        try {
            return "Temperature: " + (Math.round((mainJsonObject.getJsonNumber("temp").doubleValue() - 273.15) * 100.0) / 100.0) + "C";
        } catch (Exception e) {
            return "No temperature available";
        }
    }

    public static String getPressureForecast() {
        try {
            return "Pressure: " + mainJsonObject.getJsonNumber("pressure") + " hPA";
        } catch (Exception e) {
            return "No pressure available";
        }
    }

    public static String getHumidityForecast() {
        try {
            return "Humidity: " + mainJsonObject.getJsonNumber("humidity") + "%";
        } catch (Exception e) {
            return "No humidity available";
        }
    }

    public static String getWindSpeedForecast() {
        try {
            return "Wind Speed: " + windJsonObject.getJsonNumber("speed") + " metres/second";
        } catch (Exception e) {
            return "No wind speed available";
        }
    }

    public static double addToAverageDailyWindSpeedForecast() {
        try {
            return windJsonObject.getJsonNumber("speed").doubleValue();
        } catch (Exception e) {
            return 0.0;
        }
    }

    public static String getWindDirectionForecast() {
        try {
            return "Wind Direction: " + windJsonObject.getJsonNumber("dir") + " degrees";
        } catch (Exception e) {
            return "No wind direction available";
        }
    }

    public static String getCloudinessForecast() {
        try {
            return "Cloudiness: " + cloudsJsonObject.getJsonNumber("all") + "%";
        } catch (Exception e) {
            return "No cloud information available";
        }
    }

    public static String getRainForecast() {
        try {
            return "Rain: " + rainJsonObject.getJsonNumber("3h") + " inches in the last 3 hours";
        } catch (Exception e) {
            return "Rain: No rain in the last 3 hours";
        }
    }

    public static String getMinimumTemperatureForecast() { //Rounded to two decimal places
        try {
            return "Minimum Temperature: " + (Math.round((mainJsonObject.getJsonNumber("temp_min").doubleValue() - 273.15) * 100.0) / 100.0) + "C";
        } catch (Exception e) {
            return "No minimum temperature available";
        }
    }

    public static String getMaximumTemperatureForecast() { //Rounded to two decimal places
        try {
            return "Maximum Temperature: " + (Math.round((mainJsonObject.getJsonNumber("temp_max").doubleValue() - 273.15) * 100.0) / 100.0) + "C";
        } catch (Exception e) {
            return "No maximum temperature available";
        }
    }

}
