package ru.xxlabaza.aspect;

import java.util.stream.Collectors;
import java.util.stream.Stream;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.After;
import org.aspectj.lang.annotation.AfterReturning;
import org.aspectj.lang.annotation.AfterThrowing;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Before;
import org.aspectj.lang.reflect.MethodSignature;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

/**
 * @author Artem Labazin
 * @version 1.0.0
 * @since Dec 30, 2015 | 12:41
 */
@Aspect
@Component
class MyAspect {

    private static final Logger LOGGER;

    static {
        LOGGER = LoggerFactory.getLogger(MyAspect.class);
    }

    @Before(value = "execution(public " + // method access level
                    "java.lang.String " + // method return type
                    "ru.xxlabaza.aspect.MyController.*" + // method name
                    "(java.lang.String)) && args(text)", // method arguments
            argNames = "text")
    public void before (String text) {
        LOGGER.info("\n\tBEFORE ->\n\tIncoming argument is {}", text);
    }

    @After(value = "execution(public " + // method access level
                   "java.lang.String " + // method return type
                   "ru.xxlabaza.aspect.MyController.callMeMaybe" + // method name
                   "(java.lang.String)) && args(text)", // method arguments
           argNames = "text")
    public void after (String text) {
        LOGGER.info("\n\tAFTER ->\n\tIncoming argument is {}", text);
    }

    @Around("bean(*Controller)")
    public Object around (ProceedingJoinPoint joinPoint) throws Throwable {
        String methodName = ((MethodSignature) joinPoint.getSignature()).getMethod().getName();
        String arguments = Stream.of(joinPoint.getArgs()).map(Object::toString).collect(Collectors.joining(", "));

        LOGGER.info("\n\tAROUND ->\n" +
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
//        LOGGER.error("\n\tERROR ->\n" +
//                     "\tIncoming argument is {}\n" +
//                     "\tException name is {}",
//                     text,
//                     exception.getClass().getSimpleName());
        LOGGER.error("Some error message", exception);
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
        LOGGER.info("\n\tAFTER_RETURNING ->\n\tMethod result is {}, Incoming argument is {}", result, text);
    }
}
