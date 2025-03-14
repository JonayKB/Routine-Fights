package es.iespuertodelacruz.routinefights.shared.config;

import org.springframework.boot.context.properties.ConfigurationProperties;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import java.util.Objects;

@ConfigurationProperties(prefix = "sendgrid")
class SendGridConfigurationProperties {
    @NotBlank
    @Pattern(regexp = "^SG[0-9a-zA-Z._]{67}$")
    private String apiKey;

    @Email
    @NotBlank
    private String fromEmail;

    @NotBlank
    private String fromName;

    public SendGridConfigurationProperties() {
    }

    public SendGridConfigurationProperties(String apiKey, String fromEmail, String fromName) {
        this.apiKey = apiKey;
        this.fromEmail = fromEmail;
        this.fromName = fromName;
    }

    public String getApiKey() {
        return this.apiKey;
    }

    public void setApiKey(String apiKey) {
        this.apiKey = apiKey;
    }

    public String getFromEmail() {
        return this.fromEmail;
    }

    public void setFromEmail(String fromEmail) {
        this.fromEmail = fromEmail;
    }

    public String getFromName() {
        return this.fromName;
    }

    public void setFromName(String fromName) {
        this.fromName = fromName;
    }

    public SendGridConfigurationProperties apiKey(String apiKey) {
        setApiKey(apiKey);
        return this;
    }

    public SendGridConfigurationProperties fromEmail(String fromEmail) {
        setFromEmail(fromEmail);
        return this;
    }

    public SendGridConfigurationProperties fromName(String fromName) {
        setFromName(fromName);
        return this;
    }

    @Override
    public boolean equals(Object o) {
        if (o == this)
            return true;
        if (!(o instanceof SendGridConfigurationProperties)) {
            return false;
        }
        SendGridConfigurationProperties sendGridConfigurationProperties = (SendGridConfigurationProperties) o;
        return Objects.equals(apiKey, sendGridConfigurationProperties.apiKey) && Objects.equals(fromEmail, sendGridConfigurationProperties.fromEmail) && Objects.equals(fromName, sendGridConfigurationProperties.fromName);
    }

    @Override
    public int hashCode() {
        return Objects.hash(apiKey, fromEmail, fromName);
    }

    @Override
    public String toString() {
        return "{" +
            " apiKey='" + getApiKey() + "'" +
            ", fromEmail='" + getFromEmail() + "'" +
            ", fromName='" + getFromName() + "'" +
            "}";
    }

}