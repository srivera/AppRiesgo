package activities.riesgo.yacare.com.ec.appriesgo.datasource.local;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;

import java.util.ArrayList;

import activities.riesgo.yacare.com.ec.appriesgo.app.RiesgoLocalSqlLite;
import activities.riesgo.yacare.com.ec.appriesgo.datasource.TipoAlertaDataSource;
import activities.riesgo.yacare.com.ec.appriesgo.dto.Riesgo;

public class RiesgoDataSource {

	public static final String TABLA_RIESGO = "riesgo";
	public static final String COLUMN_RIESGO_ID = "idriesgo";
	public static final String COLUMN_RIESGO_NOMBRE = "nombreriesgo";
	public static final String COLUMN_RIESGO_ESTADO = "estadoriesgo";
	public static final String COLUMN_RIESGO_FECHA_ESTADO = "fechaestadoriesgo";
	public static final String COLUMN_RIESGO_ALERTA = "alertaactual";
	public static final String COLUMN_RIESGO_FECHA_ALERTA = "fechaalerta";

	Context context;
	  private SQLiteDatabase database;
	  private RiesgoLocalSqlLite dbHelper;
	  private String[] allColumns = { COLUMN_RIESGO_ID,
			  COLUMN_RIESGO_NOMBRE,
			  COLUMN_RIESGO_ESTADO,
			  COLUMN_RIESGO_FECHA_ESTADO,
			  COLUMN_RIESGO_ALERTA,
			  COLUMN_RIESGO_FECHA_ALERTA};

	  public RiesgoDataSource(Context context) {
		  this.context = context;
	    dbHelper = new RiesgoLocalSqlLite(context);
	  }

	  public void open() throws SQLException {
	    database = dbHelper.getWritableDatabase();
	  }

	  public void close() {
	    dbHelper.close();
	  }

	public void updateRiesgo(Riesgo riesgo) {
		System.out.println("updateRiesgo with id: " + riesgo.getId());
		ContentValues values = new ContentValues();
		values.put(COLUMN_RIESGO_ID, riesgo.getId());
		values.put(COLUMN_RIESGO_NOMBRE, riesgo.getNombreRiesgo());
		values.put(COLUMN_RIESGO_ESTADO, riesgo.getEstadoRiesgo());
		values.put(COLUMN_RIESGO_FECHA_ESTADO, riesgo.getFechaEstado());
		values.put(COLUMN_RIESGO_ALERTA, riesgo.getAlertaActual());
		values.put(COLUMN_RIESGO_FECHA_ALERTA, riesgo.getFechaAlerta());

		database.update(TABLA_RIESGO, values, COLUMN_RIESGO_ID + " = '" + riesgo.getId() + "'", null);
	}

	public Riesgo getRiesgoId(String idRiesgo) {
		Riesgo riesgo = new Riesgo();
		Cursor cursor = database.query(TABLA_RIESGO,
				allColumns,COLUMN_RIESGO_ID + " = '" + idRiesgo + "'", null, null, null, null);
		cursor.moveToFirst();
		if (!cursor.isAfterLast()) {
			riesgo = cursorToRiesgo(cursor);
		}
		cursor.close();

		TipoAlertaDataSource tipoAlertaDataSource = new TipoAlertaDataSource(context);
		tipoAlertaDataSource.open();
		riesgo.setTipoAlerta(tipoAlertaDataSource.getTipoAlertaByCodigo(riesgo.getAlertaActual(), riesgo.getId()));
		tipoAlertaDataSource.close();
		return riesgo;
	}

	public ArrayList<Riesgo> getAll() {
	    ArrayList<Riesgo> riesgos = new ArrayList<Riesgo>();

	    Cursor cursor = database.query(TABLA_RIESGO,
	        allColumns, COLUMN_RIESGO_ESTADO + " != '" + "I" + "'", null, null, null, null);

	    cursor.moveToFirst();
	    while (!cursor.isAfterLast()) {
			Riesgo riesgo = cursorToRiesgo(cursor);
			riesgos.add(riesgo);
	      cursor.moveToNext();
	    }
	    // make sure to close the cursor
	    cursor.close();
	    return riesgos;
	  }

	public ArrayList<Riesgo> getAllInfromativo() {
		ArrayList<Riesgo> riesgos = new ArrayList<Riesgo>();

		Cursor cursor = database.query(TABLA_RIESGO,
				allColumns, COLUMN_RIESGO_ESTADO + " = '" + "I" + "'", null, null, null, null);

		cursor.moveToFirst();
		while (!cursor.isAfterLast()) {
			Riesgo riesgo = cursorToRiesgo(cursor);
			riesgos.add(riesgo);
			cursor.moveToNext();
		}
		// make sure to close the cursor
		cursor.close();
		return riesgos;
	}

	  private Riesgo cursorToRiesgo(Cursor cursor) {
		  Riesgo riesgo = new Riesgo();
		  riesgo.setId(cursor.getString(0));
		  riesgo.setNombreRiesgo(cursor.getString(1));
		  riesgo.setEstadoRiesgo(cursor.getString(2));
		  riesgo.setFechaEstado(cursor.getString(3));
		  riesgo.setAlertaActual(cursor.getString(4));
		  riesgo.setFechaAlerta(cursor.getString(5));
	    return riesgo;
	  }

	public void actualizarAlertaNaranjaTungurahua() {
		database.execSQL("delete from tipoalerta WHERE idriesgo = '2' and idtipoalerta ='6' and codigotipoalerta = 'NAR';");
		database.execSQL("INSERT INTO tipoalerta (idtipoalerta,codigotipoalerta,nombrealerta,leyendacorta,codigocolor,codigocoloroscuro,codigocolorletra,icono,pregunta,leyendalarga,idriesgo) VALUES \n" +
				" ('6','NAR','ALERTA NARANJA','','#F89E1C','#CA8624','#FFFFFF','naranja.png','ALERTA NARANJA','\n" +
				"AVISO DE PREPARACIÓN PARA UNA POSIBLE ERUPCIÓN INMINENTE.\n" +
				"\n" +
				"La Secretaría de Gestión de Riesgos decretó la alerta naranja en toda la zona de influencia del volcán Tungurahua.\n" +
				"\n" +
				"Los fenómenos volcánicos asociados al Tungurahua son: gases volcánicos, caída de piroclastos, flujos de lodo y escombros, flujos piroclásticos (nubes ardientes), flujos y domos de lava, avalanchas de escombros y sismos volcánicos.  \n" +
				"\n" +
				"Sigue las instrucciones  de las autoridades oficiales y mantén la calma. \n" +
				"\n" +
				"Infórmate constantemente en los medios oficiales, acerca de la evolución del proceso eruptivo y las directrices de los voceros nacionales y locales establecidos.\n" +
				"\n" +
				"Revisa y actualiza tu Plan Familiar y sigue las disposiciones oficiales.\n" +
				"\n" +
				"Actúa siempre: solidario, ordenado e informado en fuentes oficiales.\n" +
				"\n" +
				"En caso de presentarse caída de ceniza, recuerda proteger tus ojos, nariz y boca. Cúbrete la mayor cantidad de piel posible.\n" +
				"\n" +
				"Si necesitas asistencia para la evacuación, para ti o algún familiar, comunica al respecto a los organismos de apoyo o llama al 1800SETEDIS (1800 7383347).\n" +
				"\n" +
				"PARA LAS ZONAS IDENTIFICADAS DE MAYOR RIESGO:\n" +
				"\n" +
				"En las zonas de alto riesgo, la evacuación se realizará únicamente en los sectores previamente identificados con una mayor exposición.\n" +
				"\n" +
				"Se recomienda la evacuación de las zonas de alto riesgo de los grupos de atención prioritaria  (tercera edad, niños y niñas, mujeres embarazadas y personas con discapacidad).\n" +
				"\n" +
				"Si vas a ser evacuado, o evacuas por voluntad propia de tu lugar habitual de residencia, lleva tu mochila de emergencia.  ','2');");
	}

	public void actualizarBaseFenomenoNino(){
		//TABLA RIESGO
		database.execSQL("INSERT INTO riesgo (idriesgo,nombreriesgo,estadoriesgo,fechaestadoriesgo,alertaactual,fechaalerta) VALUES ('3','FENÓMENO EL NIÑO','F','2015/10/22 08:00','AMA','2015/11/13 08:00');");

		//TABLA TIPOALERTA
		database.execSQL("INSERT INTO tipoalerta (idtipoalerta,codigotipoalerta,nombrealerta,leyendacorta,codigocolor,codigocoloroscuro,codigocolorletra,icono,pregunta,leyendalarga,idriesgo) VALUES \n" +
				"('9','AMA','ALERTA AMARILLA',' ','#F8E71C','#BEAF04','#424242','amarillafn.png','ALERTA AMARILLA','\n" +
				"ALERTA AMARILLA: Existe un Fenómeno El Niño con una alta probabilidad de intensidad moderada o alta\n" +
				"\n" +
				"El 13 de noviembre de 2015, la Secretaría de Gestión de Riesgos decretó la alerta amarilla en las zonas que se verán afectadas por el Fenómeno El Niño, que comprende 17 provincias del país (para mayor información ver mapa de afectación).\n" +
				"\n" +
				"Consulta la situación del Fenómeno El Niño periódicamente, en las fuentes oficiales www.gestionderiesgos.gob.ec www.enosecuador.com.\n" +
				"\n" +
				"Infórmate y escucha únicamente las directrices de los voceros nacionales y locales establecidos. www.gestionderiesgos.gob.ec www.enosecuador.com.\n" +
				"\n" +
				"Participa activamente en charlas y simulacros organizados por las autoridades locales\n" +
				"\n" +
				"QUÉ ES EL FENÓMENO EL NIÑO\n" +
				"\n" +
				"El Fenómeno “El Niño” es el resultado de la interacción de dos componentes: a) La corriente del Niño es originada por variaciones de la temperatura superficial del mar y lleva su nombre porque se produce durante la época de las fiestas navideñas; y, b) Cambios en la presión atmosférica denominados Oscilación Sur o ENOS.  El Niño se presenta cada cierto número de años; los últimos 2 fenómenos ocurrieron en los períodos 1982-1983 y 1997-1998.\n" +
				"\n" +
				"Las variaciones en las condiciones del tiempo y del clima ocasionadas por este fenómeno repercuten en la sociedad, al provocar principalmente inundaciones en las zonas planas y deslizamientos en las zonas de laderas, entre otros eventos adversos.\n" +
				"\n" +
				"SI VIVES EN ZONA DE RIESGO DE INUNDACIONES O DESLIZAMIENTOS:\n" +
				"\n" +
				"Elabora tu plan familiar con la participación de todos los miembros de tu familia. Identifica rutas de evacuación, puntos de encuentro y zonas seguras.\n" +
				"\n" +
				"Identifica a tu familia acogiente y albergues cercanos.\n" +
				"\n" +
				"Realiza simulacros de evacuación con tu familia, tomando en cuenta distintas situaciones: juntos, separados, de día, de noche, entre semana o fin de semana; y, revísalo permanentemente. \n" +
				"\n" +
				"En caso de evacuación, lleva tu mochila de emergencia familiar con agua, alimento y ropa. Contempla a toda tu familia para su elaboración.\n" +
				"\n" +
				"Conoce el plan de contingencia de tu lugar de trabajo y de la institución educativa de tus hijos.\n" +
				"\n" +
				"Crea tu grupo de contacto con familiares, amigos y vecinos de manera directa, en redes sociales, whatsapp u otra aplicación/medio digital de uso frecuente.','3');");
		database.execSQL(" INSERT INTO tipoalerta (idtipoalerta,codigotipoalerta,nombrealerta,leyendacorta,codigocolor,codigocoloroscuro,codigocolorletra,icono,pregunta,leyendalarga,idriesgo) VALUES \n" +
				" ('10','NAR','ALERTA NARANJA',' ','#F89E1C','#CA8624','#FFFFFF','naranjafn.png','ALERTA NARANJA','\n" +
				"ALERTA NARANJA: Aviso de preparación para un Fenómeno El Niño de intensidad moderada a alta (lluvias sobre la normal).\n" +
				"\n" +
				"Sigue las instrucciones de las autoridades oficiales y mantén la calma. \n" +
				"\n" +
				"Infórmate constantemente en los medios oficiales, acerca de la evolución del fenómeno y las directrices de los voceros nacionales y locales. www.gestionderiesgos.gob.ec www.enosecuador.com\n" +
				"\n" +
				"Revisa y actualiza tu Plan Familiar y sigue las disposiciones oficiales.\n" +
				"\n" +
				"Actúa siempre solidario, ordenado e informado a través de fuentes oficiales. \n" +
				"\n" +
				"Si necesitas asistencia para la evacuación, para ti o algún familiar con discapacidad, comunica al respecto a los organismos de apoyo o llama al 1800SETEDIS (1800 7383347)\n" +
				"\n" +
				"PARA LAS ZONAS IDENTIFICADAS DE MAYOR RIESGO: \n" +
				"\n" +
				"En las zonas de alto riesgo, la evacuación se realizará únicamente en los sectores previamente identificados con una mayor exposición.\n" +
				"\n" +
				"Si vas a ser evacuado, o evacuas por voluntad propia de tu lugar habitual de residencia, lleva siempre tu mochila de emergencia. ','3');");
		database.execSQL("INSERT INTO tipoalerta (idtipoalerta,codigotipoalerta,nombrealerta,leyendacorta,codigocolor,codigocoloroscuro,codigocolorletra,icono,pregunta,leyendalarga,idriesgo) VALUES \n" +
				" ('11','ROJ','ALERTA ROJA',' ','#F81C43','#B20524','#FFFFFF','rojafn.png','ALERTA ROJA','\n" +
				"ALERTA ROJA: El Fenómeno El Niño está en curso\n" +
				"\n" +
				"Sigue  las instrucciones  de las autoridades oficiales y mantén la calma. www.gestionderiesgos.gob.ec www.enosecuador.com\n" +
				"\n" +
				"Si la autoridad lo dispone, permanece en el lugar en que te encuentras o evacua (según sea el caso)\n" +
				"\n" +
				"Llama al 9-1-1 en caso de emergencia. \n" +
				"\n" +
				"SI TE ENCUENTRAS EN UNA ZONA DE RIESGO\n" +
				"\n" +
				"Evacua hacia los puntos de encuentro y  zonas seguras, según tu plan familiar y si las autoridades lo disponen. \n" +
				"\n" +
				"Evita atravesar ríos o zonas inundadas a pie, en animales o vehículos. Si necesitas hacerlo, busca apoyo de personal especializado.\n" +
				"\n" +
				"No cruces puentes donde el nivel del agua es alto, porque sus bases podrían estar afectadas. No te acerques a las alcantarillas.\n" +
				"\n" +
				"Evita el contacto con el agua de la inundación.\n" +
				" \n" +
				"Aléjate de postes eléctricos caídos o alambres rotos en la vía o dentro de áreas inundadas. \n" +
				"\n" +
				"Aléjate de lugares donde puedan producirse derrumbes.\n" +
				"\n" +
				"Si no estás junto a tu familia, espera a que pase la emergencia y encuéntrate con ellos en el lugar definido con anterioridad.\n" +
				"\n" +
				"Recuerda que, durante la emergencia, es posible que los servicios básicos se interrumpan por lo que debes mantener la calma y actuar acorde a tu plan familiar.\n" +
				"\n" +
				"Espera a que pase la emergencia para contactarte con tu familia, a través de tu grupo en redes sociales, radio o teléfonos convencionales.\n" +
				"\n" +
				"Infórmate sobre la evolución del fenómeno y las disposiciones de las autoridades locales o nacionales. Actuar correctamente puede salvar tu vida y la de los tuyos.\n" +
				"\n" +
				"','3');");

		//TABLA ELEMENTOEMERGENCIA
		database.execSQL("INSERT INTO elementoemergencia (idelementoemergencia,idriesgo,ordenelementoemergencia,nombreelementoemergencia,checklist,icono) VALUES \n" +
				"('41','3','M','Documentos personales y de su vivienda; y, teléfonos de los miembros de la familia','0','documentos.png');");
		database.execSQL("INSERT INTO elementoemergencia (idelementoemergencia,idriesgo,ordenelementoemergencia,nombreelementoemergencia,checklist,icono) VALUES \n" +
				" ('42','3','E','Mascarilla, toalla o bufanda para protegerse la nariz y boca','0','mascarillas.png');");
		database.execSQL("INSERT INTO elementoemergencia (idelementoemergencia,idriesgo,ordenelementoemergencia,nombreelementoemergencia,checklist,icono) VALUES  \n" +
				"('43','3','J','Linterna a pilas','0','linterna.png');");
		database.execSQL("INSERT INTO elementoemergencia (idelementoemergencia,idriesgo,ordenelementoemergencia,nombreelementoemergencia,checklist,icono) VALUES  \n" +
				"('44','3','A','Alimentos no perecibles','0','alimentos.png');");
		database.execSQL("INSERT INTO elementoemergencia (idelementoemergencia,idriesgo,ordenelementoemergencia,nombreelementoemergencia,checklist,icono) VALUES \n" +
				" ('45','3','C','Un juego adicional de ropa y abrigo','0','ropa.png');");
		database.execSQL("INSERT INTO elementoemergencia (idelementoemergencia,idriesgo,ordenelementoemergencia,nombreelementoemergencia,checklist,icono) VALUES\n" +
				" ('46','3','B','Agua limpia cerrada o embotellada','0','agua.png');");
		database.execSQL("INSERT INTO elementoemergencia (idelementoemergencia,idriesgo,ordenelementoemergencia,nombreelementoemergencia,checklist,icono) VALUES \n" +
				" ('47','3','P','Botiquín o bolso con medicina para atender heridos','0','botiquin.png');");
		database.execSQL("INSERT INTO elementoemergencia (idelementoemergencia,idriesgo,ordenelementoemergencia,nombreelementoemergencia,checklist,icono) VALUES\n" +
				" ('48','3','D','Manta delgada y liviana','0','frazadas.png');");
		database.execSQL("INSERT INTO elementoemergencia (idelementoemergencia,idriesgo,ordenelementoemergencia,nombreelementoemergencia,checklist,icono) VALUES \n" +
				" ('49','3','H','Radio a pilas','0','radio.png');");
		database.execSQL("INSERT INTO elementoemergencia (idelementoemergencia,idriesgo,ordenelementoemergencia,nombreelementoemergencia,checklist,icono) VALUES\n" +
				" ('50','3','L','Copia de las llaves de la vivienda y del auto','0','llaves.png');");
		database.execSQL("INSERT INTO elementoemergencia (idelementoemergencia,idriesgo,ordenelementoemergencia,nombreelementoemergencia,checklist,icono) VALUES\n" +
				" ('51','3','K','Bolsas plásticas','0','bolsas.png');");
		database.execSQL("INSERT INTO elementoemergencia (idelementoemergencia,idriesgo,ordenelementoemergencia,nombreelementoemergencia,checklist,icono) VALUES\n" +
				" ('52','3','I','Velas y fósforos','0','velas.png');");
		database.execSQL("INSERT INTO elementoemergencia (idelementoemergencia,idriesgo,ordenelementoemergencia,nombreelementoemergencia,checklist,icono) VALUES \n" +
				" ('53','3','F','Gafas para protegerse los ojos','0','gafas.png');");
		database.execSQL("INSERT INTO elementoemergencia (idelementoemergencia,idriesgo,ordenelementoemergencia,nombreelementoemergencia,checklist,icono) VALUES  \n" +
				"('54','3','G','Gorra o gorro','0','gorra.png');");
		database.execSQL("INSERT INTO elementoemergencia (idelementoemergencia,idriesgo,ordenelementoemergencia,nombreelementoemergencia,checklist,icono) VALUES \n" +
				" ('55','3','N','Tipo de sangre de cada miembro de la familia y medicinas de uso frecuente (incluya la cantidad y frecuencia de uso)','0','medicina.png');");
	}
	public void actualizarAlertaBlanca(){
		//TABLA TIPOALERTA
		database.execSQL("INSERT INTO tipoalerta (idtipoalerta,codigotipoalerta,nombrealerta,leyendacorta,codigocolor,codigocoloroscuro," +
				"codigocolorletra,icono,pregunta,leyendalarga,idriesgo) VALUES \n" +
				"('20','BLA','SITUACIÓN ACTUAL','','#DADADA','#DADADA','#000000','gris.png'," +
				"'SITUACIÓN ACTUAL','El volcán Cotopaxi está ubicado entre las Provincias de Pichincha y Cotopaxi,  sobre la Cordillera Oriental, a una distancia de 35 km al Noreste de Latacunga y de 45 km al Sureste de Quito. Es considerado uno de los volcanes más peligrosos del mundo debido a la frecuencia de sus erupciones, su estilo eruptivo, su relieve, su cobertura glaciar y por las poblaciones potencialmente expuestas a sus amenazas. Sus erupciones pueden generar grandes lahares (flujos de lodo y escombros), caída de ceniza, flujos de lava, flujos piroclásticos, entre otros fenómenos volcánicos.\n" +
				"\n" +
				"Los parámetros sísmicos, de concentraciones de gases de dióxido de azufre (SO2) y de deformación del volcán, monitoreados por el Instituto Geofísico de la Escuela Politécnica Nacional, registran valores normales.  \n" +
				"\n" +
				"Se mantienen las acciones de: monitoreo y generación de información permanente, actualización periódica de los planes de contingencia, así como las acciones de prevención, mitigación y ordenamiento territorial de los Gobiernos Autónomos Descentralizados.\n" +
				"\n" +
				"Consulta la situación del volcán periódicamente (www.igepn.edu.ec/cotopaxi/informes-cotopaxi/coto-diarios).\n" +
				"\n" +
				"Infórmate siempre a través de medios oficiales, no hagas caso ni difundas rumores  (www.gestionderiesgos.gob.ec, www.volcancotopaxi.com).\n" +
				"\n" +
				"Estar atento a las alertas decretadas por las autoridades que se emiten en función de los cambios que se pueden presentar en los parámetros del volcán. \n','1');");
	}
	public void actualizarBaseDatosTungurahua(){
		//TABLA RIESGO
		database.execSQL("UPDATE riesgo SET estadoriesgo = 'V', nombreriesgo = 'VOLCÁN COTOPAXI' where idriesgo = '1';");
		database.execSQL("INSERT INTO riesgo (idriesgo,nombreriesgo,estadoriesgo,fechaestadoriesgo,alertaactual,fechaalerta) VALUES ('2','VOLCÁN TUNGURAHUA','V','2006/07/14 18:00','NAR','2015/11/17 08:00');");

		//TABLA TIPOALERTA
		database.execSQL("INSERT INTO tipoalerta (idtipoalerta,codigotipoalerta,nombrealerta,leyendacorta,codigocolor,codigocoloroscuro,codigocolorletra,icono,pregunta,leyendalarga,idriesgo) VALUES \n" +
				"('5','AMA','ALERTA AMARILLA','','#F8E71C','#BEAF04','#424242','amarilla.png','ALERTA AMARILLA','\n" +
				"Infórmate siempre a través de medios oficiales, no hagas caso ni difundas rumores. www.gestionderiesgos.gob.ec\n" +
				"\n" +
				"Consulta la situación del volcán mínimo una vez al día.\n" +
				"\n" +
				"Participa activamente en charlas y simulacros organizados por las autoridades locales.\n" +
				"\n" +
				"En caso de presentarse caída de ceniza, recuerda proteger tus ojos, nariz y boca. Cúbrete la mayor cantidad de piel posible.\n" +
				"\n" +
				"SI VIVES EN ZONA DE RIESGO:\n" +
				"\n" +
				"Elabora tu plan familiar con la participación de todos los miembros de tu familia. Identifica rutas de evacuación, puntos de encuentro y zonas seguras.\n" +
				"\n" +
				"Identifica a tu familia acogiente y albergues cercanos.\n" +
				"\n" +
				"Realiza simulacros de evacuación con tu familia, tomando en cuenta distintas situaciones: juntos, separados, de día, de noche, entre semana o fin de semana; y, revísalo permanentemente.\n" +
				"\n" +
				"Prepara una mochila de emergencia familiar o personal.\n" +
				"\n" +
				"Conoce el plan de contingencia de tu lugar de trabajo y el plan de evacuación de la institución educativa de tus hijos.\n" +
				"\n" +
				"Si la escuela de tus hijos está en zona de riesgo:\n" +
				"-       Consulta el plan de contingencia educativo\n" +
				"-       Recorre la ruta de evacuación de tus hijos y el sitio a donde van a ir.\n" +
				"-       Explícales por qué razón deben de evacuar y hacia dónde deben de ir.\n" +
				"-       Explícales que, de darse una erupción, es posible que te demores en recogerlo, por lo que deberá permanecer con sus compañeros y docentes, en calma y seguro.\n" +
				"\n" +
				"Crea tu grupo de contacto con familiares, amigos y vecinos de manera directa, en redes sociales, whatsapp u otra aplicación/medio digital de uso frecuente.\n" +
				"\n" +
				"Participa en capacitaciones, simulacros e infórmate a través de medios oficiales. www.gestionderiesgos.gob.ec','2');");

		database.execSQL("INSERT INTO tipoalerta (idtipoalerta,codigotipoalerta,nombrealerta,leyendacorta,codigocolor,codigocoloroscuro,codigocolorletra,icono,pregunta,leyendalarga,idriesgo) VALUES \n" +
				" ('6','NAR','ALERTA NARANJA','','#F89E1C','#CA8624','#FFFFFF','naranja.png','ALERTA NARANJA','\n" +
				"AVISO DE PREPARACIÓN PARA UNA POSIBLE ERUPCIÓN INMINENTE.\n" +
				"\n" +
				"La Secretaría de Gestión de Riesgos decretó la alerta naranja en toda la zona de influencia del volcán Tungurahua, el 17 de Noviembre de 2015.\n" +
				"\n" +
				"Los fenómenos volcánicos asociados al Tungurahua son: gases volcánicos, caída de piroclastos, flujos de lodo y escombros, flujos piroclásticos (nubes ardientes), flujos y domos de lava, avalanchas de escombros y sismos volcánicos.  \n" +
				"\n" +
				"Sigue las instrucciones  de las autoridades oficiales y mantén la calma. \n" +
				"\n" +
				"Infórmate constantemente en los medios oficiales, acerca de la evolución del proceso eruptivo y las directrices de los voceros nacionales y locales establecidos.\n" +
				"\n" +
				"Revisa y actualiza tu Plan Familiar y sigue las disposiciones oficiales.\n" +
				"\n" +
				"Actúa siempre: solidario, ordenado e informado en fuentes oficiales.\n" +
				"\n" +
				"En caso de presentarse caída de ceniza, recuerda proteger tus ojos, nariz y boca. Cúbrete la mayor cantidad de piel posible.\n" +
				"\n" +
				"Si necesitas asistencia para la evacuación, para ti o algún familiar, comunica al respecto a los organismos de apoyo o llama al 1800SETEDIS (1800 7383347).\n" +
				"\n" +
				"PARA LAS ZONAS IDENTIFICADAS DE MAYOR RIESGO:\n" +
				"\n" +
				"En las zonas de alto riesgo, la evacuación se realizará únicamente en los sectores previamente identificados con una mayor exposición.\n" +
				"\n" +
				"Se recomienda la evacuación de las zonas de alto riesgo de los grupos de atención prioritaria  (tercera edad, niños y niñas, mujeres embarazadas y personas con discapacidad).\n" +
				"\n" +
				"Si vas a ser evacuado, o evacuas por voluntad propia de tu lugar habitual de residencia, lleva tu mochila de emergencia.  ','2');");

		database.execSQL("INSERT INTO tipoalerta (idtipoalerta,codigotipoalerta,nombrealerta,leyendacorta,codigocolor,codigocoloroscuro,codigocolorletra,icono,pregunta,leyendalarga,idriesgo) VALUES \n" +
				" ('7','ROJ','ALERTA ROJA','','#F81C43','#B20524','#FFFFFF','roja.png','ALERTA ROJA','\n" +
				"Sigue  las instrucciones  de las autoridades oficiales y mantén la calma. www.gestionderiesgos.gob.ec  \n" +
				"\n" +
				"Sintoniza Radio Pública (88.1 en Tungurahua y Chimborazo) y permanece atento a las indicaciones de las autoridades. \n" +
				"\n" +
				"Si la autoridad lo dispone, permanece en el lugar en que te encuentras o evacua (según sea el caso)\n" +
				"\n" +
				"Llama al 9-1-1 en caso de emergencia\n" +
				"\n" +
				"SI TE ENCUENTRAS EN UNA ZONA DE RIESGO\n" +
				"\n" +
				"Evacua a pie hacia los puntos de encuentro y  zonas seguras, según tu plan familiar y si las autoridades lo disponen.\n" +
				"\n" +
				"Si no estás junto a tu familia, espera a que pase la emergencia y encuéntrate con ellos en el lugar definido con anterioridad.\n" +
				"\n" +
				"Recuerda que durante la emergencia, es posible que los servicios básicos se interrumpan por lo que debes mantener la calma y actuar acorde a tu plan familiar.\n" +
				"\n" +
				"Si adviertes la presencia de ceniza, sella las puertas y ventanas del lugar donde estés. Procura no salir hasta que las autoridades lo dispongan.\n" +
				"\n" +
				"Espera a que pase la emergencia para contactarte con tu familia, a través de tu grupo en redes sociales, radio o teléfonos convencionales.\n" +
				"\n" +
				"Recuerda que estar informado sobre la evolución del proceso eruptivo y las disposiciones de las autoridades locales o nacionales. Actuar correctamente puede salvar tu vida y la de los tuyos.','2');");


		//TABLA ELEMENTOEMERGENCIA
		database.execSQL("INSERT INTO elementoemergencia (idelementoemergencia,idriesgo,ordenelementoemergencia,nombreelementoemergencia,checklist,icono) VALUES \n" +
				"('21','2','M','Documentos personales y de su vivienda; y, teléfonos de los miembros de la familia','0','documentos.png');");
		database.execSQL("INSERT INTO elementoemergencia (idelementoemergencia,idriesgo,ordenelementoemergencia,nombreelementoemergencia,checklist,icono) VALUES \n" +
				" ('22','2','E','Mascarilla, toalla o bufanda para protegerse la nariz y boca de la ceniza','0','mascarillas.png');");
		database.execSQL("INSERT INTO elementoemergencia (idelementoemergencia,idriesgo,ordenelementoemergencia,nombreelementoemergencia,checklist,icono) VALUES  \n" +
				"('23','2','J','Linterna a pilas','0','linterna.png');");
		database.execSQL("INSERT INTO elementoemergencia (idelementoemergencia,idriesgo,ordenelementoemergencia,nombreelementoemergencia,checklist,icono) VALUES  \n" +
				"('24','2','A','Alimentos no perecibles','0','alimentos.png');");
		database.execSQL("INSERT INTO elementoemergencia (idelementoemergencia,idriesgo,ordenelementoemergencia,nombreelementoemergencia,checklist,icono) VALUES \n" +
				" ('25','2','C','Un juego adicional de ropa y abrigo','0','ropa.png');");
		database.execSQL("INSERT INTO elementoemergencia (idelementoemergencia,idriesgo,ordenelementoemergencia,nombreelementoemergencia,checklist,icono) VALUES\n" +
				" ('26','2','B','Agua limpia cerrada o embotellada','0','agua.png');");
		database.execSQL("INSERT INTO elementoemergencia (idelementoemergencia,idriesgo,ordenelementoemergencia,nombreelementoemergencia,checklist,icono) VALUES \n" +
				" ('27','2','P','Botiquín o bolso con medicina para atender heridos','0','botiquin.png');");
		database.execSQL("INSERT INTO elementoemergencia (idelementoemergencia,idriesgo,ordenelementoemergencia,nombreelementoemergencia,checklist,icono) VALUES\n" +
				" ('28','2','D','Manta delgada y liviana','0','frazadas.png');");
		database.execSQL("INSERT INTO elementoemergencia (idelementoemergencia,idriesgo,ordenelementoemergencia,nombreelementoemergencia,checklist,icono) VALUES \n" +
				" ('29','2','H','Radio a pilas','0','radio.png');");
		database.execSQL("INSERT INTO elementoemergencia (idelementoemergencia,idriesgo,ordenelementoemergencia,nombreelementoemergencia,checklist,icono) VALUES\n" +
				" ('30','2','L','Copia de las llaves de la vivienda y del auto','0','llaves.png');");
		database.execSQL("INSERT INTO elementoemergencia (idelementoemergencia,idriesgo,ordenelementoemergencia,nombreelementoemergencia,checklist,icono) VALUES\n" +
				" ('31','2','K','Bolsas plásticas','0','bolsas.png');");
		database.execSQL("INSERT INTO elementoemergencia (idelementoemergencia,idriesgo,ordenelementoemergencia,nombreelementoemergencia,checklist,icono) VALUES\n" +
				" ('32','2','I','Velas y fósforos','0','velas.png');");
		database.execSQL("INSERT INTO elementoemergencia (idelementoemergencia,idriesgo,ordenelementoemergencia,nombreelementoemergencia,checklist,icono) VALUES \n" +
				" ('33','2','F','Gafas para protegerse los ojos de la ceniza','0','gafas.png');");
		database.execSQL("INSERT INTO elementoemergencia (idelementoemergencia,idriesgo,ordenelementoemergencia,nombreelementoemergencia,checklist,icono) VALUES  \n" +
				"('34','2','G','Gorra o gorro para protegerse de la ceniza','0','gorra.png');");
		database.execSQL("INSERT INTO elementoemergencia (idelementoemergencia,idriesgo,ordenelementoemergencia,nombreelementoemergencia,checklist,icono) VALUES \n" +
				" ('35','2','N','Tipo de sangre de cada miembro de la familia y medicinas de uso frecuente (incluya la cantidad y frecuencia de uso)','0','medicina.png');");


	}

	public void actualizarColorTurismo(){
		database.execSQL("UPDATE tipoalerta SET codigocolor = '#72ADEC', codigocoloroscuro = '#3173B9' WHERE idtipoalerta = '28';");

	}

	public void actualizarBaseDatosTurismo(){
		//TURISTICO
		database.execSQL("INSERT INTO riesgo (idriesgo,nombreriesgo,estadoriesgo,fechaestadoriesgo,alertaactual,fechaalerta) VALUES ('5','PLAN INTEGRAL DE ASISTENCIA TURISTICA','I','2016/10/03 18:00','BLA','2016/10/03 08:00');");

		//TODO
		database.execSQL("INSERT INTO tipoalerta (idtipoalerta,codigotipoalerta,nombrealerta,leyendacorta,codigocolor,codigocoloroscuro,codigocolorletra,icono,pregunta,leyendalarga,idriesgo) VALUES \n" +
				" ('28','BLA','ASISTENCIA TURÍSTICA','Descarga el Plan','#DADADA','#DADADA','#000000','maleta.png','ASISTENCIA TURÍSTICA','¡Bienvenido al país que ama la vida! ¡Bienvenido a Ecuador, el país de los cuatro mundos! Lo tenemos todo y tan cerca. Nos preocupamos por su bienestar, con un enfoque preventivo, elaboramos el Plan Integral de Asistencia Turística, mediante el cual nos aseguramos de que su estadía en Ecuador sea placentera, segura y en caso de que usted lo requiera, poder darle atención inmediata con las siguientes herramientas. ','5');");

	}

	public void actualizarBaseDatosTsunami2(){
		database.execSQL("delete from tipoalerta where idtipoalerta = '23';");
		//COTOPAXI


		database.execSQL("UPDATE tipoalerta SET leyendalarga = '\n" +
				"ALERTA AMARILLA: AVISO DE LA ACTIVACIÓN SIGNIFICATIVA DE LA AMENAZA.\n" +
				"\n" +
				"Infórmate y escucha únicamente las directrices de los voceros nacionales y locales establecidos.\n" +
				"\n" +
				"Consulta la situación del volcán mínimo una vez al día.\n" +
				"\n" +
				"Participa activamente en charlas y simulacros organizados por las autoridades locales.\n" +
				"\n" +
				"En caso de presentarse caída de ceniza, recuerda proteger tus ojos, nariz y boca. Cúbrete la mayor cantidad de piel posible.\n" +
				"\n" +
				"SI VIVES EN ZONA DE RIESGO:\n" +
				"\n" +
				"Plan familiar. Son actividades que permiten organizar a la familia para saber qué hacer durante una emergencia y reducir las afectaciones en caso de una erupción volcánica.\n" +
				"\n" +
				"Elabora tu plan familiar. Identifica rutas de evacuación, puntos de encuentro y zonas seguras.\n" +
				"\n" +
				"Identifica a tu familia acogiente y albergues cercanos.\n" +
				"\n" +
				"Realiza simulacros de evacuación con tu familia, tomando en cuenta distintas situaciones: familia junta, separada, de día, de noche, entre semana o fin de semana; y, revísalo permanentemente.\n" +
				"\n" +
				"Prepara una mochila de emergencia familiar o personal.\n" +
				"\n" +
				"Conoce el plan de contingencia de tu lugar de trabajo y el plan de evacuación de la institución educativa de tus hijos.\n" +
				"\n" +
				"Si la escuela de tus hijos está en zona de riesgo:\n" +
				"-       Consulta el plan de contingencia educativo\n" +
				"-       Recorre la ruta de evacuación de tus hijos y el sitio a donde van a ir.\n" +
				"-       Explícales por qué razón deben de evacuar y hacia dónde deben de ir.\n" +
				"-       Explícales que, de darse una erupción, es posible que te demores varias horas o días en recogerlo, por lo que deberá permanecer con sus compañeros y docentes, en calma y seguro.\n" +
				"\n" +
				"Crea tu grupo de contacto con familiares, amigos y vecinos de manera directa, en redes sociales, whatsapp u otra aplicación/medio digital de uso frecuente.\n" +
				"\n" +
				"Participa en capacitaciones, simulacros e infórmate a través de medios oficiales.' WHERE idtipoalerta = '1';");

		database.execSQL("UPDATE tipoalerta SET leyendalarga = '\n" +
				"ALERTA NARANJA: AVISO DE PREPARACIÓN PARA UNA POSIBLE ERUPCIÓN INMINENTE.\n" +
				"\n" +
				"Sigue las instrucciones  de las autoridades oficiales y mantén la calma.\n" +
				"\n" +
				"Aplica tu Plan Familiar y sigue las disposiciones oficiales.\n" +
				"\n" +
				"Actúa siempre: solidario, ordenado e informado en fuentes oficiales.\n" +
				"\n" +
				"En caso de presentarse caída de ceniza, recuerda proteger tus ojos, nariz y boca. Cúbrete la mayor cantidad de piel posible.\n" +
				"\n" +
				"Si necesitas asistencia para la evacuación, para ti o algún familiar, comunica al respecto a los organismos de apoyo (CRUZ ROJA, BOMBEROS) al número único 911.\n" +
				"\n" +
				"\n" +
				"PARA LAS ZONAS IDENTIFICADAS DE MAYOR RIESGO POR FLUJOS DE LODO:\n" +
				"\n" +
				"Se recomienda la evacuación de las zonas de mayor riesgo, especialmente de los grupos de atención prioritaria (tercera edad, niños y niñas, mujeres embarazadas y personas con discapacidad).\n" +
				"\n" +
				"La evacuación en las zonas de alto riesgo será focalizada para poblaciones previamente identificadas con mayor exposición.\n" +
				"\n" +
				"Si vas a ser evacuado, o evacuas por voluntad propia de tu lugar habitual de residencia, lleva tu mochila de emergencia.  ' WHERE idtipoalerta = '2';");

		database.execSQL("UPDATE tipoalerta SET leyendalarga = '\n" +
				"\n" +
				"ALERTA ROJA: LA ERUPCIÓN VOLCÁNICA ESTÁ EN CURSO\n" +
				"\n" +
				"Sigue  las instrucciones  de las autoridades oficiales y mantén la calma.\n" +
				"\n" +
				"Si la autoridad lo dispone, permanece en el lugar en que te encuentras o evacua (según sea el caso)\n" +
				"\n" +
				"Llama al 9-1-1 en caso de emergencia\n" +
				"\n" +
				"SI TE ENCUENTRAS EN UNA ZONA DE RIESGO\n" +
				"\n" +
				"- Evacúa peatonalmente hacia los puntos de encuentro y  zonas seguras, según tu Plan Familiar.\n" +
				"- Si no estás junto a tu familia, espera a que pase la emergencia y encuéntrate con ellos en el lugar definido con anterioridad.\n" +
				"- Recuerda que durante la emergencia, es posible que los servicios básicos se interrumpan por lo que debes mantener la calma y actuar acorde a tu plan familiar.\n" +
				"- Si adviertes la presencia de un lahar acude a zonas altas, lo más alejado posible.\n" +
				"- Si adviertes la presencia de ceniza, sella las puertas y ventanas del lugar donde estés. Procura no salir hasta que las autoridades lo dispongan.\n" +
				"- Espera a que pase la emergencia para contactarte con tu familia, a través de tu grupo de contacto en redes sociales, radio o teléfonos convencionales.\n" +
				"- Recuerda que estar informado y actuar correctamente puede salvar tu vida y la de los tuyos. ' WHERE idtipoalerta = '3';");

		database.execSQL("UPDATE tipoalerta SET leyendalarga = 'El volcán Cotopaxi está ubicado entre las Provincias de Pichincha y Cotopaxi,  sobre la Cordillera Oriental, a una distancia de 35 km al Noreste de Latacunga y de 45 km al Sureste de Quito. Es considerado uno de los volcanes más peligrosos del mundo debido a la frecuencia de sus erupciones, su estilo eruptivo, su relieve, su cobertura glaciar y por las poblaciones potencialmente expuestas a sus amenazas. Sus erupciones pueden generar grandes lahares (flujos de lodo y escombros), caída de ceniza, flujos de lava, flujos piroclásticos, entre otros fenómenos volcánicos.\n" +
				"\n" +
				"Los parámetros sísmicos, de concentraciones de gases de dióxido de azufre (SO2) y de deformación del volcán, monitoreados por el Instituto Geofísico de la Escuela Politécnica Nacional, registran valores normales. \n" +
				"\n" +
				"Se mantienen las acciones de: monitoreo y generación de información permanente, actualización periódica de los planes de contingencia, así como las acciones de prevención, mitigación y ordenamiento territorial de los Gobiernos Autónomos Descentralizados.\n" +
				"\n" +
				"Consulta la situación del volcán periódicamente (www.igepn.edu.ec/cotopaxi/informes-cotopaxi/coto-diarios).\n" +
				"\n" +
				"Infórmate siempre a través de medios oficiales, no hagas caso ni difundas rumores  (www.gestionderiesgos.gob.ec).\n" +
				"\n" +
				"Estar atento a las alertas decretadas por las autoridades que se emiten en función de los cambios que se pueden presentar en los parámetros del volcán. \n" +
				"' WHERE idtipoalerta = '20';");

		//TUNGURAHUA
		database.execSQL("UPDATE tipoalerta SET leyendalarga = '\nAVISO DE LA ACTIVACIÓN SIGNIFICATIVA DE LA AMENAZA\n" +
				"\n" +
				"Infórmate siempre a través de medios oficiales, no hagas caso ni difundas rumores. www.gestionderiesgos.gob.ec\n" +
				"\n" +
				"Consulta la situación del volcán mínimo una vez al día.\n" +
				"\n" +
				"Participa activamente en charlas y simulacros organizados por las autoridades locales.\n" +
				"\n" +
				"En caso de presentarse caída de ceniza, recuerda proteger tus ojos, nariz y boca. Cúbrete la mayor cantidad de piel posible.\n" +
				"\n" +
				"SI VIVES EN ZONA DE RIESGO:\n" +
				"\n" +
				"Elabora tu plan familiar con la participación de todos los miembros de tu familia. Identifica rutas de evacuación, puntos de encuentro y zonas seguras.\n" +
				"\n" +
				"Identifica a tu familia acogiente y albergues cercanos.\n" +
				"\n" +
				"Realiza simulacros de evacuación con tu familia, tomando en cuenta distintas situaciones: juntos, separados, de día, de noche, entre semana o fin de semana; y, revísalo permanentemente.\n" +
				"\n" +
				"Prepara una mochila de emergencia familiar o personal.\n" +
				"\n" +
				"Conoce el plan de contingencia de tu lugar de trabajo y el plan de evacuación de la institución educativa de tus hijos.\n" +
				"\n" +
				"Si la escuela de tus hijos está en zona de riesgo:\n" +
				"-       Consulta el plan de contingencia educativo\n" +
				"-       Recorre la ruta de evacuación de tus hijos y el sitio a donde van a ir.\n" +
				"-       Explícales por qué razón deben de evacuar y hacia dónde deben de ir.\n" +
				"-       Explícales que, de darse una erupción, es posible que te demores en recogerlo, por lo que deberá permanecer con sus compañeros y docentes, en calma y seguro.\n" +
				"\n" +
				"Crea tu grupo de contacto con familiares, amigos y vecinos de manera directa, en redes sociales, whatsapp u otra aplicación/medio digital de uso frecuente.\n" +
				"\n" +
				"Participa en capacitaciones, simulacros e infórmate a través de medios oficiales. www.gestionderiesgos.gob.ec' WHERE idtipoalerta = '5';");

		database.execSQL("UPDATE tipoalerta SET leyendalarga = '\nAVISO DE PREPARACIÓN PARA UNA POSIBLE ERUPCIÓN INMINENTE.\n" +
				"\n" +
				"Los fenómenos volcánicos asociados al Tungurahua son: gases volcánicos, caída de piroclastos, flujos de lodo y escombros, flujos piroclásticos (nubes ardientes), flujos y domos de lava, avalanchas de escombros y sismos volcánicos.  \n" +
				"\n" +
				"Sigue las instrucciones  de las autoridades oficiales y mantén la calma. \n" +
				"\n" +
				"Infórmate constantemente en los medios oficiales, acerca de la evolución del proceso eruptivo y las directrices de los voceros nacionales y locales establecidos.\n" +
				"\n" +
				"Revisa y actualiza tu Plan Familiar y sigue las disposiciones oficiales.\n" +
				"\n" +
				"Actúa siempre: solidario, ordenado e informado en fuentes oficiales.\n" +
				"\n" +
				"En caso de presentarse caída de ceniza, recuerda proteger tus ojos, nariz y boca. Cúbrete la mayor cantidad de piel posible.\n" +
				"\n" +
				"Si necesitas asistencia para la evacuación, para ti o algún familiar, comunica al respecto a los organismos de apoyo.\n" +
				"\n" +
				"PARA LAS ZONAS IDENTIFICADAS DE MAYOR RIESGO:\n" +
				"\n" +
				"En las zonas de alto riesgo, la evacuación se realizará únicamente en los sectores previamente identificados con una mayor exposición.\n" +
				"\n" +
				"Se recomienda la evacuación de las zonas de alto riesgo de los grupos de atención prioritaria  (tercera edad, niños y niñas, mujeres embarazadas y personas con discapacidad).\n" +
				"\n" +
				"Si vas a ser evacuado, o evacuas por voluntad propia de tu lugar habitual de residencia, lleva tu mochila de emergencia.  ' WHERE idtipoalerta = '6';");

		database.execSQL("UPDATE tipoalerta SET leyendalarga = ' \nLA ERUPCIÓN VOLCÁNICA ESTÁ EN CURSO \n" +
				"\n" +
				"Sigue  las instrucciones  de las autoridades oficiales y mantén la calma. www.gestionderiesgos.gob.ec  \n" +
				"\n" +
				"Sintoniza Radio Pública (88.1 en Tungurahua y Chimborazo) y permanece atento a las indicaciones de las autoridades. \n" +
				"\n" +
				"Si la autoridad lo dispone, permanece en el lugar en que te encuentras o evacua (según sea el caso)\n" +
				"\n" +
				"Llama al 9-1-1 en caso de emergencia\n" +
				"\n" +
				"SI TE ENCUENTRAS EN UNA ZONA DE RIESGO\n" +
				"\n" +
				"Evacua a pie hacia los puntos de encuentro y  zonas seguras, según tu plan familiar y si las autoridades lo disponen.\n" +
				"\n" +
				"Si no estás junto a tu familia, espera a que pase la emergencia y encuéntrate con ellos en el lugar definido con anterioridad.\n" +
				"\n" +
				"Recuerda que durante la emergencia, es posible que los servicios básicos se interrumpan por lo que debes mantener la calma y actuar acorde a tu plan familiar.\n" +
				"\n" +
				"Si adviertes la presencia de ceniza, sella las puertas y ventanas del lugar donde estés. Procura no salir hasta que las autoridades lo dispongan.\n" +
				"\n" +
				"Espera a que pase la emergencia para contactarte con tu familia, a través de tu grupo en redes sociales, radio o teléfonos convencionales.\n" +
				"\n" +
				"Recuerda que estar informado sobre la evolución del proceso eruptivo y las disposiciones de las autoridades locales o nacionales. Actuar correctamente puede salvar tu vida y la de los tuyos.\n" +
				"\n" +
				"Infórmate sobre la evolución del proceso eruptivo y las disposiciones de las autoridades locales o nacionales. Actuar correctamente puede salvar tu vida y la de los tuyos.' WHERE idtipoalerta = '7';");

		//NINO
		database.execSQL("UPDATE tipoalerta SET leyendalarga = 'ALERTA AMARILLA: Existe un Fenómeno El Niño con una alta probabilidad de intensidad moderada o alta\n" +
				"\n" +
				"Consulta la situación del Fenómeno El Niño periódicamente, en las fuentes oficiales www.gestionderiesgos.gob.ec.\n" +
				"\n" +
				"Infórmate y escucha únicamente las directrices de los voceros nacionales y locales establecidos. www.gestionderiesgos.gob.ec\n" +
				"\n" +
				"Participa activamente en charlas y simulacros organizados por las autoridades locales\n" +
				"\n" +
				"QUÉ ES EL FENÓMENO EL NIÑO\n" +
				"\n" +
				"El Fenómeno “El Niño” es el resultado de la interacción de dos componentes: a) La corriente del Niño es originada por variaciones de la temperatura superficial del mar y lleva su nombre porque se produce durante la época de las fiestas navideñas; y, b) Cambios en la presión atmosférica denominados Oscilación Sur o ENOS.  El Niño se presenta cada cierto número de años; los últimos 2 fenómenos ocurrieron en los períodos 1982-1983 y 1997-1998.\n" +
				"\n" +
				"Las variaciones en las condiciones del tiempo y del clima ocasionadas por este fenómeno repercuten en la sociedad, al provocar principalmente inundaciones en las zonas planas y deslizamientos en las zonas de laderas, entre otros eventos adversos.\n" +
				"\n" +
				"SI VIVES EN ZONA DE RIESGO DE INUNDACIONES O DESLIZAMIENTOS:\n" +
				"\n" +
				"Elabora tu plan familiar con la participación de todos los miembros de tu familia. Identifica rutas de evacuación, puntos de encuentro y zonas seguras.\n" +
				"\n" +
				"Identifica a tu familia acogiente y albergues cercanos.\n" +
				"\n" +
				"Realiza simulacros de evacuación con tu familia, tomando en cuenta distintas situaciones: juntos, separados, de día, de noche, entre semana o fin de semana; y, revísalo permanentemente. \n" +
				"\n" +
				"En caso de evacuación, lleva tu mochila de emergencia familiar con agua, alimento y ropa. Contempla a toda tu familia para su elaboración.\n" +
				"\n" +
				"Conoce el plan de contingencia de tu lugar de trabajo y de la institución educativa de tus hijos.\n" +
				"\n" +
				"Crea tu grupo de contacto con familiares, amigos y vecinos de manera directa, en redes sociales, whatsapp u otra aplicación/medio digital de uso frecuente.' WHERE idtipoalerta = '9';");

		database.execSQL("UPDATE tipoalerta SET leyendalarga = '\n" +
				"ALERTA NARANJA: Aviso de preparación para un Fenómeno El Niño de intensidad moderada a alta (lluvias sobre la normal).\n" +
				"\n " +
				"Se recomienda la evacuación de las zonas de mayor riesgo, especialmente de los grupos de atención prioritaria (tercera edad, niños y niñas, mujeres embarazadas y personas con discapacidad).\n" +
				"\n" +
				"Sigue las instrucciones de las autoridades oficiales y mantén la calma. \n" +
				"\n" +
				"Infórmate constantemente en los medios oficiales, acerca de la evolución del fenómeno y las directrices de los voceros nacionales y locales. www.gestionderiesgos.gob.ec \n" +
				"\n" +
				"Revisa y actualiza tu Plan Familiar y sigue las disposiciones oficiales.\n" +
				"\n" +
				"Actúa siempre solidario, ordenado e informado a través de fuentes oficiales. \n" +
				"\n" +
				"Si necesitas asistencia para la evacuación, para ti o algún familiar con discapacidad, comunica al respecto a los organismos de apoyo.\n" +
				"\n" +
				"PARA LAS ZONAS IDENTIFICADAS DE MAYOR RIESGO: \n" +
				"\n" +
				"En las zonas de alto riesgo, la evacuación se realizará únicamente en los sectores previamente identificados con una mayor exposición.\n" +
				"\n" +
				"Si vas a ser evacuado, o evacuas por voluntad propia de tu lugar habitual de residencia, lleva siempre tu mochila de emergencia. ' WHERE idtipoalerta = '10';");

		database.execSQL("UPDATE tipoalerta SET leyendalarga = '\n" +
				"ALERTA ROJA: El Fenómeno El Niño está en curso\n" +
				"\n" +
				"Sigue  las instrucciones  de las autoridades oficiales y mantén la calma. www.gestionderiesgos.gob.ec \n" +
				"\n" +
				"Si la autoridad lo dispone, permanece en el lugar en que te encuentras o evacua (según sea el caso)\n" +
				"\n" +
				"Llama al 9-1-1 en caso de emergencia. \n" +
				"\n" +
				"SI TE ENCUENTRAS EN UNA ZONA DE RIESGO\n" +
				"\n" +
				"Evacua hacia los puntos de encuentro y  zonas seguras, según tu plan familiar y si las autoridades lo disponen. \n" +
				"\n" +
				"Evita atravesar ríos o zonas inundadas a pie, en animales o vehículos. Si necesitas hacerlo, busca apoyo de personal especializado.\n" +
				"\n" +
				"No cruces puentes donde el nivel del agua es alto, porque sus bases podrían estar afectadas. No te acerques a las alcantarillas.\n" +
				"\n" +
				"Evita el contacto con el agua de la inundación.\n" +
				" \n" +
				"Aléjate de postes eléctricos caídos o alambres rotos en la vía o dentro de áreas inundadas. \n" +
				"\n" +
				"Aléjate de lugares donde puedan producirse derrumbes.\n" +
				"\n" +
				"Si no estás junto a tu familia, espera a que pase la emergencia y encuéntrate con ellos en el lugar definido con anterioridad.\n" +
				"\n" +
				"Recuerda que, durante la emergencia, es posible que los servicios básicos se interrumpan por lo que debes mantener la calma y actuar acorde a tu plan familiar.\n" +
				"\n" +
				"Espera a que pase la emergencia para contactarte con tu familia, a través de tu grupo en redes sociales, radio o teléfonos convencionales.\n" +
				"\n" +
				"Infórmate sobre la evolución del fenómeno y las disposiciones de las autoridades locales o nacionales. Actuar correctamente puede salvar tu vida y la de los tuyos.\n" +
				"\n" +
				"' WHERE idtipoalerta = '11';");

		database.execSQL("UPDATE tipoalerta SET leyendalarga = '\n SITUACIÓN ACTUAL\n\n" +
				"El Fenómeno “El Niño” es el resultado de la interacción de dos componentes: a) la corriente del Niño, originada por variaciones de la temperatura superficial del mar y que lleva su nombre porque se produce durante la época de las fiestas navideñas; y b) Cambios en la presión atmosférica llamado Oscilación Sur o ENOS.  El Niño se presenta cada cierto número de años; los últimos 2 fenómenos ocurrieron en los períodos 1982-1983 y 1997-1998.\n \n" +
				"Las variaciones en las condiciones del tiempo y del clima ocasionadas por este fenómeno, repercuten en la sociedad, al provocar principalmente inundaciones en las zonas planas y deslizamientos en las zonas de laderas, entre otros eventos adversos.\n \n" +
				"Actualmente, los parámetros océano-atmosféricos como la temperatura del mar, nivel medio del mar, patrones de viento, nivel de actividad y ubicación de las zonas sistemas como la Zona de Convergencia Intertropical y Anticiclón del Pacífico, indican un estado cercano a lo normal. \n \n" +
				"Se mantienen las acciones de monitoreo y generación de información permanente.\n\n" +
				"Infórmate siempre a través de medios oficiales, no hagas caso ni difundas rumores (www.gestionderiesgos.gob.ec).\n\n" +
				"Estar atento a las alertas decretadas por las autoridades que se emiten en función de los cambios que se pueden presentar en los parámetros monitoreados.\n \n' WHERE idtipoalerta = '27';");

	}


	public void actualizarBaseDatosTsunami(){
		//TABLA RIESGO

		database.execSQL("INSERT INTO riesgo (idriesgo,nombreriesgo,estadoriesgo,fechaestadoriesgo,alertaactual,fechaalerta) VALUES ('4','TSUNAMIS','T','2016/10/03 18:00','BLA','2016/10/03 08:00');");



		//TABLA TIPOALERTA
		database.execSQL("INSERT INTO tipoalerta (idtipoalerta,codigotipoalerta,nombrealerta,leyendacorta,codigocolor,codigocoloroscuro,codigocolorletra,icono,pregunta,leyendalarga,idriesgo) VALUES \n" +
				"('21','ROJ','ALERTA','','#F81C43','#B20524','#FFFFFF','tsuroja.png','ALERTA','\n" +
				"EXISTE UN PELIGRO INMINENTE DE QUE SE GENERE UN TSUNAMI.\n" +
				"\n" +
				"La Secretaría de Riesgos decretó la “alerta roja” en las zonas costeras del país.\n" +
				" \n" +
				"Evacúa al punto de encuentro más cercano por las rutas indicadas con tu mochila de emergencia y aplicando tu Plan de Emergencia Familiar.\n" +
				"\n" +
				"Sigue las instrucciones de las autoridades oficiales y mantén la calma. Consulta Radio Pública en el dial de tu provincia.\n" +
				"\n" +
				"Infórmate constantemente en los medios oficiales, sobre las directrices de los voceros nacionales y locales establecidos (www.gestionderiesgos.gob.ec, FB Riesgos Ecuador, @Riesgos_Ec).\n" +
				"Consulta la situación de la amenaza y espera hasta que las autoridades indiquen la cancelación de alerta para poder retornar a tu domicilio.  (FB Riesgos Ecuador, @Riesgos_Ec)\n" +
				"Actúa siempre: solidario, ordenado e informado en fuentes oficiales. \n\n" +
				"SI VIVES EN ZONA DE RIESGO:\n" +
				"Evacua a pie hacia los puntos de encuentro, según tu plan familiar. \n" +
				"\n" +
				"Si no estás junto a tu familia, espera a que pase la emergencia y encuéntrate con ellos en el lugar definido con anterioridad.\n" +
				"\n" +
				"Recuerda que, durante la emergencia, es posible que los servicios básicos se interrumpan por lo que debes mantener la calma y actuar acorde a tu plan familiar.\n','4');");

		database.execSQL("INSERT INTO tipoalerta (idtipoalerta,codigotipoalerta,nombrealerta,leyendacorta,codigocolor,codigocoloroscuro,codigocolorletra,icono,pregunta,leyendalarga,idriesgo) VALUES \n" +
				" ('22','NAR','ADVERTENCIA','','#F89E1C','#CA8624','#FFFFFF','tsunaranja.png','ADVERTENCIA','\n" +
				"EXISTE UNA ALTA PROBABILIDAD DE QUE SE GENERE UN TSUNAMI.\n " +
				"\n" +
				"La Secretaría de Riesgos decretó la “alerta naranja” en las zonas costeras del país.\n" +
				" \n" +
				"Evacúa al punto de encuentro más cercano por las rutas indicadas con tu mochila de emergencia y aplicando tu Plan de Emergencia Familiar.\n" +
				"\n" +
				"Sigue las instrucciones de las autoridades oficiales y mantén la calma. Consulta Radio Pública en el dial de tu provincia.\n" +
				"\n" +
				"Infórmate constantemente en los medios oficiales, sobre las directrices de los voceros nacionales y locales establecidos (www.gestionderiesgos.gob.ec, FB Riesgos Ecuador, @Riesgos_Ec).\n\n" +
				"Consulta la situación de la amenaza y espera hasta que las autoridades indiquen la cancelación de alerta para poder retornar a tu domicilio.  (FB Riesgos Ecuador, @Riesgos_Ec)\n\n" +
				"Actúa siempre: solidario, ordenado e informado en fuentes oficiales. \n" +
				"\n" +
				"SI VIVES EN ZONA DE RIESGO:\n\n" +
				"Evacua a pie hacia los puntos de encuentro, según tu plan familiar. \n" +
				"\n" +
				"Si no estás junto a tu familia, espera a que pase la emergencia y encuéntrate con ellos en el lugar definido con anterioridad.\n" +
				"\n" +
				"Recuerda que, durante la emergencia, es posible que los servicios básicos se interrumpan por lo que debes mantener la calma y actuar acorde a tu plan familiar.\n ','4');");

//		database.execSQL("INSERT INTO tipoalerta (idtipoalerta,codigotipoalerta,nombrealerta,leyendacorta,codigocolor,codigocoloroscuro,codigocolorletra,icono,pregunta,leyendalarga,idriesgo) VALUES \n" +
//				" ('23','AMA','OBSERVACIÓN','','#F8E71C','#BEAF04','#424242','tsuamarilla.png','OBSERVACIÓN','\n" +
//				"La Secretaría de Riesgos ha decretado “alerta amarilla” en las zonas costeras del país.\n\n" +
//				"Existe una probabilidad de que se genere un tsunami de origen lejano. El evento requiere resultados de simulación para su evaluación.\n\n" +
//				"Prepárate y mantente informado sobre las disposiciones de las autoridades.\n\n" +
//				"Infórmate siempre a través de medios oficiales, no hagas caso ni difundas rumores (www.gestionderiesgos.gob.ec, FB Riesgos Ecuador, @Riesgos_Ec)\n" +
//				"Consulta la situación de la amenaza durante las dos primeras horas.  (FB Riesgos Ecuador, @Riesgos_Ec)\n\n" +
//				"Participa activamente en charlas y simulacros organizados por las autoridades locales.\n\n" +
//				"SI VIVES EN ZONA DE RIESGO:\n\n" +
//				"Elabora tu plan familiar con la participación de todos los miembros de tu familia. Identifica rutas de evacuación y puntos de encuentro.\n\n" +
//				"Identifica a tu familia acogiente y puntos de encuentro.\n" +
//				"\n" +
//				"Realiza simulacros de evacuación con tu familia, tomando en cuenta distintas situaciones: juntos, separados, de día, de noche, entre semana o fin de semana; y, revísalo permanentemente. \n" +
//				"Prepara una mochila de emergencia familiar o personal.  \n\n" +
//				"Conoce el plan de contingencia de tu lugar de trabajo y el plan de evacuación de la institución educativa de tus hijos.\n\n" +
//				"Si la escuela de tus hijos está en zona de riesgo:\n\n" +
//				"-\tConsulta el plan de contingencia educativo.\n" +
//				"-\tRecorre la ruta de evacuación de tus hijos y el sitio a donde van a ir. \n" +
//				"-\tExplícales por qué razón deben de evacuar y hacia dónde deben de ir. \n" +
//				"-\tExplícales que, de generarse un tsunami, es posible que te demores en recogerlo, por lo que deberá permanecer con sus compañeros y docentes, en calma y seguro.\n" +
//				"\n" +
//				"Crea tu grupo de contacto con familiares, amigos y vecinos de manera directa, en redes sociales, whatsapp u otra aplicación/medio digital de uso frecuente. \n" +
//				"\n" +
//				"RECUERDA\n" +
//				"\n" +
//				"Todas las zonas costeras del mundo pueden experimentar tsunamis. En el Ecuador, la amenaza se concentra mayoritariamente frente a las costas de la Provincia de Esmeraldas, donde en los últimos 100 años se han registrado 3 eventos importantes a partir de sismos cercanos: 1906 (sismo de magnitud 8.8), 1956 (sismo de magnitud 7.7) y 1979 (sismo de magnitud 8.2). \n" +
//				"\n" +
//				"En Esmeraldas, los tsunamis cercanos pueden llegar a la costa aproximadamente 15 minutos después de que el sismo es sentido; mientras que los tsunamis regionales y lejanos, que son los generados en otros lugares del Océano Pacífico, pueden llegar entre 2 a 24 horas de ocurrido el sismo.\n" +
//				"\n" +
//				"Señales que pueden alertar sobre la inminente llegada de un tsunami a las costas: \n\n" +
//				"1.\tSi vives en la costa y sientes un terremoto que te impide mantenerte de pie, o que agriete las paredes, es muy probable que dentro de los próximos minutos llegue un tsunami.\n\n" +
//				"2.\tFrecuentemente, los tsunamis se presentan primero con un recogimiento de mar, el que deja secas grandes extensiones del fondo marino. Tras eso, al cabo de unos minutos, llegará un tsunami con una gran velocidad.\n \n','4');");



		database.execSQL("INSERT INTO tipoalerta (idtipoalerta,codigotipoalerta,nombrealerta,leyendacorta,codigocolor,codigocoloroscuro,codigocolorletra,icono,pregunta,leyendalarga,idriesgo) VALUES \n" +
				" ('25','BLA','SIN ALERTA','','#DADADA','#DADADA','#000000','tsugris.png','SIN ALERTA','\n" +
				"CONOZCA QUÉ ES UN TSUNAMI   \n" +
				"\n" +
				"Tsunami (tsu: puerto o bahía; nami: ola) es el término japonés que se atribuye una serie de olas generadas en el océano que se desplazan a gran velocidad en todas las direcciones desde su punto de origen, a partir de un terremoto, erupciones volcánicas o deslizamientos submarinos, el cual impulsa y desplaza verticalmente la columna de agua. \n \n" +
				"Estas olas al aproximarse a la costa, sufren alteraciones en su velocidad y altura, alcanzando grandes proporciones, por lo que descargan su energía con un gran poder destructor. Existen tsunamis de origen cercano, regional y lejano, de acuerdo al lugar donde se originen. \n \n" +
				" Señales que pueden alertar sobre la inminente llegada de un tsunami a las costas: \n \n" +
				"1. Si vives en la costa y sientes un terremoto que te impide mantenerte de pie, o que agriete las paredes, es muy probable que dentro de los próximos minutos llegue un tsunami. \n" +
				"2. Frecuentemente, los tsunamis se presentan primero con un recogimiento de mar, el que deja secas grandes extensiones del fondo marino. Tras eso, al cabo de unos minutos, llegará un tsunami con una gran velocidad. \n \n" +
				"TSUNAMIS EN EL ECUADOR \n \n" +
				"Todas las zonas costeras del mundo pueden experimentar tsunamis. En el Ecuador, la amenaza se concentra mayoritariamente frente a las costas de la Provincia de Esmeraldas, donde en los últimos 100 años se han registrado 3 eventos importantes a partir de sismos cercanos: 1906 (sismo de magnitud 8.8), 1956 (sismo de magnitud 7.7) y 1979 (sismo de magnitud 8.2).  \n" +
				" \n" +
				"En Esmeraldas, los tsunamis cercanos pueden llegar a la costa aproximadamente 15 minutos después de que el sismo es sentido; mientras que los tsunamis regionales y lejanos, que son los generados en otros lugares del Océano Pacífico, pueden llegar entre 2 a 24 horas de ocurrido el sismo. \n \n" +
				" RECUERDA \n" +
				"Un tsunami puede tener más de diez olas destructivas en un lapso de 12 horas por lo cual, no debes volver a la zona afectada, hasta que las autoridades lo indiquen. \n" +
				"Es importante recordar que los tsunamis pueden penetrar por un río o un estero varios kilómetros tierra adentro; por lo tanto debes alejarte de los mismos.','4');");


		//BLANCA PARA TUNGURAHUA Y NINO
		database.execSQL("INSERT INTO tipoalerta (idtipoalerta,codigotipoalerta,nombrealerta,leyendacorta,codigocolor,codigocoloroscuro," +
				"codigocolorletra,icono,pregunta,leyendalarga,idriesgo) VALUES \n" +
				"('26','BLA','SITUACIÓN ACTUAL','','#DADADA','#DADADA','#000000','gris.png'," +
				"'SITUACIÓN ACTUAL','El volcán Tungurahua está ubicado en la Cordillera Real del Ecuador, a una altura de 5020 msnm, a 120 km al sur de Quito, 33 km al SE de Ambato y a 8 km al norte de la ciudad de Baños. Desde el año 1300 AD, el Tunguragua ha producido erupciones con flujos piroclásticos, caídas de ceniza, flujos de lava y lahares, al menos una vez por siglo (LePennec et al., 2008).\n" +
				"\n" +
				"El período eruptivo actual se inició en 1999 y persiste hasta el momento. Al inicio la erupción fue subcontinua caracterizada por explosiones estrombolianas y vulcanianas; y emisiones de gases y ceniza. En Julio y Agosto de 2006 se profujeron dos grandes erupciones explosivas con formación de flujos piroclásticos. Desde entonces el volcán ha mantenido episodios de actividad intermitentes con duraciones de pocos días a semanas y pausas en la actividad de hasta 3 meses. En Mayo 2010, Diciembre 2012 y Julio 2013, la actividad se iniciò con fuertes explosiones vulcanianas.\n" +
				"\n" +
				"Actualmente, los parámetros sísmicos, de concentraciones de gases de dióxido de azufre (SO2) y de deformación del volcán, monitoreados por el Instituto Geofísico de la Escuela Politécnica Nacional, registran valores normales.\n" +
				"\n" +
				"Se mantienen las acciones de: monitoreo y generación de información permanente, actualización periódica de los planes de contingencia, así como las acciones de prevención y mitigación.\n" +
				"\n" +
				"Consulta la situación del volcán periódicamente (www.igepn.edu.ec/informes-tungurahua/tung-diarios).\n" +
				"\n" +
				"Infórmate siempre a través de medios oficiales, no hagas caso ni difundas rumores (www.gestionderiesgos.gob.ec).\n" +
				"\n" +
				"Estar atento a las alertas decretadas por las autoridades que se emiten en función de los cambios que se pueden presentar en los parámetros del volcán.','2');");

		//BLANCA PARA TUNGURAHUA Y NINO
		database.execSQL("INSERT INTO tipoalerta (idtipoalerta,codigotipoalerta,nombrealerta,leyendacorta,codigocolor,codigocoloroscuro," +
				"codigocolorletra,icono,pregunta,leyendalarga,idriesgo) VALUES \n" +
				"('27','BLA','SITUACIÓN ACTUAL','','#DADADA','#DADADA','#000000','fenomenonino.png'," +
				"'SITUACIÓN ACTUAL','SITUACIÓN ACTUAL\n\n" +
				"El Fenómeno “El Niño” es el resultado de la interacción de dos componentes: a) la corriente del Niño, originada por variaciones de la temperatura superficial del mar y que lleva su nombre porque se produce durante la época de las fiestas navideñas; y b) Cambios en la presión atmosférica llamado Oscilación Sur o ENOS.  El Niño se presenta cada cierto número de años; los últimos 2 fenómenos ocurrieron en los períodos 1982-1983 y 1997-1998.\n \n" +
				"Las variaciones en las condiciones del tiempo y del clima ocasionadas por este fenómeno, repercuten en la sociedad, al provocar principalmente inundaciones en las zonas planas y deslizamientos en las zonas de laderas, entre otros eventos adversos.\n \n" +
				"Actualmente, los parámetros océano-atmosféricos como la temperatura del mar, nivel medio del mar, patrones de viento, nivel de actividad y ubicación de las zonas sistemas como la Zona de Convergencia Intertropical y Anticiclón del Pacífico, indican un estado cercano a lo normal. \n \n" +
				"Se mantienen las acciones de monitoreo y generación de información permanente.\n\n" +
				"Consulta más información sobre el Fenómeno El Niño en www.fenomenoelninoec.com/\n\n" +
				"Infórmate siempre a través de medios oficiales, no hagas caso ni difundas rumores (www.gestionderiesgos.gob.ec).\n\n" +
				"Estar atento a las alertas decretadas por las autoridades que se emiten en función de los cambios que se pueden presentar en los parámetros monitoreados.\n \n','3');");


		//TABLA ELEMENTOEMERGENCIA
		database.execSQL("INSERT INTO elementoemergencia (idelementoemergencia,idriesgo,ordenelementoemergencia,nombreelementoemergencia,checklist,icono) VALUES \n" +
				"('56','4','M','Documentos personales y de su vivienda; y, teléfonos de los miembros de la familia','0','documentos.png');");
		database.execSQL("INSERT INTO elementoemergencia (idelementoemergencia,idriesgo,ordenelementoemergencia,nombreelementoemergencia,checklist,icono) VALUES  \n" +
				"('58','4','J','Linterna a pilas','0','linterna.png');");
		database.execSQL("INSERT INTO elementoemergencia (idelementoemergencia,idriesgo,ordenelementoemergencia,nombreelementoemergencia,checklist,icono) VALUES  \n" +
				"('59','4','A','Alimentos no perecibles','0','alimentos.png');");
		database.execSQL("INSERT INTO elementoemergencia (idelementoemergencia,idriesgo,ordenelementoemergencia,nombreelementoemergencia,checklist,icono) VALUES \n" +
				" ('60','4','C','Un juego adicional de ropa y abrigo','0','ropa.png');");
		database.execSQL("INSERT INTO elementoemergencia (idelementoemergencia,idriesgo,ordenelementoemergencia,nombreelementoemergencia,checklist,icono) VALUES\n" +
				" ('61','4','B','Agua limpia cerrada o embotellada','0','agua.png');");
		database.execSQL("INSERT INTO elementoemergencia (idelementoemergencia,idriesgo,ordenelementoemergencia,nombreelementoemergencia,checklist,icono) VALUES \n" +
				" ('62','4','P','Botiquín o bolso con medicina para atender heridos','0','botiquin.png');");
		database.execSQL("INSERT INTO elementoemergencia (idelementoemergencia,idriesgo,ordenelementoemergencia,nombreelementoemergencia,checklist,icono) VALUES\n" +
				" ('63','4','D','Manta delgada y liviana','0','frazadas.png');");
		database.execSQL("INSERT INTO elementoemergencia (idelementoemergencia,idriesgo,ordenelementoemergencia,nombreelementoemergencia,checklist,icono) VALUES \n" +
				" ('64','4','H','Radio a pilas','0','radio.png');");
		database.execSQL("INSERT INTO elementoemergencia (idelementoemergencia,idriesgo,ordenelementoemergencia,nombreelementoemergencia,checklist,icono) VALUES\n" +
				" ('65','4','L','Copia de las llaves de la vivienda y del auto','0','llaves.png');");
		database.execSQL("INSERT INTO elementoemergencia (idelementoemergencia,idriesgo,ordenelementoemergencia,nombreelementoemergencia,checklist,icono) VALUES\n" +
				" ('66','4','K','Bolsas plásticas','0','bolsas.png');");
		database.execSQL("INSERT INTO elementoemergencia (idelementoemergencia,idriesgo,ordenelementoemergencia,nombreelementoemergencia,checklist,icono) VALUES\n" +
				" ('67','4','I','Velas y fósforos','0','velas.png');");
		database.execSQL("INSERT INTO elementoemergencia (idelementoemergencia,idriesgo,ordenelementoemergencia,nombreelementoemergencia,checklist,icono) VALUES \n" +
				" ('70','4','N','Tipo de sangre de cada miembro de la familia y medicinas de uso frecuente (incluya la cantidad y frecuencia de uso)','0','medicina.png');");


	}
}