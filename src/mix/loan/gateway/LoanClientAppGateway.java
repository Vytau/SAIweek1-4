package mix.loan.gateway;

import mix.Constants;
import mix.loan.LoanReply;
import mix.loan.LoanRequest;
import mix.messaging.gateway.MessageReceiverGateway;
import mix.messaging.gateway.MessageSenderGateway;

import javax.jms.JMSException;
import javax.jms.Message;
import javax.jms.MessageListener;
import javax.jms.TextMessage;
import java.util.HashMap;

/**
 * Created by v on 2-6-16.
 */
public abstract class LoanClientAppGateway {
    MessageSenderGateway sender;
    MessageReceiverGateway receiver;
    LoanSerializer serializer;
    private HashMap<String, LoanRequest> cash = new HashMap<>();

    public LoanClientAppGateway(){
        serializer = new LoanSerializer();
        sender = new MessageSenderGateway(Constants.requestLoanClientChanel);
        receiver = new MessageReceiverGateway(Constants.replyLoanClientChanel);

        receiver.setListener(new MessageListener() {
            @Override
            public void onMessage(Message msg) {
                String msgText = null;
                try {
                    msgText = ((TextMessage) msg).getText();
                    LoanReply loanReply = serializer.replyFromString(msgText);
                    LoanRequest tempRequest = cash.get(msg.getJMSCorrelationID());
                    onLoanReplyArrived(tempRequest, loanReply);
                } catch (JMSException e) {
                    e.printStackTrace();
                }
            }
        });
    }

    public void applyForLoan(LoanRequest loanRequest) {
        try {
            String serLoanRequest = serializer.requestToString(loanRequest);
            Message msg = sender.createMessage(serLoanRequest);
            sender.sendMessage(msg);
            cash.put(msg.getJMSMessageID(), loanRequest);
        } catch (JMSException e){
            e.printStackTrace();
        }
    }

    public abstract void onLoanReplyArrived(LoanRequest loanRequest, LoanReply loanReply);
}
