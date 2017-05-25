package com.example.alexconstantin.jrs;

/**
 * Created by Gabriel on 08.04.2017.
 */
import java.util.List;

public class RequestObiective {
    public Integer languageId;
    public Double xLeftTop;
    public Double yLeftTop;
    public Double xRightBottom;
    public Double yRightBottom;
    public List<Integer> filteredTypes;

    public Double getxLeftTop() {
        return xLeftTop;
    }

    public void setxLeftTop(Double xLeftTop) {
        this.xLeftTop = xLeftTop;
    }

    public Double getyLeftTop() {
        return yLeftTop;
    }

    public void setyLeftTop(Double yLeftTop) {
        this.yLeftTop = yLeftTop;
    }

    public Double getxRightBottom() {
        return xRightBottom;
    }

    public void setxRightBottom(Double xRightBottom) {
        this.xRightBottom = xRightBottom;
    }

    public Double getyRightBottom() {
        return yRightBottom;
    }

    public void setyRightBottom(Double yRightBottom) {
        this.yRightBottom = yRightBottom;
    }

    public List<Integer> getFilteredTypes() {
        return filteredTypes;
    }

    public void setFilteredTypes(List<Integer> filteredTypes) {
        this.filteredTypes = filteredTypes;
    }
    public Integer getLanguageId() {
        return languageId;
    }

    public void setLanguageId(Integer languageId) {
        this.languageId = languageId;
    }

}


