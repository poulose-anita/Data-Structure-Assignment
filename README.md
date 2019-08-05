# Data-Structure-Assignment
Data Structure Implementation (Mysterious Safeguards)

The following approach is followed to solve the problem:

1.The list of shifts in each of the given file(1.in, 2.in etc) is sorted in the increasing order of start time. 
The sorted shifts are stored in a custom array of type "Shift".

2.Identity the lifeguard to be fired. True coverage is of each life guard is the duration covered by the life alone. 
True Coverage = Shift interval - Overlap. A lifeguard with least true coverage is fired. 
The array index of the lifeguard to be fired is identified.

3.The sorted shifts excluding the shift of the fired lifeguard are merged to produce a list of non-overlapping shifts. 
Stack of the type "Shift" stores the merged shifts.The duration of each shift in the stack is added to find the total coverage.

4.The output is written to a file( 1.out, 2.out etc.).


** The java project as well as the output files  are chekced in to github.

** To run the program , place the input files in a directory named Datastrructure under the root directory identified by OS.
