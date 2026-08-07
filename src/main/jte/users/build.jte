<html>
  <head>
      <meta charset="utf-8">
      <title>Новый пользователь</title>
  </head>

  <body>
    <main>
        <h3>Добавить нового пользователя</h3>

        <form action="/users" method="post">
           <fieldset>
               <legend>Данные пользователя</legend>
               <label for="1">Имя</label>
               <input id="1" type="text" name="name" required />
               <label for="2">Электронная почта</label>
               <input id="2" type="email" name="email" required />
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