package com.drawn.drawn;

import java.util.LinkedList;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Path;
import android.os.Handler;
import android.util.AttributeSet;
import android.util.Log;
import android.view.View;

public class DisplayingView extends View {

private Context context;
	
	//drawing path
	private Path drawPath;
	//drawing and canvas paint
	private Paint drawPaint, canvasPaint;
	//initial color
	private int paintColor = 0xFF660000;
	//canvas
	private Canvas drawCanvas;
	//canvas bitmap
	private Bitmap canvasBitmap;
	
	public LinkedList<Line> lineArray;
	public LinkedList<Line> lines = new LinkedList<Line>();
	
	private boolean startFlag = true;
	
	private Handler handler = new Handler();
	
	private Runnable runnable = new Runnable() {
		@Override
		public void run() {
			if (!lines.isEmpty()) {
				// IF THERE ARE STILL COORDINATES TO DRAW
				if (!lines.peek().coords.isEmpty()) {
					// IF THIS IS THE FIRST COORDINATE
					if(startFlag) {
						Coordinate temp1 = lines.peek().coords.pop();
						drawPaint.setColor(lines.peek().getColor());
						drawPath.moveTo(temp1.x, temp1.y);
						startFlag = false;
					}
					if(!lines.isEmpty()) {
						if(!lines.peek().coords.isEmpty()) {
							Coordinate temp = lines.peek().coords.pop();
							drawPath.lineTo(temp.x, temp.y);
							drawCanvas.drawPath(drawPath, drawPaint);
							invalidate();
							handler.postDelayed(this, 25);
						}
						else {
							Log.e("ERROR","peek coords");
						}
					}
					else {
						Log.e("ERROR","peek line");
					}
				}
				// WHEN LINE IS FINISHED DRAWING
				else {
					lines.pop();
					drawPath.reset();
					handler.postDelayed(this, 70);
					startFlag = true;
				}
			}
		}
	};

	public DisplayingView(Context context, AttributeSet attrs) {
		super(context, attrs);
		this.context = context;
		setupDrawing();
	}
	
	private void setupDrawing(){
		
		//get drawing area setup for interaction
		drawPath = new Path();
		drawPaint = new Paint();
		
		drawPaint.setColor(paintColor);
		
		drawPaint.setAntiAlias(true);
		drawPaint.setStrokeWidth(20);
		drawPaint.setStyle(Paint.Style.STROKE);
		drawPaint.setStrokeJoin(Paint.Join.ROUND);
		drawPaint.setStrokeCap(Paint.Cap.ROUND);
		
		canvasPaint = new Paint(Paint.DITHER_FLAG);
		
		lineArray = new LinkedList<Line>();
		
		
	}
	
	@Override
	protected void onSizeChanged(int w, int h, int oldw, int oldh) {
		//view given size
		super.onSizeChanged(w, h, oldw, oldh);
		canvasBitmap = Bitmap.createBitmap(w, h, Bitmap.Config.ARGB_8888);
		drawCanvas = new Canvas(canvasBitmap);
	}
	
	@Override
	protected void onDraw(Canvas canvas) {
		//draw view
		canvas.drawBitmap(canvasBitmap, 0, 0, canvasPaint);
		canvas.drawPath(drawPath, drawPaint);
	}
	
	public void setColor(String newColor){
		//set color 
		invalidate();
		paintColor = Color.parseColor(newColor);
		drawPaint.setColor(paintColor);
	}
	
	public String createJSON() throws JSONException {
		JSONArray jsonfinal = new JSONArray();
		JSONObject jsonline;
		JSONArray jsoncoords;
		JSONObject jsoncoord;
		for (Line line : lineArray) {
			jsonline = new JSONObject();
			jsoncoords = new JSONArray();
			jsonline.put("color", line.getColor());
			for (Coordinate coords : line.coords) {
				jsoncoord = new JSONObject();
				jsoncoord.put("x", coords.x);
				jsoncoord.put("y", coords.y);
				jsoncoords.put(jsoncoord);
			}
			jsonline.put("coords", jsoncoords);
			jsonfinal.put(jsonline);
		}
		return jsonfinal.toString();
	}
	
	public void readJSON(String data) throws JSONException {
		JSONArray json = new JSONArray(data);
		JSONObject temp;
		Line line;
		//READ LINES
		for(int i = 0; i < json.length(); i++) {
			//LINE
			temp = json.getJSONObject(i);
			line = new Line(temp.getInt("color"));
			//READ COORDS
			JSONArray tempcoords = temp.getJSONArray("coords");
			JSONObject tempcoord;
			for(int j = 0; j < tempcoords.length(); j++) {
				tempcoord = tempcoords.getJSONObject(j);
				line.add(Float.parseFloat(tempcoord.getString("x")), Float.parseFloat(tempcoord.getString("y")));
			}
			lines.add(line);
		}
		handler.postDelayed(runnable, 700);
	}

	
}
