package cs4521.mmartinez.navdrawer;

import android.app.Fragment;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
 
public class Likes extends Fragment {
		
      public static final String ITEM_NAME = "itemName";
      public DBAdapter dbHelper;
      private Cursor cursorOutfit;
      private String[] topPaths;
      private String[] bottomPaths;
      private int position = 0;
      ImageView imageViewTop;
      ImageView imageViewBottom;
      
      public Likes() {
 
      }
 
      @Override
      public View onCreateView(LayoutInflater inflater, ViewGroup container,
                  Bundle savedInstanceState) {
            View view = inflater.inflate(R.layout.fragment_likes, container, false);
            imageViewTop = (ImageView) view.findViewById(R.id.like_top);
    	    imageViewBottom = (ImageView) view.findViewById(R.id.like_bottom);
            
    	    dbHelper = new DBAdapter(getActivity());
    	    dbHelper.open();
    	    cursorOutfit = dbHelper.getLikeOutfits();
    	    if (cursorOutfit.moveToFirst()) {
            	int i = 0;
            	int size = cursorOutfit.getCount();
            	topPaths = new String[size];
            	bottomPaths = new String[size];
            	do {
            		if (cursorOutfit.getString(1) != null && cursorOutfit.getString(2) != null) {
                		topPaths[i] = new String(cursorOutfit.getString(1));
                		bottomPaths[i] = new String(cursorOutfit.getString(2));
                		i++;
            		}
            	}
            	while(cursorOutfit.moveToNext());
            }
    	    cursorOutfit.close();
            dbHelper.close();
    	    
    	    if (topPaths != null && bottomPaths != null) {
    	    	imageViewTop.setImageURI(Uri.parse(topPaths[0]));
    	    	imageViewBottom.setImageURI(Uri.parse(bottomPaths[0]));
    	    } 
    	    
            view.setOnTouchListener(new SwipeDetect() {
            	public void onSwipeRight() {
            		if (position == topPaths.length - 1) {
            			position = 0;
            		} else {
            			position++;
            		}
            		changeImages(position);
            	}
            	public void onSwipeLeft() {
            		if (position == 0) {
            			position = topPaths.length - 1;
            		} else {
            			position--;
            		}
            		changeImages(position);
            	}
            });
            
            return view;
      }
  	  
      public void changeImages(int pos) {
    	  imageViewTop.setImageURI(Uri.parse(topPaths[pos]));
    	  imageViewBottom.setImageURI(Uri.parse(bottomPaths[pos]));
	}
 
}