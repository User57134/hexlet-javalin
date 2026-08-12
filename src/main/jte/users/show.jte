@import org.example.hexlet.NamedRoutes
@import org.example.hexlet.dto.users.UserPage
@param UserPage page

@template.layout.page(
content = @`
    <h3>${page.getUser().getName()}</h3>
    <p>${page.getUser().getEmail()}</p>
    <p>
        <a href="${NamedRoutes.userEditPath(page.getUser().getId())}">Редактировать данные</a>
    </p>

    <form action="${NamedRoutes.userPath(page.getUser().getId())}" method="post">
        <input type="hidden" name="_method" value="DELETE">
        <input type="submit" value="Удалить пользователя" />
    </form>
`
,
footer = @`
<%-- Содержимое нижнего колонтитула --%>
    <span>Все права защищены. ©</span>
`
,
title = @`${page.getUser().getName()}`
)
