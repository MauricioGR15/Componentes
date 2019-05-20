package Componente2;

import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import javax.swing.JComboBox;
import javax.swing.JTextField;

public class JComboFiltrado extends JComboBox<String> {
	
	private final int[] VK_BLOCKS = new int[] { KeyEvent.VK_LEFT, KeyEvent.VK_RIGHT, KeyEvent.VK_DOWN, KeyEvent.VK_UP };
	
	private JTextField textField;
    private List<String> elementos, elementosOrd;
    private boolean ordenado;

    
    /**
     * Crea un componente JComboFiltrado que es un combo que va a filtrar automáticamente
     * con respecto al texto que se haya escrito en el combo.
     * @param elementos - Un arreglo de tipo String con los elementos que se van a agregar en el combo.
     */
    public JComboFiltrado(String[] elementos) {
        super(elementos);
        this.elementos = new ArrayList<String>();
        this.elementos = Arrays.asList(elementos);
        elementosOrd = Arrays.asList(elementos.clone());
        Collections.sort(elementosOrd);
        setEditable(true);
        textField = (JTextField) getEditor().getEditorComponent();
        textField.addKeyListener(new Escuchador());
        textField.setText("");
    }

    private void filtrarCombo(String texto, boolean enter) {
        List<String> elementos = ordenado ? elementosOrd : this.elementos;

        List<String> arrayFiltrado= new ArrayList<String>();
        for (int i = 0; i < elementos.size(); i++) {
            if (elementos.get(i).toLowerCase().startsWith(texto.toLowerCase()))
                arrayFiltrado.add(elementos.get(i));
        }

        removeAllItems();
        if (arrayFiltrado.size() > 0) {
            for (String s: arrayFiltrado)
            	super.addItem(s);

            if(enter) 
            	textField.setText(getSelectedItem().toString());
            else {
            	textField.setText(texto);
            	hidePopup();
                showPopup();
            }
        }
        else
        	textField.setText(texto);
    }
    
    /**
     * El método ordena ascendentemente el contenido.
     * 
     * @param ordenado - Parámetro booleano. True para ordenarlo y false hace el efecto contrario.
     */
    public void setOrdenado(boolean ordenado) {
    	this.ordenado = ordenado;
    	filtrarCombo(textField.getText(), false);
    }
    
    
    /**
     * Agrega un elemento a la lista
     * 
     * @param elemento - El objeto que se a añadir a la lista
     */
    public void addItem(String elemento) {
    	super.addItem(elemento);
    	elementos.add(elemento);
    	elementosOrd.add(elemento);
    	Collections.sort(elementosOrd);
    }
    
    
    /**
     * Remueve un elemento de la Lista.
     * 
     * @param elemento - El objeto que se va a remover.
     */
    public void removeItem(Object elemento) {
    	super.removeItem(elemento);
    	elementos.remove(elemento);
    	elementosOrd.remove(elemento);
    }
    
    private class Escuchador extends KeyAdapter {
		public void keyReleased(KeyEvent evt) {
			int keyCode = evt.getKeyCode();
			for(int i = 0; i < VK_BLOCKS.length; i++) {
				if(keyCode == VK_BLOCKS[i])
					return;
			}
			filtrarCombo(textField.getText(), keyCode == KeyEvent.VK_ENTER);
        }
	}
}
