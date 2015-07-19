package ua.george_nika.webtester.controllers;

import org.springframework.stereotype.Component;
import org.springframework.ui.Model;
import ua.george_nika.webtester.services.AbstractSortAndRestrictService;
import ua.george_nika.webtester.util.WebTesterConstants;

import javax.servlet.http.HttpSession;

@Component
public class SortRestrictUtil {

    private AbstractSortAndRestrictService service;
    private String sessionPageProperty;

    public AbstractSortAndRestrictService getService() {
        return service;
    }

    public void setService(AbstractSortAndRestrictService service) {
        this.service = service;
    }

    public String getSessionPageProperty() {
        return sessionPageProperty;
    }

    public void setSessionPageProperty(String sessionPageProperty) {
        this.sessionPageProperty = sessionPageProperty;
    }

    protected void executeSortBlock(String sort) {
        if (sort != null) {
            if (sort.equals("id")) {
                getService().nextIdSort();
            } else if (sort.equals("name")) {
                getService().nextNameSort();
            } else if (sort.equals("clear")) {
                getService().clearAllSort();
            }
        }
    }

    protected void executeLikeBlock(Model model, String idLike, String nameLike) {
        if (idLike != null) {
            if (idLike.equals("")) {
                getService().deleteIdLikeRestriction();
            } else {
                getService().addIdLikeRestriction(idLike);
            }
        }
        if (nameLike != null) {
            if (nameLike.equals("")) {
                getService().deleteNameLikeRestriction();
            } else {
                getService().addNameLikeRestriction(nameLike);
            }
        }

        model.addAttribute("idLikeValue", getService().getIdLikeRestriction());
        model.addAttribute("nameLikeValue", getService().getNameLikeRestriction());
    }

    protected int getShowPage(HttpSession session, String pageDirection) {
        int resultPage = 0;
        int prevPage;
        if (session.getAttribute(getSessionPageProperty()) == null) {
            prevPage = 1;
        } else {
            prevPage = (Integer) session.getAttribute(getSessionPageProperty());
        }
        if (pageDirection != null) {
            if (pageDirection.equals("start")) {
                resultPage = 1;
            } else if (pageDirection.equals("end")) {
                resultPage = (getService().getCountRecordsFilteredAndSortedList() - 1) / WebTesterConstants.ROW_ON_PAGE;
                resultPage = resultPage + 1;
            } else if (pageDirection.equals("prev")) {
                resultPage = prevPage - 1;
            } else if (pageDirection.equals("next")) {
                resultPage = prevPage + 1;
            }
        }
        if (resultPage < 1) {
            resultPage = 1;
        }
        return resultPage;
    }

    protected void setShowPageToSession(HttpSession session, int showPage) {
        session.setAttribute(getSessionPageProperty(), showPage);
    }
}
