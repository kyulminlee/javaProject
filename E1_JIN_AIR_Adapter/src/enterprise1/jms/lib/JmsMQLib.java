package enterprise1.jms.lib;

import javax.jms.JMSException;
import javax.jms.Message;
import javax.jms.Queue;
import javax.jms.QueueConnection;
import javax.jms.QueueConnectionFactory;
import javax.jms.QueueReceiver;
import javax.jms.QueueSender;
import javax.jms.QueueSession;
import javax.jms.Session;
import javax.jms.TextMessage;
import javax.naming.Context;
import javax.naming.InitialContext;


import enterprise1.jms.Util.RAException;

public class JmsMQLib extends MQLib{
	
	////////////////////변수 선언 시작 /////////////////////	
	private QueueConnectionFactory QCFACTORY;
	private QueueConnection QCON;
	private QueueSession QSESSION;
	private  QueueReceiver QRCVER;
	private  QueueSender QSNDER;
	private  QueueReceiver ADQRCVER;
	private  QueueSender ERRQSNDER;
	private  Queue SndQUEUE;
	private  Queue RcvQUEUE;
	private  Queue ErrQUEUE;
	private  Queue AdminQUEUE;

	////////////////////변수 선언 끝 /////////////////////

	public void init()   {		
		try{			
		ctx = getInitialContext();
		
		//Connection factory
		QCFACTORY = getConnectionFactory(); 

		//look up the destination
		SndQUEUE = getSndQueue();
		ErrQUEUE = getErrQueue();
		
		RcvQUEUE = getRcvQueue();
		AdminQUEUE = getAdminQueue();
		
		//Create session
		QSESSION = getQSession();
		
		System.out.println("JMS Connection Success");		
		/*initSender();
		inirErrSender();
		msgSend("메세지 보낼거야 4시 11분에 WMQ로 가라가라가라가류");*/
		
		initReceiver();
		//msgRcvString();
		msgRcvMsg();
		
		} catch (Exception ex) {
			ex.printStackTrace();
		}
		
	}
		
	public void release() throws Exception{
		try {
			if (QSNDER!=null) QSNDER.close();
			if (ERRQSNDER!=null) QSNDER.close();
			if (ADQRCVER!=null) QRCVER.close();
			if (QRCVER!=null) QRCVER.close();
			if (QSESSION!=null) QSESSION.close();
			if (QCON!=null) QCON.close();
		} catch (JMSException e) {			
			throw e;
		}
	}
	
	public void initSender() throws Exception{
		QSNDER = getSender();
		JmsSndMsg= QSESSION.createTextMessage();		
	}
		
	public void initReceiver() throws Exception{
		QRCVER = getReceiver();		
	}

	public void inirErrSender() throws Exception{	//err put
		ERRQSNDER = getErrSender();
		JmsSndMsg= QSESSION.createTextMessage();
	} 

	public void initRcvAdmin() throws Exception{  //Admin Q get 
		ADQRCVER = getADReceiver();		
	}
	
	public void msgSend(String msg) throws Exception{
		try{
			JmsSndMsg.setText(msg);
			QSNDER.send(JmsSndMsg);
			System.out.println("msg를 성공적으로 " + SND_QUEUE + "에 Put하였습니다.");
			QSESSION.commit();
		}catch (Exception e){	
			if (POLICY.equals("CONTINUE")){			
				try{
				System.out.println("msg Put 실패 !!" + ERR_QUEUE + "로 빠집니다.");
				JmsSndMsg.setText(msg);
				ERRQSNDER.send(JmsSndMsg);
				System.out.println("msg를 " + ERR_QUEUE + "에 Put하였습니다.");
				QSESSION.commit();
				}catch (Exception e1){
					QSESSION.rollback();
				}
			}else{
				QSESSION.rollback();
			}
		}
		return;
	}
	
	public String msgRcvString() throws Exception{
		JmsRcvMsg=(TextMessage) QRCVER.receive(GET_WAIT_TIME);
		if (JmsRcvMsg==null) return null;
		QSESSION.commit();
		return JmsRcvMsg.getText();
	}
	
	public Message msgRcvMsg() throws Exception{
		jmsmsg= QRCVER.receive(GET_WAIT_TIME);
		QSESSION.commit();
		return jmsmsg;
	}			
	
	public void stopCheck() throws Exception{
		initRcvAdmin();
		msgRcvString();
		String stopcheck = JmsRcvMsg.getText();
		
		if (stopcheck.equals("Y")){
			release();
			System.out.println("Stop명령어가"+ JmsRcvMsg.getText()+ "이므로 QBridge를 종료합니다.");
			
		}else{
			return;			
		}
	}
	//////////////////////Get Set //////////////////////
	
	public QueueSender getSender() throws Exception {
		if (QSESSION == null) throw new RAException("Queue Session is null");
		if (SndQUEUE == null) return null;
		return QSESSION.createSender(SndQUEUE);		
	}	
	
	public QueueSender getErrSender() throws Exception {
		if (QSESSION == null) throw new RAException("Queue Session is null");
		if (ErrQUEUE == null) return null;
		return QSESSION.createSender(ErrQUEUE);		
	}
	
	public QueueReceiver getReceiver() throws Exception{
		if (QSESSION==null) throw new RAException("Queue Session is null");
		if (RcvQUEUE == null) return null;
		return QSESSION.createReceiver(RcvQUEUE);
	}
	
	public QueueReceiver getADReceiver() throws Exception{
		if (QSESSION==null) throw new RAException("Queue Session is null");
		if (AdminQUEUE == null) return null;
		return QSESSION.createReceiver(AdminQUEUE);
	}
	
	
	public Context getInitialContext() throws Exception{		
		try{
		p.put(Context.INITIAL_CONTEXT_FACTORY, INITIAL_CONTEXT_FACTORY);
		p.put(Context.PROVIDER_URL, PROVIDER_URL);				
		}catch(Exception e){			
			throw e;
		}
		return new InitialContext(p);
	}
		
	public QueueSession getQSession() throws Exception{
		if (QCFACTORY==null) throw new RAException("Queue Connection Factory is null");
		QCON = QCFACTORY.createQueueConnection();
		QCON.start();
		return (QueueSession) QCON.createSession(true, Session.AUTO_ACKNOWLEDGE);
	}
		
	public QueueConnectionFactory getConnectionFactory() throws Exception{
		if (ctx==null) throw new RAException("Context is null");
		try{
			Object obj=ctx.lookup(CONNECTION_FACTORY);
			return (QueueConnectionFactory)obj;
		}catch (Exception e){
			throw e;
		}	
				
	}
	
	public Queue getSndQueue() throws Exception{
		if (ctx==null) throw new RAException("Context is null");
		if (SND_QUEUE==null || SND_QUEUE.equals("")) return null;
		return (Queue)ctx.lookup(SND_QUEUE);
	}
	
	public Queue getRcvQueue() throws Exception{
		if (ctx==null) throw new RAException("Context is null");
		if (RCV_QUEUE == null || RCV_QUEUE.equals("")) return null;
		return (Queue)ctx.lookup(RCV_QUEUE);
	}

	public Queue getErrQueue() throws Exception{
		if (ctx==null) throw new RAException("Context is null");
		if (ERR_QUEUE == null || ERR_QUEUE.equals("")) return null;
		return (Queue)ctx.lookup(ERR_QUEUE);

	}
	
	public Queue getAdminQueue() throws Exception{
		if (ctx==null) throw new RAException("Context is null");
		if (AD_QUEUE == null || AD_QUEUE.equals("")) return null;
		return (Queue)ctx.lookup(AD_QUEUE);		

	}
	
	public String getCONNECTION_FACTORY() {
		return CONNECTION_FACTORY;
	}

	public void setCONNECTION_FACTORY(String connection_factory) {
		CONNECTION_FACTORY = connection_factory;
	}
	
	public String getINITIAL_CONTEXT_FACTORY() {
		return INITIAL_CONTEXT_FACTORY;
	}

	public void setINITIAL_CONTEXT_FACTORY(String initial_context_factory) {
		INITIAL_CONTEXT_FACTORY = initial_context_factory;
	}

	public String getPROVIDER_URL() {
		return PROVIDER_URL;
	}

	public void setPROVIDER_URL(String provider_url) {
		PROVIDER_URL = provider_url;
	}
	 
	public int getGET_WAIT_TIME() {
		return GET_WAIT_TIME;
	}

	public void setGET_WAIT_TIME(int get_wait_time) {
		GET_WAIT_TIME = get_wait_time;
	}
	
	public String getPOLICY() {
		return POLICY;
	}

	public void setPOLICY(String Policy) {
		POLICY = Policy;
	}
	
	public String getERR_QUEUE() {
		return ERR_QUEUE;
	}

	public void setERR_QUEUE(String err_queue) {
		ERR_QUEUE = err_queue;
	}
	
	public void setSnd_QUEUE(String snd_queue){
		SND_QUEUE = snd_queue;
	}
	
	public String getSND_QUEUE() {
		return SND_QUEUE;
	}
	
	public String getRCV_QUEUE() {
		return RCV_QUEUE;
	}

	public void setRCV_QUEUE(String rcv_queue) {
		RCV_QUEUE = rcv_queue;
	}
	
	public String getAD_QUEUE() {
		return AD_QUEUE;
	}

	public void setAD_QUEUE(String ad_queue) {
		AD_QUEUE = ad_queue;
	}
} //class ended


