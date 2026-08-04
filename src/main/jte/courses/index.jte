@import org.example.hexlet.dto.courses.CoursesPage
@param CoursesPage page


@template.layout.page(
content = @`
    <h1>${page.getHeader()}</h1>

    <main>
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
    </main>
`
)