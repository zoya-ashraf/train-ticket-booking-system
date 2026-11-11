package ticket.booking.services;
import ticket.booking.entities.*;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import ticket.booking.Util.UserServiceUtil;
import ticket.booking.entities.User;
import ticket.booking.services.TrainService;
import java.io.File;
import java.io.IOException;
import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

public class BookingService {
    private User user;
    private List<User> userList;
    private final String users_path ="localDB/users.json";
    private ObjectMapper objectmapper = new ObjectMapper();

    public BookingService() throws IOException
    {
        userList=loadusers();
    }

    public BookingService(User user) throws IOException
    {
        this.user=user;
        userList=loadusers();
    }

    public List<User> loadusers() throws IOException{
        File users = new File(users_path);
        return objectmapper.readValue(users, new TypeReference<List<User>>(){});
    }

    public boolean login(User u,String pswd)
    {
        Optional<User> userFound = userList.stream().filter(user1 -> {
            return user1.getName().equals(u.getName()) && UserServiceUtil.checkPassword(pswd,user1.getHashpassword());}).findFirst();
        if(userFound.isPresent())
        {
            user = userFound.get();
        }
        return userFound.isPresent();
    }


    public boolean signUp(User user1)
    {
        try{
            userList.add(user1);
            saveUserListToFile();
            return Boolean.TRUE;
        } catch (IOException ex) {
            return Boolean.FALSE;
        }
    }

    public void saveUserListToFile() throws IOException
    {
        File usersFile = new File(users_path);
        objectmapper.writeValue(usersFile,userList);
    }

    public void fetchBooking()
    {
        user.printTickets();
    }

    public boolean cancelBooking(String ticketId) throws IOException
    {
       if(ticketId==null|| ticketId.isEmpty())
       {
           System.out.println("Ticket can not be null.");
           return Boolean.FALSE;
       }
       boolean isremoved = user.getTicketsbooked().removeIf(ticket -> ticket.getTicketId().equals(ticketId));
       if(isremoved)
       {
           saveUserListToFile();
           System.out.println("Ticket removed with ticket Id : "+ticketId);
           return true;
       }
       else
       {
           System.out.println("Ticket not found.");
           return false;
       }
    }

    public List<Train> getTrains(String source,String destination)
    {
        try {
            TrainService ts = new TrainService();
            return ts.searchTrains(source.trim(),destination.trim());
        }
        catch (Exception e)
        {
            System.out.println("Unexpected error occured.."+e);
            return new ArrayList<>();
        }
    }
    public boolean checkuser()
    {
        return user==null;
    }

    public String buildTicket(String trainNo,int row,int col,String dateStr) throws IOException {
        String ticketId = UUID.randomUUID().toString();
        Ticket t = new Ticket(ticketId,user.getUserId(),"","",dateStr,trainNo,row,col);
        user.getTicketsbooked().add(t);
        saveUserListToFile();
        return ticketId;
    }

    public List<Ticket> loadtickets()
    {
        return user.getTicketsbooked();
    }
}
