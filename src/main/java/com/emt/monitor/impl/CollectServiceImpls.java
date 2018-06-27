package com.emt.monitor.impl;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.LineNumberReader;
import java.io.PrintWriter;
import java.io.StringWriter;
import java.util.StringTokenizer;
import java.util.UUID;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import com.emt.monitor.service.CollectService;


@Service
public class CollectServiceImpls implements CollectService{
	
	private final Logger logger = LoggerFactory.getLogger(CollectServiceImpls.class);
	
	// 可以设置长些，防止读到运行此次系统检查时的cpu占用率，就不准了
	private static final int CPUTIME = 500;

	private static final int PERCENT = 100;

	private static final int FAULTLENGTH = 10;

	public double getCpuRatio() {
		// 操作系统
		String osName = System.getProperty("os.name");
		if (osName.toLowerCase().startsWith("windows")) {
			return getCpuRatioForWindows();
		} else {
			return getCpuRateForLinux();
		}
	}
	public static void main(String[] args) {
		String s = "1";
		if(s.contains("\\|")){
			String[] s1 = s.split("\\|");
			for (int i = 0; i < s1.length; i++) {
				System.out.println(s1[i]+i);
			}
		}
		
	}
	
	public double getCpuRateForLinux() {  
		String uid = UUID.randomUUID().toString();
    	logger.info("OperationLog:"+uid+"|||");
        float cpuUsage = 0;  
        Process pro1,pro2;  
        Runtime r = Runtime.getRuntime();  
        try {
            String command = "cat /proc/stat";  
            //第一次采集CPU时间  
            pro1 = r.exec(command);  
            BufferedReader in1 = new BufferedReader(new InputStreamReader(pro1.getInputStream()));  
            String line = null;  
            long idleCpuTime1 = 0, totalCpuTime1 = 0;   //分别为系统启动后空闲的CPU时间和总的CPU时间  
            while((line=in1.readLine()) != null){     
                if(line.startsWith("cpu")){  
                    line = line.trim();  
                    System.out.println(line);  
                    String[] temp = line.split("\\s+");   
                    idleCpuTime1 = Long.parseLong(temp[4]);  
                    for(String s : temp){  
                        if(!s.equals("cpu")){  
                            totalCpuTime1 += Long.parseLong(s);  
                        }  
                    }     
                    logger.info("IdleCpuTime: " + idleCpuTime1 + ", " + "TotalCpuTime" + totalCpuTime1);  
                    break;  
                }                         
            }     
            in1.close();  
            pro1.destroy();  
            try {
                Thread.sleep(1000);  
            } catch (InterruptedException e) {  
                StringWriter sw = new StringWriter();  
                e.printStackTrace(new PrintWriter(sw));  
                logger.info("CpuUsage休眠时发生InterruptedException. " + e.getMessage());  
                logger.info(sw.toString());
                return 0;
            }  
            //第二次采集CPU时间  
            pro2 = r.exec(command);  
            BufferedReader in2 = new BufferedReader(new InputStreamReader(pro2.getInputStream()));  
            long idleCpuTime2 = 0, totalCpuTime2 = 0;   //分别为系统启动后空闲的CPU时间和总的CPU时间  
            while((line=in2.readLine()) != null){     
                if(line.startsWith("cpu")){  
                    line = line.trim();  
                    System.out.println(line);  
                    String[] temp = line.split("\\s+");   
                    idleCpuTime2 = Long.parseLong(temp[4]);  
                    for(String s : temp){  
                        if(!s.equals("cpu")){  
                            totalCpuTime2 += Long.parseLong(s);  
                        }  
                    }  
                    logger.info("IdleCpuTime: " + idleCpuTime2 + ", " + "TotalCpuTime" + totalCpuTime2);  
                    break;    
                }                                 
            }  
            if(idleCpuTime1 != 0 && totalCpuTime1 !=0 && idleCpuTime2 != 0 && totalCpuTime2 !=0){  
                cpuUsage = 1 - (float)(idleCpuTime2 - idleCpuTime1)/(float)(totalCpuTime2 - totalCpuTime1);  
                logger.info("本节点CPU使用率为: " + cpuUsage);  
            }                 
            in2.close();  
            pro2.destroy();
        } catch (IOException e) {  
            StringWriter sw = new StringWriter();  
            e.printStackTrace(new PrintWriter(sw));  
            logger.info("CpuUsage发生InstantiationException. " + e.getMessage());  
            logger.info(sw.toString());
            return 0;
        }     

        logger.error("OperationLog:"+uid+"|EQ0001|必填信息不能为空|message");
        return cpuUsage * 100;  
    }

	/**
	 * 获得CPU使用率.
	 * 
	 * @return 返回cpu使用率
	 */
	private double getCpuRatioForWindows() {
		try {
			String procCmd = System.getenv("windir") + "\\system32\\wbem\\wmic.exe process get Caption,CommandLine,"
					+ "KernelModeTime,ReadOperationCount,ThreadCount,UserModeTime,WriteOperationCount";
			// 取进程信息
			long[] c0 = readCpu(Runtime.getRuntime().exec(procCmd));
			Thread.sleep(CPUTIME);
			long[] c1 = readCpu(Runtime.getRuntime().exec(procCmd));
			if (c0 != null && c1 != null) {
				long idletime = c1[0] - c0[0];
				long busytime = c1[1] - c0[1];
				return Double.valueOf(PERCENT * (busytime) / (busytime + idletime)).doubleValue();
			} else {
				return 0.0;
			}
		} catch (Exception ex) {
			ex.printStackTrace();
			return 0.0;
		}
	}

	/**
	 * 
	 * 读取CPU信息.
	 * 
	 * @param proc
	 */
	private long[] readCpu(final Process proc) {
		long[] retn = new long[2];
		try {
			proc.getOutputStream().close();
			InputStreamReader ir = new InputStreamReader(proc.getInputStream());
			LineNumberReader input = new LineNumberReader(ir);
			String line = input.readLine();
			if (line == null || line.length() < FAULTLENGTH) {
				return null;
			}
			int capidx = line.indexOf("Caption");
			int cmdidx = line.indexOf("CommandLine");
			int rocidx = line.indexOf("ReadOperationCount");
			int umtidx = line.indexOf("UserModeTime");
			int kmtidx = line.indexOf("KernelModeTime");
			int wocidx = line.indexOf("WriteOperationCount");
			long idletime = 0;
			long kneltime = 0;
			long usertime = 0;
			while ((line = input.readLine()) != null) {
				if (line.length() < wocidx) {
					continue;
				}
				// 字段出现顺序：Caption,CommandLine,KernelModeTime,ReadOperationCount,
				// ThreadCount,UserModeTime,WriteOperation
				String caption = substring(line, capidx, cmdidx - 1).trim();
				String cmd = substring(line, cmdidx, kmtidx - 1).trim();
				if (cmd.indexOf("wmic.exe") >= 0) {
					continue;
				}
				// System.out.println("line="+line);
				if (caption.equals("System Idle Process") || caption.equals("System")) {
					idletime += Long.valueOf(substring(line, kmtidx, rocidx - 1).trim()).longValue();
					idletime += Long.valueOf(substring(line, umtidx, wocidx - 1).trim()).longValue();
					continue;
				}

				kneltime += Long.valueOf(substring(line, kmtidx, rocidx - 1).trim()).longValue();
				usertime += Long.valueOf(substring(line, umtidx, wocidx - 1).trim()).longValue();
			}
			retn[0] = idletime;
			retn[1] = kneltime + usertime;
			return retn;
		} catch (Exception ex) {
			ex.printStackTrace();
		} finally {
			try {
				proc.getInputStream().close();
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
		return null;
	}


	public String substring(String src, int start_idx, int end_idx) {
		byte[] b = src.getBytes();
		String tgt = "";
		for (int i = start_idx; i <= end_idx; i++) {
			tgt += (char) b[i];
		}
		return tgt;
	}

}