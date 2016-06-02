package mix.messaging.gateway;

import javax.jms.*;
import javax.naming.Context;
import javax.naming.InitialContext;
import javax.naming.NamingException;
import java.util.Properties;

/**
 * Created by v on 2-6-16.
 */
public class MessageReceiverGateway {
    Connection connection;
    Session session;
    Destination receiveDestination;
    MessageConsumer consumer;

    public MessageReceiverGateway(String chanelName){
        try {
            Properties props = new Properties();
            props.setProperty(Context.INITIAL_CONTEXT_FACTORY,
                    "org.apache.activemq.jndi.ActiveMQInitialContextFactory");
            props.setProperty(Context.PROVIDER_URL, "tcp://localhost:61616");

            props.put(("queue." + chanelName), chanelName);

            Context jndiContext = new InitialContext(props);
            ConnectionFactory connectionFactory = (ConnectionFactory) jndiContext.lookup("ConnectionFactory");
            connection = connectionFactory.createConnection();
            session = connection.createSession(false, Session.AUTO_ACKNOWLEDGE);

            // connect to the receiver destination
            receiveDestination = (Destination) jndiContext.lookup(chanelName);
            consumer = session.createConsumer(receiveDestination);

            connection.start(); // this is needed to start receiving messages

        } catch (NamingException | JMSException e) {
            e.printStackTrace();
        }
    }

    public void setListener(MessageListener messageListener){
        try {
            consumer.setMessageListener(messageListener);
        } catch (JMSException e) {
            e.printStackTrace();
        }
    }
}
