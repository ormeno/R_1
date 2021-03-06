package rojbot;

import org.telegram.telegrambots.meta.api.methods.send.SendInvoice;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.methods.AnswerCallbackQuery;
import org.telegram.telegrambots.meta.api.methods.AnswerPreCheckoutQuery;
import org.telegram.telegrambots.meta.api.methods.ForwardMessage;
import org.telegram.telegrambots.meta.api.methods.ParseMode;
import org.telegram.telegrambots.meta.api.methods.send.SendPhoto;
import org.telegram.telegrambots.meta.api.methods.updatingmessages.EditMessageText;
import org.telegram.telegrambots.meta.api.objects.PhotoSize;
import org.telegram.telegrambots.meta.api.objects.Update;
import org.telegram.telegrambots.meta.api.objects.payments.PreCheckoutQuery;
import org.telegram.telegrambots.meta.api.objects.payments.LabeledPrice;
import org.telegram.telegrambots.meta.api.objects.CallbackQuery;
import org.telegram.telegrambots.meta.api.objects.Message;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.InlineKeyboardMarkup;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.ReplyKeyboardMarkup;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.ReplyKeyboardRemove;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.buttons.InlineKeyboardButton;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.buttons.KeyboardButton;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.buttons.KeyboardRow;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Iterator;

import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Row;
import org.telegram.telegrambots.bots.DefaultBotOptions;
import org.telegram.telegrambots.bots.TelegramLongPollingBot;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;

import static java.lang.Math.toIntExact;

public class Consultorio extends TelegramLongPollingBot {
		
	
	protected Consultorio(DefaultBotOptions botOptions) {
        super(botOptions);
    }

	public Consultorio() {
		// TODO Auto-generated constructor stub
	}

	CallbackQuery respuesta = new CallbackQuery();    
	ForwardMessage f = new ForwardMessage();

	
    @SuppressWarnings("unused")
	@Override
    public void onUpdateReceived(Update update) {    
    	    	    
    	// We check if the update has a message and the message has text
        if (update.hasMessage() && update.getMessage().hasText()) {
            // Set variables        	
            String message_text = update.getMessage().getText();
            String user_first_name = update.getMessage().getChat().getFirstName();
            String user_last_name = update.getMessage().getChat().getLastName();
            String user_username = update.getMessage().getChat().getUserName();            
            long user_id = update.getMessage().getChat().getId();
            long chat_id = update.getMessage().getChatId();                        
            Iterator<Row> rowIterator = null;            
            
            //System.out.println("message_text -> " + message_text);                                              
            if (message_text.equals("/start")) {
                // User send /start            	
            	log(user_username, user_first_name, user_last_name, Long.toString(user_id), message_text, message_text);
            	// comprobar si usuario existe, so no lo registro en la tabla de usuarios            
            	database db = new database();
                int existe = db.usuarioExiste(user_id);
                if ( existe == 0 ) {
                  db.altaUsuario(user_id, user_username, user_first_name, user_last_name);
                }
            	String msg = "Bienvenido " + user_username + ", ¿estas preparado para responder todas las preguntas que puedas? (Si|No) ";			    			    			  
			    // Add it to the message			    			   
                SendMessage message = new SendMessage() // Create a message object object
                                .setChatId(chat_id)                                
                                .setText(msg);                                
                
                List<LabeledPrice> prices1 = new ArrayList<>();
                prices1.add(new LabeledPrice("label", 100));
                                
                SendInvoice invoice = new SendInvoice()
                		               .setChatId(Integer.parseInt(message.getChatId().toString()))
                		               .setTitle("PRUEBA CON EL SUPER TEST")
                		               .setDescription("El primero que lo consigue gana 1000€€€!!!")
                		               .setPayload("factura_001")                		                   		                                  
                		               .setProviderToken("284685063:TEST:NzBhNDNjZjU1NGFl")
                		               .setStartParameter("u4u-invoice-0001")
                		               .setCurrency("EUR")                		                               		              
                		               .setPrices(prices1);
                                                          
                try {
                    execute(message); // Sending our message object to user
                    execute(invoice);                
                    
                } catch (TelegramApiException e) {
                    e.printStackTrace();
                }
            } else if (message_text.toUpperCase().equals("SI") || message_text.toUpperCase().equals("S") ) {
			    SendMessage msg2 = new SendMessage()
		                .setChatId(chat_id)
		                .setText("VAMOS!!");			    
			    InlineKeyboardMarkup markupInline = new InlineKeyboardMarkup();
                List<List<InlineKeyboardButton>> rowsInline = new ArrayList<>();
                List<InlineKeyboardButton> rowInline = new ArrayList<>();
                rowInline.add(new InlineKeyboardButton().setText("Pulsa para empezar").setCallbackData("preguntaInicial"));
                // Set the keyboard to the markup
                rowsInline.add(rowInline);
                // Add it to the message
                markupInline.setKeyboard(rowsInline);
                msg2.setReplyMarkup(markupInline);
			    try {
			        execute(msg2);
			    } catch (TelegramApiException e) {
			        e.printStackTrace();
			    }		    
			}  else if (message_text.toUpperCase().equals("NO") || message_text.toUpperCase().equals("N") ) {
				SendMessage msg = new SendMessage()
		                .setChatId(chat_id)
		                .setText("Ok. Hasta pronto");		                
			    try {
			        execute(msg); // Call method to send the photo
			    } catch (TelegramApiException e) {
			        e.printStackTrace();
			    }		    
			} else if (message_text.toUpperCase().equals("/MENU") ) {
								
			    ReplyKeyboardMarkup keyboardMarkup = new ReplyKeyboardMarkup();
			    List<KeyboardRow> keyboard = new ArrayList<>();
			    KeyboardRow rowBotonera = new KeyboardRow();
			    // Set each button, you can also use KeyboardButton objects if you need something else than text
			    rowBotonera.add("Hoy");
			    rowBotonera.add("Semana");
			    rowBotonera.add("Mes");
			    // Add the first row to the keyboard
			    keyboard.add(rowBotonera);
			    keyboardMarkup.setKeyboard(keyboard); 			   			    
								
				SendMessage msg = new SendMessage()
		                .setChatId(chat_id)
		                .setReplyMarkup(keyboardMarkup)
		                .setText("Elige una opción: ");		                		               
			    try {
			        execute(msg); // Call method to send the photo
			    } catch (TelegramApiException e) {
			        e.printStackTrace();
			    }		    
			} else if (message_text.equals("Hoy")) {
				
				String finalMessage = new String("<b> MENU DE HOY </b> \n");
				finalMessage = finalMessage.concat("------------------------------------------- \n");
				menu men = new menu();												
				String nombreArchivo = "menu.xlsx";
				// String rutaArchivo = "D:\\varios\\Berta\\" + nombreArchivo;
				String rutaArchivo = ".\\src\\main\\resources\\" + nombreArchivo;				
				rowIterator = men.LeerFicherosExcel(rutaArchivo);

				Row row;
				int numRow = 0;															
				Calendar calendar = Calendar.getInstance();
				int diaMes = calendar.get(Calendar.DAY_OF_MONTH);
				
				while (rowIterator.hasNext()) {					
					row = rowIterator.next();
					numRow++;					
					Iterator<Cell> cellIterator = row.cellIterator();
					Cell cell;
					int numCol = 0;
					boolean pintaLin = false; 
					//se recorre cada celda
					while (cellIterator.hasNext()) {
						// se obtiene la celda en específico						
						cell = cellIterator.next();
						numCol++;
						// Ponemos nombre del mes
						if (numRow > 5 && numCol == 1 && cell.getNumericCellValue() == diaMes) {
							pintaLin = true;
						}
					    if (pintaLin && numCol == 3) finalMessage = finalMessage.concat(" 1º -> " + cell.getStringCellValue() + " \n");
					    if (pintaLin && numCol == 4) finalMessage = finalMessage.concat(" 2º -> " + cell.getStringCellValue() + " \n");
					    if (pintaLin && numCol == 5) finalMessage = finalMessage.concat(" Postre -> " + cell.getStringCellValue() + " \n");
					}																		
				}											
				
				SendMessage msg = new SendMessage()
		                .setChatId(chat_id)
		                .setParseMode(ParseMode.HTML)
		                .setText(finalMessage);
				try {
			        execute(msg); // Call method to send the photo
			    } catch (TelegramApiException e) {
			        e.printStackTrace();
			    }
			} else if (message_text.equals("Semana")) {
				
				String finalMessage = new String("<b> MENU DE LA SEMANA </b> \n");
				finalMessage = finalMessage.concat("------------------------------------------- \n");
				menu men = new menu();								
				String nombreArchivo = "menu.xlsx";
				//String rutaArchivo = "D:\\varios\\Berta\\" + nombreArchivo;				
				String rutaArchivo = ".\\src\\main\\resources\\" + nombreArchivo;
				rowIterator = men.LeerFicherosExcel(rutaArchivo);

				Row row;
				int numRow = 0;															
				Calendar calendar = Calendar.getInstance();
				int diaMes = calendar.get(Calendar.DAY_OF_MONTH);
				int diaSemana = calendar.get(Calendar.DAY_OF_WEEK)-1; if (diaSemana == 0) diaSemana = 7;  
				//int primerDiaSemana = calendar.getFirstDayOfWeek();
				int primerDiaSemana = 1;
				int diaLunesSemAct = diaMes - (diaSemana - primerDiaSemana); 
				
				while (rowIterator.hasNext()) {					
					row = rowIterator.next();
					numRow++;					
					Iterator<Cell> cellIterator = row.cellIterator();
					Cell cell;
					int numCol = 0;
					boolean pintaLin = false; 
					//se recorre cada celda
					while (cellIterator.hasNext()) {
						// se obtiene la celda en específico						
						cell = cellIterator.next();
						numCol++;
						// Ponemos nombre del mes
						if (numRow > 5 && numCol == 1 && cell.getNumericCellValue() >= diaLunesSemAct && cell.getNumericCellValue() < diaLunesSemAct + 5)  {
							pintaLin = true;							
						}
						if (pintaLin && numCol == 2) finalMessage = finalMessage.concat("<b>" + cell.getStringCellValue() + "</b>  \n");
					    if (pintaLin && numCol == 3) finalMessage = finalMessage.concat(" 1º -> " + cell.getStringCellValue() + " \n");
					    if (pintaLin && numCol == 4) finalMessage = finalMessage.concat(" 2º -> " + cell.getStringCellValue() + " \n");
					    if (pintaLin && numCol == 5) finalMessage = finalMessage.concat(" Postre -> " + cell.getStringCellValue() + " \n");    					    
					}																		
				}											
				
				SendMessage msg = new SendMessage()
		                .setChatId(chat_id)
		                .setParseMode(ParseMode.HTML)
		                .setText(finalMessage);
				try {
			        execute(msg); // Call method to send the photo
			    } catch (TelegramApiException e) {
			        e.printStackTrace();
			    }
			} else if (message_text.equals("Mes") ) {
								
				//String finalMessage = new String("<b> MENU DEL MES DE ");
				menu men = new menu();								
				String nombreArchivo = "menu.xlsx";
				//String rutaArchivo = "D:\\varios\\Berta\\" + nombreArchivo;
				String rutaArchivo = ".\\src\\main\\resources\\" + nombreArchivo;
				Iterator<Row> rowIt1 = null;
				rowIt1 = men.LeerFicherosExcel(rutaArchivo);
				Iterator<Row> rowIt2 = null; 
				rowIt2 = men.LeerFicherosExcel(rutaArchivo);				   			

				String cabecera = new String("<b> MENU DEL MES </b> \n");
				cabecera = cabecera.concat("------------------------------------------- \n");
				String quincena1 = menuQuincena(rowIt1,6);
				String quincena2 = menuQuincena(rowIt2,36);
				
				
				SendMessage msgCabecera = new SendMessage()
		                .setChatId(chat_id)
		                .setParseMode(ParseMode.HTML)
		                .setText(cabecera);
				SendMessage msgQuincena1 = new SendMessage()
		                .setChatId(chat_id)
		                .setParseMode(ParseMode.HTML)
		                .setText(quincena1);
				SendMessage msgQuincena2 = new SendMessage()
		                .setChatId(chat_id)
		                .setParseMode(ParseMode.HTML)
		                .setText(quincena2);
				try {
					execute(msgCabecera);
			        execute(msgQuincena1); 
			        execute(msgQuincena2);
			    } catch (TelegramApiException e) {
			        e.printStackTrace();
			    }		
            }else {
                // Unknown command            	            			                    
                SendMessage message = new SendMessage() // Create a message object object
                                .setChatId(chat_id)
                                .setText("No entindo, ¿quieres responder a las preguntas? (Si|No) ");
                
                List<LabeledPrice> prices1 = new ArrayList<>();
                prices1.add(new LabeledPrice("label", 100));
                                
                SendInvoice invoice = new SendInvoice()
                		               .setChatId(Integer.parseInt(message.getChatId().toString()))
                		               .setTitle("PRUEBA CON EL SUPER TEST")
                		               .setDescription("El primero que lo consigue gana 1000€€€!!!")
                		               .setPayload("factura_001")                		               
                		               .setProviderToken("284685063:TEST:NzBhNDNjZjU1NGFl")
                		               .setStartParameter("u4u-invoice-0001")
                		               .setCurrency("EUR")                		                               		              
                		               .setPrices(prices1);                
                try {
                    execute(message); // Sending our message object to user
                    execute(invoice);
                } catch (TelegramApiException e) {
                    e.printStackTrace();
                }
            }            
        }else if (update.hasCallbackQuery()) {
            // Set variables
            String call_data = update.getCallbackQuery().getData();
            int posIni = 0;
            int posFin = 0;
            int finCad = 0;
            String cd_res = null; 
            String cd_idPreg = null;
            String cd_resOk = null;
            if (!call_data.equals("preguntaInicial")) {
	            posIni = call_data.indexOf("_");
	            posFin = call_data.indexOf("_", posIni + 1);
	            finCad = call_data.length();
	            cd_res = call_data.substring(0,posIni);
	            cd_idPreg = call_data.substring(posIni+1,posFin);
	            cd_resOk = call_data.substring(posFin+1,finCad);
            }
            long message_id = update.getCallbackQuery().getMessage().getMessageId();
            long chat_id = update.getCallbackQuery().getMessage().getChatId();

            if (call_data.equals("preguntaInicial")) {
            	database db = new database();
                Connection con = db.connect();
                Pregunta preg = db.obtenPregunta(con, 3);
                EditMessageText res_pregunta = new EditMessageText()
	                                .setChatId(chat_id)
	                                .setMessageId(toIntExact(message_id))
	                                .setText(preg.getPregunta());                         
	            annadirSiNo_DatosPreg(res_pregunta, preg); 	            		
	            try {
	              execute(res_pregunta);                    
	            } catch (TelegramApiException e) {
	              e.printStackTrace();
	            }	            	                          
            }  else if (cd_res.equals(cd_resOk)) {  
            	// obtener siguiente pregunta            
            	database db = new database();
                Connection con = db.connect();
                Pregunta preg = db.obtenPregunta(con, Integer.parseInt(cd_idPreg) + 1);
                EditMessageText res_pregunta = new EditMessageText();
                if (preg == null ) {
                	res_pregunta.setChatId(chat_id)
                            .setMessageId(toIntExact(message_id))
                            .setText("¡¡ ENHORABUENA !! ¿quieres volver a empezar? (Si|No) ");
                	
                } else {
                	res_pregunta.setChatId(chat_id)
                            .setMessageId(toIntExact(message_id))
                            .setText(preg.getPregunta());
                	annadirSiNo_DatosPreg(res_pregunta, preg);                	
                }                                                                                              
                try {
                	execute(res_pregunta);                    
                } catch (TelegramApiException e) {
                	e.printStackTrace();
                }
            } else {
            	SendMessage msg = new SendMessage()
		                .setChatId(chat_id)
		                .setText("¡¡¡ MAL !!! ¿Quieres volver a empezar? (Si|No)");
            	try {
                	execute(msg);                    
                } catch (TelegramApiException e) {
                	e.printStackTrace();
                }
            }
        }else if (update.getPreCheckoutQuery() != null) {
        	
        	String s1 = update.getPreCheckoutQuery().getInvoicePayload();
        	String idCheck =update.getPreCheckoutQuery().getId();
        	System.out.println("s1: " + s1);
        	System.out.println("s2: " + idCheck);
        	AnswerPreCheckoutQuery answerCheckout = new AnswerPreCheckoutQuery();
        	answerCheckout.setPreCheckoutQueryId(idCheck);
        	answerCheckout.setOk(true);
        	try {
            	execute(answerCheckout);                    
            } catch (TelegramApiException e) {
            	e.printStackTrace();
            }
        	
        }else if (update.hasMessage() && update.getMessage().hasPhoto()) {
            // Message contains photo
        	// Set variables
            long chat_id = update.getMessage().getChatId();

            // Array with photo objects with different sizes
            // We will get the biggest photo from that array
            List<PhotoSize> photos = update.getMessage().getPhoto();
            // Know file_id
            String f_id = photos.stream()
                            .sorted(Comparator.comparing(PhotoSize::getFileSize).reversed())
                            .findFirst()
                            .orElse(null).getFileId();
            // Know photo width
            int f_width = photos.stream()
                            .sorted(Comparator.comparing(PhotoSize::getFileSize).reversed())
                            .findFirst()
                            .orElse(null).getWidth();
            // Know photo height
            int f_height = photos.stream()
                            .sorted(Comparator.comparing(PhotoSize::getFileSize).reversed())
                            .findFirst()
                            .orElse(null).getHeight();
            // Set photo caption
            String caption = "file_id: " + f_id + "\nwidth: " + Integer.toString(f_width) + "\nheight: " + Integer.toString(f_height);
            SendPhoto msg = new SendPhoto()
                            .setChatId(chat_id)
                            .setPhoto(f_id)
                            .setCaption(caption);
            try {
                execute(msg); // Call method to send the photo with caption
            } catch (TelegramApiException e) {
                e.printStackTrace();
            }
        }
    }
 
    
    private String menuQuincena(Iterator<Row> rowIterator, int linInicio) {
    	
    	String finalMessage = "";
    	Row row;
		int numRow = 0;					
		boolean pintaLin = true;
		// se recorre cada fila hasta el final
		while (rowIterator.hasNext()) {					
				row = rowIterator.next();
				numRow++;
				//se obtiene las celdas por fila
				Iterator<Cell> cellIterator = row.cellIterator();
				Cell cell;
				int numCol = 0;
				pintaLin = true; 
				//se recorre cada celda
				while (cellIterator.hasNext()) {
					// se obtiene la celda en específico						
					cell = cellIterator.next();
					numCol++;					
					// Ponemos el menu de cada semana del mes
					if (numRow >= linInicio && numRow < linInicio + 30) {											
							if (numCol == 1) {
								finalMessage = finalMessage.concat("<b> Día </b>" + Math.round(cell.getNumericCellValue()) + " - ");
							}
							if (numCol == 2) {
								if (!cell.getStringCellValue().equals("Sábado") && !cell.getStringCellValue().equals("Domingo")) {
								  finalMessage = finalMessage.concat("<b>" + cell.getStringCellValue() + "</b> \n");								  
								} else{ 
									finalMessage = finalMessage.concat("<b>" + cell.getStringCellValue() + " ***************** </b> \n");
									pintaLin = false;									
								}  
							}
							if (numCol == 3 && pintaLin) {
								finalMessage = finalMessage.concat(" 1º-> " + cell.getStringCellValue() + " - ");
							}
							if (numCol == 4 && pintaLin) {
								finalMessage = finalMessage.concat(" 2º-> " + cell.getStringCellValue() + " - ");
							}
							if (numCol == 5 && pintaLin) {
								finalMessage = finalMessage.concat(" Postre-> " + cell.getStringCellValue() + " \n");
							}
					}				
				  }				    						
			}						
		return finalMessage;
    }
    private void annadirSiNo_DatosPreg(EditMessageText msg,Pregunta preg) {
    	InlineKeyboardMarkup markupInline = new InlineKeyboardMarkup();
        List<List<InlineKeyboardButton>> rowsInline = new ArrayList<>();
        List<InlineKeyboardButton> rowInline = new ArrayList<>();
        rowInline.add(new InlineKeyboardButton().setText("  Si  ").setCallbackData("S" + "_" + preg.getId() + "_" + preg.getRespuesta()));
        rowInline.add(new InlineKeyboardButton().setText("  No  ").setCallbackData("N" + "_" + preg.getId() + "_" + preg.getRespuesta()));
        // Set the keyboard to the markup
        rowsInline.add(rowInline);
        // Add it to the message
        markupInline.setKeyboard(rowsInline);
        msg.setReplyMarkup(markupInline);        
    }
    private void log(String user_name, String first_name, String last_name, String user_id, String txt, String bot_answer) {
        System.out.println("\n ----------------------------");
        DateFormat dateFormat = new SimpleDateFormat("yyyy/MM/dd HH:mm:ss");
        Date date = new Date();
        System.out.println(dateFormat.format(date));
        System.out.println("Message from " + user_name + " - " + first_name + " " + last_name + ". (id = " + user_id + ") \n Text - " + txt);
        System.out.println("Bot answer: \n Text - " + bot_answer);
    }
    
    @Override
    public String getBotUsername() {
        // Return bot username
        // If bot username is @MyAmazingBot, it must return 'MyAmazingBot'
        return "OrmenoBot";
    }

    @Override
    public String getBotToken() {
        // Return bot token from BotFather
        return "584953347:AAHVkaPORXeYADLarPmKEOAR2WKyW_sLiUU";
    }
}
