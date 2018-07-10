package com.amrtyu;

import java.util.LinkedList;
import java.util.Random;

/*
 * 1) Thread t1 & Thread t2 objects are created
 * 2) Thread t1 is calling addRanddomNumber() method 
 * 3) Thread t2 is calling verifyPrimeNumber() method
 * 4) Both t1 and t2 threads sharing queue1 object for add & remove random integer 
 * 5) Both t1 and t2 threads are synchronized; so that common resource will be accessed one thread at a time 
 * 6) Thread t1 calls addRandomNumber() method, this method will add random number into the queue  
 * 7) Thread t1 calls notify() method after calling the add() method for waiting thread to process
 * 8) Thread t1 calls wait() method once the queue reaches the maximum size
 * 9) Thread t2 wake up as soon as thread t1 go into the waiting mode
 * 10) Thread t2 calls wait() method once the queue reaches size 0
 * 11) Thread t2 calls notify() method after calling the removeFirst() method for waiting thread to process
 * 12) Both thread t1 and t2 will share the resource synchronized way 
 */

public class RandomizerPrimeApp {
	LinkedList<Integer> queue1 = new LinkedList<Integer>();
	private static final int MAX = 10;
	Random random = new Random();

	public void addRandomNumber() {
		while (true) {
			synchronized (this) {
				while (queue1.size() == MAX) {
					try {
						wait();
					} catch (InterruptedException e) {
						e.printStackTrace();
					}
				}
				int randomNumber = random.nextInt(10);
				queue1.add(randomNumber);
				System.out.println("Queue size:" + queue1.size()
						+ " Random number added --> " + randomNumber);
				notify();
			}
		}
	}

	public void verifyPrimeNumber() {
		while (true) {
			synchronized (this) {
				while (queue1.size() == 0) {
					try {
						wait();
					} catch (InterruptedException e) {
						e.printStackTrace();
					}
				}
				System.out.print("Queue size:" + queue1.size());
				int value = queue1.removeFirst();
				notify();
				System.out.println(" Random number -->" + value
						+ " isPrime --> " + isPrimeNumber(value));
				try {
					Thread.sleep(500);
				} catch (InterruptedException e) {
					e.printStackTrace();
				}
			}
		}
	} 
 
	public boolean isPrimeNumber(int primeNumber) {
		if (primeNumber > 0) {
			for (int i = 2; i < primeNumber; i++) {
				if (primeNumber % i == 0)
					return false;
			}
		}
		return true;
	}

	public static void main(String[] args) {
		RandomizerPrimeApp app = new RandomizerPrimeApp();

		Thread t1 = new Thread(new Runnable() {
			public void run() {
				app.addRandomNumber();
			}
		});
		t1.start();

		Thread t2 = new Thread(new Runnable() {
			public void run() {
				app.verifyPrimeNumber();
			}
		});
		t2.start();
	}

}
