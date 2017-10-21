package activities.riesgo.yacare.com.ec.appriesgo.datasource;

import android.content.Context;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;

import java.util.ArrayList;

import activities.riesgo.yacare.com.ec.appriesgo.app.RiesgoSqlLite;
import activities.riesgo.yacare.com.ec.appriesgo.dto.TipoIncidente;

public class TipoIncidenteDataSource {

	public static final String TABLA_TIPO_INCIDENTE = "tipoincidente";
	public static final String COLUMN_ID_INCIDENTE = "idincidente";
	public static final String COLUMN_NOMBRE_INCIDENTE = "nombreincidente";


	private SQLiteDatabase database;
	private RiesgoSqlLite dbHelper;
	private String[] allColumns = { COLUMN_ID_INCIDENTE,
			COLUMN_NOMBRE_INCIDENTE};

	public TipoIncidenteDataSource(Context context) {
		dbHelper = new RiesgoSqlLite(context);
	}

	public void open() throws SQLException {
		database = dbHelper.getWritableDatabase();
	}

	public void close() {
		dbHelper.close();
	}


	public ArrayList<TipoIncidente> getTipoIncidentes() {
		ArrayList<TipoIncidente> tipoIncidentes = new ArrayList<TipoIncidente>();

		Cursor cursor = database.query(TABLA_TIPO_INCIDENTE,
				allColumns, null, null, null, null, null);

		cursor.moveToFirst();
		while (!cursor.isAfterLast()) {
			TipoIncidente tipoIncidente = cursorToTipoIncidente(cursor);
			tipoIncidentes.add(tipoIncidente);
			cursor.moveToNext();
		}

		cursor.close();
		return tipoIncidentes;
	}

	private TipoIncidente cursorToTipoIncidente(Cursor cursor) {
		TipoIncidente tipoIncidente = new TipoIncidente();
		tipoIncidente.setId(cursor.getString(0));
		tipoIncidente.setNombreIncidente(cursor.getString(1));
		return tipoIncidente;
	}
}
