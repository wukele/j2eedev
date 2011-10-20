package org.jboss.netty.example.localtime;

import java.net.InetSocketAddress;
import java.util.concurrent.Executors;

import org.jboss.netty.bootstrap.ServerBootstrap;
import org.jboss.netty.channel.socket.nio.NioServerSocketChannelFactory;

/**
* Receives a list of continent/city pairs from a {@link LocalTimeClient} to
* get the local times of the specified cities.
*
* @author <a href="http://www.jboss.org/netty/">The Netty Project</a>
* @author <a href="http://gleamynode.net/">Trustin Lee</a>
*
* @version $Rev$, $Date$
*/
public class LocalTimeServer {

    public static void main(String[] args) throws Exception {
        // Configure the server.
        ServerBootstrap bootstrap = new ServerBootstrap(
                new NioServerSocketChannelFactory(
                        Executors.newCachedThreadPool(),
                        Executors.newCachedThreadPool()));

        // Set up the event pipeline factory.
        bootstrap.setPipelineFactory(new LocalTimeServerPipelineFactory());

        // Bind and start to accept incoming connections.
        bootstrap.bind(new InetSocketAddress(8080));
    }
}