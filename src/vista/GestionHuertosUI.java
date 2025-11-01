package vista;

import modelo.ControlProduccion;
import org.w3c.dom.ls.LSOutput;
import utilidades.*;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;
import java.util.Scanner;

public class GestionHuertosUI {
    private static GestionHuertosUI instance;
    private Scanner tcld = new Scanner(System.in).useDelimiter("[\\t\\n]+");
    private ControlProduccion cP = ControlProduccion.getInstance();

    private GestionHuertosUI(){}
    public static GestionHuertosUI getInstance(){
        if(instance == null){
            instance = new GestionHuertosUI();
        }
        return instance;
    }

    //*** MENÚ PRINCIPAL ***
    public void menu(){
        int respuesta;
        do{
            System.out.print("""
                    ::: MENU PRINCIPAL :::
                    1. Crear Personas
                    2. Menú Huertos
                    3. Menú Planes de Cosecha
                    4. Menú Listados
                    5. Salir
                        Opción:\s""");
            respuesta = tcld.nextInt();

            switch(respuesta){
                case 1 ->{}
                case 2 ->{}
                case 3 ->{}
                case 4 ->{}
                case 5 ->{
                    System.out.println("Saliendo...");
                }
                default ->{}
            }
        }while(respuesta != 5);
    }
    //*** MENÚ HUERTO ***
    private void menuHuertos(){
        int respuesta;
        do{
            System.out.print("""
                            >>> SUBMENU HUERTOS <<<
                            1. Crear Cultivo
                            2. Crear Huerto
                            3. Agregar Cuarteles a Huerto
                            4. Cambiar Estado Cuartel
                            5. Volver
                                Opción:\s""");
            respuesta = tcld.nextInt();

            switch(respuesta){
                case 1 ->{}
                case 2 ->{}
                case 3 ->{}
                case 4 ->{}
                case 5 ->{
                    System.out.println("Volviendo a Menú Principal...");
                }
                default ->{
                    System.out.println("Respuesta invalida.");
                }
            }
        }while(respuesta != 5);
        menu();
    }
    //*** MENÚ PLANES DE COSECHA ***
    public void menuPlanesCosecha(){
        byte respuesta;

        do{
            System.out.print("""
                    >>> SUBMENU PLANES DE COSECHA <<<
                    1. Crear Plan de Cosecha
                    2. Cambiar Estado de Plan
                    3. Agregar Cuadrillas a Plan
                    4. Agregar Cosechadores a Cuadrilla
                    5. Agregar Pesaje a Cosechador
                    6. Pagar Pesajes Impagos de Cosechador
                    7. Volver
                        Opción:\s""");
            respuesta = tcld.nextByte();

            switch(respuesta){
                case 1 ->{}
                case 2 ->{}
                case 3 ->{}
                case 4 ->{}
                case 5 ->{}
                case 6 ->{}
                case 7 ->{}
                default -> {}
            }
        }while(respuesta != 7);
        menu();
    }
    //*** MENÚ DE LISTAS ***
    public void menuListados(){
        byte respuesta;

        do{
            System.out.print("""
                    <<< SUBMENU LISTADOS <<<
                    1. Listado de Propietarios
                    2. Listado de Supervisores
                    3. Listado de Cosechadores
                    4. Listado de Cultivos
                    5. Listado de Huertos
                    6. Listado de Planes de Cosecha
                    7. Listado Pesajes
                    8. Listado Pesajes de un Cosechador
                    9. Listado de Pagos
                    10. Volver
                        Opción:\s""");
            respuesta = tcld.nextByte();

            switch(respuesta){
                case 1 ->{}
                case 2 ->{}
                case 3 ->{}
                case 4 ->{}
                case 5 ->{}
                case 6 ->{}
                case 7 ->{}
                case 8 ->{}
                case 9 ->{}
                case 10 ->{}
                default ->{}
            }
        }while(respuesta != 10);
        menu();
    }

    private void creaPersona(){
        byte rol = validaEntradaByte("Rol persona (1 = propietario, 2 = supervisor, 3 = cosechador): ","");
        while(rol < 1 || rol > 3){
            System.out.println("Número ingresado no es un rol válido...");
            rol = validaEntradaByte("Rol persona (1 = propietario, 2 = supervisor, 3 = cosechador): ","");
        }
        System.out.print("Rut: ");
        Rut rut = Rut.of(tcld.next());
        String nom = validaEntradaString("Nombre: ");
        String email = validaEntradaString("Email: ");
        String dir = validaEntradaString("Dirección: ");
        LocalDate fechaNac = validaEntradaFecha("Fecha de nacimiento (dd/mm/aaaa): ");

        switch(rol){
            case 1 -> {
                String dirComercial = validaEntradaString("Dirección comercial: ");
                try{
                    cP.createPropietario(rut, nom, email, dir, dirComercial);
                    System.out.println("-> Propietario creado éxitosamente...");
                }catch(GestionHuertosException e){
                    System.out.println(e.getMessage());
                }
            }
            case 2 -> {
                String profesion = validaEntradaString("Profesión: ");
                try{
                    cP.createSupervisor(rut, nom, email, dir, profesion);
                    System.out.println("-> Supervisor creado éxitosamente...");
                }catch (GestionHuertosException e){
                    System.out.println(e.getMessage());
                }
            }
            case 3 -> {
                try{
                    cP.createCosechador(rut, nom, email, dir, fechaNac);
                    System.out.println("-> Cosechador creado éxitosamente...");
                }catch(GestionHuertosException e){
                    System.out.println(e.getMessage());
                }
            }
        }
    }
    private void creaCultivo(){
        int id = validaEntradaInt("Identificación (No negativo): ");
        while(id < 0){
            System.out.println("ID no válido, ingrese valor positivo...");
            id = validaEntradaInt("Identificación (No negativo): ");
        }
        String especie = validaEntradaString("Especie: ");
        String variedad = validaEntradaString("Variedad: ");
        float rendimiento = validaEntradaFloat("Rendmiento: ");
        while(rendimiento <= 0){
            System.out.println("Valor no válido, Ingrese valor positivo");
            rendimiento = validaEntradaFloat("Rendmiento: ");
        }

        try{
            cP.createCultivo(id, especie, variedad, rendimiento);
            System.out.println("Cultivo creado éxitosamente...");
        }catch(GestionHuertosException e){
            System.out.println(e.getMessage());
        }
    }
    private void creaHuerto(){
        String nom = validaEntradaString("Nombre: ");
        float sup = validaEntradaFloat("Superficie: ");
        while(sup <= 0){
            System.out.println("Valor no válido, Ingrese valor positivo");
            sup = validaEntradaFloat("Superficie: ");
        }

        String ubi = validaEntradaString("Ubicación: ")
        System.out.print("Rut propietario: ");
        Rut rut = Rut.of(tcld.next());

        try{
            (cP.createHuerto(nom, sup, ubi, rut)) {
                System.out.println("Huerto creado exitosamente...\n");
                System.out.println("Agregando cuarteles al huerto...");
                System.out.print("Nro de cuarteles: ");
                int nroCuarteles = tcld.nextInt();

                for (int i = 0; i < nroCuarteles; i++) {
                    System.out.print("Id cuartel: ");
                    int idCuartel = tcld.nextInt();
                    System.out.print("Superficie cuartel: ");
                    float superficie = tcld.nextFloat();
                    System.out.print("Id cultivo del cuartel: ");
                    int idCultivo = tcld.nextInt();
                    if (cP.addCuartelToHuerto(nom, idCuartel, superficie, idCultivo)) {
                        System.out.println("modelo.Cuartel agregado exitosamente al huerto\n");
                    } else System.out.println("No se pudo agregar el cuartel...\n");
                }
            }
        } catch(GestionHuertosException e){
                System.out.println(e.getMessage());
        }
    }
    private void agregaCuartelesAHuerto(){
        String nomHuerto = validaEntradaString("Nombre del huerto: ");
        System.out.println("Agregando cuarteles a huerto...");
        int numCuarteles = validaEntradaInt("Numero de cuarteles a agregar: ");
        while(numCuarteles <= 0){
            System.out.println("Número de cuarteles inválido. Ingrese valor positivo.");
            numCuarteles = validaEntradaInt("Numero de cuarteles a agregar: ");
        }

        for(int i = 0; i < numCuarteles; i++) {
            int idCuartel = validaEntradaInt("\nId Cuartel: ");
            float supCuartel = validaEntradaFloat("Superficie cuartel: ");
            int idCultivo = validaEntradaInt("Id cultivo del cuartel: ");
            try{
                cP.addCuartelToHuerto(nomHuerto, idCuartel, supCuartel, idCultivo);
                System.out.println("\nCuartel agregado éxitosamente al huerto");
            }catch(GestionHuertosException e){
                System.out.println(e.getMessage());
            }
        }
    }
    private void cambiaEstadoCuartel(){

    }
    private void creaPlanDeCosecha(){
        System.out.print("Id plan: ");
        int idPlan =tcld.nextInt();
        System.out.print("Nombre plan: ");
        String nomPlan =tcld.next();
        System.out.print("Fecha inicio (dd/mm/aaaa): ");
        LocalDate fIni = fechaFormateada(tcld.next());
        System.out.print("Fecha fin estimada (dd/mm/aaaa): ");
        LocalDate fFin = fechaFormateada(tcld.next());
        System.out.print("Meta kilos: ");
        double meta =tcld.nextDouble();
        System.out.print("Precio base por kilo: ");
        double precio =tcld.nextDouble();
        System.out.print("Nombre del huerto: ");
        String nomHuerto =tcld.next();
        System.out.print("Id del cuartel: ");
        int idCuartel =tcld.nextInt();

        if(cP.createPlanCosecha(idPlan, nomPlan, fIni, fFin, meta, precio, nomHuerto, idCuartel)) {
            System.out.println("\nPlan de Cosecha creado exitosamente");
            System.out.println("Agregando cuadrillas al plan de cosecha");
            System.out.print("Nro. de cuadrillas: ");
            int nroCuadrillas =tcld.nextInt();

            for(int i = 0; i < nroCuadrillas; i++){
                System.out.print("\nId cuadrilla: ");
                int idCuadrilla =tcld.nextInt();
                System.out.print("Nombre cuadrilla: ");
                String nombreCuadrilla =tcld.next();
                System.out.print("utilidades.Rut supervisor: ");
                Rut rutSupervisor = new Rut(tcld.next());

                if(cP.addCuadrillaToPlan(idPlan, idCuadrilla, nombreCuadrilla, rutSupervisor)){
                    System.out.println("modelo.Cuadrilla agregada exitosamente al plan de cosecha");
                }else System.out.println("No se ha podido agregar la cuadrilla al plan...");
            }

        }else System.out.println("No se pudo crear el Plan de Cosecha");
    }
    private void cambiaEstadoPlan(){}
    private void agregaCuadrillasAPlan(){}
    private void asignaCosechadoresAPlan(){
        System.out.print("Id del plan: ");
        int idPlan =tcld.nextInt();
        System.out.print("Id cuadrilla: ");
        int idCuadrilla =tcld.nextInt();
        System.out.print("Nro. cosechadores a asignar: ");
        int nroCos =tcld.nextInt();

        for(int i = 0; i < nroCos; i++){
            System.out.print("\nFecha de inicio asignacion (dd/mm/aaaa): ");
            LocalDate fIni = fechaFormateada(tcld.next());
            System.out.print("Fecha de término asignacion (dd/mm/aaaa): ");
            LocalDate fFin = fechaFormateada(tcld.next());
            System.out.print("Meta (Kilos): ");
            double metaKilos =tcld.nextDouble();
            System.out.print("utilidades.Rut cosechador: ");
            Rut rut = new Rut(tcld.next());

            if(cP.addCosechadorToCuadrilla(idPlan, idCuadrilla, fIni, fFin, metaKilos, rut)){
                System.out.println("modelo.Cosechador asignado exitosamente a una cuadrilla del plan de cosecha");
            } else System.out.println("No se ha podido agregar el cosechador a la cuadrilla...");
        }
    }
    private void agregaPesajeACosechador(){}
    private void pagaPesajesPendientesACosechador(){}
    private void listaPersonas(){
        String[] listPropietarios = cP.listPropietarios();
        String[] listSupervisores = cP.listSupervisores();
        String[] listCosechadores = cP.listCosechadores();

        System.out.println("LISTADO DE PROPIETARIOS" +
                "\n-----------------------");
        System.out.printf("%-12s %-15s %-20s %-25s %-25s %-15s%n",
                "utilidades.Rut", "Nombre", "Dirección", "email", "Dirección comercial", "Nro. huertos");
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
                "utilidades.Rut", "Nombre", "Dirección", "email", "Profesión", "Nombre cuadrilla");
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
                "utilidades.Rut", "Nombre", "Dirección", "email", "Fecha nacimiento", "Nro. Cuadrillas");
        if(listCosechadores.length == 0){
            System.out.println("No hay cosechadores registrados...");
        }else{
            for(String lC : listCosechadores){
                System.out.println(lC);
            }
        }
    }
    private void listaCultivos() {
        System.out.println("\nLISTADO DE CULTIVOS");
        System.out.println("--------------------");
        System.out.printf("%-6s %-15s %-15s %-12s %-15s%n",
                "Id", "Especie", "Variedad", "Rendimiento", "Nro. cuarteles");

        String[] listaCultivos = cP.listCultivos();

        if (listaCultivos.length == 0) {
            System.out.println("No existen cultivos registrados.");
        } else {
            for (String linea : listaCultivos) {
                System.out.println(linea);
            }
        }
    }
    private void listaHuertos(){
        String [] listaDeHuertos = cP.listHuertos();
        if(listaDeHuertos.length == 0){
            System.out.println("No existen huertos registrados");
        }else{
            System.out.println("\nLISTADO DE HUERTOS");
            System.out.println("------------------");
            System.out.printf("%-20s %-12s %-20s %-15s %-20s %-15s%n",
                    "Nombre", "Superficie", "Ubicación", "utilidades.Rut propietario", "Nombre propietario", "Nro. cuarteles");
            for (String listaDeHuerto : listaDeHuertos) {
                System.out.println(listaDeHuerto);
            }
        }
    }
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
    private void listaPesajes(){}
    private void listaPesajesCosechador(){}
    private void listaPagosPesajes(){}

    private String validaEntradaString(String mensaje){
        System.out.print(mensaje);
        String cadena = tcld.next();
        while(cadena.trim().isEmpty()){
            System.out.println("No debe ser vacío...");
            System.out.print(mensaje);
            cadena = tcld.next();
        }
        return cadena;
    }
    private int validaEntradaInt(String mensaje, String condicion) {
        int num = 0;
        boolean valido = false;

        while (!valido) {
            String entrada = validaEntradaString(mensaje);

            try {
                num = Integer.parseInt(entrada);

                if (!condicion.isBlank()) {
                    if (validaRango(num, condicion)) {
                        valido = true;
                    } else {
                        System.out.println("Valor fuera de rango.");
                    }
                } else {
                    valido = true;
                }

            } catch (NumberFormatException e) {
                System.out.println("ENTRADA NO VÁLIDA, ingresa un número válido.");
            }
        }
        return num;
    }
    private long validaEntradaLong(String mensaje, String condicion) {
        long num = 0;
        boolean valido = false;

        while (!valido) {
            String entrada = validaEntradaString(mensaje);

            try {
                num = Long.parseLong(entrada);

                if (!condicion.isBlank()) {
                    if (validaRango(num, condicion)) {
                        valido = true;
                    } else {
                        System.out.println("Valor fuera de rango.");
                    }
                } else {
                    valido = true;
                }

            } catch (NumberFormatException e) {
                System.out.println("ENTRADA NO VÁLIDA, ingresa un número válido.");
            }
        }
        return num;
    }
    private float validaEntradaFloat(String mensaje, String condicion) {
        float num = 0;
        boolean valido = false;

        while (!valido) {
            String entrada = validaEntradaString(mensaje);

            try {
                num = Float.parseFloat(entrada);

                if (!condicion.isBlank()) {
                    if (validaRango(num, condicion)) {
                        valido = true;
                    } else {
                        System.out.println("Valor fuera de rango.");
                    }
                } else {
                    valido = true;
                }

            } catch (NumberFormatException e) {
                System.out.println("ENTRADA NO VÁLIDA, ingresa un número válido.");
            }
        }
        return num;
    }
    private byte validaEntradaByte(String mensaje, String condicion) {
        byte num = 0;
        boolean valido = false;

        while (!valido) {
            String entrada = validaEntradaString(mensaje);
            try {
                num = Byte.parseByte(entrada);

                if (!condicion.isBlank()) {
                    if (validaRango(num, condicion)) {
                        valido = true;
                    } else {
                        System.out.println("Valor fuera de rango.");
                    }
                } else {
                    valido = true;
                }

            } catch (NumberFormatException e) {
                System.out.println("ENTRADA NO VÁLIDA, ingresa un número válido.");
            }
        }
        return num;
    }


    /*
    Formato de cálculo de rango uwu:
    "r(n,m)"

    r = indica que se evalúa rango
    () = indica el valor del rango. Pueden ser ( o [ según si es
         incluyente o excluyente
    n,m = valores númericos separados por ","

    Formato cálculo positivo:
    "p0" ó "p"

    p0 = evalúa si el valor es mayor o igual a 0
    p = evalúa si es mayor a 0
    */
    //basicamente acabo de hacer un mini lenguaje en un lenguaje xd
    private boolean validaRango(double valor, String condicion) {
        char operacion = condicion.charAt(0);
        String rango = condicion.substring(1);

        switch (operacion) {
            case 'r' -> {
                char desde = rango.charAt(0);
                char hasta = rango.charAt(rango.length() - 1);

                String valores = rango.substring(1, rango.length() - 1); // elimina paréntesis/brackets
                String[] partes = valores.split(",");
                double min = Double.parseDouble(partes[0].trim());
                double max = Double.parseDouble(partes[1].trim());

                boolean mayorQueMin = (desde == '(') ? valor > min : valor >= min;
                boolean menorQueMax = (hasta == ')') ? valor < max : valor <= max;

                return mayorQueMin && menorQueMax;
            }
            case 'p' -> {
                return rango.startsWith("0") ? valor >= 0 : valor > 0;
            }
        }
        return false; //esto es solo pa que no reclame el compilador :v
    }


    private Enum validaEnum(String clase, String msj) {
        String cadena;
        Enum e = null;
        boolean valido = false;

        while (!valido) {
            cadena = validaEntradaString(msj).toUpperCase().trim();

            try {
                switch (clase) {
                    case "Calidad" -> e = Calidad.valueOf(cadena);
                    case "EstadoFenologico" -> e = EstadoFenologico.valueOf(cadena);
                    case "EstadoPlan" -> e = EstadoPlan.valueOf(cadena);
                }
                valido = true; // si no lanza excepción, la entrada fue válida
            } catch (IllegalArgumentException ex) {
                System.out.println("Entrada no válida, intente nuevamente.");
            }
        }

        return e;
    }
    private LocalDate validaEntradaFecha(String mensaje) {
        DateTimeFormatter formato = DateTimeFormatter.ofPattern("dd/MM/yyyy");
        LocalDate fecha;
        String entrada;

        while (true) {
            System.out.print(mensaje);
            entrada = tcld.next().trim();
            try{
                fecha = LocalDate.parse(entrada, formato);
                break;
            }catch (DateTimeParseException e){
                System.out.println("FORMATO DE FECHA NO VÁLIDO, ingere en formato dd/MM/yyyy");
            }
        }
        return fecha;
    }

}
