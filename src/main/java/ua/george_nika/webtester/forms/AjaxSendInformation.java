package ua.george_nika.webtester.forms;

import java.util.List;

/**
 * Created by George on 24.07.2015.
 */
public class AjaxSendInformation {

    int page;
    String sort;
    String idLike;
    String nameLike;
    List<? extends Object> entityList;

    public AjaxSendInformation() {
    }

    public int getPage() {
        return page;
    }

    public void setPage(int page) {
        this.page = page;
    }

    public String getSort() {
        return sort;
    }

    public void setSort(String sort) {
        this.sort = sort;
    }

    public String getIdLike() {
        return idLike;
    }

    public void setIdLike(String idLike) {
        this.idLike = idLike;
    }

    public String getNameLike() {
        return nameLike;
    }

    public void setNameLike(String nameLike) {
        this.nameLike = nameLike;
    }

    public List<? extends Object> getEntityList() {
        return entityList;
    }

    public void setEntityList(List<? extends Object> entityList) {
        this.entityList = entityList;
    }
}
