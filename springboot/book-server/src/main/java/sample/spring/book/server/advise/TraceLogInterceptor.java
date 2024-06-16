package sample.spring.book.server.advise;

import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.reflect.MethodSignature;
import org.springframework.stereotype.Component;

import lombok.extern.slf4j.Slf4j;

@Component
@Aspect
@Slf4j
public class TraceLogInterceptor {

    @Around("@annotation(sample.spring.book.server.advise.TraceLoggable)"
            + " || within(@sample.spring.book.server.advise.TraceLoggable *)")
    public Object obj(ProceedingJoinPoint pjp) throws Throwable {

        String clazz = pjp.getTarget().getClass().getSimpleName();
        MethodSignature signature = (MethodSignature) pjp.getSignature();
        String method = signature.getName();

        log.info("%sメソッドが呼ばれました。".formatted(clazz + "#" + method));

        return pjp.proceed();
    }
}
