package timingtest;
import edu.princeton.cs.algs4.Stopwatch;

/**
 * Created by hug.
 */
public class TimeAList {
    private static void printTimingTable(AList<Integer> Ns, AList<Double> times, AList<Integer> opCounts) {
        System.out.printf("%12s %12s %12s %12s\n", "N", "time (s)", "# ops", "microsec/op");
        System.out.printf("------------------------------------------------------------\n");
        for (int i = 0; i < Ns.size(); i += 1) {
            int N = Ns.get(i);
            double time = times.get(i);
            int opCount = opCounts.get(i);
            double timePerOp = time / opCount * 1e6;
            System.out.printf("%12d %12.2f %12d %12.2f\n", N, time, opCount, timePerOp);
        }
    }

    public static void main(String[] args) {
        timeAListConstruction();
    }

    public static void timeAListConstruction() {
        // TODO: YOUR CODE HERE
        int baseNum = 1000;
        AList<Integer> NsAList       = new AList<>();
        AList<Double> timesAList     = new AList<>();
        AList<Integer> opCountsAList = new AList<>();
        AList<Integer> AListGroup    = new AList<>();

        Stopwatch sw = new Stopwatch();

        for (int iter = 0; iter < 8; iter++) {
            for (int i = 0; i < baseNum; ++i) {
                AListGroup.addLast(1);
            }
            if (iter > 0) {
                baseNum *= 2;
            }
            NsAList.addLast(baseNum);
            opCountsAList.addLast(baseNum);
            timesAList.addLast(sw.elapsedTime());
        }

        printTimingTable(NsAList, timesAList, opCountsAList);
    }

}
