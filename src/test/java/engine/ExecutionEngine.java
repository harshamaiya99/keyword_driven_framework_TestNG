package engine;

import annotations.Keyword;
import model.TestRow;
import org.reflections.Reflections;
import org.reflections.scanners.Scanners;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.lang.reflect.Method;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;

public class ExecutionEngine {

    private static final Map<String, Method> KEYWORD_CACHE = new HashMap<>();
    private static final Logger log = LoggerFactory.getLogger(ExecutionEngine.class);

    // Call this explicitly before tests start
    public static void init() {
        if (KEYWORD_CACHE.isEmpty()) {
            Reflections reflections = new Reflections("keywords", Scanners.MethodsAnnotated);
            Set<Method> annotatedMethods = reflections.getMethodsAnnotatedWith(Keyword.class);

            for (Method method : annotatedMethods) {
                Keyword annotation = method.getAnnotation(Keyword.class);
                KEYWORD_CACHE.put(annotation.value(), method);
            }
        }
    }

    public static void execute(TestRow row) throws Exception {
        Method targetMethod = KEYWORD_CACHE.get(row.action);

        if (targetMethod != null) {
            targetMethod.invoke(null, row);
        } else {
            log.error("Execution aborted: Unknown action or missing @Keyword annotation for '{}'", row.action);
            throw new RuntimeException("Unknown action or missing @Keyword annotation for: " + row.action);
        }
    }
}