package enterprise1.jms.Util;

import org.apache.log4j.PropertyConfigurator;
import org.apache.log4j.Logger;

/**
 * �α� �׽�Ʈ Ŭ����
 */

public class LogExceptionTest {
	
	//�ΰ� ����
	static Logger logger = Logger.getLogger(LogExceptionTest.class);

	
	public static void main(String args[]) {
		//PropertyConfigurator.configure("log.properties");
		//10�� �������� log.properties���� ���� �ε�
		PropertyConfigurator.configureAndWatch("log.properties", 600000); 
		logger.debug("devide ȣ��");
		devide(2, 0);
	}

	static void devide(int a, int b) {

		try {
			int c = a / b;
			logger.debug(a + "/" + b + " = " + c);
		} catch(ArithmeticException ae){
		} catch (Exception e) {
			logger.error("���� �߻�");
			try {
				Thread.sleep(1000);
				System.out.println("----------------------------------------------------------------");
			} catch (Exception e2) {
			}

		}

	}

}
