package com.gamescrafters.connect4;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.view.View;
import android.view.WindowManager;
import android.view.View.OnClickListener;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.Spinner;
import android.widget.TextView;

import com.gamescrafters.gamesmanmobile.R;

public class Connect4Options extends Activity {
	Spinner select_rows;
	Spinner select_columns;
	EditText select_time;
	RadioButton comp1;
	RadioButton comp2;
	TextView time;

	/** Called when the activity is first created. */
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
	    getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,WindowManager.LayoutParams.FLAG_FULLSCREEN);

		setContentView(R.layout.connect4_options);
		
		select_columns = (Spinner) findViewById(R.id.c4_select_columns);
		ArrayAdapter<CharSequence> column_a = ArrayAdapter.createFromResource(this, R.array.Connect4_Columns, android.R.layout.simple_spinner_item);
		column_a.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
		select_columns.setAdapter(column_a);
		select_columns.setSelection(6); // default to 7 columns
		select_rows = (Spinner) findViewById(R.id.c4_select_rows);
		ArrayAdapter<CharSequence> row_a = ArrayAdapter.createFromResource(this, R.array.Connect4_Rows, android.R.layout.simple_spinner_item);
		row_a.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
		select_rows.setAdapter(row_a);
		select_rows.setSelection(5); // default to 6 rows
		
		select_time = (EditText) findViewById(R.id.c4_select_time);
		time = (TextView) findViewById(R.id.TextView01);
		comp1 = (RadioButton) findViewById(R.id.c4_RadioButtonComputer1);
		comp2 = (RadioButton) findViewById(R.id.c4_RadioButtonComputer2);
		
		changeVisibility(false);
		
		OnClickListener mylistener = new OnClickListener() {
			public void onClick(View v) {
				Intent myIntent = new Intent(Connect4Options.this, Connect4.class); // used to launch your new activity

				// used to pass data to the Connect4 Activity class.
				myIntent.putExtra("isPlayer1Computer", comp1.isChecked()); 
				myIntent.putExtra("isPlayer2Computer", comp2.isChecked());
				myIntent.putExtra("numRows", select_rows.getSelectedItemPosition()+1);
				myIntent.putExtra("numCols", select_columns.getSelectedItemPosition()+1);
				myIntent.putExtra("numDelay", select_time.getText().toString());

				Connect4Options.this.startActivity(myIntent);
			}
		};
		
		OnClickListener listener = new OnClickListener() {
			public void onClick(View v) {
				boolean c1, c2;
				c1 = comp1.isChecked();
				c2 = comp2.isChecked();
				
				if (c1 && c2) {
					changeVisibility(true);
				} else {
					changeVisibility(false);
				}
			}
		};

		findViewById(R.id.c4_PlayConnect4Button).setOnClickListener(mylistener);
		findViewById(R.id.c4_RadioButtonComputer1).setOnClickListener(listener);
		findViewById(R.id.c4_RadioButtonComputer2).setOnClickListener(listener);
		findViewById(R.id.c4_RadioButtonHuman1).setOnClickListener(listener);
		findViewById(R.id.c4_RadioButtonHuman2).setOnClickListener(listener);
	}
	
	private void changeVisibility(boolean v) {
		int invis, vis;
		invis = View.INVISIBLE;
		vis = View.VISIBLE;
		
		if (v) {
			time.setVisibility(vis);
			select_time.setVisibility(vis);
		} else {
			time.setVisibility(invis);
			select_time.setVisibility(invis);
		}
	}
	
}
