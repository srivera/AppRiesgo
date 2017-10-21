package activities.riesgo.yacare.com.ec.appriesgo.dto;

import android.widget.Button;

/**
 * Created by yacare on 22/09/2015.
 */
public class Contacto {


    private String id;
    private String nombre;
    private String tipo;

    private Button eliminar;

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

    public Button getEliminar() {
        return eliminar;
    }

    public void setEliminar(Button eliminar) {
        this.eliminar = eliminar;
    }


    public String getTipo() {
        return tipo;
    }

    public void setTipo(String tipo) {
        this.tipo = tipo;
    }
}
