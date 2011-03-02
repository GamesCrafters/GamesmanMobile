package com.gamescrafters.Y;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.WindowManager;
import android.view.View.OnClickListener;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.Spinner;

import com.gamescrafters.gamesmanmobile.R;

public class menu extends Activity {
	Spinner select_rows;
	Spinner select_columns;
	/** Called when the activity is first created. */
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
	    getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,WindowManager.LayoutParams.FLAG_FULLSCREEN);

		setContentView(R.layout.y_options);
		Button playbutton = (Button) findViewById(R.id.y_playButton);
		
	    Spinner innertrispinner = (Spinner) findViewById(R.id.y_innerSpinner);
	    ArrayAdapter innerspinArrayAdapter = new ArrayAdapter(this, android.R.layout.simple_spinner_item, new String[] { "  3", "  4", "  5" });
	    innerspinArrayAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
	    innertrispinner.setAdapter(innerspinArrayAdapter);
	    innertrispinner.setSelection(0);
	    
	    Spinner outertrispinner = (Spinner) findViewById(R.id.y_outerSpinner);
	    ArrayAdapter outerspinArrayAdapter = new ArrayAdapter(this, android.R.layout.simple_spinner_item, new String[] { "  1", "  2", "  3", "  4", "  5" });
	    outerspinArrayAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
	    outertrispinner.setAdapter(outerspinArrayAdapter);
	    outertrispinner.setSelection(0);

		
/*		select_rows = (Spinner) findViewById(R.id.y_select_rows);
		ArrayAdapter<CharSequence> row_a = ArrayAdapter.createFromResource(this, R.array.Connect4_Rows, android.R.layout.simple_spinner_item);
		row_a.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
		select_rows.setAdapter(row_a);
		select_rows.setSelection(3);
		select_columns = (Spinner) findViewById(R.id.y_select_columns);
		ArrayAdapter<CharSequence> column_a = ArrayAdapter.createFromResource(this, R.array.Connect4_Columns, android.R.layout.simple_spinner_item);
		column_a.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
		select_columns.setAdapter(column_a);
		select_columns.setSelection(5);
*/		
		OnClickListener mylistener = new OnClickListener(){
			public void onClick(View arg0) {
				// TODO Auto-generated method stub
				Intent myIntent = new Intent(menu.this, YInterface.class);
				
/*				myIntent.putExtra("isPlayer1Computer", ((RadioButton) findViewById(R.id.y_RadioButtonComputer1)).isChecked());
				myIntent.putExtra("isPlayer2Computer", ((RadioButton) findViewById(R.id.y_RadioButtonComputer2)).isChecked());
				myIntent.putExtra("numRows", select_rows.getSelectedItemPosition()+1);
				myIntent.putExtra("numCols", select_columns.getSelectedItemPosition()+1);
*/				
				menu.this.startActivity(myIntent);	
			}
		};
		
		playbutton.setOnClickListener(mylistener);
	}
}
