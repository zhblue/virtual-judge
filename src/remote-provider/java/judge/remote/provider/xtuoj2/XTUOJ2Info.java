package judge.remote.provider.xtuoj2;

import judge.remote.RemoteOj;
import judge.remote.RemoteOjInfo;

import org.apache.http.HttpHost;

public class XTUOJ2Info {

    public static final RemoteOjInfo INFO = new RemoteOjInfo( //
            RemoteOj.XTUOJ2, //
            "XTUOJ2", //
            new HttpHost("202.197.224.59") //
    );
    
    static {
        INFO.faviconUrl = "images/remote_oj/XTU_favicon.ico";
        INFO._64IntIoFormat = "%I64d & %I64u";
        INFO.urlForIndexDisplay = "http://202.197.224.59/OnlineJudge2/";
    }

}
