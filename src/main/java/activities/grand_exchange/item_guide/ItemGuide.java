package activities.grand_exchange.item_guide;

import com.google.gson.JsonElement;
import com.google.gson.JsonParseException;
import com.google.gson.JsonParser;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;

import java.io.File;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.IOException;
import java.net.URL;
import java.nio.channels.Channels;
import java.nio.channels.ReadableByteChannel;
import java.nio.file.Paths;
import java.util.*;

public class ItemGuide {
    private static final Map<String, Integer> allGEItems = new HashMap<>();

    public static Map<String, Integer> getAllGEItems() {
        if (allGEItems.isEmpty()) {

            File summaryFile = Paths.get(System.getProperty("user.home"), "OSBot", "Data", "explv_aio_osrsbox_summary.json").toFile();

            try {
                if (!summaryFile.exists()) {
                    System.out.println("Downloading item JSON from OSRS Box Github");
                    URL website = new URL("https://raw.githubusercontent.com/osrsbox/osrsbox-db/master/data/items/items-cache-data.json");
                    ReadableByteChannel rbc = Channels.newChannel(website.openStream());
                    FileOutputStream fos = new FileOutputStream(summaryFile);
                    fos.getChannel().transferFrom(rbc, 0, Long.MAX_VALUE);
                }

                try (FileReader fileReader = new FileReader(summaryFile)) {
                    JsonParser p = new JsonParser();
                    JsonElement allItems = p.parse(fileReader);
                    tradeableItems(allItems);

                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        return allGEItems;
    }

    private static void tradeableItems(JsonElement jsonElement) throws JsonParseException {
        Set<Map.Entry<String, JsonElement>> entrySet = jsonElement.getAsJsonObject().entrySet();
        for (Map.Entry<String, JsonElement> itemMapEntry : entrySet) { //item object
            boolean tradeable_on_ge = true;
            boolean noted = true;
            boolean placeholder = true;
            String name = null;
            Integer id = null;
            for (Map.Entry<String, JsonElement> itemProperties : itemMapEntry.getValue().getAsJsonObject().entrySet()) { //item properties
                String key = itemProperties.getKey();
                JsonElement val = itemProperties.getValue();
                if (key.equals("name")) name = val.getAsString();
                if (key.equals("id")) id = val.getAsInt();
                if (key.equals("noted")) noted = val.getAsBoolean();
                if (key.equals("tradeable_on_ge")) tradeable_on_ge = val.getAsBoolean();
                if (key.equals("placeholder")) placeholder = val.getAsBoolean();
            }
            if(tradeable_on_ge && !noted && !placeholder && name != null && id != null) {
                allGEItems.put(name, id);
            }
        }
    }
}
