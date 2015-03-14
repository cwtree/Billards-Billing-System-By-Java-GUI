package cn.edu.ustc;

import java.util.Date;

public class TableBall {

	private String id;
	
	private Date startTime;
	
	private Date endTime;
	
	private double money;

	private double price;
	
	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public Date getStartTime() {
		return startTime;
	}

	public void setStartTime(Date startTime) {
		this.startTime = startTime;
	}

	public Date getEndTime() {
		return endTime;
	}

	public void setEndTime(Date endTime) {
		this.endTime = endTime;
	}

	public double getMoney() {
		return money;
	}

	public void setMoney(double money) {
		this.money = money;
	}

	public double getPrice() {
		return price;
	}

	public void setPrice(double price) {
		this.price = price;
	}

	public TableBall(String id,double price) {
		super();
		this.id = id;
		this.price = price;
	}
	public  TableBall() {
		
	}
	
}
