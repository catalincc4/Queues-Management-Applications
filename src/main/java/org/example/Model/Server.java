package org.example.Model;

import java.util.concurrent.BlockingQueue;
import java.util.concurrent.atomic.AtomicInteger;

public class Server implements Runnable{
    private BlockingQueue<Task> tasks;
    private AtomicInteger waitingPeriod;
    private int maxTaskNumber = 20;
    private boolean flag = false;

    public Server(BlockingQueue<Task> tasks, AtomicInteger waitingPeriod) {
        this.tasks = tasks;
        this.waitingPeriod = waitingPeriod;
    }

    public int getMaxTaskNumber() {
        return maxTaskNumber;
    }

    public BlockingQueue<Task> getTasks() {
        return tasks;
    }

    public void setTasks(BlockingQueue<Task> tasks) {
        this.tasks = tasks;
    }

    public AtomicInteger getWaitingPeriod() {
        return waitingPeriod;
    }

    public boolean isFlag() {
        return flag;
    }

    public void setWaitingPeriod(AtomicInteger waitingPeriod) {
        this.waitingPeriod = waitingPeriod;
    }

    @Override
    public void run() {
     int i = 0;
     flag = true;
      while(!tasks.isEmpty()){
          try {
              Task task = tasks.take();
              Thread.sleep((long)(task.getServiceTime()* 1000));
              waitingPeriod = new AtomicInteger(waitingPeriod.addAndGet(-task.getServiceTime()));
          } catch (InterruptedException e) {
              e.printStackTrace();
          }
      }
      flag=false;
    }
}
