package uz.zako.service;

import java.util.List;
import java.util.UUID;

public interface SuperService {

    public <T> List findAll();

    public <T> T findById(UUID id);



    public <T> T save(T t);

    public <T> T update(T t);

    public <T> boolean delete(UUID id);

    public <T, R> T getObjectFromPayload(R payload);

    public <T, R> T getPayloadFromObject(R object);

}
