package judge.remote.provider.vjudge;

import judge.remote.RemoteOj;
import judge.remote.RemoteOjInfo;
import org.apache.http.HttpHost;

public class VjudgeInfo {

    public static final RemoteOjInfo INFO = new RemoteOjInfo( //
            RemoteOj.VJudge, //
            "VJudge", //
            new HttpHost("vjudge.net", 443, "https") //
    );

    static {
        INFO.faviconUrl = "images/remote_oj/vjudge.jpg";
        INFO._64IntIoFormat = "Unknown";
    }

}
