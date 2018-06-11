package enterprise1.jms.srv;

import java.io.File;

public class ThreadManage {
	
	public static String delete_file_name = "";
	public static boolean checkThreadDown(){
		//String filename=(String)RMUtil.convDate(new Date(), RMEnv.PROCESS_DOWN_FORMAT)+"-"+RMEnv.PROCESS_DOWN_FILE_NAME;
		//filename=RMSConfig.getRm_Home()+File.separatorChar+"server"+File.separatorChar+"bin"+File.separatorChar+filename;
		String filename ="소스수정필요";
		File f=new File(filename);
		if (f.exists()){
			delete_file_name=filename;
			return true;
		}else{
			return false;
		}
	}
	public static void deleteThreadDownFile(){
		File f=new File(delete_file_name);
		if (f.exists()){
			f.delete();
		}
	}
	
}
