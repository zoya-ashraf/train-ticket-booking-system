package ticket.booking;

import ticket.booking.Util.UserServiceUtil;
import ticket.booking.entities.Ticket;
import ticket.booking.entities.Train;
import ticket.booking.entities.User;
import ticket.booking.services.BookingService;
import ticket.booking.services.TrainService;

import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.*;

public class App {
    public static void main(String[] args) throws IOException {
        System.out.println("Running train booking system...");
        Scanner in = new Scanner(System.in);

        String option = "0";
        BookingService bookingService;
        try {
            bookingService = new BookingService();
        }
        catch (Exception e)
        {
            System.out.println("There is something wrong. "+e);
            return;
        }
        while(Integer.parseInt(option)!=7)
        {
            System.out.println("1. Signup User");
            System.out.println("2. Login User");
            System.out.println("3. FetchBooking");
            System.out.println("4. Search trains");
            System.out.println("5. Book a seat");
            System.out.println("6. Cancel my seat");
            System.out.println("7. Exit");
            option = in.nextLine();
            switch (Integer.parseInt(option))
            {
                case 1:
                    System.out.println("Enter user name");
                    String username = in.nextLine().trim();
                    System.out.println("Enter password");
                    String pswd = in.nextLine().trim();
                    User newUser = new User(username, UserServiceUtil.hashPassword(pswd), UUID.randomUUID().toString(),
                            new ArrayList<>());
                    bookingService.signUp(newUser);
                    break;
                case 2:
                    System.out.println("Enter user name to login");
                    String userName = in.nextLine().trim();
                    System.out.println("Enter password");
                    String pwd = in.nextLine().trim();
                    User userToLogin = new User(userName,UserServiceUtil.hashPassword(pwd), UUID.randomUUID().toString(),
                            new ArrayList<>());

                    if(bookingService.login(userToLogin,pwd))
                        {

                            System.out.println("User found");
                        }
                    else {
                            System.out.println("Please Sign up");
                        }
                    break;
                case 3:
                    if(bookingService.checkuser())
                    {
                        System.out.println("Please login");
                        break;
                    }
                    System.out.println("Fetching your bookings..");
                    bookingService.fetchBooking();
                    break;
                case 4:
                    System.out.println("Searching trains..");
                    System.out.println("Enter source ..");
                    String source = in.nextLine().trim();
                    System.out.println("Enter destination..");
                    String destination = in.nextLine().trim();
                    List<Train> trains = bookingService.getTrains(source,destination);
                    int index = 1;
                    if(trains.isEmpty())
                        System.out.println("No trains found");
                    for(Train t : trains)
                    {
                        System.out.println(t.getTrainInfo(source,destination
                        ));
                    }
                    break;
                case 5:
                    if(bookingService.checkuser())
                    {
                        System.out.println("Please login");
                        break;
                    }
                    System.out.println("Book a seat..");
                    System.out.println("Want to book a seat..");
                    String c = in.nextLine().trim();
                    if(c.equalsIgnoreCase("yes"))
                    {
                        Date date = new Date();
                        SimpleDateFormat date_format1 = new SimpleDateFormat("MM/dd/yyyy");
                        String date_str = date_format1.format(date);
                        System.out.println("Enter train number : ");
                        String trainNo = in.nextLine();
                        TrainService t = new TrainService();
                        List<Train> t1 = t.loadtrains();
                        Train check=null;
                        for(Train train : t1)
                        {
                            if(train.getTrainNo().equals(trainNo))
                            {
                                check = train;
                                break;
                            }
                        }
                        if(check==null) {
                            System.out.println("No train exists");
                            break;
                        }
                        else {
                            System.out.println("Exists");
                            List<List<Integer>> seatGrid = check.getSeats();

                            for (int i = 0; i < seatGrid.size(); i++) {
                                List<Integer> row = seatGrid.get(i);
                                for (int j = 0; j < row.size(); j++) {
                                    int seatValue = row.get(j);
                                    System.out.println("Row " + i + ", Col " + j + " = " + seatValue);
                                }
                            }
                            System.out.println("Enter the row you want to select");
                            String r = in.nextLine();

                            System.out.println("Enter the column you want to select");
                            String cl = in.nextLine();

                            int row;
                            int col;

                            try {
                                row = Integer.parseInt(r);
                                col = Integer.parseInt(cl);
                            } catch (NumberFormatException e) {
                                System.out.println("Please enter valid numbers for row and column.");
                                break;
                            }
                            if (row < 0 || row >= seatGrid.size()) {
                                System.out.println("Invalid row");
                                break;
                            }
                            else if (col < 0 || col >= seatGrid.get(row).size()) {
                                System.out.println("Invalid column");
                                break;
                            }
                            for (int i = 0; i < seatGrid.size(); i++) {
                                List<Integer> rows = seatGrid.get(i);
                                for (int j = 0; j < rows.size(); j++) {
                                    int seatValue = rows.get(j);
                                    if(i==row && j==col)
                                    {
                                        if(seatValue==0) {
                                            System.out.println("Seat available");
                                            System.out.println("Do you want this seat");
                                            String ch = in.nextLine();
                                            if(ch.equalsIgnoreCase("yes"))
                                            {
                                                seatGrid.get(i).set(j,1);
                                                t.saveTrainListToFile();
                                                bookingService.buildTicket(trainNo,row,col,date_str);
                                            }
                                            break;
                                        }
                                        else{
                                            System.out.println("Seat not available");
                                            break;
                                    }

                                }}
                            }
                        }
                    }
                    else {
                        break;
                    }
                    break;
                case 6:
                    if(bookingService.checkuser())
                    {
                        System.out.println("Please login");
                        break;
                    }
                    System.out.println("Cancel Booking");
                    System.out.println("Enter the ticket id ");
                    String ticketId = in.nextLine();
                    List<Ticket> ticket = bookingService.loadtickets();
                    Ticket cancel = null;
                    boolean flag = false;
                    for (Ticket t : ticket)
                    {
                        if(t.getTicketId().equals(ticketId))
                        {
                            System.out.println("Ticket found");
                            flag = true;
                            cancel = t;
                            bookingService.cancelBooking(ticketId);
                            TrainService tt = new TrainService();
                            List<Train> trains1 = tt.loadtrains();
                            for(Train t1 : trains1)
                            {
                                if(t1.getTrainNo().equals(cancel.getTrainNo()))
                                {
                                    for(int i =0;i<t1.getSeats().size();i++)
                                    {
                                        List<Integer> row = t1.getSeats().get(i);
                                        for(int j = 0;j<row.size();j++)
                                        {
                                            if(i==cancel.getRow() && j==cancel.getCol())
                                            {
                                                t1.getSeats().get(i).set(j,0);
                                                tt.saveTrainListToFile();
                                                System.out.println("Cancelled ticket with ticket id : "+cancel.getTicketId()+"" +
                                                        "Row : "+i+" column : "+j);
                                            }
                                        }
                                    }

                                }
                            }
                            break;
                        }
                    }
                    if(!flag)
                    {
                        System.out.println("No ticket found");
                        break;
                    }
                    break;
                case 7:
                    System.out.println("Thank you for visiting...");
                    return;
                default:
                    System.out.println("Choose a valid option.");
            }

        }

    }
}