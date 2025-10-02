import java.time.LocalDate;
import java.util.ArrayList;

public class ControlProduccion {
    ArrayList<Persona> personas = new ArrayList<>();
    ArrayList<Huerto> huertos = new ArrayList<>();
    ArrayList<Cultivo> cultivos = new ArrayList<>();

    public ControlProduccion(){

    }

    public boolean createPropietario(Rut rut, String nombre, String email, String dirParticular, String dirComercial){
        for(int i = 0; i < personas.size(); i++){
            if(personas.get(i).getRut().equals(rut)){
                return false;
            }
        }
        Propietario p = new Propietario(rut, nombre, email, dirParticular, dirComercial);
        return personas.add(p);
    }

    public boolean createSupervisor(Rut rut, String nombre, String email, String direccion, String profesion){
        for(int i = 0; i < personas.size(); i++){
            if(personas.get(i).getRut().equals(rut)){
                return false;
            }
        }
        Supervisor p = new Supervisor(rut, nombre, email, direccion, profesion);
        return personas.add(p);
    }

    public boolean createCosechador(Rut rut, String nom, String email, String direccion, LocalDate fNac){
        for(int i = 0; i < personas.size(); i++){
            if(personas.get(i).getRut().equals(rut)){ //no c como uasr otro metodo para comparar sin equals :'v
                return false;
            }
        }
        Cosechador c = new Cosechador(rut, nom, email, direccion, fNac);
        return personas.add(c);
    }
}
