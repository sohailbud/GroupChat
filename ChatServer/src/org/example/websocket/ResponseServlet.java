package org.example.websocket;

import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLConnection;
import java.util.HashMap;
import java.util.Map;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.google.gson.Gson;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;

/**
 * Servlet implementation class ResponseServlet
 */
@WebServlet("/ResponseServlet")
public class ResponseServlet extends HttpServlet {
	private static final long serialVersionUID = 1L;
	private static final String API_KEY = "key=AIzaSyA8Z_D_201qdMsQFVfGxhpqrM5PVDOHIZI";
	       
    /**
     * @see HttpServlet#HttpServlet()
     */
    public ResponseServlet() {
        super();
        // TODO Auto-generated constructor stub
    }

	/**
	 * @see HttpServlet#doGet(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		// TODO Auto-generated method stub
		response.getWriter().write("Hello World");
	}

	/**
	 * @see HttpServlet#doPost(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		// TODO Auto-generated method stub
		System.out.println("TOKEN RECEIVED: " + response);
		
		Gson gson=new Gson(); 
		String json="{\"data\": {\"title\": \"Test Title\",\"body\": \"Test Body\"},\"to\":\"dXEiXj4cwuk:APA91bHRBeVvLA0cvFMeKaWRm81VGIFSOYCJa4T90OEUjxz0D3LMLS3oxNnjOJkLkodLoSlNMYxIQN4_jHB2aZ5rs3US8KqGKSc_x3fo93mjfhQLyVdf1dQi9sttrVEtJM7dAHngHHwj\"}";
//		Map<String,String> map=new HashMap<String,String>();
//		map=(Map<String,String>) gson.fromJson(json, map.getClass());
		
		try {
			URL url = new URL("https://gcm-http.googleapis.com/gcm/send");
			HttpURLConnection connection = (HttpURLConnection) url.openConnection();

			connection.setRequestMethod("POST");
			connection.setRequestProperty("Authorization", API_KEY);
			connection.setRequestProperty("Content-Type", "application/json");

			//send post request
			connection.setDoOutput(true);
			DataOutputStream outputStream = new DataOutputStream(connection.getOutputStream());

			outputStream.write(json.getBytes("UTF-8"));
			outputStream.flush();
			outputStream.close();

			int responseCode = connection.getResponseCode();
			System.out.println("Sending 'POST' request to URL : " + url);
			System.out.println("Response Code : " + responseCode);
			
			BufferedReader bufferedReader = new BufferedReader(
                    new InputStreamReader(connection.getInputStream()));
            String inputLine;
            StringBuffer resp = new StringBuffer();
            while ((inputLine = bufferedReader.readLine()) != null)
                resp.append(inputLine);

            bufferedReader.close();

            //print result
            System.out.println(resp.toString());
			
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
}












