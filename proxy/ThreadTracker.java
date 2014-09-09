package proxy;

public class ThreadTracker implements Runnable{

	public void run() {
		
		while(true){
			System.out.println("THREAD COUNT:"+Thread.currentThread().getThreadGroup().activeCount());
			try {
				Thread.sleep(5000);
			} catch (InterruptedException e2) {
				e2.printStackTrace();
			}
		}
	}
}
