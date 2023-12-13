package edu.buet.thesis.le;

import ch.qos.logback.classic.Level;
import edu.buet.thesis.le.common.SimulationAbstractFactory;
import org.cloudsimplus.util.Log;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.BufferedWriter;
import java.io.FileWriter;

public class Main {
    private static final Logger logger = LoggerFactory.getLogger(Main.class.getSimpleName());

    private static int HOSTS_TO_CREATE = 100;
    private static int VMS_TO_CREATE = 50;
    private static int CLOUDLETS_TO_CREATE = 100;

    public static void main(String[] args) {
        if (args.length > 0)
            CLOUDLETS_TO_CREATE = Integer.parseInt(args[0]);
        //Enables just some level of log messages.
        Log.setLevel(Level.WARN);

        int[] hosts = {65};
        int[] vms = {100};
        int[] cloudlets = {10, 20, 35, 50, 65, 80, 100, 150, 200};

        for (int host : hosts) {
            for (int vm : vms) {
                for (int cloudlet : cloudlets) {
                    if (cloudlet > 2*host || vm > 2*host || cloudlet > 3*vm) continue;
                    for (int i = 0; i < 10; i++)
                        simulate(host, vm, cloudlet);
                }
            }
        }
    }

    private static void simulate(int HOSTS_TO_CREATE, int VMS_TO_CREATE, int CLOUDLETS_TO_CREATE) {
        System.out.printf("Starting Simulations [%d, %d, %d]\n", HOSTS_TO_CREATE, VMS_TO_CREATE, CLOUDLETS_TO_CREATE);
        final var factory = new SimulationAbstractFactory(HOSTS_TO_CREATE, VMS_TO_CREATE, CLOUDLETS_TO_CREATE);

        var geneticAlgorithm = factory.getAlgorithm(SimulationAbstractFactory.GENETIC_ALGORITHM);
        var crowSearchAlgorithm = factory.getAlgorithm(SimulationAbstractFactory.CROW_SEARCH_ALGORITHM);

        System.out.printf(
                "[%4s] Execution Time: \t\t%.2f\t -> \t%.2f\n",
                crowSearchAlgorithm.getExecutionTime() <= geneticAlgorithm.getExecutionTime() ? "CSA" : "GA",
                geneticAlgorithm.getExecutionTime(),
                crowSearchAlgorithm.getExecutionTime()
        );

        System.out.printf(
                "[%4s] Fitness: \t\t\t%.6f\t -> \t%.6f\n",
                crowSearchAlgorithm.getFitness() >= geneticAlgorithm.getFitness() ? "CSA" : "GA",
                geneticAlgorithm.getFitness(),
                crowSearchAlgorithm.getFitness()
        );

        System.out.printf(
                "[%4s] CPU Utilization: \t%.2f\t -> \t%.2f\n",
                crowSearchAlgorithm.getCPUUtilization() >= geneticAlgorithm.getCPUUtilization() ? "CSA" : "GA",
                geneticAlgorithm.getCPUUtilization(),
                crowSearchAlgorithm.getCPUUtilization()
        );

        System.out.printf(
                "[%4s] RAM Utilization: \t%.2f\t -> \t%.2f\n",
                crowSearchAlgorithm.getMemoryUtilization() >= geneticAlgorithm.getMemoryUtilization() ? "CSA" : "GA",
                geneticAlgorithm.getMemoryUtilization(),
                crowSearchAlgorithm.getMemoryUtilization()
        );

        System.out.printf(
                "[%4s] B/W Utilization: \t%.2f\t -> \t%.2f\n",
                crowSearchAlgorithm.getBandwidthUtilization() >= geneticAlgorithm.getBandwidthUtilization() ? "CSA" : "GA",
                geneticAlgorithm.getBandwidthUtilization(),
                crowSearchAlgorithm.getBandwidthUtilization()
        );

        System.out.printf(
                "[%4s] CPU Usage Mean: \t\t%.2f\t -> \t%.2f\n",
                crowSearchAlgorithm.getCPUUsageMean() >= geneticAlgorithm.getCPUUsageMean() ? "CSA" : "GA",
                geneticAlgorithm.getCPUUsageMean(),
                crowSearchAlgorithm.getCPUUsageMean()
        );

        System.out.printf(
                "[%4s] CPU Usage StdDev: \t%.2f\t -> \t%.2f\n",
                crowSearchAlgorithm.getCPUUsageStandardDeviation() <= geneticAlgorithm.getCPUUsageStandardDeviation() ? "CSA" : "GA",
                geneticAlgorithm.getCPUUsageStandardDeviation(),
                crowSearchAlgorithm.getCPUUsageStandardDeviation()
        );

        System.out.printf("[%4s] Power Consumption: \t%.2f\t -> \t%.2f\n",
                crowSearchAlgorithm.powerConsumptionMean() <= geneticAlgorithm.powerConsumptionMean() ? "CSA" : "GA",
                geneticAlgorithm.powerConsumptionMean(),
                crowSearchAlgorithm.powerConsumptionMean()
        );

        System.out.println("Finished Simulations");
        writeToFile(CSV_FILE_PATH, "%d,%d,%d,%.2f,%.2f,%.2f,%.2f,%.2f,%.2f,%.6f,%.6f,%.6f,%.6f,%.6f,%.6f,%.2f,%.2f,%.2f,%.2f\n".formatted(
                HOSTS_TO_CREATE,
                VMS_TO_CREATE,
                CLOUDLETS_TO_CREATE,
                geneticAlgorithm.getExecutionTime(),
                crowSearchAlgorithm.getExecutionTime(),
                geneticAlgorithm.getFitness(),
                crowSearchAlgorithm.getFitness(),
                geneticAlgorithm.getCPUUtilization(),
                crowSearchAlgorithm.getCPUUtilization(),
                geneticAlgorithm.getMemoryUtilization(),
                crowSearchAlgorithm.getMemoryUtilization(),
                geneticAlgorithm.getBandwidthUtilization(),
                crowSearchAlgorithm.getBandwidthUtilization(),
                geneticAlgorithm.powerConsumptionMean(),
                crowSearchAlgorithm.powerConsumptionMean(),
                geneticAlgorithm.getCPUUsageMean(),
                crowSearchAlgorithm.getCPUUsageMean(),
                geneticAlgorithm.getCPUUsageStandardDeviation(),
                crowSearchAlgorithm.getCPUUsageStandardDeviation()
        ));
    }


    public static final String CSV_FILE_PATH = "data.csv";
    private static void writeToFile(String filePath, String content) {
        try (FileWriter fw = new FileWriter(filePath, true);
             BufferedWriter writer = new BufferedWriter(fw);) {
            writer.write(content);
        } catch (Exception e) {
            System.out.println(e);
        }
    }
}
