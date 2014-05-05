/**
 **  @author Juan Manuel Carrera García
 **/

package abd.pr1.gui;

import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.sql.SQLException;
import java.text.SimpleDateFormat;
import java.util.List;

import javax.swing.*;
import javax.swing.border.LineBorder;
import javax.swing.table.DefaultTableModel;

import abd.pr1.logica.Album;
import abd.pr1.logica.Conexion;
import abd.pr1.logica.Foto;
import abd.pr1.logica.Usuario;

public class VentanaPerfilAmigoGUI extends JFrame {
	private static final long serialVersionUID = 1L;

	//------------------------ PANEL SUPERIOR ------------------------
	private JPanel panelSuperior;
	private JLabel fot;
	private JLabel nick;
	private JLabel fechaNacimiento;
	private JTextArea descrip;
	
	//----------------------- PANEL INFERIOR ------------------------------
	private JTabbedPane panelInferior;
	
	//------------------------ PESTAÑA FOTOS -------------------------
	private JPanel panelFotos;
	private DefaultComboBoxModel<Album> modeloComboA;
	private DefaultListModel<Foto> modeloListF;
	private JComboBox<Album> comboA;
	private JList<Foto> listaF;
	private JButton visualizarFoto;
	
	//------------------------ PESTAÑA AMIGOS -------------------------
	private JPanel panelAmigos;
	private DefaultTableModel modeloTablaA;
	private JTable tablaA;
	
	private Usuario usuarioPrincipal;
	private Usuario usuarioAmigo;
	
	
	public VentanaPerfilAmigoGUI(Usuario amigo, Usuario usu, JFrame padre) {
		super("Perfil");
		this.setSize(600, 700);
		this.setLocationRelativeTo(null);

		this.usuarioPrincipal = usu;
		this.usuarioAmigo = amigo;
		
		this.initVentana();
	}
	
	private void initVentana() {
		//----------------------- PANEL SUPERIOR ------------------------------
		this.panelSuperior = new JPanel();
		this.panelSuperior.setLayout(new GridBagLayout());
		this.creaPanelSuperior();
		
		//----------------------- PANEL INFERIOR ------------------------------
		this.panelInferior = new JTabbedPane();
		this.creaPanelInferior();
				
		
		// ---------------------- PESTAÑA PUBLICACIONES -------------------------
		panelInferior.add("Publicaciones", new PanelPublicacionesGUI(this.usuarioPrincipal, this.usuarioAmigo));
						
		Container cp = getContentPane();				
		cp.add(this.panelSuperior, BorderLayout.NORTH);
		cp.add(panelInferior, BorderLayout.CENTER);
		this.setVisible(true);
	}
	
	private void creaPanelSuperior() {
		//----------------------- PANEL SUPERIOR ------------------------------
	
		GridBagConstraints gb = new GridBagConstraints();
		
		//----------------------- Foto ------------------------------
		this.fot = new JLabel();
		this.fot.setSize(200, 150);
		
		ImageIcon imagen = null;
		
		try {
			imagen = new ImageIcon(this.usuarioAmigo.getFoto().getFoto().getBytes(1, (int) this.usuarioAmigo.getFoto().getFoto().length()));
		} catch (SQLException e) {}
		
		Icon icono = new ImageIcon(imagen.getImage().getScaledInstance(this.fot.getWidth(), this.fot.getHeight(), Image.SCALE_DEFAULT));
		
		this.fot.setIcon(icono);			
		
		
		gb.gridx = 0;
		gb.gridy = 0; 
		gb.gridwidth = 1; 
		gb.gridheight = 3;
		gb.weightx = 1.0;
		gb.insets = new Insets (7, 0, 0, 0);
		this.panelSuperior.add(this.fot, gb);
		gb.weightx = 0.0;
		
		//----------------------- Nick ------------------------------
		this.nick = new JLabel(this.usuarioAmigo.getNick());
		Font auxFont = this.nick.getFont(); 
		this.nick.setFont(new Font(auxFont.getFontName(), auxFont.getStyle(), 20));

		gb.gridx = 1; 
		gb.gridy = 0; 
		gb.gridwidth = 1; 
		gb.gridheight = 1; 
		gb.insets = new Insets (10, 0, 15, 0); 
		this.panelSuperior.add(this.nick, gb);
		
		//----------------------- Fecha ------------------------------
		
		SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy");
		String fecha = sdf.format(this.usuarioAmigo.getFecha());
		this.fechaNacimiento = new JLabel(fecha);
		gb.gridx = 1; 
		gb.gridy = 1; 
		gb.gridwidth = 1; 
		gb.gridheight = 1; 
		gb.weightx = 1.0;
		gb.insets = new Insets (0, 0, 15, 0);
		panelSuperior.add(this.fechaNacimiento, gb);
		gb.weightx = 0.0;
		
		//----------------------- Descripción ------------------------------
		this.descrip = new JTextArea(this.usuarioAmigo.getDescripcion());
		this.descrip.setBorder(new LineBorder(Color.BLACK));
		this.descrip.setEditable(false);
		gb.gridx = 1; 
		gb.gridy = 2; 
		gb.gridwidth = 1; 
		gb.gridheight = 1;
		gb.weightx = 1.0;
		gb.fill = GridBagConstraints.BOTH;
		gb.insets = new Insets (0, 0, 0, 12);
		this.panelSuperior.add(this.descrip, gb);
		gb.weightx = 0.0;
		gb.fill = GridBagConstraints.NONE;
		
	}
	
	
	private void creaPanelInferior() {
		// ----------------------- PANEL INFERIOR ------------------------------
		this.panelInferior = new JTabbedPane();
					
		// ---------------------- PESTAÑA FOTOS -------------------------
		this.creaPanelInferiorFotos();
				
		// ---------------------- PESTAÑA FOTOS -------------------------
		this.creaPanelInferiorAmigos();
	}
	
	
	private void creaPanelInferiorFotos() {
		// ---------------------- PESTAÑA FOTOS -------------------------
		this.panelFotos = new JPanel();
		this.panelFotos.setLayout(new GridBagLayout());
		
		GridBagConstraints gb = new GridBagConstraints();
				
		// ---------------------- Superior -------------------------
		gb.gridx = 0; 
		gb.gridy = 0; 
		gb.gridwidth = 1; 
		gb.gridheight = 1;
		gb.weightx = 1.0;
				
		// ------- COMBOBOX -------
		this.comboA = new JComboBox<Album>();
		this.cargaAlbumes();
		
		this.comboA.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				cargaFotos();
			}
		});
			
		gb.insets = new Insets (7, 0, 0, 450);
		this.panelFotos.add(new JLabel ("Álbum: "), gb);
		
		gb.insets = new Insets (7, 0, 0, 300);
		this.panelFotos.add(this.comboA, gb);
		
		// ---------------------- Centro -------------------------
		this.listaF = new JList<Foto>();
		JScrollPane barra = new JScrollPane(listaF);     
		gb.gridx = 0; 
		gb.gridy = 1; 
		gb.gridwidth = 1; 
		gb.gridheight = 1;
		gb.weighty = 1.0;
		gb.fill = GridBagConstraints.BOTH;
		gb.insets = new Insets (7, 7, 7, 7);
		this.panelFotos.add(barra, gb);
		gb.weighty = 0.0;
		gb.fill = GridBagConstraints.NONE;
				
		// ---------------------- Inferior -------------------------
		this.visualizarFoto = new JButton ("Visualizar foto");
		final JFrame padreD = this;
		this.visualizarFoto.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				if (listaF.getSelectedValue() != null)
					new VentanaVisualizaFotoGUI(usuarioPrincipal, listaF.getSelectedValue());
				else
					JOptionPane.showMessageDialog(padreD, "Por favor, elija una foto de la lista.", "", JOptionPane.WARNING_MESSAGE);

			}
		});
			
		gb.gridx = 0; 
		gb.gridy = 2; 
		gb.gridwidth = 1; 
		gb.gridheight = 1;
		gb.insets = new Insets (0, 0, 0, 0);
		panelFotos.add(this.visualizarFoto, gb);
		gb.weightx = 0.0;
		
		this.panelInferior.add("Fotos", panelFotos);
	}
	
		
	private void creaPanelInferiorAmigos() {
		// ---------------------- PESTAÑA AMIGOS -------------------------
		GridBagConstraints gb = new GridBagConstraints();
		this.panelAmigos = new JPanel();
		this.panelAmigos.setLayout(new GridBagLayout());
			
		// ---------------------- Superior -------------------------
		gb.gridx = 0; 
		gb.gridy = 0; 
		gb.gridwidth = 1; 
		gb.gridheight = 1;
		gb.insets = new Insets (7, 0, 0, 450);
		this.panelAmigos.add(new JLabel ("Lista de amigos: "), gb);
			
		// ---------------------- Centro -------------------------
		this.tablaA = new JTable();
		this.cargaUsuarios();		
		
		JScrollPane barra = new JScrollPane(this.tablaA);     //// ASI CON SCROLLPANE ???????????
		gb.gridx = 0; 
		gb.gridy = 1; 
		gb.gridwidth = 1; 
		gb.gridheight = 1;
		gb.weighty = 1.0;
		gb.fill = GridBagConstraints.BOTH;
		gb.insets = new Insets (7, 0, 10, 0);
		this.panelAmigos.add(barra, gb);
		gb.weighty = 0.0;
		gb.fill = GridBagConstraints.NONE;
						
		panelInferior.add("Amigos", panelAmigos);		
	}

		
	private void cargaAlbumes() {
		List<Album> album = new Conexion().cargaAlbumes(this.usuarioAmigo);
		this.modeloComboA = new DefaultComboBoxModel<Album>();
		
		Album a = new Album();
		a.setTitulo("Seleccione...");
		modeloComboA.addElement(a);
		for (int i = 0; i < album.size(); i++)
			modeloComboA.addElement(album.get(i));
			
		this.comboA.setModel(modeloComboA);
	}
		
		
	private void cargaFotos() {
		List<Foto> foto = new Conexion().cargaFotosAlbum((Album) comboA.getSelectedItem());
		this.modeloListF = new DefaultListModel<Foto>();
		
		for (int i = 0; i < foto.size(); i++)
			this.modeloListF.addElement(foto.get(i));
		
		this.listaF.setModel(modeloListF);
	}
	
		
	private void cargaUsuarios() {
		Object [][] amigos = new Conexion().cargaAmigos(this.usuarioAmigo);
		this.modeloTablaA = new DefaultTableModel();
		
		this.modeloTablaA.addColumn("Nick");
		this.modeloTablaA.addColumn("Desde");
				
		for (int i = 0; i < amigos.length; i++) {
			Object [] a = {amigos[i][0], amigos[i][1]};
			this.modeloTablaA.addRow(a);
		}
		
		this.tablaA.setModel(this.modeloTablaA);
	}
}
