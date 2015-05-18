package cs4521.mmartinez.uicode;

import android.app.Activity;
import android.os.Bundle;
import android.view.ViewGroup.LayoutParams;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;


public class MainActivity extends Activity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //setContentView(R.layout.activity_main);
        //--param for views--
        LayoutParams params = 
        		new LinearLayout.LayoutParams(
        				LayoutParams.FILL_PARENT,
        				LayoutParams.WRAP_CONTENT);
        
        //--create a layout--
        LinearLayout layout = new LinearLayout(this);
        layout.setOrientation(LinearLayout.VERTICAL);
        
        //create a text view--
        TextView tv = new TextView(this);
        tv.setText("This is a TextView");
        tv.setLayoutParams(params);
        
        //--create a button--
        Button btn = new Button(this);
        btn.setText("This is a Button");
        btn.setLayoutParams(params);
        
        //--adds the textView
        layout.addView(tv);
        
        //--adds the button
        layout.addView(btn);
        
        //--create a layout param for the layout--
        LinearLayout.LayoutParams layoutParam =
        		new LinearLayout.LayoutParams(
        				LayoutParams.FILL_PARENT,
        				LayoutParams.WRAP_CONTENT);
        
        this.addContentView(layout, layoutParam);
        
    }
}
