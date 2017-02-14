package judge.remote.provider.tyvj;

import judge.httpclient.DedicatedHttpClient;
import judge.httpclient.SimpleHttpResponse;
import judge.httpclient.SimpleNameValueEntityFactory;
import judge.remote.RemoteOjInfo;
import judge.remote.account.RemoteAccount;
import judge.remote.submitter.CanonicalSubmitter;
import judge.remote.submitter.SubmissionInfo;
import judge.tool.Tools;

import org.apache.http.HttpEntity;
import org.springframework.stereotype.Component;


@Component
public class TyvjSubmitter extends CanonicalSubmitter {

    @Override
    public RemoteOjInfo getOjInfo() {
        return TyvjInfo.INFO;
    }

    @Override
    protected boolean needLogin() {
        return true;
    }

    @Override
    protected Integer getMaxRunId(SubmissionInfo info, DedicatedHttpClient client, boolean submitted) {
        return submitted ? Integer.parseInt(info.remoteRunId) : -1;
    }

    @Override
    protected String submitCode(SubmissionInfo info, RemoteAccount remoteAccount, DedicatedHttpClient client) {
        SimpleHttpResponse response = client.get("/p/" + info.remoteProblemId);
        String token = Tools.regFind(response.getBody(),
                "id=\"frmSubmitCode\" method=\"post\"><input name=\"__RequestVerificationToken\" type=\"hidden\" value=\"(.+?)\" /> ");
        HttpEntity entity = SimpleNameValueEntityFactory.create(
                "__RequestVerificationToken", token, //
                "problem_id", info.remoteProblemId, //
                "code", info.sourceCode,
                "language_id", info.remotelanguage
        );
        String html = client.post("/Status/Create", entity).getBody();
        try{
            info.remoteRunId = new Integer(html).toString();
            return null;
        } catch (Exception e) {
            if(html.length() <= 24){
                return html;
            } else {
                throw e;
            }
        }
    }
}
