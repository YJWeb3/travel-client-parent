package com.zheng.travel.client.middle.jdk;

public class SellTicket implements Runnable{

	private int ticket = 100;
	private Object obj = new Object();
	
	@Override
	public void run() {
		// 卖光位置 ---请求和响应
		while(ticket > 0 ) {
			this.maketicket();
		}
	}

	public synchronized void maketicket(){
		// 判断当前票是否充足
		if(ticket > 0 ) {
			try {
				Thread.sleep(100);
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
			// 售票
			System.out.println(Thread.currentThread().getName()+"正在出售第："+ticket--+"张票!");
		}
	}

	public static void main(String[] args){
		// 买票测试
		SellTicket sellTicket = new SellTicket();
		for (int i = 1  ; i <=3 ; i++) {
			new Thread(sellTicket,"窗口-"+i).start();
		}
	}
	
}
