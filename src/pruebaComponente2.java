import java.awt.BorderLayout;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;
import javax.swing.*;

import Componente1.JComboBoxEMC;
import Componente2.ComboFiltradoPanel;
import Componente3.LectorDatos;


public class pruebaComponente2 extends JFrame implements ActionListener {

	LectorDatos lectorDatos;
	ComboFiltradoPanel nombre,apellido;
	JComboBoxEMC comboEMC;
	JTextField edad;
	JButton obtener,obtener2;
	JTabbedPane tb;

	private static final long serialVersionUID = 1L;
	public pruebaComponente2() {
		setSize(900,385);
		setDefaultCloseOperation(EXIT_ON_CLOSE);
		setLocationRelativeTo(null);
		
		//setLayout(new GridLayout(0, 1, 10, 10));
		
		String[] nombres = { "Ricardo", "Alejandro", "Raul", "Diana", "Josselyn", "José", "Daniel", "Martín", "Alberto", "Ulises", "Octavio", "Enrique",
				"Mauricio","Jazmín", "María", "Laura", "Rodrigo", "Luis", "David", "Violeta", "Jesús", "Gabriel", "Bernabé", "Margarita", "Clemente"};
		
		String[] apellidos = {"García","Escobar","Rubio","Román","González","Martínez","López","Luna","Benítez","Estrada","Loaiza","Méndez"};
		
		nombre = new ComboFiltradoPanel(nombres);
		apellido = new ComboFiltradoPanel(apellidos);
		lectorDatos = new LectorDatos();
		comboEMC = new JComboBoxEMC("Sinaloa", "SinMun2");
		
		tb = new JTabbedPane();
		JPanel tab1 = new JPanel(new GridLayout(0, 1, 10, 10));
		JPanel panel = new JPanel(new BorderLayout());
		JPanel panel2 = new JPanel();
		
		panel2.add(new JLabel ("Nombre: "));
		panel2.add(nombre);
		panel2.add(new JLabel ("Apellido: "));
		panel2.add(apellido);
		panel2.add(new JLabel ("Edad: "));
		panel2.add(edad = new JTextField(5));
		panel2.add(obtener = new JButton("Obtener datos"));
		//panel.add(panel2,BorderLayout.NORTH);
		panel.add(comboEMC, BorderLayout.NORTH);
		panel.add(new JComboBoxEMC(JComboBoxEMC.VERTICAL), BorderLayout.EAST);
		panel.add(new JComboBoxEMC("Baja California", "", JComboBoxEMC.VERTICAL), BorderLayout.WEST);
		tab1.add(panel2);
		//tab1.add(comboEMC);
		tab1.add(panel);
		tb.addTab("Combos", tab1);
		
		JPanel panel3 = new JPanel(new BorderLayout());
		panel3.add(lectorDatos);
		panel3.add(obtener2 = new JButton("Obtener lista"), BorderLayout.SOUTH);
		add(panel3);
		tb.add("Lector de datos", panel3);
		
		obtener.addActionListener(this);
		obtener2.addActionListener(this);
	
		add(tb);
		
		setVisible(true);
		
	}

	@Override
	public void actionPerformed(ActionEvent evt) {
		if(evt.getSource() == obtener2) {
			System.out.println("Tipo: " +lectorDatos.getTipo());
			ArrayList<String> lista = lectorDatos.getLista();
			System.out.println(lista == null ? "Existen campos incorrectos" : imprimirLista(lista));
			lista = lectorDatos.getLista(true);
			System.out.println(imprimirLista(lista));
			return;
		}
		if(evt.getSource() == obtener) {
//			System.out.println("Nombre > " + nombre.getComboComponent().getSelectedItem());
//			System.out.println("Apellido > " + apellido.getComboComponent().getSelectedItem());
			System.out.println("Edad > " + edad.getText());
			System.out.println("Ciudad > " + comboEMC.getSelected(JComboBoxEMC.CIUDAD));
			System.out.println("Estado > " + comboEMC.getSelected(JComboBoxEMC.ESTADO));
			System.out.println("Municipio > " + comboEMC.getSelected(JComboBoxEMC.MUNICIPIO));
		}
	}
	
	public String imprimirLista(ArrayList<String> lista) {
		String total = "";
		for(String s: lista) {
			total += s + ", ";
		}
		
		return total;
	}
	
	public static void main(String[] args) {
		new pruebaComponente2();

	}
}
