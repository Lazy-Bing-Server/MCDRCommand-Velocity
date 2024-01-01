package one.lbs.velocitymcdrcommand;

import com.google.gson.JsonParseException;
import com.google.inject.Inject;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.IOException;
import java.nio.charset.Charset;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentSkipListSet;


public class Config {
    protected final Path path;
    private ConfigData configData = new ConfigData();
    public VelocityMCDRCommand pluginInst;

    public Config(VelocityMCDRCommand pluginInst, Path path) {
        this.path = path;
        this.pluginInst = pluginInst;
    }

    // Configuration read & write
    public boolean save() {
        if (!Files.exists(path)) {
            try {
                Files.createFile(path);
            } catch (IOException e) {
                e.printStackTrace();
                pluginInst.logger.error("Save {} error: createFile fail.", path);
                return false;
            }
        }
        BufferedWriter bfw;
        try {
            bfw = Files.newBufferedWriter(path, StandardCharsets.UTF_8);
        } catch (IOException e) {
            e.printStackTrace();
            pluginInst.logger.error("Save {} error: newBufferedWriter fail.", path);
            return false;
        }

        try {
            bfw.write(VelocityMCDRCommand.GSON.toJson(getData()));
            bfw.close();
        } catch (IOException e) {
            e.printStackTrace();
            pluginInst.logger.error("Save {} error: bfw.write fail.", path);
            return false;
        }
        return true;
    }

    public boolean load() {
        if (!Files.exists(path)) {
            return save();
        }
        try {
            BufferedReader bfr = Files.newBufferedReader(path, StandardCharsets.UTF_8);
            setData(VelocityMCDRCommand.GSON.fromJson(bfr, ConfigData.class));
            bfr.close();
        } catch (IOException e) {
            e.printStackTrace();
            pluginInst.logger.error("Load {} error: newBufferedReader fail.", path);
            return false;
        } catch (JsonParseException e) {
            pluginInst.logger.error("Json {} parser fail!!", path);
            return false;
        }
        return true;
    }

    protected ConfigData getData() {
        return configData;
    }

    protected void setData(ConfigData data) {
        configData = data;
    }


    public static class ConfigData {
        public boolean enableMCDRCommandSuggestion = true;
        public boolean enablePrintPlayerChat = true;
        public String playerChatLogFormat = "[{0}] <{1}> {2}";
        public boolean enableAlertRaw = true;
    }
}