package enterprise1.jms.srv;

import enterprise1.jms.Util.RAConfig;
import enterprise1.jms.lib.*;
/*
 * 제목 : 데이터 처리 Handler
 * 작성자 : 이규민
 * 작성일 : 2012년 9월 21일
 */

public class DataHandler extends Thread{

	private DataHandler datahandle;
	private DataProcessTrd dataThread;
	private WMQLib wmqlib = new WMQLib();   //WMQLib wmqlib;
	private JmsMQLib jmslib;
	

	public DataHandler(){
		datahandle=new DataHandler();
	}
	
	public DataHandler(DataProcessTrd thread){
		//datahandle=new DataHandler();		-------이부분 이상해~!@~!@~!$#@!$! 
		dataThread = thread;
	}

	public void run(){
		try{	
			while(dataThread.stop){
				getMsg();
				
				//Thread.sleep((long)Interval*60000);
				
			}
		}catch (Exception e) {
			//e.printStackTrace();
			//에러로그 2번 출력됨
			e.printStackTrace();
		}
	}
	
	public void getMsg() throws Exception{			//송신
		if(RAConfig.getSourceVender().equals("WMQ")){
			wmqlib.init();
			wmqlib.msgRcvMsg();
		}else if(RAConfig.getSourceVender().equals("JMS")){
			jmslib.msgRcvMsg();
		}
	}
	
	public void PutMsg() throws Exception{			//수신
		if(RAConfig.getTargetVender().equals("WMQ")){
			String msg = wmqlib.msgRcvMsg().getJMSMessageID();
			wmqlib.msgSend(msg);
		}else if(RAConfig.getTargetVender().equals("JMS")){
			String msg = jmslib.msgRcvMsg().getJMSMessageID();
			jmslib.msgSend(msg);
		}
	}
} //end class


