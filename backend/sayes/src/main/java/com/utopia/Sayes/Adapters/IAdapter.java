package com.utopia.Sayes.Adapters;

import java.util.Map;

public interface IAdapter <T>{

    //convert object to Map
    Map<String, Object> toMap(T object);

    //convert Map to object
    T fromMap(Map<String, Object> map);

}
