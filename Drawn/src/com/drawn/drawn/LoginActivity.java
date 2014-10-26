package com.drawn.drawn;

import java.util.Arrays;
import java.util.List;

import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;

import com.parse.LogInCallback;
import com.parse.ParseException;
import com.parse.ParseFacebookUtils;
import com.parse.ParseUser;

public class LoginActivity extends ActionBarActivity {

	private Dialog progressDialog;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);

		setContentView(R.layout.activity_login);

		Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
		setSupportActionBar(toolbar);
		toolbar.setTitleTextColor(getResources().getColor(R.color.white));
		toolbar.setNavigationIcon(R.drawable.drawnicono);

		final Button login = (Button) findViewById(R.id.loginButton);

		login.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				// Intent i = new
				// Intent(getApplicationContext(),MainActivity.class);
				// startActivity(i);
				// finish();
				onLoginButtonClicked();
			}
		});

		ParseUser currentUser = ParseUser.getCurrentUser();
		if ((currentUser != null) && ParseFacebookUtils.isLinked(currentUser)) {
			// Go to the user info activity
			showDrawingView();
		}

	}

	@Override
	public void onActivityResult(int requestCode, int resultCode, Intent data) {
		super.onActivityResult(requestCode, resultCode, data);
		ParseFacebookUtils.finishAuthentication(requestCode, resultCode, data);
	}

	private void onLoginButtonClicked() {
		LoginActivity.this.progressDialog = ProgressDialog.show(
				LoginActivity.this, "", "Logging in...", true);
		List<String> permissions = Arrays.asList("public_profile",
				"user_about_me", "user_relationships", "user_birthday",
				"user_location", "user_friends");
		ParseFacebookUtils.logIn(permissions, this, new LogInCallback() {
			@Override
			public void done(ParseUser user, ParseException err) {
				LoginActivity.this.progressDialog.dismiss();
				if (user == null) {
					Log.d(FacebookIntegration.TAG,
							"Uh oh. The user cancelled the Facebook login.");
				} else if (user.isNew()) {
					Log.d(FacebookIntegration.TAG,
							"User signed up and logged in through Facebook!");
					showDrawingView();
				} else {
					Log.d(FacebookIntegration.TAG,
							"User logged in through Facebook!");
					showDrawingView();
				}
			}
		});
	}

	private void showDrawingView() {
		Log.e("Hurra", "Se entro al metodo de redireccion, login exitoso");
		Intent intent = new Intent(this, DrawingListActivity.class);
		startActivity(intent);
		finish();
	}
}
