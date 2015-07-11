package ua.george_nika.webtester.services;

import ua.george_nika.webtester.dao.intface.AbstractDao;
import ua.george_nika.webtester.dao.util.LimitedSortAndRestriction;

public abstract class AbstractSortAndRestrictService {

    protected LimitedSortAndRestriction limitedSortAndRestrict = new LimitedSortAndRestriction();

    abstract AbstractDao getDao();

    public void clearAllSort(){
        limitedSortAndRestrict.clearAllSort();
    }

    public void nextIdSort(){
        limitedSortAndRestrict.nextIdSort(this.getClass());
    }

    public void nextNameSort(){
        limitedSortAndRestrict.nextNameSort(this.getClass());
    }

    public void addIdLikeRestriction(String likeRestriction){
        limitedSortAndRestrict.addIdLikeRestriction(this.getClass(), likeRestriction);
    }

    public void addNameLikeRestriction(String likeRestriction){
        limitedSortAndRestrict.addNameLikeRestriction(this.getClass(), likeRestriction);
    }

    public void deleteIdLikeRestriction(){
        limitedSortAndRestrict.deleteIdLikeRestriction(this.getClass());
    }

    public void deleteNameLikeRestriction(){
        limitedSortAndRestrict.deleteNameLikeRestriction(this.getClass());
    }

    public String getIdLikeRestriction(){
        return limitedSortAndRestrict.getIdLikeRestriction(this.getClass());
    }

    public String getNameLikeRestriction(){
        return limitedSortAndRestrict.getNameLikeRestriction(this.getClass());
    }

    public int getCountRecordsFilteredAndSortedList(){
        return getDao().getCountRecordsFilteredAndSortedList(limitedSortAndRestrict);
    }
}
