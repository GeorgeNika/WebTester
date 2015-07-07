package ua.george_nika.webtester.dao.util;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by George on 05.06.2015.
 */
public class SortAndRestrictForEntity {

    private Map<String,String> likeRestrictionForEntity = new HashMap<String, String>();
    private Map<String,MySortDirection> sortForEntity = new HashMap<String, MySortDirection>();
    private Map<String,Object> equalRestrictionForEntity = new HashMap<String, Object>();

    public Map<String, String> getLikeRestrictionForEntity() {
        return likeRestrictionForEntity;
    }

    public void setLikeRestrictionForEntity(Map<String, String> likeRestrictionForEntity) {
        this.likeRestrictionForEntity = likeRestrictionForEntity;
    }

    public Map<String, MySortDirection> getSortForEntity() {
        return sortForEntity;
    }

    public void setSortForEntity(Map<String, MySortDirection> sortForEntity) {
        this.sortForEntity = sortForEntity;
    }

    public Map<String, Object> getEqualRestrictionForEntity() {
        return equalRestrictionForEntity;
    }

    public void setEqualRestrictionForEntity(Map<String, Object> equalRestrictionForEntity) {
        this.equalRestrictionForEntity = equalRestrictionForEntity;
    }

    public void clearAllLikeRestriction(){
        likeRestrictionForEntity.clear();
    }

    public void addLikeRestriction(String field, String value){
        likeRestrictionForEntity.put(field, value);
    }

    public void deleteLikeRestriction(String field){
        likeRestrictionForEntity.remove(field);
    }

    public void setSort(String field){
        sortForEntity.put(field, MySortDirection.ASCENDING);
    }

    public void nextSort(String field){
        if (sortForEntity.containsKey(field)) {
            MySortDirection tempDirection = sortForEntity.get(field);
            tempDirection = tempDirection.getNextSort();
            if (tempDirection == MySortDirection.NOSORT) {
                sortForEntity.remove(field);
            } else {
                sortForEntity.put(field,tempDirection);
            }
        } else {
            sortForEntity.put(field,MySortDirection.ASCENDING);
        }
    }

    public void clearAllSort(){
        sortForEntity.clear();
    }

    public void deleteSort(String field){
        sortForEntity.remove(field);
    }

    public void clearAllEqualRestriction(){
        equalRestrictionForEntity.clear();
    }

    public void addEqualRestriction(String field, Object value){
        equalRestrictionForEntity.put(field, value);
    }

    public void deleteEqualRestriction(String field){
        equalRestrictionForEntity.remove(field);
    }
}
