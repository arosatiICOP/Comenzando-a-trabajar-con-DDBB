package JDBC;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.Scanner;

public class AccesoDatos {

	Connection con;
	Statement st;
	ResultSet rs;
	
	public void abrirConexion() {	
	try {
		String userName="";
		String password="";
		String url="jdbc:postgresql://localhost/datosPersona";
			
		con = DriverManager.getConnection(url, userName, password);
		System.out.println("Conexión a la BD");
		
		}catch (Exception e) {
			System.out.println("Error en conexión ");
			System.out.println(e.getMessage());
		}
	}
	
	public void cerrarConexion() {	
	try {
		con.close();
		System.out.println("Conexión cerrada");
		}catch (SQLException e) {
			System.out.println("Error al cerrar conexión");
		}
	}
	
	public void mostrarPersonas() {
	    try {
	        st = con.createStatement();
	        rs = st.executeQuery("SELECT * FROM Persona");
	        System.out.println("Personas registradas en la Base de Datos:\n");
	        
	        while (rs.next()) {
	            int idPersona = rs.getInt("idPersona");
	            String apellido = rs.getString("apellido");
	            String nombre = rs.getString("nombre");
	            char genero = rs.getString("genero").charAt(0);
	            int idDomicilio = rs.getInt("idDomicilio");

	            System.out.println("ID: " + idPersona);
	            System.out.println("Apellido: " + apellido);
	            System.out.println("Nombre: " + nombre);
	            System.out.println("Género: " + genero);
	            System.out.println("ID de Domicilio: " + idDomicilio);
	            System.out.println("-------------------------------------");
	        }
	    } catch (SQLException e) {
	        e.printStackTrace();
	    }
	}
	
	public void mostrarPersona() {
		
		Scanner ingresoDatos = new Scanner(System.in);
	    System.out.println("Ingrese el ID de la persona que desea modificar:");
	    int idIngresado = ingresoDatos.nextInt();
	    ingresoDatos.nextLine();
		
	    try {
	        st = con.createStatement();
	        rs = st.executeQuery("SELECT * FROM Persona WHERE idPersona = ?");
	        System.out.println("Persona registrada en la Base de Datos con el id "+ idIngresado +":\n");
	        
	        while (rs.next()) {
	            int idPersona = rs.getInt("idPersona");
	            String apellido = rs.getString("apellido");
	            String nombre = rs.getString("nombre");
	            char genero = rs.getString("genero").charAt(0);
	            int idDomicilio = rs.getInt("idDomicilio");

	            System.out.println("ID: " + idPersona);
	            System.out.println("Apellido: " + apellido);
	            System.out.println("Nombre: " + nombre);
	            System.out.println("Género: " + genero);
	            System.out.println("ID de Domicilio: " + idDomicilio);
	            System.out.println("-------------------------------------");
	        }
	    } catch (SQLException e) {
	        e.printStackTrace();
	    }
	    ingresoDatos.close();;
	}

	
	 public void agregarPersona(String apellido, String nombre, char genero, String calle, int numero, int codigoPostal) {
	        
		 try {
			 String sql = "INSERT INTO Persona (apellido, nombre, genero, idDomicilio) " +
			              "VALUES (?, ?, ?, (SELECT idDomicilio FROM Domicilio WHERE calle = ? AND numero = ? AND codigo_postal = ? LIMIT 1))";

	            PreparedStatement preparedStatement = con.prepareStatement(sql);
	            preparedStatement.setString(1, apellido);
	            preparedStatement.setString(2, nombre);
	            preparedStatement.setString(3, String.valueOf(genero));
	            preparedStatement.setString(4, calle);
	            preparedStatement.setInt(5, numero);
	            preparedStatement.setInt(6, codigoPostal);

	            preparedStatement.executeUpdate();
	        } catch (SQLException e) {
	            e.printStackTrace();
	        } finally {
	            try {
	                if (con != null) {
	                    con.close();
	                }
	            } catch (SQLException e) {
	                e.printStackTrace();
	            }
	        }
	    }
	
	public void modificarPersona() {
		
	    Scanner ingresoDatos = new Scanner(System.in);
	    System.out.println("Ingrese el ID de la persona que desea modificar:");
	    int idIngresado = ingresoDatos.nextInt();
	    ingresoDatos.nextLine();

	    System.out.println("Ingrese el dato que desea modificar. Opciones: Nombre, Apellido, Genero:");
	    String datoIngresado = ingresoDatos.nextLine().toLowerCase().replaceAll("[0-9\\p{Punct}]", "");

	    System.out.println("Ingrese el nuevo dato:");
	    String datoNuevo = ingresoDatos.nextLine().replaceAll("[0-9\\p{Punct}]", "");

	    String modifSQL = "UPDATE Persona SET " + datoIngresado + " = ? WHERE idPersona = ?";

	    try (PreparedStatement stModif = con.prepareStatement(modifSQL)) {
	        if (datoIngresado.equals("nombre") || datoIngresado.equals("apellido") || datoIngresado.equals("genero")) {
	            stModif.setString(1, datoNuevo);
	            stModif.setInt(2, idIngresado);

	            boolean resultado = true;
	            if (resultado) {
	            	stModif.executeUpdate();
	                System.out.println("Persona modificada exitosamente.");
	            } else {
	                System.out.println("No se encontró ninguna persona con el ID proporcionado.");
	            }
	        } else {
	            System.out.println("Dato a modificar no válido.");
	        }
	    } catch (SQLException e) {
	        e.printStackTrace();
	    }
	    ingresoDatos.close();
	}
	
	public void eliminarPersona() {
		
		Scanner ingresoDatos = new Scanner(System.in);
	    System.out.println("Ingrese el ID de la persona que desea eliminar:");
	    int idIngresado = ingresoDatos.nextInt();
	    ingresoDatos.nextLine();

	    String deleteSQL = "DELETE FROM Persona WHERE idPersona = ?";
	    try (PreparedStatement stDelete = con.prepareStatement(deleteSQL)) {
	        stDelete.setInt(1, idIngresado);
	        int filasAfectadas = stDelete.executeUpdate();
	        if (filasAfectadas > 0) {
	            System.out.println("Persona eliminada exitosamente.");
	        } else {
	            System.out.println("No se encontró ninguna persona con el ID proporcionado.");
	        }
	        ingresoDatos.close();
	    } catch (SQLException e) {
	        e.printStackTrace();
	    }
	    ingresoDatos.close();
	}	
}