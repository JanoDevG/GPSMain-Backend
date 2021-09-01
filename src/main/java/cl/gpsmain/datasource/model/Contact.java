package cl.gpsmain.datasource.model;

import com.fasterxml.jackson.annotation.JsonProperty;
import org.springframework.data.mongodb.core.mapping.Document;

import java.util.List;

@Document(collection = "contact")
public class Contact {

    @JsonProperty("names")
    private String names;

    @JsonProperty("surnames")
    private String surnames;

    @JsonProperty("numbers")
    private List<String> numbers;

    @JsonProperty("mails")
    private List<String> mails;

    public Contact() {
    }

    public Contact(final String names, final String surnames, final List<String> numbers, final List<String> mails) {
        this.names = names;
        this.surnames = surnames;
        this.numbers = numbers;
        this.mails = mails;
    }

    public String getNames() {
        return names;
    }

    public void setNames(final String names) {
        this.names = names;
    }

    public String getSurnames() {
        return surnames;
    }

    public void setSurnames(final String surnames) {
        this.surnames = surnames;
    }

    public List<String> getNumbers() {
        return numbers;
    }

    public void setNumbers(final List<String> numbers) {
        this.numbers = numbers;
    }

    public List<String> getMails() {
        return mails;
    }

    public void setMails(final List<String> mails) {
        this.mails = mails;
    }
}
