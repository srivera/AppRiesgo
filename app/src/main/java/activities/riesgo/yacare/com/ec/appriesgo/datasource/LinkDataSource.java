package activities.riesgo.yacare.com.ec.appriesgo.datasource;


import android.content.Context;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;

import java.util.ArrayList;

import activities.riesgo.yacare.com.ec.appriesgo.app.RiesgoSqlLite;
import activities.riesgo.yacare.com.ec.appriesgo.dto.Link;
import activities.riesgo.yacare.com.ec.appriesgo.dto.TipoAlerta;

public class LinkDataSource {

	public static final String TABLA_LINK = "links";
	public static final String COLUMN_LINK_ID = "id";
	public static final String COLUMN_LINK_RIESGO = "idriesgo";
	public static final String COLUMN_LINK_NOMBRE = "nombre";
	public static final String COLUMN_LINK_URL = "url";
	public static final String COLUMN_LINK_TIPO = "tipo";
	public static final String COLUMN_LINK_ICONO = "icono";
	public static final String COLUMN_LINK_ORDEN = "orden";

	  private SQLiteDatabase database;
	  private RiesgoSqlLite dbHelper;
	  private String[] allColumns = { COLUMN_LINK_ID,
			  COLUMN_LINK_RIESGO,
			  COLUMN_LINK_NOMBRE,
			  COLUMN_LINK_URL,
			  COLUMN_LINK_TIPO,
			  COLUMN_LINK_ICONO,
			  COLUMN_LINK_ORDEN};

	  public LinkDataSource(Context context) {
	    dbHelper = new RiesgoSqlLite(context);
	  }

	  public void open() throws SQLException {
	    database = dbHelper.getWritableDatabase();
	  }

	  public void close() {
	    dbHelper.close();
	  }


	public ArrayList<Link> getLinkByIdRiesgoandTipo(String idRiesgo, String tipo) {
		ArrayList<Link> links = new ArrayList<Link>();

		Cursor cursor = database.query(TABLA_LINK,
				allColumns, COLUMN_LINK_RIESGO + " = '" + idRiesgo + "' and " + COLUMN_LINK_TIPO + " = '" + tipo + "'" , null, null, null, null);

		cursor.moveToFirst();
		while (!cursor.isAfterLast()) {
			Link link = cursorToLink(cursor);
			links.add(link);
			cursor.moveToNext();
		}
		// make sure to close the cursor
		cursor.close();
		return links;
	}

	public Link getLinkByIdRiesgoandRedSocial(String idRiesgo, String tipo) {
		Link link = new Link();

		Cursor cursor = database.query(TABLA_LINK,
				allColumns, COLUMN_LINK_RIESGO + " = '" + idRiesgo + "' and " + COLUMN_LINK_TIPO + " = '" + tipo + "'" , null, null, null, null);

		cursor.moveToFirst();
		if (!cursor.isAfterLast()) {
			link = cursorToLink(cursor);
		}
		cursor.close();
		return link;
	}

	  private Link cursorToLink(Cursor cursor) {
	    Link link = new Link();
		  link.setId(cursor.getString(0));
		  link.setIdRiesgo(cursor.getString(1));
		  link.setNombre(cursor.getString(2));
		  link.setUrl(cursor.getString(3));
		  link.setTipo(cursor.getString(4));
		  link.setIcono(cursor.getString(5));
		  link.setOrden(cursor.getString(6));
		  return link;
	  }
}
