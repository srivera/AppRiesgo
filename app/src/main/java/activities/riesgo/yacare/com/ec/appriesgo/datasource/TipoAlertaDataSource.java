package activities.riesgo.yacare.com.ec.appriesgo.datasource;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;

import java.util.ArrayList;

import activities.riesgo.yacare.com.ec.appriesgo.app.RiesgoLocalSqlLite;
import activities.riesgo.yacare.com.ec.appriesgo.app.RiesgoSqlLite;
import activities.riesgo.yacare.com.ec.appriesgo.dto.TipoAlerta;

public class TipoAlertaDataSource {

	public static final String TABLA_TIPO_ALERTA = "tipoalerta";
	public static final String COLUMN_TIPOA_ID = "idtipoalerta";
	public static final String COLUMN_TIPOA_CODIGO = "codigotipoalerta";
	public static final String COLUMN_TIPOA_NOMBRE= "nombrealerta";
	public static final String COLUMN_TIPOA_LEYENDAC = "leyendacorta";
	public static final String COLUMN_TIPOA_COLOR= "codigocolor";
	public static final String COLUMN_TIPOA_COLOR_OSCURO = "codigocoloroscuro";
	public static final String COLUMN_TIPOS_COLOR_LETRA = "codigocolorletra";
	public static final String COLUMN_TIPOA_ICONO = "icono";
	public static final String COLUMN_TIPOA_PREGUNTA = "pregunta";
	public static final String COLUMN_TIPOA_LEYENDAL = "leyendalarga";
	public static final String COLUMN_TIPOA_IDRIESGO = "idriesgo";

	private SQLiteDatabase database;
	private RiesgoLocalSqlLite dbHelper;
	private String[] allColumns = { COLUMN_TIPOA_ID,
			COLUMN_TIPOA_CODIGO,
			COLUMN_TIPOA_NOMBRE,
			COLUMN_TIPOA_LEYENDAC,
			COLUMN_TIPOA_COLOR,
			COLUMN_TIPOA_COLOR_OSCURO,
			COLUMN_TIPOS_COLOR_LETRA,
			COLUMN_TIPOA_ICONO,
			COLUMN_TIPOA_PREGUNTA,
			COLUMN_TIPOA_LEYENDAL,
			COLUMN_TIPOA_IDRIESGO};

	public TipoAlertaDataSource(Context context) {
		dbHelper = new RiesgoLocalSqlLite(context);
	}

	public void open() throws SQLException {
		database = dbHelper.getWritableDatabase();
	}

	public void close() {
		dbHelper.close();
	}


	public void updateTipoAlerta(TipoAlerta tipoAlerta) {
		System.out.println("updateTipoAlerta with id: " + tipoAlerta.getId());
		ContentValues values = new ContentValues();
		values.put(COLUMN_TIPOA_ID, tipoAlerta.getId());
		values.put(COLUMN_TIPOA_CODIGO, tipoAlerta.getCodigoTipoAlerta());
		values.put(COLUMN_TIPOA_NOMBRE, tipoAlerta.getNombre());
		values.put(COLUMN_TIPOA_LEYENDAC, tipoAlerta.getLeyendaCorta());
		values.put(COLUMN_TIPOA_COLOR, tipoAlerta.getCodigoColor());
		values.put(COLUMN_TIPOA_COLOR_OSCURO, tipoAlerta.getCodigoColorOscuro());
		values.put(COLUMN_TIPOS_COLOR_LETRA, tipoAlerta.getCodigoColorLetra());
		values.put(COLUMN_TIPOA_ICONO, tipoAlerta.getIcono());
		values.put(COLUMN_TIPOA_PREGUNTA, tipoAlerta.getPregunta());
		values.put(COLUMN_TIPOA_LEYENDAL, tipoAlerta.getLeyendaLarga());
		values.put(COLUMN_TIPOA_COLOR_OSCURO, tipoAlerta.getCodigoColorOscuro());


		database.update(TABLA_TIPO_ALERTA, values, COLUMN_TIPOA_ID + " = '" + tipoAlerta.getId() + "'", null);
	}

	public TipoAlerta getTipoAlertaByCodigo(String codigo, String idRiesgo) {
		TipoAlerta tipoAlerta = new TipoAlerta();

		Cursor cursor = database.query(TABLA_TIPO_ALERTA,
				allColumns, COLUMN_TIPOA_CODIGO + " = '" + codigo + "' and " +
						COLUMN_TIPOA_IDRIESGO + " = '" +idRiesgo + "'", null, null, null, null);


		cursor.moveToFirst();
		if (!cursor.isAfterLast()) {
			tipoAlerta = cursorToTipoAlerta(cursor);
		}
		cursor.close();
		return tipoAlerta;
	}

	public ArrayList<TipoAlerta> getByRiesgo(String idRiesgo) {
		ArrayList<TipoAlerta> tipoAlertas = new ArrayList<TipoAlerta>();

		Cursor cursor = database.query(TABLA_TIPO_ALERTA,
				allColumns, COLUMN_TIPOA_IDRIESGO + " = '" +idRiesgo + "'", null, null, null, null);

		cursor.moveToFirst();
		while (!cursor.isAfterLast()) {
			TipoAlerta tipoAlerta = cursorToTipoAlerta(cursor);
			tipoAlertas.add(tipoAlerta);
			cursor.moveToNext();
		}
		// make sure to close the cursor
		cursor.close();
		return tipoAlertas;
	}

	private TipoAlerta cursorToTipoAlerta(Cursor cursor) {
		TipoAlerta tipoAlerta = new TipoAlerta();
		tipoAlerta.setId(cursor.getString(0));
		tipoAlerta.setCodigoTipoAlerta(cursor.getString(1));
		tipoAlerta.setNombre(cursor.getString(2));
		tipoAlerta.setLeyendaCorta(cursor.getString(3));
		tipoAlerta.setCodigoColor(cursor.getString(4));
		tipoAlerta.setCodigoColorOscuro(cursor.getString(5));
		tipoAlerta.setCodigoColorLetra(cursor.getString(6));
		tipoAlerta.setIcono(cursor.getString(7));
		tipoAlerta.setPregunta(cursor.getString(8));
		tipoAlerta.setLeyendaLarga(cursor.getString(9));
		tipoAlerta.setIdRiesgo(cursor.getString(10));
		return tipoAlerta;
	}
}
