package converters;

import domain.ContactCard.ContactList;

import javax.persistence.AttributeConverter;
import javax.persistence.Converter;

@Converter
public class ContactListAttributeConverter implements AttributeConverter<ContactList, String> {
    @Override
    public String convertToDatabaseColumn(ContactList list) {
        return list.getStr();
    }

    @Override
    public ContactList convertToEntityAttribute(String s) {
        ContactList ret = new ContactList();
        ret.addFromStr(s);
        return ret;
    }
}
