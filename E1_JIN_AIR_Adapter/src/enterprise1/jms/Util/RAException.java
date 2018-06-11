package enterprise1.jms.Util;

public class RAException extends Exception {
	private Exception exp = null;
	private String errMsg = "";
	private String errCode = "";

	public RAException() {
	}

	// 에러메시지 발생
	public RAException(String msg) {
		errMsg = msg;
	}

	// 부모 익셉션 발생
	public RAException(Exception e) {
		exp = e;
	}

	// 부모 익셉션 + 에러메시지
	public RAException(Exception e, String msg) {
		exp = e;
		errMsg = msg;
	}

	// 부모 익셉션 + 에러코드 + 에러메시지 +
	public RAException(Exception e, String errCode, String msg) {
		exp = e;
		errCode = errCode;
		errMsg = msg;
	}

	public String getMsg() {
		return errMsg;
	}

	public void setMsg(String msg) {
		errMsg = msg;
	}

	public Exception getException() {
		return exp;
	}

	public void setException(Exception ee) {
		exp = ee;
	}

	public String getCode() {
		return errCode;
	}

	public void setCode(String code) {
		errCode = code;
	}

	public String getErrMessage() {
		String errMessge = "[" + errCode + "] : " + errMsg + exp; // 복합 메시지 어떻게
																	// 로깅할지
																	// 고민해보기
		return errMessge;
	}

	// StackTrace 로그에 기록하는 메소드
	public StringBuffer getStackTrace(Exception e) {
		StringBuffer sb = new StringBuffer();
		sb.append(e + "\n");// throw 했을 때의 오류 메시지(한 줄)가 출력됨

		StackTraceElement elem[] = e.getStackTrace();
		for (int i = 0; i < elem.length; i++) {
			sb.append("	at " + elem[i] + "\n");
		}

		return sb;
	}
}
