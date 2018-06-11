package enterprise1.jms.Util;

import org.apache.log4j.PropertyConfigurator;
import org.apache.log4j.Logger;

/**
 * 로그 테스트 클래스
 */

public class LogExceptionTest {
	
	//로거 생성
	static Logger logger = Logger.getLogger(LogExceptionTest.class);

	
	public static void main(String args[]) {
		//PropertyConfigurator.configure("log.properties");
		//10분 간격으로 log.properties파일 새로 로딩
		PropertyConfigurator.configureAndWatch("log.properties", 600000); 
		logger.debug("devide 호출");
		devide(2, 0);
	}

	static void devide(int a, int b) {

		try {
			int c = a / b;
			logger.debug(a + "/" + b + " = " + c);
		} catch(ArithmeticException ae){
		} catch (Exception e) {
			logger.error("오류 발생");
			try {
				Thread.sleep(1000);
				System.out.println("----------------------------------------------------------------");
			} catch (Exception e2) {
			}

		}

	}

}
