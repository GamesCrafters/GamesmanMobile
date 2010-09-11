package com.gamescrafters.othello;

import com.gamescrafters.gamesmanmobile.R;

import android.app.Activity;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;

public class OthelloOptions extends Activity{
	/** Called when the activity is first created. */
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.othello_options);
		
		findViewById(R.id.oth_PlayOthelloButton).setOnClickListener(new OnClickListener(){
			public void onClick(View v) {
				Intent myIntent = new Intent(OthelloOptions.this, Othello.class);
				OthelloOptions.this.startActivity(myIntent);
			}
		});

	}
}
