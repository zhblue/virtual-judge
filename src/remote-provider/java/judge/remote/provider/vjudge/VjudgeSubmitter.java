package judge.remote.provider.vjudge;

import judge.httpclient.DedicatedHttpClient;
import judge.httpclient.SimpleHttpResponse;
import judge.httpclient.SimpleHttpResponseValidator;
import judge.httpclient.SimpleNameValueEntityFactory;
import judge.remote.RemoteOjInfo;
import judge.remote.account.RemoteAccount;
import judge.remote.submitter.CanonicalSubmitter;
import judge.remote.submitter.SubmissionInfo;
import judge.tool.Tools;

import org.apache.commons.lang3.Validate;
import org.apache.http.HttpEntity;
import org.springframework.stereotype.Component;


@Component
public class VjudgeSubmitter extends CanonicalSubmitter {

    @Override
    public RemoteOjInfo getOjInfo() {
        return VjudgeInfo.INFO;
    }

    @Override
    protected boolean needLogin() {
        return true;
    }

    @Override
    protected Integer getMaxRunId(SubmissionInfo info, DedicatedHttpClient client, boolean submitted) {
        return info.remoteRunId != null ? Integer.parseInt(info.remoteRunId) : -1;
    }

    @Override
    protected String submitCode(final SubmissionInfo info, RemoteAccount remoteAccount, DedicatedHttpClient client) {
        String[] ojAndPorblemID = info.remoteProblemId.split("-", 2);
        HttpEntity entity = SimpleNameValueEntityFactory.create(
                "language", info.remotelanguage, //
                "share", "0",
                "source", info.sourceCode,
                "captcha","",
                "oj",ojAndPorblemID[0].trim(),
                "probNum",ojAndPorblemID[1].trim()
        );
        client.post("/problem/submit", entity, new SimpleHttpResponseValidator() {
            @Override
            public void validate(SimpleHttpResponse response) throws Exception {
                info.remoteRunId = Tools.regFind(response.getBody(), "\\{\"runId\":(\\d+)\\}");
                Validate.isTrue(!info.remoteRunId.isEmpty());
            }
        });
        return null;
    }

}
