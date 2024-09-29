import javax.swing.tree.DefaultMutableTreeNode;

public class CustomNode extends DefaultMutableTreeNode {

    private String nodeName, content, filePath;

    public CustomNode(String name, String content, String path) {
        super(name);
        this.nodeName = name;
        this.content = content;
        this.filePath = path;
    }

    public String getNodeName() {
        return nodeName;
    }

    public String getContent() {
        return content;
    }

    public String getFilePath() {
        return filePath;
    }

}