package com.sumilux.avro.spring.obm.avro;


import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

import org.apache.avro.Schema;
import org.apache.avro.generic.GenericDatumReader;
import org.apache.avro.generic.GenericDatumWriter;
import org.apache.avro.io.Decoder;
import org.apache.avro.io.Encoder;
import org.springframework.util.Assert;

import com.sumilux.avro.spring.obm.avro.support.DecoderFactoryBuilder;
import com.sumilux.avro.spring.obm.avro.support.EncoderFactoryBuilder;
import com.sumilux.avro.spring.obm.avro.support.SchemaFactoryBean;
import com.sumilux.avro.spring.obm.support.AbstractMarshaller;

/**
 * Implementation of the {@link org.springframework.obm.Marshaller} and
 * {@link org.springframework.obm.Unmarshaller} interfaces for Avro
 *
 * @param <T>
 * @author Josh Long
 */
public class AvroMarshaller<T> extends AbstractMarshaller<T> {

    private boolean validate = false;

    /**
     * dictates whether the {@link org.apache.avro.io.Encoder encoders} and {@link org.apache.avro.io.Decoder decoders} will
     * be wrapped in a {@link org.apache.avro.io.ValidatingDecoder} or {@link org.apache.avro.io.ValidatingEncoder}
     *
     * @param validate whether or not to validate
     */
    public void setValidate(boolean validate) {
        this.validate = validate;
    }

    @Override
    public boolean supports(Class clazz) {
        try {
            Assert.notNull(clazz, "the class must not be null");
            Schema s = new SchemaFactoryBean(clazz).getObject();
            boolean supports = s != null;

            if (log.isDebugEnabled()) {
                log.debug("returning " + supports + " for class " + clazz.getName());
            }

            return supports;
        } catch (Exception e) {
            if (log.isDebugEnabled()) {
                log.debug("exception when trying to test whether the class " + clazz.getName() + " has an Avro schema");
            }
            return false;
        }
    }

    @Override
    public void marshal(Object obj, OutputStream os) throws IOException {
        try {
            Assert.notNull(obj, "the object to encode must not be null");
            Schema schema = new SchemaFactoryBean(obj.getClass()).getObject();
            Assert.notNull(schema, "the schema must not be null");
            GenericDatumWriter writer = new GenericDatumWriter(schema);
            Encoder encoder = new EncoderFactoryBuilder()
                                      .setOutputStream(os)
                                      .setSchema(schema)
                                      .setUseBinary(true)
                                      .setValidate(this.validate)
                                      .build();
            writer.write(obj, encoder);
            encoder.flush();
        } catch (Exception e) {
            if (log.isDebugEnabled()) {
                log.debug("exception when trying to test whether the class " + obj.getClass().getName() + " has an Avro schema");
            }
            throw new RuntimeException(e);
        }
    }

    @Override
    public T unmarshal(Class<T> clazz, InputStream source) throws IOException {
        try {
            Assert.notNull(clazz, "the class must not be null");
            Schema schema = new SchemaFactoryBean(clazz).getObject();
            Assert.notNull(schema, "the schema must not be null");
            GenericDatumReader reader = new GenericDatumReader(schema);
            Object old = clazz.newInstance();
            Decoder decoder = new DecoderFactoryBuilder()
                                      .setInputStream(source)
                                      .setUseBinary(true)
                                      .setSchema(schema)
                                      .setValidate(this.validate)
                                      .build();
            return (T) reader.read(old, decoder);
        } catch (Exception e) {
            if (log.isDebugEnabled()) {
                log.debug("exception when trying to test whether the class " + clazz.getName() + " has an Avro schema");
            }
            throw new RuntimeException(e);
        }
    }
}
