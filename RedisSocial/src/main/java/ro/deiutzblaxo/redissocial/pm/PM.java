package ro.deiutzblaxo.redissocial.pm;

import lombok.Getter;
import lombok.Setter;
import net.md_5.bungee.api.chat.TextComponent;
import net.md_5.bungee.chat.ComponentSerializer;

import java.io.Serializable;

@Getter
@Setter
public class PM implements Serializable {
    public String message;
    public String uuidTarget;
    public String uuidSender;

    public PM(String target, String sender, TextComponent message) {
        this.message = ComponentSerializer.toString(message);
        this.uuidTarget = target;
        this.uuidSender = sender;
    }


    @Override
    @Deprecated
    public String toString() {
        return uuidTarget + "_._" + uuidSender + "_._" + message;
    }
}
