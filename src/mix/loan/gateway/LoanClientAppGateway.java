package mix.loan.gateway;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import mix.Constants;
import mix.messaging.gateway.MessageReceiverGateway;
import mix.messaging.gateway.MessageSenderGateway;
import mix.loan.LoanReply;
import mix.loan.LoanRequest;

import javax.jms.JMSException;
import javax.jms.Message;
import javax.jms.MessageListener;
import javax.jms.TextMessage;

/**
 * Created by v on 2-6-16.
 */
public abstract class LoanClientAppGateway {
    MessageSenderGateway sender;
    MessageReceiverGateway receiver;
    LoanSerializer serializer;

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
                    System.out.println(msgText);
                    onLoanReplyArrived(new LoanRequest(), new LoanReply());
                } catch (JMSException e) {
                    e.printStackTrace();
                }
            }
        });
    }

    public void applyForLoan(LoanRequest loanRequest) {
        try {
            String serLoanRequest = serializer.requestToString(loanRequest);
            sender.sendMessage(sender.createMessage(serLoanRequest));
        } catch (JMSException e){
            e.printStackTrace();
        }
    }

    public abstract void onLoanReplyArrived(LoanRequest loanRequest, LoanReply loanReply);
}
