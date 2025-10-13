import java.time.LocalDate;
import java.util.ArrayList;

public class Cuadrilla {
    private int id;
    private String nombre;
    private static int maximoCosechadores;

    private Supervisor supervisor;
    private PlanCosecha planCosecha;
    private ArrayList<CosechadorAsignado> asignaciones;

    public Cuadrilla(int id, String nom, Supervisor sup, PlanCosecha plan) {
        this.id = id;
        this.nombre = nom;
        this.supervisor = sup;
        this.planCosecha = plan;
        this.asignaciones = new ArrayList<>();
        sup.setCuadrilla(this); //relación bicondicional
    }

    public int getId() { return id; }
    public String getNombre() { return nombre; }
    public void setNombre(String nombre) { this.nombre = nombre; }
    public Supervisor getSupervisor() { return supervisor; }
    public PlanCosecha getPlanCosecha() { return planCosecha; }

    public boolean addCosechador(LocalDate fIni, LocalDate fFin, double meta, Cosechador cos) {
        if (asignaciones.size() >= maximoCosechadores) {
            return false;
        }
        if(findCosechadorByRut(cos) != null) {return false;}
        //CosechadorAsignado ya se encarga de enlazar la cuadrilla con el cosechador
        CosechadorAsignado nueva = new CosechadorAsignado(fIni, fFin, meta, this, cos);
        asignaciones.add(nueva);
        return true;
    }
    public Cosechador[] getCosechadores() {
        Cosechador[] arr = new Cosechador[asignaciones.size()];
        for (int i = 0; i < asignaciones.size(); i++) {
            arr[i] = asignaciones.get(i).getCosechador();
        }
        return arr;
    }
    public static int getMaximoCosechadores() { return maximoCosechadores; }
    public static void setMaximoCosechadores(int max) { maximoCosechadores = max; }

    private CosechadorAsignado findCosechadorByRut(Cosechador cos) {
        for (CosechadorAsignado ca : asignaciones) {
            if (ca.getCosechador().getRut().equals(cos.getRut())) {
                return ca;
            }
        }
        return null;
    }
}
