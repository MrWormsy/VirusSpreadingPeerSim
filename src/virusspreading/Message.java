package virusspreading;

public class Message {

    public final static int HELLOWORLD = 0;

    private int type;
    private String content;

    Message(int type, String content) {
        this.type = type;
        this.content = content;
    }

    public String getContent() {
        return this.content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public int getType() {
        return this.type;
    }

}