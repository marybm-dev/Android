package cs4521.mmartinez.databasesadd;

import android.app.Activity;
import android.os.Bundle;
import android.database.Cursor;
import android.widget.Toast;


public class MainActivity extends Activity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        
        DBAdapter db = new DBAdapter(this);
        
        //add a contact
        db.open();
        long id = db.insertContact("Wei-Meng Lee",  "weimenglee@learn2develop.net");
        id = db.insertContact("Bugs Bunny", "carrots@rabbit.com");
        id = db.insertContact("Seymore Butts", "seybutts@live.com");
        //db.close();
        
        // get a contact
        Cursor c = db.getContact(1);
        if (c.moveToFirst())
        	DisplayContact(c);
        else
        	Toast.makeText(this, "No contact found", Toast.LENGTH_LONG).show();
        db.close();
    }
        
    public void DisplayContact(Cursor c) {
    	Toast.makeText(this, "id: " + c.getString(0) + "\n" +
    					     "Name: " + c.getString(1) + "\n" +
    					     "Email: " + c.getString(2),
    					      Toast.LENGTH_LONG).show();
    }
}
