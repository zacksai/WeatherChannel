import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.util.Scanner;

public class Main {

    public static void main(String[] args) {

        // Step 1 - choose an address of a location and find its latitude and longitude
        // You can do this manually in Google Maps (or if you're up for a challenge, you could
        // investigate API's to do this!)
        // Either way, we need to set the latitude and longitude variables below:

        // Example latitude and longitude for MiraCosta College


        String latitude = "33.1902";
        String longitude = "-117.3021";
        System.out.println("STEP 1:\n Latitude: " + latitude + "\nLongitude: " + longitude + "\n\n");


        // ------------------------------------------------------------------------------

        // Step 2 - Call the National Weather Service API for getting the weather station for a location
        // Review the response data (you can print the entire response.body() string out to the console,
        // or use Postman to test the API before programming!
        // You need to parse out the "forecast" element from the response - this is the URL you need to call in Step 3.
        // NOTE: This step has been done for you, please use it as a reference for completing the additional steps

        System.out.println("STEP 2:\n");

        String requestURL = "https://api.weather.gov/points/" + latitude + "," + longitude;
        HttpResponse<String> response = invokeGET(requestURL);

        String body = response.body();
        String[] lines = splitLines(body);
        String forecastURL = null;

        for(int i = 0; i < lines.length; i++)
        {
            if(lines[i].contains("\"forecast\":"))
            {
                forecastURL = getJSONValueFromLine(lines[i]);

                System.out.println(lines[i]);
            }
        }

        // ------------------------------------------------------------------------------

        // Step 3 - Call the National Weather Service API for getting the forecast
        // Using the API URL you got from Step 2, get the forecast, and locate and parse out the current temperature


        // TO-DO: Implement step 3


        System.out.println("\n\nSTEP 3\n");
        response = invokeGET(forecastURL);
        body = response.body();
        lines = splitLines(body);

        for(int i = 0; i < lines.length; i++)
        {
            if(lines[i].contains("\"temperature\":"))
            {
                System.out.println(lines[i]);
                break;
            }
        }

        // ------------------------------------------------------------------------------

        // Step 4 - Call the National Weather Service API for getting the forecast
        // Using the API URL you got from Step 2, get the forecast, and locate and parse out the current temperature

        // Print out the temperature for the user, along with recommended clothing based on the temperature:
        // 75 and up: Looks like shorts and t-shirt weather!
        // 60 to 74: Might want to grab a sweatshirt
        // 59 and below: Time for snow pants!  Or just stay inside and drink hot chocolate...


        // If you have a different tolerance level of certain temperatures, please feel free to create your own recommendations =)

        // TO-DO: Implement step 4


        System.out.println("\n\nSTEP 4\n");

        // Store temperature as an int
        int temperature = 0;

        for(int i = 0; i < lines.length; i++)
        {
            if( lines[i].contains("\"temperature\":") )
            {
                System.out.println(lines[i]);

                // Parse temperature from the line
                temperature = Integer.parseInt(lines[i].substring(lines[i].indexOf(':')+2, lines[i].length()-1));
                System.out.println(temperature);
                break;
            }
        }

        // Print appropriate message
        if (temperature > 77) System.out.println("Classic San Diego weather! T shirt & shorts for a beach day is in order!");
        else if (temperature > 60) System.out.println("Might need a nice cardigan or flannel -- just 1 layer should do!");
        else System.out.println("The weather outside is frightful... but hot cocoa from BevMobil sounds so delightful!\n Wear your snow pants!");


    }

    /**
     * Takes the URL of a simple GET web service endpoint,
     * calls the web service, and then returns an
     * HttpResponse<String> object from the web service response.
     *
     * @param  requestURL  the web service URL to call
     * @return      the web service response
     */
    public static HttpResponse<String> invokeGET(String requestURL) {

        // Build HttpClient for making web service calls
        HttpClient client = HttpClient.newBuilder()
                // configure the HttpClient so that it will follow any web redirects
                .followRedirects(HttpClient.Redirect.ALWAYS)
                .build();

        // Create our web service request object
        HttpRequest request = HttpRequest.newBuilder()
                // set the URL using the from the method input parameter
                .uri(URI.create(requestURL))
                // configure the request to call the GET method of the web service
                .GET()
                .build();

        HttpResponse<String> response = null;

        try {
            // Attempt to call the web service
            response = client.send(request, HttpResponse.BodyHandlers.ofString());
            // If successful, the web service should return a "200 OK" status code
            if(response.statusCode() == 200) {
                //System.out.println("Hey it worked!");
            }
        } catch(Exception e) {
            System.out.println("Well that could have gone better...");
            System.out.println(e.toString());
        }

        return response;
    }

    /**
     * Takes a string of text (in this case, presumably our web service JSON response)
     * and splits each individual line into an array of Strings, which we can use to
     * loop through the lines looking for data elements we are interested in.
     *
     * @param  inputText  the String of text to split into separate lines
     * @return            the text split into separate lines as an array of Strings
     */
    public static String[] splitLines(String inputText) {

        return inputText.split("\\r?\\n");

    }

    /**
     * Takes a line of text representing a single JSON property in the format of
     * a key-value pair and then parses and returns the value, for example:
     *  INPUT: "forecast": "https://api.weather.gov/gridpoints/SGX/54,34/forecast",
     *  OUTPUT: https://api.weather.gov/gridpoints/SGX/54,34/forecast
     *
     * @param  inputLine  the String of text to split into separate lines
     * @return            the text split into separate lines as an array of Strings
     */
    public static String getJSONValueFromLine(String inputLine) {
        // First split the line on the ":"
        // left of the : will be the JSON attribute name, and to the right will be the value
        //String[] tokens = inputLine.split(":");
        String[] tokens = inputLine.replaceAll("^\"", "").split("\"?(,|:|$)(?=(([^\"]*\"){2})*[^\"]*$) *\"?");
        System.out.println("JSON Attribute Name: " + tokens[0]);
        System.out.println("JSON Attribute Value: " + tokens[1]);

        return tokens[1];
    }
}
