package ro.deiutzblaxo.schematicsave;

import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.util.Vector;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;

import java.io.File;
import java.io.FileReader;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

public class JsonReadFile {




    public static Island centerFromDB(File file){

        String[] center = read(file , "center").replace("[","").replace("]","").split(",");

        String[] nonEdited = read(file, "members").replace("{","").replace("}","")
                .replace("\"","").split(",");
        String[] memb = new String[nonEdited.length];

        for (int i = 0; i < memb.length; i++) {
            memb[i] = nonEdited[i].split(",")[0].replaceAll(" ", "");
        }

        Island island  = new Island(new Location(Bukkit.getWorld(center[0].replaceAll("\"","")),Double.parseDouble(center[1]),Double.parseDouble(center[2]),Double.parseDouble(center[3])),
                read(file,"uniqueId") , memb , read(file,"owner").replaceAll("\"",""));
        return island;
    }


    protected static String read(File file, String field){
        JSONParser parser = new JSONParser();
        try {
            Object obj = parser.parse(new FileReader(file));

            // A JSON object. Key value pairs are unordered. JSONObject supports java.util.Map interface.
            JSONObject jsonObject = (JSONObject) obj;

            // A JSON array. JSONObject supports java.util.List interface.
            return jsonObject.get(field).toString();


        } catch (Exception e) {
            e.printStackTrace();
        }
        return "Nothing here" ;
    }

}
