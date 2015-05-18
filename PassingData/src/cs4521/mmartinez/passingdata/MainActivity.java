package cs4521.mmartinez.passingdata;

import android.app.Activity;
import android.os.Bundle;
import android.content.Intent;
import android.view.View;
import android.widget.Toast;

public class MainActivity extends Activity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
    }

    public void onClick(View view) {
    	//Intent i = new Intent("cs4521.mmartinez.PassingDataSecondActivity");
    	Intent i = new Intent(this, Second.class);
    	
    	//use putExtra() to add new name/value pairs
    	i.putExtra("str1", "This is a string");
    	i.putExtra("age1", "25");
    	
    	
    	//use a Bundle object to add new name/value pairs
    	Bundle extras = new Bundle();
    	extras.putString("str2", "This is another string");
    	extras.putInt("age2", 35);
    	
    	//attach the Bundle object to the Intent object
    	i.putExtras(extras);
    	
    	//start the activity to get a result back
    	startActivityForResult(i, 1);
    }
    
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
    	//check if the request code is 1
    	if (requestCode == 1) {
    		//if the result is OK
    		if (resultCode == RESULT_OK) {
    			
    			//get the result using getIntExtra()
    			Toast.makeText(this, Integer.toString(data.getIntExtra("age3", 0)), Toast.LENGTH_SHORT).show();
    			
    			//get the result using getData()
    			Toast.makeText(this, data.getData().toString(), Toast.LENGTH_SHORT).show();
    		}
    	}
    }
}
