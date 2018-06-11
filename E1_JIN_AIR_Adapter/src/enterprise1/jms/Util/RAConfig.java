package enterprise1.jms.Util;

import org.apache.commons.configuration.ConfigurationException;
import org.apache.commons.configuration.XMLConfiguration;

public class RAConfig {

	// CFG_INFO
	private static String VERSION;
	private static String CREATE_AUTHOR;
	private static String CREATE_DATE;
	private static String MODIFY_AUTHOR;
	private static String MODIFY_DATE;
	private static String DOCUMENT_NAME;
	private static String API;

	// BASE_INFO
	private static int THREAD_COUNT;
	private static String LOG_LEVEL;
	private static String POLICY;

	// SOURCE_INFO
	private static String SOURCE_VENDER;
	private static String SOURCE_INITIAL_CONTEXT_FACTORY;
	private static String SOURCE_PROVIDER_URL;
	private static String SOURCE_CONNECTION_FACTORY;
	private static String SOURCE_QUEUE_NAME;
	private static int  GET_WAIT_TIME;
	private static String ERR_QNAME;
	private static String ADMIN_QNAME;

	// TARGET_INFO
	private static String TARGET_VENDER;
	private static String TARGET_INITIAL_CONTEXT_FACTORY;
	private static String TARGET_PROVIDER_URL;
	private static String TARGET_CONNECTION_FACTORY;
	private static String TARGET_QUEUE_NAME;

	public static RAConfig instance=null;
	
	public RAConfig() throws Exception{
	    init();
	}
	
	public void init() throws Exception {
		// xml 파일 파싱해서 값세팅
		XMLConfiguration config;
//		static String configFile = "C:\\Users\\jge\\JMS_ESB\\MyTest\\src\\ConfigTest\\RAConfig.xml";
		String configFile = "RAConfig.xml";
		
		try{
			config = new XMLConfiguration();
			config.setFileName(configFile);
			config.setDelimiterParsingDisabled(false);
			config.load();
			
			//CFG_INFO
			VERSION 								= config.getString("CFG_INFO.VERSION");
			CREATE_AUTHOR 							= config.getString("CFG_INFO.CREATE_AUTHOR");
			CREATE_DATE 							= config.getString("CFG_INFO.CREATE_DATE");
			MODIFY_AUTHOR 							= config.getString("CFG_INFO.MODIFY_AUTHOR");
			MODIFY_DATE 							= config.getString("CFG_INFO.MODIFY_DATE");
			DOCUMENT_NAME 							= config.getString("CFG_INFO.DOCUMENT_NAME");
			API 									= config.getString("CFG_INFO.API");
			                					
			//BASE_INFO     					
			THREAD_COUNT							= config.getInt("BASE_INFO.THREAD_COUNT");
			System.out.println("THREAD_COUNT:" + THREAD_COUNT);
			LOG_LEVEL								= config.getString("BASE_INFO.LOG_LEVEL");
			POLICY									= config.getString("BASE_INFO.POLICY");
			
			//SOURCE_INFO
			SOURCE_VENDER							= config.getString("SOURCE_INFO.VENDER");
			SOURCE_INITIAL_CONTEXT_FACTORY			= config.getString("SOURCE_INFO.INITIAL_CONTEXT_FACTORY");
			SOURCE_PROVIDER_URL						= config.getString("SOURCE_INFO.PROVIDER_URL");
			SOURCE_CONNECTION_FACTORY				= config.getString("SOURCE_INFO.CONNECTION_FACTORY");
			SOURCE_QUEUE_NAME						= config.getString("SOURCE_INFO.QUEUE_NAME");
			GET_WAIT_TIME							= config.getInt("SOURCE_INFO.GET_WAIT_TIME");
			ERR_QNAME								= config.getString("SOURCE_INFO.ERR_QNAME");
			ADMIN_QNAME								= config.getString("SOURCE_INFO.ADMIN_QNAME");
			
			//TARGET_INFO
			TARGET_VENDER							= config.getString("TARGET_INFO.VENDER");
			TARGET_INITIAL_CONTEXT_FACTORY			= config.getString("TARGET_INFO.INITIAL_CONTEXT_FACTORY");
			TARGET_PROVIDER_URL						= config.getString("TARGET_INFO.PROVIDER_URL");
			TARGET_CONNECTION_FACTORY				= config.getString("TARGET_INFO.CONNECTION_FACTORY");
			TARGET_QUEUE_NAME						= config.getString("TARGET_INFO.QUEUE_NAME");
			
			
		}catch(ConfigurationException e){
			//파일 로드할 때 발생하는 익셉션인듯? getstatic String 할 때는 익셉션 발생이 안된다.
			e.printStackTrace();
		}
	}


	  
	public static RAConfig getInstance() throws Exception{
		  if (instance == null){
			  instance=new RAConfig();
		  }
		  return instance;
	  }
	
	public void validCheck(){
		//config 값이 유효여부 체크
	}
	
	public static String getVersion() {
		return VERSION;
	}

	public static String getCreateAuthor() {
		return CREATE_AUTHOR;
	}

	public static String getCreateDate() {
		return CREATE_DATE;
	}

	public static String getModifyAuthor() {
		return MODIFY_AUTHOR;
	}

	public static String getModifyDate() {
		return MODIFY_DATE;
	}

	public static String getDocumentName() {
		return DOCUMENT_NAME;
	}

	public static String getApi() {
		return API;
	}

	public static int getThreadCount() {
		return THREAD_COUNT;
	}

	public static String getLogLevel() {
		return LOG_LEVEL;
	}

	public static String getPolicy() {
		return POLICY;
	}

	public static String getSourceVender() {
		return SOURCE_VENDER;
	}

	public static String getSourceInitialContextFactory() {
		return SOURCE_INITIAL_CONTEXT_FACTORY;
	}

	public static String getSourceProviderURL() {
		return SOURCE_PROVIDER_URL;
	}

	public static String getSourceConnectionFactory() {
		return SOURCE_CONNECTION_FACTORY;
	}

	public static String getSourceQueueName() {
		return SOURCE_QUEUE_NAME;
	}

	public static int  getGetWaitTime() {
		return GET_WAIT_TIME;
	}

	public static String getErrQName() {
		return ERR_QNAME;
	}

	public static String getAdminQName() {
		return ADMIN_QNAME;
	}

	public static String getTargetVender() {
		return TARGET_VENDER;
	}

	public static String getTargetInitialContextFactory() {
		return TARGET_INITIAL_CONTEXT_FACTORY;
	}

	public static String getTargetProviderURL() {
		return TARGET_PROVIDER_URL;
	}

	public static String getTargetConnectionFactory() {
		return TARGET_CONNECTION_FACTORY;
	}

	public static String getTargetQueueName() {
		return TARGET_QUEUE_NAME;
	}

}
