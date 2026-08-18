@import org.example.hexlet.dto.courses.BuildCoursePage
@import org.example.hexlet.util.NamedRoutes


@param BuildCoursePage page

@template.layout.page(
page = page,

title = @`Новый курс`,

footer = @`
<%-- Содержимое нижнего колонтитула --%>
    <span>Все права защищены. ©</span>
`,

content = @`
    <section>
        @if(page.getErrors() != null)
            <section>
                <ul>
                    @for(var validatorErrors : page.getErrors().values())
                        @for(var error : validatorErrors)
                            <li>${error.getMessage()}</li>
                        @endfor
                    @endfor
                </ul>
            </section>
        @endif
    </section>

    <section>

        <h3>Добавить новый курс</h3>

        <form action="${NamedRoutes.coursesPath()}" method="post">
            <div>
                <label>
                    Название
                    <input type="text" name="name" value="${page.getName()}"/>
                </label>
            </div>
            <div>
                <label>
                    Описание
                    <input type="text" name="description" value="${page.getDescription()}"/>
                </label>
            </div>
            <input type="submit" value="Добавить"/>
        </form>
    </section>
`)