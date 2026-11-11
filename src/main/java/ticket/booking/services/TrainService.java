package ticket.booking.services;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import ticket.booking.entities.Train;
import ticket.booking.entities.User;

import java.io.File;
import java.io.IOException;
import java.util.*;
import java.util.stream.Collectors;

public class TrainService {
    User u;
    public TrainService() throws IOException{
        trainlist = loadtrains();
    }
    public TrainService(User u)throws IOException
    {
        this.u = u;
        trainlist = loadtrains();
    }

    private final String trains_path ="localDB/trains.json";
    private ObjectMapper objectmapper = new ObjectMapper();
    private Train train;
    private List<Train> trainlist;

    public List<Train> loadtrains() throws IOException {
        File trains = new File(trains_path);
        trainlist =  objectmapper.readValue(trains, new TypeReference<List<Train>>(){});
        return trainlist;
    }

    public List<Train> searchTrains(String s, String d) throws IOException
    {
        trainlist = loadtrains();
        return trainlist.stream().filter(train -> validtrain(train,s,d)).collect(Collectors.toList());
    }

    public boolean validtrain(Train t,String s,String d)
    {
        List<String> stationOrder = t.getStations();
        List<String> stationOrderCopy= new ArrayList<>();

        for(String train: stationOrder)
        {
            stationOrderCopy.add(train.toLowerCase().trim());
        }

        int sourceIndex = stationOrderCopy.indexOf(s.toLowerCase());
        int destinationIndex = stationOrderCopy.indexOf(d.toLowerCase());

        return sourceIndex!=-1 && destinationIndex!=-1 && sourceIndex<destinationIndex;
    }

    public void saveTrainListToFile() throws IOException
    {
        File trainsFile = new File(trains_path);
        objectmapper.writeValue(trainsFile,trainlist);
    }
}

