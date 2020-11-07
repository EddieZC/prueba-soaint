package pe.com.soaint;

import java.io.File;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.sql.Statement;
import java.text.DateFormat;
import java.util.Date;
import java.util.Map;
import java.util.Properties;
import java.util.logging.ConsoleHandler;
import java.util.logging.FileHandler;
import java.util.logging.Level;
import java.util.logging.Logger;

public class DemoMejora {
    private static boolean logToFile;
    private static boolean logToConsole;
    private static boolean logMessage;
    private static boolean logWarning;
    private static boolean logError;
    private static boolean logToDatabase;
    private boolean initialized;
    private static Map dbParams;
    private static Logger logger;
    
    private static Connection connection;
    private static Statement stmt;

    public DemoMejora(boolean logToFileParam, boolean logToConsoleParam, boolean logToDatabaseParam,
                boolean logMessageParam, boolean logWarningParam, boolean logErrorParam, Map dbParamsMap) {
        logger = Logger.getLogger("MyLog");
        logError = logErrorParam;
        logMessage = logMessageParam;
        logWarning = logWarningParam;
        logToDatabase = logToDatabaseParam;
        logToFile = logToFileParam;
        logToConsole = logToConsoleParam;
        dbParams = dbParamsMap;
        connection = instanciarBD();
    }

    public void LogMessage(String messageText, boolean message, boolean warning, boolean error) throws Exception{
        
    	messageText = validaciones(messageText, message, warning, error);
    	
    	int t = tipoLog(message, warning, error);
    	
    	File logFile = new File(dbParams.get("logFileFolder") + "/logFile.txt");
    	if (!logFile.exists()) {
    		logFile.createNewFile();
    	}
    	
    	FileHandler fh = new FileHandler(dbParams.get("logFileFolder") + "/logFile.txt");
    	ConsoleHandler ch = new ConsoleHandler();
    	
    	messageText = mensajeFinal(messageText, message, warning, error);
    	
    	if(logToFile) {
    		logger.addHandler(fh);
    		logger.log(Level.INFO, messageText);
    	}
    	
    	if(logToConsole) {
    		logger.addHandler(ch);
    		logger.log(Level.INFO, messageText);
    	}
    	
    	if(logToDatabase) {
    		stmt.executeUpdate("insert into Log_Values('" + message + "', " + t + ")");
    	}
    	if(stmt!=null)
    		stmt.close();
    	if(connection!=null)
    		connection.close();
    	
    }
    
    
    public String validaciones(String messageText, boolean message, boolean warning, boolean error) throws Exception {
    	if (messageText == null || messageText.length() == 0) {
    		 throw new Exception("Invalid Message");
        }
    	if (!logToConsole && !logToFile && !logToDatabase) {
            throw new Exception("Invalid configuration");
        }
        if ((!logError && !logMessage && !logWarning) || (!message && !warning && !error)) {
            throw new Exception("Error or Warning or Message must be specified");
        }
        return messageText.trim();
    }
    
    public Connection instanciarBD() {
    	Connection connection = null;
    	try {
	        Properties connectionProps = new Properties();
	        connectionProps.put("user", dbParams.get("userName"));
	        connectionProps.put("password", dbParams.get("password"));

			connection = DriverManager.getConnection("jdbc:" + dbParams.get("dbms") + "://" + dbParams.get("serverName")
			        + ":" + dbParams.get("portNumber") + "/", connectionProps);	
			
			stmt = connection.createStatement();
		} catch (SQLException e) {
			e.printStackTrace();
		}
    	return connection;
    }
    
    public int tipoLog(boolean message, boolean warning, boolean error) {
    	int t=0;
    	if (message && logMessage) {
            t = 1;
        }

        if (error && logError) {
            t = 2;
        }

        if (warning && logWarning) {
            t = 3;
        }
        return t;
    }
    
    public String mensajeFinal(String messageText, boolean message, boolean warning, boolean error) {
    	String l = "";
        if (error && logError) {
            l = l + "error " + DateFormat.getDateInstance(DateFormat.LONG).format(new Date()) + messageText;
        }

        if (warning && logWarning) {
            l = l + "warning " +DateFormat.getDateInstance(DateFormat.LONG).format(new Date()) + messageText;
        }

        if (message && logMessage) {
            l = l + "message " +DateFormat.getDateInstance(DateFormat.LONG).format(new Date()) + messageText;
        }
    	return l;
    }
    
}
