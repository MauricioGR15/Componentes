package Componente2;

import java.awt.Font;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JPanel;
import javax.swing.JTextField;

/**
 * @author Ricardo Wong & Mauricio García.<br>
 * Este componente nos proporciona un ComboBox Filtrado con la opcion de mostrarse ordenado ascendentemente.
 */

public class ComboFiltradoPanel extends JPanel implements ActionListener{
	
	private JButton btnOriginal;
	private JButton btnOrdenado;
	private JComboFiltrado combo;
	
	/**
	 * Crea un componente panel con un combo con la capacidad de ordenar sus elementos ascendentemente,
	 * ordenar sus elementos en su manera original. Al momento de escribirse en el combo filtra el contenido
	 * buscando las coincidencias existentes con el texto introducido.
	 * @param elementos - Un arreglo de tipo String con los elementos que se van a agregar en el combo.
	 */
	public ComboFiltradoPanel(String[] elementos) {
		setLayout(new GridBagLayout());
		
		combo = new JComboFiltrado(elementos);
		btnOriginal = new JButton("ORI");
		btnOrdenado = new JButton("ORD");
		
		Font font = new Font("Comic Sans",0,9);
		btnOriginal.setFont(font);
		btnOrdenado.setFont(font);

		GridBagConstraints con = new GridBagConstraints();
		con.gridx = 0;
		con.gridy = 0;
		con.gridwidth = 3;
		con.gridheight = 2;
		add(combo, con);
		con.fill = GridBagConstraints.BOTH;
		con.anchor = GridBagConstraints.LINE_START;
		con.gridx = 3;
		con.gridy = 0;
		con.gridwidth = 1;
		con.gridheight = 1;
		add(btnOriginal, con);
		con.gridx = 3;
		con.gridy = 1;
		con.gridwidth = 1;
		con.gridheight = 1;
		add(btnOrdenado, con);
		
		btnOriginal.setEnabled(false);
		btnOriginal.addActionListener(this);
		btnOrdenado.addActionListener(this);
	}
	
	/**
	 * Método que retorna el combo
	 * @return Retorna el JComboBox Filtrado
	 */
	public JComboFiltrado getComboComponent() {
		return combo;
	}
	
	public void actionPerformed(ActionEvent evt) {
		if(evt.getSource() == btnOriginal) {
			combo.setOrdenado(false);
			btnOriginal.setEnabled(false);
			btnOrdenado.setEnabled(true);
		}
		else {
			combo.setOrdenado(true);
			btnOriginal.setEnabled(true);
			btnOrdenado.setEnabled(false);
		}
	}
}

