package cs4521.mmartinez.navdrawer;

import java.util.ArrayList;
import java.util.List;

import android.app.Activity;
import android.app.Fragment;
import android.app.FragmentManager;
import android.content.SharedPreferences;
import android.content.res.Configuration;
import android.os.Bundle;
import android.support.v4.app.ActionBarDrawerToggle;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.Toast;


public class MainActivity extends Activity {

	private DrawerLayout mDrawerLayout;
    private ListView mDrawerList;
    private ActionBarDrawerToggle mDrawerToggle;
    private CharSequence mDrawerTitle;
    private CharSequence mTitle;
    public static final String MY_PREFS_NAME = "MyPrefs";
    List<DrawerItem> dataList;
	SharedPreferences prefs;
	SharedPreferences.Editor editor;
	CustomDrawerAdapter adapter;
	String type;
    
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        
        
        
        dataList = new ArrayList<DrawerItem>();
        mTitle = mDrawerTitle = getTitle();
        mDrawerLayout = (DrawerLayout) findViewById(R.id.drawer_layout);
        mDrawerList = (ListView) findViewById(R.id.left_drawer);

        mDrawerLayout.setDrawerShadow(R.drawable.drawer_shadow, GravityCompat.START);
        
        dataList.add(new DrawerItem("MyCloset"));
        dataList.add(new DrawerItem("Likes"));
        dataList.add(new DrawerItem("Tops"));
        dataList.add(new DrawerItem("Bottoms"));
        
        adapter = new CustomDrawerAdapter(this, R.layout.custom_drawer_item, dataList);

        mDrawerList.setAdapter(adapter);
        mDrawerList.setOnItemClickListener(new DrawerItemClickListener());
        
        getActionBar().setDisplayHomeAsUpEnabled(true);
        getActionBar().setHomeButtonEnabled(true);
        getActionBar().setBackgroundDrawable(getResources().getDrawable(R.drawable.ocean));
         
        mDrawerToggle = new ActionBarDrawerToggle(this, mDrawerLayout,
                    R.drawable.ic_drawer_white, R.string.drawer_open,
                    R.string.drawer_close) {
              public void onDrawerClosed(View view) {
                    getActionBar().setTitle(mTitle);
                    invalidateOptionsMenu(); // creates call to onPrepareOptionsMenu()
              }
         
              public void onDrawerOpened(View drawerView) {
                    getActionBar().setTitle(mDrawerTitle);
                    invalidateOptionsMenu(); // creates call to onPrepareOptionsMenu()
              }
        };
         
        mDrawerLayout.setDrawerListener(mDrawerToggle);
         
        if (savedInstanceState == null) {
              SelectItem(0);
        }
    }

    public void SelectItem(int possition) {
        Fragment fragment = null;
        Bundle args = new Bundle();
        switch (possition) {
        case 0:
              fragment = new MyCloset();
              args.putString(MyCloset.ITEM_NAME, dataList.get(possition)
                          .getItemName());
              break;
        case 1:
              fragment = new Likes();
              args.putString(Likes.ITEM_NAME, dataList.get(possition)
                          .getItemName());
              break;
        case 2:
        	  fragment = new ArticleList(); // this is for Tops
              args.putString(ArticleList.ITEM_NAME, dataList.get(possition)
                        .getItemName());
              break;
        case 3:
        	  fragment = new ArticleList(); // this is for Bottoms
              args.putString(ArticleList.ITEM_NAME, dataList.get(possition)
                        .getItemName());
              break;
        default:
              break;
        }

        // change fragment
        fragment.setArguments(args);
        FragmentManager frgManager = getFragmentManager();
        frgManager.beginTransaction().replace(R.id.content_frame, fragment).commit();
        // set fragment title
        mDrawerList.setItemChecked(possition, true);
        setTitle(dataList.get(possition).getItemName());
        mDrawerLayout.closeDrawer(mDrawerList);
        // used to reset action bar menu
    }

    @Override
    public void setTitle(CharSequence title) {
          mTitle = title;
          getActionBar().setTitle(mTitle);
    }
     
    @Override
    protected void onPostCreate(Bundle savedInstanceState) {
          super.onPostCreate(savedInstanceState);
          // Sync the toggle state after onRestoreInstanceState has occurred.
          mDrawerToggle.syncState();
    }
     
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
          // The action bar home/up action should open or close the drawer.
          // ActionBarDrawerToggle will take care of this.
          if (mDrawerToggle.onOptionsItemSelected(item)) {
                return true;
          }
          return MenuChoice(item);
          //return false;
    }
     
    @Override
    public void onConfigurationChanged(Configuration newConfig) {
          super.onConfigurationChanged(newConfig);
          // Pass any configuration change to the drawer toggles
          mDrawerToggle.onConfigurationChanged(newConfig);
    }
    
    private class DrawerItemClickListener implements ListView.OnItemClickListener {
    	@Override
    	public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
    		// change fragment based on selected item
    		SelectItem(position);
    		// write data to shared prefs for creating new articles
    		if (position == 2) {
    			type = "Tops";
    		} else if (position == 3) {
    			type = "Bottoms";
    		} else if (position == 0) {
    			type = "Outfits";
    		} else {
    			type = null;
    		}
    		prefs = getSharedPreferences(MY_PREFS_NAME, MODE_PRIVATE);
    		editor = prefs.edit();
    		editor.putString("Type", type);
			editor.commit();
    	}
    }
    
    public boolean onCreateOptionsMenu(Menu menu) {
    	CreateMenu(menu);
		return true;
    }
    
    private boolean MenuChoice(MenuItem item) {
    	prefs = getSharedPreferences(MY_PREFS_NAME, MODE_PRIVATE);
    	int position = 0;
    	boolean result = false;
    	String top;
    	String bottom;
    	switch(item.getItemId()){
    	case 0: // this is menu item 1 for adding tops or bottoms
    		//store in shared prefs the type of article we are creating
  	      	type = prefs.getString("Type", null);
  	      	String itemName = null;
  	      	if (type.equals("Tops") || type.equals("Bottoms")) {
  	      		// determine which of the two we are creating
  	      		if (type.equals("Tops")) {
  	      			position = 2;
  	      			itemName = new String(ArticleDetail.ITEM_NAME);
  	      		} else if (type.equals("Bottoms")) {
  	      			position = 3;
  	      			itemName = new String(ArticleDetail.ITEM_NAME);
  	      		}
  	      		// open the database to create a new article record
  	  	      	DBAdapter dbHelper = new DBAdapter(this);
  	  	      	dbHelper.open();
  	  	      	
  	  	      	Long userId = prefs.getLong("UserId", 0);
  	  	      	Long articleId = dbHelper.insertArticle(userId, null, null, null, null, type);
  	  	      	// store the article Id to be used by the next fragment (used in uploading imageView)
  	  	      	if (articleId > 0){
  	  	      		editor = prefs.edit();
  					editor.putLong("ArticleId", articleId);
  					editor.commit();
  	  	      	}
  	  	        dbHelper.close();
  	  	      	// call the new article fragment
  	  	      	Fragment fragment = new ArticleDetail();
  	  	      	Bundle args = new Bundle();
  	  	      	args.putString(itemName, dataList.get(position).getItemName());
  	  	        fragment.setArguments(args);
  	  	      	FragmentManager frgManager = getFragmentManager();
  	  	      	frgManager.beginTransaction().replace(R.id.content_frame, fragment).commit();   
  	      	}
  	      	return true;
    	case 1: // this is menu item 1 to dislike an outfit
    			top = prefs.getString("iTP", null);
    			bottom = prefs.getString("iBP", null);
    			if (top != null && bottom != null) {
    				DBAdapter dbHelper = new DBAdapter(this);
    				dbHelper.open();
    				result = dbHelper.dislikeOutfit(top, bottom);
    				if (result) {
    					// re-generate outfits
    					Toast.makeText(getApplicationContext(), "You DISLIKED this outfit", Toast.LENGTH_SHORT).show();
    				}
    				dbHelper.close();
    			}
    		return true;
    	case 2: // this is menu item 2 to like an outfit
    			top = prefs.getString("iTP", null);
    			bottom = prefs.getString("iBP", null);
    			if (top != null && bottom != null) {
    				DBAdapter dbHelper = new DBAdapter(this);
    				dbHelper.open();
    				result = dbHelper.likeOutfit(top, bottom);
    				if (result) {
    					// re-generate outfits
    					Toast.makeText(getApplicationContext(), "You LIKED this outfit", Toast.LENGTH_SHORT).show();
    				}
    				dbHelper.close();
    			}
    		return true;
    	}    	
    	return false;
    }
    
    private void CreateMenu(Menu menu) {
    	prefs = getSharedPreferences(MY_PREFS_NAME, MODE_PRIVATE);
    	type = prefs.getString("Type", null);
    	if (type == null) {
    	} else if (type.equals("Tops") || type.equals("Bottoms")) {
    		MenuItem mnu = menu.add(0, 0, 0, "Add New");
    	    {
    	    	mnu.setIcon(R.drawable.add);
    	    	mnu.setShowAsAction(MenuItem.SHOW_AS_ACTION_IF_ROOM);
    	    }
    	} else if (type.equals("Outfits")) {
    		MenuItem mnuLike = menu.add(0, 1, 0, "Like");
    		{
    			mnuLike.setIcon(R.drawable.up);
    			mnuLike.setShowAsAction(MenuItem.SHOW_AS_ACTION_IF_ROOM);
    		}
    		MenuItem mnuDislike = menu.add(0, 2, 1, "Dislike");
    		{
    			mnuDislike.setIcon(R.drawable.down);
    			mnuDislike.setShowAsAction(MenuItem.SHOW_AS_ACTION_IF_ROOM);
    		}
    	}
	}
}
