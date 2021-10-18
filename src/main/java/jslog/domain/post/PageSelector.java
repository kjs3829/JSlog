package jslog.domain.post;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import java.util.ArrayList;
import java.util.List;

@Getter @Setter @ToString
public class PageSelector {
    private int currentPage;
    private int prevPage;
    private int nextPage;
    private int maxPage;
    private int pageListStart;
    private int pageListEnd;
    private boolean prev;
    private boolean next;
    private boolean toFirstPageButton;
    private boolean toEndPageButton;
    private List<Integer> pageList;

    public PageSelector(int currentPage, int maxPage) {
        this.currentPage = currentPage;
        this.prevPage = currentPage - 1;
        this.nextPage = currentPage + 1;
        this.maxPage = maxPage;
        this.pageListStart = (currentPage > 5) ? currentPage - 4 : 1;
        this.pageListEnd = (currentPage <= maxPage - 5) ? currentPage + 4 : maxPage;
        this.prev = currentPage > 1;
        this.next = currentPage < maxPage;
        this.toFirstPageButton = this.pageListStart != 1;
        this.toEndPageButton = this.pageListEnd != maxPage;
        this.pageList = makePageList();
    }

    private List<Integer> makePageList() {
        List<Integer> list = new ArrayList<>();
        for (int i = pageListStart; i <= pageListEnd; i++) {
            list.add(i);
        }
        return list;
    }
}
