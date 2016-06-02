package mix.messaging.gateway;

import javax.jms.*;
import javax.naming.Context;
import javax.naming.InitialContext;
import javax.naming.NamingException;
import java.util.Properties;

/**
 * Created by v on 2-6-16.
 */
public class MessageSenderGateway {
    Connection connection;
    Session session;
    Destination sendDestination;
    MessageProducer producer;

    public MessageSenderGateway(String chanelName){
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

            // connect to the sender destination
            sendDestination = (Destination) jndiContext.lookup(chanelName);
            producer = session.createProducer(sendDestination);
        } catch (NamingException e) {
            e.printStackTrace();
        } catch (JMSException e) {
            e.printStackTrace();
        }
    }

    public Message createMessage(String body) throws JMSException {
        return session.createTextMessage(body);
    }

    public void sendMessage(Message msg) throws JMSException {
        producer.send(msg);
    }
}
