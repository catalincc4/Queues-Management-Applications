package org.example.BusinessLogic;

import org.example.GUI.SimulationFrame;
import org.example.Model.Server;
import org.example.Model.Task;

import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.LinkedBlockingDeque;
import java.util.concurrent.atomic.AtomicInteger;

public class SimulationManager implements Runnable {
    private Scheduler scheduler;
    private List<Task> tasks;
    private SimulationFrame frame;
    private SelectionPolicy selectionPolicy = SelectionPolicy.SHORTEST_QUEUE;
    private int simulationTime;
    private float averageTime;
    private FileWriter logFile;

    public SimulationManager(SimulationFrame frame) {
        this.tasks = new ArrayList<>();
        this.frame = frame;
        this.scheduler = new Scheduler();
        simulationTime = Integer.parseInt(frame.getSimulationTime().getText());
        scheduler.changeStrategy(selectionPolicy);
        int n = Integer.parseInt(frame.getNumbersOfQueues().getText());
        for (int i = 0; i < n; i++) {
            scheduler.getServers().add((new Server(new LinkedBlockingDeque<Task>(), new AtomicInteger(0))));
        }
        generateRandomTasks();
        try {
            logFile = new FileWriter("D:\\PT2022_30222_Calin_Catalin_Assigment_2\\src\\main\\resources\\org\\example\\logFile.txt");
        } catch (IOException e) {
            e.printStackTrace();
        }
    }


    public void generateRandomTasks() {
        int id = 1;
        int numberOfTask = Integer.parseInt(frame.getNumberOfClients().getText());
        int minimumArrivalTime = Integer.parseInt(frame.getMinimumArrivalTime().getText());
        int maximumArrivalTime = Integer.parseInt(frame.getMaximumArrivalTime().getText());
        int minimumServiceTime = Integer.parseInt(frame.getMinimumServiceTime().getText());
        int maximumServiceTime = Integer.parseInt(frame.getMaximumServiceTime().getText());
        for (int i = 0; i < numberOfTask; i++) {
            int serviceTime = (int) (Math.random() * (maximumServiceTime - minimumServiceTime + 1) + minimumServiceTime);
            int arivalTime = (int) (Math.random() * (maximumArrivalTime - minimumArrivalTime + 1) + minimumArrivalTime);
            Task task = new Task(id++, arivalTime, serviceTime);
            tasks.add(task);
            averageTime += serviceTime;
        }
        averageTime = averageTime / numberOfTask;
        tasks.sort(new Task.SortByArrivalTime());
    }

    public void updateFrame(int currenTime) {
        String option = frame.getComboBox().getValue();
        int queueToBeDisplay = 0;
        try {
            queueToBeDisplay = Integer.parseInt(String.valueOf(option.charAt(6))) - 1;
            Server server = scheduler.getServers().get(queueToBeDisplay);
            int numberOfTasks = server.getTasks().size();
            if (numberOfTasks == 0 && server.getWaitingPeriod().get() == 0) {
                numberOfTasks = 0;
            } else if (numberOfTasks == 0 && server.getWaitingPeriod().get() != 0) {
                numberOfTasks = 1;
            } else
                numberOfTasks += 1;

            frame.getNumberQueuesText().setText(String.valueOf(numberOfTasks));
            frame.getWaittingPeriodText().setText(String.valueOf(server.getWaitingPeriod()));
        } catch (NullPointerException | NumberFormatException e) {
        }
        frame.getProgressBar().setProgress((double) currenTime / simulationTime);


    }

    public void startThreads() {
        int n = Integer.parseInt(frame.getNumbersOfQueues().getText());
        for (int i = 0; i < n; i++) {
            if (!scheduler.getServers().get(i).getTasks().isEmpty() && !scheduler.getServers().get(i).isFlag()) {
                Thread t = new Thread(scheduler.getServers().get(i));
                t.setName("Server " + i);
                t.start();
            } else if (scheduler.getServers().get(i).getWaitingPeriod().get() == 0) {
                try {
                    logFile.append("Queue " + i + ": closed\n");
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
    }
    public void printInFile(int time) {
        try {
            int i = 0;
            logFile.append("Waiting clients: ");
            for (Task task : tasks) {
                logFile.append("(" + task.getID() + "," + task.getArrivalTime() + "," + task.getServiceTime() + "), ");
            }
            logFile.append("\n");
            for (Server server : scheduler.getServers()) {
                if (server.getWaitingPeriod().get() != 0) {
                    logFile.append("Queue " + i + ":");
                    if (server.getTasks().isEmpty()) {
                        logFile.append("1 client in progress");
                    }
                    for (Task task : server.getTasks()) {
                        logFile.append("(" + task.getID() + "," + task.getArrivalTime() + "," + task.getServiceTime() + "), ");
                    }
                    logFile.append("\n");
                }
                i++;
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void run() {
        int currentTime = 0;

        frame.getStateText().setText("Running...");
        while (currentTime < simulationTime) {
            while (!tasks.isEmpty() && tasks.get(0).getArrivalTime() == currentTime) {
                scheduler.dispatchTask(tasks.get(0));
                tasks.remove(0);
            }

            try {
                logFile.append("Time " + currentTime + "\n");
            } catch (IOException e) {
                e.printStackTrace();
            }
            printInFile(currentTime);
            startThreads();
            currentTime++;
            updateFrame(currentTime);
            try {
                Thread.sleep(1000);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }

        try {
            logFile.append("Average Time: " + averageTime);
            logFile.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
        frame.getStateText().setText("Finished!");
    }
}
