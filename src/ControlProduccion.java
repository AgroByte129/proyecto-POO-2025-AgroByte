import java.time.LocalDate;
import java.util.ArrayList;

public class ControlProduccion {
    ArrayList<Persona> personas = new ArrayList<>();
    ArrayList<Huerto> huertos = new ArrayList<>();
    ArrayList<Cultivo> cultivos = new ArrayList<>();
    ArrayList<PlanCosecha> planes = new ArrayList<>();

    public ControlProduccion(){}

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

    public boolean createCultivo(int id, String especie, String variedad, float rendimiento){
        for(Cultivo c:  cultivos){
            if(c.getId() == id){return false;}
        }
        return cultivos.add(new Cultivo(id, especie, variedad, rendimiento));
    }

    public boolean createHuerto(String nombre, float superficie, String ubicacion, Rut rutPropietario){
        for(Huerto h: huertos){
            if(h.getNombre().equals(nombre)){
                return false;
            }
        }//Este codigo verifica que no exista otro huerto con el mismo nombre
        for(Persona p: personas){
            if(p.getRut().equals(rutPropietario) && p instanceof Propietario prop){
                Huerto h = new Huerto(nombre, superficie, ubicacion, prop);
                huertos.add(h);
                prop.addHuerto(h);
                return true;
            }//Este otro trozo verifica que exista el rut y sea propietario la persona
        }//si lo pilla, agrega al huerto de la persona el huerto recien creado y además
        return false;//Añade el huerto a la coleccion de control producción
    }
}
