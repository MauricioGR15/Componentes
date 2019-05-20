package Componente1;

import java.awt.Color;
import java.awt.GridLayout;
import java.awt.event.ItemEvent;
import java.awt.event.ItemListener;
import java.io.RandomAccessFile;

import javax.swing.BorderFactory;
import javax.swing.JComboBox;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.border.TitledBorder;

/**
 * @author Ricardo Wong & Mauricio García
 * Este componente nos servirá para un ComboBox dinámico de los estados,
 * municipios y ciudades de cualquier país.
 * País por defecto: México
 */

public class JComboBoxEMC extends JPanel implements ItemListener {

	private static final long serialVersionUID = 1L;

	private class IndexLugar {
		int id;
		long posicion;
		String nombre;

		public IndexLugar(int id, long posicion, String nombre) {
			this.id = id;
			this.posicion = posicion;
			this.nombre = nombre;
		}

		public String toString() {
			return nombre;
		}
	}

	private static final String DIR_FILES = "JComboBoxEMC/";
	private static final String PAIS_DEFECTO = "Mexico";
	public static final int HORIZONTAL = 0, VERTICAL = 1;
	public static final int ESTADO = 0, MUNICIPIO = 1, CIUDAD = 2;

	private final int LONG_ESTADO = 112, LONG_MUNICIPIO = 116, LONG_CIUDAD = 112;
	private JComboBox<IndexLugar> cbEstados, cbMunicipios, cbCiudades;
	private JLabel jlEstados, jlMunicipios, jlCiudades;
	private String pais;


	/**
	 * El componente JComboBoxEMC es un panel que contiene tres etiquetas y tres combos, cada uno respectivamente
	 * con el tipo de información que contiene (Estado, Municipios, Ciudades). El combo Estado es el padre y de éste 
	 * dependerá la información desplegada en el combo Municipios, el combo Ciudades es dependiente del combo Municipios.<br>
	 * 
	 * Constructor por defecto del componente JComboBoxEMC. Se crea por defecto sin ningún Item seleccionado 
	 * en el combo padre Estado. La alineación por defecto es Horizontal.
	 */
	public JComboBoxEMC() {
		this(PAIS_DEFECTO, "", "", 0);
	}


	/**
	 * Constructor sobrecargado para crear el componente con una alineación deseada, ya sea horizontal o vertical.
	 * @param alineacion - Constante para alinear los combos en horizontal o vertical. Las constantes son las siguientes
	 *  <i>HORIZONTAL</i>, <i>VERTICAL</i>.
	 */
	public JComboBoxEMC(int alineacion) {
		this("", "", alineacion);
	}

	/**
	 * Constructor sobrecargado para crear el componente con un Estado seleccionado en el combo Estado.
	 * @param estado - Estado para mostrarse como seleccionado en el combo Estado.
	 */
	public JComboBoxEMC(String estado) {
		this(PAIS_DEFECTO, estado, "", 0);
	}

	/**
	 * 
	 * Constructor sobrecargado para crear el componente con un Estado seleccionado en el combo Estado y también 
	 * para que el combo Municipios tenga una Municipio seleccionado.<br>
	 * <p>
	 * <b>Preacaución</b>: Se debe seleccionar un municipio válido que exista para el Estado.
	 * 
	 * @param estado - Estado para mostrarse como seleccionado en el combo Estado.
	 * @param municipio - Municipio para mostrarse como seleccionado en el combo Municipio.
	 */
	public JComboBoxEMC(String estado, String municipio) {
		this(PAIS_DEFECTO, estado, municipio, 0);
	}


	/**
	 * Constructor sobrecargado para crear el componente con un Estado seleccionado en el combo Estado, también 
	 * para que el combo Municipios tenga un Municipio seleccionado y el componente tenga una alineación deseada ya sea horizontal o vertical.
	 * 
	 * <p><b>Precaución:</b> Se debe seleccionar un municipio válido que exista para el el Estado.
	 * 
	 * @param estado - Estado para mostrarse como seleccionado en el combo Estado.
	 * @param municipio - Municipio para mostrarse como seleccionado en el combo Municipio.
	 * @param alineacion - Constante para alinear los combos en horizontal o vertical. Las constante son las siguientes
	 * <i>HORIZONTAL</i>, <i>VERTICAL</i>.
	 */
	public JComboBoxEMC(String estado, String municipio, int alineacion) {
		this(PAIS_DEFECTO, estado, municipio, alineacion);
	}

	/**
	 * Constructor sobrecargado más general con las opciones de crear el componente con un el país escogido para ser utilizado en el
	 * componente, Estado seleccionado en el combo Estado, un Municipio seleccionado en el combo Municipio y una alineación para 
	 * mostrar los combos y etiquetas.
	 * 
	 * <p><b>Precaución:</b> Se debe seleccionar un municipio válido que exista para el el Estado.
	 * 
	 * @param pais - Pais hace referencia al país que será utilizado en el componente.
	 * @param estado - Estado para mostrarse como seleccionado en el combo Estado.
	 * @param municipio - Municipio para mostrarse como seleccionado en el combo Municipio.
	 * @param alineacion - Constante para alinear los combos en horizontal o vertical. Las constante son las siguientes
	 * <i>HORIZONTAL</i>, <i>VERTICAL</i>.
	 */
	public JComboBoxEMC(String pais, String estado, String municipio, int alineacion) {
		this.pais = pais;
		if(alineacion == HORIZONTAL)
			setLayout(new GridLayout(1, 6));
		else 
			setLayout(new GridLayout(3, 2));

		setBorder(BorderFactory.createLineBorder(Color.GRAY, 1));
		add(jlEstados = new JLabel("Estados: ", JLabel.CENTER));
		add(cbEstados = new JComboBox<IndexLugar>());
		add(jlMunicipios = new JLabel("Municipios: ", JLabel.CENTER));
		add(cbMunicipios = new JComboBox<IndexLugar>());
		add(jlCiudades = new JLabel("Ciudades: ", JLabel.CENTER));
		add(cbCiudades = new JComboBox<IndexLugar>());

		cbEstados.addItemListener(this);
		cbMunicipios.addItemListener(this);
		cbCiudades.addItemListener(this);

		llenarEstados();
		seleccionarDefectos(estado, municipio);

	}

	/**
	 * Cambia el pais de los cuales se quiere seleccionar sus localidades.
	 * Este método simplemente cambia la ruta sobre la cual trabajaran los archivos .dat.
	 * @param pais - Nueva ruta sobre en la cual trabajarán los archivos .dat.
	 * 
	 */
	public void setPais(String pais) {
		this.pais = pais;
		cbEstados.removeAllItems();
		cbMunicipios.removeAllItems();
		cbCiudades.removeAllItems();
		llenarEstados();
	}

	private void seleccionarDefectos(String estado, String municipio) {
		if(estado.isEmpty()) 
			return;

		int nEstados = cbEstados.getItemCount();
		for(int i = 0; i < nEstados; i++) {
			if(cbEstados.getItemAt(i).nombre.equalsIgnoreCase(estado)) {
				cbEstados.setSelectedIndex(i);
				cbEstados.setEnabled(false);
				break;
			}
		}

		int nMunicipios = cbMunicipios.getItemCount();
		if(municipio.isEmpty() || nMunicipios == 0) 
			return;

		for(int i = 0; i < nMunicipios; i++) {
			if(cbMunicipios.getItemAt(i).nombre.equalsIgnoreCase(municipio)) {
				cbMunicipios.setSelectedIndex(i);
				cbMunicipios.setEnabled(false);
				break;
			}
		}
	}

	/**
	 * Retorna el toString() del objeto actual seleccionado del JComboBox.
	 * Se usan las constantes:
	 * <p>
	 * <i><b>ESTADO:</i></b> Obtiene el Item seleccionado del combo Estado.<br>
	 * <i><b>MUNICIPIO:</i></b> Obtiene el Item seleccionado del combo Municipio.<br>
	 * <i><b>CIUDAD:</i></b> Obtiene el Item seleccionado del combo Ciudad.<br>
	 * 
	 * @param lugar - La constante respectiva del combo del cual se va a obtener el toString() del Item seleccionado.
	 * 
	 * @return Retorna el toString() del objeto seleccionado.
	 */
	public String getSelected(int lugar) {
		IndexLugar il = null;
		switch(lugar) {
		case ESTADO: 
			il = (IndexLugar) cbEstados.getSelectedItem();
			break;
		case MUNICIPIO: 
			il = (IndexLugar) cbMunicipios.getSelectedItem();
			break;
		case CIUDAD:
			il=(IndexLugar) cbCiudades.getSelectedItem();
			break;
		}

		if(il != null && il.id !=0)
			return il.nombre;

		return null;
	}

	/**
	 *  Este método retorna un componente JLabel. 
	 *  Se tienen que usar las constantes:
	 * <p>
	 * <i><b>ESTADO:</i></b> Obtiene el Item seleccionado del combo Estado.
	 * <i><b>MUNICIPIO:</i></b> Obtiene el Item seleccionado del combo Municipio.
	 * <i><b>CIUDAD:</i></b> Obtiene el Item seleccionado del combo Ciudad.
	 * 
	 * @param lugar - La constante respectiva del combo del cual se va a obtener el JLabel de
	 * 
	 * @param lugar - La constante respectiva del lugar.
	 * @return Retorna el componente al que se haya hecho referencia con la constante
	 */
	public JLabel getJLabelComponent(int lugar) {
		switch(lugar) {
		case ESTADO: return jlEstados;
		case MUNICIPIO: return jlMunicipios;
		case CIUDAD: return jlCiudades;
		}

		return null;
	}

	
	/**
	 * Método que retorna el combo
	 * @param lugar - La constante de la clase JComboBoxEMC respectiva para indicar el lugar 
	 * @return Retorna JComboBox dependiendo del lugar que se recibio como parámetro
	 */
	public JComboBox<IndexLugar> getJComboBoxComponent(int lugar) {
		switch(lugar) {
		case ESTADO: return cbEstados;
		case MUNICIPIO: return cbMunicipios;
		case CIUDAD: return cbCiudades;
		}

		return null;
	}

	private void llenarEstados()  {
		try {
			RandomAccessFile estadosFile = new RandomAccessFile(DIR_FILES +pais +"/estados.dat", "r");
			int nEstados = (int)((estadosFile.length())/LONG_ESTADO);
			char[] temp;
			int id;
			String estado;
			IndexLugar il;

			cbEstados.addItem(new IndexLugar(0, 0, "Seleccione: "));
			for(int i = 0; i < nEstados; i++) {
				id = estadosFile.readInt();
				temp = new char[50];
				for (int j = 0; j < temp.length; j++)
					temp[j] = estadosFile.readChar();
				estado = new String(temp);

				il = new IndexLugar(id, estadosFile.readLong(), estado.trim());
				cbEstados.addItem(il);
			}

			estadosFile.close();
		} catch(Exception e) {
			e.printStackTrace();
		}
	}

	private void llenarMunicipios() {
		IndexLugar estado = ((IndexLugar)cbEstados.getSelectedItem());

		try {
			RandomAccessFile municipiosFile = new RandomAccessFile(DIR_FILES +pais +"/municipios.dat", "r");
			int nMunicipios = (int)((municipiosFile.length()-estado.posicion)/LONG_MUNICIPIO);
			municipiosFile.seek(estado.posicion);

			char[] temp;
			int id;
			String municipio;
			IndexLugar il;

			cbMunicipios.addItem(new IndexLugar(0, 0, "Seleccione: "));
			for(int i = 0; i < nMunicipios; i++) {
				if(estado.id != municipiosFile.readInt())
					break;

				id = municipiosFile.readInt();
				temp = new char[50];
				for (int j = 0; j < temp.length; j++)
					temp[j] = municipiosFile.readChar();
				municipio = new String(temp);

				il = new IndexLugar(id, municipiosFile.readLong(), municipio.trim());
				cbMunicipios.addItem(il);
			}

			municipiosFile.close();
		} catch(Exception e) {
			e.printStackTrace();
		}
	}

	private void llenarCiudades() {
		IndexLugar municipio = ((IndexLugar)cbMunicipios.getSelectedItem()); 

		try {
			RandomAccessFile ciudadesFile = new RandomAccessFile(DIR_FILES +pais +"/ciudades.dat", "r");
			int nCiudades = (int)((ciudadesFile.length()-municipio.posicion)/LONG_CIUDAD);
			ciudadesFile.seek(municipio.posicion);

			char[] temp;
			int id;
			String ciudad;

			cbCiudades.addItem(new IndexLugar(0, 0, "Seleccione: "));
			for(int i = 0; i < nCiudades; i++) {
				ciudadesFile.readInt(); // ID del estado que no ocupamos verificar
				if(municipio.id != ciudadesFile.readInt())
					break;

				id = ciudadesFile.readInt();
				temp = new char[50];
				for (int j = 0; j < temp.length; j++)
					temp[j] = ciudadesFile.readChar();
				ciudad = new String(temp);

				cbCiudades.addItem(new IndexLugar(id, 0, ciudad.trim()));
			}

			ciudadesFile.close();
		} catch(Exception e) {
			e.printStackTrace();
		}
	}

	public void itemStateChanged(ItemEvent evt) {
		if(evt.getStateChange() != ItemEvent.SELECTED)
			return;

		JComboBox<IndexLugar> cb = (JComboBox<IndexLugar>) evt.getSource();
		IndexLugar il = (IndexLugar)cb.getSelectedItem();

		if(cb == cbEstados) {
			cbCiudades.removeAllItems();
			cbMunicipios.removeAllItems();
			if(il.id > 0) 
				llenarMunicipios();
		}
		else if(cb == cbMunicipios) {
			cbCiudades.removeAllItems();
			if(il.id > 0) 
				llenarCiudades();
		}
	}
}