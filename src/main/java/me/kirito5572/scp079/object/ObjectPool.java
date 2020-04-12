package me.kirito5572.scp079.object;

import java.util.HashMap;

@SuppressWarnings("unchecked")
public class ObjectPool {
    private static HashMap<Object, Object> pool = new HashMap<>();

    public static <T> T get(Class<T> clazz) {
        if (pool.containsKey(clazz))
            return (T) pool.get(clazz);

        try {
            T instance = clazz.newInstance();
            pool.put(clazz, instance);
            return instance;
        } catch (InstantiationException | IllegalAccessException e) {
            throw new IllegalArgumentException(e);
        }
    }
}
