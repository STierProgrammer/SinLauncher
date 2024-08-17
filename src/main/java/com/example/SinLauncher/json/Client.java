package com.example.SinLauncher.json;

import java.lang.reflect.Type;
import java.util.Map;

import com.example.SinLauncher.SinLauncherEntites.Arch;
import com.example.SinLauncher.SinLauncherEntites.Os;
import com.google.gson.JsonDeserializationContext;
import com.google.gson.JsonDeserializer;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParseException;
import com.google.gson.annotations.SerializedName;

import lombok.ToString;

@ToString
public class Client {

    @ToString
    public static class Features {
        @SerializedName("is_demo_user")
        public Boolean isDemoUser = false;

        @SerializedName("has_custom_resolution")
        public Boolean hasCustomResolution = false;

        @SerializedName("has_quick_plays_support")
        public Boolean hasQuickPlaysSupport = false;

        @SerializedName("is_quick_play_singleplayer")
        public Boolean isQuickPlaySingleplayer = false;

        @SerializedName("is_quick_play_multiplayer")
        public Boolean isQuickPlayMultiplayer = false;

        @SerializedName("is_quick_play_realms")
        public Boolean isQuickPlayRealms = false;
    }

    @ToString
    public static class OSRules {
        public Os name;
        public Arch arch;
        public String version;
    }

    @ToString
    public static class Rule {
        public String action;
        public OSRules os;
        public Features features;
    }

    @ToString
    public static class Argument {
        public String value;
        public String[] values;
        public Rule[] rules;
    }

    // each Argument may be a plain string or a struct which looks like this
    // {
    // Rule[] rules;
    // String | String[] value;
    // }
    // we want to deserialize this into the Argument class
    public static class ArgumentDeserializer implements JsonDeserializer<Argument> {
        @Override
        public Argument deserialize(JsonElement ele, Type type, JsonDeserializationContext context)
                throws JsonParseException {
            var argument = new Argument();

            if (ele.isJsonPrimitive() && ele.getAsJsonPrimitive().isString()) {
                argument.value = ele.getAsString();
            } else if (ele.isJsonObject()) {
                JsonObject jsonObject = ele.getAsJsonObject();

                if (jsonObject.has("value")) {
                    JsonElement valueObj = jsonObject.get("value");

                    if (valueObj.isJsonPrimitive()) {
                        argument.value = valueObj.getAsString();
                    } else if (valueObj.isJsonArray()) {
                        argument.values = context.deserialize(valueObj.getAsJsonArray(), String[].class);
                    }
                }

                if (jsonObject.has("rules")) {
                    argument.rules = context.deserialize(jsonObject.get("rules"), Rule[].class);
                }
            }

            return argument;
        }
    }

    @ToString
    public static class Arguments {
        public Argument[] game;
        public Argument[] jvm;
    }

    @ToString
    public static class Download {
        public String id;
        public String path;
        public String sha1;
        public long size;
        public Long totalSize;
        public String url;
    }

    @ToString
    public static class ClientDownloads {
        public Download client;
        public Download client_mappings;
        public Download server;
        public Download server_mappings;
    }

    @ToString
    public static class JavaVersion {
        public String component;
        public int majorVersion;
    }

    @ToString
    public static class LibraryDownloads {
        public Download artifact;
        public Map<String, Download> classifiers;
    }

    public static class LibraryExtractRules {
        public String[] exclude;
        public String[] include;
    }

    public static class Library {
        public LibraryDownloads downloads;
        public String name;
        public Rule[] rules;
        public Map<Os, String> natives;
        public LibraryExtractRules extract;
    }

    public static class LoggingClient {
        public String argument;
        public Download file;
        public String type;
    }

    public static class LoggingInfo {
        public LoggingClient client;
    }

    // actual client.json
    public Arguments arguments;
    public Download assetIndex;
    public String assets;

    public short complianceLevel;

    public ClientDownloads downloads;
    public String id;
    public JavaVersion javaVersion;

    public Library[] libraries;
    public LoggingInfo logging;

    public String mainClass;
    // removed in 1.13, replaced with arguments above
    public String minecraftArguments;
    // TODO: use an enum
    public String type;
}
