package com.sumilux.avro.spring.obm.support;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import com.sumilux.avro.spring.obm.Marshaller;
import com.sumilux.avro.spring.obm.Unmarshaller;

/**
 * Simple base class to make sure we make as many things common and reusable as possible
 *
 * @author Josh Long
 */
abstract public class AbstractMarshaller<T> implements Marshaller<T>, Unmarshaller<T> {


    protected Log log = LogFactory.getLog(getClass());


}
