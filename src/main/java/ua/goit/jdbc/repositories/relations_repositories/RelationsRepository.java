package ua.goit.jdbc.repositories.relations_repositories;

public interface RelationsRepository {
    boolean delete(int entityOneId, int entityTwoId);

    boolean create(int entityOneId, int entityTwoId);

    boolean exists(int entityOneId, int entityTwoId);
}
