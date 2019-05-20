import java.awt.BorderLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JPanel;

import Componente1.JComboBoxEMC;
import Componente2.ComboFiltradoPanel;
import Componente3.LectorDatos;

public class pruebaComponentes extends JFrame implements ActionListener {

	LectorDatos lectorDatos;
	private static final long serialVersionUID = 1L;
	public pruebaComponentes() {
		setSize(700,700);
		setDefaultCloseOperation(EXIT_ON_CLOSE);
		setLocationRelativeTo(null);
		
		JComboBoxEMC comboEMC = new JComboBoxEMC("Sinala", "SinMun2");
		add(comboEMC, BorderLayout.SOUTH);
		
		String[] nombres = { "Ricardo", "Alejandro", "Raul", "Diana", "Josselyn", "José", "Zalvar", "Daniel", "Martín", "Alberto", "Ulises", "Octavio", "Enrique",
				"Mauricio","Jazmín", "María", "Laura", "Rodrigo", "Luis", "David", "Violeta", "Jesús", "Gabriel", "Bernabé", "Margarita", "Clemente"};
		
		ComboFiltradoPanel comboFiltradoP = new ComboFiltradoPanel(nombres);
		add(comboFiltradoP);
		
		lectorDatos = new LectorDatos(8);
		add(lectorDatos, BorderLayout.NORTH);
		
		setVisible(true);
		
	}

	@Override
	public void actionPerformed(ActionEvent arg0) {
		System.out.println("Tipo: " +lectorDatos.getTipo());
		ArrayList<String> lista = lectorDatos.getLista();
		System.out.println(lista == null ? "Existen campos incorrectos" : imprimirLista(lista));
		lista = lectorDatos.getLista(true);
		System.out.println(imprimirLista(lista));
	}
	
	public String imprimirLista(ArrayList<String> lista) {
		String total = "";
		for(String s: lista) {
			total += s + ", ";
		}
		
		return total;
	}
	
	public static void main(String[] args) {
		new pruebaComponentes();

	}

}
