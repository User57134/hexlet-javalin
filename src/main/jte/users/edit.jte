@import org.example.hexlet.NamedRoutes
@import org.example.hexlet.dto.users.BuildUserPage
@param BuildUserPage page

<html>
<head>
    <meta charset="utf-8">
    <title>Изменение данных пользователя</title>
</head>

<body>
<main>
    <!-- Вывод сообщения об ошибках введенных данных -->
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

    <h3>${page.getName()}</h3>

    <form action="${NamedRoutes.usersPath()}" method="post">
        <fieldset>
            <legend>Данные пользователя</legend>
            <label for="1">Имя</label>
            <input id="1" type="text" name="name" value="${page.getName()}" required />
            <label for="2">Электронная почта</label>
            <input id="2" type="email" name="email" value="${page.getEmail()}" required />
            <label for="3">Пароль</label>
            <input id="3" type="password" name="password" required />
            <label for="4">Подтверждение пароля</label>
            <input id="4" type="password" name="passwordConfirmation" required />
            <input type="submit" value="Добавить" />
        </fieldset>
    </form>
</main>
</body>
</html>