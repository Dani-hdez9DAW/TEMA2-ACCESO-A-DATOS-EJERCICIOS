import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.Scanner;

public class EjercicioMain {
    private static Scanner sc = new Scanner(System.in);

    private static void conectarBBDD() throws SQLException {
        String url = "jdbc:mysql://localhost:3306/MYSQL";
        String usuario = "DAM2";
        String contraseña = "DAM2";
        try (Connection conexion = DriverManager.getConnection(url, usuario, contraseña)) {


        } catch (Exception e) {
            System.out.println("Error al conectar con la base de datos");
        }
    }

    private static void mostrarMenu() {
        System.out.println("----EJERCICIOS-----");
        System.out.println("1. Ejercicio 1");
        System.out.println("2. Ejercicio 2");
        System.out.println("3. Ejercicio 3");
        System.out.println("4. Ejercicio 4");
        System.out.println("5. Ejercicio 5");
        System.out.println("6. Ejercicio 6");
        System.out.println("7. Ejercicio 7");
        System.out.println("0. Salir");
        System.out.println("------------------");
    }

    private static void procesarOpcion(int opcion) {
        switch (opcion) {
            case 0 -> System.out.println("Adios!");
            case 1 -> Ejercicio1();
            case 2 -> Ejercicio2();
            case 3 -> Ejercicio3();
            case 4 -> Ejercicio4();
            case 5 -> Ejercicio5();
            case 6 -> Ejercicio6();
            case 7 -> Ejercicio7();
        }
    }

    private static void Ejercicio6() {
    }

    private static void Ejercicio7() {
    }

    private static void Ejercicio5() {
    }

    private static void Ejercicio4() {
    }

    private static void Ejercicio3() {
    }

    private static void Ejercicio2() {
    }

    private static void Ejercicio1() {
    }

    public static void main(String[] args) {
        int opcion;
        do {
            mostrarMenu();
            opcion = sc.nextInt();
            if (opcion >= 0 && opcion <= 7) {
                procesarOpcion(opcion);
            }
        } while (opcion != 0);
    }
}