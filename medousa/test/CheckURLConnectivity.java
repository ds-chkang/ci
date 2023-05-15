package medousa.test;

import java.net.HttpURLConnection;
import java.net.URL;

public class CheckURLConnectivity {
    public static void main(String[] args) {
        boolean urlOk = false;
        try {
            // Let medousa know if this url is connectable using the URL below.
            // Then let medousa check if there is a newer version at github to install when it starts each time.
            // If there is a newer version, then download it from github and istall it locally.
            // If the url is not connectable, then give up download the newer version of medousa.
            URL url = new URL("https://github.com/medousa-net/medousa/blob/binary/README.md");
            HttpURLConnection connection = (HttpURLConnection) url.openConnection();
            connection.setRequestMethod("HEAD");
            int responseCode = connection.getResponseCode();
            if (responseCode == HttpURLConnection.HTTP_OK) {
                urlOk = true;
            }
        } catch (Exception e) {
            System.out.println("Error: " + e.getMessage());
        } finally {
            if (urlOk) {
                System.out.println("URL is connectable.");
            } else {
                System.out.println("URL is not connectable.");
            }
        }
    }
}