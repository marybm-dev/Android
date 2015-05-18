package cs4521.mmartinez.gesturesscreen;

import java.util.ArrayList;

import android.app.Activity;
import android.gesture.Gesture;
import android.gesture.GestureLibraries;
import android.gesture.GestureLibrary;
import android.gesture.GestureOverlayView;
import android.gesture.GestureOverlayView.OnGesturePerformedListener;
import android.gesture.Prediction;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Toast;


public class MainActivity extends Activity implements
	OnGesturePerformedListener {
	    private GestureLibrary mLibrary;

		@Override
	    public void onCreate(Bundle savedInstanceState) {
	        super.onCreate(savedInstanceState);
	        setContentView(R.layout.activity_main);

	        mLibrary = GestureLibraries.fromRawResource(this, R.raw.gestures);
	        if (!mLibrary.load()) {
	        	finish();
	        }

	        GestureOverlayView gestures = (GestureOverlayView) findViewById(R.id.gestures);
	        gestures.addOnGesturePerformedListener(this);
	    }
		
		public void onGesturePerformed(GestureOverlayView overlay, Gesture gesture) {
			ArrayList<Prediction> predictions = mLibrary.recognize(gesture);

			// We want at least one prediction
			if (predictions.size() > 0) {
				Prediction prediction = predictions.get(0);
				// We want at least some confidence in the result
				if (prediction.score > 1.0) {
					// Show the spell
					Toast.makeText(this, prediction.name, Toast.LENGTH_SHORT).show();
				}
			}
		}
	}