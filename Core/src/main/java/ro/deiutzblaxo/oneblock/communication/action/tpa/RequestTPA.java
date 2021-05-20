package ro.deiutzblaxo.oneblock.communication.action.tpa;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class RequestTPA {

    private String invited;
    private String inviter;
    private String server;

    public RequestTPA(String string) {
        String[] str = string.split(",");
        invited = str[0];
        inviter = str[1];
        server = str[2];
    }

    public RequestTPA(String invited, String inviter, String server) {
        this.inviter = inviter;
        this.invited = invited;
        this.server = server;
    }

    @Override
    public String toString() {
        return invited + "," + inviter + "," + server;
    }
}
