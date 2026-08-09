@import org.example.hexlet.NamedRoutes
@import org.example.hexlet.dto.users.UsersPage
@param UsersPage page

@template.layout.page(
content = @`
<%-- Содержимое страницы --%>
    <h1>Пользователи</h1>

    <section>
        @if (page.getUsers().isEmpty())
            <p>Пока нет ни одного пользователя</p>
        @else
            <table>
                <tr>
                    <th>№</th>
                    <th>Имя</th>
                    <th>Почта</th>
                </tr>
                @for (var user : page.getUsers())
                  <tr>
                      <td>
                          ${user.getId()}
                      </td>
                      <td>
                          <a href="${NamedRoutes.userPath(user.getId())}">${user.getName()}</a>
                      </td>
                      <td>
                          ${user.getEmail()}
                      </td>
                  </tr>
                @endfor
            </table>
        @endif
    </section>

    <section>
        <h4>
            <a href="${NamedRoutes.buildUserPath()}">Добавить пользователя</a>
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
    Пользователи
`
)