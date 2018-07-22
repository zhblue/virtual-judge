package judge.remote.provider.vjudge;

import judge.httpclient.DedicatedHttpClient;
import judge.httpclient.HttpBodyValidator;
import judge.httpclient.SimpleNameValueEntityFactory;
import judge.remote.RemoteOjInfo;
import judge.remote.account.RemoteAccount;
import judge.remote.loginer.RetentiveLoginer;
import org.apache.http.HttpEntity;
import org.springframework.stereotype.Component;

@Component
public class VjudgeLoginer extends RetentiveLoginer {

    @Override
    public RemoteOjInfo getOjInfo() {
        return VjudgeInfo.INFO;
    }

    @Override
    protected void loginEnforce(RemoteAccount account, DedicatedHttpClient client) {
        if (client.post("/user/checkLogInStatus").getBody().equals("true")) {
            return;
        }

        HttpEntity entity = SimpleNameValueEntityFactory.create( //
                "password", account.getPassword(), //
                "username", account.getAccountId());
        client.post("/user/login", entity, new HttpBodyValidator("success", false));
    }

}
