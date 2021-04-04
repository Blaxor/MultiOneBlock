package ro.deiutzblaxo.oneblock.communication.action.invite;

import lombok.Getter;

import java.io.Serializable;

@Getter
public class RequestInvite implements Serializable {


    public String invited;
    public String inviter;
    public String islandInviter;


    public RequestInvite(String str) {
        String[] splited = str.split(";");

        invited = splited[0];
        inviter = splited[1];
        islandInviter = splited[2];

    }

    public RequestInvite(String invited,String inviter,String islandInviter) {

        this.invited = invited;
        this.inviter = inviter;
        this.islandInviter=islandInviter;



    }

    @Override
    public String toString() {
        return invited+";"+inviter+";"+islandInviter;
    }
}
