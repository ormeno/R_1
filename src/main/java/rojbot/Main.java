package rojbot;

import java.net.Authenticator;
import java.net.PasswordAuthentication;

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
	
	private static String PROXY_HOST = "jcsrvprx02.ttec.es";
	private static Integer PROXY_PORT = 8080;
	private static String PROXY_USER = "ttec.es\roj";
	private static String PROXY_PASSWORD = "21553920";
    
	public static void main(String[] args) {
		
		// Create the Authenticator that will return auth's parameters for proxy authentication
        Authenticator.setDefault(new Authenticator() {
            @Override
            protected PasswordAuthentication getPasswordAuthentication() {
                return new PasswordAuthentication(PROXY_USER, PROXY_PASSWORD.toCharArray());
            }
        });
        
        // Initialize Api Context
        ApiContextInitializer.init();

        // Instantiate Telegram Bots API
        TelegramBotsApi botsApi = new TelegramBotsApi();
        
        // Set up Http proxy
        DefaultBotOptions botOptions = ApiContext.getInstance(DefaultBotOptions.class);          

        botOptions.setProxyHost(PROXY_HOST);
        botOptions.setProxyPort(PROXY_PORT);
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
        System.out.println("Successfully started!");       
    }
}