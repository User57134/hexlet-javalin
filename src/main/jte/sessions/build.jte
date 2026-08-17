@import org.example.hexlet.NamedRoutes


@template.layout.page(
content = @`
    <section>
        <h3>Вход</h3>

        <form action="${NamedRoutes.sessionsPath()}" method="post">
            <input type="text" placeholder="Nickname" name="nickname"/>
            <input type="password" placeholder="Password" name="password"/>
            <input type="submit" value="Войти"/>
        </form>
    </section>
`,
title = @`Привет, Хекслет`

)