package ro.deiutzblaxo.oneblock.communication.action.tpa;

import lombok.Getter;
import lombok.Setter;
import ro.deiutzblaxo.oneblock.communication.action.Responses;

@Getter
@Setter
public class ResponseTPA {

    private String inviter;
    private String invited;
    private Responses response;

    public ResponseTPA(String inviter, String invited, Responses response) {
        this.invited = invited;
        this.inviter = inviter;
        this.response = response;
    }

    public ResponseTPA(String string) {
        String[] split = string.split(",");
        inviter = split[0];
        invited = split[1];
        response = Responses.valueOf(split[2]);
    }

    @Override
    public String toString() {
        return inviter + "," + invited + "," + response.name();
    }
}
