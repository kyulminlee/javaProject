package enterprise1.jms.srv;

import java.util.ArrayList;
import enterprise1.jms.lib.*;
import enterprise1.jms.Util.*;



public class QBridge {

	private static JmsMQLib jms = new JmsMQLib();
	private static WMQLib wmq = new WMQLib();
	private static ArrayList thds=new ArrayList();
	
	
	public static void main(String[] args){

		QBridge qb =new QBridge();
				
	    try{
	    	
	    	QBridge.mainproc();
	    	
	    }catch(Exception e){	     
	    	e.printStackTrace();
	    }
	}  //main Ends

	private static void mainproc() throws Exception {
	    try {
	    	System.out.println();
	    	System.out.println("\t QBridge System Start  ");
	        System.out.println("");

    
	    	init();    	
	    	createThread();
	    	
	    	for (int i = 0; i < thds.size(); i++) {
				((ThreadBase)thds.get(i)).start();
			}
	    	
	    	//AdminThread.start = false;     //스레드 종료 시
	    } catch (Exception e) {
	      /////////////////////////////////exception처리 필요///////////
	      throw e;
	    }finally{
	    	destroy();
	    	System.out.println();
	    	System.out.println("\t destroy successs");
	        System.out.println("");
	    }
	}

	//자원 release 
	private static void destroy() throws Exception{
		try {
						
			if (jms!=null) jms.release();          
			if (wmq!=null) wmq.release();
			
		} catch (Exception e) {
			
			throw e;
		}
		
	}

	//자원 init
	private static void init() throws Exception{
		RAConfig.getInstance();
		try {
					
			//1. WMQ - JMS  2.WMQ - WMQ 3.JMS - WMQ  4.JMS - JMS			
			if(RAConfig.getSourceVender().equals("WMQ")){
				wmq.setPOLICY(RAConfig.getPolicy());
				wmq.setERR_QUEUE(RAConfig.getErrQName());
				wmq.setAD_QUEUE(RAConfig.getAdminQName());
				wmq.setINITIAL_CONTEXT_FACTORY(RAConfig.getSourceInitialContextFactory());
				wmq.setPROVIDER_URL(RAConfig.getSourceProviderURL());
				wmq.setCONNECTION_FACTORY(RAConfig.getSourceConnectionFactory());
				wmq.setSND_QUEUE(RAConfig.getSourceQueueName());
				wmq.setGET_WAIT_TIME(RAConfig.getGetWaitTime());
				wmq.init();
			}
			else if(RAConfig.getSourceVender().equals("JMS")){
				jms.setPOLICY(RAConfig.getPolicy());
				jms.setERR_QUEUE(RAConfig.getErrQName());
				jms.setAD_QUEUE(RAConfig.getAdminQName());
				jms.setINITIAL_CONTEXT_FACTORY(RAConfig.getSourceInitialContextFactory());
				jms.setPROVIDER_URL(RAConfig.getSourceProviderURL());
				jms.setCONNECTION_FACTORY(RAConfig.getSourceConnectionFactory());
				jms.setSnd_QUEUE(RAConfig.getSourceQueueName());
				jms.setGET_WAIT_TIME(RAConfig.getGetWaitTime());
				jms.init();
			}else{
				System.out.println("QBridge에서 제공하지 않는 Vender 입니다.");
			}
			
			if(RAConfig.getTargetVender().equals("WMQ")){
				wmq.setINITIAL_CONTEXT_FACTORY(RAConfig.getTargetInitialContextFactory());
				wmq.setPROVIDER_URL(RAConfig.getTargetProviderURL());
				wmq.setCONNECTION_FACTORY(RAConfig.getTargetConnectionFactory());
				wmq.setRCV_QUEUE(RAConfig.getTargetQueueName());
				wmq.init();
			}
			else if(RAConfig.getTargetVender().equals("JMS")){
				jms.setINITIAL_CONTEXT_FACTORY(RAConfig.getTargetInitialContextFactory());
				jms.setPROVIDER_URL(RAConfig.getTargetProviderURL());
				jms.setCONNECTION_FACTORY(RAConfig.getTargetConnectionFactory());
				jms.setRCV_QUEUE(RAConfig.getTargetQueueName());
				jms.init();
			}else{
				System.out.println("QBridge에서 제공하지 않는 Vender 입니다.");
			}
				
		} catch (Exception e) {
			throw e;
		}	  
		
	}
	
	private static void createThread() throws Exception{

		/*int thdcnt=RAConfig.getThreadCount();		//RA 스레드 개수
		if (thdcnt==0) thdcnt=1;*/
		thds.add(new DataProcessTrd());  
		
	}
		
} //class ended
