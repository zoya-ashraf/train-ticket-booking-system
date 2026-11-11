package ticket.booking.entities;

import com.fasterxml.jackson.annotation.JsonIgnore;

import java.util.Date;

public class Ticket {
    private String ticketId;
    private String userId;
    private String source;
    private String destination;
    private String dateOfTravel;
    private String trainNo;
    private int row,col;

    public Ticket(){}

    public Ticket( String ticketId,String userId,String source,String destination,String dateOfTravel,
    String trainNo,int row,int col) {
        this.ticketId = ticketId;
        this.userId = userId;
        this.source = source;
        this.destination = destination;
        this.dateOfTravel = dateOfTravel;
        this.trainNo = trainNo;
        this.row = row;
        this.col = col;
    }
    @JsonIgnore
    public String getTicketInfo()
    {
        return String.format("Ticket Id : %s of Train No : %s belongs to user %s going from station %s to station %s on %s seat is row %s and column is %s.",
                ticketId,trainNo,userId,source,destination,dateOfTravel,row,col);
    }

    public String getTicketId() {
        return ticketId;
    }

    public void setTicketId(String ticketId) {
        this.ticketId = ticketId;
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public String getSource() {
        return source;
    }

    public void setSource(String source) {
        this.source = source;
    }

    public String getDestination() {
        return destination;
    }

    public void setDestination(String destination) {
        this.destination = destination;
    }

    public String getDateOfTravel() {
        return dateOfTravel;
    }

    public void setDateOfTravel(String dateOfTravel) {
        this.dateOfTravel = dateOfTravel;
    }

    public String getTrainNo() {
        return trainNo;
    }

    public void setTrainNo(String trainNo) {
        this.trainNo = trainNo;
    }

    public int getRow() {
        return row;
    }

    public void setRow(int row) {
        this.row = row;
    }

    public int getCol() {
        return col;
    }

    public void setCol(int col) {
        this.col = col;
    }
}
