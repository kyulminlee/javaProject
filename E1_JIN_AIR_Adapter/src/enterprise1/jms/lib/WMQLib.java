package enterprise1.jms.lib;

import javax.jms.JMSException;
import javax.jms.Message;
import javax.jms.MessageConsumer;
import javax.jms.MessageProducer;
import javax.jms.Session;
import javax.jms.TextMessage;
import javax.naming.Context;
import javax.naming.InitialContext;
import com.ibm.mq.jms.MQConnection;
import com.ibm.mq.jms.MQConnectionFactory;
import com.ibm.mq.jms.MQQueue;

import enterprise1.jms.Util.RAConfig;
import enterprise1.jms.Util.RAException;

public class WMQLib extends MQLib{
	
	////////////////////변수 선언 시작 /////////////////////
	private  MQConnectionFactory MQCfactory; 
	private  MQConnection con;
	private  Session session;
	private  MessageConsumer consumer; 
	private  MessageConsumer AdConsumer; 
	private  MessageProducer producer;
	private  MessageProducer ErrProducer;
	private  MQQueue SndQUEUE;
	private  MQQueue RcvQUEUE;
	private  MQQueue ErrQUEUE;
	private  MQQueue AdminQUEUE;			

	////////////////////변수 선언 끝 /////////////////////
	
	public void init() {		
		try{		
		ctx = getInitialContext();
		
		//Connection factory		
		MQCfactory = getConnectionFactory(); 
		
		//look up the destination
		SndQUEUE = getSndQueue();	
		ErrQUEUE = getErrQueue();
		
		RcvQUEUE   = getRcvQueue();   	
		AdminQUEUE = getAdminQueue();

		//Create session
		session = getQSession(); 
	
		if (RAConfig.getSourceVender().equals("WMQ")){
			initSender();
			inirErrSender();
		}
		else if(RAConfig.getTargetVender().equals("WMQ")){
			initReceiver();
		}
		
		//msgSend("메세지 보낼거야 4시 11분에 WMQ로 가라가라가라가류");*/
		
		//msgRcvMsg();  

		}catch (Exception e) {
			e.printStackTrace();
			
		}
		
	}	
	
	public void release() throws Exception{
		try {
			if (consumer!=null) consumer.close();
			if (AdConsumer!=null) AdConsumer.close();
			if (ErrProducer!=null) ErrProducer.close();
			if (producer!=null) producer.close();
			if (session!=null) session.close();			
			if (con!=null) con.close();
		} catch (JMSException e) {			
			throw e;
		}
	}	

	public void initSender() throws Exception{  //producer
		/*producer = getProducer();
		JmsSndMsg= session.createTextMessage();*/
		consumer =getConsumer();
	}
	
	public void initReceiver() throws Exception{ //consumer
		consumer = getConsumer();
	}	
	
	public void inirErrSender() throws Exception{	//err put
		ErrProducer = getErrProducer();
		JmsSndMsg= session.createTextMessage();
	} 
	
	public void initRcvAdmin() throws Exception{  //Admin Q get 
		AdConsumer = getADConsumer();
	}
	
	public void msgSend(String msg) throws Exception{
		try{
			JmsSndMsg.setText(msg);
			producer.send(JmsSndMsg);
			System.out.println("msg를 성공적으로 " + SND_QUEUE + "에 Put하였습니다.");
			session.commit(); 			
		} catch (Exception e){
			if (POLICY.equals("CONTINUE")){			
				try{
				System.out.println("msg Put 실패 !!" + ERR_QUEUE + "로 빠집니다.");
				JmsSndMsg.setText(msg);
				ErrProducer.send(JmsSndMsg);
				System.out.println("msg를 " + ERR_QUEUE + "에 Put하였습니다.");
				session.commit();
				}catch (Exception e1){
					session.rollback();
				}
			}else{
				session.rollback();
			}
		}
		return;
	}
	
	public String msgRcvString() throws Exception{
		JmsRcvMsg=(TextMessage) consumer.receive(GET_WAIT_TIME);
		if (JmsRcvMsg==null) return null;
		session.commit();
		return JmsRcvMsg.getText();
	}
	
	public Message msgRcvMsg() throws Exception{
		jmsmsg= consumer.receive(GET_WAIT_TIME);
		session.commit();
		return jmsmsg;
	}	
			
	public void stopCheck() throws Exception{	//AdminQ에서 종료여부 get함
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
	
	public MessageProducer getProducer() throws Exception{
		if (session==null) throw new RAException("Queue Session is null");
		if (SndQUEUE == null) return null;
		return session.createProducer(SndQUEUE);
	}
	
	public MessageProducer getErrProducer() throws Exception{
		if (session==null) throw new RAException("Queue Session is null");
		if (ErrQUEUE == null) return null;
		return session.createProducer(ErrQUEUE);
	}
	
	public MessageConsumer getConsumer() throws Exception{
		if (session==null) throw new RAException("Queue Session is null");
//		if (RcvQUEUE == null) return null;
		if (SndQUEUE == null) return null;
//		return session.createConsumer(RcvQUEUE);
		return session.createConsumer(SndQUEUE);
	}
	
	public MessageConsumer getADConsumer() throws Exception{
		if (session==null) throw new RAException("Queue Session is null");
		if (AdminQUEUE == null) return null;
		return session.createConsumer(AdminQUEUE);
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
	
	public Session getQSession() throws Exception{
		if (MQCfactory==null) throw new RAException("Queue Connection Factory is null");
		con = (MQConnection) MQCfactory.createConnection();
		con.start();
		return  con.createSession(true, Session.AUTO_ACKNOWLEDGE);
	}

	public MQConnectionFactory getConnectionFactory() throws Exception{
		if (ctx==null) throw new RAException("Context is null");
		try{
			Object obj=ctx.lookup(CONNECTION_FACTORY);
			return (MQConnectionFactory)obj;
		}catch (Exception e){
			throw e;
		}	
	}
	
	public MQQueue getSndQueue() throws Exception{
		if (ctx==null) throw new RAException("Context is null");
		if (SND_QUEUE==null || SND_QUEUE.equals("")) return null;
		return (MQQueue)ctx.lookup(SND_QUEUE);
	}
	
	public MQQueue getRcvQueue() throws Exception{
		if (ctx==null) throw new RAException("Context is null");
		if (RCV_QUEUE == null || RCV_QUEUE.equals("")) return null;
		return (MQQueue)ctx.lookup(RCV_QUEUE);
	}
	
	public MQQueue getErrQueue() throws Exception{
		if (ctx==null) throw new RAException("Context is null");
		if (ERR_QUEUE == null || ERR_QUEUE.equals("")) return null;
		return (MQQueue) ctx.lookup(ERR_QUEUE);
	}

	public MQQueue getAdminQueue() throws Exception{
		if (ctx==null) throw new RAException("Context is null");
		if (AD_QUEUE == null || AD_QUEUE.equals("")) return null;
		return (MQQueue) ctx.lookup(AD_QUEUE);
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
	 
	public int  getGET_WAIT_TIME() {
		return GET_WAIT_TIME;
	}

	public void setGET_WAIT_TIME(int  get_wait_time) {
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
	
	public void setSND_QUEUE(String snd_queue){
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

