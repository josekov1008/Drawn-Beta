package com.drawn.drawn;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;

public class SentActivity extends ActionBarActivity {

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_sent);
		
		Intent i = getIntent();
		
		final Bundle extras = i.getExtras();
		
		final Button done = (Button) findViewById(R.id.sentDone);
		
		done.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
//				Intent i = new Intent(getApplicationContext(), DrawingListActivity.class);
//				startActivity(i);
//				overridePendingTransition(R.anim.abc_slide_in_bottom,R.anim.abc_slide_out_top);
				finish();
			}
		});
	}

}
