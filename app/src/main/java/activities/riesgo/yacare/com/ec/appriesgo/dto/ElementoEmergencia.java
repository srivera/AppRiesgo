package activities.riesgo.yacare.com.ec.appriesgo.dto;

import android.widget.CheckBox;
import android.widget.ImageView;

/**
 * Created by yacare on 22/09/2015.
 */
public class ElementoEmergencia {

    private String id;
    private String idRiesgo;
    private String orden;
    private String nombre;
    private String check;
    private String icono;
    private ImageView imageView;
    private CheckBox checkBox;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getIdRiesgo() {
        return idRiesgo;
    }

    public void setIdRiesgo(String idRiesgo) {
        this.idRiesgo = idRiesgo;
    }

    public String getOrden() {
        return orden;
    }

    public void setOrden(String orden) {
        this.orden = orden;
    }

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public String getCheck() {
        return check;
    }

    public void setCheck(String check) {
        this.check = check;
    }

    public String getIcono() {
        return icono;
    }

    public void setIcono(String icono) {
        this.icono = icono;
    }

    public CheckBox getCheckBox() {
        return checkBox;
    }

    public void setCheckBox(CheckBox checkBox) {
        this.checkBox = checkBox;
    }

    public ImageView getImageView() {
        return imageView;
    }

    public void setImageView(ImageView imageView) {
        this.imageView = imageView;
    }
}
