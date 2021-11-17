package controller.Hogares;

import domain.Hogares.Hogar;

import java.io.IOException;
import java.util.List;

public interface InterfazAPIHogares {
    List<Hogar> getAPIHogaresOffset(Integer offset) throws IOException;
}
