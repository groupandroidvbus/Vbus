package Modules;

import vn.map4d.map4dsdk.maps.LatLng;

import java.util.ArrayList;
import java.util.List;

import Modules.Route;


public interface DirectionFinderListener {
    void onDirectionFinderStart();
    void onDirectionFinderSuccess(List<Route> route);
}
