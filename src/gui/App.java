package gui;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;

import objs.Numero;

import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.FileDialog;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.TableItem;
import org.eclipse.swt.SWT;
import org.eclipse.swt.widgets.Text;
import org.eclipse.swt.widgets.Table;
import org.eclipse.swt.widgets.TableColumn;
import org.eclipse.swt.events.MouseAdapter;
import org.eclipse.swt.events.MouseEvent;
import org.eclipse.wb.swt.SWTResourceManager;

import util.CenterScreen;
import util.Configuracion;
import util.EstadoBarraP;
import util.Imagen;
import util.Mensaje;

import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.ProgressBar;

import controllers.HttpController;
import org.eclipse.swt.graphics.Image;

public class App {
	protected Shell shlSewhatel;
	private Display display;
	private Text text_numero;
	private Table tabla_numeros;
	private ProgressBar progress_llamadas;
	private TableColumn tblclmnNumero, tblclmnEstado;
	private final String filePath_inicio_win = "%HOMEPATH%";
	private String file_seleccionado_path = "", resp;
	private ArrayList<Numero> numeros = new ArrayList<Numero>();

	/**
	 * Launch the application.
	 * 
	 * @param args
	 */
	public static void main(String[] args) {
		try {
			App window = new App();
			window.open();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	/**
	 * Open the window.
	 */
	public void open() {
		display = Display.getDefault();
		createContents(display);
		shlSewhatel.open();
		shlSewhatel.layout();
		while (!shlSewhatel.isDisposed()) {
			if (!display.readAndDispatch()) {
				display.sleep();
			}
		}
	}

	/**
	 * Create contents of the window.
	 */
	protected void createContents(final Display display) {
		shlSewhatel = new Shell(display, SWT.CLOSE | SWT.TITLE | SWT.MIN);
		shlSewhatel.setToolTipText("Configuracion");
		shlSewhatel.setImage(SWTResourceManager.getImage(App.class,
				"/iconos/icon.ico"));
		shlSewhatel.setSize(532, 473);
		shlSewhatel.setText("SEWhatel");
		CenterScreen cs = new CenterScreen();
		int screen[] = cs.get_xy(display, shlSewhatel);
		shlSewhatel.setLocation(screen[0], screen[1]);
		Button btncall = new Button(shlSewhatel, SWT.NONE);
		btncall.addMouseListener(new MouseAdapter() {
			public void mouseUp(MouseEvent e) {
				if (!numeros.isEmpty()) {
					TableItem[] items = tabla_numeros.getItems();
					for (TableItem i : items) {
						i.setText(1, "");
					}
					tabla_numeros.update();
					progress_llamadas.setMaximum(numeros.size());
					progress_llamadas.setVisible(true);
					HttpController marcarhttp = new HttpController();
					Configuracion c = new Configuracion();
					final String host = c.leer().getProperty("host", "");
					marcarhttp.set_host(host);
					int l = 1;
					for (Numero n : numeros) {
						tabla_numeros.getItem(l - 1).setText(1, "En curso...");
						tabla_numeros.update();
						try {
							resp = marcarhttp.call_encuesta(n.get_numero());
							progress_llamadas
									.setState(EstadoBarraP.BAR_ESTADO_NORMAL);
							// ///Check linea activa
							Thread checkl = new Thread() {
								public void run() {
									try {
										HttpController marcarhttp = new HttpController();
										marcarhttp.set_host(host);
										resp = marcarhttp.check_linea();
									} catch (IOException e) {
										// TODO Auto-generated catch block
										e.printStackTrace();
									}
									if (resp.contains("AGI(encuestam.php)")) {
										try {
											Thread.sleep(5000);
										} catch (InterruptedException e) {
											// TODO Auto-generated catch block
											e.printStackTrace();
										}
										this.run();
									}
								}
							};
							checkl.run();
							// ////////////////////
							tabla_numeros.getItem(l - 1).setText(1,
									"Finalizada");
							tabla_numeros.update();
						} catch (IOException e1) {
							progress_llamadas
									.setState(EstadoBarraP.BAR_ESTADO_ERROR);
							// TODO Auto-generated catch block
							tabla_numeros.getItem(l - 1).setText(1,
									"Error de conexion");
							tabla_numeros.update();
							// e1.printStackTrace();
						}
						progress_llamadas.setSelection(l);
						l++;
					}

				} else {
					progress_llamadas.setSelection(30);
					progress_llamadas.setState(EstadoBarraP.BAR_ESTADO_ERROR);
					progress_llamadas.setVisible(true);
					Mensaje ms = new Mensaje();
					ms.error("Cargar lista", "Error");
					progress_llamadas.setSelection(0);
					progress_llamadas.setState(EstadoBarraP.BAR_ESTADO_NORMAL);
					progress_llamadas.setVisible(false);
				}
			}
		});
		btncall.setBounds(428, 381, 88, 53);
		btncall.setText("Llamar");

		text_numero = new Text(shlSewhatel, SWT.BORDER);
		text_numero
				.setToolTipText("Formato celular: 549+area sin 0+numero sin 15\r\nFormato fijo: 54+area con 0+numero");
		text_numero.setBounds(10, 52, 185, 21);

		Button btnadd = new Button(shlSewhatel, SWT.NONE);
		btnadd.addMouseListener(new MouseAdapter() {
			public void mouseUp(MouseEvent e) {
				String numero = text_numero.getText();
				if (!numero.isEmpty() && numero.length() >= 12) {
					Numero nu = new Numero(numero);
					numeros.add(nu);
					TableItem tableItem = new TableItem(tabla_numeros, SWT.NONE);
					tableItem.setText(0, numero);
					text_numero.setText("");
				} else {
					Mensaje ms = new Mensaje();
					ms.error("Numero incorrecto", "Error");
				}
			}
		});
		btnadd.setBounds(201, 48, 75, 25);
		btnadd.setText("Agregar");

		tabla_numeros = new Table(shlSewhatel, SWT.BORDER | SWT.FULL_SELECTION);
		tabla_numeros.setBounds(10, 135, 401, 199);
		tabla_numeros.setHeaderVisible(true);
		tabla_numeros.setLinesVisible(true);

		tblclmnNumero = new TableColumn(tabla_numeros, SWT.LEFT);
		tblclmnNumero.setImage(SWTResourceManager.getImage(App.class,
				"/app/users.ico"));
		tblclmnNumero.setWidth(245);
		tblclmnNumero.setText("Numero");

		tblclmnEstado = new TableColumn(tabla_numeros, SWT.CENTER);
		tblclmnEstado.setImage(SWTResourceManager.getImage(App.class,
				"/app/status.png"));
		tblclmnEstado.setWidth(152);
		tblclmnEstado.setText("Estado");

		Button btnlista = new Button(shlSewhatel, SWT.NONE);
		btnlista.setImage(null);
		btnlista.addMouseListener(new MouseAdapter() {
			public void mouseUp(MouseEvent e) {
				Shell shell = new Shell();
				FileDialog fileDialog = new FileDialog(shell, SWT.MULTI);
				fileDialog.setFilterPath(filePath_inicio_win);
				fileDialog
						.setFilterExtensions(new String[] { "*.txt", "*.csv" });
				fileDialog.setFilterNames(new String[] { "txt", "csv" });
				String seleccioando = fileDialog.open();
				if (seleccioando != null) {
					file_seleccionado_path = seleccioando.toString();
					try {
						BufferedReader archivo = new BufferedReader(
								new FileReader(file_seleccionado_path));
						String linea;
						while ((linea = archivo.readLine()) != null) {
							Numero nu = new Numero(linea);
							numeros.add(nu);
							TableItem tableItem = new TableItem(tabla_numeros,
									SWT.NONE);
							tableItem.setText(0, linea);
						}
						archivo.close();
					} catch (FileNotFoundException e1) {
						// TODO Auto-generated catch block
						e1.printStackTrace();
					} catch (IOException e1) {
						// TODO Auto-generated catch block
						e1.printStackTrace();
					}
				}
			}
		});
		btnlista.setBounds(336, 104, 75, 25);
		btnlista.setText("Cargar Lista");

		Label lblNewLabel = new Label(shlSewhatel, SWT.NONE);
		lblNewLabel.setBounds(10, 32, 55, 15);
		lblNewLabel.setText("N\u00FAmero");

		Button btnLimpiar = new Button(shlSewhatel, SWT.NONE);
		btnLimpiar.addMouseListener(new MouseAdapter() {
			public void mouseUp(MouseEvent e) {
				tabla_numeros.removeAll();
				tabla_numeros.clearAll();
				numeros.removeAll(numeros);
				numeros.clear();
			}
		});
		btnLimpiar.setBounds(10, 340, 75, 25);
		btnLimpiar.setText("Limpiar");

		progress_llamadas = new ProgressBar(shlSewhatel, SWT.NONE);
		progress_llamadas.setBounds(10, 401, 402, 17);
		progress_llamadas.setVisible(false);

		Label lbl_config = new Label(shlSewhatel, SWT.NONE);
		lbl_config.addMouseListener(new MouseAdapter() {
			@Override
			public void mouseUp(MouseEvent e) {
				ConfigApp config = new ConfigApp(display);
				config.open();
			}
		});
		Image img = SWTResourceManager.getImage(App.class, "/app/config.png");
		lbl_config.setImage(Imagen.redimensionar(img, 80, 80));
		lbl_config.setAlignment(SWT.CENTER);
		lbl_config.setBounds(428, 10, 88, 80);
		lbl_config.setCursor(SWTResourceManager.getCursor(SWT.CURSOR_HAND));
	}
}
