package cs4521.mmartinez.navdrawer;

import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

public class Login extends Activity {
	
	private DBAdapter dbHelper;
	private EditText email;
	private EditText password;
	private Button btnLogin;
	private Button btnRegister;
	public static final String MY_PREFS_NAME = "MyPrefs";
	
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		getActionBar().setBackgroundDrawable(getResources().getDrawable(R.drawable.ocean));
		dbHelper = new DBAdapter(this);
		dbHelper.open();
		setContentView(R.layout.login);
		inIt();
	}
	
	private void inIt() {
		email = (EditText) findViewById(R.id.email);
		password = (EditText) findViewById(R.id.password);
		btnLogin = (Button) findViewById(R.id.btnLogin);
		btnRegister = (Button) findViewById(R.id.btnRegister);
		
		// set button listeners
		btnLogin.setOnClickListener(new Button.OnClickListener(){
			public void onClick (View view){
				LoginUser(view);
			}
		});
		btnRegister.setOnClickListener(new Button.OnClickListener(){
			public void onClick (View view){
				RegisterUser(view);
			}
		});
		
		
	}
	
	private void LoginUser(View view) {
		
		//String userId = null;
		Long userId = (long) 0;
		String userEmail = email.getText().toString();
		String userPassword = password.getText().toString();
		int counter = 0;
		
		Cursor c = dbHelper.getAllUsers();
		if (c.moveToFirst()) {
			do {
				if ( userEmail.equals(c.getString(2)) && userPassword.equals(c.getString(1)) ) {
					// found the user!
					Toast.makeText(getApplicationContext(), "Welcome!", Toast.LENGTH_SHORT).show();
					userId = Long.valueOf(c.getLong(0));
					counter++;
					break;
				} else if ( userEmail.equals(c.getString(2)) && !userPassword.equals(c.getString(1)) ){
					// found the user but wrong password or user doesn't exist
					Toast.makeText(getApplicationContext(), "Sorry the password you entered is incorrect!", Toast.LENGTH_SHORT).show();
					counter++;
					break;
				}
			} while (c.moveToNext());
		}
		
		if (counter == 0){
			Toast.makeText(getApplicationContext(), "You don't have an account, please register.", Toast.LENGTH_SHORT).show();
		}
		
		if ( userId > 0 ) {
			// close the db since we are done with it
			dbHelper.close();
			// store user Id in shared preferences
			SharedPreferences prefs = getSharedPreferences(MY_PREFS_NAME, MODE_PRIVATE); 
			SharedPreferences.Editor editor = getSharedPreferences(MY_PREFS_NAME, MODE_PRIVATE).edit();
			Long pkId = prefs.getLong("UserId", 0);
			if (pkId == 0){
				editor.putLong("UserId", userId);
			}
			editor.putString("Type", "Outfits");
			editor.commit();
			// go to main activity
			Intent i = new Intent(view.getContext(), MainActivity.class);
			startActivity(i);
			startActivity(new Intent(""));
		}
			
		/*
		 * disable the above code and enable this code to disable login - used for testing; login not required
		 * 
			SharedPreferences prefs = getSharedPreferences(MY_PREFS_NAME, MODE_PRIVATE); 
			SharedPreferences.Editor editor = prefs.edit();

			editor.putString("Type", "Outfits");
			editor.commit();
			// go to main activity
			Intent i = new Intent(view.getContext(), MainActivity.class);
			startActivity(i);
		*/
	}
	
	private void RegisterUser(View view) {
		dbHelper.close();
		Intent i = new Intent(view.getContext(), Register.class);
		startActivity(i);
	}
	
}
