package enterprise1.jms.srv;

import enterprise1.jms.Util.RAConfig;
import enterprise1.jms.lib.*;
/*
 * ���� : ������ ó�� Handler
 * �ۼ��� : �̱Թ�
 * �ۼ��� : 2012�� 9�� 21��
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
		//datahandle=new DataHandler();		-------�̺κ� �̻���~!@~!@~!$#@!$! 
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
			//�����α� 2�� ��µ�
			e.printStackTrace();
		}
	}
	
	public void getMsg() throws Exception{			//�۽�
		if(RAConfig.getSourceVender().equals("WMQ")){
			wmqlib.init();
			wmqlib.msgRcvMsg();
		}else if(RAConfig.getSourceVender().equals("JMS")){
			jmslib.msgRcvMsg();
		}
	}
	
	public void PutMsg() throws Exception{			//����
		if(RAConfig.getTargetVender().equals("WMQ")){
			String msg = wmqlib.msgRcvMsg().getJMSMessageID();
			wmqlib.msgSend(msg);
		}else if(RAConfig.getTargetVender().equals("JMS")){
			String msg = jmslib.msgRcvMsg().getJMSMessageID();
			jmslib.msgSend(msg);
		}
	}
} //end class


