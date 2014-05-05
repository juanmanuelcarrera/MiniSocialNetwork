/**
 **  @author Juan Manuel Carrera García
 **/

package abd.pr1.gui;

import java.awt.*;
import java.awt.event.*;
import javax.swing.*;

import abd.pr1.logica.Conexion;
import abd.pr1.logica.Usuario;

public class VentanaLoginGUI extends JFrame {
	private static final long serialVersionUID = 1L;
	
	// ---------------- PANEL DE DATOS -----------------
	private JPanel panel;
	private JTextField userT;
	private JPasswordField passwordT;
	
	// ---------------- PANEL DE BOTONES -----------------
	private JPanel panelBotones;
	private JButton aceptar;
	private JButton nuevoUsuario;
		
	public VentanaLoginGUI() {
		super();
		this.setSize(370, 132);
		this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);	
		this.setLocationRelativeTo(null);

		this.initVentana();
	}
	
  	private void initVentana() {
		this.panel = new JPanel();
		this.panel.setLayout(new GridLayout(2, 2, 5, 5));
		
		JLabel userE = new JLabel("Direccion de correo: ");
		this.panel.add(userE);
		
		this.userT = new JTextField(12);
		this.panel.add(this.userT);
		
		JLabel passwordE = new JLabel("Contraseña: ");
		this.panel.add(passwordE, BorderLayout.WEST);
		
		this.passwordT = new JPasswordField(12);
		this.panel.add(this.passwordT);
		
		this.panelBotones = new JPanel();
		this.panelBotones.setLayout(new FlowLayout());
		
		this.aceptar = new JButton("Aceptar");
		final JFrame padreF = this;
		this.aceptar.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				String user = userT.getText(); 
				@SuppressWarnings("deprecation")
				String pass = passwordT.getText();
				Usuario usuario = new Conexion().login(user, pass);
				
				if (usuario != null) {
					dispose(); 
					new VentanaPerfilGUI(usuario);
				}
				else {
					passwordT.setText("");
					JOptionPane.showMessageDialog(padreF, "Por favor, asegurese que los datos son correctos.", "", JOptionPane.WARNING_MESSAGE);
				}
			}
		});
		
		this.panelBotones.add(this.aceptar);
			
		this.nuevoUsuario = new JButton("Nuevo Usuario");
		this.nuevoUsuario.addActionListener(new ActionListener (){
			public void actionPerformed(ActionEvent e) {
				new VentanaNuevoUsuarioGUI(padreF);
			}
		});
		
		this.panelBotones.add(this.nuevoUsuario);

		
		Container cp = getContentPane();			
		cp.setLayout(new FlowLayout());
		cp.add(panel, BorderLayout.CENTER);
		cp.add(panelBotones, BorderLayout.SOUTH);
		this.setVisible(true);
	}
}
