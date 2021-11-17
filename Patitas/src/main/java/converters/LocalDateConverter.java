package converters;

import javax.persistence.AttributeConverter;
import javax.persistence.Converter;
import java.time.LocalDate;

@Converter
public class LocalDateConverter implements AttributeConverter<LocalDate, String> {

    @Override
    public String convertToDatabaseColumn(LocalDate localDate) {
        return localDate.toString();
    }

    @Override
    public LocalDate convertToEntityAttribute(String s) {
        String[] cosas = s.split("-");
        return LocalDate.of(
                Integer.parseInt(cosas[0]), Integer.parseInt(cosas[1]), Integer.parseInt(cosas[2])
        );
    }
}
