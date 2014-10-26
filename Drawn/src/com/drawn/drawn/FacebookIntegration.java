package com.drawn.drawn;

import android.app.Application;
import android.util.Log;

import com.parse.Parse;
import com.parse.ParseException;
import com.parse.ParseFacebookUtils;
import com.parse.ParseInstallation;
import com.parse.PushService;
import com.parse.ParsePush;
import com.parse.SaveCallback;

public class FacebookIntegration extends Application {

	static final String TAG = "Drawn";

	@Override
	public void onCreate() {
		super.onCreate();
		
		Parse.initialize(this, "602gLYxcP08hruEXtkYSSl8n6DuHEBqQynWXiOjE",
				"2d37nFSZywxXcfKxQzP9Tbaj7RZf2nYhh34fG1nh");

		ParseInstallation.getCurrentInstallation().saveInBackground();
		
		// Set your Facebook App Id in strings.xml
		ParseFacebookUtils.initialize(getString(R.string.app_id));

	}

}
