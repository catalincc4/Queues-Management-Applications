package org.example.BusinessLogic;

import org.example.Model.Server;
import org.example.Model.Task;

import java.util.List;
import java.util.concurrent.atomic.AtomicInteger;

public class TimeStrategy implements Strategy {
    @Override
    public List<Server> addTask(List<Server> servers, Task task) {
        int i = 0;
        int minServerTime = 0;
        boolean flag = false;
        AtomicInteger minTime = new AtomicInteger(9999999);
        while (i < servers.size()) {
            AtomicInteger j = servers.get(i).getWaitingPeriod();
            int maxNumberTask = servers.get(i).getMaxTaskNumber();
            int numberOfTasks = servers.get(i).getTasks().size();
            if (j.get() < minTime.get() && numberOfTasks < maxNumberTask) {
                minTime = j;
                minServerTime = i;
                flag = true;
            }
            i++;
        }
        if (flag) {
            AtomicInteger atomicInteger1 = servers.get(minServerTime).getWaitingPeriod();
            servers.get(minServerTime).getTasks().add(task);
            servers.get(minServerTime).setWaitingPeriod(new AtomicInteger(atomicInteger1.addAndGet(task.getServiceTime())));
        }
        return servers;
    }
}
