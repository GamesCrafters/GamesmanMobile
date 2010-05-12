package com.gamescrafters.gamesmanmobile;

import org.json.JSONObject;

/**
 * The MoveValue that is returned by getMoveValue. Has accessor/checking methods 
 * for commonly used values (e.g. getValue(), getRemoteness(), getMove(), etc).
 * Also allows queries for arbitrary keys with getIntegerField and getStringField.
 * Essentially just a wrapper for the JSONObject for simpler access to value fields.
 * @author AlexD
 */
public class MoveValue implements Comparable<MoveValue> {
	private JSONObject dict;
	
	/**
	 * Constructs the MoveValue given the JSONObject it originates from.
	 * @param dict The JSONObject to build the MoveValue from.
	 */
	public MoveValue(JSONObject dict) {
		this.dict = dict; 
	}
	
	/**
	 * @return The value of the MoveValue ("win", "lose", or "tie"), or the empty string if none exists.
	 */
	public String getValue() {
		return dict.optString("value");
	}
	
	/**
	 * @return Whether the MoveValue has a value.
	 */
	public boolean hasValue() {
		return dict.has("value");
	}
	
	/**
	 * @return The remoteness of the MoveValue (use to calculate moves until primitive), or 0 if none exists.
	 */
	public int getRemoteness() {
		return dict.optInt("remoteness");
	}

	/**
	 * @return Whether the MoveValue has a remoteness.
	 */
	public boolean hasRemoteness() {
		return dict.has("remoteness");
	}
	
	/**
	 * @return The String move that reached this MoveValue, or the empty string if none exists.
	 * Move is as represented in the DB as a string. You can convert this to an internal representation.
	 */
	public String getMove() {
		return dict.optString("move");
	}
	
	/**
	 * @return The int move that reached this MoveValue, or the empty string if none exists. 0 if none found.
	 * Only use this method if you know your value is in an int representation.
	 */
	public int getIntMove() {
		return dict.optInt("move");
	}
	
	/**
	 * @return Whether the MoveValue has a move.
	 */
	public boolean hasMove() {
		return dict.has("move");
	}
	
	/**
	 * @return The board of the MoveValue, or the empty string if none exists.
	 */
	public String getBoard() {
		return dict.optString("board");
	}
	
	/**
	 * @return Whether the MoveValue has a board.
	 */
	public boolean hasBoard() {
		return dict.has("board");
	}
	
	/**
	 * @return The score of the MoveValue, or 0 if none exists.
	 */
	public int getScore() {
		return dict.optInt("score");
	}
	
	/**
	 * @return Whether the MoveValue has a score.
	 */
	public boolean hasScore() {
		return dict.has("score");
	}
	
	/**
	 * Accessor method for general String fields.
	 * @param key The name of the value to get.
	 * @param default_value The default value to return if not found.
	 * @return The field if found, otherwise the default value. 
	 */
	public String getStringField(String key, String defaultValue) {
		return dict.optString(key, defaultValue);
	}
	
	/**
	 * Accessor method for general int fields.
	 * @param key The name of the value to get.
	 * @param default_value The default value to return if not found.
	 * @return The field if found, otherwise the default value. 
	 */
	public int getIntField(String key, int defaultValue) {
		return dict.optInt(key, defaultValue);
	}
	
	/**
	 * @param field The field or key to check the existence of.
	 * @return Whether or not the field/key exists.
	 */
	public boolean hasField(String field) {
		return dict.has(field);
	}

	public int compareTo(MoveValue other) {
		return new Integer(getRemoteness()).compareTo(other.getRemoteness());
	}
	
	public String toString() {
		return dict.toString();
	}
}
