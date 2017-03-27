package round_robin;

import java.util.LinkedList;

/**
 * This class implements functionality associated with
 * the CPU unit of the simulated system.
 */
public class Cpu {
    private Statistics statistics;
    private long maxCpuTime;
    private Process activeProcess;
    private EventQueue eventQueue;
    private long lastEvent;
    private LinkedList<Process> cpuQueue;

    /**
     * Creates a new CPU with the given parameters.
     * @param cpuQueue		The CPU queue to be used.
     * @param maxCpuTime	The Round Robin time quant to be used.
     * @param statistics	A reference to the statistics collector.
     */
    public Cpu(LinkedList<Process> cpuQueue, long maxCpuTime, Statistics statistics) {
        // Incomplete
        this.cpuQueue = cpuQueue;
        this.maxCpuTime = maxCpuTime;
        this.statistics = statistics;
        this.activeProcess = null;
        lastEvent = 0;
    }

    /**
     * Adds a process to the CPU queue, and activates (switches in) the first process
     * in the CPU queue if the CPU is idle.
     * @param p		The process to be added to the CPU queue.
     * @param clock	The global time.
     * @return		The event causing the process that was activated to leave the CPU,
     *				or null	if no process was activated.
     */
    public Event insertProcess(Process p, long clock) {
        // Incomplete
        cpuQueue.add(p);
        this.statistics.totalNofTimesInReadyQueue++;
        p.enterCPUQueue(clock);
        return null;
    }

    /**
     * Activates (switches in) the first process in the CPU queue, if the queue is non-empty.
     * The process that was using the CPU, if any, is switched out and added to the back of
     * the CPU queue, in accordance with the Round Robin algorithm.
     * @param clock	The global time.
     * @return		The event causing the process that was activated to leave the CPU,
     *				or null	if no process was activated.
     */
    public Event switchProcess(long clock) {
        // Incomplete
        Process prev = activeProcess;
        if (prev != null){
            prev.leftCPU(clock);
            insertProcess(prev, clock);
        }
        activeProcessLeft(clock);
        if(activeProcess == null){
            activeProcess.enteredCPU(clock);

        }
        return new Event(Event.SWITCH_PROCESS, clock + maxCpuTime);
    }

    /**
     * Called when the active process left the CPU (for example to perform I/O),
     * and a new process needs to be switched in.
     * @return	The event generated by the process switch, or null if no new
     *			process was switched in.
     */
    public Event activeProcessLeft(long clock) {
        // Incomplete
        return null;
    }

    /**
     * Returns the process currently using the CPU.
     * @return	The process currently using the CPU.
     */
    public Process getActiveProcess() {
        // Incomplete
        return this.activeProcess;
    }

    /**
     * This method is called when a discrete amount of time has passed.
     * @param timePassed	The amount of time that has passed since the last call to this method.
     */
    public void timePassed(long timePassed) {
        // Incomplete
    }

}
