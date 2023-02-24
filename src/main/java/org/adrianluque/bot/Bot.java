package org.adrianluque.bot;

import java.io.FileReader;
import java.io.IOException;
import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.List;
import java.util.Map;

import org.javacord.api.DiscordApi;
import org.javacord.api.DiscordApiBuilder;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.reflect.TypeToken;
import com.google.gson.stream.JsonReader;

public class Bot {

    public class Config {
        private String token;
        public ArrayList<Map<String, String>> owner;
        public String username;
        public String tag;
        public String defaultPrefix;

        public String getToken() {
            return token;
        }

        public String getOwnerId() {
            return owner.get(0).get("id");
        }

        public String getOwnerUserName() {
            return owner.get(0).get("username");
        }

        public String getOwnerTag() {
            return owner.get(0).get("tag");
        }
    }

    public class Command {
        public String prefix;
        public String name;
        public ArrayList<Map<String, Object>> action;

        public String getActionToDo() {
            return String.valueOf(action.get(0).get("todo"));
        }

        public ArrayList<String> getActionParameters() {
            ArrayList<String> parametersList = null;
            try{
                JsonReader jsonReader = new JsonReader(new FileReader("commands.json"));
                Gson gson = new Gson();
                JsonArray jsonArray = gson.fromJson(jsonReader, JsonArray.class);
                parametersList = new ArrayList<String>();
                for (JsonElement jsonElement : jsonArray) {
                    JsonObject jsonObject = jsonElement.getAsJsonObject();
                    JsonArray parametersArray = jsonObject.getAsJsonArray("action").get(0).getAsJsonObject()
                            .getAsJsonArray("parameters");
                    for (JsonElement parameter : parametersArray) {
                        parametersList.add(parameter.getAsString());
                    }
                }
            } catch (IOException e) {}
            
            return parametersList;
        }

    }

    public Config config;
    public ArrayList<Command> commands;

    // Constructor
    public Bot() throws Exception {
        autoConfig();
    }

    // Autoconfig
    private void autoConfig() throws IOException {
        JsonReader jsonReader;
        Gson gson = new Gson();

        jsonReader = new JsonReader(new FileReader("config.json"));
        config = gson.fromJson(jsonReader, Config.class);

        jsonReader = new JsonReader(new FileReader("commands.json"));
        Type listType = new TypeToken<List<Command>>() {
        }.getType();
        commands = gson.fromJson(jsonReader, listType);
    }
}
