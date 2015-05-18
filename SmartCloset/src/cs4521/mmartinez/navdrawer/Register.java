package cs4521.mmartinez.navdrawer;

import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

public class Register extends Activity {
	
	private DBAdapter dbHelper;
	private EditText email;
	private EditText password;
	private EditText passwordConfirm;
	private Button btnRegister;
	public static final String MY_PREFS_NAME = "MyPrefs";
	
	@Override
	protected
	void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		
		dbHelper = new DBAdapter(this);
		dbHelper.open();
		setContentView(R.layout.register);
		inIt();
	}
	
	private void inIt() {
		email = (EditText) findViewById(R.id.regEmail);
		password = (EditText) findViewById(R.id.regPassword);
		passwordConfirm = (EditText) findViewById(R.id.regPasswordConfirm);
		btnRegister = (Button) findViewById(R.id.btnRegister);
		
		btnRegister.setOnClickListener(new Button.OnClickListener(){
			public void onClick(View view) {
				RegisterUser(view);
			}
		});
	}
	
	private void RegisterUser(View view) {
		String regEmail = email.getText().toString();
		String regPassword = password.getText().toString();
		String regPasswordConfirm = passwordConfirm.getText().toString();
		
		if (regPassword.equals(regPasswordConfirm)) {
			long userId = dbHelper.insertUser(regEmail, regPassword);
			if(userId > 1){
				dbHelper.close();
				// store user Id in shared preferences
				SharedPreferences prefs = getSharedPreferences(MY_PREFS_NAME, MODE_PRIVATE); 
				Long pkId = prefs.getLong("UserId", 0);
				if (pkId == 0){
					SharedPreferences.Editor editor = getSharedPreferences(MY_PREFS_NAME, MODE_PRIVATE).edit();
					editor.putLong("UserId", userId);
					editor.putString("Type", null);
					editor.commit();
				}
				Intent i = new Intent(view.getContext(), MainActivity.class);
				startActivity(i);
			}
		} 
		else {
			Toast.makeText(getApplicationContext(), "Error: the passwords you entered do not match!", Toast.LENGTH_SHORT).show();
		}
	}

}
