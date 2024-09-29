import javax.swing.tree.DefaultMutableTreeNode;

public class CustomNode extends DefaultMutableTreeNode implements Cloneable{

    private final String nodeName, content;

    public CustomNode(String name, String content) {
        super(name);
        this.nodeName = name;
        this.content = content;
    }

    public String getNodeName() {
        return nodeName;
    }

    public String getContent() {
        return content;
    }

    @Override
    public DefaultMutableTreeNode clone(){
        return (CustomNode) super.clone();
    }

}