/**
 **  @author Juan Manuel Carrera García
 **/

package abd.pr1.gui;

import java.awt.Color;
import java.awt.FlowLayout;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.GridLayout;
import java.awt.Insets;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.text.SimpleDateFormat;
import java.util.List;

import javax.swing.DefaultComboBoxModel;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;

import abd.pr1.logica.Conexion;
import abd.pr1.logica.Foto;
import abd.pr1.logica.Publicacion;
import abd.pr1.logica.Usuario;

@SuppressWarnings("serial")
public class PanelPublicacionesGUI extends JPanel {
	//------------------------ PANEL GENERAL ------------------------
	private JButton añadirP;
	private GridBagConstraints gb;
	private int contPublicaciones;
	
	//------------------------ PANEL PUBLICACIONES ------------------------
	private JPanel panelPublicaciones;
	
	private Usuario creador;
	private Usuario destinatario;
	
	public PanelPublicacionesGUI(Usuario creado, Usuario destino) {
		this.setSize(500, 600);
		
		this.creador = creado;
		this.destinatario = destino;
		
		this.initPanel();
	}
	
	
	private void initPanel() {
		this.setLayout(new GridBagLayout());
		GridBagConstraints gb1 = new GridBagConstraints();
					
		//--------------------------------------------- PANEL GENERAL --------------------------------------------
		gb1.insets = new Insets(2, 2, 2, 2);
		gb1.gridx = 0; 
		gb1.gridy = 0; 
		gb1.gridwidth = 1; 
		gb1.gridheight = 1;
		gb1.weightx = 1.0;
		gb1.anchor = GridBagConstraints.NORTH;
		this.añadirP = new JButton("Añadir Publicación");
		final PanelPublicacionesGUI padreP = this;
		this.añadirP.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				new VentanaNuevaPublicacionGUI(padreP, creador, destinatario);
			}
		});
		this.add(this.añadirP, gb1); 
		
		//--------------------------------------------- PANEL PUBLICACIONES --------------------------------------------
		this.panelPublicaciones = new JPanel(new GridBagLayout());
		this.gb = new GridBagConstraints();
		
		this.cargaPublicaciones();
		
		gb1.insets = new Insets(2, 2, 2, 2);
		gb1.gridx = 0; 
		gb1.gridy = 1; 
		gb1.gridwidth = 1; 
		gb1.gridheight = 1;
		gb1.weightx = 1.0;
		gb1.weighty = 1.0;
		gb1.fill = GridBagConstraints.BOTH;
		gb1.anchor = GridBagConstraints.CENTER;
		JScrollPane barra = new JScrollPane(this.panelPublicaciones);
		this.add(barra, gb1);
	}
	
	public void añadirPublicacion(Publicacion p) { 
		this.gb.gridy = this.contPublicaciones;
		this.contPublicaciones++;
		
		JPanel publiP = new PanelNuevoPublicacionGUI(p);  
		this.panelPublicaciones.add(publiP, this.gb);
		
		this.validate();	
	}
	
	
	private void cargaPublicaciones() {
		List<Publicacion> p = new Conexion().cargaPublicaciones(this.destinatario);
	
		this.contPublicaciones = 0;
		
		this.gb.gridx = 0; 
		this.gb.gridwidth = 1; 
		this.gb.gridheight = 1;
		this.gb.insets = new Insets (10, 10, 0, 10);
		this.gb.weightx = 1.0;
		this.gb.fill = GridBagConstraints.HORIZONTAL;
		this.gb.anchor = GridBagConstraints.NORTH;
				
		for (int i = 0; i < p.size(); i++)
			this.añadirPublicacion(p.get(i));
	}
	
	private void eliminarPublicacion(Publicacion publi, JPanel panelP) {
		if (this.creador.equals(publi.getDestinatario())) {
			new Conexion().borraPublicacion(publi);
			this.panelPublicaciones.remove(panelP);
			this.contPublicaciones--;
			this.repaint();
			this.validate();
		}
	}
	
	private class PanelNuevoPublicacionGUI extends JPanel {
		// ---------------- PANEL SUPERIOR -----------------
		private JPanel superior;
		private JTextArea textoPublicacion;
		private JButton eliminar;
		
		// ---------------- PANELES INFERIOR -----------------
		private JPanel inferiorFotos;
		private JPanel inferiorComentarios;
		private JLabel numFotos;
		private JButton comentarios;
		private JComboBox<Foto> fotos;
		private DefaultComboBoxModel<Foto> modeloComboF;
		private JLabel infoComentarios;
		
		private Publicacion publicacion; 
				
		public PanelNuevoPublicacionGUI(Publicacion p) {
			super(new GridLayout (4, 1));
			this.publicacion = p;
			
			this.initPanel();
		}
		
		private void initPanel() {
			this.setLayout(new GridBagLayout());
			this.setBackground(Color.white);
			GridBagConstraints gba = new GridBagConstraints();
			
			this.superior = new JPanel(new FlowLayout(FlowLayout.RIGHT));
			this.superior.setBackground(Color.white);
			
			this.eliminar = new JButton(" X ");
			final JPanel padrePanel = this;
			this.eliminar.addActionListener(new ActionListener() {
				public void actionPerformed(ActionEvent e) {
					eliminarPublicacion(publicacion, padrePanel);
				}
			});
			
			SimpleDateFormat sdf = new SimpleDateFormat("dd-MM-yyyy");
			JLabel info = new JLabel("De " + this.publicacion.getCreador() + ". Enviado el " + sdf.format(this.publicacion.getFecha()));
			
			this.superior.add(info);
			this.superior.add(eliminar);
			
			gba.gridx = 0; 
			gba.gridy = 0; 
			gba.gridwidth = 1; 
			gba.gridheight = 1;
			gba.weightx = 1.0;
			gba.fill = GridBagConstraints.HORIZONTAL;
			gba.insets = new Insets (5, 0, 5, 0);
			this.add(this.superior, gba);
			
			this.textoPublicacion = new JTextArea(this.publicacion.getTexto());
			this.textoPublicacion.setEditable(false);
			this.textoPublicacion.setRows(4);
			
			gba.gridx = 0; 
			gba.gridy = 1; 
			gba.gridwidth = 1; 
			gba.gridheight = 1;
			gba.weightx = 1.0;
			gba.weighty = 1.0;
			gba.fill = GridBagConstraints.BOTH;
			gba.insets = new Insets (5, 10, 5, 10);
			JScrollPane barra = new JScrollPane(this.textoPublicacion);
			this.add(barra, gba);
			
			this.inferiorFotos = new JPanel(new FlowLayout(FlowLayout.RIGHT));
			this.numFotos = new JLabel(); 
			this.inferiorFotos.add(this.numFotos);
			
			this.fotos = new JComboBox<Foto>();
			this.fotos.addActionListener(new ActionListener() {
				public void actionPerformed(ActionEvent e) {
					if (fotos.getSelectedIndex() > 0)
						new VentanaVisualizaFotoGUI(creador, (Foto)fotos.getSelectedItem());
				}
			});
			this.cargaFotos();
			this.inferiorFotos.add(this.fotos);
			
			gba.gridx = 0; 
			gba.gridy = 2; 
			gba.gridwidth = 1; 
			gba.gridheight = 1;
			gba.weightx = 1.0;
			gba.weighty = 0.0;
			gba.fill = GridBagConstraints.HORIZONTAL;
			gba.insets = new Insets (5, 0, 5, 0);
			this.add(this.inferiorFotos, gba);
			
			this.inferiorComentarios = new JPanel(new FlowLayout(FlowLayout.RIGHT));
			
			int contComentarios = new Conexion().comentariosPublicacion(publicacion);			
			this.infoComentarios = new JLabel(contComentarios + " comentario/s"); 
			this.inferiorFotos.setBackground(Color.white);
			this.inferiorComentarios.add(this.infoComentarios);
			
			this.comentarios = new JButton("...");
			this.comentarios.addActionListener(new ActionListener() {
				public void actionPerformed(ActionEvent e) {
					new VentanaComentariosGUI(creador, publicacion, infoComentarios);
				}
			});
			this.inferiorComentarios.add(this.comentarios);
			this.inferiorComentarios.setBackground(Color.white);
			
			gba.gridx = 0; 
			gba.gridy = 3; 
			gba.gridwidth = 1; 
			gba.gridheight = 1;
			gba.weightx = 1.0;
			gba.weighty = 0.0;
			this.add(this.inferiorComentarios, gba);
		}
		
		private void cargaFotos() {
			List<Foto> f = new Conexion().cargaFotosPublicacion(this.publicacion);
			this.modeloComboF = new DefaultComboBoxModel<Foto>();
			
			Foto fot = new Foto();
			fot.setTitulo("Seleccione...");
			modeloComboF.addElement(fot);
			for (int i = 0; i < f.size(); i++)
				modeloComboF.addElement(f.get(i));
				
			this.fotos.setModel(this.modeloComboF);
			this.numFotos.setText(f.size() + " adjunto/s");
		}
	}
}
