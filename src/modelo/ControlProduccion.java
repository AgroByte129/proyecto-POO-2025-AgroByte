package modelo;

import utilidades.Rut;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

public class ControlProduccion {
    ArrayList<Persona> personas = new ArrayList<>();
    ArrayList<Huerto> huertos = new ArrayList<>();
    ArrayList<Cultivo> cultivos = new ArrayList<>();
    ArrayList<PlanCosecha> planes = new ArrayList<>();

    public ControlProduccion(){
    }

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
        Huerto huerto = buscaHuerto(nombreHuerto);//busca si existe el huerto
        Cultivo cultivo = buscaCultivo(idCultivo);//busca si existe el cultivo

        if(huerto == null || cultivo == null) {return false;}
        //addCuartel ya hace verificación de duplicados en modelo.Huerto
        return huerto.addCuartel(idCuartel, superficie, cultivo);
    }

    public boolean createPlanCosecha(int idPlan, String nomPlan, LocalDate fIni, LocalDate fFin, double meta, double precio, String nomHuerto, int idCuartel) {

        if (buscaPlan(idPlan) != null) {
            System.out.println("Ya existe un plan de cosecha con ese identificador.");
            return false;
        }

        Huerto huerto = buscaHuerto(nomHuerto);
        if (huerto == null) {
            System.out.println("No existe un huerto con el nombre indicado.");
            return false;
        }

        Cuartel cuartel = huerto.getCuartel(idCuartel);
        if (cuartel == null) {
            System.out.println("No existe un cuartel con ese identificador en el huerto dado.");
            return false;
        }
        if (!fFin.isAfter(fIni)) {
            System.out.println("La fecha de termino debe ser posterior a la de inicio.");
            return false;
        }

        PlanCosecha plan = new PlanCosecha(idPlan, nomPlan, fIni, fFin, meta, precio, cuartel);
        return planes.add(plan);
    }

    public boolean addCuadrillaToPlan(int idPlan, int idCuad, String nomCuad, Rut rutSup){
        PlanCosecha plan = buscaPlan(idPlan);
        Persona persona = buscaPersona(rutSup);

        if(plan == null){return false;}
        if(!(persona instanceof Supervisor s)) {return false;}
        if(s.getCuadrilla() != null) {return false;}

        return plan.addCuadrilla(idCuad, nomCuad, s);
    }

    public boolean addCosechadorToCuadrilla(int idPlan, int idCuadrilla, LocalDate fIni, LocalDate fFin, double metaKilos, Rut rut) {
        PlanCosecha plan = buscaPlan(idPlan);
        Persona persona = buscaPersona(rut);

        if (plan == null || !(persona instanceof Cosechador cosechador)) {
            return false;
        }

        for (Cuadrilla c : plan.getCuadrillas()) {
            if (c.getId() == idCuadrilla) {
                if (fechaEnRangoPlan(plan, fIni, fFin)) {
                    return plan.addCosechadorToCuadrilla(idCuadrilla, fIni, fFin, metaKilos, cosechador);
                }
                return false; // fuera de rango de fechas
            }
        }

        return false; // cuadrilla no encontrada
    }

    public String[] listCultivos() {
        if (cultivos.isEmpty()) {
            return new String[0];
        }

        String[] listaCultivos = new String[cultivos.size()];

        for (int i = 0; i < cultivos.size(); i++) {
            Cultivo c = cultivos.get(i);
            listaCultivos[i] = String.format(
                    "%-6d %-15s %-15s %-10.1f %-15d",
                    c.getId(),
                    c.getEspecie(),
                    c.getVariedad(),
                    c.getRendimiento(),
                    c.getCuarteles().length
            );
        }

        return listaCultivos;
    }

    public String[] listHuertos(){
        String[] listaHuertos = new String[huertos.size()];
        if(huertos.isEmpty()){
            return new String[0];
        }
        for(int i = 0; i <huertos.size() ; i++){
            Huerto h = huertos.get(i);
            listaHuertos[i] = String.format("%-20s %-12.1f %-20s %-15s %-20s %-15d\n" ,
                    h.getNombre(),h.getSuperficie(),h.getUbicacion(),h.getPropietario().getRut(),
                    h.getPropietario().getNombre(),h.getCuarteles().length);
        }
        return  listaHuertos;
    }

    public String[] listPropietarios() {
        List<String> lista = new ArrayList<>();
        for (Persona persona : personas) {
            if (persona instanceof Propietario p) {
                lista.add(getDatosPersona(p));
            }
        }
        return lista.toArray(new String[0]);
    }

    public String[] listSupervisores(){
        List<String> lista = new ArrayList<>();
        for (Persona persona : personas) {
            if(persona instanceof Supervisor s){
                lista.add(getDatosPersona(s));
            }
        }
        return lista.toArray(new String[0]);
    }

    public String[] listCosechadores(){
        List<String> lista = new ArrayList<>();
        for (Persona persona : personas) {
            if(persona instanceof Cosechador cos) {
                lista.add(getDatosPersona(cos));
            }
        }
        return lista.toArray(new String[0]);
    }

    public String[] listPlanesCosecha(){
        String[] planesCosecha = new String[planes.size()];

        for(int i = 0; i < planes.size(); i++){
            PlanCosecha p = planes.get(i);
            LocalDate finPlan = (p.getFinReal() != null) ? p.getFinReal():p.getFinEstimado();
            Cuartel c = p.getCuartel();
            Huerto h = c.getHuerto();

            planesCosecha[i] = String.format("%-6d, %-15s, %-15s, %-15s, %-10.1f, %-17.1f," +
                    " %-12s, %-12d, %-20s, %-15d",p.getId(), p.getNombre(),
                    p.getInicio(), finPlan, p.getMetaKilos(), p.getPrecioBaseKilo(),
                    p.getEstado(), c.getId(), h.getNombre(), p.getCuadrillas().length);
        }
        return planesCosecha;
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
    private boolean fechaEnRangoPlan(PlanCosecha p, LocalDate fIni, LocalDate fFin){
        LocalDate fechaFinPlan = (p.getFinReal() != null) ? p.getFinReal() : p.getFinEstimado();
        return !fIni.isBefore(p.getInicio()) && !fFin.isAfter(fechaFinPlan);
    }
    private String getDatosPersona(Persona persona){
        if(persona instanceof Propietario p){
            return String.format("%-12s, %-15s, %-20s, %-25s, %-25s, %-15d",
                    p.getRut(),
                    p.getNombre(),
                    p.getDireccion(),
                    p.getEmail(),
                    p.getDireccionComercial(),
                    p.getHuertos().length);
        } else if(persona instanceof Supervisor s){
            return String.format("%-12s, %-15s, %-20s, %-25s, %-25s, %-15s",
                    s.getRut(),
                    s.getNombre(),
                    s.getDireccion(),
                    s.getEmail(),
                    s.getProfesion(),
                    (s.getCuadrilla() == null ? "S/A" : s.getCuadrilla().getNombre()));
        } else if(persona instanceof Cosechador c){
            return String.format("%-12s, %-15s, %-20s, %-25s, %-25s, %-15d",
                    c.getRut(),
                    c.getNombre(),
                    c.getDireccion(),
                    c.getEmail(),
                    c.getFechaNacimiento(),
                    c.getCuadrillas().length);
        }
        return "";
    }

    private void generateTestData() {
    }

}
