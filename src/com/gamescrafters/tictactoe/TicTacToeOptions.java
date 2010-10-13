package com.gamescrafters.tictactoe;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.RadioButton;
import android.widget.Spinner;

import com.gamescrafters.gamesmanmobile.R;

public class TicTacToeOptions extends Activity {
	Spinner select_rows;
	Spinner select_columns;
	Spinner select_time;

	/** Called when the activity is first created. */
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		// Refers to XML file
		setContentView(R.layout.tictactoe_options);
		
		select_columns = (Spinner) findViewById(R.id.ttt_select_columns);
		ArrayAdapter<CharSequence> column_a = ArrayAdapter.createFromResource(this, R.array.Connect4_Columns, android.R.layout.simple_spinner_item);
		column_a.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
		select_columns.setAdapter(column_a);
		// Android is stupid and setSelection(2) = 3.
		select_columns.setSelection(2); // default to 3 columns
		select_rows = (Spinner) findViewById(R.id.ttt_select_rows);
		ArrayAdapter<CharSequence> row_a = ArrayAdapter.createFromResource(this, R.array.Connect4_Rows, android.R.layout.simple_spinner_item);
		row_a.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
		select_rows.setAdapter(row_a);
		// Android is stupid and setSelection(2) = 3.		
		select_rows.setSelection(2); // default to 3 rows
		
		select_time = (Spinner) findViewById(R.id.ttt_select_time);
		ArrayAdapter<CharSequence> time_a = ArrayAdapter.createFromResource(this, R.array.Connect4_Time, android.R.layout.simple_spinner_item);
		time_a.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
		select_time.setAdapter(time_a);
		select_time.setSelection(0);
		OnClickListener mylistener = new OnClickListener() {
			public void onClick(View v) {
				Intent myIntent = new Intent(TicTacToeOptions.this, TicTacToe.class); // used to launch your new activity

				// used to pass data to the Connect4 Activity class.
				myIntent.putExtra("isPlayer1Computer", ((RadioButton) findViewById(R.id.ttt_RadioButtonComputer1)).isChecked()); 
				myIntent.putExtra("isPlayer2Computer", ((RadioButton) findViewById(R.id.ttt_RadioButtonComputer2)).isChecked());
				myIntent.putExtra("numRows", select_rows.getSelectedItemPosition()+1);
				myIntent.putExtra("numCols", select_columns.getSelectedItemPosition()+1);
				myIntent.putExtra("numDelay", select_time.getSelectedItemPosition()+1);

				TicTacToeOptions.this.startActivity(myIntent);
			}
		};

		findViewById(R.id.ttt_PlayConnect4Button).setOnClickListener(mylistener);
	}
}
