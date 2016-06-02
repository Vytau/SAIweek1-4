package mix.loan.gateway;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import mix.loan.LoanReply;
import mix.loan.LoanRequest;

/**
 * Created by v on 2-6-16.
 */
public class LoanSerializer {
    Gson gson;

    public LoanSerializer() {
        gson = new GsonBuilder().create();
    }

    public String requestToString(LoanRequest loanRequest){
        return gson.toJson(loanRequest);
    }

    public LoanRequest requestFromString(String str){
        return gson.fromJson(str, LoanRequest.class);
    }

    public String replyToString(LoanReply loanReply){
        return gson.toJson(loanReply);
    }

    public LoanReply replyFromString(String str){
        return gson.fromJson(str, LoanReply.class);
    }

}
