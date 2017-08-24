# Travelling-Salesman
A genetic algorithm that solves the Travelling Salesman problem using different Genetic Algorithm strategies

## Simulated Annealing
At the end of each generation, 2 random genes (cities) are switched.  Whether this new solution is kept is based on if the solution is better and how many generations have passed.
## Multi-Population
There are many populations that, at the end of each generation, may send a random solution to another population in a form of migration.
## Niching (Crowding)
Each population's fitness is penalized based on how similar each of the solutions are.  The more similar they are, the greater the penalty
## Insertion
Every so often, an old solution is inserted into the current population to increase diversity.
