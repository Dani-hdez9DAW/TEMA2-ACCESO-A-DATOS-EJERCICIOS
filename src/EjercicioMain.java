import org.w3c.dom.Document;
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

    private static void procesarOpcion(int opcion) throws SQLException, IOException {
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
        String url = "jdbc:mysql://localhost:3306/";
        String adminUser = "root";
        String adminPass = "";
        try (Connection conexion = DriverManager.getConnection(url, adminUser, adminPass)) {
            System.out.println("Administrador conectado");
            conexion.close();
        } catch (Exception e) {
            System.out.println("Error al conectar con la base de datos");
        }
    }

    private static void Ejercicio2() {
        String url = "jdbc:mysql://localhost:3306/";
        String adminUser = "root";
        String adminPass = "";
        try (Connection conexion = DriverManager.getConnection(url, adminUser, adminPass)) {
            Statement resultado = conexion.createStatement();
            String createUsuarioDAM = "CREATE USER 'DAM2'@'localhost' IDENTIFIED BY 'DAM2'";
            resultado.executeUpdate(createUsuarioDAM);
            String permisosUsuarioDAM = "GRANT ALL PRIVILEGES ON *.* TO 'DAM2'@'localhost'";
            resultado.executeUpdate(permisosUsuarioDAM);
            conexion.close();
        } catch (Exception e) {
            System.out.println("Error al conectar con la base de datos");
        }
    }

    private static void Ejercicio3() throws SQLException {
        conectarBBDD("DAM2", "DAM2");
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

    private static void Ejercicio5() throws IOException {
        File ficheroXmlaLeer = new File("Ficheros/CONCIERTOS.xml");
        String url = "jdbc:mysql://localhost:3306/";
        String usuarioAdmin = "root";
        String contraseñaAdmin = "";

        try {

            try (Connection connection = DriverManager.getConnection(url, usuarioAdmin, contraseñaAdmin);
                 Statement statement = connection.createStatement()) {
                // Crear la base de datos si no existe
                String crearBaseDatosSQL = "CREATE DATABASE IF NOT EXISTS conciertos";
                statement.executeUpdate(crearBaseDatosSQL);
                System.out.println("Base de datos 'conciertos' creada o ya existe.");
                connection.close();
            } catch (SQLException e) {
                e.printStackTrace();
                return;
            }

            // Actualizar la URL para conectarse a la base de datos 'conciertos'
            url = "jdbc:mysql://localhost:3306/conciertos";

            DocumentBuilderFactory ficheroXml = DocumentBuilderFactory.newInstance();
            DocumentBuilder builder = ficheroXml.newDocumentBuilder();
            Document documentoXml = builder.parse(ficheroXmlaLeer);
            documentoXml.getDocumentElement().normalize();
            NodeList listaNodos = documentoXml.getElementsByTagName("concierto");
            if (listaNodos.getLength() > 0) {
                NodeList nodosHijos = listaNodos.item(0).getChildNodes();
                StringBuilder crearTablaSQL = new StringBuilder("CREATE TABLE CONCIERTOS (");
                for (int i = 0; i < nodosHijos.getLength(); i++) {
                    if (nodosHijos.item(i).getNodeType() == org.w3c.dom.Node.ELEMENT_NODE) {
                        String nombreColumna = nodosHijos.item(i).getNodeName();
                        crearTablaSQL.append(nombreColumna).append(" VARCHAR(255), ");
                    }
                }
                crearTablaSQL.setLength(crearTablaSQL.length() - 2);
                crearTablaSQL.append(");");

                try (Connection connection = DriverManager.getConnection(url, usuarioAdmin, contraseñaAdmin);
                     Statement statement = connection.createStatement()) {
                    statement.executeUpdate(crearTablaSQL.toString());
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
        String usuarioAdmin = "root";
        String contraseñaAdmin = "";
        try {
            DocumentBuilderFactory ficheroXml = DocumentBuilderFactory.newInstance();
            DocumentBuilder builder = ficheroXml.newDocumentBuilder();
            Document documentoXml = builder.parse(ficheroXmlaLeer);
            documentoXml.getDocumentElement().normalize();
            NodeList listaNodos = documentoXml.getElementsByTagName("concierto");
            if (listaNodos.getLength() > 0) {
                try (Connection conexion = DriverManager.getConnection(url, usuarioAdmin, contraseñaAdmin);
                     Statement resultado = conexion.createStatement()) {
                    for (int i = 0; i < listaNodos.getLength(); i++) {
                        NodeList nodosHijosConcierto = listaNodos.item(i).getChildNodes();
                        StringBuilder columnas = new StringBuilder();
                        StringBuilder valores = new StringBuilder();
                        for (int j = 0; j < nodosHijosConcierto.getLength(); j++) {
                            if (nodosHijosConcierto.item(j).getNodeType() == org.w3c.dom.Node.ELEMENT_NODE) {
                                String nombreColumna = nodosHijosConcierto.item(j).getNodeName();
                                String valorColumna = nodosHijosConcierto.item(j).getTextContent();
                                columnas.append(nombreColumna).append(", ");
                                valores.append("'").append(valorColumna).append("', ");
                            }
                        }
                        columnas.setLength(columnas.length() - 2);
                        valores.setLength(valores.length() - 2);
                        String insertarDatosSQL = "INSERT INTO CONCIERTOS (" + columnas + ") VALUES (" + valores + ")";
                        resultado.executeUpdate(insertarDatosSQL);
                    }
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
        String usuario = "root";
        String contrasena = "";
        try (Connection conexion = DriverManager.getConnection(url, usuario, contrasena)) {
            Statement resultado = conexion.createStatement();
            String consultaSQL = "SELECT * FROM CONCIERTOS";
            ResultSet resultadoConsulta = resultado.executeQuery(consultaSQL);
            Boolean hayDato = false;
            System.out.printf("%-30s %-50s %-20s %-10s%n", "Grupo", "Lugar", "Fecha", "Hora");
            System.out.println("----------------------------------------------------------------------------------------");
            while (resultadoConsulta.next()) {
                hayDato = true;
                String grupo = resultadoConsulta.getString("grupo");
                String lugar = resultadoConsulta.getString("lugar");
                String fecha = resultadoConsulta.getString("fecha");
                String hora = resultadoConsulta.getString("hora");
                System.out.printf("%-25s %-40s %-25s %-10s%n", grupo, lugar, fecha, hora);
            }
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    public static void main(String[] args) throws SQLException, IOException {
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