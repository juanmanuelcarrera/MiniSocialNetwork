/**
 **  @author Juan Manuel Carrera García
 **/

package abd.pr1.gui;

import java.awt.*;
import java.awt.event.*;
import java.sql.SQLException;
import java.text.SimpleDateFormat;

import javax.swing.*;

import abd.pr1.logica.Conexion;
import abd.pr1.logica.Foto;
import abd.pr1.logica.Usuario;

public class VentanaVisualizaFotoGUI extends JFrame {
	private static final long serialVersionUID = 1L;
	
	// ---------------- PANEL DE DATOS -----------------
	private JPanel panel;
	private JLabel fot;
	private JTextArea area;
	private JLabel info;
	private JLabel infoComen;
	
	private JPanel panelinferiorComentarios;
	private JButton comentarios;
	
	private Foto foto;
	private Usuario usuario;
	
	public VentanaVisualizaFotoGUI(Usuario user, Foto f) {
		super(f.getTitulo());
		this.setSize(500, 500);
		this.setLocationRelativeTo(null);
			
		this.usuario = user;
		this.foto = f;
		
		this.initVentana();
	}
	
  	private void initVentana() {
		GridBagConstraints gb = new GridBagConstraints();
		
		this.panel = new JPanel(new GridBagLayout());
		this.fot = new JLabel();
		
		this.fot.setSize(foto.getAncho(), foto.getAlto());
		
		Icon imagen = null;
		try {
			imagen = new ImageIcon(foto.getFoto().getBytes(1, (int) foto.getFoto().length()));
		} catch (SQLException e) {}
		
		this.fot.setIcon(imagen);	
		JScrollPane pane = new JScrollPane(this.fot);
		
		gb.gridx = 0; 
		gb.gridy = 0; 
		gb.gridwidth = 1; 
		gb.gridheight = 1;
		gb.insets = new Insets (0, 0, 0, 0);
		gb.weightx = 1.0;
		gb.weighty = 1.0;
		this.panel.add(pane, gb);
			
		gb.gridx = 0; 
		gb.gridy = 1; 
		gb.gridwidth = 1; 
		gb.gridheight = 1;
		gb.insets = new Insets (0, 5, 0, 5);
		gb.fill = GridBagConstraints.BOTH;
		this.area = new JTextArea(foto.getDescripcion());
		this.area.setEditable(false);
		JScrollPane barra1 = new JScrollPane(area);
		this.panel.add(barra1, gb);
		
		this.panelinferiorComentarios = new JPanel(new FlowLayout(FlowLayout.RIGHT));
		
		int comentario = new Conexion().comentariosFoto(foto); 
		this.info = new JLabel(new SimpleDateFormat("dd/MM/yyyy").format(foto.getFecha()) + "  . "); 
		this.infoComen = new JLabel (comentario + " comentario/s");
		this.panelinferiorComentarios.add(this.info);
		this.panelinferiorComentarios.add(this.infoComen);
		
		this.comentarios = new JButton("...");
		this.comentarios.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				new VentanaComentariosGUI(usuario, foto, infoComen);
			}
		});
			
		this.panelinferiorComentarios.add(comentarios);
			
		Container cp = this.getContentPane();
		cp.add(this.panel, BorderLayout.CENTER);
		cp.add(this.panelinferiorComentarios, BorderLayout.SOUTH);
		this.setVisible(true);
	}
}
