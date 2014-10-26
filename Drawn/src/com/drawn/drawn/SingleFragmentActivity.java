package com.drawn.drawn;

import android.app.Fragment;
import android.app.FragmentManager;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;

public abstract class SingleFragmentActivity extends ActionBarActivity {

	protected abstract Fragment createFragment();
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_fragment);
		
		FragmentManager fm = getFragmentManager();
		
		Fragment fragment = fm.findFragmentById(R.id.fragmentContainer);
		
		Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
		
		setSupportActionBar(toolbar);
		
	    toolbar.setTitleTextColor(getResources().getColor(R.color.white));
	    toolbar.setNavigationIcon(R.drawable.drawnicono);
	    
	    //toolbar.inflateMenu(R.menu.main);
		
		if(fragment == null) {
			fragment = createFragment();
			fm.beginTransaction().add(R.id.fragmentContainer, fragment).commit();
		}
	}
	
	public boolean onCreateOptionsMenu(Menu menu) {
		getMenuInflater().inflate(R.menu.main, menu);
		return true;
	}
	
	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		switch(item.getItemId()) {
		case R.id.send_new_drawing:
			Log.e("test","listener new drawing");
			Intent i = new Intent(this,MainActivity.class);
			startActivity(i);
			return true;
		default:
			return true;
		}
	}
	
}
