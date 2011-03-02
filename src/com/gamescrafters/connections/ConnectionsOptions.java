package com.gamescrafters.connections;

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

public class ConnectionsOptions extends Activity {
	Spinner select_size;

	/** Called when the activity is first created. */
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
	    getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,WindowManager.LayoutParams.FLAG_FULLSCREEN);

		setContentView(R.layout.connections_options);
		
		select_size = (Spinner) findViewById(R.id.con_choosesize);
		ArrayAdapter<CharSequence> row_a = ArrayAdapter.createFromResource(this, R.array.boardsizes, android.R.layout.simple_spinner_item);
		row_a.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
		select_size.setAdapter(row_a);
		select_size.setSelection(1);

		OnClickListener mylistner = new OnClickListener(){
			public void onClick(View arg0){
				Intent myIntent = new Intent(ConnectionsOptions.this,Connections.class);
				
				myIntent.putExtra("size",2*select_size.getSelectedItemPosition()+5);
				ConnectionsOptions.this.startActivity(myIntent);
			}
		};
		Button button = (Button) findViewById(R.id.con_playbutton);
		button.setOnClickListener(mylistner);
	
	}

}

