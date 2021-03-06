package com.starit;

import org.apache.camel.CamelContext;
import org.apache.camel.Exchange;
import org.apache.camel.Processor;
import org.apache.camel.model.ProcessorDefinition;
import org.apache.camel.processor.DelegateAsyncProcessor;
import org.apache.camel.spi.InterceptStrategy;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;


/**
 * @author janstey
 * 
 */
public class MyInterceptor implements InterceptStrategy {
	private static final transient Log LOG = LogFactory.getLog(MyInterceptor.class);
    private static int count;

    public Processor wrapProcessorInInterceptors(final CamelContext context, final ProcessorDefinition definition,
                                                 final Processor target, final Processor nextTarget) throws Exception {

        // as this is based on an unit test we are a bit lazy and just create an inlined processor
        // where we implement our interception logic.
        return new Processor() {
            public void process(Exchange exchange) throws Exception {
                // we just count number of interceptions
                count++;
                LOG.info("I am the container wide interceptor. Intercepted total count: " + count);
                // its important that we delegate to the real target so we let target process the exchange
                target.process(exchange);
            }

            @Override
            public String toString() {
                return "ContainerWideInterceptor[" + target + "]";
            }
        };
    }

    public int getCount() {
        return count;
    }
}
