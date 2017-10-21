package activities.riesgo.yacare.com.ec.appriesgo.datasource;

import java.util.ArrayList;

import android.content.Context;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;

import activities.riesgo.yacare.com.ec.appriesgo.app.RiesgoSqlLite;
import activities.riesgo.yacare.com.ec.appriesgo.dto.Capa;

public class CapaDataSource {

	public static final String TABLA_CAPA = "capa";
	public static final String COLUMN_CAPA_ID = "idcapa";
	public static final String COLUMN_CAPA_RIESGO = "idriesgo";
	public static final String COLUMN_CAPA_NOMBRE = "nombrecapa";
	public static final String COLUMN_CAPA_DIRECCION = "direccion";
	public static final String COLUMN_CAPA_TELEFONO = "telefono";
	public static final String COLUMN_CAPA_FOTO = "foto";
	public static final String COLUMN_CAPA_COORDENADAS = "coordenadas";
	public static final String COLUMN_CAPA_TIPO = "tipocapa";
	public static final String COLUMN_CAPA_LATITUD = "latitud";
	public static final String COLUMN_CAPA_LONGITUD = "longitud";
	public static final String COLUMN_CAPA_HUECO1 = "hueco1";
	public static final String COLUMN_CAPA_HUECO2 = "hueco2";
	public static final String COLUMN_CAPA_HUECO3 = "hueco3";

	  private SQLiteDatabase database;
	  private RiesgoSqlLite dbHelper;
	  private String[] allColumns = { COLUMN_CAPA_ID,
			  COLUMN_CAPA_RIESGO,
			  COLUMN_CAPA_NOMBRE,
			  COLUMN_CAPA_DIRECCION,
			  COLUMN_CAPA_TELEFONO,
			  COLUMN_CAPA_FOTO,
			  COLUMN_CAPA_COORDENADAS,
			  COLUMN_CAPA_TIPO,
			  COLUMN_CAPA_LATITUD,
			  COLUMN_CAPA_LONGITUD,
			  COLUMN_CAPA_HUECO1,
			  COLUMN_CAPA_HUECO2,
			  COLUMN_CAPA_HUECO3

	  };

	  public CapaDataSource(Context context) {
	    dbHelper = new RiesgoSqlLite(context);
	  }

	  public void open() throws SQLException {
	    database = dbHelper.getWritableDatabase();
	  }

	  public void close() {
	    dbHelper.close();
	  }


	  public ArrayList<Capa> getCapaByTipo(String tipo, String idRiesgo) {
	    ArrayList<Capa> capas = new ArrayList<Capa>();

	    Cursor cursor = database.query(TABLA_CAPA,
	        allColumns, COLUMN_CAPA_TIPO + " = '" + tipo + "' and " + COLUMN_CAPA_RIESGO + " = '" + idRiesgo +  "'", null, null, null, COLUMN_CAPA_ID);

	    cursor.moveToFirst();
	    while (!cursor.isAfterLast()) {
	      Capa capa = cursorToCapa(cursor);
			capas.add(capa);
	      cursor.moveToNext();
	    }
	    // make sure to close the cursor
	    cursor.close();
	    return capas;
	  }

	public ArrayList<Capa> getAllCapaByTipo(String tipo) {
		ArrayList<Capa> capas = new ArrayList<Capa>();

		Cursor cursor = database.query(TABLA_CAPA,
				allColumns, COLUMN_CAPA_TIPO + " = '" + tipo + "'", null, null, null, null,null);

		cursor.moveToFirst();
		while (!cursor.isAfterLast()) {
			Capa capa = cursorToCapa(cursor);
			capas.add(capa);
			cursor.moveToNext();
		}
		// make sure to close the cursor
		cursor.close();
		return capas;
	}

	public Capa getCapaById(String idCapa) {
		Capa capa = new Capa();

		Cursor cursor = database.query(TABLA_CAPA,
				allColumns,  COLUMN_CAPA_ID + " = '" + idCapa + "'", null, null, null,  null);
		if(cursor.getCount() > 0){
			cursor.moveToFirst();
			capa = cursorToCapa(cursor);
			cursor.close();
			return capa;
		}else{
			cursor.close();
			return null;
		}
	}

	  private Capa cursorToCapa(Cursor cursor) {
	    Capa capa = new Capa();
	    capa.setId(cursor.getString(0));
		  capa.setIdRiesgo(cursor.getString(1));
		  capa.setNombre(cursor.getString(2));
		  capa.setDireccion(cursor.getString(3));
		  capa.setTelefono(cursor.getString(4));
		  capa.setFoto(cursor.getString(5));
		  capa.setCoordenadas(cursor.getString(6));
		  capa.setTipocapa(cursor.getString(7));
		  capa.setLatitud(cursor.getString(8));
		  capa.setLongitud(cursor.getString(9));
		  capa.setHueco1(cursor.getString(10));
		  capa.setHueco2(cursor.getString(11));
		  capa.setHueco3(cursor.getString(12));
	    return capa;
	  }
}
