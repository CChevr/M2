package fr.uge.jee.aop.students;

import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.ArrayList;

@Aspect
@Component
public class AspectDB {
    @Autowired
    private MeasureDB measure;

    @Around(value="execution(* fr.uge.jee.aop.students.RegistrationService.saveToDB*(..))")
    public Object aroundSave(ProceedingJoinPoint pjp) throws Throwable {
        var begin = System.currentTimeMillis();
        var ret = pjp.proceed(); // call the method
        var duration = System.currentTimeMillis() - begin;
        measure.addSaveTime(duration);
        return ret;
    }

    @Around(value="execution(* fr.uge.jee.aop.students.RegistrationService.loadFromDB*(..))")
    public Object aroundLoad(ProceedingJoinPoint pjp) throws Throwable {
        var begin = System.currentTimeMillis();
        var ret = pjp.proceed(); // call the method
        var duration = System.currentTimeMillis() - begin;
        measure.addLoadTime(duration);
        return ret;
    }
}
