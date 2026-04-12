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

    // Cache to store the mapping of Action Name -> Method (e.g., "CUSTPOST" -> createCustomer)
    private static final Map<String, Method> KEYWORD_CACHE = new HashMap<>();
    private static final Logger log = LoggerFactory.getLogger(ExecutionEngine.class);

    // Static block runs exactly once when the ExecutionEngine is first loaded into memory
    static {
        loadKeywords();
    }

    private static void loadKeywords() {
        // 1. Tell Reflections to scan the "keywords" package for annotated methods
        Reflections reflections = new Reflections("keywords", Scanners.MethodsAnnotated);

        // 2. Fetch all methods across all 250+ classes that have the @Keyword annotation
        Set<Method> annotatedMethods = reflections.getMethodsAnnotatedWith(Keyword.class);

        // 3. Store them in the Map for instant O(1) lookup during test execution
        for (Method method : annotatedMethods) {
            Keyword annotation = method.getAnnotation(Keyword.class);
            KEYWORD_CACHE.put(annotation.value(), method);
        }
    }

    public static void execute(TestRow row) throws Exception {

        // 4. Instantly grab the target method from the cache
        Method targetMethod = KEYWORD_CACHE.get(row.action);

        if (targetMethod != null) {
            // Invoke the static method dynamically and pass the TestRow
            targetMethod.invoke(null, row);
        } else {
            log.error("Execution aborted: Unknown action or missing @Keyword annotation for '{}'", row.action);
            throw new RuntimeException("Unknown action or missing @Keyword annotation for: " + row.action);
        }
    }
}