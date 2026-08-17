@import org.example.hexlet.dto.MainPage
@param MainPage  page

@template.layout.page(
content = @`
    <section>
        <h1 class="text-body-emphasis">Привет, Хекслет!</h1>

        @if(!page.isVisited())
            Это сообщение показывается только один раз. Если вы хотите увидеть его снова, сотрите куки
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
