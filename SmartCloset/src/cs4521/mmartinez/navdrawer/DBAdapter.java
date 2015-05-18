package cs4521.mmartinez.navdrawer;


import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

public class DBAdapter {
	// database info
	static final String TAG = "DBAdapter";
	static final String DATABASE_NAME = "MyDB";
	static final String KEY_ROWID = "_id";
	// user table
	static final String USER_TABLE = "user";
	static final String KEY_EMAIL = "email";
	static final String KEY_PASSWORD = "password";
	// article table
	static final String ARTICLE_TABLE = "article";
	static final String KEY_FKUSER = "idUser";
	static final String KEY_CATEGORY = "category";
	static final String KEY_COLOR = "color";
	static final String KEY_DESCRIPTION = "name";
	static final String KEY_MATERIAL = "material";
	static final String KEY_PATH = "path";
	static final String KEY_TYPE = "type";
	static final String KEY_THUMB = "thumb";
	static final String KEY_PHOTO = "photo";
	// outfit table
	static final String OUTFIT_TABLE = "outfit";
	static final String KEY_TPATH = "tPath";
	static final String KEY_BPATH = "bPath";
	static final String KEY_FLIKE = "fLike";
	static final String KEY_FDISLIKE = "fDislike";
	// globals for queries
	static final String TYPE_BOTTOMS = "Bottoms";
	static final String TYPE_TOPS = "Tops";
	// create tables
	static final String USER_TABLE_CREATE = "create table user (_id integer primary key autoincrement, "
			+ "password text not null, email text not null); ";
	static final String ARTICLE_TABLE_CREATE = "create table 'article' ('_id' integer not null primary key autoincrement, "
			+ "'idUser' integer not null, 'name' text, 'color' text, 'category' text, 'material' text, 'type' text, 'path' text, 'thumb' blob, 'photo' blob);";
	static final String OUTFIT_TABLE_CREATE = "create table outfit (_id integer primary key autoincrement, "
			+ "idUser integer not null, tPath text not null, bPath text not null, fLike integer, fDislike integer); ";
	static final int DATABASE_VERSION = 3;
	public static final String MY_PREFS_NAME = "MyPrefs";
	final Context context;
	DatabaseHelper DBHelper;
	SQLiteDatabase db;
	
	public DBAdapter(Context ctx) {
		this.context = ctx;
		DBHelper = new DatabaseHelper(context);
		// -- enable below to update database --
		//db = DBHelper.getWritableDatabase();
		//DBHelper.onUpgrade(db, 2, 3);
		//db.execSQL(ARTICLE_TABLE_CREATE);
		//db.execSQL(OUTFIT_TABLE_CREATE);
	}
	
	private static class DatabaseHelper extends SQLiteOpenHelper {
		DatabaseHelper(Context context) {
			super(context, DATABASE_NAME, null, DATABASE_VERSION);			
		}
		
		@Override
		public void onCreate(SQLiteDatabase db) {
			try {
				db.execSQL(USER_TABLE_CREATE);
				db.execSQL(ARTICLE_TABLE_CREATE);
				db.execSQL(OUTFIT_TABLE_CREATE);
			}
			catch (SQLException e) {
				e.printStackTrace();
			}
		}
		
		@Override
		public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
			Log.w(TAG, "Upgrading database from version " + oldVersion + " to " + newVersion + ", which will destroy all old data");
			db.execSQL("DROP TABLE IF EXISTS user");
			db.execSQL("DROP TABLE IF EXISTS article");
			onCreate(db);
		}
	}
	
	// Opens the database
	public DBAdapter open() throws SQLException {
		db = DBHelper.getWritableDatabase();
		return this;
	}
	
	// Closes the database
	public void close() {
		DBHelper.close();
	}
	
	/**
	 * User Utilities
	 */
	// Insert a User into the database
	public long insertUser(String email, String password) {
		ContentValues initialValues = new ContentValues();
		initialValues.put(KEY_EMAIL, email);
		initialValues.put(KEY_PASSWORD, password);
		return db.insert(USER_TABLE, null, initialValues);
	}
	
	// Retrieves all users
	public Cursor getAllUsers() {
		return db.query(USER_TABLE, new String[] {KEY_ROWID, KEY_PASSWORD, KEY_EMAIL}, null, null, null, null, null);
	}
	
	// Retrieves a particular User
	public Cursor getUser(long rowId) throws SQLException {
		Cursor mCursor = db.query(true, USER_TABLE, new String[] {KEY_ROWID, KEY_EMAIL, KEY_EMAIL}, KEY_ROWID + "=" + rowId, null, null, null, null, null);
		if (mCursor!= null) {
			mCursor.moveToFirst();
		}
		return mCursor;
	}
	
	public Cursor getUser(String email, String password) throws SQLException {
		Cursor mCursor = db.query(true, USER_TABLE, new String[] {KEY_EMAIL,  KEY_PASSWORD}, KEY_EMAIL + "=" + email, null, null, null, null, null);
		if (mCursor!=null){
			mCursor.moveToFirst();
		}
		return mCursor;
	}
	
	// Updates a User
	public boolean updateUser(long rowId, String password, String email) {
		ContentValues args = new ContentValues();
		args.put(KEY_PASSWORD, password);
		args.put(KEY_EMAIL, email);
		return db.update(USER_TABLE, args, KEY_ROWID + "=" + rowId, null) > 0;
	}
	
	/**
	 * Article Utilities
	 */
	public long insertArticle(long userId, String name, String color, String category, String material, String type) {
		ContentValues initialValues = new ContentValues();
		initialValues.put(KEY_FKUSER, userId);
		initialValues.put(KEY_DESCRIPTION, name);
		initialValues.put(KEY_COLOR, color);
		initialValues.put(KEY_CATEGORY, category);
		initialValues.put(KEY_MATERIAL, material);
		initialValues.put(KEY_TYPE, type);
		return db.insert(ARTICLE_TABLE, null, initialValues);
	}
	
	// Retrieves a particular Article
	public Cursor getArticle(long rowId) throws SQLException {
		Cursor mCursor = db.query(true, ARTICLE_TABLE, new String[] {KEY_ROWID, KEY_DESCRIPTION, KEY_CATEGORY, KEY_MATERIAL, KEY_COLOR}, KEY_ROWID + "=" + rowId, null, null, null, null, null);
		if (mCursor!= null) {
			mCursor.moveToFirst();
		}
		return mCursor;
	}	
	
	// Get all articles for a particular user
	public Cursor getAllUsersArticles(long userId) {
		return db.query(ARTICLE_TABLE, new String[] {KEY_ROWID, KEY_DESCRIPTION, KEY_CATEGORY, KEY_MATERIAL, KEY_COLOR}, KEY_FKUSER + "=" + userId, null, null, null, null, null);
	}
	
	// Get all tops for a particular user
	public Cursor getAllUsersTops(long userId) {
		return db.query(ARTICLE_TABLE, new String[] {KEY_ROWID, KEY_DESCRIPTION, KEY_CATEGORY, KEY_MATERIAL, KEY_COLOR, KEY_PATH}, KEY_FKUSER + "=" + userId + " and " + KEY_PATH + " is not null and " + KEY_TYPE + "= '" + TYPE_TOPS + "'" , null, null, null, null, null);
	}
	
	// Get all bottoms for a particular user
	public Cursor getAllUsersBottoms(long userId) {
		return db.query(ARTICLE_TABLE, new String[] {KEY_ROWID, KEY_DESCRIPTION, KEY_CATEGORY, KEY_MATERIAL, KEY_COLOR, KEY_PATH}, KEY_FKUSER + "=" + userId + " and " + KEY_PATH + " is not null and " + KEY_TYPE + "= '" + TYPE_BOTTOMS + "'", null, null, null, null, null);
	}
	
	// Get all bottoms for a particular user
	public Cursor getColorBottoms(String color) {
		return db.query(ARTICLE_TABLE, new String[] {KEY_ROWID, KEY_DESCRIPTION, KEY_CATEGORY, KEY_MATERIAL, KEY_COLOR, KEY_PATH}, KEY_COLOR + "= '" + color + "' AND " + KEY_TYPE + "= '" + TYPE_BOTTOMS + "'", null, null, null, null, null);
	}
	
	// Updates an Article
	public boolean updateArticle(long rowId, String name, String color, String category, String material, String type, String path) {
		ContentValues args = new ContentValues();
		args.put(KEY_DESCRIPTION, name);
		args.put(KEY_COLOR, color);
		args.put(KEY_CATEGORY, category);
		args.put(KEY_MATERIAL, material);
		args.put(KEY_TYPE, type);
		args.put(KEY_PATH, path);
		return db.update(ARTICLE_TABLE, args, KEY_ROWID + "=" + rowId, null) > 0;
	}
	
	// Updates an Article
	public boolean updateArticle(long rowId, String name, String color, String category, String material, String type, String path, byte[] thumb, byte[] photo) {
		ContentValues args = new ContentValues();
		args.put(KEY_DESCRIPTION, name);
		args.put(KEY_COLOR, color);
		args.put(KEY_CATEGORY, category);
		args.put(KEY_MATERIAL, material);
		args.put(KEY_TYPE, type);
		args.put(KEY_PATH, path);
		args.put(KEY_THUMB, thumb);
		args.put(KEY_PHOTO, photo);
		return db.update(ARTICLE_TABLE, args, KEY_ROWID + "=" + rowId, null) > 0;
	}
	
	//deletes a particular article, takes row id as param
	public boolean deleteArticle(long rowId) {
		return db.delete(ARTICLE_TABLE, KEY_ROWID + "=" + rowId, null) > 0;
	}
	
	//deletes a particular article, takes image path as param
	public boolean deleteArticle(String path) {
		return db.delete(ARTICLE_TABLE, KEY_PATH + " = '" + path + "'", null) > 0;
	}
	
	//deletes all articles with empty description
	public boolean deleteEmptyArticles() {
		return db.delete(ARTICLE_TABLE, KEY_DESCRIPTION + " is null or " + KEY_DESCRIPTION + "= ?" , new String[] {""}) > 0;
	}

	
	/**
	 * Outfit Utilities
	 */
	public long insertOutfit(long userId, String topP, String bottomP) {
		ContentValues initialValues = new ContentValues();
		initialValues.put(KEY_FKUSER, userId);
		initialValues.put(KEY_TPATH, topP);
		initialValues.put(KEY_BPATH, bottomP);
		return db.insert(OUTFIT_TABLE, null, initialValues);
	}
	
	// Retrieves a particular Outfit
	public Cursor getOutfit(long rowId) throws SQLException {
		Cursor mCursor = db.query(true, OUTFIT_TABLE, new String[] {KEY_ROWID, KEY_TPATH, KEY_BPATH}, KEY_ROWID + " = " + rowId, null, null, null, null, null);
		if (mCursor!= null) {
			mCursor.moveToFirst();
		}
		return mCursor;
	}
	
	// Retrieves a particular Outfit
	public Cursor getOutfit(String top, String bottom) throws SQLException {
		Cursor mCursor = db.query(true, OUTFIT_TABLE, new String[] {KEY_ROWID, KEY_FLIKE, KEY_FDISLIKE}, KEY_TPATH + " = '" + top + "' and " + KEY_BPATH + " = '" + bottom + "'", null, null, null, null, null);
		if (mCursor!= null) {
			mCursor.moveToFirst();
		}
		return mCursor;
	}
	
	// Retrieves Liked Outfits
	public Cursor getLikeOutfits() throws SQLException {
		Cursor mCursor = db.query(true, OUTFIT_TABLE, new String[] {KEY_ROWID, KEY_TPATH, KEY_BPATH}, KEY_FLIKE + " = " + 1, null, null, null, null, null);
		if (mCursor!= null) {
			mCursor.moveToFirst();
		}
		return mCursor;
	}

	// Retrieves DisLiked Outfits
	public Cursor getDislikeOutfits() throws SQLException {
		Cursor mCursor = db.query(true, OUTFIT_TABLE, new String[] {KEY_ROWID, KEY_TPATH, KEY_BPATH}, KEY_FDISLIKE + " = " + 1, null, null, null, null, null);
		if (mCursor!= null) {
			mCursor.moveToFirst();
		}
		return mCursor;
	}

	// Retrieves Unrated Outfits
	public Cursor getUnratedOutfits() throws SQLException {
		//Cursor mCursor = db.query(true, OUTFIT_TABLE, new String[] {KEY_ROWID, KEY_TPATH, KEY_BPATH}, KEY_FLIKE + " is null and " + KEY_FDISLIKE + " is null", null, null, null, KEY_ROWID + " DESC", null);
		Cursor mCursor = db.query(true, OUTFIT_TABLE, new String[] {KEY_ROWID, KEY_TPATH, KEY_BPATH}, KEY_FLIKE + " is null or " + KEY_FLIKE + " = 0 " + " and " + KEY_FDISLIKE + " is null or " + KEY_FDISLIKE + " = 0" , null, null, null, KEY_ROWID + " DESC", null);
		if (mCursor!= null) {
			mCursor.moveToFirst();
		}
		return mCursor;
	}

	// Flags an Outfit as Liked
	public boolean likeOutfit(String top, String bottom) {
		ContentValues args = new ContentValues();
		args.put(KEY_TPATH, top);
		args.put(KEY_BPATH, bottom);
		args.put(KEY_FLIKE, 1);
		return db.update(OUTFIT_TABLE, args, KEY_TPATH + " = '" + top + "' and " + KEY_BPATH + " = '" + bottom + "'", null) > 0;
	}	

	// Flags an Outfit as Liked
	public boolean dislikeOutfit(String top, String bottom) {
		ContentValues args = new ContentValues();
		args.put(KEY_TPATH, top);
		args.put(KEY_BPATH, bottom);
		args.put(KEY_FDISLIKE, 1);
		return db.update(OUTFIT_TABLE, args, KEY_TPATH + " = '" + top + "' and " + KEY_BPATH + " = '" + bottom + "'", null) > 0;
	}	
	
	// Updates an Outfit - used to clear flags so the images can be reused for demo
	public boolean updateOutfits() {
		ContentValues args = new ContentValues();
		args.put(KEY_FLIKE, 0);
		args.put(KEY_FDISLIKE, 0);
		return db.update(OUTFIT_TABLE, args, null, null) > 0;
	}

}




























