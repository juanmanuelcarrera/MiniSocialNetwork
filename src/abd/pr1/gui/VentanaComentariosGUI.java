/**
 **  @author Juan Manuel Carrera García
 **/

package abd.pr1.gui;

import java.awt.Container;
import java.awt.FlowLayout;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.GridLayout;
import java.awt.Insets;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.text.SimpleDateFormat;
import java.util.List;

import javax.swing.*;

import abd.pr1.logica.Comentario;
import abd.pr1.logica.Conexion;
import abd.pr1.logica.Foto;
import abd.pr1.logica.Publicacion;
import abd.pr1.logica.Usuario;

public class VentanaComentariosGUI extends JFrame {
	private static final long serialVersionUID = 1L;
	
	// ---------------- PANEL SUPERIOR -----------------
	private JPanel panelSuperior;
	private GridBagConstraints gb;
	private JTextArea nuevoComentario;
	
	// ---------------- PANEL INFERIOR -----------------
	private JPanel panelInferiorBotones;
	private JButton añadirComentarios;
	private JButton cerrar;
	
	private Usuario usuario;
	private Foto foto;
	private int contComentarios;
	private JLabel infoComentario;
	private Publicacion publicacion;
	
	public VentanaComentariosGUI(Usuario user, Foto fot, JLabel infoCo) {
		super("0 comentario/s");
		this.setSize(500, 600);
		this.setLocationRelativeTo(null);
	
		this.usuario = user;
		this.foto = fot;
		this.infoComentario = infoCo;
		
		this.initVentana();
	}
	
	public VentanaComentariosGUI(Usuario user, Publicacion publi, JLabel infoCo) {
		super("0 comentario/s");
		this.setSize(500, 600);
		this.setLocationRelativeTo(null);
	
		this.usuario = user;
		this.publicacion = publi;
		this.infoComentario = infoCo;
		
		this.initVentana();
		//this.arranca();
	}
	
  	private void initVentana() {
		// ------------------------------------- PANEL SUPERIOR DE LOS COMENTARIOS ----------------------------
		this.panelSuperior = new JPanel(new GridBagLayout());
		JScrollPane barra = new JScrollPane(this.panelSuperior);     
		this.gb = new GridBagConstraints();
					
		if (this.foto != null)
			this.cargaComentariosFoto();
		else if (this.publicacion != null)
			this.cargaComentariosPublicacion();
			
		// --------------------------------------------- BOTONES --------------------------------------------
		this.panelInferiorBotones = new JPanel(new FlowLayout(FlowLayout.RIGHT));
		
		this.añadirComentarios = new JButton("Añadir Comentario");
		this.añadirComentarios.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				java.util.Date utilDate = new java.util.Date();
				java.sql.Date sqlDate = new java.sql.Date(utilDate.getTime());
				
				Comentario c = new Comentario(-1, nuevoComentario.getText(), sqlDate, usuario);
				int id = 0;
				if (foto != null)
					id = new Conexion().añadeComentarioFoto(foto, c);
				else if (publicacion != null)
					id = new Conexion().añadeComentarioPublicacion(publicacion, c);
				c.setIdComentario(id);
				añadirComentario(c);
				
				nuevoComentario.setText("");
			}
		});
			
		this.panelInferiorBotones.add(this.añadirComentarios);
		
		this.cerrar = new JButton("Cerrar");
		this.cerrar.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				dispose();
			}
		});
		
		this.panelInferiorBotones.add(this.cerrar);
		
	
		Container cp = this.getContentPane();
		cp.setLayout(new GridBagLayout());
		GridBagConstraints gb1 = new GridBagConstraints();
		
		gb1.insets = new Insets(4, 4, 4, 4);
		gb1.gridx = 0; 
		gb1.gridy = 0; 
		gb1.gridwidth = 1; 
		gb1.gridheight = 1;
		gb1.weightx = 1.0;
		gb1.weighty = 1.0;
		gb1.fill = GridBagConstraints.BOTH;
		gb1.anchor = GridBagConstraints.CENTER;
		cp.add(barra, gb1);
		
		gb1.gridy = 1; 
		gb1.weighty = 0.0;
		gb1.fill = GridBagConstraints.HORIZONTAL;
		gb1.anchor = GridBagConstraints.SOUTH;
		this.nuevoComentario = new JTextArea();
		this.nuevoComentario.setRows(3);
		this.nuevoComentario.setLineWrap(true); 
		this.nuevoComentario.setWrapStyleWord(true);
		JScrollPane barra1 = new JScrollPane(this.nuevoComentario);
		cp.add(barra1, gb1);
		
		gb1.gridy = 2; 
		gb1.weighty = 0.0;
		gb1.fill = GridBagConstraints.HORIZONTAL;
		gb1.anchor = GridBagConstraints.SOUTH;
		cp.add(this.panelInferiorBotones, gb1);
		this.setVisible(true);
	}
	
	private void añadirComentario(Comentario comen) {
		this.gb.gridy = this.contComentarios;
		this.contComentarios++;
		
		JPanel comentP = new PanelNuevoComentarioGUI(comen);
		this.panelSuperior.add(comentP, this.gb);
		
		this.infoComentario.setText(contComentarios + " comentario/s");
		this.setTitle(this.contComentarios + " comentario/s");
		this.validate();	
	}
	
	
	private void cargaComentariosFoto() {
		List<Comentario> c = new Conexion().cargaComentariosFoto(this.foto);
	
		this.contComentarios = 0;
		
		this.gb.gridx = 0; 
		this.gb.gridwidth = 1; 
		this.gb.gridheight = 1;
		this.gb.insets = new Insets (10, 10, 0, 10);
		this.gb.weightx = 1.0;
		this.gb.fill = GridBagConstraints.HORIZONTAL;
		this.gb.anchor = GridBagConstraints.NORTH;
		
		for (int i = 0; i < c.size(); i++)
			this.añadirComentario(c.get(i));
	}
	
	private void cargaComentariosPublicacion() {
		List<Comentario> c = new Conexion().cargaComentariosPublicacion(this.publicacion);
	
		this.contComentarios = 0;
		
		this.gb.gridx = 0; 
		this.gb.gridwidth = 1; 
		this.gb.gridheight = 1;
		this.gb.insets = new Insets (10, 10, 0, 10);
		this.gb.weightx = 1.0;
		this.gb.fill = GridBagConstraints.HORIZONTAL;
		this.gb.anchor = GridBagConstraints.NORTH;
		
		for (int i = 0; i < c.size(); i++)
			this.añadirComentario(c.get(i));
	}
	
	private void eliminarComentario(Comentario comen, JPanel panelC) {
		if (this.usuario.equals(comen.getUsuario())) {
			new Conexion().borraComentario(comen.getIdComentario());
			this.panelSuperior.remove(panelC);
			this.contComentarios--;
			this.repaint();
			this.validate();
			this.infoComentario.setText(contComentarios + " comentario/s");
		}
	}
	
	
	private class PanelNuevoComentarioGUI extends JPanel {
		private static final long serialVersionUID = 1L;
		
		// ---------------- PANEL SUPERIOR -----------------
		private JPanel superior;
		private JTextArea textoComentario;
		private JButton eliminar;
		
		private Comentario comentario;
				
		public PanelNuevoComentarioGUI(Comentario comen) {
			super(new GridLayout (2, 1));
			
			this.comentario = comen;
			
			this.initPanel();
		}
		
		private void initPanel() {
			this.superior = new JPanel(new FlowLayout(FlowLayout.RIGHT));
			
			this.eliminar = new JButton(" X ");
			final JPanel padreP = this;
			this.eliminar.addActionListener(new ActionListener() {
				public void actionPerformed(ActionEvent e) {
					eliminarComentario(comentario, padreP);
				}
			});
			
			SimpleDateFormat sdf = new SimpleDateFormat("dd-MM-yyyy");
			JLabel info = new JLabel("Enviado por " + this.comentario.getUsuario() + " el " + sdf.format(this.comentario.getFecha()));
			
			this.superior.add(info);
			this.superior.add(eliminar);
			this.add(this.superior);
			
			this.textoComentario = new JTextArea(this.comentario.getTexto());
			this.textoComentario.setEditable(false);
			this.textoComentario.setRows(4);
			JScrollPane barra = new JScrollPane(this.textoComentario);			
			this.add(barra);
			this.setVisible(true);

		}
	}
}
