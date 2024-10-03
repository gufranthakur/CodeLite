import javax.swing.tree.DefaultMutableTreeNode;

public class CustomNode extends DefaultMutableTreeNode{

    private String nodeName, content, path;
    public boolean isDirectory;

    public CustomNode(String name, String content, String path) {
        super(name);
        this.nodeName = name;
        this.content = content;
        this.path = path;
    }

    public String getNodeName() {
        return nodeName;
    }

    public String getContent() {
        return content;
    }

    public String getFilePath() {
        return path;
    }

    public void setFilePath(String newPath) {
        this.path = newPath;
    }

    public void setContent(String newContent){
        content = newContent;
    }

    public void setNodeName(String newName) {
        this.nodeName = newName;
        this.setUserObject(newName);
    }



}