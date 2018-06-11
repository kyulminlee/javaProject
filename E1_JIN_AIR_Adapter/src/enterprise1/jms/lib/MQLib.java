package enterprise1.jms.lib;

import java.util.Properties;
import javax.naming.Context;
import javax.naming.NamingException;
import javax.jms.Message;
import javax.jms.TextMessage;

public abstract class MQLib {

	public Properties p = new Properties();		
	public Context ctx;	
	public TextMessage JmsSndMsg;
	public TextMessage JmsRcvMsg;
	public Message jmsmsg;
	public static String INITIAL_CONTEXT_FACTORY = ""; 
	public static String CONNECTION_FACTORY="";
	public static String PROVIDER_URL = "";  
	public static String RCV_QUEUE="";
	public static String SND_QUEUE="";
	public static String ERR_QUEUE="";
	public static String AD_QUEUE="";
	public static String POLICY="";
	public static int  GET_WAIT_TIME=0;
	
	
	public abstract void init() throws NamingException;					//MQ Init	
	
	public abstract void release() throws Exception;					//Con release	

	public abstract void initSender() throws Exception;
	
	public abstract void inirErrSender() throws Exception;
	
	public abstract void initReceiver() throws Exception;
	
	public abstract void initRcvAdmin() throws Exception;
	
	public abstract void stopCheck() throws Exception;
	
	public abstract void msgSend(String msg) throws Exception;
	
	public abstract Context getInitialContext() throws Exception; 		// get InitialContext
	
	public abstract String msgRcvString() throws Exception;
	
	public abstract Message msgRcvMsg() throws Exception;
	
} //class ended

