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
                }
                case 5 -> {
                    System.out.println("-> Asignar Cosechadores a Plan");
                }
                case 6 -> {
                    System.out.println("-> Listar Cultivos");
                }
                case 7 -> {
                    System.out.println("-> Listar Huertos");
                }
                case 8 -> {
                    System.out.println("-> Listar Personas");
                }
                case 9 -> {
                    System.out.println("-> Listar Planes de Cosecha");
                }
                case 10 -> {
                    System.out.println("-> Saliendo del sistema...");
                }
                default -> System.out.println("Opción no válida. Intente nuevamente.");
            }

        } while (opcion != 10);
    }
    private void creaPersona(){
        System.out.print("Rol persona (1 = propietario, 2 = supervisor, 3 = cosechador: ");
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
        String fecha = sc.next();
        LocalDate fechaNac = LocalDate.parse(fecha, FORMATO);

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
}
