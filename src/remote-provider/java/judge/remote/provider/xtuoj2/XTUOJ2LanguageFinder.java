package judge.remote.provider.xtuoj2;

import java.util.HashMap;
import java.util.LinkedHashMap;

import judge.remote.RemoteOjInfo;
import judge.remote.language.LanguageFinder;
import judge.tool.Handler;

import org.springframework.stereotype.Component;

@Component
public class XTUOJ2LanguageFinder implements LanguageFinder {

    @Override
    public RemoteOjInfo getOjInfo() {
        return XTUOJ2Info.INFO;
    }

    @Override
    public boolean isDiverse() {
        return false;
    }

    @Override
    public void getLanguages(String remoteProblemId, Handler<LinkedHashMap<String, String>> handler) {
        // TODO Auto-generated method stub
    }

    @Override
    public LinkedHashMap<String, String> getDefaultLanguages() {
        LinkedHashMap<String, String> languageList = new LinkedHashMap<String, String>();
        languageList.put("G++", "minGW C++");
        languageList.put("C++11", "minGW C++11");
        languageList.put("JAVA", "Java");
        languageList.put("GCC", "minGW C");
        languageList.put("C++", "Microsoft C++");
        languageList.put("C", "Microsoft C");
        return languageList;
    }

    @Override
    public HashMap<String, String> getLanguagesAdapter() {
        // TODO Auto-generated method stub
        return null;
    }

}
