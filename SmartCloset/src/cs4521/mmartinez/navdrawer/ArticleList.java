package cs4521.mmartinez.navdrawer;

import android.app.AlertDialog;
import android.app.Fragment;
import android.content.Context;
import android.content.DialogInterface;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.CursorAdapter;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;
import android.database.Cursor;
 
public class ArticleList extends Fragment {
	  ArticleCursorAdapter cursorAdapter;
      TextView tvItemName;
      ListView listView;
      private DBAdapter dbHelper;
      public static final String ITEM_NAME = "itemName";
      public static final String MY_PREFS_NAME = "MyPrefs";
      
      public ArticleList() {
 
      }
      
      @Override
      public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
            View view = inflater.inflate(R.layout.fragment_article_list, container, false);
            listView = (ListView) view.findViewById(R.id.article_list);
            
            // open the db to get the data
            dbHelper = new DBAdapter(view.getContext());
    		dbHelper.open();
    		
    		// get the userId and current type from the shared prefs file
			Context context = view.getContext();
			SharedPreferences prefs = context.getSharedPreferences(MY_PREFS_NAME, 0);
			final Long userId = prefs.getLong("UserId", 0);
			String type = prefs.getString("Type", null);
			Cursor data = null;

			// determine which data to fetch
			if (type.equals("Tops")) {
				data = dbHelper.getAllUsersTops(userId);
				//boolean result = dbHelper.deleteEmptyArticles();
				//result = dbHelper.deleteTopsArticles();
			} 
			else if (type.equals("Bottoms")) {
				data = dbHelper.getAllUsersBottoms(userId);
				//-- enable lines below to delete records --
				//boolean result = dbHelper.deleteBottomsArticles();
				//result = dbHelper.deleteEmptyArticles();
				//Toast.makeText(getActivity(), "data deleted", Toast.LENGTH_SHORT).show();
			}
			if (data.moveToFirst()) {
				cursorAdapter = new ArticleCursorAdapter(context, data);
	            listView.setAdapter(cursorAdapter);
			}
			
			// set onLongClick to allow deletion of article items
			listView.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
				@Override
				public boolean onItemLongClick(AdapterView<?> parent,
						final View view, int position, long id) {
					// display alert dialog to delete this item
	        		  new AlertDialog.Builder(getActivity())
	        		  .setTitle("Delete Article")
	        		  .setMessage("Are you sure you want to delete this entry?")
	        		  .setPositiveButton(android.R.string.yes, new DialogInterface.OnClickListener() {
	        			  public void onClick(DialogInterface dialog, int which) {
	        				  // continue with delete
	        				  TextView tv = (TextView) view.findViewById(R.id.path_row);
	        				  String path = tv.getText().toString();
	        				  if (path != null) {
	        					  
	        					  if (!dbHelper.db.isOpen())
	        						  dbHelper.open();
	        					  System.out.println("path: " + path);
	        					  boolean result = dbHelper.deleteArticle(path);
	        					  if (result) {
	        						  Cursor updated = null;
	        						  SharedPreferences sprefs = getActivity().getSharedPreferences(MY_PREFS_NAME, 0);
        							  String type = sprefs.getString("Type", null);
        							  
        							  if (type.equals("Tops"))
        								updated = dbHelper.getAllUsersTops(userId);
        							  else if (type.equals("Bottoms"))
        							  	updated = dbHelper.getAllUsersBottoms(userId);
        							  	
	        						  if (updated.moveToFirst())
	        							  cursorAdapter.changeCursor(updated);
	        						  
	        						  listView.invalidateViews();
	        	                      listView.refreshDrawableState();
	        	                      //updated.close();
	        					  }
	        					  else {
	        						  Toast.makeText(getActivity(), "There was an error deleting this entry, please contact the developer.", Toast.LENGTH_SHORT).show();
	        					  }
	        					  //dbHelper.close();  
	        				  }
	        			  }
					  })
					  .setNegativeButton(android.R.string.cancel, new DialogInterface.OnClickListener() {
						  public void onClick(DialogInterface dialog, int which) {
							  // do nothing
							  dialog.dismiss();
						  }
					  })
					  .setIcon(android.R.drawable.ic_dialog_alert)
					  .show();
	        		  return true;
				}
	        });
			// close the db
			dbHelper.close();
            return view;
      }
      
      public class ArticleCursorAdapter extends CursorAdapter {
    	public ArticleCursorAdapter(Context context, Cursor cursor) {
    		super(context, cursor, 0);
    	}

    	// inflate new view and return it
		@Override
		public View newView(Context context, Cursor cursor, ViewGroup parent) {
			return LayoutInflater.from(context).inflate(R.layout.fragment_article_row, parent, false);
		}

		// bind all data to given view
		@Override
		public void bindView(View view, Context context, Cursor cursor) {
			// get the view objects
			ImageView img = (ImageView) view.findViewById(R.id.thumbnail);
			TextView pa = (TextView) view.findViewById(R.id.path_row);
			TextView na = (TextView) view.findViewById(R.id.name_row);
			TextView cat = (TextView) view.findViewById(R.id.category_row);
			TextView mat = (TextView) view.findViewById(R.id.material_row);
			TextView col = (TextView) view.findViewById(R.id.color_row);
			// get the values
			String name = cursor.getString(1);
			String category = cursor.getString(2);
			String material = cursor.getString(3);					
			String color = cursor.getString(4);
			String path = cursor.getString(5);
			// set the values to the objects
			if (path != null) {
				img.setImageURI(Uri.parse(path));
			}
			pa.setText(path);
			na.setText(name);
			cat.setText(category);
			mat.setText(material);
			col.setText(color);
		}
      }
}