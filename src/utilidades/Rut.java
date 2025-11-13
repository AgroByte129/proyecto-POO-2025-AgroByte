package utilidades;

public class Rut {
    private long numero;
    private char dv;

    private Rut(long numero, char dv) {
        this.numero = numero;
        this.dv = dv;
    }

    public static Rut of(String rutStr) {
        if (rutStr == null) {
            throw new IllegalArgumentException("rut no puede ser null");
        }

        String s = rutStr.trim().replace(".", "").replace(" ", "");

        String[] parts = s.split("-");

        if (parts.length != 2) {
            throw new IllegalArgumentException("Formato de RUT invalido. Debe contener un '-' como separador, ejemplo: 12345678-9");
        }

        String numPart = parts[0];
        String dvPart = parts[1].trim().toUpperCase();

        if (numPart.isEmpty() || dvPart.isEmpty()) {
            throw new IllegalArgumentException("Formato de RUT inválido: parte numérica o DV vacía");
        }

        for (int i = 0; i < numPart.length(); i++) {
            if (!Character.isDigit(numPart.charAt(i))) {
                throw new IllegalArgumentException("La parte numerica del RUT debe contener sólo dígitos");
            }
        }

        long numero;

        try {
            numero = Long.parseLong(numPart);
        } catch (NumberFormatException e) {
            throw new IllegalArgumentException("Número de RUT demasiado grande o invalido");
        }

        if (dvPart.length() != 1) {
            throw new IllegalArgumentException("DV invalido. Debe ser un caracter (0-9 o K).");
        }

        char dvChar = dvPart.charAt(0);

        if (!(Character.isDigit(dvChar) || dvChar != 'K')) {
            throw new IllegalArgumentException("DV invalido. Debe ser 0-9 o K.");
        }
        char dvCal = calcularDV(numero);

        if(dvChar != dvCal) throw new IllegalArgumentException("DV invalido. Debe ingresar el DV correspondiente al rut.");

        return new Rut(numero, dvChar);
    }

    private static char calcularDV(long rut) {
        long m = 0, s = 1;
        for (; rut != 0; rut /= 10) {
            s = (s + rut % 10 * (9 - m++ % 6)) % 11;
        }
        if (s != 0) {
            return (char) (s + 47);
        } else {
            return 'K';
        }
    }

    @Override
    public String toString(){
        return Long.toString(numero)+"-"+dv;
    }
}

