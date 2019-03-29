package rojbot;

import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.Authenticator;
import java.net.PasswordAuthentication;
import java.util.Properties;

import org.apache.http.HttpHost;
import org.apache.http.auth.AuthScope;
import org.apache.http.auth.UsernamePasswordCredentials;
import org.apache.http.client.CredentialsProvider;
import org.apache.http.client.config.RequestConfig;
import org.apache.http.impl.client.BasicCredentialsProvider;
import org.telegram.telegrambots.ApiContextInitializer;
import org.telegram.telegrambots.bots.DefaultBotOptions;
import org.telegram.telegrambots.meta.ApiContext;
import org.telegram.telegrambots.meta.TelegramBotsApi;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;

public class Main2 {
		    
	public static void main(String[] args) {
		
		Properties prop = new Properties();	
		InputStream is = null;
		
		try {
			is = new FileInputStream("src/main/resources/configuracion.properties");
			prop.load(is);
		} catch(IOException e) {
			System.out.println(e.toString());
		}
				        
        // Initialize Api Context
        ApiContextInitializer.init();

        // Instantiate Telegram Bots API
        TelegramBotsApi botsApi = new TelegramBotsApi();
        
        Consultorio bot = new Consultorio();
        
        try {
            //botsApi.registerBot(new OrmenoBot());
            botsApi.registerBot(bot);                   
        } catch (TelegramApiException e) {
            e.printStackTrace();
        }
        System.out.println("Successfully started!!!!!!!!!!!");       
    }
}