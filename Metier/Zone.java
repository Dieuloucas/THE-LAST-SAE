package Metier;

import java.util.ArrayList;
import java.util.List;

public class Zone
{
    private String nomArrondissement;
    private List<Sommet> stations;

    public Zone(String nomArrondissement)
    {
        this.nomArrondissement = nomArrondissement;
        this.stations = new ArrayList<>();
    }

    public void ajouterStation(Sommet s)
    {
        this.stations.add(s);
    }

    public List<Sommet> getStations()
    {
        return this.stations;
    }
}
