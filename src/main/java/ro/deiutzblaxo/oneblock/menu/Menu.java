package ro.deiutzblaxo.oneblock.menu;

import java.util.HashMap;

public interface Menu {

    String getTitle();

    String getId();

    HashMap<Integer, Button> getContents();
}
