package es.iespuertodelacruz.routinefights.shared.controllers;


import es.iespuertodelacruz.routinefights.shared.services.DeeplService;
import io.swagger.v3.oas.annotations.tags.Tag;

import org.springframework.security.access.annotation.Secured;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;


@RequestMapping("/deepl")
@RestController()
@Tag(name = "DEEPL", description = "DeepL Translations")
public class DeeplController {
    private final DeeplService deeplService;
    public DeeplController(DeeplService deeplService) {
        this.deeplService = deeplService;
    }
    @GetMapping("/translate")
    @Secured({"ROLE_ADMIN", "ROLE_USER"})
    public String translate(@RequestParam String text, @RequestParam String targetLang) {
        return deeplService.translateText(text, targetLang);
    }
    
}
