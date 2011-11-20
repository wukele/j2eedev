package com.sumilux.avro.spring.obm;


import java.io.OutputStream;

/**
 * <p/>
 * Defines the contract for Object to Binary Marshallers. Implementations of this interface
 * can serialize a given Object to an {@link OutputStream}.
 * <p/>
 * <p>Although the <code>marshal</code> method accepts a <code>java.lang.Object</code> as its
 * first parameter, most <code>Marshaller</code> implementations cannot handle arbitrary
 * <code>Object</code>s. Instead, a object class must be registered with the thriftMarshaller,
 * or have a common base class.
 *
 * @author Josh Long
 * @see org.springframework.obm.Unmarshaller
 * @see org.springframework.oxm.Marshaller
 * @see org.springframework.oxm.Unmarshaller
 */
public interface Marshaller<T> {

    boolean supports(Class<T> clazz);

    void marshal(T obj, OutputStream os) throws Exception;

}
