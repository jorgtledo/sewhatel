package util;

import org.eclipse.swt.SWT;
import org.eclipse.swt.widgets.MessageBox;
import org.eclipse.swt.widgets.Shell;

public class Mensaje {

	public void error(String mensaje, String titulo) {
		Shell shell = new Shell();
		MessageBox creado = new MessageBox(shell, SWT.ICON_ERROR | SWT.OK);
		creado.setText(titulo);
		creado.setMessage(mensaje);
		creado.open();
	}

	public void info(String mensaje, String titulo) {
		Shell shell = new Shell();
		MessageBox creado = new MessageBox(shell, SWT.ICON_INFORMATION | SWT.OK);
		creado.setText(titulo);
		creado.setMessage(mensaje);
		creado.open();
	}

	public void ayuda(String mensaje, String titulo) {
		Shell shell = new Shell();
		MessageBox creado = new MessageBox(shell, SWT.ICON_QUESTION | SWT.OK);
		creado.setText(titulo);
		creado.setMessage(mensaje);
		creado.open();
	}

}
