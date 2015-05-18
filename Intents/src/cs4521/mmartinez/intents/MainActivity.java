package cs4521.mmartinez.intents;

import android.app.Activity;
import android.os.Bundle;
import android.content.Intent;
import android.net.Uri;
import android.view.View;

public class MainActivity extends Activity {
	// this is a comment
	int request_Code = 1;
	
	/** this is a block comment **/
	
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
    }
    
    public void onClickWebBrowser(View view) {
    	Intent i = new Intent(android.content.Intent.ACTION_VIEW, Uri.parse("http://www.amazon.com"));
    	startActivity(i);
    }
    
    public void onClickMakeCalls(View view) {
    	Intent i = new Intent(android.content.Intent.ACTION_DIAL, Uri.parse("tel:651234567"));
    	startActivity(i);
    }
    
    public void onClickShowMap(View view) {
    	Intent i = new Intent(android.content.Intent.ACTION_VIEW, Uri.parse("geo:37.827500,-122.481670"));
    	startActivity(i);
    }
    
    public void onClickLaunchMyBrowser(View view) {
    	Intent i = new Intent("cs4521.mmartinez.MyBrowser");
    	i.setData(Uri.parse("http://www.amazon.com"));
    	startActivity(i);
    }
}
