package judge.remote.provider.vjudge;

import judge.httpclient.DedicatedHttpClient;
import judge.remote.RemoteOjInfo;
import judge.remote.account.RemoteAccount;
import judge.remote.querier.AuthenticatedQuerier;
import judge.remote.status.RemoteStatusType;
import judge.remote.status.SubmissionRemoteStatus;
import judge.remote.status.SubstringNormalizer;
import judge.remote.submitter.SubmissionInfo;
import org.apache.struts2.json.JSONException;
import org.apache.struts2.json.JSONUtil;
import org.springframework.stereotype.Component;

import java.util.Map;

@Component
public class VjudgeQuerier extends AuthenticatedQuerier {

    @Override
    public RemoteOjInfo getOjInfo() {
        return VjudgeInfo.INFO;
    }

    @SuppressWarnings("unchecked")
    @Override
    protected SubmissionRemoteStatus query(SubmissionInfo info, RemoteAccount remoteAccount, DedicatedHttpClient client) throws JSONException {

    	String html = client.get("/solution/data/" + info.remoteRunId).getBody();
    	Map<String,Object> json = (Map<String, Object>) JSONUtil.deserialize(html);
    	
        SubmissionRemoteStatus status = new SubmissionRemoteStatus();
        status.rawStatus =  (String) json.get("status");
        if("Submit Failed".equals(status.rawStatus)) {
            status.rawStatus = "vjudge.net Submit Failed";
        }
        status.statusType = SubstringNormalizer.DEFAULT.getStatusType(status.rawStatus);
        if (status.statusType == RemoteStatusType.AC) {
            if(json.containsKey("memory")) {
                status.executionMemory = ((Long)json.get("memory")).intValue();
            }
            if(json.containsKey("runtime")) {
                status.executionTime =  ((Long)json.get("runtime")).intValue();
            }
        } 
        status.compilationErrorInfo = (String) json.get("additionalInfo");
        return status;
    }

}
