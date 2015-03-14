package cn.edu.ustc;

import java.awt.*;
import java.awt.event.*;
import java.util.Date;
import java.util.Properties;

import javax.swing.*;

@SuppressWarnings("static-access")
public class MainFrame {

	private static String systemname = "";

	private static String billingname = "";

	private static JFrame frame = null;

	private static JFrame billing = null;

	private static JOptionPane pane = new JOptionPane();

	private static int table = 0;

	private static double price = 0.0;

	private static String end_button = "";

	private static String start_label = "";

	private static String startTime = "";

	private static String confirm = "";

	private static String temp = "";

	private static String name = "";

	private static String alert = "";

	private static String warn_deadline = "";

	private static String warn_go_on = "";

	private static String tel_qq = "";

	private static String input_num = "";

	private static String passed = "";

	private static String buy_it = "";

	private static String unit = "";

	private static String begin = "";

	private static String end = "";

	private static String money = "";

	private static String playtime = "";

	private static String rmb = "";

	private static String change_price = "";
	
	private static String input_price = "";
	
	private static String modify = "";
	
	private static Properties p = Util.readConfig();
	
	static {
		initConfig();
		String laf = "com.sun.java.swing.plaf.windows.WindowsLookAndFeel";
		try {
			UIManager.setLookAndFeel(laf);
		} catch (Exception e1) {
			e1.printStackTrace();
		}
		// 先判断有没有过期
		if (Util.isValid(Util.getContractDate(), Util.getBeginDate())) {// 没有过期，则继续判断是否少于一个月
			if (!Util.lowtoOneMonth(Util.getContractDate(), new Date())) {// 不低于一个月

			} else {
				pane.showMessageDialog(
						frame,
						warn_deadline
								+ Util.getFormatDate(Util.getContractDate())
								+ warn_go_on + tel_qq);
			}
			temp = pane.showInputDialog(frame, input_num);
			if (temp != null && temp.matches("[\\d]+")) {
				table = (int) (Double.parseDouble(temp));
			}
		} else {//过期了
			pane.showMessageDialog(frame,
					 (Util.isModified()?modify:passed+Util.getFormatDate(Util.getActualContractDate()))
							+ buy_it + tel_qq);
			System.exit(0);
		}
	}

	private static BorderLayout bl = new BorderLayout();

	private static JLabel title = new JLabel(name, SwingConstants.LEFT);

	private static JPanel panel_up = new JPanel(new GridLayout(1,2));

	private static JButton change_button = new JButton(Double.parseDouble(Util.readConfig().getProperty("price")) + unit+change_price);
	
	private static JPanel panel = new JPanel(new GridLayout(2,
			(int) ((table + 1) / 2), 10, 20));

	// table个台球桌
	private static JPanel panel_child[] = new JPanel[table];
	private static JLabel label_start[] = new JLabel[table];
	private static TableBall t[] = new TableBall[table];
	private static JButton button_start[] = new JButton[table];
	private static JButton button_end[] = new JButton[table];

	private static int width = (int) (Toolkit.getDefaultToolkit()
			.getScreenSize().getWidth() * 0.9);

	private static int height = (int) (Toolkit.getDefaultToolkit()
			.getScreenSize().getHeight() * 0.9);

	private static int x = (int) Toolkit.getDefaultToolkit().getScreenSize()
			.getWidth()
			/ 2 - width / 2;

	private static int y = (int) Toolkit.getDefaultToolkit().getScreenSize()
			.getHeight()
			/ 2 - height / 2;

	private static Font font_title = new Font("null", Font.BOLD, 50);

	private static Font font_start = new Font("null", Font.BOLD, 20);

	private static Font font_button = new Font("null", Font.BOLD, 30);

	private static String message = "";

	private static ImageIcon button_image_start = new ImageIcon(
			"image/table_start.png");

	private static ImageIcon button_image_ing = new ImageIcon(
			"image/table_ing.png");

	private static void initConfig() {// 初始化读取config.properties属性文件
		systemname = p.getProperty("systemname");
		frame = new JFrame(systemname);
		billingname = p.getProperty("billingname");
		name = p.getProperty("name");
		price = Double.parseDouble((p.getProperty("price")));
		end_button = p.getProperty("end_button");
		start_label = p.getProperty("start_label");
		startTime = p.getProperty("startTime");
		confirm = p.getProperty("confirm");
		alert = p.getProperty("alert");
		warn_deadline = p.getProperty("warn_deadline");
		warn_go_on = p.getProperty("warn_go_on");
		tel_qq = p.getProperty("tel_qq");
		input_num = p.getProperty("input_num");
		passed = p.getProperty("passed");
		buy_it = p.getProperty("buy_it");
		unit = p.getProperty("unit");
		begin = p.getProperty("begin");
		end = p.getProperty("end");
		money = p.getProperty("money");
		playtime = p.getProperty("playtime");
		rmb = p.getProperty("rmb");
		change_price = p.getProperty("change_price");
		input_price = p.getProperty("input_price");
		modify = p.getProperty("modify");
	}

	private static void initFrame() {
		frame.setLayout(bl);
		title.setPreferredSize(new Dimension(width, (int) (height * 0.15)));
		title.setFont(font_title);
		title.setForeground(Color.RED);
		panel_up.add(title);
		change_button.setFont(font_button);
		change_button.setBackground(Color.BLUE);
		panel_up.add(change_button);
		frame.add(panel_up, BorderLayout.NORTH);
		for (int i = 0; i < table; i++) {
			panel_child[i] = new JPanel(new BorderLayout(0, 10));
			label_start[i] = new JLabel(start_label + startTime,
					SwingConstants.CENTER);
			label_start[i].setFont(font_start);
			t[i] = new TableBall("No."+(i + 1), Double.parseDouble(Util.readConfig().getProperty("price")));
			button_start[i] = new JButton(button_image_start);
			button_end[i] = new JButton(t[i].getId() + end_button);
			button_start[i].setFont(font_button);
			button_end[i].setFont(font_button);
			button_end[i].setEnabled(false);
		}

		for (int i = 0; i < table; i++) {
			panel_child[i].add(label_start[i], BorderLayout.NORTH);
			panel_child[i].add(button_start[i], BorderLayout.CENTER);
			panel_child[i].add(button_end[i], BorderLayout.SOUTH);
			panel.add(panel_child[i]);
		}

		for (int i = 0; i < table; i++) {
			button_start[i].addActionListener(new ActionListener() {
				@Override
				public void actionPerformed(ActionEvent e) {
					for (int j = 0; j < table; j++) {
						if (e.getSource() == button_start[j]) {
							button_end[j].setEnabled(true);
							button_start[j].setIcon(button_image_ing);
							Date d = new Date();
							Util.writeBeginFile(t[j],d);
							t[j].setStartTime(d);
							button_start[j].setEnabled(false);
							startTime = Util.getFormatTime(d);
							label_start[j].setText(start_label + startTime);
							label_start[j].setForeground(Color.RED);
						}
					}
				}
			});
			button_end[i].addActionListener(new ActionListener() {
				@Override
				public void actionPerformed(ActionEvent e) {
					int result = pane.showConfirmDialog(frame, confirm);
					if (result == 0) {
						for (int j = 0; j < table; j++) {
							if (e.getSource() == button_end[j]) {
								Date d = new Date();
								t[j].setEndTime(d);
								button_end[j].setEnabled(false);
								message = t[j].getId()
										+ "台"
										+ billingname + "      "
										+ price
										+ unit
										+ begin
										+ Util.getTimeShow(t[j].getStartTime())
										+ end
										+ Util.getTimeShow(t[j].getEndTime())
										+ playtime
										+ Util.consumeTime(t[j])
										+ money + Util.getMoney(t[j])
										+ rmb;
								showBilling(message);
								button_start[j].setEnabled(true);
								button_start[j].setIcon(button_image_start);
								label_start[j].setForeground(Color.BLACK);
								button_end[j].setEnabled(false);
								startTime = "--:--:--";
								label_start[j].setText(start_label + startTime);
							}
						}
					}
				}
			});
		}
		
		change_button.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				change_button.setEnabled(false);
				String str = pane.showInputDialog(null, input_price+"*请正确输入整数*");
				if (str != null && str.matches("[\\d]+")) {
					Util.changePrice(p, str);
				}
				price = Double.parseDouble(Util.readConfig().getProperty("price"));
				change_button.setText(price + unit+change_price);
				change_button.setEnabled(true);
			}
		});
		
		frame.add(panel);
		frame.add(panel, BorderLayout.CENTER);
		frame.setVisible(true);
		frame.setLocation(x, y);
		frame.setSize(width, height);
		//frame.setResizable(false);
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
	}

	private static void showBilling(String message) {
		billing = new JFrame(billingname);
		billing.setLayout(new FlowLayout());
		JTextArea bill = new JTextArea(6, 20);
		bill.setEditable(false);
		Font font = new Font("null", Font.BOLD, 30);
		bill.setForeground(Color.RED);
		bill.setFont(font);
		bill.setText(message);
		billing.add(bill);
		billing.setLocation(x / 4, y / 4);
		billing.setSize((int) (width * 0.5), (int) (height * 0.4));
		billing.setResizable(false);
		billing.setVisible(true);
		billing.setDefaultCloseOperation(JFrame.HIDE_ON_CLOSE);// 
	}

	public static void main(String[] args) {
		// TODO Auto-generated method stub
		if (table > 10 || table <= 0) {
			pane.showMessageDialog(frame, alert);
			System.exit(0);
		} else {
			initFrame();
		}
	}
}
