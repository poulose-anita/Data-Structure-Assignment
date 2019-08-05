import java.io.BufferedWriter;
import java.io.File;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.IOException;
import java.io.LineNumberReader;
import java.io.OutputStreamWriter;
import java.util.Stack;

//Class to define the Life guard shift
class Shift{
	int start,end; 
	Shift(int start, int end) 
    { 
        this.start=start; 
        this.end=end; 
    } 
}

public class MaximumCoverage {

	//Reads the input file and sort increasing order of shift start time	
	//Assumption : The input file is placed in a directory named Datastructure in the root directory identified by the OS
	public static Shift[] sortShift( String fileName) throws IOException {
		String homeDir = System.getProperty("user.home");
		File fileIn =  new File(homeDir + File.separator + "Datastructure" + File.separator + fileName + ".in"); //Input File
		FileReader fileRead =  new FileReader(fileIn);
		LineNumberReader lineReader = new LineNumberReader(fileRead);
		String line = lineReader.readLine();
		String [] time;
		Shift shifts[]=new Shift[Integer.parseInt(line)];
		
		//Read from the file 
		int i=0;
		while ( (line = lineReader.readLine())!=null) {
			
			if (lineReader.getLineNumber()!=1) {
				time = line.split(" ");				
				 shifts[i]=   new Shift(Integer.parseInt(time[0]), Integer.parseInt(time[1]));
				i++;
			}		
		}
		
		//Sorting the values read into shifts array
		Shift temp[] = new Shift[1]	;
		for(int j = 0; j < shifts.length; j++) {
			for(int k = j+1; k < shifts.length; k++) {
				if (shifts[j].start > shifts[k].start )
				{
					 temp[0] = shifts[j];
					 shifts[j] = shifts[k];
					 shifts[k] = temp[0];
				}
			}			
		}
		
		lineReader.close();//File close
		return shifts;
	}	
	
	
	//The method finds the takes the sorted(increasing order) array of shifts as the input
	//The method is calculates the true_coverage(Duration the life guard covers alone) by each life guard
	//The life guard with least true coverage is fired to retain maximum coverage
	public static int findMysteriousLifeguard(Shift shifts[]) {
		
		int lifeguardIndex  = 0;
		char completeOverlap = 'N';
		int interval = 0;
		int overlap =0;
		int overlapEnd =0;
		int trueCoverage = 0;
		int trueCoveragePrevious = 0;
		
		//Looping through each shift to find the true coverage - (True coverage  = Duration covered - overlap time)
        for (int i = 0; i < shifts.length; i++) {
        	interval =  shifts[i].end -shifts[i].start;
        	overlapEnd = shifts[i].start;
        	trueCoverage = 0;
        	overlap =0;
         if(completeOverlap=='Y')break;
         
         //Looping through all shifts with start_time greater than the end time of the shift to calculate overlap
        	for(int j = 0 ; j < shifts.length; j++) {
        		if(j==i)continue;  //Do not  compare with same shift
        		
        		//All shifts with start time lesser than the start time of the shift
        		if(j < i) {
        			if (shifts[j].end >= shifts[i].end) {
        				completeOverlap = 'Y';
        				overlap = interval;
        				break;
        			}
        			if((shifts[j].end < shifts[i].end) && (shifts[j].end > shifts[i].start )) {
        				overlap = overlap + (shifts[j].end - overlapEnd);
        				overlapEnd = shifts[j].end;			
        			}
        			
        		}
        		
        		//All shifts with start time greater than the start time of the shift
        		if(j>i) {
        			
        			if (shifts[j].start>=shifts[i].end) break;
        			if (shifts[j].start<shifts[i].end) {
        				overlapEnd = (shifts[j].start >overlapEnd)?shifts[j].start:overlapEnd;
        				
        				if( shifts[j].end >= shifts[i].end) {
        					{
        					overlap = overlap + (shifts[i].end - overlapEnd);
        					overlapEnd =shifts[i].end;
        					}
        				}
        					else 
        					{
        						overlap = overlap + (shifts[j].end - overlapEnd);
        				        overlapEnd =shifts[j].end;
        					}
        						}
     	
        		}
   
        		if(completeOverlap=='Y')break;	
		
		}

        	trueCoverage = interval-overlap;
		    if (i==0)trueCoveragePrevious = trueCoverage;
			if (trueCoverage < trueCoveragePrevious) {
				 lifeguardIndex = i;
					trueCoveragePrevious = trueCoverage;
			 }
        }
        
		return lifeguardIndex;
	}
	
	
	//Merge the overlapping shifts after excluding the shift of the mysterious lifeGuard
	//Calculate the time covered by the life guards
	public static int mergeOverlappingShifts(int indexMysteriousLifeguard,Shift[] sortedShifts){
		Stack <Shift> mergedShifts = new Stack <Shift> ();
		int i= 0;
		if (indexMysteriousLifeguard!=0) {
		mergedShifts.push(sortedShifts[0]);
		 i = 1;
		}
		else
		{
		mergedShifts.push(sortedShifts[1]);	
		 i=2;
		
		}
		for(int j = i ; j < sortedShifts.length; j++) {
			Shift top = mergedShifts.peek();
			if (j==indexMysteriousLifeguard)
				continue;
			if (top.end < sortedShifts[j].start)
				mergedShifts.push(sortedShifts[j]);
			else if (top.end < sortedShifts[j].end){
				top.end = sortedShifts[j].end;
				mergedShifts.pop();
				mergedShifts.push(top);
			}
			
		}
		
		int totalCoverage = 0;
    while(!mergedShifts.empty()) {
	Shift print = mergedShifts.pop();
	totalCoverage =  totalCoverage + (print.end - print.start);
}
		return totalCoverage; 
	}
	
	
    //Main method
	//Looping though all the 10 input files
	//Assumption : The input file is placed in a directory named Datastructure in the root directory identified by the OS
	//The output files with maximum coverage time will be created in a directory named Datastructure in the root directory identified by the OS
			
	public static void main(String[] args) {
		
		Shift [] sortedShifts;
		int totalCoverage=0;
		
		for(int i=1; i<=10;i++) {	
    try {
    	//Sort the shift in creasing order of start time and store in a array of type custom class , Shift
		sortedShifts = sortShift(Integer.toString(i));
		
		//Find the index of the life guard to be fired in the sorted shifts
		int lifeguardIndex = findMysteriousLifeguard(sortedShifts);	
		
		//Merge the overlapping shifts excluding the shift of the life guard to be fired and calculate the total coverage
		totalCoverage = mergeOverlappingShifts(lifeguardIndex,sortedShifts);
		
		//Write to the output file 
		String homeDir = System.getProperty("user.home");
		File fileOut =  new File(homeDir + File.separator + "Datastructure" + File.separator   +Integer.toString(i) + ".out" );
		FileOutputStream fileWriteCoverageTime	= new FileOutputStream(fileOut);
		BufferedWriter writeCoverageTime	= new BufferedWriter(new OutputStreamWriter(fileWriteCoverageTime));
		writeCoverageTime.write(Integer.toString(totalCoverage));
		writeCoverageTime.close(); //File close
	} catch (IOException e) {
		e.printStackTrace();
	}
	}
		
	}

}
