package utils.HandlebarsUtils;

import com.github.jknack.handlebars.Handlebars;
import com.github.jknack.handlebars.io.ClassPathTemplateLoader;
import utils.HandlebarsUtils.Helpers.GetContactListHTML;

import java.io.IOException;
import java.util.Map;

public class HandlebarsHelper {
    public static final Handlebars handlebars = new Handlebars(
            new ClassPathTemplateLoader("/templates", ".hbs")
    );

    public static void init() {
        handlebars.registerHelper(GetContactListHTML.NAME, GetContactListHTML.INSTANCE);
    }

    public static String compile(Map<String, Object> model, String templatePath) throws IOException {
        return handlebars.compile(templatePath).apply(model);
    }
}
