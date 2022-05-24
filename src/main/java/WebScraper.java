import com.gargoylesoftware.htmlunit.BrowserVersion;
import com.gargoylesoftware.htmlunit.WebClient;
import com.gargoylesoftware.htmlunit.html.*;

import java.io.IOException;


public class WebScraper {

    private final WebClient webClient;

    WebScraper(){
        webClient = new WebClient(BrowserVersion.CHROME);
        webClient.getOptions().setCssEnabled(false);
        webClient.getOptions().setThrowExceptionOnFailingStatusCode(false);
        webClient.getOptions().setThrowExceptionOnScriptError(false);
        webClient.getOptions().setPrintContentOnFailingStatusCode(false);
        webClient.getOptions().setJavaScriptEnabled(false);
    }

    public DomNodeList<DomNode> scrapePage(String url) throws IOException {
        HtmlPage page = webClient.getPage(url);
        webClient.getCurrentWindow().getJobManager().removeAllJobs();
        webClient.close();
        return page.querySelectorAll(".personal-hiscores__table tr");
    }



}
