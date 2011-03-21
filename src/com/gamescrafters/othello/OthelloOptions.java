package com.gamescrafters.othello;

import com.gamescrafters.gamesmanmobile.R;

import android.app.Activity;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.view.WindowManager;
import android.view.View.OnClickListener;
import android.widget.RadioButton;
import android.widget.Spinner;
import android.widget.ArrayAdapter;


public class OthelloOptions extends Activity{
	/** Called when the activity is first created. */
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
	    getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,WindowManager.LayoutParams.FLAG_FULLSCREEN);

		setContentView(R.layout.othello_options);

		findViewById(R.id.oth_PlayOthelloButton).setOnClickListener(new OnClickListener(){
			public void onClick(View v) {
				Intent myIntent = new Intent(OthelloOptions.this, Othello.class);
				
				myIntent.putExtra("isPlayer1Computer", ((RadioButton) findViewById(R.id.oth_compP1)).isChecked()); 
				myIntent.putExtra("isPlayer2Computer", ((RadioButton) findViewById(R.id.oth_compP2)).isChecked());
				myIntent.putExtra("dimension", ((Spinner)findViewById(R.id.oth_selectBoardSize)).getSelectedItem().toString());

				OthelloOptions.this.startActivity(myIntent);
				
				
				
			}
		});
		
		findViewById(R.id.oth_PlayOthello2Button).setOnClickListener(new OnClickListener(){
			public void onClick(View v) {
				Intent myIntent = new Intent(OthelloOptions.this, Othello2.class);
				
				myIntent.putExtra("isPlayer1Computer", ((RadioButton) findViewById(R.id.oth_compP1)).isChecked()); 
				myIntent.putExtra("isPlayer2Computer", ((RadioButton) findViewById(R.id.oth_compP2)).isChecked());
				myIntent.putExtra("dimension", ((Spinner)findViewById(R.id.oth_selectBoardSize)).getSelectedItem().toString());

				OthelloOptions.this.startActivity(myIntent);
				
				
				
			}
		});

	}
}
