package cs4521.mmartinez.dialog;

import android.app.Activity;
import android.os.Bundle;
import android.app.AlertDialog;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.DialogInterface.OnClickListener;
import android.view.View;
import android.widget.Toast;


public class DialogActivity extends Activity {

	CharSequence[] items = { "Google", "Apple", "Microsoft" };
	boolean[] itemsChecked = new boolean [items.length];
	ProgressDialog progressDialog;
	
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.dialog_main);
    }

    @SuppressWarnings("deprecation")
	public void onClick(View v) {
    	showDialog(0);
    }
    
    public void onClick2(View v) {
    	final ProgressDialog dialog = ProgressDialog.show(
    			this, "Doing something", "Please wait...", true);
    	new Thread(new Runnable(){
    		public void run(){
    			try {
    				Thread.sleep(5000);
    				dialog.dismiss();
    			} catch (InterruptedException e) {
    				e.printStackTrace();
    			}
    		}
    	}).start();
    }
    
    @SuppressWarnings("deprecation")
    public void onClick3(View v) {
    	showDialog(1);
    	progressDialog.setProgress(0);
    	
    	new Thread(new Runnable(){
    		public void run(){
    			for (int i=1; i<=15; i++){
    				try {
    					Thread.sleep(1000);
    					progressDialog.incrementProgressBy((int)(100/15));
    				} catch (InterruptedException e) {
    					e.printStackTrace();
    				}
    			}
    			progressDialog.dismiss();
    		}
    	}).start();
    }
    
    @Override
    protected Dialog onCreateDialog(int id) {
    	switch (id) {
    	case 0:
    			return new AlertDialog.Builder(this)
    			.setIcon(R.drawable.ic_launcher)
    			.setTitle("This is a dialog with some simple text...")
    			.setPositiveButton("OK",
    					new OnClickListener() {
    						public void onClick(DialogInterface dialog, int whichButton)
    							{
    								Toast.makeText(getBaseContext(), "OK clicked!", Toast.LENGTH_SHORT).show();
    							}
    					}
    			)
    			.setNegativeButton("Cancel",
    					new DialogInterface.OnClickListener() {
							
							@Override
							public void onClick(DialogInterface dialog, int whichButton) {
								Toast.makeText(getBaseContext(),
									"Cancel clicked!", Toast.LENGTH_SHORT).show();
							}
						}
    			)
    			.setMultiChoiceItems(items, itemsChecked,
    				new DialogInterface.OnMultiChoiceClickListener() {
    					public void onClick(DialogInterface dialog,
    					int which, boolean isChecked) {
    						Toast.makeText(getBaseContext(),
    								items[which] + (isChecked ? " checked!":" unchecked!"),
    								Toast.LENGTH_SHORT).show();
    					}
    				}
    			).create();
    	case 1:
    		progressDialog = new ProgressDialog(this);
    		progressDialog.setIcon(R.drawable.ic_launcher);
    		progressDialog.setTitle("Downloading files...");
    		progressDialog.setProgressStyle(ProgressDialog.STYLE_HORIZONTAL);
    		progressDialog.setButton(DialogInterface.BUTTON_POSITIVE, "OK",
    			new DialogInterface.OnClickListener() {
    				public void onClick(DialogInterface dialog,
    						int whichButton)
    				{
    					Toast.makeText(getBaseContext(), "OK clicked!", Toast.LENGTH_SHORT).show();
    				}
    		});
    		progressDialog.setButton(DialogInterface.BUTTON_NEGATIVE, "Cancel",
    			new DialogInterface.OnClickListener() {
    				public void onClick(DialogInterface dialog,
    						int whichButton)
    				{
    					Toast.makeText(getBaseContext(), "Cancel Clicked!", Toast.LENGTH_SHORT).show();
    				}
    		});
    		return progressDialog;
    		}
    		return null;
    }
}
