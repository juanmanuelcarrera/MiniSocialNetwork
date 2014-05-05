/**
 **  @author Juan Manuel Carrera García
 **/

package abd.pr1.gui;

import java.awt.BorderLayout;
import java.awt.Container;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.sql.Date;
import java.text.ParseException;
import java.text.SimpleDateFormat;

import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JPasswordField;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import javax.swing.JTextField;

import abd.pr1.logica.Conexion;
import abd.pr1.logica.Foto;
import abd.pr1.logica.Usuario;

public class VentanaModificarInformacionGUI extends JDialog {
	private static final long serialVersionUID = 1L;
	
	// ---------------- PANEL DE DATOS -----------------
	private JPanel panel;
	private JTextField cuadroTextoNick;
	private JTextArea cuadroAreaDescrip;
	private JPasswordField cuadroTextoPass;
	private JTextField cuadroTextoFecha;
	private JButton examinar;
	
	// ---------------- PANEL DE BOTONES -----------------
	private JPanel panelBotones;
	private JButton aceptar;
	private JButton cancelar;
	
	
	private Usuario usuario;
	private Foto nuevaFoto;

	public VentanaModificarInformacionGUI(JFrame padre, Usuario usu) {
		super(padre, "NUevo Usuario", true);
		this.setLocationRelativeTo(padre);
		this.setSize(380, 400);
		
		this.usuario = usu;
		
		this.initVentana();
	}
	
	private void initVentana() {
		this.panel = new JPanel(new GridBagLayout());
		GridBagConstraints gb = new GridBagConstraints();
				
		gb.gridx = 0; 
		gb.gridy = 0; 
		gb.gridwidth = 1; 
		gb.gridheight = 1;
		gb.weightx = 1.0;
		gb.weighty = 1.0;
		gb.insets = new Insets (0, 0, 0, 35);
		this.panel.add(new JLabel("Nick: ") , gb);
		
		gb.gridx = 0; 
		gb.gridy = 1; 
		gb.gridwidth = 1; 
		gb.gridheight = 1;
		gb.insets = new Insets (0, 0, 0, 35);
		this.panel.add(new JLabel("Contraseña: ") , gb);
		
		gb.gridx = 0; 
		gb.gridy = 2; 
		gb.gridwidth = 1; 
		gb.gridheight = 1;
		gb.insets = new Insets (0, 0, 0, 35);
		this.panel.add(new JLabel("Fecha: ") , gb);
		
		gb.gridx = 0; 
		gb.gridy = 3; 
		gb.gridwidth = 1; 
		gb.gridheight = 1;
		gb.insets = new Insets (0, 0, 0, 0);
		this.panel.add(new JLabel("Descripcion: ") , gb);
		
		
		gb.gridx = 0; 
		gb.gridy = 4; 
		gb.gridwidth = 1; 
		gb.gridheight = 1;
		gb.insets = new Insets (0, 0, 0, 35);
		this.panel.add(new JLabel("Avatar: ") , gb);
						
		gb.gridx = 1; 
		gb.gridy = 0; 
		gb.gridwidth = 1; 
		gb.gridheight = 1;
		gb.insets = new Insets (0, 0, 0, 0);
		this.cuadroTextoNick = new JTextField();
		this.cuadroTextoNick.setColumns(20);
		this.panel.add(this.cuadroTextoNick, gb);
				
		gb.gridx = 1; 
		gb.gridy = 1; 
		gb.gridwidth = 1; 
		gb.gridheight = 1;
		this.cuadroTextoPass = new JPasswordField();
		this.cuadroTextoPass.setColumns(20);
		this.panel.add(this.cuadroTextoPass, gb);
				
		gb.gridx = 1; 
		gb.gridy = 2; 
		gb.gridwidth = 1; 
		gb.gridheight = 1;
		this.cuadroTextoFecha = new JTextField();
		this.cuadroTextoFecha.setColumns(20);
		this.panel.add(this.cuadroTextoFecha, gb);
				
		gb.gridx = 1; 
		gb.gridy = 3; 
		gb.gridwidth = 1; 
		gb.gridheight = 1;
		this.cuadroAreaDescrip = new JTextArea(5, 20);
		this.cuadroAreaDescrip.setLineWrap(true); 
		this.cuadroAreaDescrip.setWrapStyleWord(true); 
		JScrollPane barra = new JScrollPane(this.cuadroAreaDescrip);
		this.panel.add(barra, gb);
				
		gb.gridx = 1; 
		gb.gridy = 4; 
		gb.gridwidth = 1; 
		gb.gridheight = 1;
		this.examinar = new JButton("Examinar");
		final JDialog padreF = this;
		this.examinar.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				VentanaNuevaFotoGUI pestañaFoto = new VentanaNuevaFotoGUI(padreF);
				nuevaFoto = pestañaFoto.getFoto();
			}
		});
		this.panel.add(this.examinar, gb);
		
		
		this.panelBotones = new JPanel();
		this.aceptar = new JButton("Aceptar");
		this.aceptar.addActionListener(new ActionListener() {
			@SuppressWarnings("deprecation")
			public void actionPerformed(ActionEvent e) {
				if (!cuadroAreaDescrip.getText().equals(""))
					usuario.setDescripcion(cuadroAreaDescrip.getText());
				
				if (!cuadroTextoPass.getText().equals(""))
					usuario.setPassword(cuadroTextoPass.getText());
				
				if (!cuadroTextoNick.getText().equals(""))
					usuario.setNick(cuadroTextoNick.getText());
				
				if (!cuadroTextoFecha.getText().equals("")) {
					SimpleDateFormat formatoDelTexto = new SimpleDateFormat("dd-MM-yyyy");  // Acepta 10-10-2010 o 2010-10-10 pero no con /
					String strFecha = cuadroTextoFecha.getText();
					try {
						usuario.setFechaNacimiento(new Date(formatoDelTexto.parse(strFecha).getTime()));
					} catch (ParseException ex) {}
				}
				
				if (nuevaFoto != null)
					if (nuevaFoto != null) 
						usuario.setAvatar(nuevaFoto);
			
				new Conexion().modificaUsuario(usuario);
				dispose();
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
		cp.add(this.panel, BorderLayout.CENTER);
		cp.add(this.panelBotones, BorderLayout.SOUTH);
		this.setVisible(true);
	}
	
	public Usuario getUsuario() {
		return this.usuario;
	}
}
