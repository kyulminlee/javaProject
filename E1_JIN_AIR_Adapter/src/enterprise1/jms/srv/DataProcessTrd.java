package enterprise1.jms.srv;

import java.util.ArrayList;

import enterprise1.jms.Util.RAConfig;
import enterprise1.jms.lib.*;

public class DataProcessTrd extends ThreadBase {
	
	private DataProcessTrd dataThread;
	
	private ArrayList thd=new ArrayList();
	private int thdCnt = RAConfig.getThreadCount();	
	public boolean stop = true;
	
	
	public DataProcessTrd() throws Exception{
//		run();
		System.out.println(RAConfig.getThreadCount());
	}	
	

	public void exit() throws Exception {
		stop = false;
		waitForThread();
	}
	
	public void run(){
		System.out.println("DataProcess Thread Start!!!");
		
		while(true){
			try {
			System.out.println("DataProcess Thread is Running....");
			Thread.sleep(1000);
			
			// 1. ���� �⵿�� Admin Q�� �����͸� Get �Ѵ�. 
			// 2. �޽����� �־ ���� �����͸� Get�� ���� �ϸ� ���� Flag�� �����Ѵ�.					
			
			createThread();

			for (int i=0;i<thd.size();i++){
				((DataHandler)thd.get(i)).start();
			}
			
			waitForThread();
			
			if (ThreadManage.checkThreadDown()) break;
		/*	//RA.conf�� ������ �ֱ⸸ŭ Thread�� �����.
			Thread.sleep(RMSConfig.getPORT_POLL_WAIT_TIME());	*/
		
			} catch (RuntimeException e) {
				//RMUtil.logPrint(e,true);
				e.printStackTrace();
				break;
			} catch (Exception e) {
				//RMUtil.logPrint(e,true);
			}
			
		}
		try {
			System.out.println("DataProcess Thread Stopping!!!");
			doExit();
		} catch (Exception e) {
			//RMUtil.logPrint(e,true);
		}
	}
	
	private void createThread() throws Exception{
		  System.out.println("thdCnt"+thdCnt);
		for (int i=0;i<thdCnt;i++){
				thd.add(new DataHandler(this));
		}
		  
	 }
	
	  //thread ���� wait
	  private void waitForThread() throws Exception {
		  try {
			  for (int i = 0; i < thd.size(); i++) {
				  if (((DataHandler)thd.get(i)).isAlive())
						((DataHandler)thd.get(i)).join();
			  }
		  } catch (Exception e) {
			//  RMUtil.logPrint(e,true);
			  throw e;
		  } 
	  }
	
	
} //class ended
