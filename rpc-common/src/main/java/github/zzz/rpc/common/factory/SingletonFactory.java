package github.zzz.rpc.common.factory;

import java.util.HashMap;
import java.util.Map;

/**
 * 创建单例的工厂
 * 所以需要一个Map来存储多个的对象
 * @author zzz
 */
public class SingletonFactory {

    /**
     * 分别是class和对应的实例
     */
    private static Map<Class,Object> objectMap = new HashMap<>();

    private SingletonFactory(){}

    public static <T> T getInstance(Class<T> clazz){
        // 1. 检查是否已经存在
        Object instance = objectMap.get(clazz);
        synchronized (clazz){
            if (instance == null){
                // 2. 通过反射创建实例并且存储到Map中
                try {
                    instance = clazz.newInstance();
                    objectMap.put(clazz,instance);
                } catch (InstantiationException | IllegalAccessException e) {
                    throw new RuntimeException(e.getMessage(), e);
                }
            }
        }
        // 强制类型转换，保证instance是clazz的类型
        return clazz.cast(instance);
    }
}
