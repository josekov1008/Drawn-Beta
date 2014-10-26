/**
 * Copyright 2010-present Facebook.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *    http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.drawn.drawn;

import android.app.Application;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentManager;
import android.util.Log;
import android.widget.Toast;
import com.facebook.FacebookException;
import com.facebook.FacebookRequestError;
import com.facebook.Request;
import com.facebook.Response;
import com.facebook.Session;
import com.facebook.model.GraphUser;
import com.facebook.widget.FriendPickerFragment;
import com.facebook.widget.PickerFragment;

import java.util.List;

import org.json.JSONException;
import org.json.JSONObject;

import com.parse.LogInCallback;
import com.parse.ParseException;
import com.parse.ParseFacebookUtils;
import com.parse.ParseObject;
import com.parse.ParseUser;

import com.parse.Parse;

// This class provides an example of an Activity that uses FriendPickerFragment to display a list of
// the user's friends. It takes a programmatic approach to creating the FriendPickerFragment with the
// desired parameters -- see PickPlaceActivity in the PlacePickerSample project for an example of an
// Activity creating a fragment (in this case a PlacePickerFragment) via XML layout rather than
// programmatically.
public class PickFriendsActivity extends FragmentActivity {
    FriendPickerFragment friendPickerFragment;
    String id_current_user = "";
    String name_current_user = "";
    String last_current_user = "";

    // A helper to simplify life for callers who want to populate a Bundle with the necessary
    // parameters. A more sophisticated Activity might define its own set of parameters; our needs
    // are simple, so we just populate what we want to pass to the FriendPickerFragment.
    /*public static void populateParameters(Intent intent, String userId, boolean multiSelect, boolean showTitleBar) {
        intent.putExtra(FriendPickerFragment.USER_ID_BUNDLE_KEY, userId);
        intent.putExtra(FriendPickerFragment.MULTI_SELECT_BUNDLE_KEY, multiSelect);
        intent.putExtra(FriendPickerFragment.SHOW_TITLE_BAR_BUNDLE_KEY, showTitleBar);
    }*/

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.pick_friends_activity);

        Bundle extras = getIntent().getExtras();
        final String datosJSON = extras.getString("data");
        
        FragmentManager fm = getSupportFragmentManager();

        if (savedInstanceState == null) {
            // First time through, we create our fragment programmatically.
            final Bundle args = getIntent().getExtras();
            friendPickerFragment = new FriendPickerFragment(args);
            fm.beginTransaction()
                    .add(R.id.friend_picker_fragment, friendPickerFragment)
                    .commit();
        } else {
            // Subsequent times, our fragment is recreated by the framework and already has saved and
            // restored its state, so we don't need to specify args again. (In fact, this might be
            // incorrect if the fragment was modified programmatically since it was created.)
            friendPickerFragment = (FriendPickerFragment) fm.findFragmentById(R.id.friend_picker_fragment);
        }

        friendPickerFragment.setOnErrorListener(new PickerFragment.OnErrorListener() {
            @Override
            public void onError(PickerFragment<?> fragment, FacebookException error) {
                PickFriendsActivity.this.onError(error);
            }
        });

        //Se pide el User ID a Facebook
        Session session = ParseFacebookUtils.getSession();
		if (session != null && session.isOpened()) {
			requestUserID();
		}
        
        friendPickerFragment.setOnDoneButtonClickedListener(new PickerFragment.OnDoneButtonClickedListener() {
            @Override
            public void onDoneButtonClicked(PickerFragment<?> fragment) {
                // We just store our selection in the Application for other activities to look at.
            	//Application application =  getApplication();
                //((Object) application).setSelectedUsers(friendPickerFragment.getSelection());
            	List<GraphUser> batos = friendPickerFragment.getSelection();
            	
            	//Se suben los datos de la lista a Parse, solo para debugeo
            	ParseUser current = ParseUser.getCurrentUser();
            	
            	for (int i = 0; i < batos.size(); i++) {
            		ParseObject datos = new ParseObject("Datos");
            		datos.put("user_id_destino", batos.get(i).getId());
            		datos.put("destino_nombre", batos.get(i).getName());
            		datos.put("origen", id_current_user);
            		datos.put("origen_nombre", name_current_user + " " + last_current_user);
            		
            		datos.put("sketchData", datosJSON);
            		
            		datos.saveInBackground();
            	}
            	
                setResult(RESULT_OK, null);
                
                Intent intent2 = new Intent(getBaseContext(), SentActivity.class);
        		startActivity(intent2);
                
                finish();
            }
        });
    }

    private void onError(Exception error) {
        //Se activa en caso de error
    }

    //@Override
    protected void onStart() {
        super.onStart();
        
        friendPickerFragment.loadData(false);
    }
    
    private void requestUserID() {
    	Request request = Request.newMeRequest(ParseFacebookUtils.getSession(),
				new Request.GraphUserCallback() {
					@Override
					public void onCompleted(GraphUser user, Response response) {
						if (user != null) {
							id_current_user = user.getId().toString();
							name_current_user = user.getFirstName().toString();
							last_current_user = user.getLastName().toString();

						} else if (response.getError() != null) {
							if ((response.getError().getCategory() == FacebookRequestError.Category.AUTHENTICATION_RETRY)
									|| (response.getError().getCategory() == FacebookRequestError.Category.AUTHENTICATION_REOPEN_SESSION)) {
								Log.d(FacebookIntegration.TAG,
										"The facebook session was invalidated.");
							} else {
								Log.d(FacebookIntegration.TAG,
										"Some other error: "
												+ response.getError()
														.getErrorMessage());
							}
						}
					}
				});
		request.executeAsync();
	}
    
    
}
