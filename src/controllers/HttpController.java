package controllers;

import com.squareup.okhttp.Headers;
import com.squareup.okhttp.OkHttpClient;
import com.squareup.okhttp.Request;
import com.squareup.okhttp.Response;

import java.io.IOException;
import java.net.CookieManager;
import java.net.CookiePolicy;
import java.util.concurrent.TimeUnit;

public class HttpController {
	private OkHttpClient ok_clienthttp = new OkHttpClient();
	private CookieManager cm_cookies;
	private Headers.Builder h_headers = new Headers.Builder();
	private String respue;
	private final String getstr = "/call_encuesta.php?";
	private String host;

	public HttpController() {
		// TODO Auto-generated constructor stub
		ok_clienthttp.setConnectTimeout(2, TimeUnit.SECONDS); // connect timeout
		ok_clienthttp.setReadTimeout(10, TimeUnit.SECONDS);
		cm_cookies = new CookieManager();
		cm_cookies.setCookiePolicy(CookiePolicy.ACCEPT_ALL);
		h_headers.add("Accept-Language", "es-419,es;q=0.8,en;q=0.6");
		h_headers
				.add("User-Agent",
						"Mozilla/5.0 (Windows NT 6.3; rv:37.0) Gecko/20100101 Firefox/37.0");
		h_headers
				.add("Accept",
						"text/html,application/xhtml+xml,application/xml;q=0.9,*/*;q=0.8");
		h_headers.add("Referer", "/");
		respue = "";
	}

	public void set_host(String h) {
		host = h;
	}

	public String call_encuesta(String numero) throws IOException {
		Request peticion = new Request.Builder()
				.url("http://" + host + getstr + "numero=" + numero).get()
				.headers(h_headers.build()).build();
		Response resp_response = ok_clienthttp.newCall(peticion).execute();
		respue = resp_response.body().string();
		return respue;
	}
	
	public String check_linea() throws IOException {
		Request peticion = new Request.Builder()
				.url("http://" + host + "/line_status.php").get()
				.headers(h_headers.build()).build();
		Response resp_response = ok_clienthttp.newCall(peticion).execute();
		respue = resp_response.body().string();
		return respue;
	}

}
