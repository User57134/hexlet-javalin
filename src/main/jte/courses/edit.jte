@import org.example.hexlet.NamedRoutes
@import org.example.hexlet.dto.courses.EditCoursePage
@param EditCoursePage page

<html>
<head>
    <meta charset="utf-8">
    <title>Изменение данных курса</title>
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

    <form action="${NamedRoutes.coursePath(page.getId())}" method="POST">
        <fieldset>
            <legend>Данные пользователя</legend>
            <input type="hidden" name="_method" value="PATCH">
            <label for="1">Название курса</label>
            <input id="1" type="text" name="name" value="${page.getName()}" required />
            <label for="2">Описание</label>
            <textarea id="2" rows="5" cols="30" style="resize:none" name="description">${page.getDescription()}</textarea>
            <input type="submit" value="Сохранить" />
        </fieldset>
    </form>
</main>
</body>
</html>