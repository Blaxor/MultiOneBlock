package ro.deiutzblaxo.oneblock.communication.action.chat.pm;

import lombok.Getter;

@Getter
public class PM {

    private String sender;
    private String reciver;
    private String message ="";


    public PM(String sender, String reciver, String message) {
        this.sender = sender;
        this.reciver = reciver;
        this.message = message;
    }

    public PM(String string) {
        String[] splited = string.split(";");
        sender = splited[0];
        reciver = splited[1];

        for (int i = 2; i < splited.length; i++) {
            message += splited[i];

        }


    }


    @Override
    public String toString() {
        return sender + ";" + reciver + ";" + message;
    }
}
