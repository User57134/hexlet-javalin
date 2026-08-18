@import org.example.hexlet.util.NamedRoutes
@import org.example.hexlet.dto.MainPage
@param MainPage  page

@template.layout.page(
content = @`
    <section>
        <h1>Привет, Хекслет!</h1>

        @if(page.getCurrentUser() != null)
            <h3> Добро пожаловать, ${page.getCurrentUser()}.</h3>

            <p> Чтобы разлогиниться, удалите куку JSESSIONID из браузера
                или нажмите кнопку "Выход".
            </p>

            <form action="${NamedRoutes.sessionsPath()}" method="post">
                <input type="hidden" name="_method" value="DELETE">
                <input type="submit" value="Выход"/>
            </form>
        @else
            <h3>
                <a href="/sessions/build">Вход в систему</a>
            </h3>
        @endif

    </section>
`
,
footer = @`
<%-- Содержимое нижнего колонтитула --%>
    <span>Все права защищены. ©</span>
`
,
title = @`Привет, Хекслет`
)
