import java.time.LocalDate;
import java.util.ArrayList;

public class ControlProduccion {
    ArrayList<Persona> personas = new ArrayList<>();
    ArrayList<Huerto> huertos = new ArrayList<>();
    ArrayList<Cultivo> cultivos = new ArrayList<>();
    ArrayList<PlanCosecha> planes = new ArrayList<>();

    public ControlProduccion(){}

    public boolean createPropietario(Rut rut, String nombre, String email, String dirParticular, String dirComercial){
        return buscaPersona(rut) == null && personas.add(new Propietario(rut, nombre, email, dirParticular, dirComercial));
    }

    public boolean createSupervisor(Rut rut, String nombre, String email, String direccion, String profesion){
        return buscaPersona(rut) == null && personas.add(new Supervisor(rut, nombre, email, direccion, profesion));
    }

    public boolean createCosechador(Rut rut, String nom, String email, String direccion, LocalDate fNac){
        return buscaPersona(rut) == null && personas.add(new Cosechador(rut, nom, email, direccion, fNac));
    }

    public boolean createCultivo(int id, String especie, String variedad, float rendimiento){
        return buscaCultivo(id) == null && cultivos.add(new Cultivo(id, especie, variedad, rendimiento));
    }

    public boolean createHuerto(String nombre, float superficie, String ubicacion, Rut rutPropietario){
        Huerto huerto = buscaHuerto(nombre);
        Persona persona = buscaPersona(rutPropietario);

        if(huerto != null || !(persona instanceof Propietario propietario)) {return false;}

        return huertos.add(new Huerto(nombre, superficie, ubicacion, propietario));
    }

    public boolean addCuartelToHuerto(String nombreHuerto, int idCuartel, float superficie, int idCultivo){
        Huerto huerto = buscaHuerto(nombreHuerto);
        Cultivo cultivo = buscaCultivo(idCultivo);

        if(huerto == null || cultivo == null) {return false;}
        //addCuartel ya hace verificación de duplicados en Huerto
        return huerto.addCuartel(idCuartel, superficie, cultivo);
    }

    public boolean addCosechadorToCuadrilla(int idPlan, int idCuadrilla, LocalDate fIni, LocalDate fFin, double metaKilos, Rut rut) {
        PlanCosecha plan = buscaPlan(idPlan);
        Persona persona = buscaPersona(rut);

        if (plan == null || !(persona instanceof Cosechador cosechador)) {
            return false;
        }

        for (Cuadrilla c : plan.getCuadrillas()) {
            if (c.getId() == idCuadrilla) {
                if (!plan.getInicio().isBefore(fIni) && !fechaFinPlan(plan).isAfter(fFin)) {
                    return plan.addCosechadorToCuadrilla(idCuadrilla, fIni, fFin, metaKilos, cosechador);
                }
                return false; // fuera de rango de fechas
            }
        }

        return false; // cuadrilla no encontrada
    }

    private PlanCosecha buscaPlan(int idPlan) {
        for (PlanCosecha p : planes) {
            if (p.getId() == idPlan) {return p;}
        }
        return null;
    }
    private Persona buscaPersona(Rut rut){
        for(Persona p: personas){
            if(p.getRut().equals(rut)) {return p;}
        }
        return null;
    }
    private Huerto buscaHuerto(String nombre){
        for(Huerto h: huertos){
            if(h.getNombre().equalsIgnoreCase(nombre)) {return h;}
        }
        return null;
    }
    private Cultivo buscaCultivo(int id){
        for(Cultivo c: cultivos){
            if(c.getId() == id){return c;}
        }
        return null;
    }
    private LocalDate fechaFinPlan(PlanCosecha p){
        return (p.getFinReal() != null) ? p.getFinReal() : p.getFinEstimado();
    }

    public boolean createPlanCosecha(int idPlan, String nomPlan, LocalDate fIni, LocalDate fFin, double meta, double precio, String nomHuerto, int idCuartel) {
    }
}
