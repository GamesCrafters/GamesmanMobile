package com.gamescrafters.gamesmanmobile;

import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLConnection;

/**
 * Handles the network connection/reading to download String content from URLs.
 */
public class RemoteGameValueService  {
	
	/**
	 * @param urlString The string URL of the address to attempt a connection to.
	 * @return the InputStream used for reading/downloading the contents of a URL.
	 * @throws IOException If the connection fails.
	 */
	private static InputStream OpenHttpConnection(String urlString) throws IOException{
		InputStream in = null;
		int response = -1;

		URL url = new URL(urlString); 
		URLConnection conn = url.openConnection();

		if (!(conn instanceof HttpURLConnection))                     
			throw new IOException("Not an HTTP connection");

		try {
			HttpURLConnection httpConn = (HttpURLConnection) conn;
			httpConn.setAllowUserInteraction(false);
			httpConn.setInstanceFollowRedirects(true);
			httpConn.setRequestMethod("GET");
			httpConn.connect(); 

			response = httpConn.getResponseCode();                 
			if (response == HttpURLConnection.HTTP_OK) {
				in = httpConn.getInputStream();                                 
			}                     
		}
		catch (Exception ex)
		{
			throw new IOException("Error connecting");            
		}
		return in;     
	}
	
	/**
	 * @param URL The string URL to download the contents of.
	 * @return The String contents downloaded from the URL address.
	 */
	static String DownloadText(String URL)
    {
        int BUFFER_SIZE = 2000;
        InputStream in = null;
        try {
            in = OpenHttpConnection(URL);
        } catch (IOException e1) {
            e1.printStackTrace();
            return "";
        }
        
        InputStreamReader isr = new InputStreamReader(in);
        int charRead;
          String str = "";
          char[] inputBuffer = new char[BUFFER_SIZE];          
        try {
            while ((charRead = isr.read(inputBuffer))>0)
            {                    
                //---convert the chars to a String---
                String readString = 
                    String.copyValueOf(inputBuffer, 0, charRead);                    
                str += readString;
                inputBuffer = new char[BUFFER_SIZE];
            }
            in.close();
        } catch (IOException e) {
            e.printStackTrace();
            return "";
        }    
        return str;        
    }
	
	/**
	 * Check for database connectivity.
	 */
	public static boolean isInternetAvailable()
	{
		try {
			URL url = new URL("http://nyc.cs.berkeley.edu");
			HttpURLConnection urlc = (HttpURLConnection)url.openConnection();
			urlc.setRequestProperty("User-Agent", "GamesmanMobile");
			urlc.setRequestProperty("Connection", "close");
			urlc.setConnectTimeout(1000 * 5);
			urlc.connect();
			if (urlc.getResponseCode() == 200)
			{
				return true;
			}
		} catch (MalformedURLException e1) {
			e1.printStackTrace();
		} catch (IOException e2) {
			e2.printStackTrace();
		}
		return false;
	}
}
