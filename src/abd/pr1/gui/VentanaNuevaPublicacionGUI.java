/**
 **  @author Juan Manuel Carrera García
 **/

package abd.pr1.gui;

import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;
import java.util.List;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;

import abd.pr1.logica.Conexion;
import abd.pr1.logica.Foto;
import abd.pr1.logica.Publicacion;
import abd.pr1.logica.Usuario;

/**
 **  @author Juan Manuel Carrera García
 **/

@SuppressWarnings("serial")
public class VentanaNuevaPublicacionGUI extends JFrame {
	//------------------------ PANEL GENERAL ------------------------
	private JPanel panel;
	private JTextArea texto;
	private JLabel adjuntos;
	private JButton adjuntar;
	private JButton aceptar;
	private JButton cancelar;
	private int contAdjuntos;
	
	private PanelPublicacionesGUI panelPublicaciones;
	private Usuario creador;
	private Usuario destinatario;
	private List<Foto> fotos;  // Fotos adjuntas a una publicaciones (las guardamos aquí hasta que el usuario cree la publicación)
	
	public VentanaNuevaPublicacionGUI(PanelPublicacionesGUI panelP, Usuario creado, Usuario destino){
		super("Nueva Publicación");
		this.setSize(400, 300);
		this.setLocationRelativeTo(null);
		
		this.creador = creado;
		this.destinatario = destino;
		this.panelPublicaciones = panelP;
		
		this.initVentana();
	}
	
	private void initVentana(){
		this.contAdjuntos = 0;
		this.fotos = new ArrayList<Foto>();
		this.panel = new JPanel(new GridBagLayout());
		GridBagConstraints gb = new GridBagConstraints();
		
		gb.gridx = 0; 
		gb.gridy = 0; 
		gb.gridwidth = 1; 
		gb.gridheight = 1;
		gb.insets = new Insets (0, 0, 0, 230);
		gb.weightx = 1.0;
		gb.weighty = 1.0;
		this.panel.add(new JLabel("Texto de la publicación: ") , gb);
		
		gb.gridx = 0; 
		gb.gridy = 1; 
		gb.gridwidth = 1; 
		gb.gridheight = 1;
		gb.fill = GridBagConstraints.BOTH;
		gb.insets = new Insets (0, 10, 0, 10);
		this.texto = new JTextArea();
		this.texto.setRows(4);
		this.texto.setLineWrap(true); 
		this.texto.setWrapStyleWord(true);
		JScrollPane barra =  new JScrollPane(this.texto);
		this.panel.add(barra, gb);
		gb.fill = GridBagConstraints.NONE;
		
		gb.gridx = 0; 
		gb.gridy = 2; 
		gb.gridwidth = 1; 
		gb.gridheight = 1;
		gb.insets = new Insets (0, 0, 0, 45);
		this.adjuntos = new JLabel(this.contAdjuntos + " adjuntos añadido/s");
		this.panel.add(this.adjuntos , gb);
		gb.insets = new Insets (0, 0, 0, 0);
					
		gb.gridx = 0; 
		gb.gridy = 3; 
		gb.gridwidth = 1; 
		gb.gridheight = 1;
		gb.insets = new Insets (0, 0, 0, 120);
		this.adjuntar = new JButton("Adjuntar imagen...");
		final JFrame padreF = this;
		this.adjuntar.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				Foto f = new Foto();
				new VentanaNuevaFotoGUI(padreF, f);
				if (f.getIdFoto() == -2) {
					fotos.add(f);
					contAdjuntos++;
					adjuntos.setText(contAdjuntos + " adjuntos añadido/s");
				}
			}
		});
		
		this.panel.add(this.adjuntar, gb);
				
		gb.gridx = 0; 
		gb.gridy = 3; 
		gb.gridwidth = 1; 
		gb.gridheight = 1;
		gb.insets = new Insets (0, 110, 0, 0);
		this.aceptar = new JButton("Aceptar");
		this.aceptar.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				java.util.Date utilDate = new java.util.Date();
				java.sql.Date sqlDate = new java.sql.Date(utilDate.getTime());
				
				Publicacion p = new Publicacion(-1, texto.getText(), sqlDate, creador, destinatario);
				int id = new Conexion().añadePublicacion(p);
				p.setIdPublicacion(id);
				añadeFotos(p);
				panelPublicaciones.añadirPublicacion(p);
				dispose();
			}
		});
		this.panel.add(this.aceptar, gb);
		
		gb.gridx = 0; 
		gb.gridy = 3; 
		gb.gridwidth = 1; 
		gb.gridheight = 1;
		gb.insets = new Insets (0, 290, 0, 0);
		this.cancelar = new JButton("Cancelar");
		this.cancelar.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				dispose();
			}
		});
		this.panel.add(this.cancelar, gb);
			
		this.getContentPane().add(this.panel);
		this.setVisible(true);
	}

	private void añadeFotos(Publicacion publi) {
		for (int i = 0; i < fotos.size(); i++) {
			int id = new Conexion().añadeFotoPublicacion(publi, this.fotos.get(i));
			this.fotos.get(i).setIdFoto(id);
		}		
	}
}
