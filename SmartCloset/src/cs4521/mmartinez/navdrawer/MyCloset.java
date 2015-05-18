package cs4521.mmartinez.navdrawer;

import android.app.Fragment;
import android.app.FragmentManager;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;
 
public class MyCloset extends Fragment {
 
      TextView tvItemName;
      ImageView imageViewTop;
      ImageView imageViewBottom;
      private DBAdapter dbHelper;
      private Cursor cursorOutfit;
      private SharedPreferences prefs;
      private SharedPreferences.Editor editor;
      public static final String ITEM_NAME = "itemName";
      public static final String MY_PREFS_NAME = "MyPrefs";
      
      public MyCloset() {
    	  
      }
 
      @Override
      public View onCreateView(LayoutInflater inflater, ViewGroup container,
                  Bundle savedInstanceState) {
    	  View view = inflater.inflate(R.layout.fragment_mcloset, container, false);
          tvItemName = (TextView) view.findViewById(R.id.mcloset_text);
          prefs = getActivity().getSharedPreferences(MY_PREFS_NAME, 0);
  	      
          String pathTop = null;
          String pathBottom = null;
          dbHelper = new DBAdapter(getActivity());
          dbHelper.open();
          cursorOutfit = dbHelper.getUnratedOutfits();
          if (cursorOutfit.moveToFirst()){
  			pathTop = new String(cursorOutfit.getString(1));
  			pathBottom = new String(cursorOutfit.getString(2));	
  			cursorOutfit.close();
  		  }
          dbHelper.close();

          if ( pathTop != null && pathBottom != null ) {
        	// store the paths in the shared prefs
       		editor = prefs.edit();
       		editor.putString("iTP", pathTop);
       		editor.putString("iBP", pathBottom);
   			editor.commit();
   			// create the dual image fragment
            Fragment outfitFragment = new Outfit(pathTop, pathBottom);
            FragmentManager frgManager = getFragmentManager();
            frgManager.beginTransaction().replace(R.id.content_frame, outfitFragment).commit();
          }
          else 
        	Toast.makeText(getActivity(), "You don't have any outfits. Select Tops or Bottoms to get started.", Toast.LENGTH_LONG).show();
            
          return view;
      }
 
}
