package com.drawn.drawn;

import java.util.ArrayList;
import java.util.List;

import android.app.Fragment;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.BaseAdapter;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import com.facebook.FacebookRequestError;
import com.facebook.Request;
import com.facebook.Response;
import com.facebook.Session;
import com.facebook.model.GraphUser;
import com.parse.FindCallback;
import com.parse.ParseException;
import com.parse.ParseFacebookUtils;
import com.parse.ParseObject;
import com.parse.ParseQuery;

public class DrawingListFragment extends Fragment {

	private ArrayList<Drawing> mDrawings;
	private ListView mDrawingsList;
	private GridView mDrawingsGrid;
	private String id_current_user = "";
	
	@Override
	public void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		
	}
	
	@Override
	public View onCreateView(LayoutInflater inflater,
			@Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
		
		//View v = inflater.inflate(R.layout.layout_puzzle_list, container, false);
		View v = inflater.inflate(R.layout.layout_grid, container, false);
		
		
				
		mDrawings = new ArrayList<Drawing>();
		mDrawingsList = (ListView) v.findViewById(R.id.puzzlesListView);
		
		mDrawingsGrid = (GridView) v.findViewById(R.id.gridview);
		
		/*for(int i = 0; i < 15; i++) {
			mDrawings.add(new Drawing(""));
		}*/
		
		//ParseQuery<ParseObject> query = ParseQuery.getQuery("Datos");
		
		//query.getInBackground(objectId, callback)
		
		//Se pide el User ID a Facebook
        Session session = ParseFacebookUtils.getSession();
		if (session != null && session.isOpened()) {
			requestUserID();
			
			ParseQuery<ParseObject> query = ParseQuery.getQuery("Datos");
			
			//query.whereEqualTo("user_id_destino", id_current_user);
			query.findInBackground(new FindCallback<ParseObject>() {
			    public void done(List<ParseObject> dataList, ParseException e) {
			        if (e == null) {
			            Log.d("score", "Retrieved " + dataList.size() + " scores");
			            
			            for(int z = 0; z < dataList.size(); z++) {
			            	Log.d("score", dataList.get(z).getString("user_id_destino") + " " + id_current_user);
			            	mDrawings.add(new Drawing(dataList.get(z).getString("sketchData"), dataList.get(z).getString("origen_nombre")));
			            }
			            
			            PuzzleGridAdapter adapter = new PuzzleGridAdapter(getActivity());
			    		mDrawingsGrid.setAdapter(adapter);
			            
			        } else {
			            Log.d("score", "Error: " + e.getMessage());
			        }
			    }
			});
		}
		
		//PuzzleAdapter adapter = new PuzzleAdapter(mPuzzles);
		//mPuzzlesList.setAdapter(adapter);
		
		
		
		return v;
	}

	

	private class PuzzleAdapter extends ArrayAdapter<Drawing>{
		
		public PuzzleAdapter(ArrayList<Drawing> puzzles) {
			super(getActivity(),0,puzzles);
		}

		@Override
		public View getView(int position, View convertView, ViewGroup parent) {
			if(convertView == null) {
				convertView = getActivity().getLayoutInflater().inflate(R.layout.layout_puzzle_item, null);
				final ImageView image = (ImageView) convertView.findViewById(R.id.cropImageView);
//				if(position%2 == 1 ) {
//					image.setImageDrawable(getResources().getDrawable(R.drawable.vlcsnap2));
//				}
//				else {
//					image.setImageDrawable(getResources().getDrawable(R.drawable.vlcsnap));
//				}
			}
			
			
			
			return convertView;
		}
	}
	
	private class PuzzleGridAdapter extends BaseAdapter {

		private Context mContext;
		
		public PuzzleGridAdapter(Context c) {
			mContext = c;
		}
		
		@Override
		public int getCount() {
			return mDrawings.size();
		}

		@Override
		public Object getItem(int position) {
			// TODO Auto-generated method stub
			return null;
		}

		@Override
		public long getItemId(int position) {
			// TODO Auto-generated method stub
			return 0;
		}

		@Override
		public View getView(final int position, View convertView, ViewGroup parent) {
			
			if(convertView == null) {
				convertView = getActivity().getLayoutInflater().inflate(R.layout.layout_puzzle_item, null);
				final ImageView image = (ImageView) convertView.findViewById(R.id.cropImageView);
				final TextView textview = (TextView) convertView.findViewById(R.id.myImageViewText);
//				if(position%2 == 1 ) {
//					image.setImageDrawable(getResources().getDrawable(R.drawable.vlcsnap2));
//				}
//				else {
//					image.setImageDrawable(getResources().getDrawable(R.drawable.vlcsnap));
//				}
				textview.setText(mDrawings.get(position).getName());
				
				convertView.setOnClickListener(new OnClickListener() {
					
					@Override
					public void onClick(View v) {
						// TODO Auto-generated method stub
						Intent i = new Intent(getActivity(),ReceiveActivity.class);
						i.putExtra("data", mDrawings.get(position).getJSON());
						startActivity(i);
					}
				});
			}
			
			return convertView;
		}
		
	}

	private void requestUserID() {
    	Request request = Request.newMeRequest(ParseFacebookUtils.getSession(),
				new Request.GraphUserCallback() {
					@Override
					public void onCompleted(GraphUser user, Response response) {
						if (user != null) {
							id_current_user = user.getId().toString();
							/*name_current_user = user.getFirstName().toString();
							last_current_user = user.getLastName().toString();*/

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
