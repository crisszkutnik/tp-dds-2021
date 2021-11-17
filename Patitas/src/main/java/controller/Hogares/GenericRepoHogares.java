package controller.Hogares;
import domain.Hogares.Hogar;

public interface GenericRepoHogares {
    Hogar getHogarById(String name);
}