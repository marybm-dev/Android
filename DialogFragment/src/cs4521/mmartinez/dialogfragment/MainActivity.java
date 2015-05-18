package cs4521.mmartinez.dialogfragment;

import android.app.Activity;
import android.os.Bundle;
import android.util.Log;


public class MainActivity extends Activity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        
        Fragment1 dialogFragment = Fragment1.newInstance("Are you sure you want to do this?");
        dialogFragment.show(getFragmentManager(), "dialog");
    }

    public void doPositiveClick() {
    	Log.d("DialogFragmentEample", "User clicks on OK");
    }

    public void doNegativeClick() {
    	Log.d("DialogFragmentExample", "User clicks on Cancel");
    }
}
