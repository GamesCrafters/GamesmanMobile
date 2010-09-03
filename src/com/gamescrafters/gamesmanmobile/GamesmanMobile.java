package com.gamescrafters.gamesmanmobile;

import android.app.TabActivity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.TabHost;
import android.widget.AdapterView.OnItemClickListener;

import com.gamescrafters.Y.menu;
import com.gamescrafters.connect4.Connect4Options;
import com.gamescrafters.connections.ConnectionsOptions;

public class GamesmanMobile extends TabActivity {
	/** Called when the activity is first created. */
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.main);

		final TabHost mTabHost = getTabHost();

		mTabHost.addTab(mTabHost.newTabSpec("GAMES").setIndicator("Games").setContent(R.id.gm_listview1));
		mTabHost.addTab(mTabHost.newTabSpec("DISCOVER").setIndicator("Discover").setContent(R.id.gm_discover_content));
		mTabHost.addTab(mTabHost.newTabSpec("ABOUT").setIndicator("About").setContent(R.id.gm_textview));
		mTabHost.setCurrentTab(0);
		ListView lv = (ListView) findViewById(R.id.gm_listview1);
		OnItemClickListener tttClick = new OnItemClickListener(){
			public void onItemClick(AdapterView<?> parent, 
					View v, int position, long id) {
				Intent myIntent;
				switch(position) {
				case 0:
					myIntent = new Intent(GamesmanMobile.this, Connect4Options.class);
					GamesmanMobile.this.startActivity(myIntent);
					break;
				case 1:
					myIntent = new Intent(GamesmanMobile.this, ConnectionsOptions.class);
					GamesmanMobile.this.startActivity(myIntent);
					break;
				case 2:
					myIntent = new Intent(GamesmanMobile.this, menu.class);
					GamesmanMobile.this.startActivity(myIntent);
					break;
				}
			}
		};
		lv.setOnItemClickListener(tttClick);
	}
}
