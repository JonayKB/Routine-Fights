package es.iespuertodelacruz.routinefights.shared.services;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Files;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.springframework.core.io.Resource;
import org.springframework.core.io.ResourceLoader;
import org.springframework.core.io.support.PathMatchingResourcePatternResolver;
import org.springframework.stereotype.Service;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;

import lombok.extern.java.Log;

@Service
@Log
public class TranslationService {

    private final ObjectMapper objectMapper = new ObjectMapper();
    private File translationsDirectory;
    private final Map<String, Map<String, String>> translations = new ConcurrentHashMap<>();
    private static final Pattern PLACEHOLDER = Pattern.compile("\\{([^}]+)\\}");

    public TranslationService(ResourceLoader resourceLoader) {
        try {
            Resource[] resources = new PathMatchingResourcePatternResolver()
                    .getResources("classpath:translations/*.json");

            for (Resource res : resources) {
                loadTranslationResource(res);
            }

            log.info("Loaded " + translations.size() + " translation files from classpath");
        } catch (Exception e) {
            log.warning("Could not load translations from classpath: " + e.getMessage());
        }
    }

    public File getTranslationsDirectory() {
        return translationsDirectory;
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

    private void loadTranslationResource(Resource res) {
        try (InputStream in = res.getInputStream()) {
            String filename = res.getFilename();
            if (filename == null || !filename.endsWith(".json"))
                return;

            String lang = filename.substring(0, filename.lastIndexOf('.'));

            Map<String, String> map = objectMapper.readValue(in,
                    new TypeReference<Map<String, String>>() {
                    });
            translations.put(lang, map);
        } catch (Exception e) {
            log.warning("Failed to load translation file " + res.getFilename() + ": " + e.getMessage());
        }
    }

    public List<String> getSupportedLanguages() {
        return translations.keySet().stream().toList();
    }
}
