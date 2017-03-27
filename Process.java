package round_robin;

/**
 * This class contains data associated with processes,
 * and methods for manipulating this data as well as
 * methods for displaying a process in the GUI.
 *
 * You will probably want to add more methods to this class.
 */
public class Process {
	/** The ID of the next process to be created */
	private static long nextProcessId = 1;
	/** The ID of this process */
	private long processId;
	/** The amount of memory needed by this process */
    private long memoryNeeded;
	/** The amount of cpu time still needed by this process */
    private long cpuTimeNeeded;
	/** The average time between the need for I/O operations for this process */
    private long avgIoInterval;
	/** The time left until the next time this process needs I/O */
    private long timeToNextIoOperation = 0;

	/** The time that this process has spent waiting in the memory queue */
	private long timeSpentWaitingForMemory = 0;
	/** The time that this process has spent waiting in the CPU queue */
	private long timeSpentInReadyQueue = 0;
	/** The time that this process has spent processing */
    private long timeSpentInCpu = 0;
	/** The time that this process has spent waiting in the I/O queue */
    private long timeSpentWaitingForIo = 0;
	/** The time that this process has spent performing I/O */
	private long timeSpentInIo = 0;

	/** The number of times that this process has been placed in the CPU queue */
	private long nofTimesInReadyQueue = 0;
	/** The number of times that this process has been placed in the I/O queue */
	private long nofTimesInIoQueue = 0;

	/** The global time of the last event involving this process */
	private long timeOfLastEvent;

	/**
	 * Creates a new process with given parameters. Other parameters are randomly
	 * determined.
	 * @param memorySize	The size of the memory unit.
	 * @param creationTime	The global time when this process is created.
	 */
	public Process(long memorySize, long creationTime) {
		// Memory need varies from 100 kB to 25% of memory size
		memoryNeeded = 100 + (long)(Math.random()*(memorySize/4-100));
		// CPU time needed varies from 100 to 10000 milliseconds
		cpuTimeNeeded = 100 + (long)(Math.random()*9900);
		// Average interval between I/O requests varies from 1% to 25% of CPU time needed
		avgIoInterval = (1 + (long)(Math.random()*25))*cpuTimeNeeded/100;
		// The first and latest event involving this process is its creation
		timeOfLastEvent = creationTime;
		// Assign a process ID
		processId = nextProcessId++;
        calculateNextIoOperation();
	}

	/**
	 * This method is called when the process leaves the memory queue (and
	 * enters the cpu queue).
     * @param clock The time when the process leaves the memory queue.
     */
    public void leftMemoryQueue(long clock) {
		  timeSpentWaitingForMemory += clock - timeOfLastEvent;
		  timeOfLastEvent = clock;
    }
    
    //used to calculate the time to the next IO operation using exponential averaging
    public void calculateNextIoOperation(){
    this.timeToNextIoOperation = (long)((Math.random() * (1.2*avgIoInterval - 0.8 * avgIoInterval)) + 0.8 * avgIoInterval);
    }

    public long getTimeToNextIOOperation(){
        return timeToNextIoOperation;
    }

    /**
	 * Returns the amount of memory needed by this process.
     * @return	The amount of memory needed by this process.
     */
	public long getMemoryNeeded() {
		return memoryNeeded;
	}

    /**
	 * Updates the statistics collected by the given Statistic object, adding
	 * data collected by this process. This method is called when the process
	 * leaves the system.
     * @param statistics	The Statistics object to be updated.
     */
	public void updateStatistics(Statistics statistics) {
		
		statistics.totalTimeSpentWaitingForMemory += timeSpentWaitingForMemory;
        statistics.totalTimeSpentInReadyQueue += timeSpentInReadyQueue;
        statistics.totalTimeSpentInCpu += timeSpentInCpu;
        statistics.totalTimeSpentWaitingForIo += timeSpentWaitingForIo;
        statistics.totalTimeSpentInIo += timeSpentInIo;

        statistics.totalNofTimesInReadyQueue += nofTimesInReadyQueue;
        statistics.totalNofTimesInIoQueue += nofTimesInIoQueue;

        statistics.nofCompletedProcesses++;
	}

	public long getProcessId() {
		return processId;
	}
    public void enteredIO(long clock){
        this.timeSpentWaitingForIo += this.timeOfLastEvent;
        timeOfLastEvent = clock;
    }

    public void leftIO(long clock){
        //after having left we must schedule for a new IO
        calculateNextIoOperation();
        this.timeSpentInIo += clock - timeOfLastEvent;
        timeOfLastEvent = clock;
    }

    public void enteredCPU(long clock){
        this.timeSpentInReadyQueue += clock - this.timeOfLastEvent;
        timeOfLastEvent = clock;
    }

    public void leftCPU(long clock){
        this.cpuTimeNeeded -= clock - timeOfLastEvent;
        this.timeToNextIoOperation -= clock - this.timeOfLastEvent;
        this.timeSpentInCpu += clock - this.timeOfLastEvent;
        timeOfLastEvent = clock;
    }

    public void enterCPUQueue(long clock){
        ++this.nofTimesInReadyQueue;
        timeOfLastEvent = clock;
    }

    public void enterIOQueue(long clock){
        this.timeOfLastEvent = clock;
    }

}
