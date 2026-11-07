package es.iespuertodelacruz.routinefights.shared.graphql;

import org.springframework.graphql.data.method.annotation.Argument;
import org.springframework.graphql.data.method.annotation.QueryMapping;
import org.springframework.security.access.annotation.Secured;
import org.springframework.stereotype.Controller;

import es.iespuertodelacruz.routinefights.shared.services.DeeplService;

/** DeepL translation (replaces GET /deepl/translate). */
@Controller
public class TranslationResolver {

    private final DeeplService deeplService;

    public TranslationResolver(DeeplService deeplService) {
        this.deeplService = deeplService;
    }

    @Secured({ "ROLE_USER", "ROLE_ADMIN" })
    @QueryMapping("translate")
    public String translate(@Argument String text, @Argument String targetLang) {
        return deeplService.translateText(text, targetLang);
    }
}
