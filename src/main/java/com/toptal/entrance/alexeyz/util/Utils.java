package com.toptal.entrance.alexeyz.util;

import java.lang.reflect.Field;
import java.lang.reflect.Modifier;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Map;
import java.util.function.Function;

import static java.lang.String.valueOf;

/**
 * @author alexey.zakharchenko@gmail.com
 */
public final class Utils {
    public static String s(Object obj) {
        StringBuilder b = new StringBuilder("[");
        for (Field f : obj.getClass().getFields()) {
            if (!isStaticField(f)) {
                try {
                    b.append(f.getName()).append("=").append(f.get(obj)).append(", ");
                } catch (IllegalAccessException e) {
                    // pass, don't print
                }
            }
        }
        b.append(']');

        return obj.getClass().getSimpleName() + ' ' + b.toString();
    }

    private static boolean isStaticField(Field f) {
        return Modifier.isStatic(f.getModifiers());
    }

    public static long t() { return System.currentTimeMillis(); }

    public static String s(Collection c) {
        if (c == null || c.isEmpty()) return "[]";

        return s(c.toArray());
    }

    public static <T> String s(Collection<T> c, Function<T, String> o) {
        if (c == null || c.isEmpty()) return "[]";

        Collection<String> c1 = new ArrayList<>(c.size());
        c.forEach(el -> c1.add(o.apply(el)));

        return s(c1);
    }


    @SuppressWarnings("unchecked")
    public static String sMap(Map m) {
        if (m == null || m.isEmpty()) return "{}";
        StringBuilder buf = new StringBuilder("{");
        for (Object key : m.keySet()) {
            buf.append("[").append(String.valueOf(key)).append(" -> ").append(String.valueOf(m.get(key))).append("], ");
        }

        return buf.substring(0, buf.length()-2) + "}]";

    }

    public static String s(Object[] array) {
        StringBuilder buf = new StringBuilder("[");
        for (Object o : array) buf.append(String.valueOf(o)).append(", ");

        return buf.substring(0, buf.length()-2) + "]";
    }

    public static boolean coinflip() {
        return coinflip(.5);
    }

    public static boolean coinflip(double possibility) {
        assert possibility >= 0;
        assert possibility <= 1;

        return Math.random() >= possibility;
    }

    public static <T> T first(Collection<T> c) {
        if (c == null || c.isEmpty()) return null;
        return c.iterator().next();
    }

    public static <T> T last(Collection<T> c) {
        if (c == null || c.isEmpty()) return null;

        if (c instanceof List) {
            List<T> l = (List<T>) c;
            return l.get(l.size() - 1);
        }
        T t = null;
        for (T t1 : c) {t = t1;}

        return t;
    }

    public static boolean isVoid(String s) {
        return s == null || s.trim().length() == 0;
    }

    public static String format(float f) {
        return String.format("%.2f", f);
    }

    public static String[] toHMS(int timeSec) {
        int h = timeSec / 3600;
        int m = (timeSec - 3600 * h) / 60;
        int s = timeSec - 3600*h - 60*m;

        return new String[] {valueOf(h), valueOf(m), valueOf(s)};
    }

    public static String formatHMS(int timeSec) {
        String[] hms = toHMS(timeSec);
        if (hms[0].length() == 1)
            hms[0] = "0" + hms[0];
        if (hms[1].length() == 1)
            hms[1] = "0" + hms[1];
        if (hms[2].length() == 1)
            hms[2] = "0" + hms[2];

        return hms[0] + ":" + hms[1] + ":" + hms[2];

    }

}
