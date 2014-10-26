package com.drawn.drawn;

import org.json.JSONException;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.support.v7.widget.Toolbar;

public class ReceiveActivity extends ActionBarActivity {

	private DisplayingView drawView;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		
		setContentView(R.layout.activity_receive);
		
		Intent i = getIntent();
		
		Bundle extras = i.getExtras();
		
		Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
	    setSupportActionBar(toolbar);
	    toolbar.setTitleTextColor(getResources().getColor(R.color.white));
	    toolbar.setNavigationIcon(R.drawable.drawnicono);
	    
	    drawView = (DisplayingView) findViewById(R.id.displayingView);
	    
	    try {
			drawView.readJSON(extras.getString("data"));
		} catch (JSONException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	    
	}

	
}
