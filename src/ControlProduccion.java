import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

public class ControlProduccion {
    ArrayList<Persona> personas = new ArrayList<>();
    ArrayList<Huerto> huertos = new ArrayList<>();
    ArrayList<Cultivo> cultivos = new ArrayList<>();
    ArrayList<PlanCosecha> planes = new ArrayList<>();

    public ControlProduccion() {
        generateTestData();
    }

    public boolean createPropietario(Rut rut, String nombre, String email, String dirParticular, String dirComercial) {
        return buscaPersona(rut) == null && personas.add(new Propietario(rut, nombre, email, dirParticular, dirComercial));
    }

    public boolean createSupervisor(Rut rut, String nombre, String email, String direccion, String profesion) {
        return buscaPersona(rut) == null && personas.add(new Supervisor(rut, nombre, email, direccion, profesion));
    }

    public boolean createCosechador(Rut rut, String nom, String email, String direccion, LocalDate fNac) {
        return buscaPersona(rut) == null && personas.add(new Cosechador(rut, nom, email, direccion, fNac));
    }

    public boolean createCultivo(int id, String especie, String variedad, float rendimiento) {
        return buscaCultivo(id) == null && cultivos.add(new Cultivo(id, especie, variedad, rendimiento));
    }

    public boolean createHuerto(String nombre, float superficie, String ubicacion, Rut rutPropietario) {
        Huerto huerto = buscaHuerto(nombre);
        Persona persona = buscaPersona(rutPropietario);

        if (huerto != null || !(persona instanceof Propietario propietario)) {
            return false;
        }

        return huertos.add(new Huerto(nombre, superficie, ubicacion, propietario));
    }

    public boolean addCuartelToHuerto(String nombreHuerto, int idCuartel, float superficie, int idCultivo) {
        Huerto huerto = buscaHuerto(nombreHuerto);
        Cultivo cultivo = buscaCultivo(idCultivo);

        if (huerto == null || cultivo == null) {
            return false;
        }
        //addCuartel ya hace verificación de duplicados en Huerto
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

    public boolean addCuadrillaToPlan(int idPlan, int idCuad, String nomCuad, Rut rutSup) {
        PlanCosecha plan = buscaPlan(idPlan);
        Persona persona = buscaPersona(rutSup);

        if (plan == null) {
            return false;
        }
        if (!(persona instanceof Supervisor s)) {
            return false;
        }
        if (s.getCuadrilla() != null) {
            return false;
        }

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

    public String[] listPropietarios() {
        List<String> lista = new ArrayList<>();
        for (Persona persona : personas) {
            if (persona instanceof Propietario p) {
                lista.add(getDatosPersona(p));
            }
        }
        return lista.toArray(new String[0]);
    }

    public String[] listSupervisores() {
        List<String> lista = new ArrayList<>();
        for (Persona persona : personas) {
            if (persona instanceof Supervisor s) {
                lista.add(getDatosPersona(s));
            }
        }
        return lista.toArray(new String[0]);
    }

    public String[] listCosechadores() {
        List<String> lista = new ArrayList<>();
        for (Persona persona : personas) {
            if (persona instanceof Cosechador cos) {
                lista.add(getDatosPersona(cos));
            }
        }
        return lista.toArray(new String[0]);
    }

    public String[] listPlanesCosecha() {
        String[] planesCosecha = new String[planes.size()];

        for (int i = 0; i < planes.size(); i++) {
            PlanCosecha p = planes.get(i);
            LocalDate finPlan = (p.getFinReal() != null) ? p.getFinReal() : p.getFinEstimado();
            Cuartel c = p.getCuartel();
            Huerto h = c.getHuerto();

            planesCosecha[i] = String.format("%-6d, %-15s, %-15s, %-15s, %-10.1f, %-17.1f," +
                            " %-12s, %-12d, %-20s, %-15d", p.getId(), p.getNombre(),
                    p.getInicio(), finPlan, p.getMetaKilos(), p.getPrecioBaseKilo(),
                    p.getEstado(), c.getId(), h.getNombre(), p.getCuadrillas().length);
        }
        return planesCosecha;
    }

    public String[] listHuertos() {
        String[] listaHuertos = new String[huertos.size()];
        if (huertos.size() == 0) {
            return new String[0];
        }
        for (int i = 0; i < huertos.size(); i++) {
            Huerto h = huertos.get(i);
            listaHuertos[i] = String.format("%-20s %-12.1f %-20s %-15s %-20s %-15d\n",
                    h.getNombre(), h.getSuperficie(), h.getUbicacion(), h.getPropietario().getRut(),
                    h.getPropietario().getNombre(), h.getCuarteles().length);
        }
        return listaHuertos;
    }

    public String[] listCultivos() {
        if (cultivos.isEmpty()) {
            return new String[0];
        }

        String[] listaCultivos = new String[cultivos.size()];

        for (int i = 0; i < cultivos.size(); i++) {
            Cultivo c = cultivos.get(i);
            listaCultivos[i] = String.format(
                    "%-6d %-15s %-15s %-10.1f",
                    c.getId(),
                    c.getEspecie(),
                    c.getVariedad(),
                    c.getRendimiento()
            );
        }

        return listaCultivos;
    }

    private PlanCosecha buscaPlan(int idPlan) {
        for (PlanCosecha p : planes) {
            if (p.getId() == idPlan) {
                return p;
            }
        }
        return null;
    }

    private Persona buscaPersona(Rut rut) {
        for (Persona p : personas) {
            if (p.getRut().equals(rut)) {
                return p;
            }
        }
        return null;
    }

    private Huerto buscaHuerto(String nombre) {
        for (Huerto h : huertos) {
            if (h.getNombre().equalsIgnoreCase(nombre)) {
                return h;
            }
        }
        return null;
    }

    private Cultivo buscaCultivo(int id) {
        for (Cultivo c : cultivos) {
            if (c.getId() == id) {
                return c;
            }
        }
        return null;
    }

    private boolean fechaEnRangoPlan(PlanCosecha p, LocalDate fIni, LocalDate fFin) {
        LocalDate fechaFinPlan = (p.getFinReal() != null) ? p.getFinReal() : p.getFinEstimado();
        return !p.getInicio().isBefore(fIni) && !fechaFinPlan.isAfter(fFin);
    }

    private String getDatosPersona(Persona persona) {
        if (persona instanceof Propietario p) {
            return String.format("%-12s, %-15s, %-20s, %-25s, %-25s, %-15d",
                    p.getRut(),
                    p.getNombre(),
                    p.getDireccion(),
                    p.getEmail(),
                    p.getDireccionComercial(),
                    p.getHuertos().length);
        } else if (persona instanceof Supervisor s) {
            return String.format("%-12s, %-15s, %-20s, %-25s, %-25s, %-15s",
                    s.getRut(),
                    s.getNombre(),
                    s.getDireccion(),
                    s.getEmail(),
                    s.getProfesion(),
                    (s.getCuadrilla() == null ? "S/A" : s.getCuadrilla().getNombre()));
        } else if (persona instanceof Cosechador c) {
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
        // ==== CREAR PERSONAS ====
        Rut rutProp1 = new Rut("12345678-9");
        Rut rutProp2 = new Rut("98765432-1");
        Rut rutSup1 = new Rut("23456789-0");
        Rut rutSup2 = new Rut("34567890-2");
        Rut rutCose1 = new Rut("45678901-3");
        Rut rutCose2 = new Rut("56789012-4");
        Rut rutCose3 = new Rut("67890123-5");
        Rut rutCose4 = new Rut("78901234-6");

        createPropietario(rutProp1, "Juan Pérez", "juan@email.com", "Av. Siempre Viva 123", "Camino Rural 456");
        createPropietario(rutProp2, "Ana Torres", "ana@email.com", "Calle Larga 101", "Camino Viejo 33");

        createSupervisor(rutSup1, "Laura Soto", "laura@email.com", "Calle Norte 321", "Agrónoma");
        createSupervisor(rutSup2, "Pedro Morales", "pedro@email.com", "Av. Sur 555", "Ingeniero Agrícola");

        createCosechador(rutCose1, "Carlos Díaz", "carlos@email.com", "Villa Sur 111", LocalDate.of(1990, 5, 12));
        createCosechador(rutCose2, "María Rojas", "maria@email.com", "Villa Sur 222", LocalDate.of(1995, 8, 23));
        createCosechador(rutCose3, "Luis Ramírez", "luis@email.com", "Calle Central 999", LocalDate.of(1988, 2, 10));
        createCosechador(rutCose4, "Paula Herrera", "paula@email.com", "Los Robles 45", LocalDate.of(2000, 11, 2));

        // ==== CREAR CULTIVOS ====
        createCultivo(1, "Manzana", "Fuji", 500.0f);
        createCultivo(2, "Pera", "Abate", 400.0f);
        createCultivo(3, "Cereza", "Lapins", 350.0f);
        createCultivo(4, "Durazno", "O'Henry", 300.0f);

        // ==== CREAR HUERTOS ====
        createHuerto("El Manzanar", 10.5f, "Sector A", rutProp1);
        createHuerto("La Frutera", 8.2f, "Sector B", rutProp2);

        // ==== AGREGAR CUARTELES A LOS HUERTOS ====
        addCuartelToHuerto("El Manzanar", 101, 3.0f, 1);
        addCuartelToHuerto("El Manzanar", 102, 2.5f, 2);
        addCuartelToHuerto("La Frutera", 201, 3.5f, 3);
        addCuartelToHuerto("La Frutera", 202, 2.0f, 4);

        // ==== CREAR PLANES DE COSECHA ====
        Huerto manzanar = buscaHuerto("El Manzanar");
        Huerto frutera = buscaHuerto("La Frutera");
        Cuartel cuartelM1 = manzanar.getCuartel(101);
        Cuartel cuartelM2 = manzanar.getCuartel(102);
        Cuartel cuartelF1 = frutera.getCuartel(201);
        Cuartel cuartelF2 = frutera.getCuartel(202);

        PlanCosecha plan1 = new PlanCosecha(
                1, "Cosecha Primavera",
                LocalDate.of(2025, 10, 1), LocalDate.of(2025, 10, 30),
                1000.0, 150.0, cuartelM1
        );
        PlanCosecha plan2 = new PlanCosecha(
                2, "Cosecha Verano",
                LocalDate.of(2026, 1, 5), LocalDate.of(2026, 1, 25),
                800.0, 180.0, cuartelM2
        );
        PlanCosecha plan3 = new PlanCosecha(
                3, "Cosecha Cerezos",
                LocalDate.of(2025, 11, 10), LocalDate.of(2025, 11, 30),
                900.0, 200.0, cuartelF1
        );
        PlanCosecha plan4 = new PlanCosecha(
                4, "Cosecha Duraznos",
                LocalDate.of(2026, 2, 1), LocalDate.of(2026, 2, 20),
                600.0, 220.0, cuartelF2
        );

        planes.add(plan1);
        planes.add(plan2);
        planes.add(plan3);
        planes.add(plan4);

        // ==== AGREGAR CUADRILLAS ====
        Supervisor sup1 = (Supervisor) buscaPersona(rutSup1);
        Supervisor sup2 = (Supervisor) buscaPersona(rutSup2);

        plan1.addCuadrilla(1, "Cuadrilla A", sup1);
        plan2.addCuadrilla(2, "Cuadrilla B", sup2);
        plan3.addCuadrilla(3, "Cuadrilla C", sup1);
        plan4.addCuadrilla(4, "Cuadrilla D", sup2);

        // ==== ASIGNAR COSECHADORES A CUADRILLAS ====
        addCosechadorToCuadrilla(1, 1, LocalDate.of(2025, 10, 5), LocalDate.of(2025, 10, 25), 150.0, rutCose1);
        addCosechadorToCuadrilla(1, 1, LocalDate.of(2025, 10, 10), LocalDate.of(2025, 10, 28), 130.0, rutCose2);

        addCosechadorToCuadrilla(2, 2, LocalDate.of(2026, 1, 6), LocalDate.of(2026, 1, 20), 120.0, rutCose3);
        addCosechadorToCuadrilla(2, 2, LocalDate.of(2026, 1, 10), LocalDate.of(2026, 1, 23), 140.0, rutCose4);

        addCosechadorToCuadrilla(3, 3, LocalDate.of(2025, 11, 12), LocalDate.of(2025, 11, 25), 160.0, rutCose2);
        addCosechadorToCuadrilla(3, 3, LocalDate.of(2025, 11, 15), LocalDate.of(2025, 11, 28), 110.0, rutCose3);

        addCosechadorToCuadrilla(4, 4, LocalDate.of(2026, 2, 3), LocalDate.of(2026, 2, 18), 100.0, rutCose1);
        addCosechadorToCuadrilla(4, 4, LocalDate.of(2026, 2, 5), LocalDate.of(2026, 2, 17), 130.0, rutCose4);
    }
}
