package gui;

import java.io.IOException;
import java.util.Properties;

import org.eclipse.swt.SWT;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.swt.events.DisposeListener;
import org.eclipse.swt.events.DisposeEvent;
import org.eclipse.wb.swt.SWTResourceManager;

import util.CenterScreen;
import util.Configuracion;
import util.Mensaje;

import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Text;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.events.MouseAdapter;
import org.eclipse.swt.events.MouseEvent;

public class ConfigApp extends Shell {
	private Text text_host;

	/**
	 * Launch the application.
	 * @param args
	 */
	public static void main(String args[]) {
		try {
			Display display = Display.getDefault();
			ConfigApp shell = new ConfigApp(display);
			shell.open();
			shell.layout();
			while (!shell.isDisposed()) {
				if (!display.readAndDispatch()) {
					display.sleep();
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	/**
	 * Create the shell.
	 * @param display
	 */
	public ConfigApp(Display display) {
		super(display, SWT.CLOSE | SWT.TITLE);
		setImage(SWTResourceManager.getImage(ConfigApp.class, "/iconos/icon.ico"));
		CenterScreen cs = new CenterScreen();
		int screen[] = cs.get_xy(display, this);
		setLocation(screen[0], screen[1]);
		
		Label lblhost = new Label(this, SWT.NONE);
		lblhost.setBounds(10, 58, 55, 15);
		lblhost.setText("Servidor");
		
		text_host = new Text(this, SWT.BORDER);
		text_host.setBounds(74, 55, 127, 21);
		Configuracion c= new Configuracion();
		text_host.setText(c.leer().getProperty("host",""));
		Button btn_save = new Button(this, SWT.NONE);
		btn_save.addMouseListener(new MouseAdapter() {
			@Override
			public void mouseUp(MouseEvent e) {
				Configuracion cfg =new Configuracion();
				cfg.escribir("host", text_host.getText());
				Mensaje ms= new Mensaje();
				ms.info("Guardado", "Info");
			}
		});
		btn_save.setBounds(359, 214, 75, 47);
		btn_save.setText("Guardar");
		createContents();
	}

	/**
	 * Create contents of the shell.
	 */
	protected void createContents() {
		setText("Configuracion");
		setSize(450, 300);

	}

	@Override
	protected void checkSubclass() {
		// Disable the check that prevents subclassing of SWT components
	}
}
