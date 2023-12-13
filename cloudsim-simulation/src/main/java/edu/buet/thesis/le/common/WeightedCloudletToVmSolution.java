package edu.buet.thesis.le.common;

import org.cloudsimplus.cloudlets.Cloudlet;
import org.cloudsimplus.heuristics.CloudletToVmMappingSolution;
import org.cloudsimplus.heuristics.Heuristic;
import org.cloudsimplus.vms.Vm;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

public class WeightedCloudletToVmSolution extends CloudletToVmMappingSolution {
    public static final double PES_WEIGHT = 0.2, RAM_WEIGHT = 0.2, BANDWIDTH_WEIGHT = 0.2, THROUGHPUT_WEIGHT = 0.5, EXECUTION_TIME_WEIGHT = 0.5;
    private boolean recomputeCost = true;
    private double lastCost;

    public WeightedCloudletToVmSolution(Heuristic heuristic) {
        super(heuristic);
    }

    public WeightedCloudletToVmSolution(CloudletToVmMappingSolution solution) {
        super(solution, 1);
    }

    private void recomputeCostIfRequested() {
        if (this.recomputeCost) {
            this.lastCost = this.computeCostOfAllVms();
            this.recomputeCost = false;
        }
    }

    @Override
    public double getCost() {
        this.recomputeCostIfRequested();
        return this.lastCost;
    }

    @Override
    public double getCost(boolean forceRecompute) {
        this.recomputeCost |= forceRecompute;
        return this.getCost();
    }

    private double computeCostOfAllVms() {
        var result = this.getResult().entrySet().stream().collect(Collectors.groupingBy(Map.Entry::getValue));
        var averageCost = result.entrySet().stream().mapToDouble(this::getVmCost).average().orElse(1);
        var costSum = result.entrySet().stream().mapToDouble(this::getVmCost).map(cost -> Math.abs(cost/averageCost - 1)).sum();
        return costSum / result.size();
    }

    @Override
    public double getVmCost(Vm vm, List<Cloudlet> cloudlets) {
        double cost = 0;

        double ramUtilization = cloudlets.stream().mapToDouble(Cloudlet::getUtilizationOfRam).sum();
        double bandwidthUtilization = cloudlets.stream().mapToDouble(Cloudlet::getUtilizationOfBw).sum();
        double cpuUtilization = cloudlets.stream().mapToDouble(Cloudlet::getUtilizationOfCpu).sum();

        double pesNeeded = cloudlets.stream().mapToDouble(Cloudlet::getPesNumber).sum();
        double instructionCount = cloudlets.stream().mapToDouble(Cloudlet::getTotalLength).sum();

        double relativePesCost = pesNeeded / vm.getPesNumber();
        double relativeInstruction = instructionCount / vm.getTotalMipsCapacity();

        cost += ramUtilization * RAM_WEIGHT;
        cost += bandwidthUtilization * BANDWIDTH_WEIGHT;
        cost += cpuUtilization * THROUGHPUT_WEIGHT;

        cost += relativePesCost * PES_WEIGHT;
        cost += relativeInstruction * EXECUTION_TIME_WEIGHT;

        return cost;
    }
}
