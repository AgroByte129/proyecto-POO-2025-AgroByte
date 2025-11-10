package vista;

import controlador.ControladorProduccion;
import utilidades.*;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;
import java.util.Locale;
import java.util.Scanner;

public class GestionHuertosUI {
    private static GestionHuertosUI instance;
    private Scanner tcld = new Scanner(System.in).useDelimiter("[\\t\\n]+");
    private ControladorProduccion cP = ControladorProduccion.getInstance();

    private GestionHuertosUI(){
        try{
            cP.readDataFromTextFile();
        }catch(GestionHuertosException e){
            System.out.println(e.getMessage());
        }
    }
    public static GestionHuertosUI getInstance(){
        if(instance == null){
            instance = new GestionHuertosUI();
        }
        return instance;
    }

    //*** MENÚ PRINCIPAL ***
    public void menu(){
        byte respuesta;
        do{
            System.out.println("""
                    ::: MENU PRINCIPAL :::
                    1. Crear Personas
                    2. Menú Huertos
                    3. Menú Planes de Cosecha
                    4. Menú Listados
                    5. Salir
                    """);
            respuesta = validaNumero("\tOpción: ", "r[1,5]", "byte").byteValue();

            switch(respuesta){
                case 1 -> creaPersona();
                case 2 -> menuHuertos();
                case 3 -> menuPlanesCosecha();
                case 4 -> menuListados();
                case 5 -> System.out.println("Saliendo...");
            }
        }while(respuesta != 5);
    }
    //*** MENÚ HUERTO ***
    private void menuHuertos(){
        byte respuesta;
        do{
            System.out.print("""
                            
                            >>> SUBMENU HUERTOS <<<
                            1. Crear Cultivo
                            2. Crear Huerto
                            3. Agregar Cuarteles a Huerto
                            4. Cambiar Estado Cuartel
                            5. Volver
                            """);
            respuesta = validaNumero("\tOpción: ", "r[1,5]", "byte").byteValue();

            switch(respuesta){
                case 1 -> creaCultivo();
                case 2 -> creaHuerto();
                case 3 -> agregaCuartelesAHuerto();
                case 4 -> cambiaEstadoCuartel();
                case 5 -> System.out.println("\n-> Volviendo al menú principal");
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
                    """);
            respuesta = validaNumero("\tOpción: ", "r[1,7]", "byte").byteValue();

            switch(respuesta){
                case 1 -> creaPlanDeCosecha();
                case 2 -> cambiaEstadoPlan();
                case 3 -> agregaCuadrillasAPlan();
                case 4 -> asignaCosechadoresAPlan();
                case 5 -> agregaPesajeACosechador();
                case 6 -> pagaPesajesPendientesACosechador();
                case 7 -> System.out.println("\n-> Volviendo al menú principal");

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
                    """);
            respuesta = validaNumero("\tOpción: ", "r[1,10]", "byte").byteValue();

            switch(respuesta){
                case 1 -> listaPropietarios();
                case 2 -> listaSupervisores();
                case 3 -> listaCosechadores();
                case 4 -> listaCultivos();
                case 5 -> listaHuertos();
                case 6 -> listaPlanesCosecha();
                case 7 -> listaPesajes();
                case 8 -> listaPesajesCosechador();
                case 9 -> listaPagosPesajes();
                case 10 -> System.out.println("\n-> Volviendo al menú principal");

            }
        }while(respuesta != 10);
        menu();
    }

    private void creaPersona(){
        System.out.println("\n-> Creando a una persona\n");
        String msj = "Rol persona (1 = propietario, 2 = supervisor, 3 = cosechador): ";
        byte rol = validaNumero(msj,"r[1,3]", "byte").byteValue();
        Rut rut = ingresaRut("Rut: ");
        String nom = validaEntradaString("Nombre: ");
        String email = validaEntradaString("Email: ");
        String dir = validaEntradaString("Dirección: ");
        LocalDate fechaNac = validaEntradaFecha("Fecha de nacimiento (dd/mm/aaaa): ");

        switch(rol){
            case 1 -> {
                String dirComercial = validaEntradaString("Dirección comercial: ");
                try{
                    cP.createPropietario(rut, nom, email, dir, dirComercial);
                    System.out.println("\n-> Propietario creado éxitosamente");
                }catch(GestionHuertosException e){
                    System.out.println(e.getMessage());
                }
            }
            case 2 -> {
                String profesion = validaEntradaString("Profesión: ");
                try{
                    cP.createSupervisor(rut, nom, email, dir, profesion);
                    System.out.println("\n-> Supervisor creado éxitosamente");
                }catch (GestionHuertosException e){
                    System.out.println(e.getMessage());
                }
            }
            case 3 -> {
                try{
                    cP.createCosechador(rut, nom, email, dir, fechaNac);
                    System.out.println("\n-> Cosechador creado éxitosamente");
                }catch(GestionHuertosException e){
                    System.out.println(e.getMessage());
                }
            }
        }
    }
    private void creaCultivo(){
        System.out.println("\n-> Creando un cultivo\n");
        int id = validaNumero("Identificación (No negativo): ", "p", "int").intValue();
        String especie = validaEntradaString("Especie: ");
        String variedad = validaEntradaString("Variedad: ");
        float rendimiento = validaNumero("Rendmiento: ", "p","float").floatValue();

        try{
            cP.createCultivo(id, especie, variedad, rendimiento);
            System.out.println("\n-> Cultivo creado éxitosamente\n");
        }catch(GestionHuertosException e){
            System.out.println(e.getMessage());
        }
    }
    private void creaHuerto(){
        System.out.println("\n-> Creando un huerto\n");
        String nom = validaEntradaString("Nombre: ");
        float sup = validaNumero("Superficie: ", "p", "float").floatValue();

        String ubi = validaEntradaString("Ubicación: ");
        Rut rut = ingresaRut("Rut propietario: ");
        try{
            cP.createHuerto(nom, sup, ubi, rut);
            System.out.println("\n-> Huerto creado éxitosamente");
        } catch(GestionHuertosException e){
                System.out.println(e.getMessage());
        }
    }
    private void agregaCuartelesAHuerto(){
        System.out.println("\n-> Agregando cuarteles a huerto\n");
        String nomHuerto = validaEntradaString("Nombre del huerto: ");
        System.out.println("\n-> Agregar cuarteles\n");
        int numCuarteles = validaNumero("Numero de cuarteles a agregar: ", "p", "int").intValue();

        for(int i = 0; i < numCuarteles; i++) {
            int idCuartel = validaNumero("\nId Cuartel: ", "p", "int").intValue();
            float supCuartel = validaNumero("Superficie cuartel: ", "p", "float").floatValue();
            int idCultivo = validaNumero("Id cultivo del cuartel: ", "p", "int").intValue();
            try{
                cP.addCuartelToHuerto(nomHuerto, idCuartel, supCuartel, idCultivo);
                System.out.println("\n-> Cuartel agregado éxitosamente al huerto");
            }catch(GestionHuertosException e){
                System.out.println(e.getMessage());
            }
        }
    }
    private void cambiaEstadoCuartel(){
        System.out.println("\n-> Cambiando el estado del cuartel\n");
        int idCuartel = validaNumero("Id del cuartel: ", "p", "int").intValue();
        String nomHuerto = validaEntradaString("Nombre del Huerto: ");
        EstadoFenologico estado = (EstadoFenologico) validaEnum("EstadoFenologico", "Nuevo estado: ");

        try{
            cP.changeEstadoCuartel(nomHuerto, idCuartel, estado);
            System.out.println("\n-> Se ha cambiado el estado del cuartel éxitosamente");
        }catch(GestionHuertosException e){
            System.out.println(e.getMessage());
        }
    }
    private void creaPlanDeCosecha(){
        System.out.println("\n-> Creando un plan de cosecha\n");
        int idPlan = validaNumero("Id de plan: ", "p", "int").intValue();
        String nomPlan = validaEntradaString("Nombre plan: ");

        LocalDate[] fechas = entradaFechaComienzoFin("Fecha inicio (dd/mm/aaaa): ",
                "Fecha fin estimada (dd/mm/aaaa");
        LocalDate fIni = fechas[0];
        LocalDate fFin = fechas[1];

        double meta = validaNumero("Meta kilos: ", "p", "double").doubleValue();
        double precio = validaNumero("Precio base por kilo: ", "p", "double").doubleValue();

        String nomHuerto = validaEntradaString("Nombre del Huerto: ");
        int idCuartel = validaNumero("Id del cuartel: ", "p", "int").intValue();

        try{
            cP.createPlanCosecha(idPlan, nomPlan, fIni, fFin, meta, precio, nomHuerto, idCuartel);
            System.out.println("\n-> Plan de Cosecha creado exitosamente");
        }catch(GestionHuertosException e){
            System.out.println(e.getMessage());
        }
    }
    private void cambiaEstadoPlan(){
        System.out.println("\n-> Cambiando el estado de un plan de cosecha\n");
        int idPlan = validaNumero("Id plan: ", "p", "int").intValue();
        String msj = "Nuevo estado plan [1 = Planificado, 2 = Ejecutando, 3 = Cerrado, 4 = Cancelado]: ";
        EstadoPlan estado = (EstadoPlan) validaEnum("EstadoPlan", msj);

        try{
            cP.changeEstadoPlan(idPlan, estado);
            System.out.println("\n-> Estado cambiado éxitosamente");
        }catch(GestionHuertosException e){
            System.out.println(e.getMessage());
        }
    }
    private void agregaCuadrillasAPlan(){
        System.out.println("\n-> Agregando cuadrillas a un plan de cosecha\n");
        int idPlan = validaNumero("Ide del plan: ", "p", "int").intValue();
        int nroCuadrillas = validaNumero("Nro de cuadrillas: ", "p", "int").intValue();

        for(int i = 0; i < nroCuadrillas; i++){
            int idCuadrilla = validaNumero("\nId Cuadrilla: ", "p", "int").intValue();
            String nombreCuadrilla = validaEntradaString("Nombre cuadrilla: ");
            Rut rutSupervisor = ingresaRut("Rut supervisor [12.345.678-9]: ");

            try{
                cP.addCuadrillaToPlan(idPlan, idCuadrilla, nombreCuadrilla, rutSupervisor);
                System.out.println("\n-> Cuadrilla agregada exitosamente al plan de cosecha");
            } catch(GestionHuertosException e){
                System.out.println(e.getMessage());
            }
        }
    }
    private void asignaCosechadoresAPlan(){
        System.out.println("\n-> Asignando cosechadores a un plan\n");
        int idPlan = validaNumero("Id del plan: ", "p", "int").intValue();
        int idCuadrilla = validaNumero("Id cuadrilla: ", "p", "int").intValue();
        int nroCos = validaNumero("Nro. cosechadores a asignar: ", "p", "int").intValue();

        for(int i = 0; i < nroCos; i++){
            LocalDate[] fechas = entradaFechaComienzoFin("\nFecha de inicio asignación (dd/mm/aaaa): ",
                    "Fecha de término asignacion (dd/mm/aaaa): ");
            LocalDate fIni = fechas[0];
            LocalDate fFin = fechas[1];
            double metaKilos = validaNumero("Meta kilos: ", "p", "double").doubleValue();
            Rut rut = ingresaRut("Rut cosechador: ");

            try{
                cP.addCosechadorToCuadrilla(idPlan, idCuadrilla, fIni, fFin, metaKilos, rut);
                System.out.println("\n-> Cosechador asignado exitosamente a una cuadrilla del plan de cosecha");
            } catch (GestionHuertosException e) {
                System.out.println(e.getMessage());
            }
        }
    }
    private void agregaPesajeACosechador(){
        System.out.println("\n-> Agregando pesaje a un cosechador\n");
        int idPesaje = validaNumero("Id pesaje: ", "p", "int").intValue();
        Rut rutCos = ingresaRut("Rut cosechador: ");
        int idPlan = validaNumero("Id plan: ", "p", "int").intValue();
        int idCuadrilla = validaNumero("Id cuadrilla: ", "p", "int").intValue();
        float cantKilos = validaNumero("Cantidad de kilos: ", "p", "float").floatValue();
        byte opcion = validaNumero("Calidad: [1 = Excelente, 2 = Suficiente, 3 = Deficiente]: ", "r[1,3]", "byte").byteValue();
        Calidad calidad = null;
        switch(opcion){
            case 1 -> calidad = Calidad.EXCELENTE;
            case 2 -> calidad = Calidad.SUFICIENTE;
            case 3 -> calidad = Calidad.DEFICIENTE;
        }

        try{
            cP.addPesaje(idPesaje, rutCos, idPlan, idCuadrilla, cantKilos, calidad);
            System.out.println("\n-> Pesaje agregado exitosamente al cosechador");
        }catch(GestionHuertosException e){
            System.out.println(e.getMessage());
        }
    }
    private void pagaPesajesPendientesACosechador(){
        System.out.println("\n-> Pagando pesajes pendientes de un cosechador\n");
        int idPesaje = validaNumero("Id pesaje: ", "p", "int").intValue();
        Rut rutCos = ingresaRut("Rut cosechador: ");

        try{//Se cambia el formato a alemán poque ahí se separa con "." los miles y "," los decimales
            System.out.printf(Locale.GERMANY,"%n%s %,.1f%n", "Monto pagado al cosechador: ", cP.addPagoPesaje(idPesaje, rutCos));
        }catch(GestionHuertosException e){
            System.out.println(e.getMessage());
        }
    }

    private void listaPropietarios(){
        String[] listPropietarios = cP.listPropietarios();
        System.out.println("LISTADO DE PROPIETARIOS" +
                "\n-----------------------");
        System.out.printf("%-10s  %-20s  %-20s  %-25s  %-25s  %-15s%n",
                "Rut", "Nombre", "Dirección", "email", "Dirección comercial", "Nro. huertos");
        if(listPropietarios.length == 0){
            System.out.println("No hay propietarios registrados...");
        }else {
            for(String lP : listPropietarios){
                String[] dato = lP.split(";");
                System.out.printf("%-10s %-20s  %-20s  %-25s  %-25s  %-15s%n", dato[0], dato[1], dato[2], dato[3], dato[4], dato[5]);
            }
        }
    }
    private void listaSupervisores(){
        String[] listSupervisores = cP.listSupervisores();
        System.out.println("""
                \nLISTADO DE SUPERVISORES
                -----------------------
                """);
        System.out.printf("%-12s %-15s %-20s %-25s %-25s %-15s%n",
                "utilidades.Rut", "Nombre", "Dirección", "email", "Profesión", "Nombre cuadrilla");
        if(listSupervisores.length == 0){
            System.out.println("No hay supervisores registrados...");
        }else{
            for(String lS : listSupervisores){
                System.out.println(lS);
            }
        }
    }
    private void listaCosechadores(){
        String[] listCosechadores = cP.listCosechadores();
        System.out.println("""
        \nLISTADO DE COSECHADORES
        -----------------------
        """);
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
        System.out.println("""
                \nLISTADO DE CULTIVOS
                -------------------
                """);
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
                    "Nombre", "Superficie", "Ubicación", "Rut propietario", "Nombre propietario", "Nro. cuarteles");
            for (String listaDeHuerto : listaDeHuertos) {
                System.out.println(listaDeHuerto);
            }
        }
    }
    private void listaPlanesCosecha(){
        String[] listaP = cP.listPlanesCosecha();

        System.out.println("""
        \nLISTADO DE PLANES COSECHA
        -------------------------
        """);

        System.out.printf(
                "%-6s %-15s %-15s %-15s %-10s %-17s %-12s %-12s %-20s %-15s %s%n",
                "Id", "Nombre", "Fecha inicio", "Fecha término", "Meta (kg)",
                "Precio base (kg)", "Estado", "Id cuartel", "Nombre huerto",
                "Nro. cuadrillas", "Meta %"
        );

        if(listaP.length == 0){
            System.out.println("No hay planes registrados...");
        }else {
            for(String l : listaP){
                System.out.println(l);
            }
        }
    }
    private void listaPesajes(){
        System.out.printf("""
                \nLISTADO DE PESAJES
                ------------------
                 %-3s %-10s  %-12s  %-10s %-10s  %-8s   %-7s %s%n
                """, "Id", "Fecha", "Rut Cosechador", "Calidad", "Cantidad Kg",
                "Precio $", "Monto $", "Pagado el");
        String[] lista = cP.listPesajes();
        if(lista.length == 0){
            System.out.println("No se han registrado pesajes...");
        }else{
           for(String l: lista){
               System.out.println(l);
           }
        }
    }
    private void listaPesajesCosechador(){
        System.out.print("Rut del cosechador: ");
        Rut rut = Rut.of(tcld.next());

        System.out.printf("""
                \nLISTADO DE PESAJES DEL COSECHADOR
                ---------------------------------
                 %-3s %-10s %-12s %-10s %-10s  %-8s   %-7s %s%n
                """, "Id", "Fecha", "Calidad", "Cantidad Kg", "Precio $", "Precio $", "Monto $", "Pagado el");
        try{
            String[] lista = cP.listPesajesCosechador(rut);
            if(lista.length == 0){
                System.out.println("El cosechador no tiene registrado pesajes...");
            }else{
                for(String l: lista){
                    System.out.println(l);
                }
            }
        }catch(GestionHuertosException e){
            System.out.println(e.getMessage());
        }
    }
    private void listaPagosPesajes(){
        System.out.printf("""
                \nLISTADO DE PAGOS DE PESAJES
                ---------------------------
                    %-3s %-10s %-6s %-11s %s%n
                """, "Id", "Fecha", "Monto $", "Nro.Pesajes", "Rut Cosechador");
        String[] lista = cP.listPagosPesajes();
        if(lista.length == 0){
            System.out.println("No se han registrado pagos...");
        }else{
            for(String l: lista){
                System.out.println(l);
            }
        }
    }

    private String validaEntradaString(String mensaje){
        System.out.print(mensaje);
        String cadena = tcld.next();
        while(cadena.isBlank()){
            System.out.println("No debe ser vacío...");
            System.out.print(mensaje);
            cadena = tcld.next();
        }
        return cadena.trim();
    }
    private Number validaNumero(String msj, String condicion, String tipo){
        Number num = null;
        boolean valido = false;

        while(!valido){
            try{
                String entrada = validaEntradaString(msj);
                switch(tipo){
                    case "byte" -> num = Byte.parseByte(entrada);
                    case "int" -> num = Integer.parseInt(entrada);
                    case "long" -> num = Long.parseLong(entrada);
                    case "float" -> num = Float.parseFloat(entrada);
                    case "double" -> num = Double.parseDouble(entrada);
                }

                if (!condicion.isBlank()) {
                    if (validaRango(num, condicion)) {
                        valido = true;
                    } else {
                        System.out.println("Valor fuera de rango.");
                    }
                } else {
                    valido = true;
                }

            }catch(NumberFormatException e){
                System.out.println("Entrada no válida. Ingrese un número");
            }
        }
        return num;
    }
    /*
    Formato de cálculo de rango uwu:
    "r(n,m)"

    r = indica que se evalúa rango
    () = indica el valor del rango. paréntesis redondos "( )" es excluyente,
         "[ ]" por legibilidad, dejemoslo para incluyente, aunque puede ser cualquier caracter xd...
         por ahora dejemoslo así :v
    n,m = valores númericos separados por ","

    Formato cálculo positivo:
    "p0" ó "p"

    p0 = evalúa si el valor es mayor o igual a 0
    p = evalúa si es mayor a 0
    */
    private boolean validaRango(Number valor, String condicion) {
        char operacion = condicion.charAt(0);
        String rango = "";
        if(condicion.length() > 1){
            rango = condicion.substring(1);
        }

        double v = valor.doubleValue(); //Convertimos a double para comparar genéricamente

        switch (operacion) {
            case 'r' -> {
                char desde = rango.charAt(0);
                char hasta = rango.charAt(rango.length() - 1);

                // eliminamos los delimitadores como [ , ( , ] , )
                String valores = rango.substring(1, rango.length() - 1);
                String[] partes = valores.split(",");

                double min = Double.parseDouble(partes[0].trim());
                double max = Double.parseDouble(partes[1].trim());

                boolean mayorQueMin = (desde == '(') ? v > min : v >= min;
                boolean menorQueMax = (hasta == ')') ? v < max : v <= max;

                return mayorQueMin && menorQueMax;
            }
            case 'p' -> {
                // Ejemplo: "p0" → >=0 ; "p" → >0
                return rango.startsWith("0") ? v >= 0 : v > 0;
            }
        }
        // Si no hay operación reconocida, se asume válido
        return true;
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
                System.out.println("FORMATO DE FECHA NO VÁLIDO. Ingere en formato dd/MM/yyyy");
            }
        }
        return fecha;
    }
    private LocalDate[] entradaFechaComienzoFin(String msjFIni, String msjFFin){
        LocalDate fIni = validaEntradaFecha(msjFIni);
        LocalDate fFin = validaEntradaFecha(msjFFin);

        while(fIni.isAfter(fFin)){
            System.out.println("La fecha de inicio debe ser antes de la fecha de fin");
            fIni = validaEntradaFecha(msjFIni);
            fFin = validaEntradaFecha(msjFFin);
        }
        return new LocalDate[] {fIni, fFin};
    }
    private Rut ingresaRut(String msj){
        String entrada;
        Rut rut;
        while(true){
            try{
                System.out.print(msj);
                entrada = validaEntradaString(msj);
                rut = Rut.of(entrada);
                break;
            }catch (IllegalArgumentException e){
                System.out.println(e.getMessage());
            }
        }
        return rut;
    }
}
