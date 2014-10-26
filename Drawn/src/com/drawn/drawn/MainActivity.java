package com.drawn.drawn;

import org.json.JSONException;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.ImageButton;
import android.widget.LinearLayout;

public class MainActivity extends ActionBarActivity {
	
	private DrawingView drawView;
	private ImageButton currPaint;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);
		drawView = (DrawingView)findViewById(R.id.drawing);
		
		Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
		
		setSupportActionBar(toolbar);
		
	    toolbar.setTitleTextColor(getResources().getColor(R.color.white));
	    toolbar.setNavigationIcon(R.drawable.drawnicono);
	    
	    //toolbar.inflateMenu(R.menu.main);
	    
	    
		
		LinearLayout paintLayout = (LinearLayout)findViewById(R.id.paint_colors);
		
		currPaint = (ImageButton)paintLayout.getChildAt(0);
		
		currPaint.setImageDrawable(getResources().getDrawable(R.drawable.paint_pressed));
		
		ImageButton testButton = (ImageButton) findViewById(R.id.testButton);
		
		testButton.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				/*try {
					drawView.replayTest();
				} catch (JSONException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}*/
				
				//Se genera el friend picker
				envioPicker();
			}
		});
		
	}
	
	public void paintClicked(View view){
	    //use chosen color
		if(view!=currPaint){
		//update color
			ImageButton imgView = (ImageButton)view;
			String color = view.getTag().toString();
			drawView.setColor(color);
			imgView.setImageDrawable(getResources().getDrawable(R.drawable.paint_pressed));
			currPaint.setImageDrawable(getResources().getDrawable(R.drawable.paint));
			currPaint=(ImageButton)view;
		}
	}
	
	private void envioPicker() {
		//Este codigo envia un objeto, Parse infiere la clase y la agrega en la DB
		/*ParseObject prueba = new ParseObject("Meza");
		prueba.put("Meza", "Me la pela!");
		prueba.saveInBackground();*/
		Intent intent = new Intent(this, PickFriendsActivity.class);
		try {
			intent.putExtra("data", drawView.createJSON());
		} catch (JSONException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		startActivity(intent);
		overridePendingTransition(R.anim.abc_slide_in_bottom,R.anim.abc_slide_out_top);
		finish();
	}
}
