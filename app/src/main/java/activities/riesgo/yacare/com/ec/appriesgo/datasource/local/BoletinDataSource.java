package activities.riesgo.yacare.com.ec.appriesgo.datasource.local;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;

import java.util.ArrayList;

import activities.riesgo.yacare.com.ec.appriesgo.app.RiesgoLocalSqlLite;
import activities.riesgo.yacare.com.ec.appriesgo.dto.Boletin;

public class BoletinDataSource {

	public static final String TABLA_BOLETIN = "boletin";
	public static final String COLUMN_BOLETIN_ID = "idboletin";
	public static final String COLUMN_BOLETIN_TITULO = "titulo";
	public static final String COLUMN_BOLETIN_URL = "url";
	public static final String COLUMN_BOLETIN_LEYENDA = "leyenda";
	public static final String COLUMN_BOLETIN_FECHA = "fecha";
	public static final String COLUMN_BOLETIN_ID_RIESGO = "idriesgo";


	  private SQLiteDatabase database;
	  private RiesgoLocalSqlLite dbHelper;
	  private String[] allColumns = { COLUMN_BOLETIN_ID,
			  COLUMN_BOLETIN_TITULO,
			  COLUMN_BOLETIN_URL,
			  COLUMN_BOLETIN_LEYENDA,
			  COLUMN_BOLETIN_FECHA,
			  COLUMN_BOLETIN_ID_RIESGO};

	  public BoletinDataSource(Context context) {
	    dbHelper = new RiesgoLocalSqlLite(context);
	  }

	  public void open() throws SQLException {
	    database = dbHelper.getWritableDatabase();
	  }

	  public void close() {
	    dbHelper.close();
	  }

	public Boletin createBoletin(Boletin boletin) {
		ContentValues values = new ContentValues();
		values.put(COLUMN_BOLETIN_ID, boletin.getId());
		values.put(COLUMN_BOLETIN_TITULO, boletin.getTitulo());
		values.put(COLUMN_BOLETIN_URL, boletin.getUrl());
		values.put(COLUMN_BOLETIN_LEYENDA, boletin.getLeyenda());
		values.put(COLUMN_BOLETIN_FECHA, boletin.getFecha());
		values.put(COLUMN_BOLETIN_ID_RIESGO, boletin.getIdRiesgo());


		database.insert(TABLA_BOLETIN, null, values);
		Cursor cursor = database.query(TABLA_BOLETIN,
				allColumns, COLUMN_BOLETIN_ID + " = '" + boletin.getId() + "'"  , null,
				null, null, null);
		cursor.moveToFirst();
		Boletin newBoletin = cursorToBoletin(cursor);
		cursor.close();
		return newBoletin;
	}
	  public ArrayList<Boletin> getBoletinByRiesgo(String idRiesgo) {
	    ArrayList<Boletin> boletines = new ArrayList<Boletin>();

	    Cursor cursor = database.query(TABLA_BOLETIN,
	        allColumns, COLUMN_BOLETIN_ID_RIESGO + " = '" + idRiesgo + "'", null, null, null,  COLUMN_BOLETIN_FECHA + " DESC",  0 + "," + 150);

	    cursor.moveToFirst();
	    while (!cursor.isAfterLast()) {
	      Boletin boletin = cursorToBoletin(cursor);
			boletines.add(boletin);
	      cursor.moveToNext();
	    }
	    // make sure to close the cursor
	    cursor.close();
	    return boletines;
	  }

	public Boletin getBoletinByRiesgoId(String idRiesgo, String id) {
		Boletin boletin = new Boletin();

		Cursor cursor = database.query(TABLA_BOLETIN,
				allColumns, COLUMN_BOLETIN_ID_RIESGO + " = '" + idRiesgo  + "' and " +
						COLUMN_BOLETIN_ID + " = '" +id + "'", null, null, null,  null,  null);

		if(cursor.getCount() > 0){
			cursor.moveToFirst();
			boletin = cursorToBoletin(cursor);
			cursor.close();
			return boletin;
		}else{
			cursor.close();
			return null;
		}
	}

	public void deleteBoletin(String idRiesgo, String id) {
		System.out.println("deleteContacto with id: " + idRiesgo);
		database.delete(TABLA_BOLETIN, COLUMN_BOLETIN_ID_RIESGO + " = '" + idRiesgo  + "' and " +
				COLUMN_BOLETIN_ID + " = '" +id + "'", null);
	}

	public Integer getMaxBoletinByRiesgo(String idRiesgo) {
		Integer id = 0;
		 String[] maximo = { "MAX(CAST(idboletin as integer))"};
		Cursor cursor = database.query(TABLA_BOLETIN, maximo, COLUMN_BOLETIN_ID_RIESGO + " = '" + idRiesgo + "'", null, null, null, null);
		if (cursor != null) {
			cursor.moveToFirst();
			id= cursor.getInt(0);
		}
		return id;
	}

	  private Boletin cursorToBoletin(Cursor cursor) {
	    Boletin boletin = new Boletin();
		  boletin.setId(cursor.getString(0));
		  boletin.setTitulo(cursor.getString(1));
		  boletin.setUrl(cursor.getString(2));
		  boletin.setLeyenda(cursor.getString(3));
		  boletin.setFecha(cursor.getString(4));
		  boletin.setIdRiesgo(cursor.getString(5));
	    return boletin;
	  }


}
