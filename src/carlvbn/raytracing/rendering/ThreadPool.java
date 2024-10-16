package carlvbn.raytracing.rendering;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ArrayBlockingQueue;

public class ThreadPool {
	ArrayBlockingQueue<Runnable> jobs;
	List<Thread> workers;
	
	public ThreadPool(int taille, int nbworker) {
		jobs = new ArrayBlockingQueue(taille);
		workers = new ArrayList<Thread>();
		for(int i = 0; i<nbworker;i++) {
			Thread td= new Thread(new PoolWorker());
			workers.add(td);
			td.start();
		}
	}
	
	
	private class PoolWorker implements Runnable{
		public void run() {
			try {
				while(! Thread.interrupted()) {
					Runnable job = jobs.take();
					job.run();
				}
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
	}
	
	public void submit(Runnable job) {
		jobs.add(job);
	}
	
	public void shutdown() {
		for(Thread t: workers) {
			t.interrupt();
			try {
				t.join();
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
		workers.clear();
		jobs.clear();
	}	
}
