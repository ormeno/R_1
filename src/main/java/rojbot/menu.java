package rojbot;

import java.io.File;
import java.io.FileInputStream;
import java.util.Iterator;

import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

public class menu {		

	public Iterator<Row> LeerFicherosExcel(String archivo) {
			
			String hoja = "Hoja1";
			Iterator<Row> rowIterator = null; 
			try (FileInputStream file = new FileInputStream(new File(archivo))) {
				// leer archivo excel
				XSSFWorkbook worbook = new XSSFWorkbook(file);
				//obtener la hoja que se va leer
				XSSFSheet sheet = worbook.getSheetAt(0);
				//obtener todas las filas de la hoja excel
				rowIterator = sheet.iterator();			
				
			} catch (Exception e) {
				e.getMessage();
			}
			return rowIterator;
		}
}
