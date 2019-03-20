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

public class Main {
		    
	public static void main(String[] args) {
		
		Properties prop = new Properties();	
		InputStream is = null;
		
		try {
			is = new FileInputStream("src/main/resources/configuracion.properties");
			prop.load(is);
		} catch(IOException e) {
			System.out.println(e.toString());
		}
		
		String TTEC_HOST = prop.getProperty("ttec.HOST");
		//String TTEC_HOST = "jcsrvprx02.ttec.es";
		Integer TTEC_PORT = Integer.parseInt(prop.getProperty("ttec.PORT"));			
		String TTEC_USER = prop.getProperty("ttec.USER");
		String TTEC_PASSWORD = prop.getProperty("ttec.PASSWORD");
		
		// Create the Authenticator that will return auth's parameters for proxy authentication
        Authenticator.setDefault(new Authenticator() {
            @Override
            protected PasswordAuthentication getPasswordAuthentication() {
                return new PasswordAuthentication(TTEC_USER, TTEC_PASSWORD.toCharArray());
            }
        });
        
        // Initialize Api Context
        ApiContextInitializer.init();

        // Instantiate Telegram Bots API
        TelegramBotsApi botsApi = new TelegramBotsApi();
        
        // Set up Http proxy
        DefaultBotOptions botOptions = ApiContext.getInstance(DefaultBotOptions.class);          

        botOptions.setProxyHost(TTEC_HOST);
        botOptions.setProxyPort(TTEC_PORT);
        // Select proxy type: [HTTP|SOCKS4|SOCKS5] (default: NO_PROXY)
        botOptions.setProxyType(DefaultBotOptions.ProxyType.HTTP);
       
        // Register your newly created AbilityBot
        //OrmenoBot bot = new OrmenoBot(botOptions);
        //PhotoBot bot = new PhotoBot(botOptions);
        Consultorio bot = new Consultorio(botOptions);
        //BotApi20 bot = new BotApi20(botOptions);
        //LoggingTestBot bot = new LoggingTestBot(botOptions);
        //EmojiTestBot bot = new EmojiTestBot(botOptions);        
        // Register our bot
        try {
            //botsApi.registerBot(new OrmenoBot());
            botsApi.registerBot(bot);                   
        } catch (TelegramApiException e) {
            e.printStackTrace();
        }
        System.out.println("Successfully started!!!!!!!!!!!");       
    }
}