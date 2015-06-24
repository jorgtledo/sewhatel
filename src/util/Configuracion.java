package util;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;

public class Configuracion {

	private Properties config;
	private File archivo;
	private FileOutputStream archivo_steam;
	private String filen = "config.properties";
	public void escribir(String key, String valor) {
		config = new Properties();
		archivo = new File(filen);
		try {
			archivo_steam = new FileOutputStream(archivo);
		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		config.setProperty(key, valor);
		try {
			config.store(archivo_steam, "config");
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		try {
			archivo_steam.close();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	public Properties leer() {
		archivo = new File(filen);
		Properties config = new Properties();
		FileInputStream prop;
		try {
			prop = new FileInputStream(archivo);
			if (prop != null) {
				config.load(prop);
			}
			prop.close();
		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return config;
	}

}
