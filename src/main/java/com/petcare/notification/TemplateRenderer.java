package com.petcare.notification;

import com.petcare.enums.EmailTemplate;
import com.petcare.utils.constants.*;
import org.springframework.stereotype.Component;
import org.thymeleaf.TemplateEngine;
import org.thymeleaf.context.Context;
import org.thymeleaf.spring6.SpringTemplateEngine;
import org.thymeleaf.templateresolver.ClassLoaderTemplateResolver;

import java.nio.charset.StandardCharsets;
import java.util.Locale;
import java.util.Map;

/**
 * Componente que gestiona la carga y renderizado de plantillas HTML de correo usando Thymeleaf.
 * Configura la carpeta de plantillas, codificación y modo de procesamiento.
 */
@Component
public class TemplateRenderer {

    private final TemplateEngine templateEngine;

    public TemplateRenderer() {
        ClassLoaderTemplateResolver resolver = new ClassLoaderTemplateResolver();
        resolver.setPrefix(EmailConstants.TEMPLATE_FOLDER);
        resolver.setSuffix(EmailConstants.TEMPLATE_SUFFIX);
        resolver.setTemplateMode(EmailConstants.TEMPLATE_MODE);
        resolver.setCharacterEncoding(StandardCharsets.UTF_8.name());
        resolver.setCheckExistence(true); // Evita fallos silenciosos si la plantilla no existe

        this.templateEngine = new SpringTemplateEngine();
        this.templateEngine.setTemplateResolver(resolver);
    }

    /**
     * Renderiza una plantilla de correo a partir de un Enum {@link EmailTemplate} y un mapa de variables.
     *
     * @param template  plantilla definida en el enum EmailTemplate
     * @param variables mapa clave-valor con los datos que serán reemplazados en el HTML
     * @return el HTML renderizado como String
     */
    public String render(EmailTemplate template, Map<String, Object> variables) {
        Context context = new Context(Locale.getDefault());
        context.setVariables(variables);
        return templateEngine.process(template.getFileName(), context);
    }
}