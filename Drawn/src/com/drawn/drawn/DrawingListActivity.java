package com.drawn.drawn;

import android.app.Fragment;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;

public class DrawingListActivity extends SingleFragmentActivity {

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
	}

	@Override
	protected Fragment createFragment() {
		// TODO Auto-generated method stub
		return new DrawingListFragment();
	}
	
}
