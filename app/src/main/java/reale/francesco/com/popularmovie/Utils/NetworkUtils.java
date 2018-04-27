package reale.francesco.com.popularmovie.Utils;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.Uri;

import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.Scanner;

public class NetworkUtils {

    private static final String BASE_URL_MOVIE = "https://api.themoviedb.org/3/movie";
    private static final String KEY_FOR_REQUEST = "?api_key=";
    private static final String KEY_VALUE = "";
    private static final int SUCCESFULL_REQUEST = 200;
    public static URL buildUrl(Boolean isPopular, int page ) {
        URL url = null;
        try {
            if (isPopular)
                url = new URL(BASE_URL_MOVIE +"/popular"+KEY_FOR_REQUEST +KEY_VALUE + "&page=" + String.valueOf(page));
            else
                url = new URL(BASE_URL_MOVIE +"/top_rated"+KEY_FOR_REQUEST+KEY_VALUE+ "&page=" + String.valueOf(page));

        } catch (MalformedURLException e) {
            e.printStackTrace();
            return null;
        }
        return url;
    }

    /**
     * This method returns the entire result from the HTTP response.
     *
     * @param url The URL to fetch the HTTP response from.
     * @return The contents of the HTTP response.
     * @throws IOException Related to network and stream reading
     */
    public static String getResponseFromHttpUrl(URL url) throws IOException {
        String response = null;
        HttpURLConnection urlConnection = (HttpURLConnection) url.openConnection();
        int responseCode = urlConnection.getResponseCode();
        if (responseCode == SUCCESFULL_REQUEST) {
            try {
                InputStream in = urlConnection.getInputStream();

                Scanner scanner = new Scanner(in);
                scanner.useDelimiter("\\A");

                boolean hasInput = scanner.hasNext();
                if (hasInput) {
                    response =  scanner.next();
                }
            } finally {
                urlConnection.disconnect();
            }
        }
        return response;
    }

    public static boolean isOnline(Context context){
        ConnectivityManager connMgr = (ConnectivityManager)
                context.getSystemService(Context.CONNECTIVITY_SERVICE);
        if (connMgr != null) {
            NetworkInfo netInfo = connMgr.getActiveNetworkInfo();
            return
                    (netInfo != null && netInfo.isConnected());
        }
        else
            return false;
    }


}
