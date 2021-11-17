package utils.HandlebarsUtils.Helpers;

import com.github.jknack.handlebars.Helper;
import com.github.jknack.handlebars.Options;
import domain.ContactCard.ContactList;

import java.io.IOException;

public class GetContactListHTML implements Helper<ContactList> {
    public static final String NAME = "getContactListHTML";
    public static final Helper<ContactList> INSTANCE = new GetContactListHTML();

    @Override
    public String apply(ContactList s, Options options) throws IOException {
        return s.toHTML();
    }
}
