package com.kderyabin.models;

import lombok.ToString;

import javax.persistence.*;
import java.math.BigDecimal;
import java.sql.Date;
import java.util.Objects;

@ToString
@Entity
@Table(name = "item")
@NamedQuery(name = "BoardItemModel.findAllByBoardId", query = "select i from BoardItemModel i where board_id = ?1")
public class BoardItemModel {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id", nullable = false)
    private Integer id;

    @Column(name = "title", nullable = false, length = 50)
    private String title;


    @Column(name = "amount", nullable = false)
    private BigDecimal amount;

    @Column(name = "pay_date", nullable = false)
    private Date date = new Date(System.currentTimeMillis());

    @ToString.Exclude
    @ManyToOne
    @JoinColumn(name = "person_id", foreignKey = @ForeignKey(name = "fk_person_id"))
    private PersonModel person;

    @ToString.Exclude
    @ManyToOne
    @JoinColumn(name = "board_id", foreignKey = @ForeignKey(name = "fk_board_id"))
    private BoardModel board;


    public BoardItemModel() {
    }

    public BoardItemModel(String title) {
        this.title = title;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public BigDecimal getAmount() {
        return amount;
    }

    public void setAmount(BigDecimal amount) {
        this.amount = amount;
    }

    public void setAmount(String amount) {
        this.amount = new BigDecimal(amount);
    }

    public Date getDate() {
        return date;
    }

    public void setDate(Date date) {
        this.date = date;
    }

    public PersonModel getPerson() {
        return person;
    }

    public void setPerson(PersonModel person) {
        this.person = person;
    }

    public BoardModel getBoard() {
        return board;
    }

    public void setBoard(BoardModel board) {
        this.board = board;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof BoardItemModel)) return false;
        BoardItemModel that = (BoardItemModel) o;
        return id.equals(that.id) &&
                title.equals(that.title) &&
                amount.equals(that.amount) &&
                date.equals(that.date);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, title, amount, date);
    }
}
