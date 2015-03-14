package cn.edu.ustc;

import java.io.File;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.IOException;
import java.io.OutputStream;
import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Properties;

public class Util {

	public static String sysFile = "C:/Program Files/billiards/system.properties";

	public static String config = "config/config.properties";

	public static String dir = sysFile.substring(0, sysFile.lastIndexOf("/"));

	public static String beginFile = "D:/开台时间记录-防断电.properties";
	
	public static String consumeTime(TableBall t) {
		String result = "";
		int conMins = getMinute(t);
		int hours = 0;
		int minutes = 0;
		if (conMins >= 60) {
			hours = (int) (conMins / 60);
			minutes = conMins - hours * 60;
			result = hours + " 小时 " + minutes + " 分钟";
		} else {
			hours = 0;
			minutes = conMins;
			result = minutes + " 分钟";
		}
		return result;
	}

	public static int getMinute(TableBall t) {
		return (int) ((t.getEndTime().getTime() - t.getStartTime().getTime()) / 1000 / 60 + 1);
	}

	public static String formatOutput(double d) {
		DecimalFormat df = new DecimalFormat("###,###.##");
		return df.format(d);
	}

	public static String getMoney(TableBall t) {
		DecimalFormat df = new DecimalFormat("###,###.#");
		double money = 0;
		int remainMin = getMinute(t) - (int) (getMinute(t) / 60) * 60;
		if (remainMin > 30) {
			money = (int) (getMinute(t) / 60 + 1) * t.getPrice();
		} else {
			money = getMinute(t) * t.getPrice() / 60.0;
		}
		return df.format(money);
	}

	public static String getFormatTime(Date d) {
		SimpleDateFormat sdf = new SimpleDateFormat("HH:mm");
		return sdf.format(d);
	}

	public static String getFormatDate(Date d) {
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
		return sdf.format(d);
	}

	public static String getTimeShow(Date d) {
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm");
		return sdf.format(d);
	}

	public static boolean lowtoOneMonth(Date contract, Date now) {
		long result = (contract.getTime() - now.getTime()) / 1000 / 60 / 60
				/ 24;
		if (result < 30 && result >= 0) {
			return true;
		} else {
			return false;
		}
	}

	public static boolean isModified() {//false表示未修改，true表示修改了
		Date contract = getContractDate();
		Date actualCon =  getActualContractDate();
		String str1 = getFormatDate(contract);
		String str2 = getFormatDate(actualCon);
		return !str1.equals(str2);
	}
	
	public static Date getActualContractDate() {
		Properties p = new Properties();
		Date begin = new Date();//获取当前的日期
		int result = 0;
		Calendar c = Calendar.getInstance();
		c.setTime(begin);
		File file = new File(sysFile);
		File direct = new File(dir);
		boolean flag_dir = direct.exists();// 检查目录是否存在
		boolean flag_file = file.exists();// 检查文件是否存在
		if (!flag_dir) {
			direct.mkdir();
		}
		if (flag_file) {
			try {
				p.load(new FileReader(new File(sysFile)));
				String con = p.getProperty("contractMonth");// 获取合同有效月数值
				if (null == con || "".equals(con)) {
					writeContractDate();
					con = "0";
				}
				result = Integer.parseInt(con);
				c.add(Calendar.MONTH, result);
			} catch (Exception e) {
				e.printStackTrace();
			}
		} else {
			try {
				file.createNewFile();
				getContractDate();
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
		return c.getTime();
	}
	
	public static boolean isValid(Date contract, Date begin) {
		boolean flag = false;
		Date current = new Date();
		if(begin.after(current)) {//当前日期在合同开始日期之前，直接报过期
			flag = false;//begin已经减过一天了，这样，begin就在current之前了，肯定无效
			//这里需要自定义异常，判断是因为什么原因而过期了，这样在提示用户的时候更加清楚
			
		}else {
			if(contract.after(begin)&&current.before(contract)) {//合同结束期在开始期后，才有效
				flag = true;
			}
		}
		return flag;
	}

	public static void writeContractDate() {
		Properties p = new Properties();
		try {
			p.load(new FileReader(new File(sysFile)));
			OutputStream os = new FileOutputStream(sysFile);
			p.setProperty("contractMonth", "1");
			p.store(os, "update contractMonth 1 \n");
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	public static void writeBeginDate(Date d) {
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
		Properties p = new Properties();
		try {
			p.load(new FileReader(new File(sysFile)));
			OutputStream os = new FileOutputStream(sysFile);
			p.setProperty("beginDate", sdf.format(d));
			p.store(os, "update beginDate " + sdf.format(d) + "\n");
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	public static Date getBeginDate() {
		Properties p = new Properties();
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
		Date d = new Date();
		File file = new File(sysFile);
		File direct = new File(dir);
		boolean flag_dir = direct.exists();// 检查目录是否存在
		boolean flag_file = file.exists();// 检查文件是否存在
		if (!flag_dir) {
			direct.mkdir();
		}
		if (flag_file) {// 该文件如果存在，则读取信息
			try {
				p.load(new FileReader(new File(sysFile)));
				String str = p.getProperty("beginDate");
				if (null == str || "".equals(str)) {
					writeBeginDate(new Date());
				} else {
					d = sdf.parse(str);
				}
			} catch (Exception e) {
				e.printStackTrace();
			}
		} else {
			try {
				file.createNewFile();
				getBeginDate();
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
		return d;
	}

	public static Date getContractDate() {
		Properties p = new Properties();
		Calendar c = Calendar.getInstance();
		int result = 0;
		Date begin = getBeginDate();
		c.setTime(begin);
		File file = new File(sysFile);
		File direct = new File(dir);
		boolean flag_dir = direct.exists();// 检查目录是否存在
		boolean flag_file = file.exists();// 检查文件是否存在
		if (!flag_dir) {
			direct.mkdir();
		}
		if (flag_file) {
			try {
				p.load(new FileReader(new File(sysFile)));
				String con = p.getProperty("contractMonth");// 获取合同有效月数值
				if (null == con || "".equals(con)) {
					writeContractDate();
					con = "1";
				}
				result = Integer.parseInt(con);
				c.add(Calendar.MONTH, result);
			} catch (Exception e) {
				e.printStackTrace();
			}
		} else {
			try {
				file.createNewFile();
				getContractDate();
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
		return c.getTime();
	}

	public static Properties readConfig() {
		Properties p = new Properties();
		try {
			p.load(new FileReader(config));
		} catch (Exception e) {
			e.printStackTrace();
		}
		return p;
	}

	public static void changePrice(Properties p, String price) {
		p = readConfig();
		OutputStream os;
		try {
			os = new FileOutputStream(config);
			p.setProperty("price", price);
			p.store(os, "update beginDate " + Double.parseDouble(price) + "\n");
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	public static void writeBeginFile(TableBall t,Date d) {
		Properties p = new Properties();
		File file = new File(beginFile);
		if(!file.exists()) {
			try {
				file.createNewFile();
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm");
		try {
			p.load(new FileReader(beginFile));
			OutputStream os = new FileOutputStream(beginFile);
			System.out.println(getTimeShow(d));
			p.setProperty(t.getId(), sdf.format(d));
			p.store(os, "Log BeginTime");
		} catch (Exception e1) {
			e1.printStackTrace();
		}
	}
	
}
