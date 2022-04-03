package org.example.BusinessLogic;

import org.example.Model.Server;
import org.example.Model.Task;

import java.util.List;
import java.util.concurrent.atomic.AtomicInteger;

public class ShortestQueueStrategy implements Strategy{
    @Override
    public List<Server> addTask(List<Server> servers, Task task) {
        int i = 0;
        int minServerQueue = 0;
        int minQueue = 999999;
        boolean flag = false ;
        while(i < servers.size()){

            int j = servers.get(i).getTasks().size();
            int maxNumberTask = servers.get(i).getMaxTaskNumber();
            if(minQueue > j && j < maxNumberTask){
                minQueue = j ;
                minServerQueue = i;
                flag = true;
            }
            i++;
        }
        if(flag) {

            AtomicInteger atomicInteger1 = servers.get(minServerQueue).getWaitingPeriod();
            servers.get(minServerQueue).getTasks().add(task);
            servers.get(minServerQueue).setWaitingPeriod(new AtomicInteger(atomicInteger1.addAndGet(task.getServiceTime())));
        }

        return servers;
        }
}