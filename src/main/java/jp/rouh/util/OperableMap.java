package jp.rouh.util;

import java.util.HashMap;
import java.util.function.BiFunction;
import java.util.function.Function;
import java.util.function.UnaryOperator;

public class OperableMap<K, V> extends HashMap<K, V>{

    public OperableMap<K, V> mapKey(UnaryOperator<K> keyMapper){
        var resultMap = new OperableMap<K, V>();
        forEach((key, value)->resultMap.put(keyMapper.apply(key), value));
        return resultMap;
    }

    public <V2> OperableMap<K, V2> mapValue(Function<V, V2> valueMapper){
        var resultMap = new OperableMap<K, V2>();
        forEach((key, value)->resultMap.put(key, valueMapper.apply(value)));
        return resultMap;
    }

    public <V2> OperableMap<K, V2> mapValue(BiFunction<K, V, V2> valueMapper){
        var resultMap = new OperableMap<K, V2>();
        forEach((key, value)->resultMap.put(key, valueMapper.apply(key, value)));
        return resultMap;
    }

}
