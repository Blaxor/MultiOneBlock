package ro.deiutzblaxo.oneblock.communication.action.invite;

import lombok.Getter;
import ro.deiutzblaxo.oneblock.communication.action.Responses;

import java.io.Serializable;

@Getter

public class ResponseInvite implements Serializable {

    private final String invited;
    private final Responses error;
    private final String inviter;

    public ResponseInvite(String invited, String inviter, Responses error) {
        this.invited = invited;
        this.error = error;
        this.inviter=inviter;
    }

    public ResponseInvite(String string) {
        String[] splited = string.split(";");
        this.invited = splited[0];
        this.inviter = splited[1];
        this.error = Responses.valueOf(splited[2]);
    }

    @Override
    public String toString() {
        return invited + ";" +inviter+";"+ error.name();
    }
}
