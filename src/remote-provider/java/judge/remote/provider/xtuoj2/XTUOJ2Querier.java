package judge.remote.provider.xtuoj2;

import org.springframework.stereotype.Component;

import judge.httpclient.DedicatedHttpClient;
import judge.httpclient.HttpBodyValidator;
import judge.remote.RemoteOjInfo;
import judge.remote.account.RemoteAccount;
import judge.remote.querier.AuthenticatedQuerier;
import judge.remote.status.RemoteStatusType;
import judge.remote.status.SubmissionRemoteStatus;
import judge.remote.status.SubstringNormalizer;
import judge.remote.submitter.SubmissionInfo;
import judge.tool.Tools;

@Component
public class XTUOJ2Querier extends AuthenticatedQuerier {

    @Override
    public RemoteOjInfo getOjInfo() {
        return XTUOJ2Info.INFO;
    }

    @Override
    protected SubmissionRemoteStatus query(SubmissionInfo info, RemoteAccount remoteAccount, DedicatedHttpClient client) {
        String html = client.get(
                "/OnlineJudge2/index.php/Solution/read/id/" + info.remoteRunId,
                new HttpBodyValidator("<title>Hint</title>", true)).getBody();
        SubmissionRemoteStatus status = new SubmissionRemoteStatus();
        status.rawStatus = Tools.regFind(html, "<B>Result:</B>(.+?)</TD>").replaceAll("<.*?>", "").trim();
        if(status.rawStatus.isEmpty()) {
            status.rawStatus = "Compile Error";
        }
        status.statusType = SubstringNormalizer.DEFAULT.getStatusType(status.rawStatus);
        if (status.statusType == RemoteStatusType.AC) {
            status.executionMemory = Integer.parseInt(Tools.regFind(html, "<B>Memory:</B> ([-\\d]+)"));
            status.executionTime = Integer.parseInt(Tools.regFind(html, "<B>Time:</B> ([-\\d]+)"));
        } else if (status.statusType == RemoteStatusType.CE) {
            html = client.get("/OnlineJudge2/index.php/solution/compileinfo/id/" + info.remoteRunId).getBody();
            status.compilationErrorInfo = Tools.regFind(html, "(<pre>[\\s\\S]*?</pre>)");
        }
        return status;
    }
    
}
