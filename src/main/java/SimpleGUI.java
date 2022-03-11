import com.gargoylesoftware.htmlunit.html.DomNode;
import com.gargoylesoftware.htmlunit.html.DomNodeList;
import com.gargoylesoftware.htmlunit.html.HtmlElement;

import javax.swing.*;
import java.awt.*;
import java.awt.font.TextAttribute;
import java.io.IOException;
import java.util.Map;

public class SimpleGUI extends JFrame{

    private final WebScraper webScraper;
    private final Font aliveFont;
    private final Font deadFont;
    private final JPanel userPanel;
    private final JPanel buttonPanel;
    private final String baseUrl = "https://secure.runescape.com/m=hiscore_oldschool_hardcore_ironman/overall?table=0&page=";
    private int pageNumber = 1;

    public SimpleGUI() {
        super();
        this.setTitle("Runescape HCIM LeaderBoard");
        this.setLayout(new FlowLayout());
        this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        this.setResizable(false);
        this.setSize(400,750);
        this.setVisible(true);

        webScraper = new WebScraper();
        userPanel = new JPanel();
        userPanel.setPreferredSize(new Dimension(400, 660));
        buttonPanel = new JPanel();
        this.add(userPanel);
        this.add(buttonPanel);
        aliveFont = new Font(Font.MONOSPACED, Font.PLAIN, 12);
        //Creating strikethrough font
        Map attributes = (new Font(Font.MONOSPACED, Font.BOLD, 12)).getAttributes();
        attributes.put(TextAttribute.STRIKETHROUGH, TextAttribute.STRIKETHROUGH_ON);
        deadFont = new Font(attributes);

        JButton previous = new JButton("Previous Page");
        JButton next = new JButton("Next Page");
        buttonPanel.add(previous);
        buttonPanel.add(next);
        previous.addActionListener(e -> {
            if(pageNumber > 1){
                pageNumber--;
                userPanel.removeAll();
                this.fetchData();
            }
        });
        next.addActionListener(e -> {
            pageNumber++;
            userPanel.removeAll();
            this.fetchData();
        });


        this.fetchData();
    }

    public void createLabel(String text, boolean alive){
        JLabel label = new JLabel(text, SwingConstants.LEFT);
        label.setPreferredSize(new Dimension(375, 20));
        if(alive){
            label.setFont(aliveFont);
        }
        else{
            label.setFont(deadFont);
        }
        userPanel.add(label);
    }

    public void fetchData(){
        this.createLabel(String.format("%-10s %-17s %-8s %15s", "Rank", "Name", "Level", "XP"), true);
        try {
            DomNodeList<DomNode> domList = webScraper.scrapePage(baseUrl + pageNumber);
            //Parse columns in row (User Data)
            for(DomNode node : domList){
                HtmlElement htmlElement = (HtmlElement) node;
                String data = htmlElement.getTextContent();
                String[] arr = data.split("\\r?\\n");

                switch (arr.length) {
                    //12 for alive players
                    case 12 -> this.createLabel(String.format("%-10s %-17s %-8s %15s", arr[2], arr[5], arr[8], arr[11]), true);
                    //13 for dead players
                    case 13 -> this.createLabel(String.format("%-10s %-17s %-8s %15s", arr[2], arr[5], arr[9], arr[12]), false);
                }
            }
            //Refresh content window
            this.revalidate();
        }
        catch (IOException e){
            e.printStackTrace();
        }
    }


}
