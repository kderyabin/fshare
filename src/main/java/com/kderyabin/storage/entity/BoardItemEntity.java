package com.kderyabin.storage.entity;

import com.kderyabin.model.BoardPersonTotal;
import lombok.ToString;

import javax.persistence.*;
import java.math.BigDecimal;
import java.sql.Date;
import java.util.Objects;

@ToString
@Entity
@Table(name = "item")
@NamedQuery(
        name = "BoardItemEntity.findAllByBoardId",
        query = "select i from BoardItemEntity i where board_id = ?1")
@NamedNativeQuery(
        name = "BoardItemEntity.getBoardPersonTotal",
        query = "select " +
                "SUM(i.amount) as total, " +
                "p.id as personId, " +
                "p.name as personName," +
                "i.board_id as boardId  " +
                "from item as i " +
                "left join person AS p on i.person_id = p.id " +
                "where i.board_id = ?1 " +
                "group by i.person_id " +
                "order by personName"
)
public class BoardItemEntity {
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
    private PersonEntity person;

    @ToString.Exclude
    @ManyToOne
    @JoinColumn(name = "board_id", foreignKey = @ForeignKey(name = "fk_board_id"))
    private BoardEntity board;

    public BoardItemEntity() {
    }

    public BoardItemEntity(String title) {
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

    public PersonEntity getPerson() {
        return person;
    }

    public void setPerson(PersonEntity person) {
        this.person = person;
    }

    public BoardEntity getBoard() {
        return board;
    }

    public void setBoard(BoardEntity board) {
        this.board = board;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof BoardItemEntity)) return false;
        BoardItemEntity that = (BoardItemEntity) o;
        return  Objects.equals(id, that.id) &&
                Objects.equals(title, that.title) &&
                Objects.equals(amount, that.amount) &&
                Objects.equals(date, that.date);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, title, amount, date);
    }
}
