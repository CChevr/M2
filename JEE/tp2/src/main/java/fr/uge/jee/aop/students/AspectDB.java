package fr.uge.jee.aop.students;

import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.AfterReturning;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.springframework.stereotype.Component;

import java.sql.Array;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

@Aspect
@Component
public class AspectDB {
    private final List<Long> saveTimes;
    private final List<Long> loadTimes;

    AspectDB() {
        saveTimes = new ArrayList<>();
        loadTimes = new ArrayList<>();
    }

    @Around(value="execution(* fr.uge.jee.aop.students.RegistrationService.saveToDB*(..))")
    public Object aroundSave(ProceedingJoinPoint pjp) throws Throwable {
        var begin = System.currentTimeMillis();
        var ret = pjp.proceed(); // call the method
        var duration = System.currentTimeMillis() - begin;
        saveTimes.add(duration);
        return ret;
    }

    @Around(value="execution(* fr.uge.jee.aop.students.RegistrationService.loadFromDB*(..))")
    public Object aroundLoad(ProceedingJoinPoint pjp) throws Throwable {
        var begin = System.currentTimeMillis();
        var ret = pjp.proceed(); // call the method
        var duration = System.currentTimeMillis() - begin;
        loadTimes.add(duration);
        return ret;
    }

    @Override
    public String toString() {
        return "DB timing report:\n"+
            "\tsaveToDB\n"+
            "\tMesured access times : " + saveTimes + "\n"+
            "\tAverage time :" + " \n"+
            "\tloadFromDB\n" +
            "\tMesured access times : "+ loadTimes + "\n" +
            "\tAverage time :107.5\n";
    }
}
