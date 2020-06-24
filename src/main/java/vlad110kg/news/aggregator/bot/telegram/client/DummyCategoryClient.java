package vlad110kg.news.aggregator.bot.telegram.client;

import com.google.common.collect.ImmutableList;
import org.springframework.stereotype.Service;
import vlad110kg.news.aggregator.bot.telegram.domain.Category;
import vlad110kg.news.aggregator.bot.telegram.domain.ListCategoryResponse;

@Service
public class DummyCategoryClient implements CategoryClient {

    @Override
    public ListCategoryResponse list(int page, int pageSize) {
        ImmutableList.Builder<Category> builder = ImmutableList.builder();
        for (int i = 0; i < 10; i++) {
            Category category = new Category();
            category.setId(i);
            category.setName("dummy" + i);
            category.setLocalised("Dummy" + i);
            builder.add(category);
        }

        int parentI = 51;
        Category parent = new Category();
        parent.setId(parentI);
        parent.setName("parentdummy" + parentI);
        parent.setLocalised("Parent" + parentI);
        builder.add(parent);

        ImmutableList.Builder<Category> subBuilder = ImmutableList.builder();
        for (int i = 100; i < 110; i++) {
            Category category = new Category();
            category.setId(i);
            category.setName("subdummy" + i);
            category.setLocalised("SubDummy" + i);
            category.setParent(parent);
            subBuilder.add(category);
        }
        parent.setChildren(subBuilder.build());
        ListCategoryResponse response = new ListCategoryResponse();
        response.setCategories(builder.build());
        return response;
    }

    @Override
    public ListCategoryResponse list(long parentId, int page, int pageSize) {
        return null;
    }

    @Override
    public Category find(long categoryId) {
        Category category = new Category();
        category.setId(categoryId);
        category.setName("dummy");
        category.setLocalised("dummy");
        return category;
    }

    @Override
    public Category subscribe(long readerChatId, long categoryId) {
        Category category = new Category();
        category.setId(categoryId);
        category.setName("dummy");
        category.setLocalised("dummy");
        return category;
    }
}
