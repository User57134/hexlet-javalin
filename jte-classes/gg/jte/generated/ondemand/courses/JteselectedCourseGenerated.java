package gg.jte.generated.ondemand.courses;
import org.example.hexlet.dto.courses.CoursePage;
public final class JteselectedCourseGenerated {
	public static final String JTE_NAME = "courses/selectedCourse.jte";
	public static final int[] JTE_LINE_INFO = {0,0,1,1,1,3,3,9,9,10,15,15,15,16,16,16,19,19,19,19,20,22,22,22,22,23,25,25,25,25,25,1,1,1,1};
	public static void render(gg.jte.html.HtmlTemplateOutput jteOutput, gg.jte.html.HtmlInterceptor jteHtmlInterceptor, CoursePage page) {
		jteOutput.writeContent("\r\n");
		gg.jte.generated.ondemand.layout.JtepageGenerated.render(jteOutput, jteHtmlInterceptor, new gg.jte.html.HtmlContent() {
			public void writeTo(gg.jte.html.HtmlTemplateOutput jteOutput) {
				jteOutput.writeContent("\r\n");
				jteOutput.writeContent("\r\n    <h2>Курс</h2>\r\n\r\n    <main>\r\n        <section>\r\n            <h1>");
				jteOutput.setContext("h1", null);
				jteOutput.writeUserContent(page.getCourse().getName());
				jteOutput.writeContent("</h1>\r\n            <p>");
				jteOutput.setContext("p", null);
				jteOutput.writeUserContent(page.getCourse().getDescription());
				jteOutput.writeContent("</p>\r\n        </section>\r\n    </main>\r\n");
			}
		}, new gg.jte.html.HtmlContent() {
			public void writeTo(gg.jte.html.HtmlTemplateOutput jteOutput) {
				jteOutput.writeContent("\r\n");
				jteOutput.writeContent("\r\n    Курс\r\n");
			}
		}, new gg.jte.html.HtmlContent() {
			public void writeTo(gg.jte.html.HtmlTemplateOutput jteOutput) {
				jteOutput.writeContent("\r\n");
				jteOutput.writeContent("\r\n    <span>Все права защищены. ©</span>\r\n");
			}
		});
	}
	public static void renderMap(gg.jte.html.HtmlTemplateOutput jteOutput, gg.jte.html.HtmlInterceptor jteHtmlInterceptor, java.util.Map<String, Object> params) {
		CoursePage page = (CoursePage)params.get("page");
		render(jteOutput, jteHtmlInterceptor, page);
	}
}
