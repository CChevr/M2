package fr.uge.jee.aop.students;

import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;

@Component
public class MeasureDB {
    private final List<Long> saveTimes;
    private final List<Long> loadTimes;

    MeasureDB() {
        saveTimes = new ArrayList<>();
        loadTimes = new ArrayList<>();
    }

    public void addSaveTime(long time) {
        if(time < 0)
            throw new IllegalArgumentException("Time must be positive");
        saveTimes.add(time);
    }

    public void addLoadTime(long time) {
        if (time < 0)
            throw new IllegalArgumentException("Time must bu positive");
        loadTimes.add(time);
    }

    public String description() {
        var averageSave = saveTimes.stream().mapToDouble(x -> x).average();
        var averageLoad = loadTimes.stream().mapToDouble(x -> x).average();

        return "DB timing report:\n"+
                "\tsaveToDB\n"+
                "\t\tMeasured access times : " + saveTimes + "\n"+
                "\t\tAverage time :" + averageSave.orElse(0.) + "\n"+
                "\tloadFromDB\n" +
                "\t\tMeasured access times : "+ loadTimes + "\n" +
                "\t\tAverage time :" + averageLoad.orElse(0.) + "\n";
    }
}
