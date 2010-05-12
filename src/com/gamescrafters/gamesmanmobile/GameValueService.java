package com.gamescrafters.gamesmanmobile;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

/**
 * A class that allows games to query the online database for moveValues.
 * @author AlexD
 */
public class GameValueService {
	final static String root_url = "http://nyc.cs.berkeley.edu:8080/gcweb/service/gamesman/";
	final static String getMove_end_url = "/getMoveValue;board="; 
	final static String getNextMove_end_url =  "/getNextMoveValues;board=";
	
	/**
	 * Retrieves the moveValue of the board from the database.
	 * @param board The String representation of the board.
	 * @return A MoveValue object for the current board.
	 */
	static MoveValue getMoveValue(String gameType, String gameName, String board) {
		
		MoveValue value = null;
		try {
			String url = makeURLString(gameType, gameName, board, getMove_end_url);
			String url_content = RemoteGameValueService.DownloadText(url);
			value = getMoveValueFromURL(url_content);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return value;
	}
	
	/**
	 * Retrieves the moveValues of the child boards from the database.
	 * @param board The String representation of the board.
	 * @return An array of MoveValue objects, one for each child of the board.
	 */
	static MoveValue[] getNextMoveValues(String gameType, String gameName, String board) {
		
		MoveValue[] values = null;
		try {
			String url = makeURLString(gameType, gameName, board, getNextMove_end_url);
			String url_content = RemoteGameValueService.DownloadText(url);
			values = getNextMoveValuesFromURL(url_content);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return values;
	}
		
	/**
	 * @param gameType The type of game this is (generally "puzzles").
	 * @param gameName The name of the game.
	 * @param board The string representation of a board.
	 * @return The url to use for the http connection.
	 */
	private static String makeURLString(String gameType, String gameName, String board, String end_url) {
		StringBuffer buf = new StringBuffer();
		buf.append(root_url);
		buf.append(gameType);
		buf.append("/");
		buf.append(gameName);
		buf.append(end_url);
		buf.append(board);
		return buf.toString();
	}

	/**
	 * Returns the MoveValues of this child boards.
	 * @param urlResponse The response received from the server.
	 * @return An array of MoveValue objects, one for each child.
	 * @throws JSONException
	 */
	private static MoveValue[] getNextMoveValuesFromURL(String urlResponse) throws JSONException {
		JSONObject response = new JSONObject(urlResponse);
		JSONArray jarray = response.getJSONArray("response");
		MoveValue[] values = new MoveValue[jarray.length()];
		if (values != null && values.length != 0) {
			for (int i=0; i < values.length; i++){
				values[i] = new MoveValue(jarray.getJSONObject(i));
			}
		}
		return values;
	}
	
	/**
	 * Retrieves the moveValue of the board from the database.
	 * @param urlResponse The response received from the server.
	 * @return An array of MoveValue objects, one for each child.
	 * @throws JSONException
	 */
	private static MoveValue getMoveValueFromURL(String urlResponse) throws JSONException {
		JSONObject response = new JSONObject(urlResponse);
		return new MoveValue(response.getJSONObject("response"));
	}
}
