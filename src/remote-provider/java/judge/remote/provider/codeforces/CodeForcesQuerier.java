package judge.remote.provider.codeforces;

import judge.remote.RemoteOjInfo;
import judge.remote.shared.CFStyleQuerier;
import org.springframework.stereotype.Component;

@Component
public class CodeForcesQuerier extends CFStyleQuerier {

    @Override
    public RemoteOjInfo getOjInfo() {
        return CodeForcesInfo.INFO;
    }

}
