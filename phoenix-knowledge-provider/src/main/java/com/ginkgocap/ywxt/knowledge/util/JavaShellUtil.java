package com.ginkgocap.ywxt.knowledge.util;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

/**
 * java的shell脚本调用
 * 
 * @author lk
 * @version 2013-04-11
 */
public class JavaShellUtil {
	private InputStream is;
	private InputStreamReader ir;
	private InputStream erris;
	private BufferedReader bufferedReader;
	public String executeShell() throws IOException {
		
		StringBuffer stringBuffer = new StringBuffer();
		// 格式化日期时间，记录日志时使用
		DateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:SS ");
		try {
			stringBuffer.append(dateFormat.format(new Date())).append("准备执行Shell命令 ")
			.append(this.shellPath + "/" + this.shellName).append(" \r\n");
			Process pid = null;
			String[] cmd = { "/bin/sh", "-c", shellPath + "/" + shellName,this.paths,this.genPath};
			// 执行Shell命令
			pid = Runtime.getRuntime().exec(cmd);
			if (pid != null) {
				stringBuffer.append("进程号：").append(pid.toString()).append("\r\n");
				pid.waitFor();
			} else {
				stringBuffer.append("没有pid\r\n");
			}
            is = pid.getInputStream();
            ir = new InputStreamReader(is);
            bufferedReader = new BufferedReader(ir);
            
            
            
            
            
			stringBuffer.append(dateFormat.format(new Date())).append("Shell命令执行完毕\r\n执行结果为：\r\n");
			String line = null;
			// 读取Shell的输出内容，并添加到stringBuffer中
			while (bufferedReader != null && (line = bufferedReader.readLine()) != null) {
				stringBuffer.append(line).append("\r\n");
			}

			erris = pid.getErrorStream();
			ir = new InputStreamReader(erris);
			bufferedReader = new BufferedReader(ir);
			while (bufferedReader != null && (line = bufferedReader.readLine()) != null) {
				err.append(line);
				stringBuffer.append(err.toString()).append("\r\n");
			}
			if (err != null && !err.toString().equals(""))
				success.put("1", "0");
			else
				success.put("1", "1");
		} catch (Exception ioe) {
			success.put("1", "0");
			stringBuffer.append("执行Shell命令时发生异常：\r\n").append(ioe.getMessage()).append("\r\n");
		} finally {
			if (bufferedReader != null) {
				try {
					if (bufferedReader != null)bufferedReader.close();
					if (is != null)is.close();
					if (erris != null) erris.close();
					if (ir != null)ir.close();
//					 ************************将Shell的执行情况输出到日志文件中***************************
					File file = new File(this.logFilePath);
					if (!file.exists()) file.mkdirs();
					os = new FileOutputStream(this.logFilePath + "/" + this.logFileName);
					osw = new OutputStreamWriter(os,"UTF-8");
					osw.write(stringBuffer.toString());
//					***************************************************************************
					success.put("2", "1");
				} catch (Exception e) {
					success.put("2", "0");
					e.printStackTrace();
				} finally {
					if (osw != null)osw.close();
					if (os != null)os.close();
				}
			}
		}
		return stringBuffer.toString();
	}
	
	
	public Map<String, String> getSuccess() {
		return success;
	}
	private void setSuccess(Map<String, String> success) {
		this.success = success;
	}
	public String getGenPath() {
		return genPath;
	}
	public void setGenPath(String genPath) {
		this.genPath = genPath;
	}
	public String getGenName() {
		return genName;
	}
	public void setGenName(String genName) {
		this.genName = genName;
	}
	public String getLogFileName() {
		return logFileName;
	}
	public void setLogFileName(String logFileName) {
		this.logFileName = logFileName;
	}
	public String getLogFilePath() {
		return logFilePath;
	}
	public void setLogFilePath(String logFilePath) {
		this.logFilePath = logFilePath;
	}
	public String getShellPath() {
		return shellPath;
	}
	public void setShellPath(String shellPath) {
		this.shellPath = shellPath;
	}
	public String getShellName() {
		return shellName;
	}
	public void setShellName(String shellName) {
		this.shellName = shellName;
	}
	public StringBuffer getErr() {
		return err;
	}
	private void setErr(StringBuffer err) {
		this.err = err;
	}
	public String getPaths() {
		return paths;
	}
	public void setPaths(String paths) {
		this.paths = paths;
	}
	//执行shell脚本所需的参数
	private String paths;
	// 记录Shell执行状况的日志文件的位置(绝对路径)
	private String logFilePath;
	// 生成的日志文件名称
	private String logFileName;
	// Shell文件的路径
	private String shellPath;
	// Shell的文件名(绝对路径)
	private String shellName;
	//生成文件路径
	private String genPath;
	//生成文件名称
	private String genName;
	//日志输出流
	private OutputStream os;
	//日志输出Writer
	private OutputStreamWriter osw;
	//取到执行shell脚本时若出错时的信息
	private StringBuffer err = new StringBuffer();
	//shell脚本执行是否成功标志    1,0shell脚本执行失败  1,1shell脚本执行成功  2,0日志文件生成失败  2,1日志文件生成成功
	private Map<String ,String> success = new HashMap<String,String>();
	public static void main(String[] arges) throws IOException{
		JavaShellUtil js = new JavaShellUtil();
		js.setLogFileName(new SimpleDateFormat("yyyy-MM-dd HH:mm:SS").format(new Date()) + "_test.log");
		js.setLogFilePath("/tmp/shellexe/" + new SimpleDateFormat("yyyy-MM-dd HH:mm:SS").format(new Date()));
		js.setShellName("hello.sh");
		js.setShellPath("/root/apache-tomcat-6.0.36/webapps/phoenix-knowledge-web/WEB-INF/classes");
		js.setPaths("");
		js.setGenPath("");
		System.out.println(js.executeShell());
		System.out.println("--------------------------------");
		String exestate = js.getSuccess().get("1") == null ? "" : js.getSuccess().get("1").toString();
		String logstate = js.getSuccess().get("2") == null ? "" : js.getSuccess().get("2").toString();
		System.out.println("shell执行状态为:" + exestate);
		System.out.println("log生成状态为:" + logstate);
		System.out.println("log生成文件在" + js.getLogFilePath() + "/" + js.getLogFileName());
//		for(String arg:arges){
//			try {
//				System.out.println(js.executeShell());
//			} catch (IOException e) {
//				e.printStackTrace();
//			}
//		}
	}
}
