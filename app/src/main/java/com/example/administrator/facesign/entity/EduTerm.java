package com.example.administrator.facesign.entity;

import java.io.Serializable;
import java.util.Date;

/**
 * Created by Administrator on 2016/11/30.
 */

public class EduTerm implements Serializable {
    private Date startDate;
    private Date endDate;

    public EduTerm(){}

    public EduTerm(Date startDate, Date endDate) {
        super();
        this.startDate = startDate;
        this.endDate = endDate;
    }

    public Date getStartDate() {
        return startDate;
    }

    public void setStartDate(Date startDate) {
        this.startDate = startDate;
    }

    public Date getEndDate() {
        return endDate;
    }

    public void setEndDate(Date endDate) {
        this.endDate = endDate;
    }



}
