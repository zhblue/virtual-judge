package judge.remote.provider.codeforcesgym;

import org.springframework.stereotype.Component;

import judge.remote.RemoteOjInfo;
import judge.remote.shared.CFStyleQuerier;

@Component
public class CodeForcesGymQuerier extends CFStyleQuerier {
    @Override
    public RemoteOjInfo getOjInfo() {
        return CodeForcesGymInfo.INFO;
    }
    
}
