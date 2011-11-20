package com.sumilux.avro.spring.http.converter.obm;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.List;

import org.springframework.beans.factory.InitializingBean;
import org.springframework.http.HttpInputMessage;
import org.springframework.http.HttpOutputMessage;
import org.springframework.http.MediaType;
import org.springframework.http.converter.AbstractHttpMessageConverter;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.http.converter.HttpMessageNotWritableException;
import org.springframework.util.Assert;

import com.sumilux.avro.spring.obm.Marshaller;
import com.sumilux.avro.spring.obm.Unmarshaller;

/**
 * The idea is that this class wil do the work that all of other defintions do because, essentially, their only differentiator is that
 * they do project-specific IO, which the {@link Marshaller} and {@link Unmarshaller} encapsulate nicely.
 *
 * @author Josh Long
 * @see org.springframework.http.converter.HttpMessageConverter
 */
public class MarshallingHttpMessageConverter extends AbstractHttpMessageConverter<Object> implements InitializingBean {

    private Marshaller marshaller;

    private Unmarshaller unmarshaller;

    public MarshallingHttpMessageConverter(Marshaller marshaller) {
        Assert.isInstanceOf(Unmarshaller.class, marshaller);
        this.marshaller = marshaller;
        this.unmarshaller = (Unmarshaller) marshaller;
    }

    public MarshallingHttpMessageConverter(Marshaller marshaller, Unmarshaller unmarshaller) {
        this.unmarshaller = unmarshaller;
        this.marshaller = marshaller;
    }

    public void setMarshaller(Marshaller<Object> marshaller) {
        this.marshaller = marshaller;
    }

    public void setUnmarshaller(Unmarshaller<Object> unmarshaller) {
        this.unmarshaller = unmarshaller;
    }

    @Override
    public boolean supports(Class<?> clazz) {
        return marshaller.supports(clazz) && unmarshaller.supports(clazz);
    }

    @Override
    public void afterPropertiesSet() throws Exception {
    	List<MediaType> mediaTypes = this.getSupportedMediaTypes();
        Assert.isTrue(mediaTypes.size() > 0, "the " + getClass().getName() + " has no " +
                                                     "'supportedMediaTypes.' This is most likely a configuration error" +
                                                     " and is not likely to work the way you expect it.");
        Assert.notNull(this.marshaller, "the 'thriftMarshaller' can't be null");
        Assert.notNull(this.unmarshaller, "the 'unmarshaller' can't be null");
    }

    @Override
    protected Object readInternal(Class clazz, HttpInputMessage inputMessage) throws IOException, HttpMessageNotReadableException {
        InputStream in = inputMessage.getBody();
        try {
            return unmarshaller.unmarshal(clazz, in);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    protected void writeInternal(Object o, HttpOutputMessage outputMessage) throws IOException, HttpMessageNotWritableException {
        OutputStream out = outputMessage.getBody();
        try {
            marshaller.marshal(o, out);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }
}
