package net.hydrotuconnais.custompets.utils;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import net.hydrotuconnais.custompets.commands.AdminPetsCommand;

import java.io.*;
import java.lang.reflect.Type;
import java.util.Map;
import java.util.Set;
import java.util.UUID;

public class FileControl {
    private static final String PERMISSIONS_FILE = "config/custompets/player_pets.json";
    private static final Gson GSON = new Gson();

    public static void savePermissions() {
        File file = new File(PERMISSIONS_FILE);
        file.getParentFile().mkdirs();
        try (Writer writer = new FileWriter(file)) {
            GSON.toJson(AdminPetsCommand.playerPetPermissions, writer);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static void loadPermissions() {
        File file = new File(PERMISSIONS_FILE);
        if (!file.exists()) return;
        try (Reader reader = new FileReader(file)) {
            Type type = new TypeToken<Map<UUID, Set<String>>>(){}.getType();
            Map<UUID, Set<String>> loaded = GSON.fromJson(reader, type);
            if (loaded != null) {
                AdminPetsCommand.playerPetPermissions.clear();
                AdminPetsCommand.playerPetPermissions.putAll(loaded);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
