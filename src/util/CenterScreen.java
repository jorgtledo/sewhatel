package util;

import org.eclipse.swt.graphics.Rectangle;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Monitor;
import org.eclipse.swt.widgets.Shell;

public class CenterScreen {

	public CenterScreen() {
		// TODO Auto-generated constructor stub
	}

	public int[] get_xy(Display display, Shell shlSewhatel) {
		Monitor primary = display.getPrimaryMonitor();
		Rectangle bounds = primary.getBounds();
		Rectangle rect = shlSewhatel.getBounds();
		int x = bounds.x + (bounds.width - rect.width) / 2;
		int y = bounds.y + (bounds.height - rect.height) / 2;
		int[] xy = { x, y };
		return xy;
	}

}
