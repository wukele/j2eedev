package com.sumilux.avro.spring.obm;


import java.io.InputStream;

public interface Unmarshaller<T> {

    boolean supports(Class<T> clazz);

    T unmarshal(Class<T> clazz, InputStream source) throws Exception;

}
