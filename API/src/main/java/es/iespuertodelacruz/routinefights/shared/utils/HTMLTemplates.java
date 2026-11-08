package es.iespuertodelacruz.routinefights.shared.utils;

import java.util.Locale;
import java.util.Map;

import org.springframework.web.util.HtmlUtils;

/**
 * HTML for the verification e-mail and for the page the verification link opens.
 *
 * Both follow the app's look (warm neutrals, ember accent) and switch to the dark
 * palette when the reader's mail client or browser is in dark mode. Copy is
 * available in the same languages as the app; anything else falls back to English.
 */
public class HTMLTemplates {
    private HTMLTemplates() {
    }

    /** Result of opening the verification link. */
    public enum VerifyResult {
        VERIFIED, MISSING_DATA, ERROR, BAD_REQUEST
    }

    public static final String DEFAULT_LANGUAGE = "en";

    private record EmailCopy(String subject, String subjectNewEmail, String preheader, String heading,
            String body, String bodyNewEmail, String button, String fallback, String ignore) {
    }

    private record PageCopy(String verifiedTitle, String verifiedBody, String missingTitle, String missingBody,
            String errorTitle, String errorBody, String openApp) {
    }

    private static final Map<String, EmailCopy> EMAIL = Map.of(
            "en", new EmailCopy(
                    "Confirm your email for Routine Fights",
                    "Confirm your new email for Routine Fights",
                    "One tap and your account is ready.",
                    "Hi %s, confirm your email",
                    "Thanks for joining Routine Fights. Confirm this address to activate your account and start building your streaks.",
                    "You changed the email on your Routine Fights account. Confirm this address to keep using it.",
                    "Confirm email",
                    "If the button doesn't work, copy this link into your browser:",
                    "If you didn't request this, you can safely ignore this message."),
            "es", new EmailCopy(
                    "Confirma tu correo en Routine Fights",
                    "Confirma tu nuevo correo en Routine Fights",
                    "Un toque y tu cuenta estará lista.",
                    "Hola %s, confirma tu correo",
                    "Gracias por unirte a Routine Fights. Confirma esta dirección para activar tu cuenta y empezar a encender tus rachas.",
                    "Has cambiado el correo de tu cuenta de Routine Fights. Confirma esta dirección para seguir usándola.",
                    "Confirmar correo",
                    "Si el botón no funciona, copia este enlace en tu navegador:",
                    "Si no has sido tú, puedes ignorar este mensaje."),
            "fr", new EmailCopy(
                    "Confirmez votre e-mail pour Routine Fights",
                    "Confirmez votre nouvel e-mail pour Routine Fights",
                    "Un geste et votre compte est prêt.",
                    "Bonjour %s, confirmez votre e-mail",
                    "Merci d'avoir rejoint Routine Fights. Confirmez cette adresse pour activer votre compte et lancer vos séries.",
                    "Vous avez modifié l'e-mail de votre compte Routine Fights. Confirmez cette adresse pour continuer à l'utiliser.",
                    "Confirmer l'e-mail",
                    "Si le bouton ne fonctionne pas, copiez ce lien dans votre navigateur :",
                    "Si vous n'êtes pas à l'origine de cette demande, ignorez ce message."),
            "de", new EmailCopy(
                    "Bestätige deine E-Mail für Routine Fights",
                    "Bestätige deine neue E-Mail für Routine Fights",
                    "Ein Tipp und dein Konto ist bereit.",
                    "Hallo %s, bestätige deine E-Mail",
                    "Danke, dass du bei Routine Fights dabei bist. Bestätige diese Adresse, um dein Konto zu aktivieren und deine Serien zu starten.",
                    "Du hast die E-Mail deines Routine-Fights-Kontos geändert. Bestätige diese Adresse, um es weiter zu nutzen.",
                    "E-Mail bestätigen",
                    "Falls der Button nicht funktioniert, kopiere diesen Link in deinen Browser:",
                    "Wenn du das nicht angefordert hast, kannst du diese Nachricht ignorieren."));

    private static final Map<String, PageCopy> PAGE = Map.of(
            "en", new PageCopy(
                    "Email confirmed", "Your account is ready. Go back to the app and log in to light your first flame.",
                    "Something is missing", "This link is incomplete. Open the latest email we sent you and tap the button again.",
                    "We couldn't confirm your email", "The link may have expired or already been used. Try logging in, or request a new email from the app.",
                    "You can close this page."),
            "es", new PageCopy(
                    "Correo confirmado", "Tu cuenta está lista. Vuelve a la app e inicia sesión para encender tu primera llama.",
                    "Falta información", "Este enlace está incompleto. Abre el último correo que te enviamos y pulsa el botón de nuevo.",
                    "No hemos podido confirmar tu correo", "Puede que el enlace haya caducado o ya se haya usado. Prueba a iniciar sesión o pide un nuevo correo desde la app.",
                    "Ya puedes cerrar esta página."),
            "fr", new PageCopy(
                    "E-mail confirmé", "Votre compte est prêt. Revenez dans l'application et connectez-vous pour allumer votre première flamme.",
                    "Il manque des informations", "Ce lien est incomplet. Ouvrez le dernier e-mail que nous vous avons envoyé et touchez à nouveau le bouton.",
                    "Impossible de confirmer votre e-mail", "Le lien a peut-être expiré ou déjà été utilisé. Essayez de vous connecter ou demandez un nouvel e-mail depuis l'application.",
                    "Vous pouvez fermer cette page."),
            "de", new PageCopy(
                    "E-Mail bestätigt", "Dein Konto ist bereit. Geh zurück zur App und melde dich an, um deine erste Flamme zu entfachen.",
                    "Hier fehlt etwas", "Dieser Link ist unvollständig. Öffne die letzte E-Mail von uns und tippe erneut auf den Button.",
                    "Wir konnten deine E-Mail nicht bestätigen", "Der Link ist vielleicht abgelaufen oder wurde schon benutzt. Melde dich an oder fordere in der App eine neue E-Mail an.",
                    "Du kannst diese Seite schließen."));

    /**
     * Picks one of the supported languages from a tag such as "es-ES", "fr" or an
     * Accept-Language header ("de-DE,de;q=0.9,en;q=0.8"). Unknown or empty → English.
     */
    public static String resolveLanguage(String tag) {
        if (tag == null || tag.isBlank()) {
            return DEFAULT_LANGUAGE;
        }
        for (String part : tag.split(",")) {
            String code = part.split(";")[0].trim().replace('_', '-').split("-")[0].toLowerCase(Locale.ROOT);
            if (EMAIL.containsKey(code)) {
                return code;
            }
        }
        return DEFAULT_LANGUAGE;
    }

    public static String verificationSubject(String language, boolean newEmail) {
        EmailCopy t = EMAIL.get(resolveLanguage(language));
        return newEmail ? t.subjectNewEmail() : t.subject();
    }

    /**
     * Verification e-mail. Layout uses tables and inline styles because many mail
     * clients drop or ignore stylesheets; the {@code <style>} block only adds the
     * dark palette for the clients that honour prefers-color-scheme.
     */
    public static String verificationEmail(String language, String username, String link, boolean newEmail) {
        String lang = resolveLanguage(language);
        EmailCopy t = EMAIL.get(lang);
        String name = HtmlUtils.htmlEscape(username == null ? "" : username);
        String href = HtmlUtils.htmlEscape(link);
        return """
                <!DOCTYPE html>
                <html lang="%1$s">
                <head>
                <meta charset="UTF-8">
                <meta name="viewport" content="width=device-width, initial-scale=1">
                <meta name="color-scheme" content="light dark">
                <meta name="supported-color-schemes" content="light dark">
                <title>%2$s</title>
                <style>
                  @media (prefers-color-scheme: dark) {
                    .rf-page { background-color: #161513 !important; }
                    .rf-card { background-color: #1C1A18 !important; border-color: #2E2B27 !important; }
                    .rf-text { color: #EFE9DE !important; }
                    .rf-muted { color: #AAA398 !important; }
                    .rf-faint { color: #8A847A !important; }
                    .rf-link { color: #FB923C !important; }
                    .rf-rule { border-color: #2E2B27 !important; }
                  }
                  @media (max-width: 480px) {
                    .rf-pad { padding: 28px 22px !important; }
                    .rf-h1 { font-size: 22px !important; }
                  }
                </style>
                </head>
                <body class="rf-page" style="margin:0;padding:0;background-color:#F7F4EE;">
                <div style="display:none;max-height:0;overflow:hidden;opacity:0;">%3$s</div>
                <table role="presentation" class="rf-page" width="100%%" cellpadding="0" cellspacing="0" border="0" style="background-color:#F7F4EE;">
                  <tr><td align="center" style="padding:40px 16px;">
                    <table role="presentation" width="100%%" cellpadding="0" cellspacing="0" border="0" style="max-width:520px;">
                      <tr><td style="padding:0 4px 20px;font-family:-apple-system,BlinkMacSystemFont,'Segoe UI',Roboto,Helvetica,Arial,sans-serif;">
                        <span style="display:inline-block;width:10px;height:10px;border-radius:5px;background-color:#C2410C;vertical-align:middle;"></span>
                        <span class="rf-text" style="font-size:17px;font-weight:700;letter-spacing:-0.2px;color:#1F1B16;vertical-align:middle;padding-left:6px;">Routine Fights</span>
                      </td></tr>
                      <tr><td class="rf-card rf-pad" style="background-color:#FFFFFF;border:1px solid #E8E2D7;border-radius:16px;padding:40px 36px;font-family:-apple-system,BlinkMacSystemFont,'Segoe UI',Roboto,Helvetica,Arial,sans-serif;">
                        <h1 class="rf-text rf-h1" style="margin:0 0 14px;font-size:24px;line-height:1.3;font-weight:700;color:#1F1B16;">%4$s</h1>
                        <p class="rf-muted" style="margin:0 0 28px;font-size:16px;line-height:1.6;color:#5E554A;">%5$s</p>
                        <table role="presentation" cellpadding="0" cellspacing="0" border="0"><tr>
                          <td style="border-radius:10px;background-color:#C2410C;">
                            <a href="%6$s" style="display:inline-block;padding:14px 28px;font-size:16px;font-weight:600;color:#FFFFFF;text-decoration:none;border-radius:10px;">%7$s</a>
                          </td>
                        </tr></table>
                        <hr class="rf-rule" style="border:none;border-top:1px solid #E8E2D7;margin:32px 0 20px;">
                        <p class="rf-faint" style="margin:0 0 8px;font-size:13px;line-height:1.5;color:#7B7266;">%8$s</p>
                        <p style="margin:0;font-size:13px;line-height:1.5;word-break:break-all;"><a class="rf-link" href="%6$s" style="color:#C2410C;text-decoration:underline;">%6$s</a></p>
                      </td></tr>
                      <tr><td class="rf-faint" style="padding:20px 4px 0;font-family:-apple-system,BlinkMacSystemFont,'Segoe UI',Roboto,Helvetica,Arial,sans-serif;font-size:12px;line-height:1.5;color:#7B7266;">%9$s</td></tr>
                    </table>
                  </td></tr>
                </table>
                </body>
                </html>
                """.formatted(lang, newEmail ? t.subjectNewEmail() : t.subject(), t.preheader(),
                t.heading().formatted(name), newEmail ? t.bodyNewEmail() : t.body(), href, t.button(),
                t.fallback(), t.ignore());
    }

    /** Page shown in the browser after tapping the link in the e-mail. */
    public static String verifyPage(String language, VerifyResult result) {
        String lang = resolveLanguage(language);
        PageCopy t = PAGE.get(lang);
        String title;
        String body;
        boolean ok = result == VerifyResult.VERIFIED;
        switch (result) {
            case VERIFIED -> {
                title = t.verifiedTitle();
                body = t.verifiedBody();
            }
            case MISSING_DATA -> {
                title = t.missingTitle();
                body = t.missingBody();
            }
            default -> {
                title = t.errorTitle();
                body = t.errorBody();
            }
        }
        // Check mark for success, exclamation mark otherwise.
        String icon = ok
                ? "<path d='M5 12.5l4.5 4.5L19 7.5' />"
                : "<path d='M12 7v6' /><path d='M12 17h.01' />";
        return """
                <!DOCTYPE html>
                <html lang="%1$s">
                <head>
                <meta charset="UTF-8">
                <meta name="viewport" content="width=device-width, initial-scale=1">
                <meta name="color-scheme" content="light dark">
                <title>%2$s · Routine Fights</title>
                <style>
                  :root {
                    --page: #F7F4EE; --card: #FFFFFF; --line: #E8E2D7;
                    --text: #1F1B16; --muted: #5E554A; --faint: #7B7266;
                    --accent: #C2410C; --badge: %4$s;
                  }
                  @media (prefers-color-scheme: dark) {
                    :root {
                      --page: #161513; --card: #1C1A18; --line: #2E2B27;
                      --text: #EFE9DE; --muted: #AAA398; --faint: #8A847A;
                      --accent: #FB923C; --badge: %5$s;
                    }
                  }
                  * { box-sizing: border-box; }
                  body {
                    margin: 0; min-height: 100vh; display: flex; align-items: center; justify-content: center;
                    padding: 24px 16px; background: var(--page); color: var(--text);
                    font-family: -apple-system, BlinkMacSystemFont, 'Segoe UI', Roboto, Helvetica, Arial, sans-serif;
                  }
                  main {
                    width: 100%%; max-width: 440px; background: var(--card); border: 1px solid var(--line);
                    border-radius: 16px; padding: 40px 32px; text-align: center;
                  }
                  .icon {
                    width: 56px; height: 56px; margin: 0 auto 20px; border-radius: 50%%;
                    display: flex; align-items: center; justify-content: center; background: var(--badge);
                  }
                  .icon svg { width: 28px; height: 28px; fill: none; stroke: var(--accent); stroke-width: 2.4;
                    stroke-linecap: round; stroke-linejoin: round; }
                  h1 { margin: 0 0 12px; font-size: 22px; line-height: 1.3; }
                  p { margin: 0; font-size: 16px; line-height: 1.6; color: var(--muted); }
                  .hint { margin-top: 24px; font-size: 13px; color: var(--faint); }
                  .brand { margin-top: 28px; font-size: 13px; font-weight: 700; color: var(--faint); }
                  .brand span { display: inline-block; width: 8px; height: 8px; border-radius: 4px;
                    background: var(--accent); margin-right: 6px; }
                </style>
                </head>
                <body>
                <main>
                  <div class="icon"><svg viewBox="0 0 24 24" aria-hidden="true">%3$s</svg></div>
                  <h1>%2$s</h1>
                  <p>%6$s</p>
                  %7$s
                  <div class="brand"><span></span>Routine Fights</div>
                </main>
                </body>
                </html>
                """.formatted(lang, title, icon,
                ok ? "#FDEBDD" : "#F3EEE6", ok ? "#3A2416" : "#262421",
                body, ok ? "<p class=\"hint\">" + t.openApp() + "</p>" : "");
    }
}
