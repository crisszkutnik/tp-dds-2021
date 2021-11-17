package controller.Repositorios;

public interface GenericRepo<T> {
    void persist(T data);
}
