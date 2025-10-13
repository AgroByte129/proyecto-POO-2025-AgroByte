import java.util.ArrayList;
import java.util.List;

public class Propietario extends Persona{
    private String direccionCom;
    private List<Huerto> huertos = new ArrayList<>();

    public Propietario(Rut rut, String nom, String email, String dir, String direccionCom) {
        super(rut, nom, email,  dir);
        this.direccionCom = direccionCom;
    }

    public String getDireccionComercial(){return direccionCom;}
    public void setDireccionComercial(String direccion){direccionCom = direccion;}
    public boolean addHuerto(Huerto huerto){return huertos.add(huerto);}
    public Huerto[] getHuertos(){
        return huertos.toArray(new Huerto[0]);
    }
}





