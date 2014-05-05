/**
 **  @author Juan Manuel Carrera García
 **/

package abd.pr1.gui;

import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.sql.SQLException;
import java.text.SimpleDateFormat;

import javax.swing.*;
import javax.swing.border.*;
import javax.swing.table.DefaultTableModel;
import abd.pr1.logica.Album;
import abd.pr1.logica.Conexion;
import abd.pr1.logica.Foto;
import abd.pr1.logica.Usuario;

import java.util.List;

public class VentanaPerfilGUI extends JFrame {
	private static final long serialVersionUID = 1L;
	
	//------------------------ PANEL SUPERIOR ------------------------
	private JPanel panelSuperior;
	private JLabel fot;
	private JLabel nick;
	private JLabel fechaNacimiento;
	private JTextArea descrip;
	private JButton modifica;
	
	//----------------------- PANEL INFERIOR ------------------------------
	private JTabbedPane panelInferior;
	
	//------------------------ PESTAÑA FOTOS -------------------------
	private DefaultComboBoxModel<Album> modeloComboA;
	private DefaultListModel<Foto> modeloListF;
	private JComboBox<Album> comboA;
	private JList<Foto> listaF;
	private JPanel panelFotos;
	private JButton nuevoAlbum;
	private JButton eliminarAlbum;
	private JButton eliminarFoto;
	private JButton añadirFoto;
	private JButton visualizarFoto;
	
	//------------------------ PESTAÑA AMIGOS -------------------------
	private JPanel panelAmigos;
	private DefaultTableModel modeloTablaA;
	private JTable tablaA;
	private JButton añadirAmigo;
	private JButton eliminarAmigo;
	private JButton informacionAmigo;
	
	
	private Usuario usuario;
	private JFrame ventana;
		
	
	public VentanaPerfilGUI(Usuario user) {
		super("Perfil");
		this.setSize(600, 700);
		this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);	
		this.setLocationRelativeTo(null);
		
		this.usuario = user;
		this.ventana = this;
		
		this.initVentana();	
		
	}
	
	private void initVentana() {
		//----------------------- PANEL SUPERIOR ------------------------------
		this.creapanelSuperior();
		
		//----------------------- PANEL INFERIOR ------------------------------
		this.panelInferior = new JTabbedPane();
		this.creaPanelInferior();
				
		
		// ---------------------- PESTAÑA PUBLICACIONES -------------------------
		panelInferior.add("Publicaciones", new PanelPublicacionesGUI(this.usuario, this.usuario));
						
		Container cp = getContentPane();				// Obtenemos el contenedor del JFrame y lo modificamos
		cp.add(this.panelSuperior, BorderLayout.NORTH);
		cp.add(this.panelInferior, BorderLayout.CENTER);
		this.setVisible(true);
	}
	
	private void creapanelSuperior() {
		//----------------------- PANEL SUPERIOR ------------------------------
		this.panelSuperior = new JPanel();
		this.panelSuperior.setLayout(new GridBagLayout());
		GridBagConstraints gb = new GridBagConstraints();
		
		//----------------------- Foto ------------------------------
		this.fot = new JLabel();
		this.fot.setSize(200, 150);
		
		ImageIcon imagen = null;
		
		try {
			imagen = new ImageIcon(this.usuario.getFoto().getFoto().getBytes(1, (int) this.usuario.getFoto().getFoto().length()));
		} catch (SQLException e) {}
		
		Icon icono = new ImageIcon(imagen.getImage().getScaledInstance(fot.getWidth(), fot.getHeight(), Image.SCALE_DEFAULT));
		
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
		this.nick = new JLabel(this.usuario.getNick());
		
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
		String fecha = sdf.format(this.usuario.getFecha());
		this.fechaNacimiento = new JLabel(fecha);
		
		gb.gridx = 1; 
		gb.gridy = 1; 
		gb.gridwidth = 1; 
		gb.gridheight = 1; 
		gb.weightx = 1.0;
		gb.insets = new Insets (0, 0, 15, 0);
		this.panelSuperior.add(this.fechaNacimiento, gb);
		gb.weightx = 0.0;
		
		//----------------------- Descripción ------------------------------
		this.descrip = new JTextArea(this.usuario.getDescripcion());
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
		
		//----------------------- Botón ------------------------------
		this.modifica = new JButton("Modificar información...");
		this.modifica.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				VentanaModificarInformacionGUI pestañaModifica = new VentanaModificarInformacionGUI(ventana, usuario);
				usuario = pestañaModifica.getUsuario();
				actualizaPanelSuperior();
			}
		});
		
		gb.gridx = 1; 
		gb.gridy = 3; 
		gb.gridwidth = 1; 
		gb.gridheight = 1;
		gb.weightx = 1.0;
		gb.anchor = GridBagConstraints.EAST;
		gb.insets = new Insets (15, 0, 0, 10);
		this.panelSuperior.add(this.modifica, gb);
		gb.weightx = 0.0;	
	}
	
	private void actualizaPanelSuperior() {
		ImageIcon imagen = null;
		
		try {
			imagen = new ImageIcon(this.usuario.getFoto().getFoto().getBytes(1, (int) this.usuario.getFoto().getFoto().length()));
		} catch (SQLException e) {}
		
		Icon icono = new ImageIcon(imagen.getImage().getScaledInstance(fot.getWidth(), fot.getHeight(), Image.SCALE_DEFAULT));
		
		this.fot.setIcon(icono);			
				
		//----------------------- Nick ------------------------------
		this.nick.setText(this.usuario.getNick());
		Font auxFont = this.nick.getFont(); 
		this.nick.setFont(new Font(auxFont.getFontName(), auxFont.getStyle(), 20));
		
		//----------------------- Fecha ------------------------------
		SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy");
		String fecha = sdf.format(this.usuario.getFecha());
		this.fechaNacimiento.setText(fecha);
		
		//----------------------- Descripción ------------------------------
		this.descrip.setText(this.usuario.getDescripcion());
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
			
		// ------- BOTONES -------- 
		this.nuevoAlbum = new JButton ("Nuevo álbum...");
		this.nuevoAlbum.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				Album a = new Album();
				new VentanaNuevoAlbumGUI(usuario, ventana, a);
				if (a.getIdAlbum() != -1)
					modeloComboA.addElement(a);
			}
		});
		
		this.eliminarAlbum = new JButton ("Eliminar álbum"); 
		this.eliminarAlbum.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				if (comboA.getSelectedIndex() > 0) {
					new Conexion().borraAlbum((Album) comboA.getSelectedItem());
					modeloComboA.removeElementAt(comboA.getSelectedIndex());
				}
				else 
					JOptionPane.showMessageDialog(ventana, "Por favor, elija un álbum de la lista.", "", JOptionPane.WARNING_MESSAGE);
			}
		});
		
		gb.insets = new Insets (7, 0, 0, 480);
		this.panelFotos.add(new JLabel ("Álbum: "), gb);
		
		gb.insets = new Insets (7, 0, 0, 300);
		this.panelFotos.add(this.comboA, gb);
		
		gb.insets = new Insets (7, 50, 0, 0);
		this.panelFotos.add(this.nuevoAlbum, gb);
		
		gb.insets = new Insets (7, 400, 0, 0);
		this.panelFotos.add(this.eliminarAlbum, gb);
		
		
		
		// ---------------------- Centro -------------------------
		this.listaF = new JList<Foto>();
		JScrollPane barra = new JScrollPane(this.listaF);   
		
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
		this.eliminarFoto = new JButton ("Eliminar foto");
		this.eliminarFoto.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				if (listaF.getSelectedValue() != null) {
					new Conexion().borraFoto(listaF.getSelectedValue().getIdFoto());
					modeloListF.removeElementAt(listaF.getSelectedIndex());
				}
				else 
					JOptionPane.showMessageDialog(ventana, "Por favor, elija una foto de la lista.", "", JOptionPane.WARNING_MESSAGE);
			}
		});
		
		
		this.añadirFoto = new JButton ("Añadir foto");
		this.añadirFoto.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				if (comboA.getSelectedIndex() > 0) {
					Foto f = new Foto();
					new VentanaNuevaFotoGUI((Album) comboA.getSelectedItem(), ventana, f);
					if (f.getIdFoto() != -1)
						modeloListF.addElement(f);
				}
				else
					JOptionPane.showMessageDialog(ventana, "Por favor, elija un álbum de la lista.", "", JOptionPane.WARNING_MESSAGE);
			}
		});
		
		
		this.visualizarFoto = new JButton ("Visualizar foto");
		this.visualizarFoto.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				if (listaF.getSelectedValue() != null)
					new VentanaVisualizaFotoGUI(usuario, listaF.getSelectedValue());
				else
					JOptionPane.showMessageDialog(ventana, "Por favor, elija una foto de la lista.", "", JOptionPane.WARNING_MESSAGE);
			}
		});
		
		gb.gridx = 0; 
		gb.gridy = 2; 
		gb.gridwidth = 1; 
		gb.gridheight = 1;
		gb.insets = new Insets (0, 8, 7, 0);
		panelFotos.add(this.añadirFoto, gb);
		gb.insets = new Insets (0, 0, 7, 220);
		panelFotos.add(this.visualizarFoto, gb);
		gb.insets = new Insets (0, 220, 7, 0);
		panelFotos.add(this.eliminarFoto , gb);
		gb.weightx = 0.0;
		
		this.panelInferior.add("Fotos", this.panelFotos);
	}
	
		
	private void creaPanelInferiorAmigos() {
		// ---------------------- PESTAÑA AMIGOS -------------------------
		this.panelAmigos = new JPanel();
		this.panelAmigos.setLayout(new GridBagLayout());
		
		GridBagConstraints gb = new GridBagConstraints();
				
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
		panelAmigos.add(barra, gb);
		gb.weighty = 0.0;
		gb.fill = GridBagConstraints.NONE;
								
		// ---------------------- Inferior -------------------------
		this.añadirAmigo = new JButton ("Añadir");
		this.añadirAmigo.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				Usuario amigo = new Usuario();
				new VentanaNuevoAmigoGUI(usuario, ventana, amigo);
				if (!amigo.getCorreo().equals("")) {
					java.util.Date utilDate = new java.util.Date();
					java.sql.Date sqlDate = new java.sql.Date(utilDate.getTime());
					
					Object [] a = {amigo, new SimpleDateFormat("dd/MM/yyyy").format(sqlDate)};
					modeloTablaA.addRow(a);
				}
			}
		});
		
		
		this.informacionAmigo = new JButton ("Información");
		this.informacionAmigo.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				if (tablaA.getSelectedRow() != -1) {
					Usuario amigo = (Usuario) tablaA.getValueAt(tablaA.getSelectedRow(), 0);
					new VentanaPerfilAmigoGUI(amigo, usuario, ventana);
				}
				else 
					JOptionPane.showMessageDialog(ventana, "Por favor, elija un usuario de la lista.", "", JOptionPane.WARNING_MESSAGE);
			}
		});
		
		
		this.eliminarAmigo = new JButton ("Eliminar");
		this.eliminarAmigo.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				if (tablaA.getSelectedRow() != -1) {
					new Conexion().borraAmigo(usuario, (Usuario) tablaA.getValueAt(tablaA.getSelectedRow(), 0));
					modeloTablaA.removeRow(tablaA.getSelectedRow());
				}
				else 
					JOptionPane.showMessageDialog(ventana, "Por favor, elija un usuario de la lista.", "", JOptionPane.WARNING_MESSAGE);
			}
		});
		
		
		gb.gridx = 0; 
		gb.gridy = 2; 
		gb.gridwidth = 1; 
		gb.gridheight = 1;
		gb.insets = new Insets (0, 0, 7, 0);
		panelAmigos.add(this.añadirAmigo, gb);
		gb.insets = new Insets (0, 0, 7, 220);
		panelAmigos.add(this.informacionAmigo, gb);
		gb.insets = new Insets (0, 200, 7, 0);
		panelAmigos.add(this.eliminarAmigo, gb);
		
		
		panelInferior.add("Amigos", this.panelAmigos);		
}

	
	
	private void cargaAlbumes() {
		List<Album> album = new Conexion().cargaAlbumes(this.usuario);
		this.modeloComboA = new DefaultComboBoxModel<Album>();
		
		Album a = new Album();
		a.setTitulo("Seleccione...");
		modeloComboA.addElement(a);
		for (int i = 0; i < album.size(); i++)
			modeloComboA.addElement(album.get(i));
			
		this.comboA.setModel(this.modeloComboA);
	}
	
	
	private void cargaFotos() {
		List<Foto> foto = new Conexion().cargaFotosAlbum((Album) comboA.getSelectedItem());
		this.modeloListF = new DefaultListModel<Foto>();
		
		for (int i = 0; i < foto.size(); i++)
			modeloListF.addElement(foto.get(i));
		
		this.listaF.setModel(this.modeloListF);
	}
	
	
	private void cargaUsuarios() {
		Object [][] amigos = new Conexion().cargaAmigos(this.usuario);
		this.modeloTablaA = new DefaultTableModel();
		
		this.modeloTablaA.addColumn("Nick");
		this.modeloTablaA.addColumn("Desde");
				
		for (int i = 0; i < amigos.length; i++) {
			Object [] a = {amigos[i][0], new SimpleDateFormat("dd/MM/yyyy").format(amigos[i][1])};
			this.modeloTablaA.addRow(a);
		}
		
		this.tablaA.setModel(this.modeloTablaA);
	}
}
