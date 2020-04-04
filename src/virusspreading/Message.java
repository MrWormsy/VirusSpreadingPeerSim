package virusspreading;

public class Message {

    enum MessageType {
        INFECTION(1),
        END_INCUBATION(2);

        private int typeID;

        MessageType(int i) {
            this.setTypeID(i);
        }

        public int getTypeID() {
            return this.typeID;
        }

        public void setTypeID(int typeID) {
            this.typeID = typeID;
        }
    }

    private int type;
    private String content;

    Message(MessageType type, String content) {
        this.type = type.getTypeID();
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