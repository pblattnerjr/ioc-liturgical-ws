package ioc.liturgical.ws.forms.manager;

import j2html.tags.ContainerTag;
import static j2html.TagCreator.*;

public class FormsManager {
	public static String docEditor() {
		ContainerTag page = 
				html().with(
					    head().with(
					        title("IOC Liturgical Web Services - Simple Doc Editor"),
					        link().withRel("stylesheet").withHref("/css/main.css")
					    ),
					    body().with(
					        main().with(
					            h1("Heading!")
					        )
					    )
					)				
				;
		return page.render();
	}
}
