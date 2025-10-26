package utilidades;
//es necesario cambiar muchas cosas en los ejemplos. Les dejo mis comentarios en cada parte para evitar cualquier problema en la comprension de Rut
public class Rut {
    private long numero;
    private char dv;

    private Rut(long numero, char dv) {
        this.numero = numero;
        this.dv = dv;
    }

    public Rut of(String rutStr) {
        if (rutStr == null) {
            throw new IllegalArgumentException("rut no puede ser null");
        }

        String s = rutStr.trim().replace(".", "").replace(" ", ""); //elimina espacios al inicio y final del rut. Además, reemplaza los puntos y espacios entre los numeros por espacios vacios. Convierte todos estos cambios en un String (ej: 12.345.678-9 = 12345678-9)

        String[] parts = s.split("-"); //busca el - en el rut y lo separa en dos partes (ej: 12345678-9 -> [0]=12345678 , [1]=9)

        if (parts.length != 2) {
            throw new IllegalArgumentException("Formato de RUT invalido. Debe contener un '-' como separador, ejemplo: 12345678-9");
        }

        String numPart = parts[0]; //parte numerica conseguida en el split (ej: 12345678)
        String dvPart = parts[1].trim().toUpperCase(); //dv conseguido en el split (ej: 9)

        if (numPart.isEmpty() || dvPart.isEmpty()) {
            throw new IllegalArgumentException("Formato de RUT inválido: parte numérica o DV vacía");
        }

        for (int i = 0; i < numPart.length(); i++) {
            if (!Character.isDigit(numPart.charAt(i))) { //recorre el String por partes, verificando que la parte numerica del rut sea totalmente compuesta por numeros del 0 al 9
                throw new IllegalArgumentException("La parte numerica del RUT debe contener sólo dígitos");
            }
        }

        //if (numPart.length() < 7 || numPart.length() > 8) { //en caso de decidir guardar la parte numerica como String, por si llegara a empezar con 0
        //    throw new IllegalArgumentException("La parte numérica del RUT debe tener entre 7 y 8 dígitos.");
        //}

        long numero;

        try {
            numero = Long.parseLong(numPart); //toma los digitos (0-9) en un String y lo convierte en long
        } catch (NumberFormatException e) {
            throw new IllegalArgumentException("Número de RUT demasiado grande o invalido");
        } //Pros: convierte el String en long. Contras: esto eliminaria los 0 a la izquierda (ej: 01234567=1234567)

        if (dvPart.length() != 1) {
            throw new IllegalArgumentException("DV invalido. Debe ser un caracter (0-9 o K).");
        }

        char dvChar = dvPart.charAt(0);

        if (!(Character.isDigit(dvChar) || dvChar == 'K')) { //confirma si el dv es un numero entre 0 y 9 o K
            throw new IllegalArgumentException("DV invalido. Debe ser 0-9 o K.");
        }

        return new Rut(numero, dvChar);
    }

    @Override
    public String toString(){
        return Long.toString(numero)+"-"+dv;
    }
}

