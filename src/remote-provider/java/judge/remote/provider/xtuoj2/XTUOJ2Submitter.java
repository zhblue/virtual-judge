package judge.remote.provider.xtuoj2;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.apache.commons.lang3.Validate;
import org.apache.http.HttpEntity;
import org.springframework.stereotype.Component;

import judge.httpclient.DedicatedHttpClient;
import judge.httpclient.HttpStatusValidator;
import judge.httpclient.SimpleHttpResponse;
import judge.httpclient.SimpleNameValueEntityFactory;
import judge.remote.RemoteOjInfo;
import judge.remote.account.RemoteAccount;
import judge.remote.submitter.CanonicalSubmitter;
import judge.remote.submitter.SubmissionInfo;

@Component
public class XTUOJ2Submitter extends CanonicalSubmitter {

    @Override
    public RemoteOjInfo getOjInfo() {
        return XTUOJ2Info.INFO;
    }

    @Override
    protected boolean needLogin() {
        return true;
    }

    @Override
    protected Integer getMaxRunId(SubmissionInfo info, DedicatedHttpClient client, boolean submitted) {
        String html = client.get("/OnlineJudge2/index.php/Solution/onlinestatus/user_id/" + info.remoteAccountId + "/problem_id/" + info.remoteProblemId).getBody();
        Matcher matcher = Pattern.compile("<tr align=\"center\">\\s*<td>(\\d+)").matcher(html);
        return matcher.find() ? Integer.parseInt(matcher.group(1)) : -1;
    }

    @Override
    protected String submitCode(SubmissionInfo info, RemoteAccount remoteAccount, DedicatedHttpClient client) {
        HttpEntity entity = SimpleNameValueEntityFactory.create(
            "problem_id", info.remoteProblemId, //
            "language", info.remotelanguage, //
            "source_code", info.sourceCode,
            "submit", "Submit",
            "think_html_token",""
        );
        SimpleHttpResponse response = client.post("/OnlineJudge2/index.php/Problem/submitcode", entity, HttpStatusValidator.SC_OK);
        Validate.isTrue(response.getBody().contains("Submit Solution successfully"));
        return null;
    }

}
