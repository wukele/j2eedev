package com.sumilux.avro.spring.remoting.avro.servers;

import org.apache.avro.ipc.NettyServer;
import org.apache.avro.ipc.Responder;
import org.apache.avro.ipc.Server;

import java.net.InetSocketAddress;

/**
 * <p/>
 * implementation of {@link org.springframework.remoting.avro.AvroExporter} that builds a {@link NettyServer}.
 * <p/>
 * This should be your default choice to expose services in the {@link org.springframework.remoting.avro.AvroExporter} tree.
 *
 * @author Josh Long
 */
public class NettyServerCreationCallback implements ServerCreationCallback {

    @Override
    public Server buildServer(InetSocketAddress address, Responder responder) throws Exception {
        return new NettyServer(responder, address);
    }
}
