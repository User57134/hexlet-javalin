package gg.jte.generated.ondemand.courses;
import org.example.hexlet.dto.courses.CoursePage;
public final class JteselectedCourseGenerated {
	public static final String JTE_NAME = "courses/selectedCourse.jte";
	public static final int[] JTE_LINE_INFO = {0,0,1,1,1,3,3,14,14,14,15,15,15,19,19,19,1,1,1,1};
	public static void render(gg.jte.html.HtmlTemplateOutput jteOutput, gg.jte.html.HtmlInterceptor jteHtmlInterceptor, CoursePage page) {
		jteOutput.writeContent("\r\n");
		jteOutput.writeContent("\r\n<!doctype html>\r\n<html lang=\"en\">\r\n    <head>\r\n      <meta charset=\"utf-8\" />\r\n      <meta name=\"viewport\" content=\"width=device-width, initial-scale=1\" />\r\n      <title>Курс</title>\r\n    </head>\r\n    <body>\r\n      <div class=\"col-lg-8 mx-auto p-4 py-md-5\">\r\n        <main>\r\n            <h1>");
		jteOutput.setContext("h1", null);
		jteOutput.writeUserContent(page.getCourse().getName());
		jteOutput.writeContent("</h1>\r\n            <p>");
		jteOutput.setContext("p", null);
		jteOutput.writeUserContent(page.getCourse().getDescription());
		jteOutput.writeContent("</p>\r\n        </main>\r\n      </div>\r\n    </body>\r\n</html>");
	}
	public static void renderMap(gg.jte.html.HtmlTemplateOutput jteOutput, gg.jte.html.HtmlInterceptor jteHtmlInterceptor, java.util.Map<String, Object> params) {
		CoursePage page = (CoursePage)params.get("page");
		render(jteOutput, jteHtmlInterceptor, page);
	}
}
