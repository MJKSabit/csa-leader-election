# Crow Search Based Leader Election

In this project we will implement crow search based leader election of distributed system in CloudSim Plus. 

In static leader election like bully and ring algorithm, there is a predefined order or priority for each host and leader is elected based on that priority. 
In our approach, we will be changing the priority according to the workload before next scheduling interval starts. In this approach, whenever a leader fails,
next elected leader will be able to perform extra tasks without increasing execution time or decreasing performace more than it should be.

## Presentation Slides

- Leader Election Comparison
- CSA and Schedule

## CloudSim Plus Simulation
```
src/main/java/edu/buet/thesis/le
├── Main.java
├── common
│   ├── SimulationAbstractFactory.java
│   ├── VmExtended.java
│   └── WeightedCloudletToVmSolution.java
├── csa
│   └── CloudletToVmMappingCrowSearchAlgorithm.java
└── ga
    └── CloudletToVmMappingGeneticAlgorithm.java
pom.xml
```
