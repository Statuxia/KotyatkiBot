package me.statuxia.config;

import lombok.AccessLevel;
import lombok.Data;
import lombok.Setter;
import org.jetbrains.annotations.NotNull;
import org.json.JSONObject;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

@Data
public final class JSONManager {
    @Setter(AccessLevel.PRIVATE)
    private Path path;
    private JSONObject jsonObject;

    public JSONManager(@NotNull String path) throws IOException {
        this.path = getPath(path);
        create();
        this.jsonObject = getObject();
        updateFile();
    }

    private @NotNull Path getPath(@NotNull String stringPath) {
        return Paths.get(stringPath);
    }

    private void create() throws IOException {
        if (!Files.isDirectory(path.getParent())) {
            Files.createDirectories(path.getParent());
        }
        if (Files.notExists(path)) {
            Files.createFile(path);
        }
    }

    public @NotNull JSONManager updateFile() throws IOException {
        Files.writeString(path, this.jsonObject.toString(), StandardCharsets.UTF_8);
        return this;
    }

    public @NotNull JSONManager updateFile(@NotNull JSONObject jsonObject, boolean saveInObject) throws IOException {
        if (saveInObject) {
            this.jsonObject = jsonObject;
        }
        Files.writeString(path, jsonObject.toString(), StandardCharsets.UTF_8);
        return this;
    }

    public @NotNull JSONObject getObject() throws IOException {
        create();
        try {
            return new JSONObject(Files.readString(this.path, StandardCharsets.UTF_8));
        } catch (Exception exception) {
            return new JSONObject().put("token", "default").put("guildID", 1106315972547194983L);
        }
    }

    public static @NotNull JSONManager of(@NotNull String path) throws IOException {
        return new JSONManager(path);
    }
}