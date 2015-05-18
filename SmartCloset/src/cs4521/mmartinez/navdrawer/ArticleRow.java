package cs4521.mmartinez.navdrawer;

import android.app.Fragment;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
 
public class ArticleRow extends Fragment {
 
      TextView tvItemName;
      public static final String ITEM_NAME = "itemName";
      
      public ArticleRow() {
    	  // empty constructor needed for fragments
      }
 
      @Override
      public View onCreateView(LayoutInflater inflater, ViewGroup container,
                  Bundle savedInstanceState) { 
            View view = inflater.inflate(R.layout.fragment_article_row, container, false);
            return view;
      }

}