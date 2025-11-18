package es.iespuertodelacruz.routinefights.shared.services;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.springframework.core.io.Resource;
import org.springframework.core.io.ResourceLoader;
import org.springframework.stereotype.Service;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;

@Service
public class TranslationService {

    private final ObjectMapper objectMapper = new ObjectMapper();
    private File translationsDirectory;
    private final Map<String, Map<String, String>> translations = new ConcurrentHashMap<>();
    private static final Pattern PLACEHOLDER = Pattern.compile("\\{([^}]+)\\}");

    public TranslationService(ResourceLoader resourceLoader) {
        try {
            Resource resource = resourceLoader.getResource("classpath:translations");
            if (resource.exists() && resource.getFile().isDirectory()) {
                this.translationsDirectory = resource.getFile();
            } else {
                this.translationsDirectory = new File("translations");
            }
        } catch (Exception e) {
            this.translationsDirectory = new File("translations");
        }
        loadTranslations();
    }

    public File getTranslationsDirectory() {
        return translationsDirectory;
    }

    public void setTranslationsDirectory(File translationsDirectory) {
        this.translationsDirectory = translationsDirectory;
        loadTranslations();
    }

    /**
     * Translate a key to targetLanguage and replace named placeholders with values
     * from args map. Placeholders must be in the form {name}.
     *
     * @param key            translation key (the JSON top-level key)
     * @param targetLanguage language code matching the filename (e.g. "en", "es")
     * @param args           named arguments used to format placeholders
     * @return translated and formatted string (if not found returns the key)
     */
    public String translate(String key, String targetLanguage, Map<String, ?> args) {
        if (key == null) {
            return null;
        }
        Map<String, String> langMap = translations.get(targetLanguage);
        if (langMap == null) {
            langMap = translations.getOrDefault("en-US", translations.values().stream().findFirst().orElse(null));
        }
        String template = (langMap == null) ? null : langMap.get(key);
        if (template == null) {
            return key;
        }
        if (args == null || args.isEmpty()) {
            return template;
        }
        Matcher m = PLACEHOLDER.matcher(template);
        StringBuffer sb = new StringBuffer();
        while (m.find()) {
            String name = m.group(1);
            Object replacement = args.get(name);
            String rep = replacement == null ? "" : replacement.toString();
            rep = rep.replace("$", "\\$");
            m.appendReplacement(sb, rep);
        }
        m.appendTail(sb);
        return sb.toString();
    }

    public String translate(String key, String targetLanguage) {
        return translate(key, targetLanguage, null);
    }

    private void loadTranslations() {
        translations.clear();
        if (translationsDirectory == null) {
            return;
        }
        File[] files = translationsDirectory.listFiles((d, name) -> name.toLowerCase().endsWith(".json"));
        if (files == null) {
            return;
        }
        for (File f : files) {
            try {
                String name = f.getName();
                String lang = name.substring(0, name.lastIndexOf('.'));
                byte[] bytes = Files.readAllBytes(f.toPath());
                Map<String, String> map = objectMapper.readValue(bytes, new TypeReference<Map<String, String>>() {});
                translations.put(lang, map);
            } catch (IOException e) {
                // ignore malformed files, continue loading others
            }
        }
    }

    public List<String> getSupportedLanguages() {
        return translations.keySet().stream().toList();
    }
}
