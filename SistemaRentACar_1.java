import java.io.FileWriter;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Scanner;
/*************************************************************************************************************/
class Vehiculo {
    String id;
    String tipo;
    String color;
    String combustible;
    String estado;
    double precioPorDia;
    Vehiculo sig;

    public Vehiculo(String id, String tipo, String color, String combustible, double precioPorDia) {
        this.id = id;
        this.tipo = tipo;
        this.color = color;
        this.combustible = combustible;
        this.estado = "Disponible"; // estado inicial del vehiculo 
        this.precioPorDia = precioPorDia;
        this.sig = null; // siguiente vehiculo en la lista
    }
    /**
     * constructor de la clase vehiculo
     * inicializa un objeto vehiculo con los atributos proporcionados
     */

    public void mostrarDetalles() {
        System.out.println("ID: " + id + ", Tipo: " + tipo + ", Color: " + color + ", Combustible: " + combustible + ", Estado: " + estado + ", Precio por dia: ¢" + precioPorDia);
    }
}
/*************************************************************************************************************/
class Cliente {
    String nombre;
    String cedula;
    String direccion;
    String numeroTarjeta;
    String tipoTarjeta;
    String tipoCliente;

    public Cliente(String nombre, String cedula, String direccion, String numeroTarjeta, String tipoTarjeta, String tipoCliente) {
        this.nombre = nombre;
        this.cedula = cedula;
        this.direccion = direccion;
        this.numeroTarjeta = numeroTarjeta;
        this.tipoTarjeta = tipoTarjeta;
        this.tipoCliente = tipoCliente;
    }

    /**
     * constructor de la clase cliente
     * inicializa un objeto cliente con la informacion personal y financiera
     */

    public void mostrarDetalles() {
        System.out.println("Nombre: " + nombre + ", Cedula/Pasaporte: " + cedula + ", Direccion: " + direccion + ", Numero de Tarjeta: " + numeroTarjeta + ", Tipo de Cliente: " + tipoCliente);
    }
    /**
     * muestra los detalles del cliente en la pantalla
     */
}
/*************************************************************************************************************/
class Recibo {
    Cliente cliente;
    Vehiculo vehiculo;
    Date fechaAlquiler;
    Date fechaDevolucion;
    double montoTotal;
    boolean seguroAdicional;

    public Recibo(Cliente cliente, Vehiculo vehiculo, Date fechaAlquiler, Date fechaDevolucion, double montoTotal, boolean seguroAdicional) {
        this.cliente = cliente;
        this.vehiculo = vehiculo;
        this.fechaAlquiler = fechaAlquiler;
        this.fechaDevolucion = fechaDevolucion;
        this.montoTotal = montoTotal;
        this.seguroAdicional = seguroAdicional;
    }
    /**
     * constructor de la clase recibo
     * inicializa un recibo con los detalles del cliente, vehiculo y alquiler
     */

    public void generarReciboTxt() {
        try {
            FileWriter writer = new FileWriter("Recibo_" + cliente.cedula + ".txt");
            SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy");

            writer.write("Recibo de Alquiler\n");
            writer.write("Cliente: " + cliente.nombre + "\n");
            writer.write("Cedula/Pasaporte: " + cliente.cedula + "\n");
            writer.write("Direccion: " + cliente.direccion + "\n");
            writer.write("Vehiculo Alquilado: " + vehiculo.tipo + " " + vehiculo.color + "\n");
            writer.write("ID Vehiculo: " + vehiculo.id + "\n");
            writer.write("Fecha de Alquiler: " + sdf.format(fechaAlquiler) + "\n");
            writer.write("Fecha de Devolucion: " + sdf.format(fechaDevolucion) + "\n");
            writer.write("Monto Total: ¢" + montoTotal + "\n");
            if (seguroAdicional) {
                writer.write("Seguro Adicional: Si\n");
            } else {
                writer.write("Seguro Adicional: No\n");
            }
            writer.close();
            System.out.println("Recibo generado con exito!");
        } catch (IOException e) {
            System.out.println("Error al generar el recibo.");
        }
    }
    /*Genera un archivo txt con los detalles del recibo de alquiler*/
}
/*************************************************************************************************************/
class RentACar {
    Vehiculo cabezaDisponible = null; 
    Vehiculo cabezaAlquilado = null; 
    double montoTotalRecaudado = 0.0;

    public void agregarVehiculo(Vehiculo vehiculo) {
        if (cabezaDisponible == null) {
            cabezaDisponible = vehiculo;
        } else {
            Vehiculo actual = cabezaDisponible;
            while (actual.sig != null) {
                actual = actual.sig;
            }
            actual.sig = vehiculo;
        }
        System.out.println("Vehiculo agregado: " + vehiculo.id);
    }
    /* Agrega un nuevo vehiculo a la lista de vehiculos disponibles */

    public void alquilarVehiculo(String vehiculoId, Cliente cliente, boolean seguroAdicional) {
        Vehiculo vehiculo = buscarVehiculo(vehiculoId, "Disponible");
        if (vehiculo != null) {
            vehiculo.estado = "Alquilado";

            double monto = vehiculo.precioPorDia;

            Recibo recibo = new Recibo(cliente, vehiculo, new Date(), new Date(), monto, seguroAdicional);
            recibo.generarReciboTxt();

            agregarVehiculoAlquilado(vehiculo);
            montoTotalRecaudado += monto;
        } else {
            System.out.println("Vehiculo no disponible.");
        }
    }
    /* Alquila un vehículo disponible y genera un recibo */

    public void devolverVehiculo(String vehiculoId) {
        Vehiculo vehiculo = buscarVehiculo(vehiculoId, "Alquilado");
        if (vehiculo != null) {
            vehiculo.estado = "Disponible";
            agregarVehiculoDisponible(vehiculo);
            System.out.println("Vehiculo devuelto: " + vehiculo.id);
        } else {
            System.out.println("Vehiculo no encontrado en lista de alquiler.");
        }
    }
    /* Devuelve un vehiculo alquilado y lo vuelve a la lista de disponibles */

    private Vehiculo buscarVehiculo(String vehiculoId, String estado) {
        Vehiculo actual = (estado.equals("Disponible")) ? cabezaDisponible : cabezaAlquilado;
        while (actual != null) {
            if (actual.id.equals(vehiculoId) && actual.estado.equals(estado)) {
                return actual;
            }
            actual = actual.sig;
        }
        return null;
    }
    /* Busca un vehiculo por ID  */

    private void agregarVehiculoAlquilado(Vehiculo vehiculo) {
        if (cabezaAlquilado == null) {
            cabezaAlquilado = vehiculo;
        } else {
            Vehiculo actual = cabezaAlquilado;
            while (actual.sig != null) {
                actual = actual.sig;
            }
            actual.sig = vehiculo;
        }
    }

    private void agregarVehiculoDisponible(Vehiculo vehiculo) {
        if (cabezaDisponible == null) {
            cabezaDisponible = vehiculo;
        } else {
            Vehiculo actual = cabezaDisponible;
            while (actual.sig != null) {
                actual = actual.sig;
            }
            actual.sig = vehiculo;
        }
    }

    public void consultarVehiculos() {
        System.out.println("Vehiculos disponibles:");
        mostrarVehiculos(cabezaDisponible);
        System.out.println("\nVehiculos alquilados:");
        mostrarVehiculos(cabezaAlquilado);
    }
    /*Muestra los vehiculos disponibles y alquilados*/

    private void mostrarVehiculos(Vehiculo cabeza) {
        Vehiculo actual = cabeza;
        while (actual != null) {
            actual.mostrarDetalles();
            actual = actual.sig;
        }
    }
    /**
     * Muestra los detalles de una lista de vehiculos.
     */

    public double calcularMontoTotal() {
        return montoTotalRecaudado;
    }
    /*Calcula el monto total recaudado por alquileres*/
}
/*************************************************************************************************************/
public class SistemaRentACar_1 {
    public static void main(String[] args) {
        RentACar rentACar = new RentACar();
        Scanner scanner = new Scanner(System.in);

        while (true) {
            System.out.println("\n--- Menu Principal ---");
            System.out.println("1. Agregar Vehiculo");
            System.out.println("2. Alquilar Vehiculo");
            System.out.println("3. Devolver Vehiculo");
            System.out.println("4. Consultar Vehiculo");
            System.out.println("5. Monto Total Recaudado");
            System.out.println("6. Salir");
            int opcion = obtenerOpcionMenu(scanner);

            switch (opcion) {
                case 1:
                    agregarVehiculo(rentACar, scanner);
                    break;
                case 2:
                    alquilarVehiculo(rentACar, scanner);
                    break;
                case 3:
                    devolverVehiculo(rentACar, scanner);
                    break;
                case 4:
                    rentACar.consultarVehiculos();
                    break;
                case 5:
                    System.out.println("Monto total recaudado: ¢" + rentACar.calcularMontoTotal());
                    break;
                case 6:
                    System.out.println("¡Hasta luego!");
                    scanner.close();
                    return;
                default:
                    System.out.println("Opcion invalida, intenta de nuevo.");
            }
        }
    }

    private static int obtenerOpcionMenu(Scanner scanner) {
        while (true) {
            try {
                System.out.print("Selecciona una opcion: ");
                return Integer.parseInt(scanner.nextLine());
            } catch (NumberFormatException e) {
                System.out.println("Por favor ingrese un numero valido.");
            }
        }
    }

    private static void agregarVehiculo(RentACar rentACar, Scanner scanner) {
        String id = obtenerTexto(scanner, "ID Vehiculo: ");
        String tipo = obtenerTexto(scanner, "Tipo de Vehiculo: ");
        String color = obtenerTexto(scanner, "Color: ");
        String combustible = obtenerTexto(scanner, "Combustible: ");
        double precioPorDia = obtenerNumero(scanner, "Precio por dia: ");
        rentACar.agregarVehiculo(new Vehiculo(id, tipo, color, combustible, precioPorDia));
    }

    private static void alquilarVehiculo(RentACar rentACar, Scanner scanner) {
        String idAlquilar = obtenerTexto(scanner, "ID Vehiculo a alquilar: ");
        String nombre = obtenerTexto(scanner, "Nombre del Cliente: ");
        String cedula = obtenerCedula(scanner);
        boolean seguroAdicional = obtenerSeguroAdicional(scanner);
        Cliente cliente = new Cliente(nombre, cedula, "Dirección Ejemplo", "123456789", "Visa", "Nacional");
        rentACar.alquilarVehiculo(idAlquilar, cliente, seguroAdicional);
    }

    private static void devolverVehiculo(RentACar rentACar, Scanner scanner) {
        String idDevolver = obtenerTexto(scanner, "ID Vehiculo a devolver: ");
        rentACar.devolverVehiculo(idDevolver);
    }

    private static String obtenerTexto(Scanner scanner, String mensaje) {
        System.out.print(mensaje);
        return scanner.nextLine().toUpperCase();  // Convertir a mayusculas
    }

    private static double obtenerNumero(Scanner scanner, String mensaje) {
        while (true) {
            try {
                System.out.print(mensaje);
                return Double.parseDouble(scanner.nextLine());
            } catch (NumberFormatException e) {
                System.out.println("Por favor ingresa un numero valido.");
            }
        }
    }

    private static String obtenerCedula(Scanner scanner) {
        while (true) {
            System.out.print("Cedula (solo numeros): ");
            String cedula = scanner.nextLine();
            if (cedula.matches("\\d+")) {
                return cedula;
            } else {
                System.out.println("La cedula debe contener solo numeros.");
            }
        }
    }

    private static boolean obtenerSeguroAdicional(Scanner scanner) {
        while (true) {
            System.out.print("¿Seguro Adicional (1 = Si, 2 = No)? ");
            double opcion = obtenerNumero(scanner, "");
            if (opcion == 1) return true;
            if (opcion == 2) return false;
            System.out.println("Por favor selecciona 1 para Si o 2 para No.");
        }
    }
}
/*************************************************************************************************************/