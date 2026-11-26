
package cpu;

import java.util.ArrayList;
import java.util.List;

public class FcfsScheduler implements CpuScheduler {

    public String getName() {
        return "First-Come, First-Served (FCFS)";
    }

    public boolean usesPriority() {
        return false;
    }

    public List<GanttBlock> schedule(List<Process> processes) {
        List<GanttBlock> chart = new ArrayList<GanttBlock>();
        boolean[] finished = new boolean[processes.size()];
        int time = 0;

        for (int count = 0; count < processes.size(); count++) {

           
            int next = -1;
            for (int i = 0; i < processes.size(); i++) {
                if (!finished[i]) {
                    if (next == -1 || processes.get(i).arrivalTime < processes.get(next).arrivalTime) {
                        next = i;
                    }
                }
            }
            Process p = processes.get(next);

            
            if (time < p.arrivalTime) {
                GanttBlock.add(chart, "Idle", time, p.arrivalTime);
                time = p.arrivalTime;
            }

            
            p.startTime = time;
            time = time + p.burstTime;
            p.completionTime = time;
            GanttBlock.add(chart, p.pid, p.startTime, p.completionTime);

            finished[next] = true;
        }
        return chart;
    }
}
