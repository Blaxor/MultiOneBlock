package ro.deiutzblaxo.oneblock.menu.objects.buttons;

import lombok.Data;
import org.bukkit.inventory.ItemStack;
import ro.deiutzblaxo.oneblock.menu.objects.Menu;


@Data
public class ButtonObject {

    private ItemStack item;
    private String command;
    private Menu menu;
    private String message;


    public ButtonObject(ItemStack item) {
        this.item = item;
    }

    public ButtonObject(Menu menu) {
        this.menu = menu;
    }

    public ButtonObject(String message, boolean isMessage) {
        if (isMessage) {
            this.message = message;
            return;
        }
        this.command = message;
    }


}
