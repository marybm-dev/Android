package cs4521.mmartinez.UsingIntent;

import android.app.Activity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.content.Intent;
import android.view.View;
import android.widget.Toast;


public class MainActivity extends Activity {
	int request_Code = 1;
	
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
    }

    public void onClick(View view) {
    	// startActivity(new Intent("cs4521.mmartinez.SecondActivity")
    	// or
    	// startActivity(new Intent(this, SecondActivity.class));
    	startActivityForResult(new Intent("cs4521.mmartinez.SecondActivity"), request_Code);
    }
    
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
    	if (requestCode == request_Code) {
    		if (resultCode == RESULT_OK) {
    			Toast.makeText(this, data.getData().toString(), Toast.LENGTH_SHORT).show();
    		}
    	}
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();
        if (id == R.id.action_settings) {
            return true;
        }
        return super.onOptionsItemSelected(item);
    }
}
