package cs4521.mmartinez.navdrawer;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Bitmap.CompressFormat;
 
public class Utilities {
	
	  // convert from bitmap to byte array
      public static byte[] getBytesImage(Bitmap bitmap) {
    	  ByteArrayOutputStream stream = new ByteArrayOutputStream();
    	  bitmap.compress(CompressFormat.PNG, 0, stream);
    	  return stream.toByteArray();
      }
      
      //convert from byte array to bitmap
      public static Bitmap getImage(byte[] image) {
    	  return BitmapFactory.decodeByteArray(image, 0, image.length);
      }
 
      public static byte[] getBytesThumb(File fileName) {
    	  byte[] imageData = null;
    	  try {
    		  final int THUMBNAIL_SIZE = 64;
    		  FileInputStream fis = new FileInputStream(fileName);
    		  Bitmap imageBitmap = BitmapFactory.decodeStream(fis);
    		  imageBitmap = Bitmap.createScaledBitmap(imageBitmap, THUMBNAIL_SIZE, THUMBNAIL_SIZE, false);
    		  ByteArrayOutputStream baos = new ByteArrayOutputStream();
    		  imageBitmap.compress(Bitmap.CompressFormat.JPEG, 100, baos);
    		  imageData = baos.toByteArray();
    	  } catch (Exception ex) {
    		  // do something
    	  }
    	  return imageData;
      } 
      
}