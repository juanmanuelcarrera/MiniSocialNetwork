/**
 **  @author Juan Manuel Carrera García
 **/

package abd.pr1.gui;

import java.awt.*;
import java.awt.event.*;
import java.io.BufferedInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.sql.*;
import javax.sql.rowset.serial.SerialBlob;
import javax.swing.*;
import javax.swing.filechooser.FileNameExtensionFilter;

import abd.pr1.logica.Album;
import abd.pr1.logica.Conexion;
import abd.pr1.logica.Foto;

public class VentanaNuevaFotoGUI extends JDialog {
	private static final long serialVersionUID = 1L;
	
	// ---------------- PANEL DE DATOS -----------------
	private JPanel panel;
	private JTextField cuadroTexto1;
	private JTextArea cuadroArea;
	private JButton examinar;
	
	// ---------------- PANEL DE BOTONES -----------------
	private JPanel panelBotones;
	private JButton aceptar;
	private JButton cancelar;

	private Foto foto;
	//private DefaultListModel<Foto> modeloListF;
	private Album album;
	private boolean pertenecePublicacion;
	
	public VentanaNuevaFotoGUI(Album alb, JFrame padr, Foto fot) {
		super(padr, "Nueva Foto", true);
		this.setLocationRelativeTo(padr);
		this.setSize(380, 270);
		
		this.album = alb;
		this.pertenecePublicacion = false;
		//this.modeloListF = modelo;
		this.foto = fot;
		
		this.initVentana();		
	}
	
	
	public VentanaNuevaFotoGUI(JFrame padr, Foto foto) {
		super(padr, "Nueva Foto", true);
		this.setLocationRelativeTo(padr);
		this.setSize(380, 270);
		
		this.album = null;
		this.foto = foto;
		this.pertenecePublicacion = true;
		
		this.initVentana();		
		//this.arranca();
	}
	

    
	public VentanaNuevaFotoGUI(JDialog padr) {
		super(padr, "Nueva Foto", true);
		this.setSize(380, 270);
		
		this.foto = new Foto();
		
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
			
		gb.gridx = 0; 
		gb.gridy = 3; 
		gb.gridwidth = 1; 
		gb.gridheight = 1;
		gb.insets = new Insets (0, 0, 0, 45);
		this.panel.add(new JLabel("Foto: ") , gb);
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
			
		gb.gridx = 1; 
		gb.gridy = 3; 
		gb.gridwidth = 1; 
		gb.gridheight = 1;
		this.examinar = new JButton("Examinar...");
		final JDialog padreF = this;	
		this.examinar.addActionListener(new ActionListener() {
			@SuppressWarnings("resource")
			public void actionPerformed(ActionEvent e) {
				 JFileChooser selector = new JFileChooser();
				 FileNameExtensionFilter filter = new FileNameExtensionFilter("JPG & GIF & PNG", "jpg", "gif", "png");
				 selector.setFileFilter(filter); 
                 
				 if(selector.showOpenDialog(padreF) == JFileChooser.APPROVE_OPTION) {
                	 File archivo = selector.getSelectedFile();
                     String ruta = archivo.getPath();
                   
            	 	 ImageIcon imagen = new ImageIcon(ruta);
                     foto.setAncho(imagen.getIconWidth());
                     foto.setAlto(imagen.getIconHeight());
                     
                     File file = new File(ruta);
                     FileInputStream fileInput;
                     byte[] bytes = null;
                    
                     try {
						fileInput = new FileInputStream(file);
					
						bytes = new byte[(int) file.length()];
                     
                     	BufferedInputStream buffInput = new BufferedInputStream(fileInput);
                     	buffInput.read(bytes);
                     	
                     	foto.setFoto(new SerialBlob(bytes));
					} catch (SQLException | IOException e1) {
						System.out.println("aqui");
					}
				}
            }
		});
		
		this.panel.add(this.examinar , gb);
				
		
		this.panelBotones = new JPanel();
		
		this.aceptar = new JButton("Aceptar");
		final JDialog padre = this;
		aceptar.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				foto.setTitulo(cuadroTexto1.getText());
				foto.setDescripcion(cuadroArea.getText());
				
				java.util.Date utilDate = new java.util.Date();
				foto.setFecha(new java.sql.Date(utilDate.getTime()));
								
				if (foto.getFoto() != null) {
					int id;
					if (album != null) {
						id = new Conexion().añadeFotoAlbum(foto, album);
						foto.setIdFoto(id);	
						//modeloListF.addElement(foto);
					}
					else if (!pertenecePublicacion){
						id = new Conexion().añadeFoto(foto);
						foto.setIdFoto(id);	
					}
					else if (pertenecePublicacion)
						foto.setIdFoto(-2);
					dispose();
				}
				else {
					cuadroTexto1.setText("");
					cuadroArea.setText("");
					JOptionPane.showMessageDialog(padre, "Por favor, asegurese que los datos son correctos.", "", JOptionPane.WARNING_MESSAGE);
				}
			}
		});
		
		this.panelBotones.add(aceptar);
		
		
		this.cancelar = new JButton("Cancelar");
		cancelar.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				dispose();
			}
		});
		
		this.panelBotones.add(cancelar);
		
		
		Container cp = this.getContentPane();
		cp.add(this.panel, BorderLayout.CENTER);
		cp.add(panelBotones, BorderLayout.SOUTH);
		this.setVisible(true);
	}
	

	public Foto getFoto() {
		return this.foto;
	}
}
