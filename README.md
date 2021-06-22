# Evolution-Simulator

I made this simulator for my third-year project in university. It simulates a scenario where an environment is inhabited by plants who must compete for sunlight. These plants can reproduce and evolve to live longer or shorter lives, reach larger sizes, grow more aggressively, and so on. It's not designed to simulate exactly how evolution works, but rather reproduce the results produced by natural evolution. Specifically, it was intended to demonstrate the processes of adaptation, coevolution, speciation and extinction. I wrote my dissertation on this simulator, and explained in detail how it works, the results it produces and what they mean, but I will summarise that here.

### Development Environment
- Programmed in JavaSE-14
- Uses JavaFX 11.0.2

### Overview
- The program simulates an environment that is split into a number of tiles.
- Each tile can have any number of plants inhabiting it.
- Each time step, every tile receives sunlight, some of which is absorbed by plants on that tile as energy.
  Sunlight is absorbed such that the largest plant on a tile absorbs a portion of the initial sunlight, the second largest absorbs a portion of the remaining sunlight, and so on.
- Plants lose a small amount of energy each time step and will die if they run out.
- Plants can also spend energy to grow larger and to reproduce.
- Whenever a plant reproduces, its offspring will have slight genetic differences that affect their survival.

### Genetics
- Each plant has genes for its lifespan, max size, mutation rate, growth behaviour, reproduction behaviour, seeding range and offspring size.
- Genes are modelled as a number that represents a trait and is incremented or decremented during mutation.
  For example, lifespan represents the number of time steps before a plant dies of age, can be between 100 and 1000, and changes by increments of 100 during mutation.
- Having a higher or lower value for any gene always has advantages and disadvantages to its survival.
  For example, a longer lifespan gives more time to grow and reproduce, however it also increases the cost of surviving each time step.
- When a plant reproduces, a number of mutations specified by its mutation rate gene will be applied randomly to the offspring’s genes.
- The growth and reproduction behaviour genes specify how much energy should be kept in reserve when growing or reproducing.
  For example, if a plant has a growth behaviour of 0.4, then it will only grow when doing so will leave it with more than 40% of its max energy.

### Statistical Analysis
- The stats window contains 10 graphs that update as the simulation runs.
- The first shows the size of plants on a histogram.
- The second shows the total size of the population over time.
- The third shows the average value of each gene over time.
- And the other 7 are histograms showing the distribution of values of each gene in the population.

### User Control
- The user can control the speed of simulation, either in batches or continuously.
- They can create new randomly generated plants.
- They can click on any plant to see its traits.
- They can set a periodic cull of the population.
  For example, a cull with a period of 1000 and radius 3 will randomly select a 7 by 7 area every 1000 time steps and kill all plants in it.

### Code Structure
I wrote this program in the most adaptable way I could because I had many features in mind that I wasn’t sure I'd have time to add. For example, I originally planned to add creatures other than plants, such as herbivores, carnivores, and parasites, so the program is still structured to allow for them to be added. As a result, it should be fairly easy to modify the existing code and add new functions.
