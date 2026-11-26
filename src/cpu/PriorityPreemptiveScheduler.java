/*
Priority Scheduling – Preemptive - LARIN, Emmanuel A.
 */
package cpu;

import java.util.ArrayList;
import java.util.List;

public class PriorityPreemptiveScheduler implements CpuScheduler {

    public String getName() {
        return "Priority Scheduling (Preemptive)";
    }

    public boolean usesPriority() {
        return true;
    }

    public List<GanttBlock> schedule(List<Process> processes) {
        List<GanttBlock> chart = new ArrayList<GanttBlock>();
        int time = 0;
        int finishedCount = 0;
        Process running = null;   

        while (finishedCount < processes.size()) {

            // finds the process with the highest priority.
            Process best = null;
            for (Process p : processes) {
                boolean isReady = p.arrivalTime <= time && p.remainingTime > 0;
                if (isReady && (best == null || isHigherPriority(p, best))) {
                    best = p;
                }
            }

            //No process ready: the CPU is idle for this time unit.
            if (best == null) {
                GanttBlock.add(chart, "Idle", time, time + 1);
                time++;
                continue;
            }

            // this preempt only if the new process has a stictly higher priority.
            if (running != null && best.priority >= running.priority) {
                best = running;
            }

            // Run the chosen process for one time unit.
            if (best.startTime == -1) {
                best.startTime = time;         
            }
            GanttBlock.add(chart, best.pid, time, time + 1);
            best.remainingTime--;
            time++;

            // checks if its finished.
            if (best.remainingTime == 0) {
                best.completionTime = time;
                finishedCount++;
                running = null;
            } else {
                running = best;
            }
        }
        return chart;
    }

    
    private boolean isHigherPriority(Process a, Process b) {
        if (a.priority != b.priority) {
            return a.priority < b.priority;
        }
        return a.arrivalTime < b.arrivalTime;
    }
}
