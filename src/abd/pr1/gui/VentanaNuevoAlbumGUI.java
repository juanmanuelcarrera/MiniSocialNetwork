/**
 **  @author Juan Manuel Carrera García
 **/

package abd.pr1.gui;

import java.awt.*;
import java.awt.event.*;
import java.sql.Date;
import javax.swing.*;

import abd.pr1.logica.Album;
import abd.pr1.logica.Conexion;
import abd.pr1.logica.Usuario;

public class VentanaNuevoAlbumGUI extends JDialog {
	private static final long serialVersionUID = 1L;
		
	// ---------------- PANEL DE DATOS -----------------
	private JPanel panel;
	private JTextField cuadroTexto1;
	private JTextArea cuadroArea;
	
	// ---------------- PANEL DE BOTONES -----------------
	private JPanel panelBotones;
	private JButton aceptar;
	private JButton cancelar;

	private Album album;
	private Usuario usuario;
	
	public VentanaNuevoAlbumGUI(Usuario user, JFrame padre, Album alb) {
		super(padre, "Nuevo Álbum", true);
		this.setLocationRelativeTo(padre);
		this.setSize(380, 270);
		
		this.usuario = user;
		this.album = alb;
		
		this.initVentana();
	}
	
	private void initVentana() {
		GridBagConstraints gb = new GridBagConstraints();
		this.panel = new JPanel(new GridBagLayout());
		
		gb.gridx = 0; 
		gb.gridy = 0; 
		gb.gridwidth = 1; 
		gb.gridheight = 1;
		gb.insets = new Insets (0, 0, 0, 40);
		gb.weightx = 1.0;
		gb.weighty = 1.0;
		this.panel.add(new JLabel("Titulo: ") , gb);
				
		gb.gridx = 0; 
		gb.gridy = 1; 
		gb.gridwidth = 1; 
		gb.gridheight = 1;
		gb.insets = new Insets (0, 0, 0, 0);
		this.panel.add(new JLabel("Descripcion: ") , gb);
		gb.insets = new Insets (0, 0, 0, 0);
				
		gb.gridx = 1; 
		gb.gridy = 0; 
		gb.gridwidth = 1; 
		gb.gridheight = 1;
		this.cuadroTexto1 = new JTextField();
		this.cuadroTexto1.setColumns(20);
		this.panel.add(this.cuadroTexto1, gb);
					
		gb.gridx = 1; 
		gb.gridy = 1; 
		gb.gridwidth = 1; 
		gb.gridheight = 1;
		this.cuadroArea = new JTextArea(5, 20);
		this.cuadroArea.setLineWrap(true); 
		this.cuadroArea.setWrapStyleWord(true); 
		JScrollPane barra = new JScrollPane(this.cuadroArea);
		this.panel.add(barra, gb);
		
	
		this.panelBotones = new JPanel();
		this.aceptar = new JButton("Aceptar");
		final JDialog padreD = this;
		this.aceptar.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				album.setTitulo(cuadroTexto1.getText());
				album.setDescripcion(cuadroArea.getText());
				
				java.util.Date utilDate = new java.util.Date();
				Date fecha = new java.sql.Date(utilDate.getTime());
				album.setFecha(fecha);
				
				if (!album.getTitulo().equalsIgnoreCase("")) {
					int id = new Conexion().añadeAlbum(album, usuario);
					album.setIdAlbum(id);
					dispose();
				}
				else {
					cuadroTexto1.setText("");
					cuadroArea.setText("");
					JOptionPane.showMessageDialog(padreD, "Por favor, asegurese que los datos son correctos.", "", JOptionPane.WARNING_MESSAGE);
				}
			}
		});
		
		this.panelBotones.add(this.aceptar);
				
		this.cancelar = new JButton("Cancelar");
		this.cancelar.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				dispose();
			}
		});
		
		this.panelBotones.add(this.cancelar);
		
		Container cp = this.getContentPane();
		cp.add(panel, BorderLayout.CENTER);
		cp.add(panelBotones, BorderLayout.SOUTH);
		this.setVisible(true);
	}
}
