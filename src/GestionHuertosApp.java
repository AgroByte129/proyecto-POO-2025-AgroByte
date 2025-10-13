import java.util.Scanner;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;

public class GestionHuertosApp {
    private Scanner sc = new Scanner(System.in).useDelimiter("[\\t\\n]+");
    private ControlProduccion cP = new ControlProduccion();
    private static final DateTimeFormatter FORMATO = DateTimeFormatter.ofPattern("dd/MM/yyyy");

    public static void main(String[] args){
        GestionHuertosApp app = new GestionHuertosApp();
        app.menu();
    }
    private void menu(){
        byte opcion;

        do {
            System.out.println("\n*** Sistema de Gestión de Huertos ***\n");
            System.out.println("MENÚ DE OPCIONES");
            System.out.println("1. Crear Persona");
            System.out.println("2. Crear Cultivo");
            System.out.println("3. Crear Huerto");
            System.out.println("4. Crear Plan de Cosecha");
            System.out.println("5. Asignar Cosechadores a Plan");
            System.out.println("6. Listar Cultivos");
            System.out.println("7. Listar Huertos");
            System.out.println("8. Listar Personas");
            System.out.println("9. Listar Planes de Cosecha");
            System.out.println("10. Salir");
            System.out.print("   Opción: ");

            opcion = sc.nextByte();

            switch (opcion) {
                case 1 -> {
                    System.out.println("-> Creando una persona...");
                    creaPersona();
                }
                case 2 -> {
                    System.out.println("-> Creando un cultivo...");
                    creaCultivo();
                }
                case 3 -> {
                    System.out.println("-> Crear Huerto");
                    creaHuerto();
                }
                case 4 -> {
                    System.out.println("-> Crear Plan de Cosecha");
                    creaPlanCosecha();
                }
                case 5 -> {
                    System.out.println("-> Asignando cosechadores a un plan de cosecha...");
                    asignaCosechadoresAPlan();
                }
                case 6 -> {
                    System.out.println("-> Listar Cultivos");
                }
                case 7 -> {
                    System.out.println("-> Listar Huertos");
                    listaHuertos();
                }
                case 8 -> {
                    System.out.println("-> Listar Personas");
                    listaPersonas();
                }
                case 9 -> {
                    System.out.println("-> Listar Planes de Cosecha");
                    listaPlanesCosecha();
                }
                case 10 -> {
                    System.out.println("-> Saliendo del sistema...");
                }
                default -> System.out.println("Opción no válida. Intente nuevamente.");
            }

        } while (opcion != 10);
    }

    // **** Menu 1 ****
    private void creaPersona(){
        System.out.print("Rol persona (1 = propietario, 2 = supervisor, 3 = cosechador): ");
        byte rol = sc.nextByte();
        System.out.print("Rut: ");
        String r = sc.next();
        Rut rut = new Rut(r);
        System.out.print("Nombre: ");
        String nom = sc.next();
        System.out.print("Email: ");
        String email = sc.next();
        System.out.print("Dirección: ");
        String dir = sc.next();
        System.out.print("Fecha de nacimiento (dd/mm/aaaa): ");
        LocalDate fechaNac = fechaFormateada(sc.next());

        switch(rol){
            case 1 -> {
                System.out.print("Dirección comercial: ");
                String dirComercial = sc.next();
                if(cP.createPropietario(rut, nom, email, dir, dirComercial)){
                    System.out.println("\nPropietario creado exitosamente");
                }else System.out.println("No se ha podido crear el propietario");
            }
            case 2 -> {
                System.out.print("Profesión: ");
                String profesion = sc.next();
                if(cP.createSupervisor(rut, nom, email, dir, profesion)){
                    System.out.println("\nSupervisor creado exitosamente");
                }else System.out.println("No se ha podido crear el Supervisor");
            }
            case 3 -> {
                if(cP.createCosechador(rut, nom, email, dir, fechaNac)){
                    System.out.println("\nCosechaddor creado exitosamente");
                }else System.out.println("No se ha podido crear el Cosechador");
            }
        }
    }

    // **** Menu 2 ****
    private void creaCultivo(){
        System.out.print("Identificación: ");
        int id = sc.nextInt();
        System.out.print("Especie: ");
        String especie = sc.next();
        System.out.print("Variedad: ");
        String variedad = sc.next();
        System.out.print("Rendimiento: ");
        float rendimiento = sc.nextFloat();

        if(cP.createCultivo(id, especie, variedad, rendimiento)){
            System.out.println("\nCultivo creado exitosamente...");
        } else System.out.println("\nNo se pudo crear el cultivo...");
    }

    // **** Menu 3 ****
    private void creaHuerto(){
        System.out.print("Nombre: ");
        String nom = sc.next();
        System.out.print("Superficie: ");
        float sup = sc.nextFloat();
        System.out.print("Ubicación: ");
        String ubi = sc.next();
        System.out.print("Rut Propietario: ");
        Rut rut = new Rut(sc.next());

        if(cP.createHuerto(nom, sup, ubi, rut)){
            System.out.println("Huerto creado exitosamente...\n");
            System.out.println("Agregando cuarteles al huerto...");
            System.out.print("Nro de cuarteles: ");
            int nroCuarteles = sc.nextInt();

            for(int i = 0; i < nroCuarteles; i++){
                System.out.print("Id cuartel: ");
                int idCuartel = sc.nextInt();
                System.out.print("Superficie cuartel: ");
                float superficie = sc.nextFloat();
                System.out.print("Id cultivo del cuartel: ");
                int idCultivo = sc.nextInt();
                if(cP.addCuartelToHuerto(nom, idCuartel, superficie, idCultivo)){
                    System.out.println("Cuartel agregado exitosamente al huerto\n");
                } else System.out.println("No se pudo agregar el cuartel...\n");
            }
        } else System.out.println("No se ha podido crear el huerto...");
    }
    // **** Menu 4 ****
    private void creaPlanCosecha(){
        System.out.print("Id plan: ");
        int idPlan = sc.nextInt();
        System.out.print("Nombre plan: ");
        String nomPlan = sc.next();
        System.out.print("Fecha inicio (dd/mm/aaaa): ");
        LocalDate fIni = fechaFormateada(sc.next());
        System.out.print("Fecha fin estimada (dd/mm/aaaa): ");
        LocalDate fFin = fechaFormateada(sc.next());
        System.out.print("Meta kilos: ");
        double meta = sc.nextDouble();
        System.out.print("Precio base por kilo: ");
        double precio = sc.nextDouble();
        System.out.print("Nombre del huerto: ");
        String nomHuerto = sc.next();
        System.out.print("Id del cuartel: ");
        int idCuartel = sc.nextInt();

        if(cP.createPlanCosecha(idPlan, nomPlan, fIni, fFin, meta, precio, nomHuerto, idCuartel)) {
            System.out.println("\nPlan de Cosecha creado exitosamente");
            System.out.println("Agregando cuadrillas al plan de cosecha");
            System.out.print("Nro. de cuadrillas: ");
            int nroCuadrillas = sc.nextInt();

            for(int i = 0; i < nroCuadrillas; i++){
                System.out.print("\nId cuadrilla: ");
                int idCuadrilla = sc.nextInt();
                System.out.print("Nombre cuadrilla: ");
                String nombreCuadrilla = sc.next();
                System.out.print("Rut supervisor: ");
                Rut rutSupervisor = new Rut(sc.next());

                if(cP.addCuadrillaToPlan(idPlan, idCuadrilla, nombreCuadrilla, rutSupervisor)){
                    System.out.println("Cuadrilla agregada exitosamente al plan de cosecha");
                }else System.out.println("No se ha podido agregar la cuadrilla al plan...");
            }

        }else System.out.println("No se pudo crear el Plan de Cosecha");
    }

        // **** Menu 5 ****
    private void asignaCosechadoresAPlan(){
        System.out.print("Id del plan: ");
        int idPlan = sc.nextInt();
        System.out.print("Id cuadrilla");
        int idCuadrilla = sc.nextInt();
        System.out.print("Nro. cosechadores a asignar: ");
        int nroCos = sc.nextInt();

        for(int i = 0; i < nroCos; i++){
            System.out.print("\nFecha de inicio asignacion (dd/mm/aaaa): ");
            LocalDate fIni = fechaFormateada(sc.next());
            System.out.print("Fecha de término asignacion (dd/mm/aaaa): ");
            LocalDate fFin = fechaFormateada(sc.next());
            System.out.print("Meta (Kilos): ");
            double metaKilos = sc.nextDouble();
            System.out.print("Rut cosechador: ");
            Rut rut = new Rut(sc.next());

            if(cP.addCosechadorToCuadrilla(idPlan, idCuadrilla, fIni, fFin, metaKilos, rut)){
                System.out.println("Cosechador asignado exitosamente a una cuadrilla del plan de cosecha");
            } else System.out.println("No se ha podido agregar el cosechador a la cuadrilla...");
        }
    }
    // ****Menu 6 ****cultivos
    // **** Menu 7 **** huertos

    // ***** Menu 8 ****
    private void listaPersonas(){
        String[] listPropietarios = cP.listPropietarios();
        String[] listSupervisores = cP.listSupervisores();
        String[] listCosechadores = cP.listCosechadores();

        System.out.println("LISTADO DE PROPIETARIOS" +
                "\n-----------------------");
        System.out.printf("%-12s %-15s %-20s %-25s %-25s %-15s%n",
                "Rut", "Nombre", "Dirección", "email", "Dirección comercial", "Nro. huertos");
        if(listPropietarios.length == 0){
            System.out.println("No hay propietarios registrados...");
        }else {
            for(String lP : listPropietarios){
                System.out.println(lP);
            }
        }

        System.out.println("\nLISTADO DE SUPERVISORES" +
                         "\n-----------------------");
        System.out.printf("%-12s %-15s %-20s %-25s %-25s %-15s%n",
                "Rut", "Nombre", "Dirección", "email", "Profesión", "Nombre cuadrilla");
        if(listSupervisores.length == 0){
            System.out.println("No hay supervisores registrados...");
        }else{
            for(String lS : listSupervisores){
                System.out.println(lS);
            }
        }

        System.out.println("\nLISTADO DE COSECHADORES" +
                         "\n-----------------------");
        System.out.printf("%-12s %-15s %-20s %-25s %-25s %-15s%n",
                "Rut", "Nombre", "Dirección", "email", "Fecha nacimiento", "Nro. Cuadrillas");
        if(listCosechadores.length == 0){
            System.out.println("No hay cosechadores registrados...");
        }else{
            for(String lC : listCosechadores){
                System.out.println(lC);
            }
        }

    }
    // **** Menu 9 *****
    private void listaPlanesCosecha(){
        String[] listaP = cP.listPlanesCosecha();

        System.out.println("\nLISTADO DE PLANES COSECHA" +
                "\n-------------------------");

        System.out.printf(
                "%-6s %-15s %-15s %-15s %-10s %-17s %-12s %-12s %-20s %-15s%n",
                "Id", "Nombre", "Fecha inicio", "Fecha término", "Meta (kg)",
                "Precio base (kg)", "Estado", "Id cuartel", "Nombre huerto",
                "Nro. cuadrillas"
        );
        if(listaP.length == 0){
            System.out.println("No hay planes registrados...");
        }else {
            for(String l : listaP){
                System.out.println(l);
            }
        }
    }
    private void listaHuertos(){
        String [] listaDeHuertos = cP.listHuertos();
        if(listaDeHuertos.length == 0){
            System.out.println("No existen huertos registrados");
        }else{
            System.out.printf("LISTADO DE HUERTOS\n");
            System.out.printf("------------------\n");
            System.out.printf("%-20s %-12s %-20s %-15s %-20s %-15s\n",
                    "Nombre", "Superficie", "Ubicación", "Rut propietario", "Nombre propietario", "Nro. cuarteles");
            for(int i = 0; i < listaDeHuertos.length; i++){
                System.out.println(listaDeHuertos[i]);
            }
        }
    }

    private LocalDate fechaFormateada(String fecha){
        return LocalDate.parse(fecha, FORMATO);
    }
}
