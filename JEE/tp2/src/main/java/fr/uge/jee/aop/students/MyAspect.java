package fr.uge.jee.aop.students;

import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.annotation.AfterReturning;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Before;
import org.springframework.stereotype.Component;

@Aspect
@Component
public class MyAspect {
    @Before("execution(* fr.uge.jee.aop.students.RegistrationService.create*(..))")
    public void beforeCreate(JoinPoint jp) throws Throwable {
        System.out.println("Before create");
    }

    @AfterReturning(value="execution(* fr.uge.jee.aop.students.RegistrationService.create*(..))", returning="value")
    public void afterCreate(JoinPoint jp, Object value) throws Throwable {
        System.out.println("After create");
    }
}
