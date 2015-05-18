package cs4521.mmartinez.navdrawer;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;
import android.annotation.SuppressLint;
import android.app.Fragment;
import android.app.FragmentManager;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.provider.MediaStore.Images;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

@SuppressLint("SimpleDateFormat") public class ArticleDetail extends Fragment {
	TextView tvItemName;
	EditText description;
	ImageView imageView;
	Spinner spinnerCategory;
	Spinner spinnerColor;
	Spinner spinnerMaterial;
    String mCurrentPhotoPath;
    String path;
    byte[] fullSize;
    byte[] thumbSize;
    File photoFile = null;
	SharedPreferences prefs;
	SharedPreferences.Editor editor;
	public static final String MY_PREFS_NAME = "MyPrefs";
	public static final String ITEM_NAME = "itemName";
	static final int REQUEST_IMAGE_CAPTURE = 1;
	static final int REQUEST_TAKE_PHOTO = 1;
	
	public ArticleDetail() {
    	 
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                Bundle savedInstanceState) {

          View view = inflater.inflate(R.layout.fragment_article_detail, container, false);
          tvItemName = (TextView) view.findViewById(R.id.article_text);
          tvItemName.setText(getArguments().getString(ITEM_NAME));
          description = (EditText) view.findViewById(R.id.article_name);
          spinnerCategory = (Spinner) view.findViewById(R.id.article_category);
          spinnerColor = (Spinner) view.findViewById(R.id.article_color);
          spinnerMaterial = (Spinner) view.findViewById(R.id.article_material);
          
          // Create an ArrayAdapter using the string array and a default spinner layout
          ArrayAdapter<CharSequence> adapterCategory = ArrayAdapter.createFromResource(getActivity(),
               R.array.category_array, android.R.layout.simple_spinner_item);
          ArrayAdapter<CharSequence> adapterColor = ArrayAdapter.createFromResource(getActivity(),
                  R.array.color_array, android.R.layout.simple_spinner_item);
          ArrayAdapter<CharSequence> adapterMaterial = ArrayAdapter.createFromResource(getActivity(),
                  R.array.material_array, android.R.layout.simple_spinner_item);
          
          // Specify the layout to use when the list of choices appears
          adapterCategory.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
          adapterColor.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
          adapterMaterial.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
          
          // Apply the adapter to the spinner
          spinnerCategory.setAdapter(adapterCategory);
          spinnerColor.setAdapter(adapterColor);
          spinnerMaterial.setAdapter(adapterMaterial);
          
          // set ImageView listener
          imageView = (ImageView) view.findViewById(R.id.article_img);
          imageView.setOnClickListener(new OnClickListener() {
        	  public void onClick(View v) {
        		  dispatchTakePictureIntent();
        	  }
          });
          
          // set save button listener
          Button btnSave = (Button) view.findViewById(R.id.article_btn_save);
          btnSave.setOnClickListener(new OnClickListener() {
        	  public void onClick(View v) {
        		  // grab values from shared prefs file
        		  prefs = getActivity().getSharedPreferences(MY_PREFS_NAME, 0);
          	      Long articleId = prefs.getLong("ArticleId", 0);
                  String type = prefs.getString("Type", null);
                  String path = prefs.getString("thumbPath", null);
                  
                  // grab values from input fields
        		  String name = description.getText().toString();
        		  String category = spinnerCategory.getSelectedItem().toString();
        		  String color = spinnerColor.getSelectedItem().toString();
        		  String material = spinnerMaterial.getSelectedItem().toString();
        		  
        		  // open the database to update the record
        		  DBAdapter dbHelper = new DBAdapter(getActivity());
        		  dbHelper.open();
        		  boolean result = dbHelper.updateArticle(articleId, name, color, category, material, type, path, thumbSize, fullSize);
        		  //boolean result = dbHelper.updateArticle(articleId, name, color, category, material, type, path);
        		  if (result){
        			  Toast.makeText(getActivity(), type + " created.", Toast.LENGTH_SHORT).show();
        			  // clear the imageView paths
        			  editor = prefs.edit();
        			  editor.putString("Type", type);
        			  editor.putString("thumbPath", null);
        			  editor.putString("imagePath", null);
        			  editor.commit();
        			  // call the article list fragment
        	  	      Fragment fragment = new ArticleList();
        	  	      Bundle args = new Bundle();
        	  	      args.putString(ArticleList.ITEM_NAME, type);
        	  	      fragment.setArguments(args);
        	  	      FragmentManager frgManager = getFragmentManager();
        	  	      frgManager.beginTransaction().replace(R.id.content_frame, fragment).commit();
        		  }
        		  dbHelper.close();
        	  }
          });
          
          // set cancel button listener
          Button btnCancel = (Button) view.findViewById(R.id.article_btn_cancel);
          btnCancel.setOnClickListener(new OnClickListener() {
        	  public void onClick(View v) {
        		  // grab the rowId to delete
        		  prefs = getActivity().getSharedPreferences(MY_PREFS_NAME, 0);
          	      Long articleId = prefs.getLong("ArticleId", 0);
                  String type = prefs.getString("Type", null);
          	      // open the database to remove the record
        		  DBAdapter dbHelper = new DBAdapter(getActivity());
        		  dbHelper.open();
        		  boolean result = dbHelper.deleteArticle(articleId);
        		  if (result) {
        			  Toast.makeText(getActivity(), "article was deleted", Toast.LENGTH_SHORT).show();
        			  // clear the imageView paths
        			  editor = prefs.edit();
        			  editor.putString("Type", type);
        			  editor.putString("thumbPath", null);
        			  editor.putString("imagePath", null);
        			  editor.commit();
        			  // call the article list fragment
        	  	      Fragment fragment = new ArticleList();
        	  	      Bundle args = new Bundle();
        	  	      args.putString(ArticleList.ITEM_NAME, type);
        	  	      fragment.setArguments(args);
        	  	      FragmentManager frgManager = getFragmentManager();
        	  	      frgManager.beginTransaction().replace(R.id.content_frame, fragment).commit();
        		  }
        		  dbHelper.close();
        	  }
          });
          
          return view;
    }

    private void dispatchTakePictureIntent() {
        Intent takePictureIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        // Ensure that there's a camera activity to handle the intent
        if (takePictureIntent.resolveActivity(getActivity().getPackageManager()) != null) {
            // Create the File where the photo should go
            // File photoFile = null;
            try {
                photoFile = createImageFile();
            } catch (IOException ex) {
            	System.out.println("Error creating imageView file");
            }
            // Continue only if the File was successfully created
            if (photoFile != null) {
                takePictureIntent.putExtra(MediaStore.EXTRA_OUTPUT,
                        Uri.fromFile(photoFile));
                startActivityForResult(takePictureIntent, REQUEST_TAKE_PHOTO);
            }
        }
    }
    
    // Get the picture that was just taken
    @SuppressWarnings("static-access")
	@Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == REQUEST_IMAGE_CAPTURE && resultCode == getActivity().RESULT_OK) {
            setPic();
            storePrefs();
        }
    }

    private File createImageFile() throws IOException {
        // Create an imageView file name
        String timeStamp = new SimpleDateFormat("yyyyMMdd_HHmmss").format(new Date());
        String imageFileName = "JPEG_" + timeStamp + "_";
        File storageDir = Environment.getExternalStoragePublicDirectory(
                Environment.DIRECTORY_PICTURES);
        File image = File.createTempFile(
            imageFileName,  // prefix
            ".jpg",         // sufix
            storageDir      // directory
        );
        // Save a file: path for use with ACTION_VIEW intents
        mCurrentPhotoPath = "file:" + image.getAbsolutePath();
        path = image.getAbsolutePath();
        return image;
    }
    
    @SuppressWarnings("static-access")
	private void setPic() {
        // Get the dimensions of the View
        int targetW = imageView.getWidth();
        int targetH = imageView.getHeight();

        // Get the dimensions of the bitmap
        BitmapFactory.Options bmOptions = new BitmapFactory.Options();
        bmOptions.inJustDecodeBounds = true;
        BitmapFactory.decodeFile(path, bmOptions);
        int photoW = bmOptions.outWidth;
        int photoH = bmOptions.outHeight;

        // Determine how much to scale down the image
        int scaleFactor = 0;
        if (targetW !=0 && targetH !=0)
        	scaleFactor = Math.min(photoW/targetW, photoH/targetH);

        // Decode the image file into a Bitmap sized to fill the View
        bmOptions.inJustDecodeBounds = false;
        bmOptions.inSampleSize = scaleFactor;
        bmOptions.inPurgeable = true;

        Bitmap bitmap = BitmapFactory.decodeFile(path, bmOptions);
        imageView.setImageBitmap(bitmap);

        Uri imageUri = getImageUri(getActivity(), bitmap);
        path = getRealPathFromURI(imageUri);
        
        Utilities utilHelper = new Utilities();
        thumbSize = utilHelper.getBytesThumb(photoFile);
        fullSize = utilHelper.getBytesImage(bitmap);
        
    }
    
    private void storePrefs() {
    	prefs = getActivity().getSharedPreferences(MY_PREFS_NAME, 0);
		editor = prefs.edit();
		editor.putString("thumbPath", path);
		editor.commit();
    }
    
    public Uri getImageUri(Context inContext, Bitmap inImage) {
        ByteArrayOutputStream bytes = new ByteArrayOutputStream();
        inImage.compress(Bitmap.CompressFormat.JPEG, 100, bytes);
        String path = Images.Media.insertImage(inContext.getContentResolver(), inImage, "Title", null);
        return Uri.parse(path);
    }

    public String getRealPathFromURI(Uri uri) {
        Cursor cursor = getActivity().getContentResolver().query(uri, null, null, null, null); 
        cursor.moveToFirst(); 
        int idx = cursor.getColumnIndex(MediaStore.Images.ImageColumns.DATA); 
        String result = cursor.getString(idx);
        cursor.close();
        return result;
    }
    
}
