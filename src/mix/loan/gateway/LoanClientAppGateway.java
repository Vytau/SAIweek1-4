package mix.loan.gateway;

import mix.Constants;
import mix.messaging.gateway.MessageReceiverGateway;
import mix.messaging.gateway.MessageSenderGateway;
import mix.loan.LoanReply;
import mix.loan.LoanRequest;

import javax.jms.JMSException;

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
