package com.duckyshine.app.debug;

import org.joml.Vector3f;

public class Debug {
    private static final String RED = "\u001B[31m";
    private static final String RESET = "\u001B[0m";

    private static <T> String toString(T object) {
        if (object == null) {
            return "null";
        }

        if (object instanceof Vector3f) {
            Vector3f vector = (Vector3f) object;
            return "[" + vector.x + ", " + vector.y + ", " + vector.z + "]";
        }

        return object.toString();
    }

    private static String getClassName(String className) {
        String[] splitClassName = className.split("[.]");

        return splitClassName[splitClassName.length - 1];
    }

    private static String getMethodName(StackTraceElement caller) {
        String className = Debug.getClassName(caller.getClassName());
        String methodName = caller.getMethodName();

        return className + "." + methodName;
    }

    public static void debug(Object... objects) {
        StackTraceElement caller = Thread.currentThread().getStackTrace()[2];

        String methodName = Debug.getMethodName(caller);

        int lineNumber = caller.getLineNumber();

        System.err.print(RED + methodName + ":" + lineNumber + " ");

        for (int i = 0; i < objects.length; i++) {
            if (i > 0) {
                System.err.print(", ");
            }

            System.err.print(Debug.toString(objects[i]));
        }

        System.err.print("\n" + RESET);
    }
}
