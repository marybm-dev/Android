package cs4521.mmartinez.usingpreferences;

import android.app.Activity;
import android.os.Bundle;
import android.content.Intent;
import android.view.View;
import android.content.SharedPreferences;
import android.widget.EditText;
import android.widget.Toast;


public class MainActivity extends Activity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
    }

    public void onClickLoad(View view) {
    	Intent i = new Intent("cs4521.mmartinez.AppPreferenceActivity");
    	startActivity(i);
    }
    
    public void onClickDisplay(View view) {
    	SharedPreferences appPrefs = getSharedPreferences("cs4521.mmartinez.usingpreferences_preferences", MODE_PRIVATE);
    	DisplayText(appPrefs.getString("editTextPref", ""));
    }
    
    public void onClickModify(View view){
    	SharedPreferences appPrefs = getSharedPreferences("cs4521.mmartinez.usingpreferences_preferences", MODE_PRIVATE);
    	SharedPreferences.Editor prefsEditor = appPrefs.edit();
    	prefsEditor.putString("editTextPref", ((EditText) findViewById(R.id.txtString)).getText().toString());
    	prefsEditor.commit();
    }
    
    private void DisplayText(String str) {
    	Toast.makeText(getBaseContext(), str, Toast.LENGTH_LONG).show();
    }
}
