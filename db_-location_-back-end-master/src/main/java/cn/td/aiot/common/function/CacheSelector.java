package cn.td.aiot.common.function;

@FunctionalInterface
public interface CacheSelector<T> {
    T select() throws Exception;
}
