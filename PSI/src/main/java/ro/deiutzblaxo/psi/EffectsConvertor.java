package ro.deiutzblaxo.psi;

import org.bukkit.inventory.ItemStack;
import org.bukkit.potion.PotionEffect;
import org.bukkit.util.io.BukkitObjectInputStream;
import org.bukkit.util.io.BukkitObjectOutputStream;
import org.yaml.snakeyaml.external.biz.base64Coder.Base64Coder;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.util.ArrayList;
import java.util.Collection;

public class EffectsConvertor {


    public static byte[] serialize(Collection<PotionEffect> obj) {
        try {
            ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
            BukkitObjectOutputStream dataOutput = new BukkitObjectOutputStream(outputStream);

            dataOutput.writeInt(obj.size());

            for (int i = 0; i < obj.size(); i++) {
                dataOutput.writeObject(obj.toArray()[i]);

            }

            dataOutput.close();
            return outputStream.toByteArray();
        } catch (Exception e) {
            throw new IllegalStateException("Unable to save potion effect.", e);
        }
    }

    public static Collection<PotionEffect> deserialize(byte[] str) {
        try {
            ByteArrayInputStream inputStream = new ByteArrayInputStream(str);
            BukkitObjectInputStream dataInput = new BukkitObjectInputStream(inputStream);
            Collection<PotionEffect> effects = new ArrayList<PotionEffect>(dataInput.readInt());

            for (int i = 0; i < effects.size(); i++) {
                effects.add((PotionEffect) dataInput.readObject());
            }

            dataInput.close();
            return effects;
        } catch (Exception e) {
            e.printStackTrace();
            return new ArrayList<>();
        }
    }
}
