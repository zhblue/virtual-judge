package judge.remote.provider.vjudge;

import judge.httpclient.DedicatedHttpClient;
import judge.httpclient.HttpStatusValidator;
import judge.remote.RemoteOjInfo;
import judge.remote.crawler.RawProblemInfo;
import judge.remote.crawler.SyncCrawler;
import judge.tool.HtmlHandleUtil;
import judge.tool.Tools;

import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.Validate;
import org.apache.http.HttpHost;
import org.springframework.stereotype.Component;

@Component
public class VjudgeCrawler extends SyncCrawler {

    @Override
    public RemoteOjInfo getOjInfo() {
        return VjudgeInfo.INFO;
    }
    
    public RawProblemInfo crawl(final String problemId) throws Exception {
        Validate.isTrue(problemId.matches(".+?-.+"));
        HttpHost host = getOjInfo().mainHost;
        DedicatedHttpClient client = dedicatedHttpClientFactory.build(host, getOjInfo().defaultChaset);
        String outerUrl = host.toURI() + "/problem/" + problemId.trim();

        RawProblemInfo info = new RawProblemInfo();
        info.url = outerUrl;
        String html = client.get(outerUrl, HttpStatusValidator.SC_OK).getBody();
        info.title = Tools.regFind(html, "<div id=\"prob-title\">\\s*<h2>(.+?)</h2>").trim();
        String timeLimit = Tools.regFind(html, "Time limit</dt>\\s*<dd class=\"col-sm-7\">(\\d+) ms</dd>");
        info.timeLimit = timeLimit.isEmpty() ? 0 : Integer.parseInt(timeLimit);
        String memoryLimit = Tools.regFind(html, "Memory limit</dt>\\s*<dd class=\"col-sm-7\">(\\d+) kB</dd>");
        info.memoryLimit = memoryLimit.isEmpty() ? 0 : Integer.parseInt(memoryLimit);
        info.source =  Tools.regFind(html, "Source</dt>\\s*<dd class=\"col-sm-7\">([\\s\\S]*?)</dd>")
                + Tools.regFind(html, "Author</dt>\\s*<dd class=\"col-sm-7\">([\\s\\S]*?)</dd>");
        
        String innerUrl = host.toURI() + Tools.regFind(html,"<iframe id=\"frame-description\"\\s*src=\"(.*?)\"");
        html = client.get(innerUrl, HttpStatusValidator.SC_OK).getBody();
        info.description = HtmlHandleUtil.transformUrlToAbs(html, innerUrl).replaceAll("<script.*?</script>", "");
        Validate.isTrue(!StringUtils.isBlank(info.title));
        return info;
    }

}

