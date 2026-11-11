package ticket.booking.entities;
import java.util.*;
public class User {
    private String name;
    private String password;
    private String hashpassword;
    private String userId;
    private List<Ticket> ticketsbooked;

    public User(){}

    public User(String name,String hashpassword,String userId,List<Ticket> ticketsbooked)
    {
        this.name = name;
        this.hashpassword = hashpassword;
        this.userId = userId;
        this.ticketsbooked = ticketsbooked;
    }


    public String getName()
    {
        return name;
    }
    public String getHashpassword()
    {
        return hashpassword;
    }
    public void printTickets()
    {
        for(int i=0;i<ticketsbooked.size();i++)
        {
            System.out.println(ticketsbooked.get(i).getTicketInfo());
        }
    }
    public String getUserId()
    {
        return userId;
    }
    public void setName(String name)
    {
        this.name = name;
    }
    public void setHashpassword(String hashpassword)
    {
        this.hashpassword = hashpassword;
    }
    public void setUserId(String userId)
    {
        this.userId = userId;
    }
    public void setTicketsbooked(List<Ticket> ticketsbooked)
    {
        this.ticketsbooked = ticketsbooked;
    }

    public List<Ticket> getTicketsbooked() {
        return ticketsbooked;
    }
}
