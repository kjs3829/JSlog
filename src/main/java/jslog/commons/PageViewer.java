package jslog.commons;

import jslog.post.SearchCondition;
import jslog.post.domain.Post;
import lombok.Getter;
import org.springframework.data.domain.Page;

import java.util.ArrayList;
import java.util.List;

@Getter
public class PageViewer {
    int pageSize;
    int startPage;
    int endPage;
    int currentPage;
    int totalPages;
    List posts;
    List<Integer> pageNumbers;

    public PageViewer(Page page, int pageSize) {
        this.pageSize = pageSize;
        startPage = Math.max(page.getNumber() - (this.pageSize -1)/2 + 1, 1);
        endPage = Math.min(startPage + pageSize - 1,page.getTotalPages());
        currentPage = page.getNumber()+1;
        totalPages = page.getTotalPages();
        posts = page.toList();
        List<Integer> pageNumbers = new ArrayList<>();
        for (int i=startPage; i<=endPage; i++)
            pageNumbers.add(i);
        this.pageNumbers = pageNumbers;
    }

    public boolean isFirstPage() {
        return currentPage == 1;
    }

    public boolean isLastPage() {
        return currentPage == Math.max(totalPages,1);
    }

    public boolean isNeedButtonToFirst() {
        return startPage != 1;
    }

    public boolean isNeedButtonToLast() {
        return endPage != totalPages;
    }
}
