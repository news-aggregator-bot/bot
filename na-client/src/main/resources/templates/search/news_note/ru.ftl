Результаты поиска по ключу <b>${key}</b>
<#if notes?has_content>
    Найдено ${total} новостей. Стр. ${page}/${total_pages}

    <#list notes as note>
        <a href="${note.url}">${note.title}</a>
        ${note.source} / ${note.date?date} / ${note.region} / ${note.common}

    </#list>
<#else>
    <b>Нет результатов</b>
</#if>

