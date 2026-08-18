@import org.example.hexlet.util.NamedRoutes
@import org.example.hexlet.dto.BasePage
@import gg.jte.Content


@param Content content
@param Content title = null
@param Content footer = null
@param BasePage page = null

<!doctype html>
<html>
    <head>
        <meta charset="utf-8"/>
        <meta name="viewport" content="width=device-width, initial-scale=1" />
        <link rel="stylesheet" href="https://cdn.jsdelivr.net/npm/bootstrap@4.0.0/dist/css/bootstrap.min.css" integrity="sha384-Gn5384xqQ1aoWXA+058RXPxPg6fy4IWvTNh0E263XmFcJlSAwiGgFAW/dAiS6JXm" crossorigin="anonymous">
        <title>
        @if (title == null)
            Layout Test
        @else
            ${title}
        @endif
        </title>

    </head>
    <body>
        <header>
            <nav>
                <ul>
                    <li>
                        <a href="/"> Главная </a>
                    </li>
                    <li>
                        <a href="${NamedRoutes.coursesPath()}"> Курсы </a>
                    </li>
                    <li>
                        <a href="${NamedRoutes.usersPath()}"> Пользователи </a>
                    </li>
                </ul>
            </nav>
        </header>

        <main>
            <section>
                @if(page != null && page.getFlash() != null)
                    @if  (page.isSuccess())
                        <div class="alert alert-success" role="alert">
                            ${page.getFlash()}
                        </div>
                    @elseif (page.isDanger())
                        <div class="alert alert-danger" role="alert">
                            ${page.getFlash()}
                        </div>
                    @elseif (page.isWarning())
                        <div class="alert alert-warning" role="alert">
                            ${page.getFlash()}
                        </div>
                    @elseif (page.isInfo())
                        <div class="alert alert-info" role="alert">
                            ${page.getFlash()}
                        </div>
                    @endif
                @endif
            </section>

            ${content}
        </main>

        <footer>
            @if (footer != null)
               ${footer}
            @endif
        </footer>
    </body>
</html>





