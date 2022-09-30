package fr.uge.jee.aop.students;

import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.AfterReturning;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Before;
import org.springframework.stereotype.Component;

import java.util.Arrays;

@Aspect
@Component
public class MyAspect {
    /*
    @Before("execution(* fr.uge.jee.aop.students.RegistrationService.create*(..))")
    public void beforeCreate(JoinPoint jp) throws Throwable {
        System.out.println("Before create");
    }

    @AfterReturning(value="execution(* fr.uge.jee.aop.students.RegistrationService.create*(..))", returning="value")
    public void afterCreate(JoinPoint jp, Object value) throws Throwable {
        System.out.println("After create");
    }
     */

    @Around(value="execution(* fr.uge.jee.aop.students.RegistrationService.create*(..))")
    public Object aroundCreate(ProceedingJoinPoint pjp) throws Throwable {
        var methodName = pjp.getSignature().getName();
        var args = Arrays.toString(pjp.getArgs());

        System.out.println("Calling "+methodName+" with arguments "+ args);
        var ret = pjp.proceed(); // call the method
        System.out.println("Return id "+ret+" by "+methodName);

        return ret;
    }
}
