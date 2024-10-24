import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.NodeList;
import org.xml.sax.SAXException;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import java.io.*;
import java.sql.*;
import java.util.Scanner;

public class EjercicioMain {
    private static Scanner sc = new Scanner(System.in);
    private final static String adminBBDD = "root";
    private final static String passBBDD = "";
    private final static String urlBBDD = "jdbc:mysql://localhost:3306/";
    private final static String usuarioDAM2 = "DAM2";
    private final static String passDAM2 = "DAM2";


    private static void borrarBBDDDAM2() {
        String url = urlBBDD;
        String administrador = adminBBDD;
        String contrasena = passBBDD;
        try (Connection conexion = DriverManager.getConnection(url, administrador, contrasena)) {
            Statement resultado = conexion.createStatement();
            String borrarUsuarioDAM = "DROP USER IF EXISTS 'DAM2'@'localhost'";
            System.out.println("Borrando usuario DAM2 ...");
            resultado.executeUpdate(borrarUsuarioDAM);
            String borrarBaseDatos = "DROP DATABASE IF EXISTS conciertos";
            resultado.executeUpdate(borrarBaseDatos);
            System.out.println("Borrando base de datos conciertos ...");
        } catch (Exception e) {
            System.out.println("No se han encontrado datos de la base de datos de conciertos o del usuario DAM2 (EJECUTA LOS EJERCICIOS)");
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

    private static void conectarBBDD(String usuario, String contraseña) throws SQLException {
        String url = "jdbc:mysql://localhost:3306/MYSQL";
        try (Connection conexion = DriverManager.getConnection(url, usuario, contraseña)) {
            System.out.println("Conexión establecida con la base de datos");
            conexion.close();
        } catch (Exception e) {
            System.out.println("Error al conectar con la base de datos");
        }
    }

    private static void procesarOpcion(int opcion) throws SQLException, IOException, ParserConfigurationException, SAXException {
        switch (opcion) {
            case 0 -> System.out.println("Cerrando programa ...");
            case 1 -> Ejercicio1();
            case 2 -> Ejercicio2();
            case 3 -> Ejercicio3();
            case 4 -> Ejercicio4();
            case 5 -> Ejercicio5();
            case 6 -> Ejercicio6();
            case 7 -> Ejercicio7();
        }
    }

    private static void Ejercicio1() {
        String url = urlBBDD;
        String adminUser = adminBBDD;
        String adminPass = passBBDD;
        try (Connection conexion = DriverManager.getConnection(url, adminUser, adminPass)) {
            System.out.println("Administrador conectado");
            conexion.close();
        } catch (Exception e) {
            System.out.println("Error al conectar con la base de datos");
        }
    }

    private static void Ejercicio2() {
        String url = urlBBDD;
        String adminUser = adminBBDD;
        String adminPass = passBBDD;
        try (Connection conexion = DriverManager.getConnection(url, adminUser, adminPass)) {
            Statement resultado = conexion.createStatement();
            String createUsuarioDAM = "CREATE USER 'DAM2'@'localhost' IDENTIFIED BY 'DAM2'";
            resultado.executeUpdate(createUsuarioDAM);
            System.out.println("Usuario DAM2 creado correctamente");
            String permisosUsuarioDAM = "GRANT ALL PRIVILEGES ON *.* TO 'DAM2'@'localhost'";
            System.out.println("Permisos al usuario DAM2 concedidos correctamente");
            resultado.executeUpdate(permisosUsuarioDAM);
            conexion.close();
        } catch (Exception e) {
            System.out.println("Error al conectar con la base de datos");
        }

    }

    private static void Ejercicio3() throws SQLException {
        conectarBBDD(usuarioDAM2, passDAM2);
    }

    private static void Ejercicio4() throws IOException {
        File ficheroConciertos = new File("Ficheros/CONCIERTOS.xml");
        FileReader ficheroALeer = new FileReader(ficheroConciertos);
        BufferedReader br = new BufferedReader(ficheroALeer);
        String lineaArchivo;
        while ((lineaArchivo = br.readLine()) != null) {
            System.out.println(lineaArchivo);
        }
    }

    private static void Ejercicio5() throws IOException, SAXException, ParserConfigurationException, SQLException {
        File ficheroXmlaLeer = new File("Ficheros/CONCIERTOS.xml");
        String url = urlBBDD;
        String usuarioAdmin = adminBBDD;
        String contraseñaAdmin = passBBDD;
        DocumentBuilderFactory dbFactory = DocumentBuilderFactory.newInstance();
        DocumentBuilder dBuilder = dbFactory.newDocumentBuilder();
        Document doc = dBuilder.parse(ficheroXmlaLeer);
        doc.getDocumentElement().normalize();

        // Extraer los conciertos
        NodeList conciertoList = doc.getElementsByTagName("concierto");


        try {
            // Crear la base de datos si no existe
            try (Connection connection = DriverManager.getConnection(url, usuarioAdmin, contraseñaAdmin);
                 Statement statement = connection.createStatement()) {
                String crearBaseDatosSQL = "CREATE DATABASE IF NOT EXISTS conciertos";
                statement.executeUpdate(crearBaseDatosSQL);
                System.out.println("Base de datos 'conciertos' creada o ya existe.");
            } catch (SQLException e) {
                e.printStackTrace();
                return;
            }

            // Actualizar la URL para conectarse a la base de datos 'conciertos'
            url = "jdbc:mysql://localhost:3306/conciertos";

            // Leer el fichero XML y extraer los datos
            DocumentBuilderFactory ficheroXml = DocumentBuilderFactory.newInstance();
            DocumentBuilder builder = ficheroXml.newDocumentBuilder();
            Document documentoXml = builder.parse(ficheroXmlaLeer);
            documentoXml.getDocumentElement().normalize();
            NodeList listaNodos = documentoXml.getElementsByTagName("concierto");

            if (listaNodos.getLength() > 0) {
                // Crear la tabla CONCIERTOS
                try (Connection connection = DriverManager.getConnection(url, usuarioAdmin, contraseñaAdmin);
                     Statement statement = connection.createStatement()) {
                    String crearTablaSQL = "CREATE TABLE IF NOT EXISTS CONCIERTOS (" +
                            "grupo VARCHAR(255), " +
                            "lugar VARCHAR(255), " +
                            "fecha VARCHAR(255), " +
                            "hora VARCHAR(255)" +
                            ");";
                    statement.executeUpdate(crearTablaSQL);
                    System.out.println("Tabla CONCIERTOS creada correctamente.");
                } catch (SQLException e) {
                    e.printStackTrace();
                }
            } else {
                System.out.println("No se han encontrado conciertos en el fichero XML");
            }

        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    private static void Ejercicio6() {
        File ficheroXmlaLeer = new File("Ficheros/CONCIERTOS.xml");
        String url = "jdbc:mysql://localhost:3306/conciertos";
        String usuarioAdmin = usuarioDAM2;
        String contraseñaAdmin = passDAM2;
        try {
            DocumentBuilderFactory ficheroXml = DocumentBuilderFactory.newInstance();
            DocumentBuilder builder = ficheroXml.newDocumentBuilder();
            Document documentoXml = builder.parse(ficheroXmlaLeer);
            documentoXml.getDocumentElement().normalize();
            NodeList listaNodos = documentoXml.getElementsByTagName("concierto");
            if (listaNodos.getLength() > 0) {
                try (Connection conexion = DriverManager.getConnection(url, usuarioAdmin, contraseñaAdmin)) {
                    String insertSQL = "INSERT INTO CONCIERTOS (grupo, lugar, fecha, hora) VALUES (?, ?, ?, ?)";
                    PreparedStatement datosCampo = conexion.prepareStatement(insertSQL);

                    // Insertar cada concierto en la base de datos
                    for (int i = 0; i < listaNodos.getLength(); i++) {
                        Element concierto = (Element) listaNodos.item(i);

                        // Extraer datos del XML
                        String grupo = concierto.getElementsByTagName("grupo").item(0).getTextContent();
                        String lugar = concierto.getElementsByTagName("lugar").item(0).getTextContent();
                        String fecha = (concierto.getElementsByTagName("fecha").item(0).getTextContent());
                        String hora = (concierto.getElementsByTagName("hora").item(0).getTextContent());

                        // Insertar los datos
                        datosCampo.setString(1, grupo);
                        datosCampo.setString(2, lugar);
                        datosCampo.setString(3, fecha);
                        datosCampo.setString(4, hora);
                        datosCampo.executeUpdate();
                    }

                    System.out.println("Conciertos insertados en la base de datos correctamente.");
                    conexion.close();
                    System.out.println("Datos insertados correctamente en la tabla CONCIERTOS.");
                } catch (SQLException e) {
                    e.printStackTrace();
                }
            } else {
                System.out.println("No se han encontrado ningun dato para insertar los conciertos");
            }

        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    private static void Ejercicio7() {
        String url = "jdbc:mysql://localhost:3306/conciertos";
        String usuario = usuarioDAM2;
        String contrasena = passDAM2;
        try (Connection conexion = DriverManager.getConnection(url, usuario, contrasena)) {
            Statement resultado = conexion.createStatement();
            String consultaSQL = "SELECT * FROM CONCIERTOS";
            ResultSet resultadoConsulta = resultado.executeQuery(consultaSQL);
            Boolean hayDato = false;
            System.out.printf("%-50s %-50s %-30s %-10s%n", "Grupo", "Lugar", "Fecha", "Hora");
            System.out.println("----------------------------------------------------------------------------------------");
            while (resultadoConsulta.next()) {
                hayDato = true;
                String grupo = resultadoConsulta.getString("grupo");
                String lugar = resultadoConsulta.getString("lugar");
                String fecha = resultadoConsulta.getString("fecha");
                String hora = resultadoConsulta.getString("hora");
                System.out.printf("%-50s %-50s %-30s %-10s%n", grupo, lugar, fecha, hora);
            }
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

public static void main(String[] args) throws SQLException, IOException, ParserConfigurationException, SAXException {
    borrarBBDDDAM2();
    //Borro el usuario DAM2 y la base de datos conciertos en caso de que existan, para que se observe como se crean
    //Y así se pueda ejecutar el resto de ejercicios
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