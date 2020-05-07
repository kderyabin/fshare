package com.kderyabin.model;

import java.math.BigDecimal;

public interface IBoardPersonTotal {
    BigDecimal getTotal();

    void setTotal(BigDecimal total);

    Integer getPersonId();

    void setPersonId(Integer personId);

    Integer getBoardId();

    void setBoardId(Integer boardId);

    String getPersonName();

    void setPersonName(String personName);
}
