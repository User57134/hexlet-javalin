@import org.example.hexlet.util.NamedRoutes
@import org.example.hexlet.dto.courses.CoursePage
@param CoursePage page

@template.layout.page(
content = @`
<%-- Содержимое страницы --%>
    <h2>Курс</h2>

    <section>
        <h1>${page.getCourse().getName()}</h1>
        <p>${page.getCourse().getDescription()}</p>

        <p>
            <a href="${NamedRoutes.courseEditPath(page.getCourse().getId())}">Редактировать данные</a>
        </p>

        <form action="${NamedRoutes.coursePath(page.getCourse().getId())}" method="post">
            <input type="hidden" name="_method" value="DELETE">
            <input type="submit" value="Удалить курс" />
        </form>
    </section>
`
,
footer = @`
<%-- Содержимое нижнего колонтитула --%>
    <span>Все права защищены. ©</span>
`
,
title = @`
<%-- Содержимое заголовка страницы --%>
    Курс
`
)