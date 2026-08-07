@import org.example.hexlet.dto.users.UserPage
@param UserPage page

@template.layout.page(
content = @`
    <h3>${page.getUser().getName()}</h3>
    <p>${page.getUser().getEmail()}</p>
`
,
footer = @`
<%-- Содержимое нижнего колонтитула --%>
    <span>Все права защищены. ©</span>
`
,
title = @`${page.getUser().getName()}`
)
