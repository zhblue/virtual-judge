package judge.remote.provider.xtuoj2;

import judge.remote.RemoteOjInfo;
import judge.remote.crawler.RawProblemInfo;
import judge.remote.crawler.SimpleCrawler;
import judge.tool.Tools;

import org.apache.commons.lang3.Validate;
import org.springframework.stereotype.Component;

@Component
public class XTUOJ2Crawler extends SimpleCrawler {

    @Override
    public RemoteOjInfo getOjInfo() {
        return XTUOJ2Info.INFO;
    }

    @Override
    protected String getProblemUrl(String problemId) {
        return getHost().toURI() + "/OnlineJudge2/index.php/Problem/read/id/" + problemId;
    }
    
    @Override
    protected void preValidate(String problemId) {
        Validate.isTrue(problemId.matches("[1-9]\\d\\d\\d"));
    }

    @Override
    protected void populateProblemInfo(RawProblemInfo info, String problemId, String html) {
        info.title = Tools.regFind(html, "<title>\\d{3,} - ([\\s\\S]*?)</title>").trim();
        info.timeLimit = Integer.parseInt(Tools.regFind(html, "<td align=\"right\">Time Limit : (\\d{3,}) MS</td>"));
        info.memoryLimit = Integer.parseInt(Tools.regFind(html, "<td align=\"left\">Memory Limit : (\\d{2,}) KB"));
        info.description = Tools.regFind(html, "<h3>Description</h3>([\\s\\S]*?)<h3>Input</h3>");
        if(info.description.trim().isEmpty()) {
        	 info.description = Tools.regFind(html, "<table width=\"90%\" border=\"1\" align=\"center\"><tbody><tr align=\"left\"><td>"
        	 		+ "([\\s\\S]*?)"
        	 		+ "<h3>Source</h3>");
        	 if(info.description.trim().isEmpty()) {
        		 info.description = Tools.regFind(html, "<table width=\"90%\" border=\"1\" align=\"center\"><tbody><tr align=\"left\"><td>"
        		 		+ "([\\s\\S]*?)"
        		 		+ "\\s*</td>\\s*</tr>\\s*</table>\\s*<div align=\"center\">\\[ <a href=\"/OnlineJudge2/index.php/Problem/submit/id/");
        	 }
        } else {
        	info.input = Tools.regFind(html, "<h3>Input</h3>([\\s\\S]*?)<h3>Output</h3>");
            info.output = Tools.regFind(html, "<h3>Output</h3>([\\s\\S]*?)<h3>Sample Input</h3>");
            info.sampleInput = Tools.regFind(html, "<h3>Sample Input</h3>([\\s\\S]*?)<h3>Sample Output</h3>");
            info.sampleOutput = Tools.regFind(html, "<h3>Sample Output</h3>([\\s\\S]*?)<h3>Hint</h3>");
            info.hint = Tools.regFind(html, "<h3>Hint</h3>([\\s\\S]*?)<h3>Source</h3>");
        }
        info.source = Tools.regFind(html, "<h3>Source</h3>([\\s\\S]*?)\\s*</td>");
    }

}
