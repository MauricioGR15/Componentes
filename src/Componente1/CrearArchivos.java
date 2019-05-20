package Componente1;


import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.io.RandomAccessFile;

public class CrearArchivos {

	private static final String[] estados = { "Aguascalientes", "Baja California", "Baja California Sur", "Campeche", "Coahuila de Zaragoza", "Colima", "Chiapas", "Chihuahua", "Distrito Federal", 
						"Durango", "Guanajuato", "Guerrero", "Hidalgo", "Jalisco", "México", "Michoacán de Ocampo", "Morelos", "Nayarit", "Nuevo León", "Oaxaca", "Puebla",
						"Querétaro", "Quintana Roo", "San Luis Potosí", "Sinaloa", "Sonora", "Tabasco", "Tamaulipas", "Tlaxcala", "Veracruz de Ignacio de la Llave",
						"Yucatán", "Zacatecas" };
	
	private static final int[] municipiosPorEstado = new int[] { 11, 5, 5, 11, 124, 67, 16, 38, 10, 39, 46, 81, 84, 125, 125, 113, 33, 20, 51, 570, 217, 18, 11, 58, 18, 72, 17, 43, 60, 212, 106, 58 };
	private static final int ciudadesPorMunicipio = 5;
	
	private static final File fEstados = new File("guardado/JComboBoxEMC/estados.dat"),
				fMunicipios = new File("guardado/JComboBoxEMC/municipios.dat"),
				fCiudades = new File("guardado/JComboBoxEMC/ciudades.dat");
	
	public static void main(String[] args) throws IOException {
		
		// Eliminamos los archivos
		fEstados.delete();
		fMunicipios.delete();
		fCiudades.delete();
		
		// Los creamos de nuevo
		OutputStream out = new FileOutputStream(fEstados);
		out = new FileOutputStream(fMunicipios);
		out = new FileOutputStream(fCiudades);
		
		// Insertamos datos
		RandomAccessFile rafe = new RandomAccessFile(fEstados, "rw"),
						rafm = new RandomAccessFile(fMunicipios, "rw"),
						rafc = new RandomAccessFile(fCiudades, "rw");
		StringBuffer sb;
		
		int nMunicipios = 0, nCiudades = 0;
		
		for(int i = 0; i < estados.length; i++) {
			sb = new StringBuffer(estados[i]);
			sb.setLength(50);
			rafe.writeInt(i+1);
			rafe.writeChars(sb.toString());
			rafe.writeLong(rafm.length());
			
			for(int j = 0; j < municipiosPorEstado[i]; j++) {
				sb = new StringBuffer(estados[i].substring(0, 3) +"Mun" +(j+1));
				sb.setLength(50);
				rafm.writeInt(i+1);
				rafm.writeInt(++nMunicipios);
				rafm.writeChars(sb.toString());
				rafm.writeLong(rafc.length());
				
				for(int k = 0; k < ciudadesPorMunicipio; k++) {
					sb = new StringBuffer(estados[i].substring(0, 3) +"Mun" +(j+1) +"Ciu" +(k+1));
					sb.setLength(50);
					
					rafc.writeInt(i+1);
					rafc.writeInt(nMunicipios);
					rafc.writeInt(++nCiudades);
					rafc.writeChars(sb.toString());
				}
			}
		}
		
		rafe.close();
		rafm.close();
		rafc.close();
		System.out.println("Archivos creados");
	}
}
