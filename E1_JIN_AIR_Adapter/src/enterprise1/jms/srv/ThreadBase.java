package enterprise1.jms.srv;

public abstract class ThreadBase extends Thread {
	
/*	public abstract void initResource() throws Exception;
	
	public void init() throws Exception{
		initResource();
	}
	public abstract void releaseResource() throws Exception;
	public void release() throws Exception{
		releaseResource();
	}
	*/
	public ThreadBase() {
	}
	
	public void doExit() throws Exception{
		//RMUtil.logPrint("Thread down : "+this.getName()+"*****"+this.getPriority(),1);
		//release();
		exit();
	}
	public abstract void exit() throws Exception;

} //class ended
