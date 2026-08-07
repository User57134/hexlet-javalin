@import org.example.hexlet.dto.courses.CoursesPage
@param CoursesPage page


@template.layout.page(
content = @`
<%-- Содержимое страницы --%>
    <h1>${page.getHeader()}</h1>

    <form action="/courses" method="get">
        <input type="search" name="term" value="${page.getTerm()}"/>
        <input type="submit" value="Search"/>
    </form>

    <section>
        @if (page.getCourses().isEmpty())
            <p>Пока не добавлено ни одного курса</p>
        @else
            @for(var course : page.getCourses())
                <section>
                    <h2>
                        <a href="/courses/${course.getId()}">${course.getName()}</a>
                    </h2>
                    <p>${course.getDescription()}</p>
                </section>
            @endfor
        @endif
    </section>

    <section>
        <h4>
            <a href="courses/build">Добавить курс</a>
        </h4>
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
    Курсы
`
)