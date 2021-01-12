Search results for key <b>${key}</b>
<#if notes?has_content>
Found ${total} news notes. Page ${page}/${total_pages}

<#list notes as note>
<a href="${note.url}">${note.title}</a>
${note.source} / ${note.date?date} / ${note.region} / ${note.common}

</#list>
<#else>
<b>No results</b>
</#if>

