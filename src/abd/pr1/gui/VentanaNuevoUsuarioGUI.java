/**
 **  @author Juan Manuel Carrera García
 **/

package abd.pr1.gui;

import javax.swing.*;

import java.awt.*;

import abd.pr1.logica.Conexion;
import abd.pr1.logica.Foto;
import abd.pr1.logica.Usuario;

import java.awt.event.ActionListener;
import java.awt.event.ActionEvent;
import java.sql.Date;
import java.text.ParseException;
import java.text.SimpleDateFormat;

public class VentanaNuevoUsuarioGUI extends JDialog {
	private static final long serialVersionUID = 1L;
	
	// ---------------- PANEL DE DATOS -----------------
	private JPanel panel;
	private JTextField cuadroTextoCorreo;
	private JTextField cuadroTextoNick;
	private JTextArea cuadroAreaDescrip;
	private JPasswordField cuadroTextoPass;
	private JTextField cuadroTextoFecha;
	private JButton examinar;
		
	// ---------------- PANEL DE BOTONES -----------------
	private JPanel panelBotones;
	private JButton aceptar;
	private JButton cancelar;

	private Foto nuevaFoto;
	
	public VentanaNuevoUsuarioGUI(JFrame padre) {    // SI lo dejo como JFRAME QUITAR EL PARAMETTRO ?????????
		super(padre, "Nuevo Usuario", true);
		this.setLocationRelativeTo(padre);
		this.setSize(380, 300);
		
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
		this.panel.add(new JLabel("Correo: ") , gb);
		
		gb.gridx = 0; 
		gb.gridy = 1; 
		gb.gridwidth = 1; 
		gb.gridheight = 1;
		gb.insets = new Insets (0, 0, 0, 35);
		this.panel.add(new JLabel("Nick: ") , gb);
		
		gb.gridx = 0; 
		gb.gridy = 2; 
		gb.gridwidth = 1; 
		gb.gridheight = 1;
		gb.insets = new Insets (0, 0, 0, 35);
		this.panel.add(new JLabel("Contraseña: ") , gb);
		
		gb.gridx = 0; 
		gb.gridy = 3; 
		gb.gridwidth = 1; 
		gb.gridheight = 1;
		gb.insets = new Insets (0, 0, 0, 35);
		this.panel.add(new JLabel("Fecha: ") , gb);
		
		gb.gridx = 0; 
		gb.gridy = 4; 
		gb.gridwidth = 1; 
		gb.gridheight = 1;
		gb.insets = new Insets (0, 0, 0, 0);
		this.panel.add(new JLabel("Descripcion: ") , gb);
				
		gb.gridx = 0; 
		gb.gridy = 5; 
		gb.gridwidth = 1; 
		gb.gridheight = 1;
		gb.insets = new Insets (0, 0, 0, 35);
		this.panel.add(new JLabel("Avatar: ") , gb);
		gb.insets = new Insets (0, 0, 0, 0);
				
		gb.gridx = 1; 
		gb.gridy = 0; 
		gb.gridwidth = 1; 
		gb.gridheight = 1;
		this.cuadroTextoCorreo = new JTextField();
		this.cuadroTextoCorreo.setColumns(20);
		this.panel.add(this.cuadroTextoCorreo, gb);
		
		gb.gridx = 1; 
		gb.gridy = 1; 
		gb.gridwidth = 1; 
		gb.gridheight = 1;
		this.cuadroTextoNick = new JTextField();
		this.cuadroTextoNick.setColumns(20);
		this.panel.add(this.cuadroTextoNick, gb);
		
		gb.gridx = 1; 
		gb.gridy = 2; 
		gb.gridwidth = 1; 
		gb.gridheight = 1;
		this.cuadroTextoPass = new JPasswordField();
		this.cuadroTextoPass.setColumns(20);
		this.panel.add(this.cuadroTextoPass, gb);
			
		gb.gridx = 1; 
		gb.gridy = 3; 
		gb.gridwidth = 1; 
		gb.gridheight = 1;
		this.cuadroTextoFecha = new JTextField();
		this.cuadroTextoFecha.setColumns(20);
		this.panel.add(this.cuadroTextoFecha, gb);
			
		gb.gridx = 1; 
		gb.gridy = 4; 
		gb.gridwidth = 1; 
		gb.gridheight = 1;
		this.cuadroAreaDescrip = new JTextArea(5, 20);
		this.cuadroAreaDescrip.setLineWrap(true); 
		this.cuadroAreaDescrip.setWrapStyleWord(true); 
		JScrollPane barra = new JScrollPane(this.cuadroAreaDescrip);
		this.panel.add(barra, gb);
			
		gb.gridx = 1; 
		gb.gridy = 5; 
		gb.gridwidth = 1; 
		gb.gridheight = 1;
		this.examinar = new JButton("Examinar");
		final JDialog padreD = this;
		this.examinar.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				VentanaNuevaFotoGUI pestañaFoto = new VentanaNuevaFotoGUI(padreD);
				nuevaFoto = pestañaFoto.getFoto();
			}
		});
			
		this.panel.add(this.examinar, gb);
				
		this.panelBotones = new JPanel();
		this.aceptar = new JButton("Aceptar");
		this.aceptar.addActionListener(new ActionListener() {
			@SuppressWarnings("deprecation")
			public void actionPerformed(ActionEvent e) {
				Usuario usuario = new Usuario();
				usuario.setCorreo(cuadroTextoCorreo.getText());
				usuario.setDescripcion(cuadroAreaDescrip.getText());
				usuario.setPassword(cuadroTextoPass.getText());
				usuario.setNick(cuadroTextoNick.getText());
						
				SimpleDateFormat formatoDelTexto = new SimpleDateFormat("dd-MM-yyyy");  // Acepta 10-10-2010 o 2010-10-10 pero no con /
				String strFecha = cuadroTextoFecha.getText();
				try {
					Date fechaNacimiento = new Date(formatoDelTexto.parse(strFecha).getTime());
					usuario.setFechaNacimiento(fechaNacimiento);
				} catch (ParseException ex) {}
				
				
				if (!usuario.getCorreo().equals("") && !usuario.getPassword().equals("") && (usuario.getFecha() != null) && !usuario.getNick().equals("") && (nuevaFoto != null)) {
					usuario.setAvatar(nuevaFoto);
					new Conexion().añadeUsuario(usuario);
					dispose();
				}
				else {
					cuadroTextoCorreo.setText("");
					cuadroAreaDescrip.setText("");
					cuadroTextoPass.setText("");
					cuadroTextoNick.setText("");
					cuadroTextoFecha.setText("");
					JOptionPane.showMessageDialog(padreD, "Por favor, asegurese que los datos son correctos y ha elegido una imagen.", "", JOptionPane.WARNING_MESSAGE);
				}
			}
		});
		
		this.panelBotones.add(aceptar);
		
		this.cancelar = new JButton("Cancelar");
		this.cancelar.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				dispose();
			}
		});
		
		this.panelBotones.add(cancelar);
		
		
		Container cp = this.getContentPane();
		cp.add(this.panel, BorderLayout.CENTER);
		cp.add(this.panelBotones, BorderLayout.SOUTH);
		this.setVisible(true);
	}
}
