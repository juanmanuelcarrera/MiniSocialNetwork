/**
 **  @author Juan Manuel Carrera García
 **/

package abd.pr1.gui;

import java.awt.*;
import java.awt.event.*;
import java.util.List;

import javax.swing.*;
import abd.pr1.logica.Conexion;
import abd.pr1.logica.Usuario;

public class VentanaNuevoAmigoGUI extends JDialog {
	private static final long serialVersionUID = 1L;

	// ------------------ PANEL GLOBAL ----------------
	private JPanel panel;
	
	// ------------------ PANEL SUPERIOR ----------------
	private JPanel panelSuperior;
	private JTextField buscador;
	private JButton buscar;
	
	// ------------------ PANEL CENTRAL ----------------
	private DefaultListModel<Usuario> modeloListU;
	private JList<Usuario> listaU;
	
	// ------------------ PANEL INFERIOR ----------------
	private JButton aceptar;
	private JButton cancelar;

	private Usuario amigo;
	private Usuario usuario;
		
	public VentanaNuevoAmigoGUI(Usuario user, JFrame padre, Usuario amig) {
		super(padre, "Añadir Amigo", true);
		this.setLocationRelativeTo(padre);
		this.setSize(500, 400);
		
		this.usuario = user;
		this.amigo = amig;
		
		this.initVentana();
	}
	
 	private void initVentana() {
		GridBagConstraints gb = new GridBagConstraints();
		this.panel = new JPanel(new GridBagLayout());
		
		
		this.panelSuperior = new JPanel(new FlowLayout());
		
		this.panelSuperior.add(new JLabel("Buscar por nick: "));
		
		this.buscador = new JTextField(20);
		this.panelSuperior.add(this.buscador);
		
		this.buscar = new JButton("Buscar");
		this.buscar.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				cargaUsuarios();
			}
		});
		
		this.panelSuperior.add(this.buscar);
		
		gb.gridx = 0; 
		gb.gridy = 0; 
		gb.gridwidth = 1; 
		gb.gridheight = 1;
		gb.weightx = 1.0;
		gb.weighty = 1.0;
		this.panel.add(panelSuperior, gb);
		
		gb.gridx = 0; 
		gb.gridy = 1; 
		gb.gridwidth = 1; 
		gb.gridheight = 1;
		gb.anchor = GridBagConstraints.WEST;
		gb.insets = new Insets (0, 5, 0, 0);
		this.panel.add(new JLabel("Encontrados: ") , gb);
				
		this.listaU = new JList<Usuario>();
		JScrollPane barra = new JScrollPane(this.listaU);    
		
		gb.gridy = 2; 
		gb.gridwidth = 1; 
		gb.gridheight = 1;
		gb.anchor = GridBagConstraints.CENTER;
		gb.fill = GridBagConstraints.BOTH;
		gb.insets = new Insets (0, 5, 0, 5);
		this.panel.add(barra, gb);
		
		this.aceptar = new JButton("Aceptar");
		final JDialog padreD = this;
		this.aceptar.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				if (listaU.getSelectedValue() != null) {
					if (new Conexion().añadeAmigos(usuario, listaU.getSelectedValue())) {
						amigo.setAvatar(listaU.getSelectedValue().getAvatar());
						amigo.setCorreo(listaU.getSelectedValue().getCorreo());
						amigo.setDescripcion(listaU.getSelectedValue().getDescripcion());
						amigo.setNick(listaU.getSelectedValue().getNick());
						amigo.setFechaNacimiento(listaU.getSelectedValue().getFechaNacimiento());
					}
					dispose();
				}
				else 
					JOptionPane.showMessageDialog(padreD, "Por favor, asegurese que ha seleccionado un usuario.", "", JOptionPane.WARNING_MESSAGE);
			}
		});
		
		this.cancelar = new JButton("Cancelar");
		this.cancelar.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				dispose();
			}
		});
		
		
		gb.gridx = 0; 
		gb.gridy = 3; 
		gb.gridwidth = 1; 
		gb.gridheight = 1;
		gb.fill = GridBagConstraints.NONE;
		gb.insets = new Insets (0, 380, 0, 0);
		this.panel.add(cancelar , gb);
		gb.insets = new Insets (0, 200, 0, 0);
		this.panel.add(aceptar , gb);
		
				
		Container cp = this.getContentPane();
		cp.add(this.panel, BorderLayout.CENTER);
		this.setVisible(true);
	}
	
	
	private void cargaUsuarios() {
		List<Usuario> usuarios = new Conexion().buscaPersonas(this.buscador.getText());
		this.modeloListU = new DefaultListModel<Usuario>();
		
		for (int i = 0; i < usuarios.size(); i++)
			this.modeloListU.addElement(usuarios.get(i));
			
		this.listaU.setModel(this.modeloListU);
	}
}
