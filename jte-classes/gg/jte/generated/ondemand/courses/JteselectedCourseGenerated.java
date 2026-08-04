package gg.jte.generated.ondemand.courses;
import org.example.hexlet.dto.courses.CoursePage;
public final class JteselectedCourseGenerated {
	public static final String JTE_NAME = "courses/selectedCourse.jte";
	public static final int[] JTE_LINE_INFO = {0,0,1,1,1,3,3,5,5,10,10,10,11,11,11,14,14,14,14,14,1,1,1,1};
	public static void render(gg.jte.html.HtmlTemplateOutput jteOutput, gg.jte.html.HtmlInterceptor jteHtmlInterceptor, CoursePage page) {
		jteOutput.writeContent("\r\n");
		gg.jte.generated.ondemand.layout.JtepageGenerated.render(jteOutput, jteHtmlInterceptor, new gg.jte.html.HtmlContent() {
			public void writeTo(gg.jte.html.HtmlTemplateOutput jteOutput) {
				jteOutput.writeContent("\r\n    <h2>Курс</h2>\r\n\r\n    <main>\r\n        <section>\r\n            <h1>");
				jteOutput.setContext("h1", null);
				jteOutput.writeUserContent(page.getCourse().getName());
				jteOutput.writeContent("</h1>\r\n            <p>");
				jteOutput.setContext("p", null);
				jteOutput.writeUserContent(page.getCourse().getDescription());
				jteOutput.writeContent("</p>\r\n        </section>\r\n    </main>\r\n");
			}
		});
	}
	public static void renderMap(gg.jte.html.HtmlTemplateOutput jteOutput, gg.jte.html.HtmlInterceptor jteHtmlInterceptor, java.util.Map<String, Object> params) {
		CoursePage page = (CoursePage)params.get("page");
		render(jteOutput, jteHtmlInterceptor, page);
	}
}
