package judge.remote.provider.xtuoj2;

import judge.httpclient.DedicatedHttpClient;
import judge.httpclient.HttpStatusValidator;
import judge.httpclient.SimpleNameValueEntityFactory;
import judge.remote.RemoteOjInfo;
import judge.remote.account.RemoteAccount;
import judge.remote.loginer.RetentiveLoginer;

import org.apache.http.HttpEntity;
import org.springframework.stereotype.Component;

@Component
public class XTUOJ2Loginer extends RetentiveLoginer {

    @Override
    public RemoteOjInfo getOjInfo() {
        return XTUOJ2Info.INFO;
    }

    @Override
    protected void loginEnforce(RemoteAccount account, DedicatedHttpClient client) {
        if (client.get("/OnlineJudge2/index.php/User/menu/redirect/:OnlineJudge2:index.php").getBody().contains("logout")) {
            return;
        }

        HttpEntity entity = SimpleNameValueEntityFactory.create( //
                "tab_user_id", account.getAccountId(), //
                "redirect", "/OnlineJudge2/index.php", //
                "tab_password", account.getPassword(), //
                "Submit", "Login", //
                "think_html_token", "");
        client.post("/OnlineJudge2/index.php/User/login", entity, HttpStatusValidator.SC_MOVED_TEMPORARILY);
    }

}
