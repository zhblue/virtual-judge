package judge.remote.provider.codeforcesgym;

import org.springframework.stereotype.Component;

import judge.remote.RemoteOjInfo;
import judge.remote.shared.CFStyleSubmitter;

@Component
public class CodeForcesGymSubmitter extends CFStyleSubmitter {

    @Override
    public RemoteOjInfo getOjInfo() {
        return CodeForcesGymInfo.INFO;
    }
}
