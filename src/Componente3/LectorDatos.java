package Componente3;
import javax.swing.*;
import javax.swing.border.Border;
import javax.swing.border.TitledBorder;

import java.awt.*;
import java.awt.event.*;
import java.util.ArrayList;
import java.util.regex.Pattern;

/**
 * @author Ricardo Wong & Mauricio García
 * Este componente nos da la opción de crear un panel para poder registrar una lista ya sea de
 * RFC, Correos o Teléfonos.
 */

public class LectorDatos extends JPanel implements ActionListener,KeyListener,FocusListener{
	private static final int RFC = 0, CORREO = 1, TELEFONO = 2;
	private static final Border ERROR_BORDER = BorderFactory.createLineBorder(Color.RED);
	private static final Border DEFAULT_BORDER = BorderFactory.createLineBorder(Color.BLACK);
	private static final Border FOCUS_BORDER = BorderFactory.createLineBorder(Color.BLACK, 2);
	private static final String[] EXPRESIONES = { "[^[A-Z]{4}[\\d]{6}[A-Z0-9]{3}$]|[^[A-Z]{3}[\\d]{6}[A-Z0-9]{3}$]",
			"^[a-zA-z0-9_\\-\\.]{4,30}@[a-z]{2,15}\\.[a-zA-Z]{2,4}$",
			"^[\\d]{3}\\-[\\d]{7}$" }; // ^667[0-9]{7}$ Lada Culiacan.
	
	private JRadioButton radioRFC,radioCorreo,radioTelefono;
	private JButton btnHacer;
	private JPanel panel;
	private ArrayList<TextFields> listaTextFields;
	private JScrollPane scroll;

	private int maxFields, textFieldWidth;
	private byte expresion;
	private JLabel aviso;
	private boolean bloqueado;
	
	
	/**
	 * Constructor por defecto del componente LectorDatos. El componente LectorDatos es un panel que contiene
	 * tres criterios (RFC, Correo, Télefono), un botón que permite agregar nuevas cajas. Cada caja tiene que ser 
	 * llenada correctamente para la creación de una nueva. Todas las cajas de texto se pueden eliminar 
	 * en cualquier momento.
	 */
	public LectorDatos() {
		this(5);
	}
	
	/**
	 * Constructor sobrecargado del componente LectorDatos. Pone un límite máximo de cajas en el JScrollPane para 
	 * mostrar cuando se van agregando los campos de texto. 
	 * @param maxFields - Un entero que limita el número de cajas máximo para mostrarse en el JScrollPane. 
	 */
	public LectorDatos(int maxFields) {
		this.maxFields = maxFields;
		hazInterfaz();
		hazEscuchadores();
		expresion = CORREO;
		listaTextFields = new ArrayList<TextFields>();
		setBorder(BorderFactory.createTitledBorder(BorderFactory.createEtchedBorder(), "Captura", TitledBorder.CENTER, TitledBorder.TOP));
	}
	
	private void hazEscuchadores() {
		btnHacer.addActionListener(this);
		radioRFC.addActionListener(this);
		radioCorreo.addActionListener(this);
		radioTelefono.addActionListener(this);
	}
	
	private void agregaEscuchadores(TextFields tf) {
		tf.getCampoTexto().addKeyListener(this);
		tf.getCampoTexto().addFocusListener(this);
		tf.getBotonElimina().addActionListener(this);
	}
	
	private void hazInterfaz() {
		ButtonGroup grupo = new ButtonGroup();
		grupo.add(radioRFC = new JRadioButton("RFC"));
		grupo.add(radioCorreo = new JRadioButton("Correo",true));
		grupo.add(radioTelefono = new JRadioButton("Teléfono"));
		btnHacer = new JButton("Agregar");
		
		setLayout(new GridBagLayout());
		GridBagConstraints c = new GridBagConstraints();
		
		c.gridwidth = 1; c.weighty = 1; c.weightx = 2;
		c.gridy = 0; c.gridx = 1;  c.anchor = GridBagConstraints.NORTH;
		add(radioRFC, c);
		c.gridx = 2; 
		add(radioCorreo, c);
		c.gridx = 3; 
		add(radioTelefono, c);
		c.weighty = 0; c.gridy=1; c.weighty = 1; c.gridx = 2;
		add(btnHacer,c);
		c.gridy = 2;
		add(aviso = new JLabel(), c);
		c.weighty = 10; c.gridx = 1; c.gridwidth=3; c.gridy = 3; c.fill = GridBagConstraints.BOTH;

		scroll = new JScrollPane(panel = new JPanel(new GridLayout(0, 1, 1, 1)));
		scroll.setVerticalScrollBarPolicy(ScrollPaneConstants.VERTICAL_SCROLLBAR_AS_NEEDED);
		scroll.setHorizontalScrollBarPolicy(ScrollPaneConstants.HORIZONTAL_SCROLLBAR_AS_NEEDED);
		add(scroll,c);
	}
	
	/**
	 * Se obtiene la cantidad de elementos (cajas de texto) que hay en el panel.
	 * 
	 * @return Un entero que nos dice la cantidad de cajas de texto en el panel.
	 */
	public int getCount() {
		return listaTextFields.size();
	}
	
	/**
	 * Retorna un elemento de la lista.
	 * 
	 * @param indice - La posición específica en la lista.
	 * @return Retorna un String del elemento de la lista.
	 */
	public String getElemento(int indice) {
		return listaTextFields.get(indice).getCampoTexto().getText();
	}
	
	/**
	 * Retorna la lista de elementos registrados en los campos de texto con respecto al criterio seleccionado.
	 * No consigue la lista forzadamente. Si hay algún error o un campo vacío, retorna una lista null.
	 * 
	 * <p><b>Precaución:</b> Si se usa cuando hay campos vacíos o incorrectos el retorno será null
	 * 
	 * @return Retorna un ArrayList de String con los registros en los campos de texto con respecto 
	 * el criterio seleccionado.
	 */
	public ArrayList<String> getLista() {
		return getLista(false);
	}
	
	
	/**
	 * Retorna la lista de elementos registrados en los campos de texto con respecto al criterio seleccionado.
	 * 
	 * 
	 * @param forzado - Parámetro booleano para el retorno de la lista. Con true la lista a retornar puede 
	 * tener campos vacíos o incorrectos y regresa la lista forzada. Con false el retorno de la lista será 
	 * sin ser forzado y si hay campos vacíos o incorrectos retorna una lista null.
	 * @return Retorna un ArrayList de String con los registros  en los campos de texto con respecto el criterio seleccionado.
	 * Retorna una lista null en caso de que sea no forzada o esten todos los campos vacíos.
	 */
	public ArrayList<String> getLista(boolean forzado) {
		if(!forzado && checarCajas()) 
			return null;
		
		return generarLista();
	}
	
	private ArrayList<String> generarLista() {
		ArrayList<String> list = new ArrayList<String>();
		String texto;
		
		for(TextFields tf: listaTextFields) {
			texto = tf.getCampoTexto().getText();
			if(texto.isEmpty() || !verificaExpresion(texto))
				continue;
			
			list.add(texto);
		}

		return list;
	}
	
	/**
	 * Método que retorna un entero con respecto al criterio seleccionado.
	 * <p>Los criterios:
	 * <br>RFC = 0
	 * <br>Correo = 1
	 * <br>Teléfono = 2</p>
	 * 
	 * @return Retorna un entero con respecto al criterio seleccionado.
	 */
	public int getTipo() {
		return expresion;
	}
	
	private void metRadios(ActionEvent evt) {
		if(evt.getSource() == radioRFC) {
			expresion = RFC;
			return;
		}
		if(evt.getSource() == radioCorreo) {
			expresion = CORREO;
			return;
		}
		if(evt.getSource() == radioTelefono) {
			expresion = TELEFONO;
			return;
		}
	}
	
	private void activarRadios(boolean x) {
		radioRFC.setEnabled(x);
		radioCorreo.setEnabled(x);
		radioTelefono.setEnabled(x);
	}
	
	/**
	 * El método recibe una cadena como parámetro para buscar si coincide con la expresión regular que está evaluando.
	 * La evaluación es con respecto al criterio seleccionado en el componente.
	 *  
	 * @param cadena - El parámetro cadena es el texto del cual se quiere verificar si tiene coincidencia con la 
	 * expresión regular.
	 * @return Retorna true si la coincidencia existe. Retorna false si no hay coincidencia.
	 */
	public boolean verificaExpresion(String cadena) {
		Pattern patron = Pattern.compile(EXPRESIONES[expresion]);
		return patron.matcher(cadena).find();
	}
	
	private boolean checarCajas() {
		JTextField textField;
		boolean band = false;
		
		for(TextFields tf: listaTextFields) {
			textField = tf.getCampoTexto();
			if(verificaExpresion(textField.getText()))
				continue;
			
			textField.setBorder(ERROR_BORDER);
			band = true;
		}

		aviso.setText(band ? "<html><font color='red'> Algunas cajas estan incorrectas" : "");
		return band;
	}

	public void keyTyped(KeyEvent evt) {
		JTextField aux = (JTextField) evt.getSource();
		Character car = evt.getKeyChar();
		if(car == KeyEvent.VK_ENTER)
			return;
		
		if(expresion == TELEFONO) {
			if(aux.getText().length() == 11){
				evt.consume();
				Toolkit.getDefaultToolkit().beep();
				return;
			}

			if(!Character.isDigit(car) && car != '-')
				evt.consume();
		}
		else if(expresion == RFC) {
			if(aux.getText().length() == 13){
				evt.consume();
				Toolkit.getDefaultToolkit().beep();
				return;
			}

			if(!Character.isLetterOrDigit(car))
				evt.consume();
		}
		else {
			if(!Character.isLetterOrDigit(car) && "_-.@".indexOf(car) < 0)
				evt.consume();
		}
	}
	
	public void keyPressed(KeyEvent evt) {
		if(evt.getKeyChar() == KeyEvent.VK_ENTER) {
			JTextField aux = (JTextField) evt.getSource();
			if(listaTextFields.get(listaTextFields.size()-1).getCampoTexto() == aux)
				btnHacer.doClick();
			else {
				aux.transferFocus();
				TextFields tfAux = (TextFields)aux.getParent();
				tfAux.getBotonElimina().transferFocus();
			}
		}
	}
	
	public void keyReleased(KeyEvent evt) {
		JTextField aux = (JTextField) evt.getSource();
		String cad = aux.getText();
		if(expresion == RFC) 
			aux.setText(cad.toUpperCase());
	}

	public void actionPerformed(ActionEvent evt) {
		if(evt.getSource() instanceof JRadioButton) {
			metRadios(evt);
			return;
		}
		
		if(evt.getSource() == btnHacer) {
			if(checarCajas())
				return;

			if(!bloqueado) {
				activarRadios(false);
				bloqueado = true;
				textFieldWidth = panel.getWidth()/20;
			}
			
			TextFields aux = new TextFields();
			listaTextFields.add(aux);
			agregaEscuchadores(aux);
			panel.add(aux);
			
			if(listaTextFields.size() > maxFields)
				scroll.setPreferredSize(new Dimension((int)scroll.getSize().getWidth(), (int)(aux.getPreferredSize().getHeight()*maxFields)));
			
			aux.getCampoTexto().requestFocus();
			updateUI();
			return;
		}
		
		if(evt.getSource() instanceof JButton) {
			JButton auxBtn = (JButton) evt.getSource();
			TextFields auxTF = (TextFields) auxBtn.getParent();
			listaTextFields.remove(auxTF);
			panel.remove(auxTF);
			panel.updateUI();
			checarCajas();
			
			if(listaTextFields.size() == 0) {
				activarRadios(true);
				bloqueado = false;
			}
		}
	}
	
	public void focusGained(FocusEvent evt) {
		JTextField aux = (JTextField) evt.getSource();
		if(aux.getBorder() != ERROR_BORDER)
			aux.setBorder(FOCUS_BORDER);
	}

	public void focusLost(FocusEvent evt) {
		JTextField aux = (JTextField) evt.getSource();
		if(aux.getBorder() == ERROR_BORDER && !verificaExpresion(aux.getText()))
			return;
			
		aux.setBorder(DEFAULT_BORDER);
	}
	
	private class TextFields extends JPanel{
		private static final long serialVersionUID = 1L;
		private JTextField campoTexto;
		private JButton botonElimina;
		
		public TextFields() {
			add(campoTexto = new JTextField(textFieldWidth));
			add(botonElimina = new JButton("X"));
			campoTexto.setBorder(DEFAULT_BORDER);
		}
		
		public JTextField getCampoTexto() {
			return campoTexto;
		}
		public JButton getBotonElimina() {
			return botonElimina;
		}	
	}	
}
