package enterprise1.jms.Util;

public class RAException extends Exception {
	private Exception exp = null;
	private String errMsg = "";
	private String errCode = "";

	public RAException() {
	}

	// �����޽��� �߻�
	public RAException(String msg) {
		errMsg = msg;
	}

	// �θ� �ͼ��� �߻�
	public RAException(Exception e) {
		exp = e;
	}

	// �θ� �ͼ��� + �����޽���
	public RAException(Exception e, String msg) {
		exp = e;
		errMsg = msg;
	}

	// �θ� �ͼ��� + �����ڵ� + �����޽��� +
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
		String errMessge = "[" + errCode + "] : " + errMsg + exp; // ���� �޽��� ���
																	// �α�����
																	// ����غ���
		return errMessge;
	}

	// StackTrace �α׿� ����ϴ� �޼ҵ�
	public StringBuffer getStackTrace(Exception e) {
		StringBuffer sb = new StringBuffer();
		sb.append(e + "\n");// throw ���� ���� ���� �޽���(�� ��)�� ��µ�

		StackTraceElement elem[] = e.getStackTrace();
		for (int i = 0; i < elem.length; i++) {
			sb.append("	at " + elem[i] + "\n");
		}

		return sb;
	}
}
