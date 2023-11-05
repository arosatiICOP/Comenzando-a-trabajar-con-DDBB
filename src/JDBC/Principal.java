package JDBC;

public class Principal {

	public static void main(String[] args) {

		AccesoDatos conexion = new AccesoDatos();
		conexion.abrirConexion();
		conexion.agregarPersona("Z", "Agustin", 'M', "calle1", 4550, 3000);
		conexion.mostrarPersonas();
		conexion.mostrarPersona();
		conexion.modificarPersona();
		conexion.eliminarPersona();
		conexion.cerrarConexion();
	}

}
