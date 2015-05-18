package cs4521.mmartinez.databases;

import android.app.Activity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.database.Cursor;
import android.widget.Toast;


public class MainActivity extends Activity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        
        DBAdapter db = new DBAdapter(this);
        
        
        //add a contact
        //db.open();
        //long id = db.insertContact("Wei-Meng Lee", "weimenglee@learn2develop.net");
        //id = db.insertContact("Mary Jackson", "mary@jackson.com");
        //db.close();
    
        
        //get all contacts
        db.open();
        //long i = 0;
        Cursor c = db.getAllContacts();
        if (c.moveToFirst()) {
        	do {
        		DisplayContact(c);
        		//db.deleteContact(i);
        		//i++;
        	}
        	while(c.moveToNext());
        }
        db.close(); 
        
        /*
        //get a contact
        db.open();
        c = db.getContact(2);
        if (c.moveToFirst())
        	DisplayContact(c);
        else
        	Toast.makeText(this, "No contact found", Toast.LENGTH_LONG).show();
        db.close(); 
        
        //update contact
        db.open();
        if (db.updateContact(1, "Wei-Meng Lee", "weimenglee@gmail.com"))
        	Toast.makeText(this, "Update successful", Toast.LENGTH_LONG).show();
        else
        	Toast.makeText(this, "Update failed.", Toast.LENGTH_LONG).show();	
        db.close();
        
        
        db.open();
        if (db.deleteContact(1))
        	Toast.makeText(this, "Delete Successful.", Toast.LENGTH_LONG).show();
        else
        	Toast.makeText(this, "Delete failed.", Toast.LENGTH_LONG).show();
        db.close();*/
    }
        
    public void DisplayContact(Cursor c) {
    	Toast.makeText(this, "id: " + c.getString(0) + "\n" +
    					     "Name: " + c.getString(1) + "\n" +
    					     "Email: " + c.getString(2),
    					      Toast.LENGTH_LONG).show();
    }
}
