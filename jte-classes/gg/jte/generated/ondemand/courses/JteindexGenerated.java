package gg.jte.generated.ondemand.courses;
import org.example.hexlet.dto.courses.CoursesPage;
public final class JteindexGenerated {
	public static final String JTE_NAME = "courses/index.jte";
	public static final int[] JTE_LINE_INFO = {0,0,1,1,1,9,9,9,9,10,10,12,12,13,13,15,15,15,15,15,15,15,16,16,16,17,17,17,19,19,20,20,22,22,22,1,1,1,1};
	public static void render(gg.jte.html.HtmlTemplateOutput jteOutput, gg.jte.html.HtmlInterceptor jteHtmlInterceptor, CoursesPage page) {
		jteOutput.writeContent("\r\n<html>\r\n    <head>\r\n        <meta charset=\"utf-8\" />\r\n        <title>Хекслет 2026</title>\r\n    </head>\r\n    <body>\r\n        <h1>");
		jteOutput.setContext("h1", null);
		jteOutput.writeUserContent(page.getHeader());
		jteOutput.writeContent("</h1>\r\n        ");
		if (page.getCourses().isEmpty()) {
			jteOutput.writeContent("\r\n            <p>Пока не добавлено ни одного курса</p>\r\n        ");
		} else {
			jteOutput.writeContent("\r\n            ");
			for (var course : page.getCourses()) {
				jteOutput.writeContent("\r\n               <div>\r\n                   <h2><a href=\"/courses/");
				jteOutput.setContext("a", "href");
				jteOutput.writeUserContent(course.getId());
				jteOutput.setContext("a", null);
				jteOutput.writeContent("\">");
				jteOutput.setContext("a", null);
				jteOutput.writeUserContent(course.getName());
				jteOutput.writeContent("</a></h2>\r\n                   <h4>");
				jteOutput.setContext("h4", null);
				jteOutput.writeUserContent(course.getId());
				jteOutput.writeContent("</h4>\r\n                   <p>");
				jteOutput.setContext("p", null);
				jteOutput.writeUserContent(course.getDescription());
				jteOutput.writeContent("</p>\r\n               </div>\r\n            ");
			}
			jteOutput.writeContent("\r\n        ");
		}
		jteOutput.writeContent("\r\n    </body>\r\n</html>");
	}
	public static void renderMap(gg.jte.html.HtmlTemplateOutput jteOutput, gg.jte.html.HtmlInterceptor jteHtmlInterceptor, java.util.Map<String, Object> params) {
		CoursesPage page = (CoursesPage)params.get("page");
		render(jteOutput, jteHtmlInterceptor, page);
	}
}
