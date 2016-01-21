package ru.xxlabaza.aspect;

import java.util.stream.Collectors;
import java.util.stream.Stream;
import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.After;
import org.aspectj.lang.annotation.AfterReturning;
import org.aspectj.lang.annotation.AfterThrowing;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Before;
import org.aspectj.lang.reflect.MethodSignature;
import org.springframework.stereotype.Component;

/**
 * @author Artem Labazin
 * @version 1.0.0
 * @since Dec 30, 2015 | 12:41
 */
@Slf4j
@Aspect
@Component
class MyAspect {

    @Before(value = "execution(public " + // method access level
                    "java.lang.String " + // method return type
                    "ru.xxlabaza.aspect.MyController.*" + // method name
                    "(java.lang.String)) && args(text)", // method arguments
            argNames = "text")
    public void before (String text) {
        log.info("\n\tBEFORE ->\n\tIncoming argument is {}", text);
    }

    @After(value = "execution(public " + // method access level
                   "java.lang.String " + // method return type
                   "ru.xxlabaza.aspect.MyController.callMeMaybe" + // method name
                   "(java.lang.String)) && args(text)", // method arguments
           argNames = "text")
    public void after (String text) {
        log.info("\n\tAFTER ->\n\tIncoming argument is {}", text);
    }

    @Around("bean(*Controller)")
    public Object around (ProceedingJoinPoint joinPoint) throws Throwable {
        String methodName = ((MethodSignature) joinPoint.getSignature()).getMethod().getName();
        String arguments = Stream.of(joinPoint.getArgs()).map(Object::toString).collect(Collectors.joining(", "));

        log.info("\n\tAROUND ->\n" +
                    "\tMethod name: {}\n" +
                    "\tArguments: {}",
                    methodName,
                    arguments);

        return joinPoint.proceed();
    }

    @AfterThrowing(
            value = "execution(public " + // method access level
                    "* " + // method return type
                    "ru.xxlabaza.aspect.MyController.callMeMaybe" + // method name
                    "(java.lang.String)) && args(text)", // method arguments
            argNames = "text, exception",
            throwing = "exception"
    )
    public void afterThrowing (String text, Exception exception) {
        log.error("\n\tERROR ->\n" +
                     "\tIncoming argument is {}\n" +
                     "\tException name is {}",
                     text,
                     exception.getClass().getSimpleName());
    }

    @AfterReturning(
            value = "execution(public " + // method access level
                    "java.lang.String " + // method return type
                    "ru.xxlabaza.aspect.MyController.callMeMaybe" + // method name
                    "(java.lang.String)) && args(text)", // method arguments
            returning = "result",
            argNames = "text, result"
    )
    public void afterReturning (String text, String result) {
        log.info("\n\tAFTER_RETURNING ->\n\tMethod result is {}, Incoming argument is {}", result, text);
    }
}
