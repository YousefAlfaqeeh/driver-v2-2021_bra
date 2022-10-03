package database;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;

import com.readystatesoftware.sqliteasset.SQLiteAssetHelper;


public class OpenHelper extends SQLiteAssetHelper {


	public static final String DATABASE_NAME = "LocationDataV3.sqlite";

	private static final int DATABASE_VERSION = 1;

	private OpenHelper(Context context) {
//		super(context, DATABASE_NAME, context.getExternalFilesDir(null).getAbsolutePath(), null, DATABASE_VERSION);
		super(context, DATABASE_NAME, null, DATABASE_VERSION);
		Log.e("Database", "current version: " + getWritableDatabase().getVersion());
		setForcedUpgrade();

	}

	private OpenHelper(Context context, String absolute_path, String database_name) {
		super(context, database_name, absolute_path, null, DATABASE_VERSION);
		Log.e("Database", "current version: " + getWritableDatabase().getVersion());
		setForcedUpgrade();
	}

	public static SQLiteDatabase getDatabase(Context context, String absolute_path, String database_name) {
		return new OpenHelper(context, absolute_path, database_name).getWritableDatabase();
	}

	public static SQLiteDatabase getDatabase(Context context) {
		return new OpenHelper(context).getWritableDatabase();
	}


}
