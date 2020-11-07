package pe.com.soaint.prueba;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotEquals;
import static org.junit.Assert.assertNull;

import java.sql.Connection;
import java.util.HashMap;
import java.util.Map;

import org.junit.Before;
import org.junit.Test;
import pe.com.soaint.DemoMejora;

public class TestDemoMejora {

	DemoMejora demo;
	
	
	@Before
	public void inicio() {
		boolean logError = false;
		boolean logMessage = true;
		boolean logWarning = false;
		boolean logToDatabase = false;
		boolean logToFile = false;
		boolean logToConsole = true;
        Map<String, String> dbParams = new HashMap<String, String>();
        dbParams.put("logFileFolder", "Ruta");
        dbParams.put("userName", "root");
        dbParams.put("password", "");
        dbParams.put("dbms", "mysql");
        dbParams.put("serverName", "localhost");
        dbParams.put("portNumber", "5432");
        
		demo = new DemoMejora(logToFile, logToConsole, logToDatabase, logMessage, logWarning, logError, dbParams);
	}
	
	@Test
	public void testValidaciones() {
		try {
			String msg = demo.validaciones("Mensaje ", true, false, false);
			assertEquals(msg, "Mensaje");
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	@Test
	public void testInstanciarBD() {
		Connection conn = demo.instanciarBD();
		assertNull(conn);
	}
	
	@Test
	public void testTipoLogMessage() {
		int tipo = demo.tipoLog(true, false, false);
		assertEquals(1, tipo);
	}
	
	@Test
	public void testTipoLogError() {
		int tipo = demo.tipoLog(false, false, true);
		assertNotEquals(2, tipo);
	}
	
	@Test
	public void testTipoLogWarning() {
		int tipo = demo.tipoLog(false, true, false);
		assertNotEquals(3, tipo);
	}

}
