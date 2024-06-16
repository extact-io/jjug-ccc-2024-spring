package sample.microprofile.book.server.interceptor;

import java.util.logging.Logger;

import jakarta.annotation.Priority;
import jakarta.interceptor.AroundInvoke;
import jakarta.interceptor.Interceptor;
import jakarta.interceptor.InvocationContext;

@Interceptor
@Priority(Interceptor.Priority.APPLICATION)
@TraceLoggable
public class TraceLogInterceptor {

    private static final Logger LOGGER = Logger.getLogger(TraceLogInterceptor.class.getName());

    @AroundInvoke
    public Object obj(InvocationContext ic) throws Exception {

        String clazz = ic.getTarget().getClass().getSimpleName();
        String method = ic.getMethod().getName();
        LOGGER.info("%sメソッドが呼ばれました。".formatted(clazz + "#" + method) );

        return ic.proceed();
    }
}
