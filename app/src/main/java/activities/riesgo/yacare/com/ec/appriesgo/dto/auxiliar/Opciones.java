package activities.riesgo.yacare.com.ec.appriesgo.dto.auxiliar;

import android.widget.Button;

import java.io.Serializable;


/**
 * Created by yacare on 22/09/2015.
 */
public class Opciones implements Serializable{

    private String id;
    private String nombre;
    private String leyenda;
    private Button btnOpcion;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public String getLeyenda() {
        return leyenda;
    }

    public void setLeyenda(String leyenda) {
        this.leyenda = leyenda;
    }

    public Button getBtnOpcion() {
        return btnOpcion;
    }

    public void setBtnOpcion(Button btnOpcion) {
        this.btnOpcion = btnOpcion;
    }
}
