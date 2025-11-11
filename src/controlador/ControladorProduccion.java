package controlador;

import modelo.*;
import utilidades.*;

import java.io.*;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;
import java.util.*;
//comentario
public class ControladorProduccion {
    private static ControladorProduccion instance;
    DateTimeFormatter FORMATO_FH = DateTimeFormatter.ofPattern("dd/MM/yyyy HH:mm");
    DateTimeFormatter FORMATO_F = DateTimeFormatter.ofPattern("dd/MM/yyyy");

    private final ArrayList<Persona> personas = new ArrayList<>();
    private final ArrayList<Huerto> huertos = new ArrayList<>();
    private final ArrayList<Cultivo> cultivos = new ArrayList<>();
    private final ArrayList<PlanCosecha> planes = new ArrayList<>();
    private final ArrayList<Pesaje> pesajes = new ArrayList<>();
    private final ArrayList<PagoPesaje> pagos = new ArrayList<>();

    private ControladorProduccion(){}

    public static ControladorProduccion getInstance(){
        if (instance == null) {
            instance = new ControladorProduccion();
        }
        return instance;
    }

    public void createPropietario(Rut rut, String nombre, String email, String dirParticular, String dirComercial) throws GestionHuertosException {
        if (findPropietarioByRut(rut).isPresent()) throw new GestionHuertosException("Ya existe un propietario con el rut indicado");
        personas.add(new Propietario(rut, nombre, email, dirParticular, dirComercial));
    }

    public void createSupervisor(Rut rut, String nombre, String email, String direccion, String profesion) throws GestionHuertosException {
        if (findSupervisorByRut(rut).isPresent()) throw new GestionHuertosException("Ya existe un supervisor con el rut indicado");
        personas.add(new Supervisor(rut, nombre, email, direccion, profesion));
    }

    public void createCosechador(Rut rut, String nombre, String email, String direccion, LocalDate fechaNacimiento) throws GestionHuertosException {
        if (findCosechadorByRut(rut).isPresent()) throw new GestionHuertosException("Ya existe un cosechador con el rut indicado");
        personas.add(new Cosechador(rut, nombre, email, direccion, fechaNacimiento));
    }

    public void createCultivo(int id, String especie, String variedad, float rendimiento) throws GestionHuertosException {
        if (findCultivoById(id).isPresent()) throw new GestionHuertosException("Ya existe un cultivo con el id indicado");
        cultivos.add(new Cultivo(id, especie, variedad, rendimiento));
    }

    public void createHuerto(String nombre, float superficie, String ubicacion, Rut rutPropietario) throws GestionHuertosException {
        if (findHuertoByNombre(nombre).isPresent()) throw new GestionHuertosException("Ya existe un huerto con el nombre indicado");
        Optional<Propietario> p = findPropietarioByRut(rutPropietario);
        if (p.isEmpty()) throw new GestionHuertosException("No existe un propietario con el rut indicado");
        huertos.add(new Huerto(nombre, superficie, ubicacion, p.get()));
    }

    public void addCuartelToHuerto(String nombreHuerto, int idCuartel, float superficie, int idCultivo) throws GestionHuertosException {
        Optional<Huerto> h = findHuertoByNombre(nombreHuerto);
        if (h.isEmpty()) throw new GestionHuertosException("No existe un huerto con el nombre indicado");
        Optional<Cultivo> c = findCultivoById(idCultivo);
        if (c.isEmpty()) throw new GestionHuertosException("No existe un cultivo con el id indicado");
        boolean ok = h.get().addCuartel(idCuartel, superficie, c.get());
        if (!ok) throw new GestionHuertosException("No fue posible agregar el cuartel (duplicado o superficie excede)");
    }

    public void changeEstadoCuartel(String nombreHuerto, int idCuartel, EstadoFenologico estado) throws GestionHuertosException {
        Optional<Huerto> h = findHuertoByNombre(nombreHuerto);
        if (h.isEmpty()) throw new GestionHuertosException("No existe un huerto con el nombre indicado");
        Cuartel c = h.get().getCuartel(idCuartel);
        if (c == null) throw new GestionHuertosException("No existe el cuartel con el id indicado");
        c.setEstado(estado);
    }

    public void createPlanCosecha(int idPlan, String nom, LocalDate inicio, LocalDate finEstim, double meta, double precioBase, String nomHuerto, int idCuartel) throws GestionHuertosException {
        if (findPlanById(idPlan).isPresent()) throw new GestionHuertosException("Ya existe un plan con el id indicado");
        Optional<Huerto> h = findHuertoByNombre(nomHuerto);
        if (h.isEmpty()) throw new GestionHuertosException("No existe un huerto con el nombre indicado");
        Cuartel c = h.get().getCuartel(idCuartel);
        if (c == null) throw new GestionHuertosException("No existe en el huerto un cuartel con el id indicado");
        if (!finEstim.isAfter(inicio)) throw new GestionHuertosException("La fecha de término debe ser posterior a la de inicio");
        PlanCosecha plan = new PlanCosecha(idPlan, nom, inicio, finEstim, meta, precioBase, c);
        planes.add(plan);
    }

    public void changeEstadoPlan(int idPlan, EstadoPlan estado) throws GestionHuertosException {
        Optional<PlanCosecha> p = findPlanById(idPlan);
        if (p.isEmpty()) throw new GestionHuertosException("No existe un plan con el id indicado");
        p.get().setEstado(estado);
    }

    public void addCuadrillaToPlan(int idPlan, int idCuad, String nomCuad, Rut rutSupervisor) throws GestionHuertosException {
        Optional<PlanCosecha> p = findPlanById(idPlan);
        if (p.isEmpty()) throw new GestionHuertosException("No existe un plan con el id indicado");
        Optional<Supervisor> s = findSupervisorByRut(rutSupervisor);
        if (s.isEmpty()) throw new GestionHuertosException("No existe un supervisor con el rut indicado");
        if (s.get().getCuadrilla() != null) throw new GestionHuertosException("El supervisor ya tiene asignada una cuadrilla a su cargo");
        p.get().addCuadrilla(idCuad, nomCuad, s.get()); //una vez vistas todas las posibles excepciones, se realiza la asignación
    }

    public void addCosechadorToCuadrilla(int idPlan, int idCuadrilla, LocalDate fInicio, LocalDate fFin, double meta, Rut rutCosechador) throws GestionHuertosException {
        Optional<PlanCosecha> p = findPlanById(idPlan);
        if (p.isEmpty()) throw new GestionHuertosException("No existe un plan con el id indicado");
        Optional<Cosechador> c = findCosechadorByRut(rutCosechador);
        if (c.isEmpty()) throw new GestionHuertosException("No existe un cosechador con el rut indicado");
        if (!fFin.isAfter(fInicio)) throw new GestionHuertosException("La fecha de inicio debe ser anterior a la fecha de término");
        PlanCosecha plan = p.get();
        LocalDate finPlan = (plan.getFinReal() != null) ? plan.getFinReal() : plan.getFinEstimado();
        if (fInicio.isBefore(plan.getInicio()) || fFin.isAfter(finPlan)) throw new GestionHuertosException("El rango de fechas de asignación del cosechador a la cuadrilla está fuera del rango de fechas del plan");
        plan.addCosechadorToCuadrilla(idCuadrilla, fInicio, fFin, meta, c.get());
    }

    public void addPesaje(int id, Rut rutCosechador, int idPlan, int idCuadrilla, float cantidadKg, Calidad calidad) throws GestionHuertosException {
        if (findPesajeById(id).isPresent()) throw new GestionHuertosException("Ya existe un pesaje con id indicado");
        Optional<Cosechador> cosechadorOpt = findCosechadorByRut(rutCosechador);
        if (cosechadorOpt.isEmpty()) throw new GestionHuertosException("No existe un cosechador con el rut indicado");
        Optional<PlanCosecha> planOpt = findPlanById(idPlan);
        if (planOpt.isEmpty()) throw new GestionHuertosException("No existe un plan con el id indicado");
        PlanCosecha plan = planOpt.get();
        if (plan.getEstado() != EstadoPlan.EJECUTANDO) throw new GestionHuertosException("El plan no se encuentra en estado EJECUTANDO");
        Cuadrilla cuad = null;
        for (Cuadrilla q : plan.getCuadrillas()) {
            if (q.getId() == idCuadrilla) { cuad = q; break; }
        }
        if (cuad == null) throw new GestionHuertosException("No existe una cuadrilla con el id indicado en el plan");
        boolean asignado = false;
        CosechadorAsignado asign = null;
        for (CosechadorAsignado ca : cuad.getAsignaciones()) {
            if (ca.getCosechador().getRut().toString().equals(rutCosechador.toString())) {
                asignado = true;
                asign = ca;
                break;
            }
        }
        if (!asignado) throw new GestionHuertosException("El cosechador no tiene una asignación a la cuadrilla indicada en el plan");
        LocalDateTime ahora = LocalDateTime.now();
        if (ahora.toLocalDate().isBefore(asign.getDesde()) || ahora.toLocalDate().isAfter(asign.getHasta())) throw new GestionHuertosException("La fecha no está en el rango de la asignación del cosechador a la cuadrilla");
        if (cuad.getPlanCosecha().getCuartel().getEstado() != EstadoFenologico.COSECHA) throw new GestionHuertosException("El cuartel no se encuentra en estado fenológico COSECHA");
        Pesaje pe = new Pesaje(id, cantidadKg, calidad, ahora, asign);
        pesajes.add(pe);
    }

    public double addPagoPesaje(int id, Rut rutCosechador) throws GestionHuertosException {
        if (findPagoPesajeById(id).isPresent())
            throw new GestionHuertosException("Ya existe un pago con ese id");

        Optional<Cosechador> cOp = findCosechadorByRut(rutCosechador);
        if (cOp.isEmpty())
            throw new GestionHuertosException("No existe un cosechador con el rut indicado");

        List<Pesaje> impagos = new ArrayList<>();
        for (Pesaje p : pesajes) {
            if (p.getCosechadorAsignado() != null &&
                    p.getCosechadorAsignado().getCosechador().getRut().toString().equals(rutCosechador.toString()) &&
                    !p.isPagado()) {
                impagos.add(p);
            }
        }

        LocalDate hoy = LocalDate.now();
        PagoPesaje nuevoPago = new PagoPesaje(id, hoy, impagos);

        if (nuevoPago.getFecha().isAfter(hoy))
            throw new GestionHuertosException("La fecha del pago no puede ser posterior a la actual");

        pagos.add(nuevoPago);

        for (Pesaje p : impagos) {
            p.setPago(nuevoPago);
        }

        return nuevoPago.getMonto();
    }

    public String[] listCultivos() {
        if (cultivos.isEmpty()) return new String[0];
        String[] out = new String[cultivos.size()];
        for (int i = 0; i < cultivos.size(); i++) {
            Cultivo c = cultivos.get(i);//pa que tenga decimal el rendimiento :v
            out[i] = String.format(Locale.GERMANY, "%d; %s; %s; %.1f; %d",
                    c.getId(),
                    c.getEspecie(),
                    c.getVariedad(),
                    c.getRendimiento(),
                    c.getCuarteles().length);
        }
        return out;
    }

    public String[] listHuertos() {
        if (huertos.isEmpty()) return new String[0];
        String[] out = new String[huertos.size()];

        for (int i = 0; i < huertos.size(); i++) {
            Huerto h = huertos.get(i);
            out[i] = String.format(Locale.GERMANY, "%s; %,.1f; %s; %s; %s; %d",
                    h.getNombre(),
                    h.getSuperficie(),
                    h.getUbicacion(),
                    h.getPropietario().getRut(),
                    h.getPropietario().getNombre(),
                    h.getCuarteles().length);
        }
        return out;
    }

    public String[] listPropietarios() {
        List<String> lista = new ArrayList<>();
        for (Persona p : personas) if (p instanceof Propietario pr) lista.add(String.format("%s; %s; %s; %s; %s; %d",
                pr.getRut(),
                pr.getNombre(),
                pr.getDireccion(),
                pr.getEmail(),
                pr.getDireccionComercial(),
                pr.getHuertos().length));
        return lista.toArray(new String[0]);
    }

    public String[] listSupervisores() {
        List<String> lista = new ArrayList<>();

        for (Persona p : personas) {
            if (p instanceof Supervisor s) {
                String nomCuad = "N/A";
                int psjImpago = 0;
                double kg = 0;

                if(s.getCuadrilla() != null){
                    Cuadrilla c = s.getCuadrilla();
                    nomCuad = c.getNombre();
                    kg = c.getKilosPesados();
                    for(CosechadorAsignado cosAs : c.getAsignaciones()){
                        psjImpago += cosAs.getNroPesajesImpagos();
                    }
                }

                lista.add(String.format(Locale.GERMANY, "%s; %s; %s; %s; %s; %s; %.1f; %d",
                s.getRut(),
                s.getNombre(),
                s.getDireccion(),
                s.getEmail(),
                s.getProfesion(),
                nomCuad,
                kg,
                psjImpago));
            }
        }
        return lista.toArray(new String[0]);
    }

    public String[] listCosechadores() {
        List<String> lista = new ArrayList<>();
        for (Persona p : personas) {
            if (p instanceof Cosechador c) {
                double montoImpago = 0;
                double montoPagado = 0;
                CosechadorAsignado[] asignaciones = c.getAsignaciones();
                for (CosechadorAsignado asignado : asignaciones) {
                    montoImpago += asignado.getMontoPesajesImpagos();
                    montoPagado += asignado.getMontoPesajesPagados();
                }
                //Con el Locale.GERMANY los números tienen "." para miles y "," para decimales :v
                lista.add(String.format(Locale.GERMANY, "%s; %s; %s; %s; %s; %d; %,.1f; %,.1f",
                        c.getRut(),
                        c.getNombre(),
                        c.getDireccion(),
                        c.getEmail(),
                        c.getFechaNacimiento().format(FORMATO_F),
                        c.getCuadrillas().length,
                        montoImpago,
                        montoPagado));
            }
        }
        return lista.toArray(new String[0]);
    }

    public String[] listPlanesCosecha() {
        if (planes.isEmpty()) return new String[0];
        String[] out = new String[planes.size()];
        for (int i = 0; i < planes.size(); i++) {
            PlanCosecha p = planes.get(i);
            LocalDate finPlan = (p.getFinReal() != null) ? p.getFinReal() : p.getFinEstimado();
            Cuartel c = p.getCuartel();
            Huerto h = c.getHuerto();
            out[i] = String.format(Locale.GERMANY, "%d; %s; %s; %s; %,.1f; %,.1f; %s; %d; %s; %d; %.1f",
                    p.getId(),
                    p.getNombre(),
                    p.getInicio().format(FORMATO_F),
                    finPlan.format(FORMATO_F),
                    p.getMetaKilos(),
                    p.getPrecioBaseKilo(),
                    p.getEstado(),
                    c.getId(),
                    h.getNombre(),
                    p.getCuadrillas().length,
                    p.getCumplimientoMeta());
        }
        return out;
    }

    public String[] listPesajes() {
        if (pesajes.isEmpty()) return new String[0];

        String[] out = new String[pesajes.size()];

        for(int i = 0; i  < pesajes.size(); i++){
            Pesaje p = pesajes.get(i);
            String rut = p.getCosechadorAsignado().getCosechador().getRut().toString();
            String pago = p.isPagado() ? p.getPagoPesaje().getFecha().format(FORMATO_F) : "Impago";

            out[i] = String.format(Locale.GERMANY,"%d; %s; %s; %s; %,.1f; %,.1f; %,.1f; %s",
                    p.getId(),
                    p.getFechaHora().format(FORMATO_FH),
                    rut,
                    p.getCalidad(),
                    p.getCantidadKg(),
                    p.getPrecioKg(),
                    p.getMonto(),
                    pago);
        }
        return out;
    }

    public String[] listPesajesCosechador(Rut rut) throws GestionHuertosException {
        Optional<Cosechador> cosechadorOpt = findCosechadorByRut(rut);
        if (cosechadorOpt.isEmpty()) throw new GestionHuertosException("No existe un cosechador con el rut indicado");

        Cosechador cos = cosechadorOpt.get();
        CosechadorAsignado[] asignaciones = cos.getAsignaciones();
        if(asignaciones.length == 0) throw new GestionHuertosException("El cosechador no está asignado a ninguna cuadrilla");

        List<String> outList = new ArrayList<>();

        for(CosechadorAsignado cosAs: asignaciones){
            for(Pesaje p: cosAs.getPesajes()){
                String pago = p.isPagado() ? p.getPagoPesaje().getFecha().format(FORMATO_F) : "Impago";
                outList.add(String.format(Locale.GERMANY, "%d; %s; %s; %,.1f; %,.1f; %,.1f; %s",
                        p.getId(),
                        p.getFechaHora().format(FORMATO_FH),
                        p.getCalidad(),
                        p.getCantidadKg(),
                        p.getPrecioKg(),
                        p.getMonto(),
                        pago
                ));
            }
        }
        return outList.toArray(new String[0]);
    }

    public String[] listPagosPesajes() {
        if (pagos.isEmpty()) return new String[0];
        String[] out = new String[pagos.size()];
        for (int i = 0; i < pagos.size(); i++) {
            PagoPesaje pago = pagos.get(i);
            Pesaje[] pesajes = pago.getPesajes();
            Rut rut = pesajes[0].getCosechadorAsignado().getCosechador().getRut();
            out[i] = String.format(Locale.GERMANY, "%d; %s; %,.2f; %d; %s",
                    pago.getId(),
                    pago.getFecha().format(FORMATO_F),
                    pago.getMonto(),
                    pesajes.length,
                    rut
                    );
        }
        return out;
    }

    public void readDataFromTextFile() throws GestionHuertosException {
        try(Scanner sc = new Scanner(new File("src/DatosIniciales.txt"))){
            while(sc.hasNextLine()){
                String linea = sc.nextLine();
                if(linea.startsWith("#") || linea.isBlank()) continue;

                String[] tokens = linea.split(";");
                int num = Integer.parseInt(tokens[1]);

                for(int i = 0;  i < num; i++){
                    String datos = sc.nextLine();
                    if(datos.isEmpty()) continue;
                    String[] dato = datos.split(";");

                    for(int j = 0; j < dato.length; j++){
                        dato[j] = dato[j].trim();
                    }

                    switch (tokens[0]) {
                        case "createPropietario" ->{
                            Rut rut = Rut.of(dato[0]);
                            createPropietario(rut, dato[1], dato[2], dato[3], dato[4]);
                        }
                        case "createSupervisor" ->{
                            Rut rut = Rut.of(dato[0]);
                            createSupervisor(rut, dato[1], dato[2], dato[3], dato[4]);
                        }
                        case "createCosechador" ->{
                            Rut rut = Rut.of(dato[0]);
                            try{
                                LocalDate fNac = LocalDate.parse(dato[4], FORMATO_F);
                                createCosechador(rut, dato[1], dato[2], dato[3], fNac);
                            }catch(DateTimeParseException e){
                                lanzaExcepcion(tokens[0], "Dato de fecha no válido");
                            }
                        }
                        case "createCultivo" ->{
                            try{
                                int id = Integer.parseInt(dato[0]);
                                float rendimiento = Float.parseFloat(dato[3]);
                                createCultivo(id, dato[1], dato[2], rendimiento);
                            }catch(NumberFormatException e){
                                lanzaExcepcion(tokens[0], "Excepción en datos numéricos");
                            }
                        }
                        case "createHuerto" ->{
                            Rut rut = Rut.of(dato[3]);
                            try{
                                float superficie = Float.parseFloat(dato[1]);
                                createHuerto(dato[0], superficie, dato[2], rut);
                            }catch(NumberFormatException e){
                                lanzaExcepcion(tokens[0], "Dato numérico de superficie no es válido");
                            }
                        }
                        case "addCuartelToHuerto" ->{
                            try{
                                int id = Integer.parseInt(dato[1]);
                                float sup = Float.parseFloat(dato[2]);
                                int idCul = Integer.parseInt(dato[3]);
                                addCuartelToHuerto(dato[0], id, sup, idCul);
                            }catch(NumberFormatException e){
                                lanzaExcepcion(tokens[0], "Excepción en datos numéricos");
                            }

                        }
                        case "createPlanCosecha" ->{
                            try{
                                int idPlan = Integer.parseInt(dato[0]);
                                LocalDate fIni = LocalDate.parse(dato[2], FORMATO_F);
                                LocalDate fFin = LocalDate.parse(dato[3], FORMATO_F);
                                double meta = Double.parseDouble(dato[4]);
                                double precio = Double.parseDouble(dato[5]);
                                int idCuartel = Integer.parseInt(dato[7]);
                                createPlanCosecha(idPlan, dato[1], fIni, fFin, meta, precio, dato[6], idCuartel);
                            }catch(NumberFormatException e){
                                lanzaExcepcion(tokens[0], "Excepción en datos numéricos");
                            }
                        }
                        case "addCuadrillaToPlan" ->{
                            try{
                                int idPlan = Integer.parseInt(dato[0]);
                                int idCuadrilla = Integer.parseInt(dato[1]);
                                Rut rut = Rut.of(dato[3]);
                                addCuadrillaToPlan(idPlan, idCuadrilla, dato[2], rut);
                            }catch(NumberFormatException e){
                                lanzaExcepcion(tokens[0], "Excepción en datos numéricos");
                            }
                        }
                        case "addCosechadorToCuadrilla" ->{
                            try{
                                int idPlan = Integer.parseInt(dato[0]);
                                int idCuadrilla = Integer.parseInt(dato[1]);
                                LocalDate fIni = LocalDate.parse(dato[2], FORMATO_F);
                                LocalDate fFin = LocalDate.parse(dato[3], FORMATO_F);
                                double meta = Double.parseDouble(dato[4]);
                                Rut rut = Rut.of(dato[5]);
                                addCosechadorToCuadrilla(idPlan, idCuadrilla, fIni, fFin, meta, rut);
                            }catch(NumberFormatException e){
                                lanzaExcepcion(tokens[0], "Excepción en datos numéricos");
                            }catch(DateTimeParseException e){
                                lanzaExcepcion(tokens[0], "Excepción en datos de fechas");
                            }
                        }
                        case "changeEstadoPlan" ->{
                            try{
                                int idPlan = Integer.parseInt(dato[0]);
                                EstadoPlan estPlan = EstadoPlan.valueOf(dato[1].toUpperCase());
                                changeEstadoPlan(idPlan, estPlan);
                            }catch(NumberFormatException e){
                                lanzaExcepcion(tokens[0], "Dato numérico en idPlan no válido");
                            }catch(IllegalArgumentException e){
                                lanzaExcepcion(tokens[0], "Dato de estado del plan no es válido");
                            }
                        }
                        case "changeEstadoCuartel" ->{
                            try{
                                int idCuartel = Integer.parseInt(dato[0]);
                                EstadoFenologico estCuartel = EstadoFenologico.valueOf(dato[2].toUpperCase());
                                changeEstadoCuartel(dato[1], idCuartel, estCuartel);
                            }catch(NumberFormatException e){
                                lanzaExcepcion(tokens[0], "Dato numérico en idCuartel no válido");
                            }catch(IllegalArgumentException e){
                                lanzaExcepcion(tokens[0], "Dato de estado fenológico no válido");
                            }
                        }
                        case "addPesaje" ->{
                            try{
                                int id = Integer.parseInt(dato[0]);
                                Rut rut = Rut.of(dato[1]);
                                int idPlan = Integer.parseInt(dato[2]);
                                int idCuadrilla = Integer.parseInt(dato[3]);
                                float cantKg = Float.parseFloat(dato[4]);
                                Calidad calidad = Calidad.valueOf(dato[5].toUpperCase());
                                addPesaje(id, rut, idPlan, idCuadrilla, cantKg, calidad);
                            }catch(NumberFormatException e){
                                lanzaExcepcion(tokens[0], "Excepción en datos numéricos");
                            }catch(IllegalArgumentException e){
                                lanzaExcepcion(tokens[0], "Dato de calidad no válido");
                            }
                        }
                    }
                }
            }
        }catch(FileNotFoundException e){
            throw new GestionHuertosException("Archivo de datos inexistente");
        }
    }
//rutina auxiliar sólo para el lector de datos desde archivo de texto por ahora.
    private void lanzaExcepcion(String operacion, String msj) throws GestionHuertosException{
        throw new GestionHuertosException("Error en " + operacion + ": " + msj);
    }

    private Optional<Propietario> findPropietarioByRut(Rut rut) {
        for (Persona p : personas) {
            if (p instanceof Propietario pr && pr.getRut().toString().equals(rut.toString())) {
                return Optional.of(pr);
            }
        }
        return Optional.empty();
    }

    private Optional<Supervisor> findSupervisorByRut(Rut rut) {
        for (Persona p : personas) if (p instanceof Supervisor s && s.getRut().toString().equals(rut.toString())) return Optional.of(s);
        return Optional.empty();
    }

    private Optional<Cosechador> findCosechadorByRut(Rut rut) {
        for (Persona p : personas) if (p instanceof Cosechador c && c.getRut().toString().equals(rut.toString())) return Optional.of(c);
        return Optional.empty();
    }

    private Optional<Cultivo> findCultivoById(int id) {
        for (Cultivo c : cultivos) if (c.getId() == id) return Optional.of(c);
        return Optional.empty();
    }

    private Optional<Huerto> findHuertoByNombre(String nombre) {
        for (Huerto h : huertos) if (h.getNombre().equalsIgnoreCase(nombre)) return Optional.of(h);
        return Optional.empty();
    }

    private Optional<PlanCosecha> findPlanById(int id) {
        for (PlanCosecha p : planes) if (p.getId() == id) return Optional.of(p);
        return Optional.empty();
    }

    private Optional<Pesaje> findPesajeById(int id) {
        for (Pesaje p : pesajes) if (p.getId() == id) return Optional.of(p);
        return Optional.empty();
    }

    private Optional<PagoPesaje> findPagoPesajeById(int id) {
        for (PagoPesaje p : pagos) if (p.getId() == id) return Optional.of(p);
        return Optional.empty();
    }
}





