package cs4521.mmartinez.navdrawer;

import java.util.ArrayList;

import android.app.Fragment;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

public class Outfit extends Fragment {
    ImageView imageViewTop;
    ImageView imageViewBottom;
    String topPath = null;
    String bottomPath = null;
    int position;
    public ArrayList<ArrayList<String>> imagePaths;
    public DBAdapter dbHelper;
    private Cursor cursorTops;
    private Cursor cursorBottoms;
    private Cursor cursorOutfit;
    private SharedPreferences prefs;
    private SharedPreferences.Editor editor;
    public static final String ITEM_NAME = "itemName";
    public static final String MY_PREFS_NAME = "MyPrefs";
    public static final String[][] colorCombos = new String[][]{
  	  {"Tan", "Black"},
  	  {"Tan", "Brown"},
  	  {"Tan", "White"},
  	  {"Tan", "Blue"},
  	  {"Red", "Tan"},
  	  {"Red", "Black"},
  	  {"Red", "Blue"},
  	  {"White", "Blue"},
  	  {"White", "Black"},
  	  {"White", "Brown"},
  	  {"White", "Tan"},
  	  {"Brown", "Blue"},
  	  {"Brown", "White"},
  	  {"Brown", "Tan"},
  	  {"Black", "White"},
  	  {"Black", "Blue"},
  	  {"Black", "Tan"},
  	  {"Blue", "White"},
  	  {"Blue", "Brown"},
  	  {"Blue", "Black"},
  	  {"Blue", "Blue"},
  	  {"Green", "Tan"},
  	  {"Green", "Black"},
  	  {"Green", "Brown"},
  	  {"Green", "Blue"},
  	  {"Green", "Gray"}
    };
    
    public Outfit() {
    	
    }
    
    public Outfit(String tPath, String bPath) {
    	topPath = new String(tPath);
    	bottomPath = new String(bPath);
    }
    
	public Outfit(ArrayList<ArrayList<String>> iPaths, int pos) {
		imagePaths = iPaths;
		position = pos;
		
	}
	
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
            Bundle savedInstanceState) {
		View view = inflater.inflate(R.layout.fragment_outfit, container, false);
	    imageViewTop = (ImageView) view.findViewById(R.id.outfit_top);
	    imageViewBottom = (ImageView) view.findViewById(R.id.outfit_bottom);
	    
	    if (topPath != null && bottomPath != null) {
	    	imageViewTop.setImageURI(Uri.parse(topPath));
	    	imageViewBottom.setImageURI(Uri.parse(bottomPath));
	    } 
	    
        view.setOnTouchListener(new SwipeDetect() {
        	public void onSwipeRight() {
        		if (position == (imagePaths.size() / 2 ) - 1 ) {
        			position = 0;
        		} else {
        			position++;
        		}
        		changeImages(position);
        	}
        	public void onSwipeLeft() {
        		if (position == 0) {
        			position = (imagePaths.size() / 2) - 1 ;
        		} else {
        			position--;
        		}
        		changeImages(position);
        	}
        });
        
        generateOutfits();
	    return view;
	}
	
	public void generateOutfits() {
		imagePaths = new ArrayList<ArrayList<String>>();
		String [] topColors = null;
		String [] topPaths = null;
		String [] bottomColors = null;
		String [] bottomPaths = null;
		int like = 0;
		int dislike = 0;
        int pathRowIndex = 0;
        prefs = getActivity().getSharedPreferences(MY_PREFS_NAME, 0);
	    Long userId = prefs.getLong("UserId", 0);
	    // get all of the tops
	    dbHelper = new DBAdapter(getActivity());
        dbHelper.open();
        cursorTops = dbHelper.getAllUsersTops(userId);
        if (cursorTops.moveToFirst()) {
        	int i = 0;
        	int size = cursorTops.getCount();
        	topColors = new String[size];
        	topPaths = new String[size];
        	do {
        		if (cursorTops.getString(4) != null && cursorTops.getString(5) != null) {
            		topColors[i] = new String(cursorTops.getString(4));
            		topPaths[i] = new String(cursorTops.getString(5));
            		i++;
        		}
        	}
        	while(cursorTops.moveToNext());
        }
        cursorTops.close();
        
        // get all of the bottoms
        cursorBottoms = dbHelper.getAllUsersBottoms(userId);
        if (cursorBottoms.moveToFirst()) {
        	int i = 0;
        	int size = cursorBottoms.getCount();
        	bottomColors = new String[size];
        	bottomPaths = new String[size];
        	do {
        		if (cursorBottoms.getString(4) != null && cursorBottoms.getString(5) != null) {
            		bottomColors[i] = new String(cursorBottoms.getString(4));
            		bottomPaths[i] = new String(cursorBottoms.getString(5));
            		i++;
        		}
        	}
        	while(cursorBottoms.moveToNext());
        }
        cursorBottoms.close();
        
        
        // generate matching outfits, store this combination in the 2D array
        if (topColors != null && bottomColors != null && topPaths != null && bottomPaths != null) {
        	for (int i = 0 ; i < colorCombos.length ; i++) {
        		for (int j = 0 ; j < colorCombos[i].length ; j++) {
        			for (int k = 0 ; k < topColors.length ; k++) {
        				for (int l = 0 ; l < bottomColors.length ; l++) {
        					if (j == 0) {
            					if ( topColors[k].equals(colorCombos[i][j]) && bottomColors[l].equals(colorCombos[i][j+1]) ) {
            						// first query the db
            						cursorOutfit = dbHelper.getOutfit(topPaths[k], bottomPaths[l]);
            						if (cursorOutfit.moveToFirst()) {
            							// outfit already exists, so check the flags
            							like = cursorOutfit.getInt(1);
            							dislike = cursorOutfit.getInt(2);
            							// if these items haven't been flagged then add them into the images array
            							if (like == 0 && dislike == 0) {
            								for (int m = 0 ; m < 2 ; m++) {
                    							imagePaths.add(new ArrayList<String>());
                    							if (m == 0)
                    								imagePaths.get(pathRowIndex).add(topPaths[k]);
                    							else if (m == 1)
                    								imagePaths.get(pathRowIndex).add(bottomPaths[l]);
                    						} 
                    						pathRowIndex++;
            							}
            							cursorOutfit.close();
            						}
            						// this outfit doesn't exist so add it to the database, and then into the images array
            						else {
            							// insert the new row
            							Long outfitId = dbHelper.insertOutfit(userId, topPaths[k], bottomPaths[l]);
            							// if it was created then add this to the array
            							if (outfitId > 0) {
            								for (int m = 0 ; m < 2 ; m++) {
                    							imagePaths.add(new ArrayList<String>());
                    							if (m == 0)
                    								imagePaths.get(pathRowIndex).add(topPaths[k]);
                    							else if (m == 1)
                    								imagePaths.get(pathRowIndex).add(bottomPaths[l]);
                    						} 
                    						pathRowIndex++;
            							}
            						}
            					}
        					}
        				}
        			}
        		}
        	}
        }
        
        /*
        // print the contents - use for debugging
		if (imagePaths != null){
			for (int i = 0 ; i < imagePaths.size() / 2 ; i++) {
				System.out.println("imagePaths.[" + i + "].size(): " + imagePaths.get(i).size());
				for (int j = 0 ; j < imagePaths.get(i).size() ; j++) {
					System.out.println("imagePaths[" + i + "][" + j + "]: " + imagePaths.get(i).get(j));
				}
			}
		}*/
		
        dbHelper.close();
        position = 0;
	}
	
	public void changeImages(int pos) {
		imageViewTop.setImageURI(Uri.parse(imagePaths.get(pos).get(0)));
		imageViewBottom.setImageURI(Uri.parse(imagePaths.get(pos).get(1)));
		// store the paths in the shared prefs, this is used for the like/dislike feature
		editor = prefs.edit();
		editor.putString("iTP", imagePaths.get(pos).get(0));
		editor.putString("iBP", imagePaths.get(pos).get(1));
		editor.commit();
	}
}
